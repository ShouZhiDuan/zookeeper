package com.example.zk.distribution_lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Auther: ShouZhi@Duan
 * @Description: 分布式锁
 */
public class ZookeeperLockDemo {

    private static String CONNECTION_STR = "192.168.10.33:2181,192.168.10.34:2181";

    private static CuratorFramework curatorFramework;

    static {
        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString(CONNECTION_STR)
                .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3))
                //.namespace("myname_space") //工作空间，不同的空间可以存在相同的数据名称
                .build();
        curatorFramework.start();
    }

    public static void main(String[] args) {
        //可重入排他锁
        final InterProcessMutex lock = new InterProcessMutex(curatorFramework,"/locks");
        for(int i=0;i<10;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"->尝试竞争锁");
                try {
                    lock.acquire(); //阻塞竞争锁
                    System.out.println(Thread.currentThread().getName()+"->成功获得了锁");
                    Thread.sleep(400);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        lock.release(); //释放锁
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },"Thread-"+i).start();
        }
    }
}
