package com.zzy.permission.m_device_usb.communication.api;

import android.content.Context;

import com.zzy.permission.m_device_usb.communication.common.DeviceTypeConstant;
import com.zzy.permission.m_device_usb.communication.common.LightAndRelayManager;
import com.zzy.permission.m_device_usb.communication.common.ScannerListener;
import com.zzy.permission.m_device_usb.communication.common.ScreenControl;
import com.zzy.permission.m_device_usb.communication.common.SoundManager;
import com.zzy.permission.m_device_usb.communication.serialport.SerialPortManager;
import com.zzy.permission.m_device_usb.communication.usb.UsbDevicesManager;

import java.security.InvalidParameterException;

/**
 * @author : zsf
 * @date : 2020/12/24 6:30 PM
 * @desc :
 */
public class ScannerControlApi {

    private final UsbDevicesManager usbDevicesManager;

    private final LightAndRelayManager lightAndRelayManager;

    /**
     * 声音管理
     */
    private final SoundManager mSoundManager;

    private final ScannerBuild scannerBuild;

    private SerialPortManager serialPortManager;

    private ScannerControlApi(ScannerBuild scannerBuild) {
        this.scannerBuild = scannerBuild;
        if (DeviceTypeConstant.X6S.equals(android.os.Build.MODEL) || DeviceTypeConstant.X8.equals(android.os.Build.MODEL)) {
            serialPortManager = new SerialPortManager(scannerBuild.scannerListener);
        }
        usbDevicesManager = new UsbDevicesManager.UsbManagerBuilder(scannerBuild.mContext)
                .setScannerListener(scannerBuild.scannerListener)
                .build();
        lightAndRelayManager = new LightAndRelayManager.LightAndRelayBuild(scannerBuild.mContext)
                .setDelayCloseLightTime(2000)
                .setDelayCloseRelayTime(2000)
                .setScannerListener(scannerBuild.scannerListener)
                .build();
        mSoundManager = new SoundManager(scannerBuild.mContext);
    }

    /**
     * 灯光控制
     *
     * @param color  0 : 绿、1 : 红、4 : 白
     * @param isOpen 1 : 开启、 0 : 关闭
     */
    public void operateLight(int color, int isOpen) {
        lightAndRelayManager.operateLight(color, isOpen);
    }

    /**
     * 继电器控制
     *
     * @param isOpen 1 : 开启、 0 : 关闭
     */
    public void operateRelay(boolean isOpen) {
        lightAndRelayManager.operateRelay(isOpen);
    }

    /**
     * 播放铃音
     *
     * @param playerFileId 提示音文件
     */
    public void startPlayRing(int playerFileId) {
        mSoundManager.startPlayRing(playerFileId);
    }

    /**
     * 自定义播报
     *
     * @param playingInfo 提示音文件
     */
    public void startPlayRing(String playingInfo) {
        mSoundManager.startPlayRing(playingInfo);
    }

    /**
     * 停止播放铃音
     */
    public void stopPlayRing() {
        mSoundManager.stopPlayRing();
    }

    /**
     * 霸屏
     *
     * @param show false : 霸屏 true : 解除
     */
    public void showOrHideNavigationStatusBar(boolean show) {
        ScreenControl.showOrHideNavigationStatusBar(scannerBuild.mContext, show);
    }

    public void release() {
        if (usbDevicesManager != null) {
            usbDevicesManager.release();
        }
        if (lightAndRelayManager != null) {
            lightAndRelayManager.release();
        }
        if (mSoundManager != null) {
            mSoundManager.release();
        }
        if (serialPortManager != null) {
            serialPortManager.release();
            serialPortManager = null;
        }
    }

    public static class ScannerBuild {
        /**
         * 灯管关闭延时时间 默认 2000
         */
        private int delayCloseLightTime = LightAndRelayManager.LightAndRelayConstant.LIGHT_DELAY_CLOSE_TIME;

        /**
         * 继电器关闭延时间 默认 2000
         */
        private int delayCloseRelayTime = LightAndRelayManager.LightAndRelayConstant.RELAY_DELAY_CLOSE_TIME;

        private ScannerListener scannerListener;

        private final Context mContext;

        public ScannerBuild(Context context) {
            this.mContext = context;
        }

        public ScannerBuild setDelayCloseRelayTime(int time) {
            this.delayCloseRelayTime = time;
            return this;
        }

        public ScannerBuild setDelayCloseLightTime(int time) {
            this.delayCloseLightTime = time;
            return this;
        }

        public ScannerBuild setScannerListener(ScannerListener scannerListener) {
            this.scannerListener = scannerListener;
            return this;
        }

        public ScannerControlApi build() throws InvalidParameterException {
            if (delayCloseLightTime <= 0 || delayCloseRelayTime <= 0 || mContext == null || scannerListener == null) {
                throw new InvalidParameterException("params set no right, please check build params");
            }
            return new ScannerControlApi(this);
        }
    }

}
