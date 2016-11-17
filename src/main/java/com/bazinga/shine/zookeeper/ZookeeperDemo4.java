package com.bazinga.shine.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 
 * @author BazingaLyncc
 * 描述：创建节点
 * 时间  2016年11月10日
 */
public class ZookeeperDemo4 implements Watcher {
    
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    
    
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper keeper = new ZooKeeper("127.0.0.1", 2000, new ZookeeperDemo4());
        System.out.println(keeper.getState());
        countDownLatch.await();
        keeper.create("/zk-lyncc-per1-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL,new IStringCallBack(),"heheda1");
        System.out.println("asyc");
        Thread.sleep(50000l);
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("Received watched event "+event);
        if(KeeperState.SyncConnected == event.getState()){
            countDownLatch.countDown();
        }
    }
    
    static class IStringCallBack implements AsyncCallback,StringCallback {

        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.println("Create path result " + rc + " "+ path +" " + ctx +" " +name);
        }
        
    }

}
