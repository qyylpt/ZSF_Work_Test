package com.zsf.device.manager.api;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.zsf.device.manager.receiver.LockReceiver;
import com.zsf.global.GlobalData;

/**
 * @author zsf
 * @date 2019/8/15
 * 设备管理器激活,锁定设备...
 */
public class ApiLockScreen {
    private DevicePolicyManager policyManager;
    private ComponentName componentName;
    private Activity activity;
    private static ApiLockScreen apiLockScreen;

    public ApiLockScreen(Activity activity) {
        this.activity = activity;
        policyManager = (DevicePolicyManager) GlobalData.getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(GlobalData.getContext(), LockReceiver .class);
    }

    public static ApiLockScreen getInstance(Activity activity){
        if (apiLockScreen == null){
            synchronized (ApiLockScreen.class){
                if (apiLockScreen == null){
                    apiLockScreen = new ApiLockScreen(activity);
                    return apiLockScreen;
                }
            }
        }
        return apiLockScreen;
    }

    /**
     * 激活设备
     */
    public void activeManager(){
        //使用隐式意图调用系统方法来激活指定的设备管理器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "一键锁屏");
        activity.startActivity(intent);
    }

    /**
     * 锁定设备
     */
    public void lockDevice(){
        policyManager.lockNow();
    }

    /**
     * 是否有锁定权限（设备管理器是否激活）
     * @return
     */
    public boolean isAdminActive(){
        return policyManager.isAdminActive(componentName);
    }
}
