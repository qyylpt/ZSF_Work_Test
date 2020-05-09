package com.zsf.fingerprint.wrapper;

/**
 * @author zsf
 * @date 2019/11/13
 * @Usage
 */
public interface AuthFingerprintCallBack {

    /**
     * 初始化 or 指纹设置 or 解锁结果
     * @param authFingerprintResult
     */
    void onResult(AuthFingerprintResult authFingerprintResult);
}
