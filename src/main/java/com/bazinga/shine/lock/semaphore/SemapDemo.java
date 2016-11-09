package com.bazinga.shine.lock.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemapDemo implements Runnable {

    final static Semaphore semaphore = new Semaphore(5);
    
    
    @Override
    public void run() {
        try {
            semaphore.acquire();
            Thread.sleep(2000l);
            System.out.println(Thread.currentThread().getId()+"  in...");
            semaphore.release();
        } catch (Exception e) {
        }
    }
    
    
    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(20);
        
        for(int i = 0;i<20;i++){
            SemapDemo demo = new SemapDemo();
            exec.submit(demo);
        }
    }
    
    

}
