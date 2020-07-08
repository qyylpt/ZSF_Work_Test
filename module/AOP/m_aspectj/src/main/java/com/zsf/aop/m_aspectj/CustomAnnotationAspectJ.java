package com.zsf.aop.m_aspectj;

import com.zsf.utils.ZsfLog;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @Author: zsf
 * @Date: 2020-07-09 11:25
 */
@Aspect
public class CustomAnnotationAspectJ {

    /**
     * 自定义注解 切点
     */
    @Pointcut("execution(@com.zsf.aop.m_aspectj.CustomAnnotation * *(..))")
    public void pointcutCustomAnnotation() {}

    @Before("pointcutCustomAnnotation()")
    public void beforeCustomAnnotation(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 通过Method对象得到切点上的注解
        CustomAnnotation annotation = method.getAnnotation(CustomAnnotation.class);
        String value = annotation.value();
        int type = annotation.type();
        ZsfLog.d(TestAspectJ.class,joinPoint.getSignature().getName() + " -- @Before; value = " + value + "; type = " + type);
    }
}
