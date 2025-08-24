package com.mgy.springboottutorial.proxy_test;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CookServiceProxyUseCglib implements MethodInterceptor {

    private final Object target; // 目标对象

    public CookServiceProxyUseCglib(Object target) {
        this.target = target;
        System.out.println("this是什么呢"+this);
    }

    // 创建代理对象
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        // 设置父类（要代理的类）
        enhancer.setSuperclass(target.getClass());
        // 设置回调
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("【代理增强】开始执行方法: " + method.getName() + "，参数: " + java.util.Arrays.toString(args));

        // 可以添加前置逻辑（如日志、权限）
        logBefore(method.getName());

        Object result = null;
        try {
            // 调用原始方法（关键！）
            result = proxy.invokeSuper(obj, args);

            // 可以添加后置逻辑（如结果处理）
            logAfter(method.getName(), result);
        } catch (Exception e) {
            // 异常处理
            logException(method.getName(), e);
            throw e;
        } finally {
            // finally 增强（类似 @After）
            logFinally(method.getName());
        }

        return result;
    }

    private void logBefore(String methodName) {
        System.out.println("【前置】即将执行方法: " + methodName);
    }

    private void logAfter(String methodName, Object result) {
        System.out.println("【后置】方法 " + methodName + " 执行完成，结果: " + result);
    }

    private void logException(String methodName, Exception e) {
        System.out.println("【异常】方法 " + methodName + " 抛出异常: " + e.getMessage());
    }

    private void logFinally(String methodName) {
        System.out.println("【最终】方法 " + methodName + " 执行结束（finally）");
    }


}
