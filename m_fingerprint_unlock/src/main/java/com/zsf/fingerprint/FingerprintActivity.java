package com.zsf.fingerprint;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zsf.fingerprint.fragment.FingerprintContentFragment;
import com.zsf.fingerprint.fragment.FingerprintSettingFragment;
import com.zsf.fingerprint.fragment.FingerprintUnlockFragment;
import com.zsf.fingerprint.utils.BiometricDataUtils;
import com.zsf.fingerprint.utils.BiometricUtils;
import com.zsf.global.GlobalData;
import com.zsf.utils.ToastUtils;
import com.zsf.view.activity.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author zsf
 */
public class FingerprintActivity extends BaseActivity {

    // 指纹设置标识
    private static final int FINGERPRINT_ABOUT_SETTING = 0;
    // 指纹解锁标识
    private static final int FINGERPRINT_ABOUT_UNLOCK = 1;

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            BiometricDataUtils.getInstance().resetUnlockCount(GlobalData.getContext());
        }
    };

    public static final int SETTING_FINGERPRINT = 0;
    public static final int UNLOCK_FINGERPRINT = 1;
    public static final int CONTENT_FINGERPRINT = 2;

    Timer timer = new Timer();
    Fragment settingFragment = new FingerprintSettingFragment();
    Fragment unlockFragment = new FingerprintUnlockFragment();
    Fragment contentFragment = new FingerprintContentFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_fingerprint);
    }

    @Override
    public void initData(Activity activity) {
        if (!BiometricUtils.isSupportBiometric(this)){
            ToastUtils.showToast(this, GlobalData.getContext().getResources().getString(R.string.no_support_fingerprint));
            return;
        }
        if (BiometricUtils.isSettingFingerprint(GlobalData.getContext())){
            showFragment(UNLOCK_FINGERPRINT);
        } else {
            showFragment(SETTING_FINGERPRINT);
        }

    }

    @Override
    public void onClick(View view) {

    }

    public void showFragment(int fragmentType) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (fragmentType){
            case SETTING_FINGERPRINT:
                ft.replace(R.id.ll_content, settingFragment).commitAllowingStateLoss();
                break;
            case UNLOCK_FINGERPRINT:
                ft.replace(R.id.ll_content, unlockFragment).commitAllowingStateLoss();
                break;
            case CONTENT_FINGERPRINT:
                ft.replace(R.id.ll_content, contentFragment).commitAllowingStateLoss();
                break;
            default:
        }

    }

    public void startTimer(){
        timer.schedule(task, 60000);
    }
    public void cancelTimer(){
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}
