package com.lzq.adream.utils;

import android.os.Build;

/**
 * Created by ${廖昭启} on 2017/11/25.
 */

public class StringUtils {
    /**
     * @return   获取手机型号
     */
    public static String getPhoneCode() {

        return Build.MODEL;
    }
}
