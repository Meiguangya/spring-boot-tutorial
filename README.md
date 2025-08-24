

###  如何使用Spring Boot Actuator：生产监控与运维


### 源码级启动过程
``` 
public ConfigurableApplicationContext run(String... args) {
    // 1. 创建计时器，用于统计启动耗时
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    // 2. 创建 Spring 应用上下文（根据 webApplicationType 决定是 Servlet 还是 Reactive）
    ConfigurableApplicationContext context = null;

    // 3. 关闭 Java 的 headless 模式（与图形界面相关，一般无需关注）
    configureHeadlessProperty();

    // 4. 获取并启动“运行监听器”（如 EventPublishingRunListener）
    //    它负责发布启动过程中的各种事件（如 starting、started、ready）
    SpringApplicationRunListeners listeners = getRunListeners(args);
    listeners.starting();

    try {
        // 5. 构建应用环境（Environment）
        //    加载 application.properties/yml，命令行参数等
        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
        ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
        
        // 绑定环境到当前 SpringApplication
        bindToSpringApplication(environment);

        // 6. 根据环境决定是否打印 banner（那个启动时的 Spring 图标）
        if (this.bannerMode != Banner.Mode.OFF) {
            printBanner(environment);
        }

        // 7. 创建 Spring 容器（ApplicationContext）
        //    常见实现类：AnnotationConfigServletWebServerApplicationContext
        context = createApplicationContext();

        // 8. 获取异常报告器（用于启动失败时打印错误信息）
        context.setApplicationStartup(applicationStartup);
        prepareContext(context, environment, listeners, applicationArguments, printedBanner);

        // 9. 核心！刷新容器（Spring 的核心流程）
        //    - 执行自动配置（@EnableAutoConfiguration）
        //    - 实例化所有非懒加载的 Bean
        //    - 启动内嵌 Tomcat/Jetty
        refreshContext(context);

        // 10. 刷新完成后的额外处理（如注册 shutdown hook）
        afterRefresh(context, applicationArguments);

        // 11. 启动完成，停止计时
        stopWatch.stop();
        if (this.logStartupInfo) {
            new StartupInfoLogger(this.mainApplicationClass)
                .logStarted(getApplicationLog(), stopWatch);
        }

        // 12. 发布“应用已启动”事件
        listeners.started(context);

        // 13. 调用所有 CommandLineRunner 和 ApplicationRunner
        //     （常用于启动后执行初始化任务）
        callRunners(context, applicationArguments);
    }
    catch (Throwable ex) {
        // 启动失败，报告错误
        handleRunFailure(context, ex, listeners);
        throw new IllegalStateException(ex);
    }

    try {
        // 14. 发布“应用准备就绪”事件（ApplicationReadyEvent）
        //     标志应用可以接收请求了
        listeners.running(context);
    }
    catch (Throwable ex) {
        // 运行中失败
        handleRunFailure(context, ex, null);
        throw new IllegalStateException(ex);
    }

    // 15. 返回应用上下文（可用于获取 Bean）
    return context;
}

```


### Spring Boot 与 GraalVM 原生镜像（Native Image）


### Spring Boot 测试最佳实践

### Spring Boot 与响应式编程（WebFlux）


### 整合JDBC的自动装配
