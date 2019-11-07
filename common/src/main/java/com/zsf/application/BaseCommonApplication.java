package com.zsf.application;

import android.app.Application;

import com.zsf.application.set.CommonApplicationSetting;
import com.zsf.global.GlobalData;

/**
 * @author zsf; 2019/7/31
 */
public class BaseCommonApplication extends Application {
    public static final boolean IS_DEBUG = true;
    @Override
    public void onCreate() {
        super.onCreate();
        GlobalData.setApplication(this);
        CommonApplicationSetting.initCommonApplication(this);
    }

}
