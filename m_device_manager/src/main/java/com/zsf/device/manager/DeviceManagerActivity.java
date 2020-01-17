package com.zsf.device.manager;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
//        setContentView(R.layout.activity_device_manager);
//        buttonLockScreen = findViewById(R.id.button_local_screen);
//        buttonLockScreen.setOnClickListener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        /*
         * 将对话框的大小按屏幕大小的百分比设置
         */
        Window window = this.getWindow();
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = 0; // 高度设置为0
        p.width = 0;//宽0
        p.gravity = Gravity.CENTER;
        window.setAttributes(p);
        lockScreen();
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
