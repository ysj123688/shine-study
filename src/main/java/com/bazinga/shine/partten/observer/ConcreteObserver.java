package com.bazinga.shine.partten.observer;

public class ConcreteObserver implements Observer {

    @Override
    public void update(Subject subject) {
        if(subject instanceof ConcreteSubject){
            Integer state = ((ConcreteSubject)subject).getTargetState();
            System.out.println(state);
        }
        
    }

}
