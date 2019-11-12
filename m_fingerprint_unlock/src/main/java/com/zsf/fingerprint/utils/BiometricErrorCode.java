package com.zsf.fingerprint.utils;

import com.tencent.soter.wrapper.wrap_core.SoterProcessErrCode;

/**
 * @author zsf
 * @date 2019/11/11
 * @Usage 针对目前已经在设备上验证过的失败情景,这里定义的值是对 腾讯生物识别SDK 失败状态的验证解析
 *        后期如果在其他设备或场景中如果有新的情况,可以在这里对使用情景进行解释
 */
public class BiometricErrorCode {

    /**
     * 强管控下：在擦除设备之后 -> 设备密码清除成功,设备指纹存在 但是 sdk （初始化）无法识别设备已存在指纹
     */
    public static final int NO_SET_DEVICE_PASSWORD = SoterProcessErrCode.ERR_AUTH_KEY_GEN_FAILED;

    /**
     * 找不到auth key: 需要做初始化操作
     */
    public static final int ERR_AUTHKEY_NOT_FOUND =  SoterProcessErrCode.ERR_AUTHKEY_NOT_FOUND;

    /**
     * key 过期: 需要做初始化操作
     */
    public static final int ERR_AUTHKEY_ALREADY_EXPIRED = SoterProcessErrCode.ERR_AUTHKEY_ALREADY_EXPIRED;

    /**
     * 秘钥不存在: 需要做初始化操作
     */
    public static final int ERR_ASK_NOT_EXIST = SoterProcessErrCode.ERR_ASK_NOT_EXIST;

    /**
     * 准备签名对象失败: 需要做初始化操作
     */
    public static final int ERR_INIT_SIGN_FAILED = SoterProcessErrCode.ERR_INIT_SIGN_FAILED;

    /**
     * 在强管控下: 擦出设备 -> 设备用户密码清除,部分机型即使打开指纹锁并且存在之前指纹,依然无法通过指纹验证,需要引导用户增删新的指纹激活sdk对用户校验
     */
    public static final int ERR_SIGNATURE_INVALID = SoterProcessErrCode.ERR_SIGNATURE_INVALID;

    /**
     * 取消指纹验证
     */
    public static final int ERR_USER_CANCELLED = SoterProcessErrCode.ERR_USER_CANCELLED;

    /**
     * 解锁失败次数过多,设备会暂停一段时间限制解锁.(时间会根据不同设备有不同的ERR信息提示,禁止解锁时间也有不同的时间限制)
     */
    public static final int ERR_FINGERPRINT_LOCKED = SoterProcessErrCode.ERR_FINGERPRINT_LOCKED;

    /**
     * 解锁失败次数过多,部分设备会直接禁止使用指纹锁，包括禁止解锁设备都会禁用.(支付宝也会存在同样的问题),VIVO IQOO 需要重启设备恢复解锁限制
     */
    public static final int ERR_BIOMETRIC_AUTHENTICATION_FAILED = SoterProcessErrCode.ERR_BIOMETRIC_AUTHENTICATION_FAILED;


}
