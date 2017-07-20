package com.github.huajianjiang.magic.core.aspect.permission;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/20
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class PermResult {
    private String mPermission;
    private boolean mExplain;
    private boolean mGranted;
    private boolean mSetting;

    public String permission() {
        return mPermission;
    }

    void setPermission(String permission) {
        mPermission = permission;
    }

    public boolean granted() {
        return mGranted;
    }

    void setGranted(boolean granted) {
        mGranted = granted;
    }

    public boolean explain() {
        return mExplain;
    }

    void setExplain(boolean explain) {
        mExplain = explain;
    }
}
