package com.mgy.springboottutorial.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
public class ThreadPoolConfig {

    @Bean("countTaskExecutor")
    public Executor countTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心配置
        executor.setThreadNamePrefix("count-index-task-");
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(20000);
        executor.setKeepAliveSeconds(60);

        // 设置拒绝策略
        // executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 启动线程池
        log.info("✅ 正在初始化线程池...");
        executor.initialize(); // ✅ 确保这行执行了
        log.info("✅ 线程池已初始化，线程名前缀: {}", executor.getThreadNamePrefix());

        return executor;
    }

}
