package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Slf4j
public class AutoFillAspectPlus {

    /**
     * 缓存方法对象
     */
    private final Map<String, Method> methodCache = new ConcurrentHashMap<>();

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {

        log.info("开始进行公共字段自动填充Plus版[{}]", joinPoint.toShortString());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];

        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        if (operationType == OperationType.INSERT) {
            setFieldValue(entity, "setCreateTime", now);
            setFieldValue(entity, "setCreateUser", currentId);
            setFieldValue(entity, "setUpdateTime", now);
            setFieldValue(entity, "setUpdateUser", currentId);
            log.info("初始化完毕[{}]", entity);
        } else if (operationType == OperationType.UPDATE) {
            setFieldValue(entity, "setUpdateTime", now);
            setFieldValue(entity, "setUpdateUser", currentId);
            log.info("初始化完毕[{}]", entity);
        }
    }

    /**
     * 设置实体对象中某个字段的值
     * @param entity 实体对象
     * @param methodName 字段名称
     * @param value 字段的值
     */
    private void setFieldValue(Object entity, String methodName, Object value) {
        try {
            Method method = getMethod(entity, methodName, value.getClass());
            log.info("获取到method为[{}]", method.getName());
            method.invoke(entity, value);
        } catch (Exception e) {
            log.error("初始化字段异常: [{}]", methodName, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 查找指定名称和参数类型的方法
     * @param entity 实体
     * @param methodName set方法
     * @param parameterType 参数类型
     * @return 相关set方法
     */
    private Method getMethod(Object entity, String methodName, Class<?> parameterType) {

        // 获得方法的名称,使用 类名.方法名 作为key
        String key = entity.getClass().getName() + "." + methodName;
        log.info("获取当前key[{}]的值", key);
        /*
                computeIfAbsent 方法是 ConcurrentHashMap 的一个方法，它接受一个键和一个函数。
            如果缓存中已经存在这个键，它会返回缓存中的值；如果不存在，它会执行提供的函数，将结果存储在缓存中，
            并返回这个结果。确保方法对象的查找和缓存操作是线程安全的。
         */
        return methodCache.computeIfAbsent(key, k -> {
            try {
                return entity.getClass().getDeclaredMethod(methodName, parameterType);
            } catch (NoSuchMethodException e) {
                log.error("方法未找到: [{}]", methodName, e);
                throw new RuntimeException(e);
            }
        });
    }

}
