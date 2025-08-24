package com.mgy.springboottutorial.taskafterstarted;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

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

    private final DataSource dataSource;

    public UseCommandLineRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("使用CommandLineRunner执行在项目启动完成后的逻辑,args:{}", (Object) args);

        System.out.println("===================================");
        System.out.println("DataSource class: " + dataSource.getClass().getName());
        System.out.println("DataSource package: " + dataSource.getClass().getPackageName());
        System.out.println("DataSource class loader: " + dataSource.getClass().getClassLoader());
        System.out.println("DataSource interface: " + DataSource.class);
        // 关键：打印类的完整类型（看是 javax 还是 jakarta）
        System.out.println("DataSource type: " + dataSource.getClass().getSuperclass());
        System.out.println("===================================");

        Thread.sleep(1000);

        log.info("使用CommandLineRunner执行完成");
    }





}
