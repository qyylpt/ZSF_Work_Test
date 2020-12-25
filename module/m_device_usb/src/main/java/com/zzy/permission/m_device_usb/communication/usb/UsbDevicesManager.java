package com.zzy.permission.m_device_usb.communication.usb;

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

    private final ScannerListener scannerListener;

    private UsbManager usbManager;

    private final ArrayList<UsbEndpointThread> usbEndpointList = new ArrayList<>(5);

    private final ArrayList<Integer> deviceVendorIds = new ArrayList<>();

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
            this.context.registerReceiver(usbDevicesBroadcastReceiver, intentFilter);
        }
        initConnectedDevice();
    }

    private void initConnectedDevice() {
        usbManager = (UsbManager) this.context.getSystemService(Context.USB_SERVICE);
        HashMap<String,UsbDevice> deviceMap = usbManager.getDeviceList();
        for (UsbDevice usbDevice : deviceMap.values()) {
            if (deviceVendorIds.contains(usbDevice.getVendorId())) {
                deviceStartRead(usbDevice);
            }
        }
    }

    private void deviceStartRead(UsbDevice usbDevice) {
        scannerListener.deviceConnect(usbDevice);
        UsbEndpointThread usbEndpointThread = new UsbEndpointThread(usbDevice);
        String threadName = usbDevice.getSerialNumber();
        usbEndpointThread.setName(threadName != null ? threadName : String.valueOf(usbDevice.getProductId()));
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
    }


    private class UsbDevicesBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ToastUtils.showToast(context, "action = " + action);
            ZsfLog.d(UsbDevicesManager.class, "action = " + action);
            switch (action) {
                case UsbManager.ACTION_USB_ACCESSORY_ATTACHED:
                case UsbManager.ACTION_USB_DEVICE_ATTACHED :
                    synchronized (this) {
                        UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if(usbDevice != null && deviceVendorIds.contains(usbDevice.getVendorId())){
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
                        if (usbDevice != null) {
                            scannerListener.deviceDisconnect(usbDevice);
                            Iterator<UsbEndpointThread> usbEndpointThreadIterator = usbEndpointList.iterator();
                            while (usbEndpointThreadIterator.hasNext()) {
                                UsbEndpointThread usbEndpointThread = usbEndpointThreadIterator.next();
                                if (usbEndpointThread.getUsbDevice().getProductId() == usbDevice.getProductId() || usbEndpointThread.getUsbDevice().getSerialNumber().equals(usbDevice.getSerialNumber())) {
                                    usbEndpointThread.release();
                                    usbEndpointThreadIterator.remove();
                                }
                            }
                        } else {
                            ZsfLog.d(UsbDevicesManager.class, "UsbDevice is null");
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

        public UsbEndpointThread(UsbDevice usbDevice) {
            this.usbDevice = usbDevice;
            readyDevicesProtocolAisle();
        }

        public UsbDevice getUsbDevice() {
            return usbDevice;
        }

        public void release() {
            this.interrupt = true;
            usbDeviceConnection.releaseInterface(usbInterface);
            usbDeviceConnection.close();
            usbDeviceConnection = null;
            usbEndpointRead = null;
            usbDevice = null;
            usbRequest.close();
            usbRequest = null;
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
                            ZsfLog.d(UsbDevicesManager.class, Thread.currentThread().getName() + " : 读取到数据 => " + s);
                            scannerListener.scanResult(usbDevice, s);
                            byteBuffer.clear();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private int readData() throws IOException {
            if (!usbRequest.queue(mReadBuffer,mReadBuffer.array().length)) {
                throw new IOException(Thread.currentThread().getName() + " : Error queueing request.");
            }
            final UsbRequest usbRequestResponse = usbDeviceConnection.requestWait();
            if (usbRequestResponse == null) {
                throw new IOException(Thread.currentThread().getName() + " : Null response");
            }
            final int readNum = mReadBuffer.position();
            return Math.max(readNum, 0);
        }

        private void readyDevicesProtocolAisle() throws IllegalArgumentException{
            usbDeviceConnection = usbManager.openDevice(this.usbDevice);
            for (int i = 0; i < usbDevice.getInterfaceCount(); i++) {
                ZsfLog.d(UsbDevicesManager.class, Thread.currentThread().getName() + " : usbInterface : " + usbDevice.getInterface(i).toString());
                if (usbDevice.getInterface(i).getInterfaceClass() == UsbConstants.USB_CLASS_CDC_DATA || usbDevice.getInterface(i).getInterfaceClass() == UsbConstants.USB_CLASS_VIDEO || usbDevice.getInterface(i).getInterfaceClass() == UsbConstants.USB_CLASS_VENDOR_SPEC) {
                    this.usbInterface = usbDevice.getInterface(i);
                }
            }
            if (usbDeviceConnection == null) {
                ZsfLog.d(UsbDevicesManager.class, Thread.currentThread().getName() + " : UsbDeviceConnection is null");
                throw new IllegalArgumentException(Thread.currentThread().getName() + " : UsbDeviceConnection : not connect find");
            } else {
                usbDeviceConnection.claimInterface(this.usbInterface, true);
                ZsfLog.d(UsbDevicesManager.class, Thread.currentThread().getName() + " : UsbDeviceConnection claimInterface success");
            }
            for (int i = 0; i < this.usbInterface.getEndpointCount(); i++) {
                ZsfLog.d(UsbDevicesManager.class, Thread.currentThread().getName() + " : usbEndpoint : " + usbInterface.getEndpoint(i).toString());
                ZsfLog.d(UsbDevicesManager.class, Thread.currentThread().getName() + " : type : " + usbInterface.getEndpoint(i).getType() + "; Direction : " + usbInterface.getEndpoint(i).getDirection());
                if (usbInterface.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK || usbInterface.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_ISOC) {
                    if (usbInterface.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN) {
                        this.usbEndpointRead = usbInterface.getEndpoint(i);
                        ZsfLog.d(UsbDevicesManager.class, Thread.currentThread().getName() + " : usbEndpoint : read");
                    }
                }
            }
            if (usbEndpointRead == null) {
                throw new IllegalArgumentException(Thread.currentThread().getName() + " : UsbEndpoint : readUsbEndpoint not all endpoints found");
            }
        }
    }
}
