package com.zsf.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * @author zsf; 2019/7/31
 */
public class ToastUtils {
    public static void showToast(final Context context, final String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(final Context context, final String msg, final int length) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            return;
        }
        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, length).show();
            }
        });
    }

    public static void showToast(final Context context, final int resId) {
        showToast(context, context.getString(resId), Toast.LENGTH_SHORT);
    }

    public static void showToast(final Context context, final int resId, final int length) {
        showToast(context, context.getString(resId), length);
    }
}
