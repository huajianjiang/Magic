package com.github.huajianjiang.magic.core.aspect.permission;

import android.app.Activity;

import com.github.huajianjiang.magic.core.module.RuntimePermissionModule;
import com.github.huajianjiang.magic.core.util.Logger;
import com.github.huajianjiang.magic.core.util.Perms;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import magic.annotation.RequirePermission;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
@Aspect
public class RequestPermsAspect {

    @Pointcut("execution(@magic.annotation.RequirePermission * *(..)) && @annotation(permMetaData)")
    public void method(RequirePermission permMetaData) {
    }

    @Around("method(permMetaData)")
    public Object requestPermsAdvice(ProceedingJoinPoint joinPoint, RequirePermission permMetaData)
            throws Throwable
    {
        return PermProcessor.get(joinPoint, permMetaData).proceedRequest();
    }

}
