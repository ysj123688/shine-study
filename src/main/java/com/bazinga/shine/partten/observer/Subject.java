package com.bazinga.shine.partten.observer;

/**
 * 
 * @author liguolin
 * 描述：抽象目标对象
 * 时间  2016年7月5日
 */
public interface Subject {
    
    void registerObserver(Observer observer);
    
    void unregisterObserver(Observer observer);
    
    void notifyObserver();

}
