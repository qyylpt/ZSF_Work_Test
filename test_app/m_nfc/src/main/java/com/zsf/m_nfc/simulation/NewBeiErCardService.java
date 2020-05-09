package com.zsf.m_nfc.simulation;

import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Bundle;
import com.newabel.nfcsdk.NfcHelper;
import com.zsf.utils.ToastUtils;
import com.zsf.utils.ZsfLog;

import androidx.annotation.RequiresApi;

/**
 * Auth: zhangzhang
 * Date: 2020-04-08
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class NewBeiErCardService extends HostApduService implements NfcHelper.NfcCallBack {

    private NfcHelper mNfcHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mNfcHelper = new NfcHelper(this);
        mNfcHelper.setNfcCallBack(this);
    }

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        final String hexCommandApdu = encodeHexString(commandApdu, true);
        ToastUtils.showToast(getApplicationContext(), "AID = " + hexCommandApdu);

        try {
//            String mCardID = PreferenceUtils.getUser();//mNfcHelper.getSN();
//            ZsfLog.d(NewBeiErCardService.class, "user: " + mCardID);
            mNfcHelper.setSN("6e6968616f");
            return mNfcHelper.processCommandApdu(commandApdu);
        } catch (Exception e) {
            ZsfLog.d(NewBeiErCardService.class, e.getMessage());
        }
        return new byte[0];
    }

    @Override
    public void onDeactivated(int reason) {

    }

    @Override
    public void dealWithMode(int mode, int state) {
        //mode 1 进门   2 出门   state: 0 通信成功  其他表示失败
        if(state!=0){
            ZsfLog.d(NewBeiErCardService.class, "state: " + state + "mode: " + mode);
            ToastUtils.showToast(getApplicationContext(), "nfc通信失败 state: " + state + " mode: " + mode);
            return;
        }

        if(mode==1){
            //进门
            ToastUtils.showToast(getApplicationContext(), "进门");
        }else if(mode == 2){
            //出门
            ToastUtils.showToast(getApplicationContext(), "出门");
        }else {
            ToastUtils.showToast(getApplicationContext(), "进门出门状态获取错误,错误状态mode: " + mode);
        }
    }

    public static String encodeHexString(byte[] byteArray, boolean upper) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (byte aByteArray : byteArray) {
            hexStringBuffer.append(byteToHex(aByteArray, upper));
        }
        return hexStringBuffer.toString();
    }

    public static String byteToHex(byte num, boolean upper) {
        char[] hexDigits = new char[2];
        if (upper) {
            hexDigits[0] = Character.toUpperCase(Character.forDigit((num >> 4) & 0xF, 16));
            hexDigits[1] = Character.toUpperCase(Character.forDigit((num & 0xF), 16));
        } else {
            hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
            hexDigits[1] = Character.forDigit((num & 0xF), 16);
        }

        return new String(hexDigits);
    }



    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(Character.toLowerCase(s.charAt(i)), 16) << 4)
                    + Character.digit(Character.toLowerCase(s.charAt(i+1)), 16));
        }
        return data;
    }
}
