package com.github.huajianjiang.magic.core.module;

import android.support.annotation.NonNull;

import com.github.huajianjiang.magic.core.aspect.permission.PermissionHandler;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public interface RuntimePermissionModule {
    /**
     * @param permissions
     * @param handler
     */
    void showRequestPermissionsRationale(@NonNull String[] permissions, PermissionHandler handler);

    /**
     * @param requestCode
     * @param grantedPermissions
     * @param deniedPermissions
     */
    void onRequestPermissionsGranted(int requestCode, @NonNull String[] grantedPermissions,
            @NonNull String[] deniedPermissions);

    /**
     * @param grantedPermissions
     * @param deniedPermissions
     */
    void onRequestPermissionsDenied(@NonNull String[] grantedPermissions,
            @NonNull String[] deniedPermissions);
}
