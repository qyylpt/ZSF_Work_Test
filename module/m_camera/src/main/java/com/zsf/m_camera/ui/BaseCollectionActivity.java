package com.zsf.m_camera.ui;

import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author : zsf
 * @date : 2021/1/26 3:17 PM
 * @desc :
 */
public abstract class BaseCollectionActivity extends AppCompatActivity {

    private BaseFragment fragment;

    /**
     * 获取activity中fragment容器
     * @return
     */
    public abstract int getFragmentContainer();

    /**
     * 刷新样式
     *
     * @return
     */
    public abstract void refreshStyle();

    /**
     * 返回首页
     */
    public abstract void goBackFirstPage();

    public void switchStyle(boolean enable) {
        Window window = getWindow();
        if (enable) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
