package com.github.huajianjiang.magic.core.aspect;

import com.github.huajianjiang.magic.core.module.LoginModule;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/19
 * <br>Email: developer.huajianjiang@gmail.com
 */
@Aspect
public class LoginAspect {

    private LoginAspect() {
    }

    @Pointcut("execution(@magic.annotation.RequireLogin * *(..))")
    public void method() {
    }

    @Around("method()")
    public Object loginAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean shouldIntercept = false;
        Object target = joinPoint.getTarget();

        if (target instanceof LoginModule) {
            LoginModule module = (LoginModule) target;
            shouldIntercept = !module.checkLogin();
        }

        if (shouldIntercept) {
            return null;
        }

        return joinPoint.proceed();
    }

}
