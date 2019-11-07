package com.zsf.test.branch.arouter.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.PretreatmentService;
import com.zsf.global.GlobalData;
import com.zsf.test.R;
import com.zsf.utils.ToastUtils;
import com.zsf.utils.ZsfLog;

/**
 * @author zsf
 * @date 2019/8/5
 * 预处理服务
 * 实现 PretreatmentService 接口，并加上一个Path内容任意的注解即可
 */
@Route(path = "/s1/s")
public class PretreatmentServiceImpl implements PretreatmentService {
    /**
     * 跳转前预处理，如果需要自行处理跳转，该方法返回 false 即可
     * @param context
     * @param postcard
     * @return
     */
    @Override
    public boolean onPretreatment(Context context, Postcard postcard) {
        ZsfLog.d(PretreatmentServiceImpl.class, GlobalData.getContext().getString(R.string.m_test_ARouter_Pretreatment_service));
        //需要预处理服务return false，就无法打开activity
        //不需要预处理服务return true
        if (postcard.getTag() != null){
            if (postcard.getTag().toString().equals(GlobalData.getContext().getResources().getString(R.string.m_test_ARouter_Pretreatment_sign))){
                ToastUtils.showToast(GlobalData.getContext(),GlobalData.getContext().getString(R.string.m_test_ARouter_Pretreatment_sign));
                return true;
            }
        }
        return true;
    }

    @Override
    public void init(Context context) {
        ZsfLog.d(PretreatmentServiceImpl.class, GlobalData.getContext().getString(R.string.m_test_ARouter_Pretreatment_service_init));
    }
}
