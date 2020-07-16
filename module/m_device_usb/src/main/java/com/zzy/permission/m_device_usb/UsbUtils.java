package com.zzy.permission.m_device_usb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;

import com.zsf.global.GlobalData;
import com.zsf.utils.Singleton;
import com.zsf.utils.ZsfLog;

import java.util.HashMap;

import androidx.annotation.RequiresApi;

import static android.hardware.usb.UsbManager.ACTION_USB_ACCESSORY_ATTACHED;

/**
 * @Author: zsf
 * @Date: 2020-07-10 16:03
 */
public class UsbUtils {

    private static final Class TAG = UsbUtils.class;

    /**
     * usb设备列表
     */
    private HashMap<String, UsbDevice> deviceList;

    /**
     * USB管理器: 负责管理USB设备的类
     */
    private UsbManager manager;

    /**
     * 代表USB设备的一个接口
     */
    private UsbInterface usbInterface;

    /**
     * usb 状态变化监听
     */
    private UsbAttachedListener usbAttachedListener;

    /**
     * USB 监听广播
     */
    private UsbBroadcastReceiver usbBroadcastReceiver;

    private static final Singleton<UsbUtils> SINGLETON = new Singleton<UsbUtils>() {
        @Override
        protected UsbUtils create() {
            return new UsbUtils();
        }
    };

    public UsbUtils() {
        init();
    }

    public static UsbUtils getInstance(){
        return SINGLETON.get();
    }

    private void init() {
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        usbDeviceStateFilter.addAction(ACTION_USB_ACCESSORY_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        usbBroadcastReceiver = new UsbBroadcastReceiver();
        GlobalData.getContext().registerReceiver(usbBroadcastReceiver, usbDeviceStateFilter);
    }

    /**
     * 设置USB监听
     * @param listener
     */
    public void setListener(UsbAttachedListener listener){
        usbAttachedListener = listener;
    }

    /**
     * 销毁广播、监听
     */
    public void onDestroy(){
        usbAttachedListener = null;
        if (usbBroadcastReceiver != null){
            GlobalData.getContext().unregisterReceiver(usbBroadcastReceiver);
            usbBroadcastReceiver = null;
        }
    }

    /**
     * 获取设备列表信息
     * @return
     */
    public HashMap<String, UsbDevice> getUsbDevice(){
        deviceList = null;
        // 获取USB设备
        manager = (UsbManager) GlobalData.getContext().getSystemService(Context.USB_SERVICE);
        // 获取设备列表
        deviceList = manager.getDeviceList();
        return deviceList;
    }

    /**
     * usb 插入、拔出监听(Android OTG 功能)
     */
    private class UsbBroadcastReceiver extends BroadcastReceiver{

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            switch (action){
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                case UsbManager.ACTION_USB_ACCESSORY_DETACHED:
                    ZsfLog.d(TAG, "USB 拔出了");
                    usbAttachedListener.onDetached();
                    break;
                case ACTION_USB_ACCESSORY_ATTACHED:
                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                    ZsfLog.d(TAG, "USB 插入了");
                    usbAttachedListener.onAttached();
                    break;
                default:
                    break;

            }
        }
    }

    /**
     * USB 依附监听
     */
    public interface UsbAttachedListener{

        /**
         * 连接USB
         */
        void onAttached();

        /**
         * 断开USB连接
         */
        void onDetached();

    }

}
