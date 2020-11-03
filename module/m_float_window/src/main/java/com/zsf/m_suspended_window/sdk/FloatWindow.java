package com.zsf.m_suspended_window.sdk;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zsf.m_suspended_window.R;

import java.lang.reflect.Method;

/**
 * @Author: zsf
 * @Date: 2020-07-21 10:23
 */
public class FloatWindow implements View.OnTouchListener, View.OnClickListener {

    private WindowManager windowManager;

    private WindowManager.LayoutParams layoutParams;

    private Context mContext;

    private WindowContentView windowContentView;

    private int mLastY, mLastX;

    private int screenWidth = 0, screenHeight = 0;

    /**
     * 悬浮窗点击回调
     */
    private ClickFloatWindowListener clickFloatWindowListener;

    public FloatWindow(Context context) {
        this.mContext = context;
    }

    /**
     * 设置悬浮窗点击事件
     *
     * @param clickFloatWindowListener
     */
    public void setClickFloatWindowListener(ClickFloatWindowListener clickFloatWindowListener) {
        this.clickFloatWindowListener = clickFloatWindowListener;
    }

    /**
     * 自定义悬浮窗内容,默认 {@link DefaultWindowContentView}.
     *
     * @param view
     */
    public void setWindowContentView(WindowContentView view) {
        windowContentView = view;
    }

    /**
     * 打开悬浮窗
     */
    public View createWindow() {
        if (!canDrawOverlayViews()) {
            return null;
        }
        if (windowContentView == null) {
            windowContentView = new DefaultWindowContentView(mContext);
        }
        windowContentView.setOnTouchListener(this);
        windowContentView.setOnClickListener(this);
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            //获取屏幕宽高
            Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            screenHeight = point.y;
            screenWidth = point.x;
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            // 设置type
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            // 设置flag
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
            layoutParams.gravity = Gravity.START | Gravity.TOP;
            //背景设置成透明
            layoutParams.format = PixelFormat.TRANSPARENT;
            layoutParams.x = screenWidth - mContext.getResources().getDimensionPixelSize(R.dimen.window_icon_width);
            layoutParams.y = screenHeight / 2;
            //将View添加到屏幕上
            windowManager.addView(windowContentView, layoutParams);
        }
        return windowContentView;
    }

    /**
     * 更新window
     */
    private void updateWindowLayout() {
        if (windowManager != null && layoutParams != null) {
            windowManager.updateViewLayout(windowContentView, layoutParams);
        }
    }

    /**
     * 销毁悬浮窗
     */
    public void destroyWindow() {
        if (windowManager != null && windowContentView != null) {
            windowManager.removeView(windowContentView);
            windowContentView = null;
        }

        if (clickFloatWindowListener != null) {
            clickFloatWindowListener = null;
        }
    }

    /**
     * 隐藏悬浮窗
     */
    public void hiddenWindow(){
        if (windowContentView != null){
            windowContentView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示悬浮窗
     */
    public void showWindow() {
        if (windowContentView != null) {
            windowContentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int mInScreenX = (int) event.getRawX();
        int mInScreenY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                windowContentView.doMove();
                mLastX = (int) event.getRawX();
                mLastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                layoutParams.x += mInScreenX - mLastX;
                layoutParams.y += mInScreenY - mLastY;
                mLastX = mInScreenX;
                mLastY = mInScreenY;
                updateWindowLayout();
                break;
            case MotionEvent.ACTION_UP:
                if (mLastX >= screenWidth/2){
                    layoutParams.x = screenWidth - mContext.getResources().getDimensionPixelSize(R.dimen.window_icon_width);
                    windowContentView.showRight();
                }
                if (mLastX < screenWidth/2){
                    layoutParams.x = 0;
                    windowContentView.showLeft();
                }
                updateWindowLayout();
                break;
            default:
        }
        return false;
    }

    /**
     * 是否存在桌面悬浮窗权限
     * @return
     */
    @SuppressLint("NewApi")
    public boolean canDrawOverlayViews(){
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP){return true;}
        try {
            return Settings.canDrawOverlays(mContext);
        }
        catch(NoSuchMethodError e){
            return canDrawOverlaysUsingReflection(mContext);
        }
    }


    private boolean canDrawOverlaysUsingReflection(Context context) {
        try {
            AppOpsManager manager = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                Class clazz = AppOpsManager.class;
                Method dispatchMethod = clazz.getMethod("checkOp", new Class[] { int.class, int.class, String.class });
                int mode = (Integer) dispatchMethod.invoke(manager, new Object[] { 24, Binder.getCallingUid(), context.getApplicationContext().getPackageName() });
                return AppOpsManager.MODE_ALLOWED == mode;
            } else {
                return false;
            }

        } catch (Exception e) {  return false;  }

    }

    @Override
    public void onClick(View v) {
        if (this.clickFloatWindowListener != null) {
            this.clickFloatWindowListener.onClick(v);
        }
    }

    /**
     * 设置桌面悬浮窗权限
     */
    @SuppressLint("InlinedApi")
    public void requestOverlayDrawPermission(){
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mContext.getPackageName()));
        mContext.startActivity(intent);

    }

    /**
     * 触发悬浮按钮回调
     */
    public interface ClickFloatWindowListener {

        /**
         * 点击悬浮窗
         * @param view
         */
        void onClick(View view);
    }
}
