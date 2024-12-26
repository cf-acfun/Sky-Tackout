package com.sky.config.beanprocessors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class LogMappingInfoRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogMappingInfoRequestMappingHandlerMapping.class);
    private boolean isLogFirst = true;

    @Override
    public void registerMapping(RequestMappingInfo mapping, Object handler, Method method) {
        super.registerMapping(mapping, handler, method);
        logRequestMappingInfo(handler, method, mapping);
    }

    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        super.registerHandlerMethod(handler, method, mapping);
        logRequestMappingInfo(handler, method, mapping);
    }

    private void logRequestMappingInfo(Object handler, Method method, RequestMappingInfo mapping) {
        if (LOGGER.isInfoEnabled()) {
            if (isLogFirst) {
                LOGGER.info("--------------------------------handlerMapping LOG---------------------------------------");
                isLogFirst = false;
            }
            String handlerName = handler instanceof String ? (String) handler : handler.getClass().getName();
            LOGGER.info(mapping + " is mapped to " + handlerName + "." + method.getName() + "()");
        }
    }
}