package com.bazinga.shine.lock.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author BazingaLyn
 * 描述：固定大小线程池测试
 * 时间  2016年11月9日
 */
public class FixedThreadPoolDemo {
    
    
    public static class SimpleTask implements Runnable {

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + "Thread Id :" +Thread.currentThread().getId());
            try {
                Thread.sleep(4000l);//睡4000毫秒
            } catch (Exception e) {
            }
        }
        
    }
    
//    public static void main(String[] args) {
//        ExecutorService es = Executors.newFixedThreadPool(5);
//        SimpleTask simpleTask = new SimpleTask();
//        for(int i = 0 ; )
//    }

}
