package com.zsf.test.branch.phone;

import android.content.Context;
import android.os.Build;
import android.telecom.TelecomManager;

import com.zsf.utils.ZsfLog;

/**
 * @author zsf
 * @date 2019/9/29
 */
public class PhoneUtils {
    public static void endCall(Context context){
        ZsfLog.d(PhoneUtils.class, "Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);

        if ( Build.VERSION.SDK_INT == 29){
            try {
                TelecomManager tm = (TelecomManager)context.getSystemService(Context.TELECOM_SERVICE);
                tm.endCall();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
