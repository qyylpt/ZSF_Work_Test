package com.zzy.permission.m_device_usb.communication.common;

/**
 * @author : zsf
 * @date : 2020/12/25 11:20 AM
 * @desc :
 */
public class DeviceTypeConstant {
    /**
     * 睿智谷平板
     */
    public static final String PAD_RUI_ZHI_GUO = "zxsn";
    /**
     * 兼容老版本设备
     */
    public static final String X6S = "X6S";

    /**
     * FM25-U、FM25-EX-U 新大陆
     */
    public static final int NLS_FM25 = 7851;

    /**
     * RD4500R_2006010947
     */
    public static final int RD4500R = 1137;

    /**
     * usb虚拟串口 排除X6S 标示[由于只有一台设备无法确认唯一标示]
     */
    public static final String X6S_EXCLUDE_ONE = "/dev/bus/usb/005/002";
    public static final String X6S_EXCLUDE_TWO = "/dev/bus/usb/001/002";

}
