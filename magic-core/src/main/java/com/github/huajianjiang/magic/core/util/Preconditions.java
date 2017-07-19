package com.github.huajianjiang.magic.core.util;

import android.os.Bundle;

import java.util.Collection;
import java.util.Map;

/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/3/28
 * <br>Email: developer.huajianjiang@gmail.com
 */

public class Preconditions {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNullOrEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public static <T> T checkNotNull(T ref, Object expMsg) {
        if (ref == null) throw new NullPointerException(String.valueOf(expMsg));
        return ref;
    }

}
