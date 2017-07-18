package com.github.huajianjiang.magic.core.aspect.permission;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/19
 * <br>Email: developer.huajianjiang@gmail.com
 */
@Aspect
public class RespondPermissionAspect {

    @Pointcut("execution(public void onRequestPermissionsResult(int,String[],int[])")
    public void method() {
    }

    @Around("method()")
    public void respondPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();

        Log.e(target.getClass().getSimpleName(), Arrays.toString(args));

        joinPoint.proceed();
    }
}

