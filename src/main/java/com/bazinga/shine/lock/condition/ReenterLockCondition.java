package com.bazinga.shine.lock.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class ReenterLockCondition implements Runnable {
    
    
    public static ReentrantLock lock = new ReentrantLock();
    
    public static Condition condition = lock.newCondition();

    @Override
    public void run() {
        
        try {
            lock.lock();
            condition.await();
            System.out.println("Thread is going on...");
        } catch (Exception e) {
        }finally{
            lock.unlock();
        }
    }
    
    
    public static void main(String[] args) throws InterruptedException {
        ReenterLockCondition rc = new ReenterLockCondition();
        Thread t1 = new Thread(rc);
        t1.start();
        Thread.sleep(2000l);
        lock.lock();
        condition.signal();
        lock.unlock();
    }

}
