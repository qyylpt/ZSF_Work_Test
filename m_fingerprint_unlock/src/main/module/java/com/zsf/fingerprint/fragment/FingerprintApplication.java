package com.zsf.fingerprint.fragment;

import android.app.Application;
import androidx.annotation.NonNull;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessCallback;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessNoExtResult;
import com.zsf.application.BaseCommonApplication;
import com.zsf.fingerprint.utils.BiometricUtils;
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
        BiometricUtils.initFingerprintSupport(this, new SoterProcessCallback<SoterProcessNoExtResult>() {
            @Override
            public void onResult(@NonNull SoterProcessNoExtResult result) {
                ZsfLog.d(FingerprintApplication.class, "指纹初始化：" + result.toString());
            }
        }, 5);
    }
}
