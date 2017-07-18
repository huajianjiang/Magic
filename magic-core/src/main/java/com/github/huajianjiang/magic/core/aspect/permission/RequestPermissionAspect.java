package com.github.huajianjiang.magic.core.aspect.permission;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

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
public class RequestPermissionAspect {

    private static final int REQUEST_CODE = 1;

    @Pointcut("execution(@magic.annotation.RequirePermission * *(..)) && @annotation(perm)")
    public void method(RequirePermission perm) {
    }

    @Around("method(perm)")
    public void requestPermission(ProceedingJoinPoint joinPoint, RequirePermission perm) throws
            Throwable
    {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object target = joinPoint.getTarget();

        String[] perms = perm.value();

        Log.e(signature.getDeclaringType().getSimpleName(), "requestPermission");

        if (target instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) target, perms, REQUEST_CODE);
        } else if (target instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) target).requestPermissions(perms, REQUEST_CODE);
        } else if (target instanceof android.app.Fragment &&
                   Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            ((android.app.Fragment) target).requestPermissions(perms, REQUEST_CODE);
        }

        //joinPoint.proceed(joinPoint.getArgs());
    }
}
