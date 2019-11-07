package com.zsf.global;

import android.app.Application;
import android.content.Context;

/**
 * @author zsf; 2019/7/31
 */
public class GlobalData {
    public static Application application;

    /**
     * 全局上下文
     * @return
     */
    public static Context getContext(){
        return application.getApplicationContext();
    }

    /**
     * 设置全局上下文
     * @param application
     */
    public static void setApplication(Application application){
        GlobalData.application = application;
    }
}
