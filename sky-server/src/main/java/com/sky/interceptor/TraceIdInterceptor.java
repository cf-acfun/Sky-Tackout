package com.sky.interceptor;

import com.sky.utils.TraceIdGenerator;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: QingKong
 * @Date: 2025/4/27
 * @Description: 日志拦截器，生成TransId并赋值给MDC
 * MDC 是日志框架（如 Logback/Log4j2）提供的线程本地变量存储工具，底层基于 ThreadLocal，能直接与日志模板集成。
 */
@Component
public class TraceIdInterceptor implements HandlerInterceptor {

    @Autowired
    private TraceIdGenerator traceIdGenerator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String transId = traceIdGenerator.generateTraceId();
        MDC.put("transId", transId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove("transId");
    }
}
