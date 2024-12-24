package com.sky.config;

import com.sky.config.beanprocessors.HandlerMappingLoggerPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
