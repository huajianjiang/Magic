package com.github.huajianjiang.magic.core.aspect.permission;

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

    private RequestPermsAspect() {
    }

    @Pointcut("execution(@magic.annotation.RequirePermission * *(..)) && @annotation(permMetaData)")
    public void method(RequirePermission permMetaData) {
    }

    @Around("method(permMetaData)")
    public Object requestPerms(ProceedingJoinPoint joinPoint,
            RequirePermission permMetaData) throws Throwable
    {
        return new PermProcessor(joinPoint, permMetaData).proceedRequest();
    }

}
