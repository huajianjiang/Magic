package com.github.huajianjiang.magic.core.module;

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
    void showRequestPermissionsRationale(String[] permissions, PermProcessor processor);

    /**
     * @param permissions
     */
    void onRequestPermissionsGranted(int requestCode, String[] permissions);

    /**
     * @param permissions
     */
    void onRequestPermissionsDenied(String[] permissions);
}
