package com.zsf.fingerprint;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.zsf.wrapper.AuthFingerprintCallBack;
import com.zsf.wrapper.AuthFingerprintResult;
import static com.zsf.wrapper.ApiFingerprintWrapper.getInstance;

/**
 * @author zsf
 */
public class FingerprintActivity extends FragmentActivity implements AuthFingerprintCallBack {
    private final String TAG = FingerprintActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void initData() {
        // 判断设备是否支持
        if (!getInstance().isSupportFingerprint(this)){
            Toast.makeText(this, this.getResources().getString(R.string.client_platform_fingerprint_no_support), Toast.LENGTH_LONG).show();
            return;
        }
        // 判断是否已经设置指纹
        if (!getInstance().isSettingFingerprint(this)){
            getInstance().settingFingerprint(this, R.id.fingerprint_container);
        } else {
            getInstance().unLockFingerprint(this, R.id.fingerprint_container);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void resetFingerprint(){
        getInstance().settingFingerprint(this, R.id.fingerprint_container);
    }

    @Override
    public void onResult(AuthFingerprintResult authFingerprintResult) {
        Log.d(TAG, authFingerprintResult.toString());
        if (authFingerprintResult.getSceneType() != 0 && authFingerprintResult.getErrorCode() == 0){
            getSupportFragmentManager().beginTransaction().replace( R.id.fingerprint_container, new FingerprintContentFragment()).commitAllowingStateLoss();
        }
    }
}
