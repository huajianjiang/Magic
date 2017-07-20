package com.github.huajianjiang.magic.core.aspect.permission;

import android.util.Log;

import com.github.huajianjiang.magic.core.util.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
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

    @After("method()")
    public void respondPermsAdvice(JoinPoint joinPoint) throws Throwable {
        PermProcessor.proceedResponse(joinPoint);
    }

}

