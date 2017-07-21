package com.github.huajianjiang.magic.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;



/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/19
 * <br>Email: developer.huajianjiang@gmail.com
 */
public final class Perms {
    private static final int REQUEST_PERM = 1;

    public static Activity getContext(Object obj) {
        if (obj instanceof Activity) {
            return ((Activity) obj);
        } else if (obj instanceof android.support.v4.app.Fragment) {
            return ((android.support.v4.app.Fragment) obj).getActivity();
        } else if (obj instanceof android.app.Fragment &&
                   Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            return ((android.app.Fragment) obj).getActivity();
        } else {
            throw new RuntimeException(
                    "Can not find Context for " + obj.getClass().getSimpleName());
        }
    }

    public static void requestPermissions(Object context, String[] permissions) {
        //检查权限请求所在的上下文环境
        if (context instanceof Activity) {
            // 权限请求所在上下文为 Framework 中的 Activity 或者 supportLibrary 中的 Activity
            // (FragmentActivity/AppCompatActivity)
            ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_PERM);
        } else if (context instanceof android.support.v4.app.Fragment) {
            // supportLibrary 中的 Fragment
            ((android.support.v4.app.Fragment) context)
                    .requestPermissions(permissions, REQUEST_PERM);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Framework 中的 Fragment
            ((android.app.Fragment) context).requestPermissions(permissions, REQUEST_PERM);
        } else {
            throw new RuntimeException("Can not find correct context for permissions request");
        }
    }


    /**
     * 验证请求的权限是否都已被用户授予
     *
     * @param context     权限请求所在上下文
     * @param permissions 所有需要验证的权限名称
     * @return 如果之前都被授予就返回 true，如果其中一个权限之前被拒,就返回 false
     */
    public static boolean verifyAllPermissions(@NonNull Context context, @NonNull String[] permissions)
    {
        if (permissions.length < 1) {
            return true;
        }
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(context, perm) !=
                PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }
        return true;
    }

    public static boolean verifyAnyPermissions(@NonNull Context context, @NonNull String[] permissions)
    {
        if (permissions.length < 1) {
            return true;
        }
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(context, perm) ==
                PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
        }
        return false;
    }


    /**
     * 验证之前权限请求后的授予情况结果
     *
     * @param grantResults 所有需要验证的权限名称
     * @return 如果之前请求的权限都被授予了就返回 true，如果其中一个权限之前被拒,则返回 false
     */
    public static boolean verifyAllPermissions(@NonNull int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean verifyAnyPermissions(@NonNull int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

}
