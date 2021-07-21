package com.example.zk.testmain;

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

    private final static String NAME_SPACE = "watch-curator";

    private static CuratorFramework curatorFramework;

    static {
         curatorFramework = CuratorFrameworkFactory.builder().
                connectString(CONNECT_STR)
                .sessionTimeoutMs(5000)
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
    //1、NodeCache       针对当前节点的创建、删除和更新触发事件
    //2、PathChildCache  针对于子节点的创建、删除和更新触发事件
    //3、TreeCache       综合事件(PathChildCache+NodeCache)
    //=======================================================

    /**
     * NodeCache
     * 当前节点监听
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
     */
    @Test
    public void pathChildCache() throws Exception {
        PathChildrenCache nodeCache=new PathChildrenCache(curatorFramework,"/watch",true);
        PathChildrenCacheListener nodeCacheListener = (curatorFramework1,pathChildrenCacheEvent) -> {
            //CONNECTION_RECONNECTED
            //CHILD_REMOVED
            //CHILD_UPDATED
            //CHILD_ADDED
            System.out.println(pathChildrenCacheEvent.getType()+"->"+new String(pathChildrenCacheEvent.getData().getData()));
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start(PathChildrenCache.StartMode.NORMAL);
        System.in.read();
    }

}
