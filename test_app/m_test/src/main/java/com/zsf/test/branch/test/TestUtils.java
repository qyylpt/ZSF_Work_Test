package com.zsf.test.branch.test;


import android.bluetooth.BluetoothAdapter;


/**
 * @author zsf
 * @date 2019/10/22
 */
public class TestUtils {

    public static boolean setBluetooth(boolean enable){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.disable();
        return true;
    }



}
