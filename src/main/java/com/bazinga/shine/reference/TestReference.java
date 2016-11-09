package com.bazinga.shine.reference;

public class TestReference {
    
    public static void main(String[] args) {
        
        int num = 100;
        change(num);
        System.out.println(num);
        Integer num2 = new Integer(124);
        change(num2);
        System.out.println(num2);
        String test = new String("heheda");
        change(test);
        System.out.println(test);
    }
    
    public static void change(Integer num){
        num = 2000;
    }
    
    public static void change(String test){
        test = new String("dddd");
    }

}
