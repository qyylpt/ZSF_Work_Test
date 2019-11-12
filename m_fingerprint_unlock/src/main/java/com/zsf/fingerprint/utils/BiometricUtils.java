package com.zsf.fingerprint.utils;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.util.Log;
import androidx.annotation.NonNull;
import com.tencent.soter.core.SoterCore;
import com.tencent.soter.core.model.ConstantsSoter;
import com.tencent.soter.wrapper.SoterWrapperApi;
import com.tencent.soter.wrapper.wrap_biometric.SoterBiometricCanceller;
import com.tencent.soter.wrapper.wrap_biometric.SoterBiometricStateCallback;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessAuthenticationResult;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessCallback;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessKeyPreparationResult;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessNoExtResult;
import com.tencent.soter.wrapper.wrap_core.SoterProcessErrCode;
import com.tencent.soter.wrapper.wrap_task.AuthenticationParam;
import com.tencent.soter.wrapper.wrap_task.InitializeParam;
import com.zsf.global.GlobalData;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author zsf
 * @date 2019/10/10
 */
public class BiometricUtils {

    /**
     * EMM指纹场景标识
     */
    public static final int SCENE_EMM_FINGERPRINT = 0;
    private static SoterBiometricCanceller mCanceller;
    private static String TAG = BiometricUtils.class.getSimpleName();


    /**
     * 指纹功能初始化,建议在Application onCreate中进行初始化操作
     * @param context 应用上下文
     * @param callback 初始化回调.
     * @param count 解锁失败限制次数
     */
    public static void initFingerprintSupport(Context context, SoterProcessCallback<SoterProcessNoExtResult> callback, int count){
        BiometricDataUtils.getInstance().init(context);
        InitializeParam param = new InitializeParam.InitializeParamBuilder()
                .setScenes(SCENE_EMM_FINGERPRINT)
                .build();
        SoterWrapperApi.init(context, callback, param);
    }

    /**
     * 准备AuthKey
     * 调用时机: 在指纹认证之前调用
     * @param iOnAuthKeyPreparedCallBack AuthKey 获取获取监听回调
     */
    public static void prepareSoterAuthKey(final IOnAuthKeyPreparedCallBack iOnAuthKeyPreparedCallBack){
        SoterWrapperApi.prepareAuthKey(new SoterProcessCallback<SoterProcessKeyPreparationResult>(){

            @Override
            public void onResult(@NonNull SoterProcessKeyPreparationResult result) {
                if (result.errCode == SoterProcessErrCode.ERR_OK){
                    iOnAuthKeyPreparedCallBack.onResult(result, true);
                } else {
                    iOnAuthKeyPreparedCallBack.onResult(result, false);
                }
            }
        }, false, true, SCENE_EMM_FINGERPRINT, null, null);
    }


    /**
     * 开始指纹识别
     * @param context
     * @param processCallback 指纹认证结果回调
     * @param soterBiometricStateCallback  指纹验证程序的回调. 注意,不要直接依赖回调来做逻辑,应该只使用它刷新UI组件
     * @param sceneType 场景标识
     */
    public static void startFingerprintAuthentication(Context context, SoterProcessCallback<SoterProcessAuthenticationResult> processCallback, SoterBiometricStateCallback soterBiometricStateCallback, int sceneType){
        if (mCanceller != null) {
            mCanceller = null;
        }
        mCanceller = new SoterBiometricCanceller();
        // 通过Builder来构建认证请求
        AuthenticationParam param =  new AuthenticationParam.AuthenticationParamBuilder()
                // 指定需要认证的场景。必须在init中初始化。必填
                .setScene(sceneType)
                .setBiometricType(ConstantsSoter.FINGERPRINT_AUTH)
                // 指定当前上下文。必填。
                .setContext(context)
                // 指定当前用于控制指纹取消的控制器。当因为用户退出界面或者进行其他可能引起取消的操作时，需要开发者通过该控制器取消指纹授权。建议必填。
                .setSoterBiometricCanceller(mCanceller)
                // 如果之前已经通过其他方式获取了挑战因子，则设置此字段。如果设置了该字段，则忽略获取挑战因子网络封装结构体的设置。如果两个方法都没有调用，则会引起错误。
                .setPrefilledChallenge("prefilled challenge")
                // 指纹回调仅仅用来更新UI相关，不建议在指纹回调中进行任何业务操作。选填。
                .setSoterBiometricStateCallback(soterBiometricStateCallback).build();
        SoterWrapperApi.requestAuthorizeAndSign(processCallback, param);
    }

