package com.zsf.device.manager;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.device.manager.api.ApiLockScreen;
import com.zsf.device.manager.receiver.LockReceiver;
import com.zsf.view.activity.BaseActivity;
/**
 * @author zsf
 * @date 2019/8/15
 */
@Route(path = "/m_device_manager/DeviceManagerActivity")
public class DeviceManagerActivity extends BaseActivity {
    private Button buttonLockScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData(Activity activity) {
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_device_manager);
        buttonLockScreen = findViewById(R.id.button_local_screen);
        buttonLockScreen.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_local_screen){
            lockScreen();
        }
    }

    /**
     * 锁屏
     */
    private void lockScreen(){
        // 判断是否有权限（设备管理器是否已经激活）
        if (ApiLockScreen.getInstance(DeviceManagerActivity.this).isAdminActive()){
            // 锁屏
            ApiLockScreen.getInstance(DeviceManagerActivity.this).lockDevice();
            finish();
        } else {
            // 设备激活
            ApiLockScreen.getInstance(DeviceManagerActivity.this).activeManager();
            finish();
        }
    }

}
