package com.zzy.permission.m_device_usb.communication.vendor;

import com.zx.zxlibrary.SystemUtils;
import com.zzy.permission.m_device_usb.communication.common.LightAndRelayManager;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author : zsf
 * @date : 2020/12/23 5:34 PM
 * @desc : 睿智谷
 */
public class LightRelayManagerForRzg extends LightAndRelayManager {

    /**
     * 继电器
     */
    public static String GDQ = "/sys/class/zx_relay_ctl/zx_relay_ctl";

    public LightRelayManagerForRzg(LightAndRelayBuild lightAndRelayBuild) {
        super(lightAndRelayBuild);
    }

    @Override
    public void operateLight(int color, int isOpen) {
        resetLight();
        switch (color) {
            case 0 :
                SystemUtils.setGPIO5(true);
                break;
            case 1 :
                SystemUtils.setGPIO6(true);
                break;
            case 4 :
                SystemUtils.setGPIO4(true);
                break;
            default:
                break;
        }
        super.operateLight(color, isOpen);
    }

    @Override
    public void operateRelay(int isOpen) {
        setRelay(isOpen == 1);
        super.operateRelay(isOpen);
    }

    @Override
    protected void resetLight() {
        //白色
        SystemUtils.setGPIO4(false);
        //绿色
        SystemUtils.setGPIO5(false);
        //红色
        SystemUtils.setGPIO6(false);
    }

    public static void setRelay(boolean open) {
        KctWriteSysFile(open ? "1" : "0", GDQ);
    }

    public static void KctWriteSysFile(String data, String mFilePath) {
        FileOutputStream fops;
        try {
            fops = new FileOutputStream(mFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            return;
        }
        try {
            fops.write(data.getBytes());
            fops.flush();
            fops.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}