package com.sky.context;

public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static ThreadLocal<Integer> threadLocalInteger = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static void setCurrentInteger(Integer id) {
        threadLocalInteger.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static Integer getCurrentI() {
        return threadLocalInteger.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
