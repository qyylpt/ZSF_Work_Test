package com.zsf.test.branch.arouter.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.DegradeService;
import com.zsf.global.GlobalData;
import com.zsf.test.R;
import com.zsf.utils.ToastUtils;
import com.zsf.utils.ZsfLog;

/**
 * @author zsf
 * @date 2019/8/5
 * 路由丢失策略
 * 在路由丢失到的时候，执行onLost方法
 * 实现DegradeService接口，并加上一个Path内容任意的注解即可
 */
@Route(path = "/branch/ARouter")
public class DegradeServiceImpl implements DegradeService {
    @Override
    public void onLost(Context context, Postcard postcard) {
        ToastUtils.showToast(GlobalData.getContext(), GlobalData.getContext().getString(R.string.m_test_ARouter_lost));
    }

    /**
     * 在ARouter初始化时候执行一次
     * @param context
     */
    @Override
    public void init(Context context) {
        ZsfLog.d(DegradeServiceImpl.class, GlobalData.getContext().getResources().getString(R.string.m_test_ARouter_degrade_service_init));
    }
}
