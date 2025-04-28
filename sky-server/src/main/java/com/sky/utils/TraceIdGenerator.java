package com.sky.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: QingKong
 * @Date: 2025/4/27
 * @Description:
 */
@Component
@Slf4j
public class TraceIdGenerator {

    public String generateTraceId() {
        // 使用 UUID 或其他分布式 ID 算法（如 Snowflake）
        return UUID.randomUUID().toString().replace("-", "");
    }
}
