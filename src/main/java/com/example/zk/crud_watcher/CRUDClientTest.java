package com.example.zk.crud_watcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

/**
 * @Auther: ShouZhi@Duan
 * @Description: 增删改查操作
 */
public class CRUDClientTest {

    private static String CONNECTION_STR = "192.168.10.33:2185";

    private static CuratorFramework curatorFramework;

    static {
        //CuratorFramework curatorFramework= CuratorFrameworkFactory.newClient("")
        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString(CONNECTION_STR)
                .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("my_space") //工作空间，不同的空间可以存在相同的数据名称
                .build();
        curatorFramework.sync();//数据强一致保证
        //ExponentialBackoffRetry
        //RetryOneTime  仅仅只重试一次
        //RetryUntilElapsed
        //RetryNTimes
        curatorFramework.start(); //启动
//        createData(curatorFramework);
//        updateData(curatorFramework);
//        deleteData(curatorFramework);
        //CRUD
//        curatorFramework.create();
//        curatorFramework.setData(); //修改
//        curatorFramework.delete() ;// 删除
//        curatorFramework.getData(); //查询
    }


    public static void main(String[] args) throws Exception {

    }

    private static void createData(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/data/program", "test".getBytes());

    }

    private static void updateData(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.setData().forPath("/data/program", "up".getBytes());

    }

    private static void deleteData(CuratorFramework curatorFramework) throws Exception {
        Stat stat = new Stat();
        //设置stat
        curatorFramework.getData().storingStatIn(stat).forPath("/data/program");
        //乐观锁执行删除
        curatorFramework.delete().withVersion(stat.getVersion()).forPath("/data/program");
    }

    /**
     * 增
     */
    @Test
    public void insert() throws Exception {
        //单级目录创建
        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/2","666666".getBytes());
        //多级目录创建
        //curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/1/2","666666".getBytes());
        //创建临时节点，临时节点只会对最后节点进行移除，父节点不会移除
//        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/2/2-1/2-2","2-1".getBytes());
//        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/2/2-2","2-2".getBytes());
//        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/2/2-3","2-3".getBytes());
        //创建持久有序节点
//        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/1/1", "1".getBytes());
//        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/1/2", "2".getBytes());
//        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/1/3", "3".getBytes());
        //System.in.read();
    }


}
