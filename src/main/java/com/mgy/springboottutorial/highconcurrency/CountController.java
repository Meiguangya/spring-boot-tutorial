package com.mgy.springboottutorial.highconcurrency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Slf4j
public class CountController {

    @Autowired
    @Qualifier("countTaskExecutor")
    private Executor executor;

    AtomicInteger count = new AtomicInteger(0);

    @GetMapping("/high/concurrency/countdemo/index")
    public String index(){

        count.incrementAndGet();

        executor.execute(() -> {
            try {
                long random = (long) (Math.random()*1000);
                log.info(Thread.currentThread().getName());
                log.info("模拟处理业务:[{}]",random);
                Thread.sleep(random);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("模拟处理业务");
        });

        return "index";
    }

    @GetMapping("/high/concurrency/countdemo/count")
    public Integer getCount(){
        return count.get();
    }


}
