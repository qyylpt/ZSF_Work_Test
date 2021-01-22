package com.zzy.permission.m_device_usb.communication.vendor;

import com.q_zheng.QZhengGPIOManager;
import com.vs.hx.VideoStrongManager;
import com.zzy.permission.m_device_usb.communication.common.DeviceTypeConstant;
import com.zzy.permission.m_device_usb.communication.common.LightAndRelayManager;

/**
 * @author : zsf
 * @date : 2020/12/23 4:57 PM
 * @desc : 远景达
 */
public class LightRelayManagerForYjd extends LightAndRelayManager {

    public LightRelayManagerForYjd(LightAndRelayBuild lightAndRelayBuild) {
        super(lightAndRelayBuild);
    }

    @Override
    public void operateLight(int color, int isOpen) {
        resetLight();
        int localColor = 0;
        switch (color) {
            case 0 :
                localColor = QZhengGPIOManager.GPIO_ID_LED_G;
                break;
            case 1 :
                localColor = QZhengGPIOManager.GPIO_ID_LED_R;
                break;
            case 4 :
                localColor = QZhengGPIOManager.GPIO_ID_LED_B;
                break;
            default:
                break;
        }
        if (localColor != 0 && !android.os.Build.MODEL.equals("X6S")) {
            QZhengGPIOManager.getInstance(lightAndRelayBuild.context).setValue(localColor, isOpen);
        } else {
            VideoStrongManager.VSSetIOValue(color, 1);
        }
        super.operateLight(color, isOpen);
    }

    @Override
    public void operateRelay(boolean isOpen) {
        int signal = isOpen ? 1 : 0;
        if (!DeviceTypeConstant.X6S.equals(android.os.Build.MODEL)){
            QZhengGPIOManager.getInstance(lightAndRelayBuild.context).setValue(QZhengGPIOManager.GPIO_ID_DOOR, signal);
        } else {
            VideoStrongManager.VSSetIOValue(5, signal);
        }
        super.operateRelay(isOpen);
    }

    @Override
    protected void resetLight() {
        if (!DeviceTypeConstant.X6S.equals(android.os.Build.MODEL)){
            QZhengGPIOManager.getInstance(lightAndRelayBuild.context).setValue(QZhengGPIOManager.GPIO_ID_LED_B, 0);
            QZhengGPIOManager.getInstance(lightAndRelayBuild.context).setValue(QZhengGPIOManager.GPIO_ID_LED_G, 0);
            QZhengGPIOManager.getInstance(lightAndRelayBuild.context).setValue(QZhengGPIOManager.GPIO_ID_LED_R, 0);
        } else {
            VideoStrongManager.VSSetIOValue(0, 0);
            VideoStrongManager.VSSetIOValue(1, 0);
            VideoStrongManager.VSSetIOValue(4, 0);
        }
    }
}
