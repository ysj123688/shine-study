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
 * 描述：连接复用
 * 时间  2016年11月10日
 */
public class ZookeeperDemo2 implements Watcher {
    
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    
    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper keeper = new ZooKeeper("127.0.0.1", 2000, new ZookeeperDemo2());
        countDownLatch.await();
        System.out.println(keeper.getState());
        long sessionId = keeper.getSessionId();
        byte[] passwd = keeper.getSessionPasswd();
        
        keeper = new ZooKeeper("127.0.0.1", 2000, new ZookeeperDemo2(), 1l, "!23".getBytes());
        System.out.println(keeper.getState());
        keeper = new ZooKeeper("127.0.0.1", 2000, new ZookeeperDemo2(), sessionId, passwd);
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
