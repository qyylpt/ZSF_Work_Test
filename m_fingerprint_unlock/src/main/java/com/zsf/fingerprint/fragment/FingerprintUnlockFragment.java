package com.zsf.fingerprint.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tencent.soter.wrapper.wrap_biometric.SoterBiometricStateCallback;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessAuthenticationResult;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessCallback;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessKeyPreparationResult;
import com.tencent.soter.wrapper.wrap_core.SoterProcessErrCode;
import com.zsf.fingerprint.FingerprintActivity;
import com.zsf.fingerprint.R;
import com.zsf.fingerprint.utils.BiometricDataUtils;
import com.zsf.fingerprint.utils.BiometricErrorCode;
import com.zsf.fingerprint.utils.BiometricUtils;
import com.zsf.global.GlobalData;

/**
 * @author zsf
 * @date 2019/10/14
 */
public class FingerprintUnlockFragment extends Fragment implements View.OnClickListener, SoterProcessCallback<SoterProcessAuthenticationResult>, SoterBiometricStateCallback {

    private Button imageViewUnlockFingerprint;
    private static final String TAG = FingerprintUnlockFragment.class.getSimpleName();
    private AlertDialog alertDialog = null;
    private View viewDialog;
    private TextView dialogTextViewContent;
    private TextView dialogTextViewButton;
    /**
     * 0:正常 1:设备无指纹 2:无网络 3:取消指纹校验
     */
    private int settingType = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint_open, container, false);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        BiometricUtils.prepareSoterAuthKey(new BiometricUtils.IOnAuthKeyPreparedCallBack() {
            @Override
            public void onResult(SoterProcessKeyPreparationResult authKeyResult, boolean isSuccess) {
                if (isSuccess){
                    Log.d(TAG, "指纹 auth key 初始化成功！ info = " + authKeyResult.toString());
                    if (alertDialog != null){
                        alertDialog.dismiss();
                    }
                } else {
                    Log.d(TAG, "指纹 auth key 初始化失败！info = " + authKeyResult.toString() );
                    if (alertDialog != null){
                        settingType = 2;
                        dialogTextViewContent.setText("初始化失败(code:" + authKeyResult.errCode);
                        dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_close));
                    }
                }
            }
        });
    }


    private void initView(View view) {
        imageViewUnlockFingerprint = (Button) view.findViewById(R.id.emm_unlock_fingerprint);
        imageViewUnlockFingerprint.setOnClickListener(this);
        LayoutInflater inflater = LayoutInflater.from(GlobalData.getContext());
        viewDialog = inflater.inflate(R.layout.fingerprint_dialog_layout, null);
        alertDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .create();
        dialogTextViewButton = (TextView) viewDialog.findViewById(R.id.fingerprint_dialog_button);
        dialogTextViewContent = (TextView) viewDialog.findViewById(R.id.fingerprint_dialog_content_text);
        dialogTextViewButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.emm_unlock_fingerprint){
            fingerprintUnlock();
        } else if (v.getId() == R.id.fingerprint_dialog_button){
            if (settingType == 0){
                BiometricUtils.startFingerprintAuthentication(getContext(), FingerprintUnlockFragment.this, FingerprintUnlockFragment.this, BiometricUtils.SCENE_EMM_FINGERPRINT);
                dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_check_cancel));
                settingType = 3;
            } else if (settingType == 1){
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

    private void fingerprintUnlock(){
        if (!BiometricUtils.isSystemHasFingerprint(getContext())){
            // 设备不存在指纹
            settingType = 2;
            dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_delete));
            dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_close));
        } else {
            if (BiometricDataUtils.getInstance().getUnlockCount(getContext()) <= 0){
                settingType = 2;
                dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_unlock_fail_five));
                dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_close));
            } else {
                settingType = 3;
                dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_dialog_open));
                dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_check_cancel));
                BiometricUtils.startFingerprintAuthentication(getContext(), FingerprintUnlockFragment.this, FingerprintUnlockFragment.this, BiometricUtils.SCENE_EMM_FINGERPRINT);
            }
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


    // start 指纹验证程序的回调。注意，不要直接依赖回调来做逻辑，应该只使用它刷新UI组件
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
        BiometricDataUtils.getInstance().setUnlockCount(getContext(), BiometricDataUtils.getInstance().getUnlockCount(getContext()) - 1);
        unLockFailed();
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
    public void onResult(@NonNull SoterProcessAuthenticationResult result) {
        if (result.isSuccess()){
            settingType = 0;
            BiometricDataUtils.getInstance().resetUnlockCount(GlobalData.getContext());
            if (result.getExtData().getFid().equals(BiometricDataUtils.getInstance().getFingerprintId(getContext()))){
                ((FingerprintActivity)getActivity()).showFragment(FingerprintActivity.CONTENT_FINGERPRINT);
            } else {
                dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_dialog_error_finger));
                dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_retry));
            }
        } else {
            Log.d(TAG, "error_code = " + result.getErrCode() + "; info = " + result.getErrMsg());
            if (result.errCode == BiometricErrorCode.ERR_AUTHKEY_NOT_FOUND || result.errCode == BiometricErrorCode.ERR_AUTHKEY_ALREADY_EXPIRED || result.errCode == BiometricErrorCode.ERR_ASK_NOT_EXIST
                    || result.errCode == BiometricErrorCode.ERR_SIGNATURE_INVALID || result.errCode == BiometricErrorCode.ERR_INIT_SIGN_FAILED){
                settingType = 5;
                dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_init));
                dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_wait));
                initData();
            } else if (result.errCode == BiometricErrorCode.ERR_USER_CANCELLED){

            } else if (result.errCode == BiometricErrorCode.ERR_BIOMETRIC_AUTHENTICATION_FAILED) {
                BiometricDataUtils.getInstance().setUnlockCount(getContext(), 5);
                dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_error));
                dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_close));
            } else if (result.errCode == BiometricErrorCode.ERR_FINGERPRINT_LOCKED){
                BiometricDataUtils.getInstance().setUnlockCount(getContext(), 5);
                dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_unlock_fail_five));
                dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_close));
            } else {
                BiometricDataUtils.getInstance().setUnlockCount(getContext(), BiometricDataUtils.getInstance().getUnlockCount(getContext()) - 1);
                unLockFailed();
            }
        }
    }


    @Override
    public void onDestroy() {
        alertDialog.dismiss();
        super.onDestroy();
    }



    private void unLockFailed(){
        if (BiometricDataUtils.getInstance().getUnlockCount(getContext()) <= 0){
            dialogTextViewContent.setText(getContext().getResources().getString(R.string.fingerprint_unlock_fail_five));
            dialogTextViewButton.setText(getContext().getResources().getString(R.string.fingerprint_dialog_close));
            BiometricUtils.asyncCancelBiometricAuthentication();
            if (BiometricDataUtils.getInstance().getUnlockCount(getContext()) < 0){
                return;
            }
            ((FingerprintActivity)getActivity()).startTimer();
//            Scheduler.getDefault().dispatchDelayAsync(new Runnable() {
//                @Override
//                public void run() {
//                    BiometricDataUtils.getInstance().setUnlockCount(getContext(),5);
//                }
//            }, 60000);
        } else {
            dialogTextViewContent.setText("指纹验证失败，您还可以尝试" + BiometricDataUtils.getInstance().getUnlockCount(getContext()) + "次");
        }
    }
}
