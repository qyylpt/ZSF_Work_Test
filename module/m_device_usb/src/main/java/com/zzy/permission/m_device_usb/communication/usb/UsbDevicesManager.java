package com.zzy.permission.m_device_usb.communication.usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.nfc.Tag;

import com.zsf.utils.ToastUtils;
import com.zsf.utils.ZsfLog;
import com.zzy.permission.m_device_usb.communication.common.DeviceTypeConstant;
import com.zzy.permission.m_device_usb.communication.common.ScannerListener;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static android.hardware.usb.UsbManager.ACTION_USB_ACCESSORY_ATTACHED;

/**
 * @author : zsf
 * @date : 2020/12/14 4:56 PM
 * @desc :
 */
public class UsbDevicesManager {

    private UsbDevicesBroadcastReceiver usbDevicesBroadcastReceiver;

    private final Context context;

    private ScannerListener scannerListener;

    private UsbManager usbManager;

    private final ArrayList<UsbEndpointThread> usbEndpointList = new ArrayList<>(5);

    private final ArrayList<Integer> deviceVendorIds = new ArrayList<>();

    private PendingIntent permissionIntent;

    public static final String ACTION_USB_PERMISSION = "com.zhizhangyi.scan.USB_PERMISSION";

    private UsbDevicesManager(UsbManagerBuilder usbManagerBuilder) {
        addSupportDevice();
        this.context = usbManagerBuilder.mContext;
        this.scannerListener = usbManagerBuilder.scannerListener;
        if (usbDevicesBroadcastReceiver == null) {
            usbDevicesBroadcastReceiver = new UsbDevicesBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            intentFilter.addAction(ACTION_USB_ACCESSORY_ATTACHED);
            intentFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
            intentFilter.addAction(ACTION_USB_PERMISSION);
            this.context.registerReceiver(usbDevicesBroadcastReceiver, intentFilter);
        }
        permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        initConnectedDevice();
    }

    private void initConnectedDevice() {
        usbManager = (UsbManager) this.context.getSystemService(Context.USB_SERVICE);
        HashMap<String,UsbDevice> deviceMap = usbManager.getDeviceList();
        for (UsbDevice usbDevice : deviceMap.values()) {
            ZsfLog.d(UsbDevicesManager.class, "initConnectedDevice UsbDevice : " + usbDevice.toString());
            deviceStartRead(usbDevice);
        }
    }

    private void deviceStartRead(UsbDevice usbDevice) {
        if (!usbManager.hasPermission(usbDevice)) {
            usbManager.requestPermission(usbDevice, permissionIntent);
            return;
        }
        if (usbManager.openDevice(usbDevice) == null || !deviceVendorIds.contains(usbDevice.getVendorId())) {
            return;
        }
        scannerListener.deviceConnect(usbDevice);
        UsbEndpointThread usbEndpointThread = new UsbEndpointThread(usbDevice);
        usbEndpointThread.setName(usbDevice.getDeviceName());
        usbEndpointList.add(usbEndpointThread);
        usbEndpointThread.start();
    }

    private void addSupportDevice() {
        deviceVendorIds.add(DeviceTypeConstant.NLS_FM25);
        deviceVendorIds.add(DeviceTypeConstant.RD4500R);
    }

    /**
     * 回收资源
     */
    public void release() {
        if (usbDevicesBroadcastReceiver != null) {
            this.context.unregisterReceiver(usbDevicesBroadcastReceiver);
            usbDevicesBroadcastReceiver = null;
        }
        for (int i = 0; i < usbEndpointList.size(); i++) {
            usbEndpointList.get(i).release();
        }
        usbEndpointList.clear();
        scannerListener = null;
    }


