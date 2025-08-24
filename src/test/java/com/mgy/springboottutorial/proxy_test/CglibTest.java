package com.mgy.springboottutorial.proxy_test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class CglibTest {


    @Test
    public void cookTest() {

        CookService cookService = new CookService();

        CookServiceProxyUseCglib proxy = new CookServiceProxyUseCglib(cookService);

        CookService cookServiceProxy = (CookService) proxy.getProxy();

        // 有返回值的代理
        String noodles = cookServiceProxy.cookNoodles("牛肉");
        System.out.println(noodles);

        cookServiceProxy.clean();

    }


    @Test
    public void swimTest() throws InterruptedException {


        Swim swim = new Swim();

        SwimProxyUseCglib proxy = new SwimProxyUseCglib(swim);

        Swim swimProxy = (Swim) proxy.getProxy();

        swimProxy.start();

        Thread.sleep(2000);

        swimProxy.stop();

        log.info("swimProxy count:[{}]", swimProxy.getCount().get());
        log.info("swim count:[{}]", swim.getCount().get());

    }

}
