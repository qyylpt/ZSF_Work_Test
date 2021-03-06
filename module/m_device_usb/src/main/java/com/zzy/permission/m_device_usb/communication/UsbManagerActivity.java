package com.zzy.permission.m_device_usb.communication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.global.GlobalData;
import com.zsf.utils.ToastUtils;
import com.zsf.utils.ZsfLog;
import com.zsf.view.activity.BaseActivity;
import com.zzy.permission.m_device_usb.R;
import com.zzy.permission.m_device_usb.communication.api.ScannerControlApi;
import com.zzy.permission.m_device_usb.communication.common.ScannerListener;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhangzhang
 */
@Route(path = "/m_device_usb/UsbManagerActivity")
public class UsbManagerActivity extends BaseActivity {

    private TextView mUsbTextViewShowResult;

    private ScannerControlApi scannerControlApi;

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_main);
        mUsbTextViewShowResult = findViewById(R.id.m_usb_device_text_show_result);
        mUsbTextViewShowResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        Button mUsbDeviceButtonComplexDataClear = findViewById(R.id.m_usb_device_button_complex_data_clear);
        mUsbDeviceButtonComplexDataClear.setOnClickListener(this);
        Button mUsbDeviceButtonLightRed = findViewById(R.id.m_device_usb_light_red);
        mUsbDeviceButtonLightRed.setOnClickListener(this);
        Button mUsbDeviceButtonLightGreen = findViewById(R.id.m_device_usb_light_green);
        mUsbDeviceButtonLightGreen.setOnClickListener(this);
        Button mUsbDeviceButtonLightWhite = findViewById(R.id.m_device_usb_text_light_white);
        mUsbDeviceButtonLightWhite.setOnClickListener(this);
        Button mUsbDeviceButtonRelayOpen = findViewById(R.id.m_device_usb_text_relay_open);
        mUsbDeviceButtonRelayOpen.setOnClickListener(this);
        Button mUsbDeviceButtonRelayClose = findViewById(R.id.m_device_usb_text_relay_close);
        mUsbDeviceButtonRelayClose.setOnClickListener(this);
        Button mUsbDeviceButtonSoundOpen = findViewById(R.id.m_device_usb_button_sound_open);
        mUsbDeviceButtonSoundOpen.setOnClickListener(this);
        Button mUsbDeviceButtonSoundClose = findViewById(R.id.m_device_usb_button_sound_close);
        mUsbDeviceButtonSoundClose.setOnClickListener(this);
        Button mUsbDeviceButtonScreenOpen = findViewById(R.id.m_device_usb_button_screen_open);
        mUsbDeviceButtonScreenOpen.setOnClickListener(this);
        Button mUsbDeviceButtonScreenClose = findViewById(R.id.m_device_usb_button_screen_close);
        mUsbDeviceButtonScreenClose.setOnClickListener(this);
        editText = findViewById(R.id.m_device_usb_edit_sound_title);
    }

    @Override
    public void initData(Activity activity) {
        if (scannerControlApi == null) {
            scannerControlApi = new ScannerControlApi.ScannerBuild(GlobalData.getContext())
                    .setDelayCloseLightTime(2000)
                    .setDelayCloseRelayTime(2000)
                    .setScannerListener(new ScannerListener() {
                        @Override
                        public void deviceConnect(UsbDevice usbDevice) {
                            ToastUtils.showToast(GlobalData.getContext(), usbDevice == null ? "X6S" : usbDevice.getSerialNumber() + " : 设备连接!");
                            setResultText("设备连接 \n " + (usbDevice == null ? "X6S" : usbDevice.toString()) + "\n");
                        }

                        @Override
                        public void deviceDisconnect(UsbDevice usbDevice) {
                            ToastUtils.showToast(GlobalData.getContext(), usbDevice == null ? "X6S" : usbDevice.getSerialNumber() + " : 设备断开连接!");
                            setResultText("设备断开 \n " + (usbDevice == null ? "X6S" : usbDevice.toString()) + "\n");
                        }

                        @Override
                        public void scanResult(UsbDevice usbDevice, String scanInfo) {
                            if (usbDevice != null) {
                                if ("FC8K4247".equals(usbDevice.getSerialNumber())) {
                                    scannerControlApi.operateRelay(true);
                                    scannerControlApi.startPlayRing("进场成功");
                                } else {
                                    scannerControlApi.operateRelay(true);
                                    scannerControlApi.startPlayRing("出厂成功");
                                }
                            } else {
                                scannerControlApi.operateRelay(true);
                                scannerControlApi.startPlayRing("x6s扫码成功");
                            }
                            ToastUtils.showToast(GlobalData.getContext(), (usbDevice == null ? "X6S" : usbDevice.getSerialNumber()) + " : 读取到数据!");
                            setResultText("【 " + (usbDevice == null ? "X6S" : usbDevice.getSerialNumber()) + " 】读取到数据:  \n " + scanInfo + "\n");

                        }

                        @Override
                        public void closeLight() {
                            ToastUtils.showToast(GlobalData.getContext(), "关闭灯光");
                            setResultText("关闭灯光:  \n ");
                        }

                        @Override
                        public void closeRelay() {
                            ToastUtils.showToast(GlobalData.getContext(), "关闭继电器");
                            setResultText("关闭继电器:  \n ");
                        }
                    })
                    .build();
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.m_device_usb_light_red) {
            scannerControlApi.operateLight(1, 1);
        } else if (id == R.id.m_device_usb_light_green) {
            scannerControlApi.operateLight(0, 1);
        } else if (id == R.id.m_device_usb_text_light_white) {
            scannerControlApi.operateLight(4, 1);
        } else if (id == R.id.m_device_usb_text_relay_open) {
            scannerControlApi.operateRelay(true);
        } else if (id == R.id.m_device_usb_text_relay_close) {
            scannerControlApi.operateRelay(false);
        } else if (id == R.id.m_device_usb_button_sound_open) {
            if (TextUtils.isEmpty(editText.getText())) {
                scannerControlApi.startPlayRing(R.raw.sound);
            } else {
                scannerControlApi.startPlayRing(editText.getText().toString());
            }
        } else if (id == R.id.m_device_usb_button_sound_close) {
            scannerControlApi.stopPlayRing();
        } else if (id == R.id.m_usb_device_button_complex_data_clear) {
            mUsbTextViewShowResult.setText("");
        } else if (id == R.id.m_device_usb_button_screen_open) {
            scannerControlApi.showOrHideNavigationStatusBar(false);
        } else if (id == R.id.m_device_usb_button_screen_close) {
            scannerControlApi.showOrHideNavigationStatusBar(true);
        }

    }


    public void setResultText(final String result) {
        mUsbTextViewShowResult.post(new Runnable() {
            @Override
            public void run() {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat alldate = new SimpleDateFormat("yy/MM/dd HH:mm:ss");//获取日期时间
                mUsbTextViewShowResult.append("\n" + alldate.format(new Date()) + "   " + result);
                int scrollAmount = mUsbTextViewShowResult.getLayout().getLineTop(mUsbTextViewShowResult.getLineCount()) - mUsbTextViewShowResult.getHeight();
                mUsbTextViewShowResult.scrollTo(0, Math.max(scrollAmount, 0));
            }
        });
    }

    public String deviceToString(UsbDevice usbDevice) {
        StringBuilder builder = new StringBuilder(
                "\n UsbDevice[mName=" + usbDevice.getDeviceName() +
                        "\n mVendorId=" + usbDevice.getVendorId() +
                        "\n mProductId=" + usbDevice.getProductId() +
                        "\n mClass=" + usbDevice.getDeviceClass() +
                        "\n mSubclass=" + usbDevice.getDeviceSubclass() +
                        "\n mProtocol=" + usbDevice.getDeviceProtocol() +
                        "\n mManufacturerName=" + usbDevice.getManufacturerName() +
                        "\n mProductName=" + usbDevice.getProductName() +
//                "\n mVersion=" + usbDevice.getVersion() +
                        "\n mSerialNumberReader=" + usbDevice.getSerialNumber() +
                        "\n ================ " +
                        "\n mConfigurations=[");
        for (int i = 0; i < usbDevice.getConfigurationCount(); i++) {
            builder.append("\n");
            builder.append(usbDevice.getConfiguration(i).toString());
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scannerControlApi != null) {
            scannerControlApi.release();
            scannerControlApi = null;
        }
    }
}