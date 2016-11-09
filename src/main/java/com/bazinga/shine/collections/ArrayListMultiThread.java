package com.bazinga.shine.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author liguolin
 * 描述：线程不安全的ArrayList
 * 时间  2016年11月8日
 */
public class ArrayListMultiThread {
    
    public static List<Integer> list = new ArrayList<Integer>();
    
    public static class AddThread implements Runnable {

        @Override
        public void run() {
            for(int i = 0;i<10000;i++){
                list.add(i);
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new AddThread());
        Thread thread2 = new Thread(new AddThread());
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(list.size());
    }

}
