package com.zzy.permission.m_device_usb.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.zsf.utils.ToastUtils;
import com.zsf.utils.ZsfLog;

import java.security.InvalidParameterException;

import static android.hardware.usb.UsbManager.ACTION_USB_ACCESSORY_ATTACHED;

/**
 * @author : zsf
 * @date : 2020/12/14 4:56 PM
 * @desc :
 */
public class UsbCommunicationManager {

    private UsbDevicesBroadcastReceiver usbDevicesBroadcastReceiver;

    private Context context;

    private CommunicationListener communicationListener;

    private UsbCommunicationManager() {}

    private UsbCommunicationManager(UsbManagerBuilder usbManagerBuilder) {
        this.context = usbManagerBuilder.context;
        this.communicationListener = usbManagerBuilder.communicationListener;
        if (usbDevicesBroadcastReceiver == null) {
            usbDevicesBroadcastReceiver = new UsbDevicesBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            intentFilter.addAction(ACTION_USB_ACCESSORY_ATTACHED);
            intentFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
            this.context.registerReceiver(usbDevicesBroadcastReceiver, intentFilter);
        }
    }

    public void release() {
        if (usbDevicesBroadcastReceiver != null) {
            this.context.unregisterReceiver(usbDevicesBroadcastReceiver);
            usbDevicesBroadcastReceiver = null;
        }
    }

    private class UsbDevicesBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ToastUtils.showToast(context, "action = " + action);
            ZsfLog.d(UsbCommunicationManager.class, "action = " + action);
            switch (action) {
                case UsbManager.ACTION_USB_ACCESSORY_ATTACHED:
                case UsbManager.ACTION_USB_DEVICE_ATTACHED :
                    synchronized (this) {
                        UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if(usbDevice != null){
                            communicationListener.deviceConnect(usbDevice);
                        } else {
                            ZsfLog.d(UsbCommunicationManager.class, "UsbDevice is null" );
                        }
                    }
                    break;
                case UsbManager.ACTION_USB_ACCESSORY_DETACHED :
                case UsbManager.ACTION_USB_DEVICE_DETACHED :
                    synchronized (this) {
                        UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (usbDevice != null) {
                            communicationListener.deviceDisconnect(usbDevice);
                        } else {
                            ZsfLog.d(UsbCommunicationManager.class, "UsbDevice is null");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public interface CommunicationListener {
        /**
         * 设备连接
         * @param accessory
         */
        void deviceConnect(UsbDevice accessory);

        /**
         * 设备断开连接
         * @param accessory
         */
        void deviceDisconnect(UsbDevice accessory);

    }

    public static class UsbManagerBuilder {

        public CommunicationListener communicationListener;

        public Context context;

        public UsbManagerBuilder setContext(Context context) {
            this.context = context;
            return this;
        }

        public UsbManagerBuilder setCommunicationListener (CommunicationListener communicationListener) {
            this.communicationListener = communicationListener;
            return this;
        }

        public UsbCommunicationManager build() {
            if (context == null) {
                throw new InvalidParameterException("setContext(Context context) Must be called");
            }
            if (communicationListener == null) {
                throw new InvalidParameterException("setCommunicationListener (CommunicationListener communicationListener) Must be called");
            }
            return new UsbCommunicationManager(this);
        }
    }

}
