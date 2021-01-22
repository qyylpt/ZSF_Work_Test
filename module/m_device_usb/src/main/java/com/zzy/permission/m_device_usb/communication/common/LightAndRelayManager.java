package com.zzy.permission.m_device_usb.communication.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.zzy.permission.m_device_usb.communication.vendor.LightRelayManagerForRzg;
import com.zzy.permission.m_device_usb.communication.vendor.LightRelayManagerForYjd;

import java.security.InvalidParameterException;

/**
 * @author : zsf
 * @date : 2020/12/23 4:11 PM
 * @desc :
 */
public class LightAndRelayManager {

    private ScanDeviceHandler scanDeviceHandler;

    protected LightAndRelayBuild lightAndRelayBuild;

    protected LightAndRelayManager(LightAndRelayBuild lightAndRelayBuild) {
        this.lightAndRelayBuild = lightAndRelayBuild;
        scanDeviceHandler = new ScanDeviceHandler(Looper.getMainLooper());
    }

    private class ScanDeviceHandler extends Handler {

        private ScanDeviceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LightAndRelayConstant.CLOSE_LIGHT) {
                resetLight();
                lightAndRelayBuild.scannerListener.closeLight();
            }
            if (msg.what == LightAndRelayConstant.CLOSE_RELAY) {
                operateRelay(false);
                lightAndRelayBuild.scannerListener.closeRelay();
            }
        }
    }

    /**
     * 继电器控制(需要子类重写,子类执行之后调用super)
     * @param isOpen 1 : 开启、 0 : 关闭
     */
    public void operateRelay(boolean isOpen) {
        if (scanDeviceHandler != null && isOpen) {
            scanDeviceHandler.removeMessages(LightAndRelayConstant.CLOSE_RELAY);
            scanDeviceHandler.sendEmptyMessageDelayed(LightAndRelayConstant.CLOSE_RELAY, lightAndRelayBuild.delayCloseRelayTime);
        }
    }

    /**
     * 灯光控制(需要子类重写,子类执行之后调用super)
     * @param color 灯管颜色 0 : 绿、1 : 红、4 : 白
     * @param isOpen 1 : 开启、 0 : 关闭
     */
    public void operateLight(int color, int isOpen) {
        if (scanDeviceHandler != null && isOpen == 1) {
            scanDeviceHandler.removeMessages(LightAndRelayConstant.CLOSE_LIGHT);
            scanDeviceHandler.sendEmptyMessageDelayed(LightAndRelayConstant.CLOSE_LIGHT, lightAndRelayBuild.delayCloseLightTime);
        }
    }

    /**
     * 灯光重置(需要子类重写,子类执行之后调用super)
     */
    protected void resetLight(){
    }


    /**
     * 释放资源
     */
    public void release() {
        if (scanDeviceHandler != null) {
            scanDeviceHandler.removeCallbacksAndMessages(null);
            scanDeviceHandler = null;
        }
        resetLight();
        operateRelay(false);
        lightAndRelayBuild.scannerListener = null;
    }

    public static class LightAndRelayBuild {
        /**
         * 默认关闭灯管时间
         */
        public int delayCloseLightTime;

        /**
         * 默认关闭继电器时间
         */
        public int delayCloseRelayTime;

        public ScannerListener scannerListener;

        public final Context context;

        public LightAndRelayBuild(Context context) {
            this.context = context;
        }

        /**
         * 设置自定义延时关闭灯管时间
         * @param delayCloseLightTime 毫秒
         */
        public LightAndRelayBuild setDelayCloseLightTime(int delayCloseLightTime) {
            this.delayCloseLightTime = delayCloseLightTime;
            return this;
        }


        /**
         * 设置自定义延时关闭继电器时间
         * @param delayCloseRelayTime 毫秒
         */
        public LightAndRelayBuild setDelayCloseRelayTime(int delayCloseRelayTime) {
            this.delayCloseRelayTime = delayCloseRelayTime;
            return this;
        }

        public LightAndRelayBuild setScannerListener(ScannerListener scannerListener) {
            this.scannerListener = scannerListener;
            return this;
        }

        public LightAndRelayManager build() throws InvalidParameterException{
            if (delayCloseLightTime <= 0 || delayCloseRelayTime <= 0 || context == null || scannerListener == null) {
                throw new InvalidParameterException("LightAndRelayManager params set not right,please check prams");
            }
            String brand = android.os.Build.BRAND;
            if (DeviceTypeConstant.PAD_RUI_ZHI_GUO.equals(brand)) {
                return new LightRelayManagerForRzg(this);
            }
            return new LightRelayManagerForYjd(this);
        }
    }

    public static class LightAndRelayConstant {
        /**
         * 默认关闭灯管时间
         */
        public static final int LIGHT_DELAY_CLOSE_TIME = 2000;

        /**
         * 默认关闭继电器时间
         */
        public static final int RELAY_DELAY_CLOSE_TIME = 2000;

        /**
         * 关闭灯光【消息】
         */
        public static final int CLOSE_LIGHT = 1;

        /**
         *关闭继电器【消息】
         */
        public static final int CLOSE_RELAY = 2;
    }
}
