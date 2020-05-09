package com.zsf.m_ipc;

import android.app.Service;

/**
 * Author: zsf
 * Date: 2020-04-28 14:51
 */
public abstract class BaseService extends Service {

    @Override
    public void onCreate() {
        initData();
        super.onCreate();
    }

    public abstract void initData();
}
