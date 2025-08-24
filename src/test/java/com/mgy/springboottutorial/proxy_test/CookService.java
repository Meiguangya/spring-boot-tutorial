package com.mgy.springboottutorial.proxy_test;

public class CookService {


    public String cookNoodles(String name) {
        return name + "拉面";
    }

    public void clean() {
        System.out.println("打扫卫生");
    }

}
