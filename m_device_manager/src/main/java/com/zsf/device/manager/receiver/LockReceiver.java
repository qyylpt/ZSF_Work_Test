package com.zsf.device.manager.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.zsf.utils.ZsfLog;

/**
 * @author zsf
 * @date 2019/8/15
 */
public class LockReceiver extends DeviceAdminReceiver {
    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        ZsfLog.d(LockReceiver.class, "onReceive");
    }

    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        super.onEnabled(context, intent);
        ZsfLog.d(LockReceiver.class, "onEnabled->设备管理器激活");
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        super.onDisabled(context, intent);
        ZsfLog.d(LockReceiver.class, "onDisabled->设备管理器取消激活");
    }
}