    /**
     * 异步取消指纹验证
     */
    public static void asyncCancelBiometricAuthentication(){
        if(mCanceller != null){
            mCanceller.asyncCancelBiometricAuthentication();
        }
    }

    /**
     * 擦除指纹设置信息
     * @param context
     */
    public static void wipeFingerprintData(Context context){
        BiometricDataUtils.getInstance().setIsSoterOpened(context, false);
        BiometricDataUtils.getInstance().setFingerprintId(context, null);
    }

    /**
     * 系统是否存在指纹信息,在非强管控情况下可以获取正确的信息
     * 在强管控下清除设备密码目前测试存在三种情况：
     *      1.设备密码清除,同时可以清除设备生物识别信息(指纹 or 人脸)
     *      2.设备密码清除,设备密码锁关闭,指纹信息存在并且可以检测到,应用中的指纹信息仍然可以使用
     *      3.设备密码清除,设备密码锁关闭,指纹信息存在但是不可以检测到,应用中指纹信息校验失败(需要引导用户激活指纹信息,打开密码锁并且操作新的指纹录入,之前存在的指纹也会激活)
     * @param context
     * @return
     */
    public static boolean isSystemHasFingerprint(Context context){
        return SoterCore.isSystemHasBiometric(context, ConstantsSoter.FINGERPRINT_AUTH);
    }

    /**
     * 设备是否支持指纹
     * @param context
     * @return
     */
    public static boolean isSupportBiometric(Context context){
        return SoterCore.isSupportBiometric(context, ConstantsSoter.FINGERPRINT_AUTH);
    }

    /**
     * AuthKey 获取获取监听回调
     */
    public interface IOnAuthKeyPreparedCallBack {
        /**
         * 回调结果
         * @param authKeyResult
         * @param isSuccess true:成功 false:失败
         */
        void onResult(SoterProcessKeyPreparationResult authKeyResult, boolean isSuccess);
    }

    /**
     * 是否已经应用指纹
     * @return
     */
    public static boolean isSettingFingerprint(Context context){
        if (BiometricDataUtils.getInstance().getIsSoterOpened(context)){
            return true;
        }
        return false;
    }



    /**
     * 获取指纹ID,兼容性存在问题.
     * @return
     */
    private String getFingerprintInfo(Context context){
        try {
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            Method method = FingerprintManager.class.getDeclaredMethod("getEnrolledFingerprints");
            Object obj = method.invoke(fingerprintManager);
            StringBuilder stringBuilder = new StringBuilder();
            if (obj != null) {
                Class<?> clazz = Class.forName("android.hardware.fingerprint.Fingerprint");
                Method getName = clazz.getDeclaredMethod("getName");
                Method getFingerId = clazz.getDeclaredMethod("getFingerId");
                Method getGroupId = clazz.getDeclaredMethod("getGroupId");
                Method getDeviceId = clazz.getDeclaredMethod("getDeviceId");
                for (int i = 0; i < ((List) obj).size(); i++) {
                    Object item = ((List) obj).get(i);
                    if (null == item) {
                        continue;
                    }
                    Log.e(TAG,
                            "指纹name:" + getName.invoke(item)+
                                    " 指纹库ID:"+getGroupId.invoke(item)+
                                    "指纹ID:"+getFingerId.invoke(item)+
                                    "设备Id:"+getDeviceId.invoke(item));
                    stringBuilder.append("指纹name:" + getName.invoke(item) + "; 指纹库ID:" + getGroupId.invoke(item) + "; 指纹ID:" + getFingerId.invoke(item) + "; 设备Id: " + getDeviceId.invoke(item) + "\n");
                }
            }
            if (stringBuilder.toString().equals("")){
                return "无法获取设备指纹信息或者设备不存在指纹信息";
            }
            return stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "无法获取设备指纹信息或者设备不存在指纹信息";
    }
}
