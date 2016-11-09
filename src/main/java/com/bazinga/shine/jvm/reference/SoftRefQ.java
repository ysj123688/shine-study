package com.bazinga.shine.jvm.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public class SoftRefQ {
    
    
    static ReferenceQueue<User> softQueue = null;
    
    public static void main(String[] args) {
        Thread thread = new CheckRefQueue();
        thread.setDaemon(true);
        thread.start();
        
        User u = new User(1,"Bazinga");
        softQueue = new ReferenceQueue<SoftRefQ.User>();
        UserSoftReference userSoftReference = new UserSoftReference(u, softQueue);
        
    }
    
    
    public static class UserSoftReference extends SoftReference<User> {

        int uid;
        
        public UserSoftReference(User referent, ReferenceQueue<? super User> q) {
            super(referent, q);
            uid = referent.getId();
        }
        
    }
    
    
    public static class CheckRefQueue extends Thread {
        
        @Override
        public void run() {
            while(true){
                if(softQueue != null){
                    
                    UserSoftReference obj = null;
                    try {
                        obj = (UserSoftReference) softQueue.remove();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    if(obj != null){
                        System.out.println(obj.uid +" is delete");
                    }
                }
            }
        }
    }
    
    
    public static class User {
        
        private int id;
        
        private String name;
        
        public User(int id,String name){
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User [id=" + id + ", name=" + name + "]";
        }
        
    }

}
