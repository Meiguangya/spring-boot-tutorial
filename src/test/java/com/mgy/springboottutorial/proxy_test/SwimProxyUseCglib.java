package com.mgy.springboottutorial.proxy_test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
public class SwimProxyUseCglib implements MethodInterceptor {

    private final Object target;

    public SwimProxyUseCglib(Object target) {
        this.target = target;
    }

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

        Object result = null;
        try {

            if (method.getName().equals("start")) {
                log.info("戴上泳帽");
            }

            result = proxy.invokeSuper(obj, args);

            if (method.getName().equals("stop")) {
                log.info("休息一下，可以补充一点能量");
            }
        } catch (Exception e) {
            log.error("发生异常了", e);
        } finally {
            log.info("");
        }


        return result;
    }


}
