package com.github.huajianjiang.magic.core.module;

import android.support.annotation.NonNull;

import com.github.huajianjiang.magic.core.aspect.permission.PermProcessor;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public interface RuntimePermissionModule {
    /**
     * @param permissions
     * @param processor
     */
    void showRequestPermissionsRationale(@NonNull String[] permissions, PermProcessor processor);

    /**
     * @param permissions
     */
    void onRequestPermissionsGranted(int requestCode, @NonNull String[] permissions);

    /**
     * @param permissions
     */
    void onRequestPermissionsDenied(@NonNull String[] permissions);
}
