package com.zsf.test.branch.arouter.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zsf.test.R;
import com.zsf.test.StatusCode;
import com.zsf.utils.ZsfLog;

/**
 * @author zsf; 2019/8/2
 */
@Interceptor(priority =  1, name = "第一拦截器")
public class ArouterFirstInterceptorImpl implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        ZsfLog.d(ArouterFirstInterceptorImpl.class, "ARouter第一拦截器path = " + postcard.getPath());
        ZsfLog.d(ArouterFirstInterceptorImpl.class, "ARouter第一拦截器group = " + postcard.getGroup());
        ZsfLog.d(ArouterFirstInterceptorImpl.class, "ARouter第一拦截器extra = " + postcard.getExtra());
        // 打印bundle
        ZsfLog.d(ArouterFirstInterceptorImpl.class, "ARouter第一拦截器extras = " + postcard.getExtras());

        if (postcard.getExtra() == StatusCode.IS_NEED_LOGIN && !StatusCode.IS_LOGIN){
            // 比如：是未登录状态，跳转到登录页面
            ARouter.getInstance()
                    .build("/branch/ARouter/LoginArouterActivity")
                    .withString("url", postcard.getPath())
                    .navigation();
        } else if (postcard.getExtra() == 2){
            // 发现问题，爆出异常
            callback.onInterrupt(new RuntimeException("发现异常数据返回路由"));
        } else if (postcard.getExtra() == 3){
            // 拦截路由，中断分发
            callback.onInterrupt(null);
        } else {
            // 默认继续往下传递路由
            callback.onContinue(postcard);
        }

    }

    @Override
    public void init(Context context) {
        ZsfLog.d(ArouterFirstInterceptorImpl.class, "第一" + context.getString(R.string.m_test_ARouter_Interceptor_init));
    }
}
