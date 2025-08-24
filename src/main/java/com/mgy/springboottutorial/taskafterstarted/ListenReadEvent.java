package com.mgy.springboottutorial.taskafterstarted;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 语义清晰：明确表示“应用已就绪”
 * 执行时机最晚，所有 Bean 都已初始化
 * 支持异步：@Async + @EventListener
 */
@Component
@Slf4j
public class ListenReadEvent {


    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterSpringBootReady() {

        log.info("应用准备就绪，我要开始操作了");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("执行完毕");
    }


}
