package com.zsf.fingerprint;

import android.util.Log;
import com.zsf.application.BaseCommonApplication;
import com.zsf.wrapper.ApiFingerprintWrapper;
import com.zsf.wrapper.AuthFingerprintCallBack;
import com.zsf.wrapper.AuthFingerprintResult;

/**
 * @author zsf
 * @date 2019/11/11
 * @Usage
 */
public class ClientApplication extends BaseCommonApplication {
    private final String TAG = ClientApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        ApiFingerprintWrapper.getInstance().initFingerprint(this, new AuthFingerprintCallBack() {
            @Override
            public void onResult(AuthFingerprintResult authFingerprintResult) {
                Log.d(TAG, "指纹初始化结果：" + authFingerprintResult.toString());
            }
        });
    }
}
