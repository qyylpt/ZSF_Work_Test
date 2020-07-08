package com.zsf.aop.m_aspectj;

import com.zsf.utils.ZsfLog;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @Author: zsf
 * @Date: 2020-07-06 16:42
 *
 * @Use
 * MethodSignature signature = (MethodSignature) joinPoint.getSignature();
 * String name = signature.getName(); // 方法名：test
 * Method method = signature.getMethod(); // 方法：public void com.lqr.androidaopdemo.MainActivity.test(android.view.View)
 * Class returnType = signature.getReturnType(); // 返回值类型：void
 * Class declaringType = signature.getDeclaringType(); // 方法所在类名：MainActivity
 * String[] parameterNames = signature.getParameterNames(); // 参数名：view
 * Class[] parameterTypes = signature.getParameterTypes(); // 参数类型：View
 */
@Aspect
public class TestAspectJ {

    @Pointcut("execution(* com.zsf.aop.m_aspectj.AspectJActivity.testAspectJ(..))")
    public void pointcut(){}

    @Before("pointcut()")
    public void before(JoinPoint point){
        ZsfLog.d(TestAspectJ.class,point.getSignature().getName() + " -- @Before");
    }

    @Around("pointcut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        ZsfLog.d(TestAspectJ.class, joinPoint.getSignature().getName() + " -- @Around before");
        joinPoint.proceed();
        ZsfLog.d(TestAspectJ.class, joinPoint.getSignature().getName() + " -- @Around after");
    }

    @After("pointcut()")
    public void after(JoinPoint point){
        ZsfLog.d(TestAspectJ.class, point.getSignature().getName()  + " -- @After");
    }

    @AfterReturning("pointcut()")
    public void afterReturning(JoinPoint point, Object returnValue){
        ZsfLog.d(TestAspectJ.class, point.getSignature().getName() + " -- @AfterReturning");
    }

    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public void afterThrowing(Throwable ex){
        ZsfLog.d(TestAspectJ.class, "AfterThrowing  Throwable: " + ex.getMessage());
    }
}
