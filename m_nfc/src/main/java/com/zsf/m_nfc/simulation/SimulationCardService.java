package com.zsf.m_nfc.simulation;

import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Bundle;
import com.zsf.utils.ToastUtils;
import androidx.annotation.RequiresApi;

/**
 * Auth: zhangzhang
 * Date: 2020-03-18
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class SimulationCardService extends HostApduService {

    // self-defined APDU
    public static final String STATUS_SUCCESS = "9000";
    public static final String STATUS_FAILED = "6F00";
    public static final String CLA_NOT_SUPPORTED = "6E00";
    public static final String INS_NOT_SUPPORTED = "6D00";
    public static final String AID = "A0000002471001";
    public static final String SELECT_INS = "A4";
    public static final String DEFAULT_CLA = "00";
    public static final int MIN_APDU_LENGTH = 12;

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {



        if (commandApdu == null) {
            return hexStringToByteArray(STATUS_SUCCESS);
        }

        final String hexCommandApdu = encodeHexString(commandApdu, true);
        ToastUtils.showToast(this, "莫妮卡来了!  " + hexCommandApdu);
        if (hexCommandApdu.length() < MIN_APDU_LENGTH) {
            return hexStringToByteArray(STATUS_FAILED);
        }

        if (!hexCommandApdu.substring(0, 2).equals(DEFAULT_CLA)) {
            return hexStringToByteArray(CLA_NOT_SUPPORTED);
        }

        if (!hexCommandApdu.substring(2, 4).equals(SELECT_INS)) {
            return hexStringToByteArray(INS_NOT_SUPPORTED);
        }

        if (hexCommandApdu.substring(10, 24).equals(AID))  {
            return hexStringToByteArray(STATUS_SUCCESS);
        } else {
            return hexStringToByteArray(STATUS_FAILED);
        }
    }

    @Override
    public void onDeactivated(int reason) {
        ToastUtils.showToast(this, "断开原因 code :" + reason);
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

    public static String encodeHexString(byte[] byteArray, boolean upper) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (byte aByteArray : byteArray) {
            hexStringBuffer.append(byteToHex(aByteArray, upper));
        }
        return hexStringBuffer.toString();
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
