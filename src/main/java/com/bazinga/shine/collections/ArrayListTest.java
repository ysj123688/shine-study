package com.bazinga.shine.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ArrayListTest {
    
    //线程不安全
    private static List<String> list = new ArrayList<String>();
    
    private static List<String> list1 = Collections.synchronizedList(new ArrayList<String>());
    
    public static void main(String[] args) throws InterruptedException {

        int count = 5000;
        
        final CountDownLatch countDownLatch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    list.add(Thread.currentThread().getName());
                    countDownLatch.countDown();
                }
            }).start();

        }
        
        countDownLatch.await();
        
        System.out.println(list.size());
        
        for(String str:list){
            System.out.println(str);
        }

    }

}
