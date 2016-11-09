package com.bazinga.shine.partten.cor;

public abstract class PriceHandler {
    
    protected PriceHandler successor;

    public PriceHandler getSuccessor() {
        return successor;
    }

    public void setSuccessor(PriceHandler successor) {
        this.successor = successor;
    }
    
    public abstract void handlerRequest(float discount);

}
