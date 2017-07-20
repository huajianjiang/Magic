package com.github.huajianjiang.magic.core.aspect.permission;

import org.aspectj.lang.ProceedingJoinPoint;

import magic.annotation.RequirePermission;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/20
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class PendingTask {
    private int requestCode;
    private ProceedingJoinPoint mRequestJoinPoint;

}
