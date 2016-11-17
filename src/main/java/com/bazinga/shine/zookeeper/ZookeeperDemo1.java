package com.bazinga.shine.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 
 * @author BazingaLyncc
 * 描述：连接
 * 时间  2016年11月10日
 */
public class ZookeeperDemo1 implements Watcher {
    
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    
    
    public static void main(String[] args) throws IOException {
        ZooKeeper keeper = new ZooKeeper("127.0.0.1", 2000, new ZookeeperDemo1());
        System.out.println(keeper.getState());
        try {
            countDownLatch.await();
        } catch (Exception e) {
        }
        System.out.println(keeper.getState());
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("Received watched event "+event);
        if(KeeperState.SyncConnected == event.getState()){
            countDownLatch.countDown();
        }
    }

}
