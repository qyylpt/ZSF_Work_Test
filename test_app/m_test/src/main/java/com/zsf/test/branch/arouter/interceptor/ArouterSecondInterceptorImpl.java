package com.zsf.test.branch.arouter.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.zsf.test.R;
import com.zsf.utils.ZsfLog;

/**
 * @author zsf
 * @date 2019/8/2
 * 这里为了测试拦截器的优先级，不做拦截
 */
@Interceptor(priority =  2, name = "第二拦截器")
public class ArouterSecondInterceptorImpl implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        ZsfLog.d(ArouterSecondInterceptorImpl.class, "ARouter第二拦截器path = " + postcard.getPath());
        ZsfLog.d(ArouterSecondInterceptorImpl.class, "ARouter第二拦截器group = " + postcard.getGroup());
        ZsfLog.d(ArouterSecondInterceptorImpl.class, "ARouter第二拦截器extra = " + postcard.getExtra());
        // 打印bundle
        ZsfLog.d(ArouterSecondInterceptorImpl.class, "ARouter第二拦截器extras = " + postcard.getExtras());
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {
        ZsfLog.d(ArouterSecondInterceptorImpl.class, "第二" + context.getString(R.string.m_test_ARouter_Interceptor_init));
    }
}
