package com.zsf.fingerprint.m_fingerprint_unlock;

import com.zsf.application.BaseCommonApplication;
import com.zsf.fingerprint.wrapper.ApiFingerprintWrapper;
import com.zsf.fingerprint.wrapper.AuthFingerprintCallBack;
import com.zsf.fingerprint.wrapper.AuthFingerprintResult;
import com.zsf.utils.ZsfLog;

/**
 * @author zsf
 * @date 2019/11/11
 * @Usage
 */
public class FingerprintApplication extends BaseCommonApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiFingerprintWrapper.getInstance().initFingerprint(this, new AuthFingerprintCallBack() {
            @Override
            public void onResult(AuthFingerprintResult authFingerprintResult) {
                ZsfLog.d(FingerprintApplication.class, "指纹初始化结果：" + authFingerprintResult.toString());
            }
        });
    }
}
