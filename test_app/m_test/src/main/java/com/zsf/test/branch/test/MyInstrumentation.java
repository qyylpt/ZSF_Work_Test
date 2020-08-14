package com.zsf.test.branch.test;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.InstrumentationInfo;
import android.os.Bundle;
import android.widget.Button;

import com.blankj.utilcode.util.AppUtils;
import com.zsf.utils.ToastUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author zsf
 * @date 2020/1/9
 * @Usage
 */
public class MyInstrumentation extends Instrumentation {




    public static void restartApp(Context context){
        ToastUtils.showToast(context, android.os.Build.MODEL);
//        try {
//            Runtime.getRuntime().exec("am instrument -w com.zsf.m_test/com.zsf.test.branch.test.MyInstrumentation");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        GlobalData.getContext().startInstrumentation(new ComponentName(context, MyInstrumentation.class), null, null);
//        boolean is =  GlobalData.getContext().startInstrumentation(new ComponentName(AppUtils.getAppPackageName(),"com.zsf.test.branch.test.MyInstrumentation" ),null,null);
//        ZsfLog.d(MyInstrumentation.class, "restartApp : " + GlobalData.getContext().startInstrumentation(new ComponentName(AppUtils.getAppPackageName(),"com.zsf.test.branch.test.MyInstrumentation" ),null,null));
//        String packageName = "com.zsf.test";
//        final List<InstrumentationInfo> list = context.getPackageManager().queryInstrumentation(AppUtils.getAppPackageName(), 0);
//        if (list.isEmpty()){
//            ToastUtils.showToast(context, "Instrumentation is empty");
//            return;
//        }
//        final InstrumentationInfo instrumentationInfo = list.get(0);
//        final ComponentName componentName = new ComponentName(instrumentationInfo.targetPackage, instrumentationInfo.name);
//        Bundle arguments = new Bundle();
//        arguments.putString("class", "com.zsf.m_test");
//        context.startInstrumentation(componentName, null, arguments);
//        context.getInstrumentation().callActivityOnRestart(myActivity);

    }
}
