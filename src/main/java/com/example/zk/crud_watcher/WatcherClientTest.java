package com.example.zk.crud_watcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;


/**
 * @Auther: ShouZhi@Duan
 * @Description: 事件监听操作
 */
public class WatcherClientTest {

    private static String CONNECT_STR="192.168.10.33:2185";

    private final static String NAME_SPACE = "myRegistry";

    private static CuratorFramework curatorFramework;

    static {
         curatorFramework = CuratorFrameworkFactory.builder().
                connectString(CONNECT_STR)
                .sessionTimeoutMs(5000)
                 //初次间隔1秒，重试3次。
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .namespace(NAME_SPACE)
                .build();
        curatorFramework.start();
    }


    /**
     * 原生watch事件监听方式
     * 一次性监听
     */
    @Test
    public void onceWatch() throws Exception {
        String nodeName = "/dsz";
        Stat stat = curatorFramework.checkExists().forPath(nodeName);
        if(null == stat){
          curatorFramework.create().creatingParentsIfNeeded().forPath(nodeName,"666666".getBytes());
        }
        System.out.println("stat = " + stat);
        Watcher watcher = (watchedEvent) -> {
            //修改监听 WatchedEvent state:SyncConnected type:NodeDataChanged path:/dsz
            //删除监听 WatchedEvent state:SyncConnected type:NodeDeleted path:/dsz
            System.out.println("监听回调数据：" + watchedEvent.toString());
        };
        byte[] bytes = curatorFramework.getData().usingWatcher(watcher).forPath(nodeName);
        System.out.println(new String(bytes));
        System.in.read();
    }

    /**
     * 原生watch事件监听方式
     * 循环监听
     */
    @Test
    public void reWatch() throws Exception {
        MyWatcher watcher = new MyWatcher();
        ZooKeeper zk = new ZooKeeper("192.168.10.33:2185", 5000, watcher);
        watcher.setZk(zk);
        String path = "/test/name";//选择要监听的目录
        /**
         * 监听当前节点
         */
        Stat stat = zk.exists(path, true);
        zk.getData(path,true,stat);

        /**
         * 监听子节点
         */
//        zk.getChildren(path, true);
//        int count=1;
//        while(count<6){
//            zk.setData(path, (count+"").getBytes(), -1);//因为监听器是getChildren的 所以只能监听子节点数目变化
//            //zk.create(path+"/"+count, (count+"").getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//            Thread.sleep(4000);
//            count++;
//        }
//        zk.close();
    }

    //=======================================================
    //1、NodeCache       监听当前节点的创建、删除和更新触发事件
    //2、PathChildCache  监听子节点的创建、删除和更新触发事件
    //3、TreeCache       监听该路径下所有节点
    //=======================================================

    /**
     * NodeCache
     * 当前节点监听
     * 1、不会默认创建数据目录。
     * 2、每次重新启动不会加载历史事件。
     */
    @Test
    public void nodeCache() throws Exception {
        NodeCache nodeCache = new NodeCache(curatorFramework,"/watch-1",false);
        NodeCacheListener nodeCacheListener = () -> {
            System.out.println("======收到节点变更信息======");
            System.out.println(nodeCache.getCurrentData().getPath());
            //System.out.println(nodeCache.getCurrentData().getPath()+"<===>"+new String(nodeCache.getCurrentData().getData()));
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start(true);//初始化的时候获取node的值并且缓存
        System.in.read();
    }


    /**
     * PathChildCache
     * 子节点监听
     * 1、会默认创建当前节点。
     * 2、每次重新启动都会监听/watch下每个子目录最后一次的事件。
     */
    @Test
    public void pathChildCache() throws Exception {
        //false不缓存目录的值。 true缓存目录的值。
        PathChildrenCache nodeCache=new PathChildrenCache(curatorFramework,"/watch",true);
        PathChildrenCacheListener nodeCacheListener = (curatorFramework1,pathChildrenCacheEvent) -> {
        //System.out.println("子节点事件类型：" + pathChildrenCacheEvent.getType());
        //System.out.println("子节点的值：" + new String(pathChildrenCacheEvent.getData().getData()));
        switch (pathChildrenCacheEvent.getType()) {
                case CHILD_ADDED:
                    System.out.println("添加目录 :" + pathChildrenCacheEvent.getData().getPath());
                    System.out.println("添加数据 : " + new String(pathChildrenCacheEvent.getData().getData()));
                    break;
                case CHILD_UPDATED:
                    System.out.println("更新目录 :" + pathChildrenCacheEvent.getData().getPath());
                    System.out.println("更新数据 : " + new String(pathChildrenCacheEvent.getData().getData()));
                    break;
                case CHILD_REMOVED:
                    System.out.println("移除目录 :" + pathChildrenCacheEvent.getData().getPath());
                    System.out.println("移除数据 : " + new String(pathChildrenCacheEvent.getData().getData()));
                    break;
                case CONNECTION_RECONNECTED:
                    System.out.println("======CONNECTION_RECONNECTED======");
                    break;
                case CONNECTION_SUSPENDED:
                    System.out.println("======CONNECTION_SUSPENDED======");
                    break;
                case INITIALIZED:
                    System.out.println("======本地数据缓存成功======");
                    break;
                case CONNECTION_LOST:
                    System.out.println("======CONNECTION_LOST======");
                    break;
                default:
                    System.out.println("======其他类型操作======");
                    break;
            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        //BUILD_INITIAL_CACHE // 同步初始化客户端的 cache，及创建 cache 后，就从服务器端拉入对应的数据
        //NORMAL // 异步初始化 cache
        //POST_INITIALIZED_EVENT // (表示本地数据已缓存成功了)异步初始化，初始化完成触发事件。如果要启用监听，必须使用此参数。
        //nodeCache.start(PathChildrenCache.StartMode.NORMAL);
        nodeCache.start(PathChildrenCache.StartMode.NORMAL);//这个模式上面才会触发case INITIALIZED逻辑
        //nodeCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        System.in.read();
    }

}
