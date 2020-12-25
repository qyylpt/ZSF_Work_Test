package com.zzy.permission.m_device_usb.communication.common;

import android.hardware.usb.UsbDevice;

/**
 * @author : zsf
 * @date : 2020/12/23 4:34 PM
 * @desc :
 */
public interface ScannerListener {

    /**
     * 设备连接
     * @param usbDevice 连接设备
     */
    void deviceConnect(UsbDevice usbDevice);

    /**
     * 设备断开连接
     * @param usbDevice 断开设备
     */
    void deviceDisconnect(UsbDevice usbDevice);

    /**
     * 扫码结果
     * @param usbDevice 数据来源
     * @param scanInfo 扫码结果
     */
    void scanResult(UsbDevice usbDevice, String scanInfo);

    /**
     * 关闭灯光(注意 : 灯光关闭与UI结果展示同步)
     */
    void closeLight();

    /**
     * 关闭继电器(注意 : 继电器关闭与UI结果展示同步)
     */
    void closeRelay();
}
