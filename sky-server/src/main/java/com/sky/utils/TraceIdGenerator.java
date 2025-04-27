package com.sky.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: QingKong
 * @Date: 2025/4/27
 * @Description:
 */
@Component
public class TraceIdGenerator {

    public String generateTraceId() {
        // 使用 UUID 或其他分布式 ID 算法（如 Snowflake）
        return UUID.randomUUID().toString().replace("-", "");
    }
}