    private class UsbDevicesBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ZsfLog.d(UsbDevicesManager.class, "action = " + action);
            switch (action) {
                case UsbManager.ACTION_USB_ACCESSORY_ATTACHED:
                case UsbManager.ACTION_USB_DEVICE_ATTACHED :
                    synchronized (this) {
                        UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if(usbDevice != null){
                            deviceStartRead(usbDevice);
                        } else {
                            ZsfLog.d(UsbDevicesManager.class, "UsbDevice is null" );
                        }
                    }
                    break;
                case UsbManager.ACTION_USB_ACCESSORY_DETACHED :
                case UsbManager.ACTION_USB_DEVICE_DETACHED :
                    synchronized (this) {
                        UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        ZsfLog.d(UsbDevicesManager.class, "USB_DEVICE_DETACHED : " + usbDevice.getSerialNumber());
                        if (usbDevice != null) {
                            scannerListener.deviceDisconnect(usbDevice);
                            Iterator<UsbEndpointThread> usbEndpointThreadIterator = usbEndpointList.iterator();
                            while (usbEndpointThreadIterator.hasNext()) {
                                UsbEndpointThread usbEndpointThread = usbEndpointThreadIterator.next();
                                if (!usbEndpointThread.getUsbDevice().getDeviceName().equals(usbDevice.getDeviceName())) {
                                    usbEndpointThread.release();

                                }
                                usbEndpointThreadIterator.remove();
                            }
                            initConnectedDevice();
                        } else {
                            ZsfLog.d(UsbDevicesManager.class, "UsbDevice is null");
                        }
                    }
                    break;
                case ACTION_USB_PERMISSION :
                    synchronized (this) {
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            deviceStartRead((UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE));
                            ZsfLog.d(UsbDevicesManager.class, "Permission passed for USB device");
                        } else {
                            ZsfLog.d(UsbDevicesManager.class, "Permission denied for USB device");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }


    public static class UsbManagerBuilder {

        private ScannerListener scannerListener;

        private final Context mContext;

        public UsbManagerBuilder(Context context) {
            this.mContext = context;
        }

        public UsbManagerBuilder setScannerListener (ScannerListener scannerListener) {
            this.scannerListener = scannerListener;
            return this;
        }

        public UsbDevicesManager build() {
            if (mContext == null) {
                throw new InvalidParameterException("setContext(Context context) Must be called");
            }
            if (scannerListener == null) {
                throw new InvalidParameterException("setCommunicationListener (CommunicationListener communicationListener) Must be called");
            }
            return new UsbDevicesManager(this);
        }
    }

    private class UsbEndpointThread extends Thread {

        private UsbDevice usbDevice;
        private boolean interrupt = false;
        private UsbDeviceConnection usbDeviceConnection;
        private UsbEndpoint usbEndpointRead;
        private UsbInterface usbInterface;
        private static final int BUF_SIZE = 2048;
        private final ByteBuffer mReadBuffer = ByteBuffer.allocate(BUF_SIZE);
        private final ByteBuffer byteBuffer = ByteBuffer.wrap(mReadBuffer.array());
        private UsbRequest usbRequest;
        private boolean isFirst = true;

        public UsbEndpointThread(UsbDevice usbDevice) {
            this.usbDevice = usbDevice;
            readyDevicesProtocolAisle();
        }

        public UsbDevice getUsbDevice() {
            return usbDevice;
        }

        public void release() {
            this.interrupt = true;
            // 由于睿智谷设备系统问题，如果直接reset()会造成usbDeviceConnection.requestWait()系统内部空指针。解决办法通过重新声明读取通道发送一条空信息，然后跳出while循环之后再回收
            usbManager.openDevice(this.usbDevice).claimInterface(this.usbInterface, true);
            ZsfLog.d(UsbDevicesManager.class, "release : " + usbDevice.toString());
        }

        private void reset(){
            ZsfLog.d(UsbDevicesManager.class, "reset : " + usbDevice.toString());
            usbRequest.close();
            usbRequest = null;
            usbDeviceConnection.releaseInterface(usbInterface);
            usbDeviceConnection.close();
            usbDeviceConnection = null;
            usbEndpointRead = null;
            usbDevice = null;
            byteBuffer.clear();
        }

        @Override
        public void run() {
            try {
                usbRequest = new UsbRequest();
                usbRequest.initialize(usbDeviceConnection, usbEndpointRead);
                if (usbDeviceConnection != null && usbEndpointRead != null) {
                    while (!this.interrupt) {
                        int length = readData();
                        if (length > 0) {
                            final byte[] data = new byte[length];
                            byteBuffer.get(data, 0, length);
                            final StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < data.length && data[i] != 0; i++) {
                                stringBuilder.append((char) data[i]);
                            }
                            String s = stringBuilder.toString();
                            ZsfLog.d(UsbDevicesManager.class, usbDevice.getSerialNumber() == null ? Thread.currentThread().getName() : usbDevice.getSerialNumber() + " : 读取到数据 => " + s);
                            if (isFirst) {
                                isFirst = false;
                            } else {
                                scannerListener.scanResult(usbDevice, s);
                            }
                            byteBuffer.clear();
                        }
                    }
                }
            } catch (Exception e) {
                if (usbDevice != null) {
                    ZsfLog.d(UsbDevicesManager.class, "Exception UsbDeviceName : " + usbDevice.getSerialNumber());
                }
                ZsfLog.d(UsbDevicesManager.class, "It may be an abnormality caused by X6S equipment or resource recycling \n ");
                usbEndpointList.remove(this);
                e.printStackTrace();
            } finally {
                reset();
            }
        }

        private int readData() throws IOException {
            if (!usbRequest.queue(mReadBuffer, mReadBuffer.array().length)) {
                throw new IOException(Thread.currentThread().getName() + " : Error queueing request.");
            }
            final UsbRequest usbRequestResponse = usbDeviceConnection.requestWait();
            ZsfLog.d(UsbDevicesManager.class, "readData()");
            if (usbRequestResponse == null) {
                throw new IOException(Thread.currentThread().getName() + " : Null response");
            }
            final int readNum = mReadBuffer.position();
            return Math.max(readNum, 0);
        }

        private void readyDevicesProtocolAisle() throws IllegalArgumentException{
            for (int i = 0; i < usbDevice.getInterfaceCount(); i++) {
                UsbInterface usbInterface = usbDevice.getInterface(i);
                if (usbInterface == null) {
                    continue;
                }
                if (usbInterface.getInterfaceClass() == UsbConstants.USB_CLASS_CDC_DATA || usbInterface.getInterfaceClass() == UsbConstants.USB_CLASS_VIDEO || usbInterface.getInterfaceClass() == UsbConstants.USB_CLASS_VENDOR_SPEC) {
                    this.usbInterface = usbDevice.getInterface(i);
                }
            }
            usbDeviceConnection = usbManager.openDevice(this.usbDevice);
            usbDeviceConnection.claimInterface(this.usbInterface, true);
            for (int i = 0; i < this.usbInterface.getEndpointCount(); i++) {
                UsbEndpoint usbEndpoint = usbInterface.getEndpoint(i);
                if (usbEndpoint == null) {
                    continue;
                }
                if (usbEndpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK || usbEndpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_ISOC) {
                    if (usbInterface.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN) {
                        this.usbEndpointRead = usbInterface.getEndpoint(i);
                        ZsfLog.d(UsbDevicesManager.class, Thread.currentThread().getName() + " : usbEndpoint : read");
                        if (usbDevice != null){
                            ZsfLog.d(UsbDevicesManager.class, usbDevice.toString());
                        }
                    }
                }
            }
        }
    }
}
