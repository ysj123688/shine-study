package com.bazinga.shine.singleton;

/**
 * 
 * @author liguolin
 * 描述： 延迟加载的原因就是静态内部类不会随着外部类的初始化而初始化
 * 安全的原因也是因为初始化全部结束之后才会返回，不管你是否发生了重排序
 * 时间  2016年11月8日
 */
public class InstanceFactory {
    
    private static class InstanceHolder {
        public static Instance instance = new Instance();
    }
    
    public static Instance getInstance(){
        return InstanceHolder.instance;
    }
    
    private static class Instance {
        
    }

}
