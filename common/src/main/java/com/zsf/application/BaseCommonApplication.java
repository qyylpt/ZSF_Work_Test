package com.zsf.application;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.zsf.application.set.CommonApplicationSetting;
import com.zsf.global.GlobalData;

import java.lang.reflect.Method;

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
        supportHideApiOpen(this);
    }

    private void supportHideApiOpen(Context context) {
        if (28 <= Build.VERSION.SDK_INT) {//  for  android  Q,  fix  hidden  api
            try {
                Method mm = Class.class.getDeclaredMethod("forName", String.class);
                Class<?> cls = (Class) mm.invoke(null, "dalvik.system.VMRuntime");
                mm = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
                Method m = (Method) mm.invoke(cls, "getRuntime", null);
                Object vr = m.invoke(null);
                m = (Method) mm.invoke(cls, "setHiddenApiExemptions", new Class[]{String[].class});
                final String[] args = {"L"};
                m.invoke(vr, new Object[]{args});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
