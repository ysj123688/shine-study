package com.bazinga.shine.singleton;

/**
 * 
 * @author liguolin
 * 描述：双重检查的好处是降低锁的竞争，达到延长加载，但是如果instance变量不加volatile修饰的话，会导致内存重排序
 * 在高并发的时候，会导致instance没有完成初始化就会被返回
 * 
 * 内存重排序的问题就是先分配内存，然后初始化对象，然后设置对象指向刚才分配好初始化对象的地址
 * 但是如果重排序，就会存在先分配内存，然后先将对象指向刚才分配好的内存地址，然后再初始化好对象，问题就是当在高并发的时候，在第二个步骤判断
 * instance==null的时候会返回false，这时候其实instance并没有初始化好，就会报错
 * 时间  2016年11月8日
 */
public class DoubleCheckLocking {
    
    private static Instance instance;
    
    public static Instance getInstance(){
        if(instance == null){
            synchronized (DoubleCheckLocking.class) {
                if(instance == null){
                    instance = new Instance();
                }
            }
        }
        return instance;
    }
    
    private static class Instance {
        
    }

}
