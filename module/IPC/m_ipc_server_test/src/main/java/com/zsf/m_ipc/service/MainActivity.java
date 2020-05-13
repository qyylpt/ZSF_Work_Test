package com.zsf.m_ipc.service;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zsf.ipc_core.IPCManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IPCManager.getDefault().serviceRegister(DataManager.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IPCManager.getDefault().serviceUnRegiter(DataManager.class);
    }
}
