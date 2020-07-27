package com.zzy.permission.m_device_usb;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.view.activity.BaseActivity;
import java.util.HashMap;

@Route(path = "/m_device_usb/DeviceUsbActivity")
public class DeviceUsbActivity extends BaseActivity implements UsbUtils.UsbAttachedListener {

    private Button deviceUsbButton;
    private Button deviceUsbButtonClear;
    private TextView deviceUsbTextView;
    private HashMap<String, UsbDevice> deviceHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_device_usb);
        deviceUsbButton = findViewById(R.id.m_device_usb_button);
        deviceUsbButton.setOnClickListener(this);
        deviceUsbButtonClear = findViewById(R.id.m_device_usb_button_clear);
        deviceUsbButtonClear.setOnClickListener(this);
        deviceUsbTextView = findViewById(R.id.m_device_usb_text_view);
        deviceUsbTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    public void initData(Activity activity) {
        UsbUtils.getInstance().setListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.m_device_usb_button){
            showUsbInfo();
        }
        if (id == R.id.m_device_usb_button_clear){
            deviceUsbTextView.setText("");
        }
    }

    public void setResultText(final String result){
        deviceUsbTextView.post(new Runnable() {
            @Override
            public void run() {
                deviceUsbTextView.append("\n" + result);
                int scrollAmount = deviceUsbTextView.getLayout().getLineTop(deviceUsbTextView.getLineCount()) - deviceUsbTextView.getHeight();
                if (scrollAmount > 0)
                    deviceUsbTextView.scrollTo(0, scrollAmount);
                else
                    deviceUsbTextView.scrollTo(0, 0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UsbUtils.getInstance().onDestroy();
    }

    private void showUsbInfo(){
        deviceUsbTextView.setText("");
        deviceHashMap = UsbUtils.getInstance().getUsbDevice();
        StringBuilder item = new StringBuilder();
        if (deviceHashMap == null || deviceHashMap.size() == 0){
            item.append("未检测到有效外置USB设备!");
        }
        for (UsbDevice usbDevice : deviceHashMap.values()){
            item.append("\n 设备类别  :   " + usbDevice.getDeviceClass()
                    + "\n 设备标识  :   " + usbDevice.getDeviceId()
                    + "\n 设备名称  :   " + usbDevice.getDeviceName()
                    + "\n 设备协议  :   " + usbDevice.getDeviceProtocol()
                    + "\n 厂商 ID      :   " + usbDevice.getVendorId()
                    + "\n 产品 ID      :   " + usbDevice.getProductId()
                    + "\n " + usbDevice.toString());
            if (usbDevice.getProductId() == 25479){
                item.append("\n 设备不符合要求被禁用!");
            } else {

            }
        }
        setResultText(item.toString());
    }

    @Override
    public void onAttached() {
        deviceHashMap = UsbUtils.getInstance().getUsbDevice();
        deviceUsbTextView.setText("");
        deviceHashMap = UsbUtils.getInstance().getUsbDevice();
        StringBuilder item = new StringBuilder();
        if (deviceHashMap == null || deviceHashMap.size() == 0){
            item.append("未检测到有效外置USB设备!");
        }
        for (UsbDevice usbDevice : deviceHashMap.values()){
            item.append("\n 设备类别  :   " + usbDevice.getDeviceClass()
                    + "\n 设备标识  :   " + usbDevice.getDeviceId()
                    + "\n 设备名称  :   " + usbDevice.getDeviceName()
                    + "\n 设备协议  :   " + usbDevice.getDeviceProtocol()
                    + "\n 厂商 ID      :   " + usbDevice.getVendorId()
                    + "\n 产品 ID      :   " + usbDevice.getProductId()
                    + "\n " + usbDevice.toString());
            item.append("\n 设备USB已连接!");
            if (usbDevice.getProductId() == 25479){
                item.append("\n 设备不符合要求被禁用!");
            } else {

            }
        }
        setResultText(item.toString());
    }

    @Override
    public void onDetached() {
        deviceHashMap = UsbUtils.getInstance().getUsbDevice();
        deviceUsbTextView.setText("");
        StringBuilder item = new StringBuilder();
        if (deviceHashMap == null || deviceHashMap.size() == 0){
            item.append("未检测到有效外置USB设备!");
            item.append("\n设备断开USB连接! ");
        }
        setResultText(item.toString());
    }
}
