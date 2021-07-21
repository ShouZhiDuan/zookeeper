package com.example.zk.testmain;


import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;


/**
 * @Auther: ShouZhi@Duan
 * @Description:
 */
public class MyWatcher implements Watcher {

    private ZooKeeper zk=null;
    public MyWatcher() {
        // TODO Auto-generated constructor stub
    }
    public MyWatcher(ZooKeeper zk) {
        this.zk=zk;
    }
    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    @Override
    public void process(WatchedEvent event) {
        String path = event.getPath();
        if(path.equals(null)){
            System.out.println("======连接成功======");
            return;
        }
        Event.KeeperState state = event.getState();
        Event.EventType type = event.getType();
        System.out.println("监听中"+path+"\t"+state+"\t"+type);
        try {
            Stat stat = zk.exists(path, true);
            zk.getData(path, true, stat);
            //zk.getChildren(path, true);//用了参数就可以不断循环监听
        } catch (KeeperException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
