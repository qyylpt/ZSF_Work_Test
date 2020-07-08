package com.zsf.aop.m_aspectj;

import com.zsf.utils.ZsfLog;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @Author: zsf
 * @Date: 2020-07-06 16:42
 */
public class TestAspectJ {

    @Pointcut("execution(* com.zsf.aop.m_aspectj.AspectJActivity.test(..))")
    public void pointcut(){}

    @Before("pointcut()")
    public void before(JoinPoint point){
        ZsfLog.d(TestAspectJ.class,point.getSignature().getName() + " -- @Before");
    }

    @Around("pointcut")
    public void around(ProceedingJoinPoint joinPoint){
        ZsfLog.d(TestAspectJ.class, joinPoint.getSignature().getName() + " -- @Around before");
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        ZsfLog.d(TestAspectJ.class, joinPoint.getSignature().getName() + " -- @Around after");
    }

    @After("pointcut")
    public void after(JoinPoint point){
        ZsfLog.d(TestAspectJ.class, point.getSignature().getName()  + " -- @After");
    }

    @AfterReturning("pointcut")
    public void afterReturning(JoinPoint point, Object returnValue){
        ZsfLog.d(TestAspectJ.class, point.getSignature().getName() + " -- @AfterReturning");
    }

    @AfterThrowing("pointcut")
    public void afterThrowing(Throwable throwable){
        ZsfLog.d(TestAspectJ.class, "AfterThrowing  Throwable: " + throwable.getMessage());
    }
}
