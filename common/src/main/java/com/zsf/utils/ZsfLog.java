package com.zsf.utils;

import android.util.Log;

/**
 * @author zsf; 2019/7/31
 */
public class ZsfLog {
    public static final String TAG = "zsf_tag";
    public static void d(Class tClass, String msg){
        Log.e(TAG, tClass.getSimpleName() +": " + msg);
    }
}
