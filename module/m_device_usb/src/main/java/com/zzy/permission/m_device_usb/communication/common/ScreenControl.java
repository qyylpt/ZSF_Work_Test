package com.zzy.permission.m_device_usb.communication.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.q_zheng.QZhengIFManager;
import com.zsf.utils.ZsfLog;

/**
 * @author : zsf
 * @date : 2020/12/25 11:16 AM
 * @desc :
 */
public class ScreenControl {

    /**
     * 霸屏
     *
     * @param show false : 霸屏 true : 解除
     */
    public static void showOrHideNavigationStatusBar(Context context, boolean show) {
        String brand = android.os.Build.BRAND;
        String model = android.os.Build.MODEL;
        ZsfLog.d(ScreenControl.class, "showOrHideNavigationStatusBar 设备型号 = " + android.os.Build.MODEL + "; 厂商 :" + brand +"; isShow = " + show);
        if (brand.equals(DeviceTypeConstant.PAD_RUI_ZHI_GUO)) {
            return;
        }
        if (!model.equals(DeviceTypeConstant.X6S)) {
            new QZhengIFManager(context).disableStatusBar(!show);
        } else {
            Intent navigationBarIntent = new Intent();
            navigationBarIntent.setAction("android.intent.action.NAVIGATIONBAR");
            navigationBarIntent.putExtra("status", show);
            context.sendBroadcast(navigationBarIntent);

            Intent statusBarIntent = new Intent();
            statusBarIntent.setAction("android.intent.action.STATUSBAR");
            statusBarIntent.putExtra("status", show);
            context.sendBroadcast(statusBarIntent);
        }
    }
}
