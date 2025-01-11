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

/**
 * 自定义切面实现公共处理逻辑
 */
@Slf4j
public class AutoFillAspect {

    /**
     * 定义切入点
     * 1. execution:最长用的 PCD（切入点标识符），用来匹配特定方法的执行，具体参数如下：
     *      execution([修饰符] <返回类型> [全限定类名.]<方法>(<参数>) [异常])
     *      这其中每一部分都可以使用 * 通配符， []表示选输 <>表示必输
     *      类名中使用 .* 表示包中的所有类， ..* 表示当前包与子包中的所有类
     *      参数主要分为以下几种情况：
     *          - () 表示方法五参数
     *          - (..) 表示有任意个参数
     *          - (*) 表示有一个任意类型的参数
     *          - (String) 表示有一个 String 类型的参数
     *          - (String, String) 表示有两个 String 类型的参数
     *
     * 2. 如果类上面带了注解，也可以用以下 PCD
     *      - @target 指定的目标对象带有特定类型的注解
     *      - @args 传入的方法参数带有特定类型的注解
     *      - @annotation 拦截的方法上带有特定类型的注解
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 前置通知
     * @param joinPoint 连接点
     *      JoinPoint 表示程序执行过程中的一个连接点，通常是方法的调用。
     *      JoinPoint 提供了关于当前连接点的详细信息，这些信息可以用于在通知（Advice）中进行各种操作，
     *                  如日志记录、参数检查、性能监控等。
     *
     */
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充[{}]", joinPoint);
        // 获取当前被拦截方法上@AutoFill()中的数据库操作类型
        /*
         * 获取方法的签名对象
         *       getSignature() 是 JoinPoint 接口中的一个方法，用于获取当前连接点的签名。
         *    签名包含了方法的名称、参数类型和返回类型等信息。Signature 接口提供了多种方法，可以用来获取这些详细信息
         *    joinPoint.getSignature() 返回的 Signature 对象可以强制转换为几种类型，具体取决于当前连接点的类型，在Spring AOP 中
         *    最常见的类型是 MethodSignature, MethodSignature 是 Signature 的一个子接口，用于表示方法执行的连接点。
         *    它提供了更多关于方法的详细信息，如参数名称、参数类型、返回类型等。除了该类型还有 ConstructorSignature（构造器连接点）
         *    和 FieldSignature（字段访问连接点）
         *      MethodSignature 常用方法如下：
         *          - String getName()：返回方法的名称。
         *          - Class<?> getDeclaringType()：返回声明该方法的类。
         *          - Class<?> getReturnType()：返回方法的返回类型。
         *          - Class<?>[].getParameterTypes()：返回方法的参数类型数组。
         *          - String[].getParameterNames()：返回方法的参数名称数组。
         *          - Method getMethod()：返回 java.lang.reflect.Method 对象。
         *
         */
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取方法上的注解对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        // 获取数据库操作类型
        OperationType operationType = autoFill.value();

        // 获取被拦截方法的参数（约定实体为第一个参数）
        /*
            joinPoint.getArgs() 方法是 JoinPoint 接口中的一个方法，用于获取当前连接点（通常是方法调用）的参数数组。
            这个方法返回一个 Object[] 类型的数组，其中每个元素对应方法调用的一个参数。
         */
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        // 获取方法的第一个参数及实体对象
        Object entity = args[0];

        // 为参数中的对象的公共属性进行赋值
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        if (operationType == OperationType.INSERT) {
            try {
                // 通过反射获取实体类中的相关set方法
                // 通过反射获取指定名称和参数类型的已声明方法
                Method setCreatTime = entity.getClass().getDeclaredMethod("setCreatTime", LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                // 通过反射为对象属性赋值
                setCreatTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (operationType == OperationType.UPDATE) {
            try {
                // 通过反射获取实体类中的相关set方法
                // 通过反射获取指定名称和参数类型的已声明方法
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                // 通过反射为对象属性赋值
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
                log.info("update操作舒适化完毕[{}]", entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
