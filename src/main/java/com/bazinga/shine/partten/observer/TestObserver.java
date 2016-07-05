package com.bazinga.shine.partten.observer;

public class TestObserver {
    
    public static void main(String[] args) {
        
        ConcreteObserver concreteObserver = new ConcreteObserver();
        ConcreteSubject concreteSubject = new ConcreteSubject();
        concreteSubject.registerObserver(concreteObserver);
     
        concreteSubject.setTargetState(2);
    }

}
