package com.mgy.springboottutorial;

import com.mgy.ChatService;
import com.mgy.springboottutorial.beans.A;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class SpringBootTutorialApplication {

    public static void main2(String[] args) {
        SpringApplication.run(SpringBootTutorialApplication.class, args);
    }


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringBootTutorialApplication.class);
        // 可以在这里进行各种定制
        //app.setBannerMode(Banner.Mode.OFF); // 关闭 banner
        //app.setWebApplicationType(WebApplicationType.NONE); // 非 Web 应用
        //app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));

        ConfigurableApplicationContext context = app.run(args);

        A a = (A) context.getBean("a");
        a.print();

        log.info("=====================");

        // ChatService chatService = context.getBean(ChatService.class);

        // String ret = chatService.question("我想问你一些问题");
        // log.info("ret:{}", ret);

    }


}
