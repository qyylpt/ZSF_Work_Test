package com.zsf.wrapper;

import android.content.Context;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessCallback;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessNoExtResult;
import com.zsf.utils.BiometricDataUtils;
import com.zsf.utils.BiometricUtils;
import com.zsf.wrapper.fragment.FingerprintSettingFragment;
import com.zsf.wrapper.fragment.FingerprintUnlockFragment;

/**
 * @author zsf
 * @date 2019/11/13
 * @Usage
 */
public class ApiFingerprintWrapper {
    private final String TAG = ApiFingerprintWrapper.class.getSimpleName();

    private volatile static ApiFingerprintWrapper apiFingerprintWrapper;

    public static ApiFingerprintWrapper getInstance(){
        if (apiFingerprintWrapper == null){
            synchronized (ApiFingerprintWrapper.class){
                if (apiFingerprintWrapper == null){
                    apiFingerprintWrapper = new ApiFingerprintWrapper();
                }
            }
        }
        return apiFingerprintWrapper;
    }

    /**
     * 初始化
     * @param context
     * @param authFingerprintCallBack   初始化回调
     */
    public void initFingerprint(Context context, final AuthFingerprintCallBack authFingerprintCallBack){
        BiometricUtils.initFingerprintSupport(context, new SoterProcessCallback<SoterProcessNoExtResult>() {
            @Override
            public void onResult(SoterProcessNoExtResult result) {
                Log.d(TAG, "指纹初始化：" + result.toString());
                authFingerprintCallBack.onResult(new AuthFingerprintResult(0, result.errCode, result.errMsg));
            }
        });
    }

    /**
     * 检查设备是否支持指纹解锁
     * @param context
     * @return
     */
    public boolean isSupportFingerprint(Context context){
        return BiometricUtils.isSupportBiometric(context);
    }

    /**
     * 是否已经设置指纹
     * @param context
     * @return
     */
    public boolean isSettingFingerprint(Context context){
        return BiometricUtils.isSettingFingerprint(context);
    }

    /**
     * 设置指纹
     * @param fragmentActivity   FragmentActivity
     * @param containerId   fragment显示容器
     */
    public void settingFingerprint(FragmentActivity fragmentActivity, int containerId){
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerId, new FingerprintSettingFragment()).commitAllowingStateLoss();
    }

    /**
     * 解锁
     * @param fragmentActivity   FragmentActivity
     * @param containerId   fragment显示容器
     */
    public void unLockFingerprint(FragmentActivity fragmentActivity, int containerId){
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerId, new FingerprintUnlockFragment()).commitAllowingStateLoss();
    }

    /**
     * 擦除指纹信息
     * @param context
     */
    public void wipeFingerprint(Context context){
        BiometricDataUtils.getInstance().releaseFingerprintCache(context);
    }

}
