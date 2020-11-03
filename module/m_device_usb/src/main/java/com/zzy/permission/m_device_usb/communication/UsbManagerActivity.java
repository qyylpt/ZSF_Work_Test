package com.zzy.permission.m_device_usb.communication;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.global.GlobalData;
import com.zsf.view.activity.BaseActivity;
import com.zzy.permission.m_device_usb.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhangzhang
 */
@Route(path = "/m_orc/UsbManagerActivity")
public class UsbManagerActivity extends BaseActivity {

    private Button mUsbDeviceButtonSendMsg, mUsbDeviceButtonComplexDataClear;
    private TextView mUsbTextViewShowResult;

    private UsbCommunicationManager.UsbManagerBuilder usbManagerBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_main);
        mUsbDeviceButtonSendMsg = findViewById(R.id.m_usb_device_button_send_msg);
        mUsbDeviceButtonSendMsg.setOnClickListener(this);
        mUsbTextViewShowResult = findViewById(R.id.m_usb_device_text_show_result);
        mUsbTextViewShowResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        mUsbDeviceButtonComplexDataClear = findViewById(R.id.m_usb_device_button_complex_data_clear);
        mUsbDeviceButtonComplexDataClear.setOnClickListener(this);
    }

    @Override
    public void initData(Activity activity) {
        if (usbManagerBuilder == null) {
            usbManagerBuilder = new UsbCommunicationManager.UsbManagerBuilder();
            usbManagerBuilder.setContext(GlobalData.getContext())
                    .setCommunicationListener(new UsbCommunicationManager.CommunicationListener() {
                        @Override
                        public void deviceConnect(UsbDevice usbDevice) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                setResultText("设备连接 \n " + deviceToString(usbDevice) + "\n");
                            }
                        }

                        @Override
                        public void deviceDisconnect(UsbDevice usbDevice) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                setResultText("设备断开 \n " + deviceToString(usbDevice) + "\n");
                            }
                        }
                    }).build();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.m_usb_device_button_complex_data_clear) {
            mUsbTextViewShowResult.setText("");
        }
    }


    public void setResultText(final String result){
        mUsbTextViewShowResult.post(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat alldate = new SimpleDateFormat("yy/MM/dd HH:mm:ss");//获取日期时间
                mUsbTextViewShowResult.append("\n" + alldate.format(new Date()) + "   " + result);
                int scrollAmount = mUsbTextViewShowResult.getLayout().getLineTop(mUsbTextViewShowResult.getLineCount()) - mUsbTextViewShowResult.getHeight();
                if (scrollAmount > 0) {
                    mUsbTextViewShowResult.scrollTo(0, scrollAmount);
                } else {
                    mUsbTextViewShowResult.scrollTo(0, 0);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                "\n mVersion=" + usbDevice.getVersion() +
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
}