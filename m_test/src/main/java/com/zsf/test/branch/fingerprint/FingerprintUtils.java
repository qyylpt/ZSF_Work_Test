package com.zsf.test.branch.fingerprint;

import android.app.KeyguardManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

/**
 * @author zsf
 * @date 2019/10/9
 */
public class FingerprintUtils {
    /**
     * 指纹操作回调
     */
    public interface OnCallBackListenr {
        /**
         * 不支持指纹识别
         */
        void onNotSupport();

        /**
         * 没有开启密码保护
         */
        void onNotSecured();

        /**
         * 未注册指纹
         */
        void onNotEnrollFingerprints();

        /**
         * 开始识别
         */
        void onAuthenticationStart();


        void onAuthenticationError(int errMsgId, CharSequence errString);

        void onAuthenticationFailed();

        void onAuthenticationHelp(int helpMsgId, CharSequence helpString);

        /**
         * 识别成功
         * @param result An object containing authentication-related data
         */
        void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result);
    }

    private static CancellationSignal cancellationSignal;

    /**
     * 开始调用指纹功能
     *
     * @param listener 指纹操作回调
     */
    public static void callFingerPrint(@NonNull final OnCallBackListenr listener, Context context) {
        FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(context);
        //判断设备是否支持
        if (!managerCompat.isHardwareDetected()) {
            listener.onNotSupport();
            return;
        }

        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        //判断设备是否处于安全保护中
        if (!keyguardManager.isKeyguardSecure()) {
            listener.onNotSecured();
            return;
        }

        //判断设备是否已经注册过指纹
        if (!managerCompat.hasEnrolledFingerprints()) {
            listener.onNotEnrollFingerprints();
            return;
        }

        //开始指纹识别
        listener.onAuthenticationStart();
        cancellationSignal = new CancellationSignal();
        managerCompat.authenticate(null, 0, cancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
            // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息，比如华为的提示就是：尝试次数过多，请稍后再试。
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                listener.onAuthenticationError(errMsgId, errString);
            }

            // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
            @Override
            public void onAuthenticationFailed() {
                listener.onAuthenticationFailed();
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                listener.onAuthenticationHelp(helpMsgId, helpString);
            }

            // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                listener.onAuthenticationSucceeded(result);
            }
        }, null);
    }

    /**
     * 退出时调用
     */
    public static void cancel() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }
}
