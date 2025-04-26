package com.sky.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author:
 * @Date: 2025/3/13 15:23
 * @Description:
 */
@Slf4j
public class LogStepUtil {

    public static ThreadLocal<List<String>> logStepThreadLocal = new ThreadLocal<>();

    public static void init() {
        logStepThreadLocal.set(new LinkedList<>());
    }

    public static void log(String log, Object ... args) {
        if (logStepThreadLocal.get() == null) {
            return;
        }
        if (args.length > 0) {
            String s = log.replaceAll("\\{}", "%s");
            log = String.format(s, args);
        }
    }

    public static String getLog() {
        List<String> logs = logStepThreadLocal.get();
        String logStr = JSON.toJSONString(logs);
        logStepThreadLocal.remove();
        return logStr;
    }
}
