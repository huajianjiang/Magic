package com.github.huajianjiang.magic.core.aspect.permission;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.github.huajianjiang.magic.core.util.Preconditions;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.ArrayList;
import java.util.List;

import magic.annotation.RequirePermission;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/19
 * <br>Email: developer.huajianjiang@gmail.com
 */
public final class PermProcessor {
    private static final int REQUEST_PERM = 1;
    private static PermProcessor INSTANCE;

    private RuntimePermissionListener mListener;

    private ProceedingJoinPoint mRequestJoinPoint;
    private RequirePermission mPermMetaData;

    private List<String> mDainedPerms = new ArrayList<>();
    private List<String> mRationalePerms = new ArrayList<>();

    private PermProcessor(ProceedingJoinPoint requestJoinPoint, RequirePermission permMetaData) {
        mRequestJoinPoint = requestJoinPoint;
        mPermMetaData = permMetaData;
    }

    public PermProcessor get() {
        return INSTANCE;
    }

    static PermProcessor get(ProceedingJoinPoint requestJoinPoint) {
        return get(requestJoinPoint, null);
    }

    static PermProcessor get(ProceedingJoinPoint requestJoinPoint, RequirePermission permMetaData)
    {
        if (INSTANCE == null) {
            INSTANCE = new PermProcessor(requestJoinPoint, permMetaData);
        }
        return INSTANCE;
    }


    private static Object proceed() throws Throwable {
//        if (Perms.verifyPermissions(mContext, mPermMetaData.value())) {
//            return mRequestJoinPoint.proceed();
//        }
//        requestPerms();
        return null;
    }

    private void requestPermissions() {
        Object context = mRequestJoinPoint.getTarget();
        String[] perms = mPermMetaData.value();
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
        }
    }

    private void requestPerms() {
        mRationalePerms.clear();
        // TODO
        boolean showRationaleUi = true;

        String[] perms = mPermMetaData.value();

//        if (showRationaleUi) {
//            for (String perm : perms) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, perm)) {
//                    mRationalePerms.add(perm);
//                }
//            }
//        }

        if (!Preconditions.isNullOrEmpty(mRationalePerms)) {
            //TODO

        } else {
            requestPermissions();
        }
    }

    public interface RuntimePermissionListener {
        boolean shouldShowRationale();
    }

}
