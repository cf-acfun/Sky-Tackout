package com.sky.config;

import com.sky.config.beanprocessors.HandlerMappingLoggerPostProcessor;
import com.sky.config.beanprocessors.LogMappingInfoRequestMappingHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @Author: ChenFeng
 * @Date: 2024/12/24 17:09
 * @Description:
 */
@Configuration
public class AppConfig {

    @Bean
    public HandlerMappingLoggerPostProcessor handlerMappingLoggerPostProcessor() {
        return new HandlerMappingLoggerPostProcessor();
    }

    @Bean
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new LogMappingInfoRequestMappingHandlerMapping();
    }
}
