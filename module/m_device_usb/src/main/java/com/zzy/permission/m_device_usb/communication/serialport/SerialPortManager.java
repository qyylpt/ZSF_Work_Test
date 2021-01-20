package com.zzy.permission.m_device_usb.communication.serialport;

import android.os.Handler;
import android.os.Message;

import com.zsf.utils.ZsfLog;
import com.zzy.permission.m_device_usb.communication.common.ScannerListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android_serialport_api.SerialPort;

/**
 * @author : zsf
 * @date : 2021/1/20 3:33 PM
 * @desc :
 */
public class SerialPortManager {

    /**
     * 远景达二维码串口
     */
    private final String YJD_SERIAL_PORT= "/dev/ttyS4";

    /**
     * 远景达二维码波特率
     */
    private final int YJD_BAUD_RATE = 115200;

    private SerialPort mSerialPort;
    private InputStream mInputStream;
    private ScannerListener scannerListener;
    private volatile boolean interrupted = false;
    private ScanHandler mScanHandler;

    /**
     * 用于缓存完整读码信息
     */
    private StringBuffer mStringBuffer = new StringBuffer();


    /**
     * 读码完成消息
     */
    private final int EDIT_OVER = 0;

    /**
     * 读码阈值
     */
    private int editThreshold = 50;

    private int readFrequency = 20;

    private ReadThread mReadThread;



    public SerialPortManager(ScannerListener scannerListener) {
        this.scannerListener = scannerListener;
        mScanHandler = new ScanHandler();
        this.scannerListener.deviceConnect(null);
        openSerialPort();
        mReadThread = new ReadThread();
        mReadThread.start();
    }

    private void openSerialPort() {
        try {
            mSerialPort = new SerialPort(new File(YJD_SERIAL_PORT), YJD_BAUD_RATE, 0);
            mInputStream = mSerialPort.getInputStream();
        } catch (IOException e) {
            ZsfLog.d(SerialPortManager.class, "串口打开失败");
            e.printStackTrace();
        }
    }

    public void release() {
        interrupted = true;
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        if (mScanHandler != null) {
            mScanHandler.removeCallbacksAndMessages(null);
            mScanHandler = null;
        }
    }

    /**
     * 串口读码
     * 注: 拔出USB设备依然会从串口读取。直至USB 设备监听到拔出动作release
     */
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!interrupted) {
                try {
                    if (mInputStream == null) {
                        return;
                    }
                    int available = mInputStream.available();
                    if (available > 0) {
                        byte[] buffer = new byte[available];
                        int size = mInputStream.read(buffer);
                        if (size > 0) {
                            String tmp = new String(buffer);
                            String stringBuffer = mStringBuffer.toString();
                            if (stringBuffer.contains(tmp)) {
                                continue;
                            }
                            mStringBuffer.append(tmp);
                            mScanHandler.removeMessages(EDIT_OVER);
                            mScanHandler.sendEmptyMessageDelayed(EDIT_OVER, editThreshold);
                        }
                    }
                    Thread.sleep(readFrequency);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ScanHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (EDIT_OVER == msg.what) {
                if (scannerListener != null) {
                    String newQrCode = mStringBuffer.toString();
                    scannerListener.scanResult(null, newQrCode);
                    ZsfLog.d(SerialPortManager.class, "二维码信息 : " + newQrCode);
                }
                mStringBuffer.setLength(0);
            }
        }
    }
}
