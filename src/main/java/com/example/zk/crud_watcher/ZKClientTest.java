package com.example.zk.crud_watcher;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @Auther: ShouZhi@Duan
 * @Description: 原生zk客户端操作
 */
public class ZKClientTest implements Watcher {

    private static ZooKeeper zk;

    static {
        try {
            zk = new ZooKeeper("192.168.10.33:2185",3000,new ZKClientTest());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void process(WatchedEvent event) {
        System.out.println("======事件类型======" + event.getType());
        try {
            //循环监听
            zk.exists(event.getPath(),true);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * ZK Wather的三种方式
     * 1、getData 获取数据的同时触发监听
     * 2、exists  判断数据是否存在同时触发监听
     * 3、getChildren 获取子节点的同时触发监听
     */
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        String path = "/test_add";
        Stat exists = zk.exists(path, false);
        if(exists == null){
            zk.create(path,"value".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        }else {
            //首次监听
            zk.exists(path, true);
        }
        System.in.read();
    }

}
