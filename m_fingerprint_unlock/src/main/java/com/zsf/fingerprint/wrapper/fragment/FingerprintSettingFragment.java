package com.zsf.fingerprint.wrapper.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.tencent.soter.wrapper.wrap_biometric.SoterBiometricStateCallback;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessAuthenticationResult;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessCallback;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessKeyPreparationResult;
import com.zsf.fingerprint.R;
import com.zsf.fingerprint.utils.BiometricDataUtils;
import com.zsf.fingerprint.utils.BiometricErrorCode;
import com.zsf.fingerprint.utils.BiometricUtils;
import com.zsf.fingerprint.wrapper.AuthFingerprintCallBack;
import com.zsf.fingerprint.wrapper.AuthFingerprintResult;

/**
 * @author zsf
 * @date 2019/10/14
 */
public class FingerprintSettingFragment extends Fragment implements View.OnClickListener, SoterProcessCallback<SoterProcessAuthenticationResult>, SoterBiometricStateCallback {

    private ImageView imageViewSetFingerprint;
    private static final String TAG = FingerprintSettingFragment.class.getSimpleName();
    private AlertDialog alertDialog = null;
    private View viewDialog;
    private TextView dialogTextViewContent;
    private TextView dialogTextViewButton;

    /**
     * 0:正常 1:设备无指纹 2:取消验证（仅关闭dialog） 3:取消指纹校验
     */
    private int settingType = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint_setting, container, false);
        initView(view);
        return view;
    }

    private void initData() {
        BiometricUtils.prepareSoterAuthKey(new BiometricUtils.IOnAuthKeyPreparedCallBack() {
            @Override
            public void onResult(SoterProcessKeyPreparationResult authKeyResult, boolean isSuccess) {
                if (isSuccess){
                    Log.d(TAG, "指纹 auth key 初始化成功！ info = " + authKeyResult.toString());
                    if (alertDialog != null && alertDialog.isShowing()){
                        settingType = 3;
                        BiometricUtils.startFingerprintAuthentication(getContext(), FingerprintSettingFragment.this, FingerprintSettingFragment.this, BiometricUtils.SCENE_EMM_FINGERPRINT);
                        dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_dialog_checking));
                        dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_check_cancel));
                    }
                } else {
                    Log.d(TAG, "指纹 auth key 初始化失败！info = " + authKeyResult.toString() );
                    if (alertDialog != null){
                        if (authKeyResult.errCode == BiometricErrorCode.NO_SET_DEVICE_PASSWORD){
                            settingType = 1;
                            dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_invalid_set_new));
                            dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_go_setting));
                        } else {
                            settingType = 2;
                            dialogTextViewContent.setText("初始化失败(code:" + authKeyResult.errCode + ")");
                            dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_close));
                        }
                    }
                }
            }
        });
    }

    private void initView(View view) {
        imageViewSetFingerprint = (ImageView) view.findViewById(R.id.emm_setting_fingerprint);
        imageViewSetFingerprint.setOnClickListener(this);
        viewDialog = View.inflate(getContext(), R.layout.fingerprint_dialog_layout, null);
        alertDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .create();
        dialogTextViewButton = (TextView) viewDialog.findViewById(R.id.fingerprint_dialog_button);
        dialogTextViewContent = (TextView) viewDialog.findViewById(R.id.fingerprint_dialog_content_text);
        dialogTextViewButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.emm_setting_fingerprint){
            settingFingerprint();
        } else if (v.getId() == R.id.fingerprint_dialog_button){
            if (settingType == 1){
                alertDialog.dismiss();
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            } else if (settingType == 2){
                alertDialog.dismiss();
            } else if (settingType == 3){
                alertDialog.dismiss();
                BiometricUtils.asyncCancelBiometricAuthentication();
            }
        }
    }

    private void settingFingerprint(){
        if (!BiometricUtils.isSystemHasFingerprint(getContext())){
            // 设备不存在指纹
            settingType = 1;
            dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_dialog_device_no_info));
            dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_go_setting));
        } else {
            // 开始指纹验证
            settingType = 3;
            BiometricUtils.startFingerprintAuthentication(getContext(), FingerprintSettingFragment.this, FingerprintSettingFragment.this, BiometricUtils.SCENE_EMM_FINGERPRINT);
            dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_check_cancel));
        }
        alertDialog.show();
        alertDialog.setContentView(viewDialog);
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) getContext().getResources().getDimension(R.dimen.fingerprint_dialog_width);
        params.height = (int) getContext().getResources().getDimension(R.dimen.fingerprint_dialog_height);
        alertDialog.getWindow().setAttributes(params);
    }

    /**
     * 指纹识别结果回调
     * @param result
     */
    @Override
    public void onResult(SoterProcessAuthenticationResult result) {
        if (result.isSuccess()){
            if (BiometricDataUtils.getInstance().getFingerprintId(getContext()) == null){
                BiometricDataUtils.getInstance().setIsSoterOpened(getContext(), true);
                BiometricDataUtils.getInstance().setFingerprintId(getContext(), result.getExtData().getFid());
            }
            if (result.getExtData().getFid().equals(BiometricDataUtils.getInstance().getFingerprintId(getContext()))){
                ((AuthFingerprintCallBack)getActivity()).onResult(new AuthFingerprintResult(1, result.errCode, result.errMsg));
                alertDialog.dismiss();
            }
        } else {
            Log.d(TAG, "error_code = " + result.getErrCode() + "; info = " + result.getErrMsg());
            if (result.errCode == BiometricErrorCode.ERR_AUTHKEY_NOT_FOUND || result.errCode == BiometricErrorCode.ERR_AUTHKEY_ALREADY_EXPIRED || result.errCode == BiometricErrorCode.ERR_ASK_NOT_EXIST
                    || result.errCode == BiometricErrorCode.ERR_INIT_SIGN_FAILED){
                // 这里初始化设置指纹不会存在过期的场景，不做处理
                Log.d(TAG, "指纹密钥无效！请设置指纹 errCode = " + result.errCode);
                settingType = 5;
                dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_init));
                dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_wait));
                initData();
            } else if (result.errCode == BiometricErrorCode.ERR_SIGNATURE_INVALID){
                settingType = 1;
                dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_invalid_set_new));
                dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_go_setting));
            } else if (result.errCode == BiometricErrorCode.ERR_USER_CANCELLED){
                Log.d(TAG, "指纹解锁取消！ errCode = " + result.errCode);
            } else if (result.errCode == BiometricErrorCode.ERR_FINGERPRINT_LOCKED){
                settingType = 3;
                dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_check_fail_five));
                dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_check_cancel));
            } else if (result.errCode == BiometricErrorCode.ERR_BIOMETRIC_AUTHENTICATION_FAILED){
                settingType = 3;
                dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_error));
                dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_close));
            } else {
                dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_check_fail));
            }
        }
    }


    /**
     * start 指纹验证程序的回调。注意，不要直接依赖回调来做逻辑，应该只使用它刷新UI组件
     */
    @Override
    public void onStartAuthentication() {
        Log.d(TAG, "指纹开始认证！");
        dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_dialog_checking));
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        Log.d(TAG, "onAuthenticationHelp： helpCode = " + helpCode + "; helpString = " + helpString);
    }

    @Override
    public void onAuthenticationSucceed() {
        Log.d(TAG, "指纹认证成功！");
    }

    @Override
    public void onAuthenticationFailed() {
        Log.d(TAG, "onAuthenticationFailed");
        settingType = 1;
        dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_invalid));
        dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_go_setting));
        BiometricUtils.asyncCancelBiometricAuthentication();
    }

    @Override
    public void onAuthenticationCancelled() {
        Log.d(TAG, "当用户取消身份验证时回调");
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errorString) {
        Log.d(TAG, "当传感器指示此身份验证事件为不可恢复的错误时回调，例如，验证密钥永久无效 errorCode = " + errorCode + "; errorString = " + errorString);
    }
    // end 指纹验证程序的回调。注意，不要直接依赖回调来做逻辑，应该只使用它刷新UI组件


    @Override
    public void onDestroy() {
        alertDialog.dismiss();
        super.onDestroy();
    }

}
