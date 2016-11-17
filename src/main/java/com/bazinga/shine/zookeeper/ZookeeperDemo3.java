package com.bazinga.shine.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 
 * @author BazingaLyncc
 * 描述：创建节点
 * 时间  2016年11月10日
 */
public class ZookeeperDemo3 implements Watcher {
    
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    
    
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper keeper = new ZooKeeper("127.0.0.1", 2000, new ZookeeperDemo3());
        System.out.println(keeper.getState());
        countDownLatch.await();
        //临时节点 
        String path1 = keeper.create("/zk-lyncc-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("success create node "+path1);
        //临时顺序节点
        String path2 = keeper.create("/zk-lyncc-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("success create node "+path2);
        String path3 = keeper.create("/zk-lyncc-per-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("success create node "+path3);
        String path4 = keeper.create("/zk-lyncc-per-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println("success create node "+path4);
        
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("Received watched event "+event);
        if(KeeperState.SyncConnected == event.getState()){
            countDownLatch.countDown();
        }
    }

}
