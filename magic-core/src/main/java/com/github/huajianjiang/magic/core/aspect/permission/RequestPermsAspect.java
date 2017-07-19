package com.github.huajianjiang.magic.core.aspect.permission;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import com.github.huajianjiang.magic.core.util.Perms;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import magic.annotation.RequirePermission;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
@Aspect
public class RequestPermsAspect {

    private static final int REQUEST_PERM = 1;

    @Pointcut("execution(@magic.annotation.RequirePermission * *(..)) && @annotation(permMetaData)")
    public void method(RequirePermission permMetaData) {
    }

    @Around("method(permMetaData)")
    public Object requestPermsAdvice(ProceedingJoinPoint joinPoint, RequirePermission permMetaData)
            throws Throwable
    {
        Object target = joinPoint.getTarget();

        Perms.checkContext(target);

        String[] perms = permMetaData.value();

        if (Perms.verifyPermissions(Perms.getContext(target), perms)) {
            return joinPoint.proceed();
        }

        Perms.requestPermissions(target, perms);

        return null;
    }
}
