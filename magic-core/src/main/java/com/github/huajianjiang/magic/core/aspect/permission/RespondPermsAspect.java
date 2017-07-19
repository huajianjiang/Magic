package com.github.huajianjiang.magic.core.aspect.permission;

import android.util.Log;

import com.github.huajianjiang.magic.core.util.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/19
 * <br>Email: developer.huajianjiang@gmail.com
 */
@Aspect
public class RespondPermsAspect {

    @Pointcut("execution(public void onRequestPermissionsResult(int,String[],int[]))")
    public void method() {
    }

    @Around("method()")
    public Object respondPermsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String tag = signature.getDeclaringType().getSimpleName();
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();

        String[] perms = (String[]) args[1];
        for (String perm : perms) {
            Logger.e(tag, perm);
        }

        return joinPoint.proceed();
    }

}

