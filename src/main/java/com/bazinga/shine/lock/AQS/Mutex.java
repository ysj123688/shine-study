package com.bazinga.shine.lock.AQS;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 该代码来自于AbstractQueuedSynchronizer.java 198~253行
 * 
 * AbstractQueuedSynchronizer同步器
 * 三个核心方法
 * getState()获取当前同步状态
 * setState(int newState)设置当前同步状态(一般是已经当前处于什么状态的时候)
 * compareAndSetState(int expect,int update)expect原值，update现值，原子替换 
 */
public class Mutex implements Lock {
    
    
    private static class Sync extends AbstractQueuedSynchronizer {
        
        /**
         * 
         */
        private static final long serialVersionUID = -2304747043082708792L;

        //判断当前同步器是否被线程独占,简单的说就是当前的state是否等于1
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }
        
        //尝试独占获取锁
        @Override
        protected boolean tryAcquire(int acquires) {
            if(compareAndSetState(0, 1)){
                //设置只有当前线程可以到达
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }
        
        @Override
        protected boolean tryRelease(int release) {
            //如果当前的state已经是0(释放了),再次释放会报出异常
            if(getState() == 0)
                throw new IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
        
        Condition newCondition(){return new ConditionObject();}
    }
    
    
    private final Sync sync = new Sync();
    

    public void lock() {
        sync.acquire(1);
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    public void unlock() {
        sync.acquire(1);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }

}
