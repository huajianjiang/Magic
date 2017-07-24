package com.github.huajianjiang.magic.core.aspect.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.github.huajianjiang.magic.core.module.RuntimePermissionModule;
import com.github.huajianjiang.magic.core.util.Logger;
import com.github.huajianjiang.magic.core.util.Perms;
import com.github.huajianjiang.magic.core.util.Preconditions;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.ArrayList;
import java.util.List;

import magic.annotation.RequirePermission;

import static magic.annotation.RequirePermission.ALL;
import static magic.annotation.RequirePermission.ANY;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/19
 * <br>Email: developer.huajianjiang@gmail.com
 */
public final class PermProcessor {
    private ProceedingJoinPoint mRequestJoinPoint;
    private RequirePermission mPermMetaData;

    PermProcessor(ProceedingJoinPoint requestJoinPoint) {
        this(requestJoinPoint, null);
    }

    PermProcessor(ProceedingJoinPoint requestJoinPoint, RequirePermission permMetaData) {
        mRequestJoinPoint = requestJoinPoint;
        mPermMetaData = checkPermMetaData(requestJoinPoint, permMetaData);
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
        return RuntimePermissionModule.class.isInstance(target) ? (RuntimePermissionModule) target :
                null;
    }

    Object proceedRequest() throws Throwable {
        final ProceedingJoinPoint joinPoint = mRequestJoinPoint;
        final RequirePermission permMetaData = mPermMetaData;

        Object target = joinPoint.getTarget();
        Activity context = Perms.getContext(target);
        int limit = permMetaData.limit();
        String[] perms = permMetaData.value();

        boolean ok;

        Logger.e("PermProcessor", "proceedRequest>>>limit===>"+limit);

        switch (limit) {
            case ALL:
                ok = Perms.verifyAllPermissions(context, perms);
                break;
            case ANY:
                ok = Perms.verifyAnyPermissions(context, perms);
                break;
            default:
                return new IllegalArgumentException(
                        "unknown limit , please using RequirePermission.ALL or " +
                        "RequirePermission.ANY");
        }

        if (ok) {
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

            boolean hasShouldExplainPerms = !Preconditions.isNullOrEmpty(shouldExplainPerms);

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
        RuntimePermissionModule module = getModule(responseJoinPoint.getTarget());
        if (module == null) return;
        
        final Object[] args = responseJoinPoint.getArgs();
        if (Preconditions.isNullOrEmpty(args)) return;

        final int prc = (int) args[0];
        final String[] perms = (String[]) args[1];
        final int[] grantResults = (int[]) args[2];
        final int requestCode = getRequestCode(prc);
        final int limit = getLimit(prc);

        boolean ok = false;

        Logger.e("PermProcessor",
                "proceedResponse>>>limit===>" + limit + ",requestCode=" + requestCode);

        switch (limit) {
            case ALL:
                ok = Perms.verifyAllPermissions(grantResults);
                break;
            case ANY:
                ok = Perms.verifyAnyPermissions(grantResults);
                break;
        }
        
        if (ok) {
            module.onRequestPermissionsGranted(requestCode, perms);
        } else {
            List<String> deniedPerms = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPerms.add(perms[i]);
                }
            }
            module.onRequestPermissionsDenied(deniedPerms.toArray(new String[]{}));
        }
    }

    /**
     * 请求运行时权限
     */
    public void requestPermissions() {
        final ProceedingJoinPoint joinPoint = mRequestJoinPoint;
        final RequirePermission permMetaData = mPermMetaData;
        final int prc = getPackagedRequestCode(permMetaData.limit(), permMetaData.requestCode());

        Object context = joinPoint.getTarget();
        String[] perms = permMetaData.value();

        Logger.e("PermProcessor", "requestPermissions>>>prc=" + prc);

        //检查权限请求所在的上下文环境
        if (context instanceof Activity) {
            // 权限请求所在上下文为 Framework 中的 Activity 或者 supportLibrary 中的 Activity
            // (FragmentActivity/AppCompatActivity)
            ActivityCompat.requestPermissions((Activity) context, perms, prc);
        } else if (context instanceof android.support.v4.app.Fragment) {
            // supportLibrary 中的 Fragment
            ((android.support.v4.app.Fragment) context).requestPermissions(perms, prc);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Framework 中的 Fragment
            ((android.app.Fragment) context).requestPermissions(perms, prc);
        } else {
            throw new RuntimeException("Can not find correct context for permissions request");
        }
    }


    private static final int LIMIT_SHIFT = 15;
    private static final int LIMIT_MASK = 1 << LIMIT_SHIFT;

    private static int getPackagedRequestCode(int limit, int requestCode) {
        return (limit & LIMIT_MASK) | (requestCode & ~LIMIT_MASK);
    }

    private static int getLimit(int packagedRequestCode) {
        return packagedRequestCode & LIMIT_MASK;
    }

    private static int getRequestCode(int packagedRequestCode) {
        return packagedRequestCode & ~LIMIT_MASK;
    }

}
