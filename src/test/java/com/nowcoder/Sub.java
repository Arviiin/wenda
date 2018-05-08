package com.nowcoder;

public class Sub extends Base{
    static {
        System.out.println("sub static");
    }
    public Sub(){
        System.out.println("sub constructor");
    }

    public static void main(String[] args) {
        new Sub();
    }
}
class Base{
    static {
        System.out.println("base static");
    }
    public Base(){
        System.out.println("base construsttt");
    }
}
