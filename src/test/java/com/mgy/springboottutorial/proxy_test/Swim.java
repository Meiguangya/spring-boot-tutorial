package com.mgy.springboottutorial.proxy_test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Swim {

    private ReentrantLock lock = new ReentrantLock();

    private Thread thread;

    private AtomicInteger count = new AtomicInteger(0);

    public void start() {

        try {
            lock.lock();

            thread = new Thread(() -> {
                log.info("开始游泳咯");
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        int i = count.incrementAndGet();
                        Thread.sleep(1000);
                        log.info("正在游泳:[{}]", i);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.info("游泳线程被中断，准备停止。");
                        return;
                    }
                }
            });

            thread.start();
        } catch (Exception e) {
            System.out.println("发生异常");
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public void stop() throws InterruptedException {
        log.info("停止游泳");
        if (thread != null) {
            thread.interrupt();
            thread.join(); // 等待线程结束
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Swim swim = new Swim();
        swim.start();

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        swim.stop();

        System.out.println("游了" + swim.count.get() + "秒");
    }


    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }
}
