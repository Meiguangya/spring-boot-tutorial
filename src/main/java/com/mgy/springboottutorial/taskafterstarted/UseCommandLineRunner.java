package com.mgy.springboottutorial.taskafterstarted;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 适合处理命令行参数或简单初始化。
 * 简单直观
 * 可以获取 main 方法的启动参数
 * 支持 @Order 控制顺序
 */
@Component
@Order(20)
@Slf4j
public class UseCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        log.info("使用CommandLineRunner执行在项目启动完成后的逻辑,args:{}", (Object) args);
        Thread.sleep(2000);
        log.info("使用CommandLineRunner执行完成");
    }





}
