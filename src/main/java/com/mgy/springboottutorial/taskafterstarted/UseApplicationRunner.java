package com.mgy.springboottutorial.taskafterstarted;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 和 CommandLineRunner 类似，但参数是结构化的 ApplicationArguments。
 */
@Component
@Order(10)
@Slf4j
public class UseApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("使用ApplicationRunner执行项目启动成功后的操作 args:{}", args);
        Thread.sleep(1000);
        log.info("使用ApplicationRunner执行项目启动成功后的操作完成");
    }

}
