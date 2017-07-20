package com.github.huajianjiang.magic.core.aspect.permission;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;

import com.github.huajianjiang.magic.core.module.RuntimePermissionModule;
import com.github.huajianjiang.magic.core.util.Logger;
import com.github.huajianjiang.magic.core.util.Perms;
import com.github.huajianjiang.magic.core.util.Preconditions;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import magic.annotation.RequirePermission;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/19
 * <br>Email: developer.huajianjiang@gmail.com
 */
public final class PermProcessor {
    private static final AtomicInteger REQUEST_PERM = new AtomicInteger();

    private SparseArray<ProceedingJoinPoint> mRequestJoinPoints = new SparseArray<>();

    private RequirePermission mPermMetaData;

    PermProcessor(ProceedingJoinPoint requestJoinPoint, RequirePermission permMetaData) {
        mRequestJoinPoint = requestJoinPoint;
        permMetaData = checkPermMetaData(requestJoinPoint, permMetaData);
        mPermMetaData = permMetaData;
    }


    private static RequirePermission checkPermMetaData(ProceedingJoinPoint requestJoinPoint,
            RequirePermission permMetaData)
    {
        if (permMetaData == null) {
            MethodSignature signature = (MethodSignature) requestJoinPoint.getSignature();
            permMetaData = signature.getMethod().getAnnotation(RequirePermission.class);
        }
        return permMetaData;
    }

    @Nullable
    private static RuntimePermissionModule getModule(Object target) {
        return RuntimePermissionModule.class.isInstance(target) ? (RuntimePermissionModule) target
                                                                : null;
    }


    Object proceedRequest() throws Throwable {
        ProceedingJoinPoint joinPoint = mRequestJoinPoint;
        RequirePermission permMetaData = mPermMetaData;
        if (joinPoint == null) {
            return null;
        }

        Object target = joinPoint.getTarget();
        Activity context = Perms.getContext(target);
        String[] perms = permMetaData.value();

        if (Perms.verifyPermissions(context, perms)) {
            return joinPoint.proceed();
        }

        boolean shouldExplain = permMetaData.explain();
        if (shouldExplain) {
            List<String> shouldExplainPerms = new ArrayList<>();
            for (String perm : perms) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, perm)) {
                    shouldExplainPerms.add(perm);
                }
            }

            final boolean hasShouldExplainPerms = !Preconditions.isNullOrEmpty(shouldExplainPerms);

            if (hasShouldExplainPerms) {
                RuntimePermissionModule module = getModule(target);
                if (module != null) {
                    module.showRequestPermissionsRationale(
                            shouldExplainPerms.toArray(new String[]{}), PermProcessor.this);
                }
                return null;
            }
        }

        requestPermissions();

        return null;
    }


    static void proceedResponse(JoinPoint responseJoinPoint) throws Throwable {
        PermProcessor permProcessor = INSTANCE;
        if (permProcessor == null) {
            return;
        }

        Object[] args = responseJoinPoint.getArgs();
        if (Preconditions.isNullOrEmpty(args)) return;
        final int[] grantResults = (int[]) args[2];
        final boolean allGranted = Perms.verifyPermissions(grantResults);

        if (allGranted) {
            ProceedingJoinPoint requestJoinPoint = permProcessor.mRequestJoinPoint;
            if (requestJoinPoint != null) {
                requestJoinPoint.proceed();
            }
        } else {
            RuntimePermissionModule module = getModule(responseJoinPoint.getTarget());
            if (module != null) {
                String[] perms = (String[]) args[1];
                List<String> deniedPerms = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        deniedPerms.add(perms[i]);
                    }
                }
                module.onRequestPermissionsDenied(deniedPerms.toArray(new String[]{}),
                                                  permProcessor);
            }
        }
    }

    public void requestPermissions() {
        ProceedingJoinPoint joinPoint = mRequestJoinPoint;
        RequirePermission permMetaData = mPermMetaData;
        if (joinPoint == null) {
            Logger.e("MainActivity", "requestPermissions failed because of GC");
            return;
        }

        Object context = joinPoint.getTarget();
        String[] perms = permMetaData.value();

        //检查权限请求所在的上下文环境
        if (context instanceof Activity) {
            // 权限请求所在上下文为 Framework 中的 Activity 或者 supportLibrary 中的 Activity
            // (FragmentActivity/AppCompatActivity)
            ActivityCompat.requestPermissions((Activity) context, perms, REQUEST_PERM);
        } else if (context instanceof android.support.v4.app.Fragment) {
            // supportLibrary 中的 Fragment
            ((android.support.v4.app.Fragment) context).requestPermissions(perms, REQUEST_PERM);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Framework 中的 Fragment
            ((android.app.Fragment) context).requestPermissions(perms, REQUEST_PERM);
        } else {
            throw new RuntimeException("Can not find correct context for permissions request");
        }
    }

}
