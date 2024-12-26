package com.sky.config.beanprocessors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @Author: ChenFeng
 * @Date: 2024/12/24 17:05
 * @Description:
 */
@Slf4j
public class HandlerMappingLoggerPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof HandlerMapping) {
            log.info("拦截器注册开始[{}]", bean);
            if (bean instanceof RequestMappingHandlerMapping) {
                log.info("开始注册RequestMappingHandlerMapping类");
            }
        }
        return bean;
    }
}
