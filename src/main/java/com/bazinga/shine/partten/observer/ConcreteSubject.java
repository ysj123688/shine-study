package com.bazinga.shine.partten.observer;

import java.util.ArrayList;
import java.util.List;

public class ConcreteSubject implements Subject {
    
    private List<Observer> observers = new ArrayList<Observer>();
    
    private Integer targetState;

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver() {

        if(observers.size() > 0){
            for(Observer observer:observers){
                observer.update(this);
            }
        }
    }

    public Integer getTargetState() {
        return targetState;
    }

    public void setTargetState(Integer targetState) {
        this.targetState = targetState;
        this.notifyObserver();
    }
    
    

}
