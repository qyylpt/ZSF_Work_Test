package com.zsf.test.branch.arouter.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.global.GlobalData;
import com.zsf.test.R;
import com.zsf.utils.ToastUtils;
import com.zsf.utils.ZsfLog;

/**
 * @author zsf; 2019/7/31
 * ARouter提供提供暴露服务
 */
@Route(path = "/ARouter/ARouterService")
public class ArouterServiceImpl implements IService {
    @Override
    public void sayHello(String msg) {
        ZsfLog.d(ArouterServiceImpl.class, msg);
    }

    /**
     * 在ARouter初始化的时候执行一次
     * @param context
     */
    @Override
    public void init(Context context) {
        ZsfLog.d(ArouterServiceImpl.class, GlobalData.getContext().getResources().getString(R.string.m_test_ARouter_IProvider_service_init));
    }
}
