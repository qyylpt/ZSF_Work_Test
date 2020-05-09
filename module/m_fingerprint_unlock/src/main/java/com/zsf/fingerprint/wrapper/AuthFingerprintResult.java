package com.zsf.fingerprint.wrapper;


/**
 * @author zsf
 * @date 2019/11/13
 * @Usage
 */
public class AuthFingerprintResult {
    private int errorCode;
    private String errorMsg;
    /**
     * 场景区分：0 初始化 1 设置 2 解锁
     */
    private int sceneType;


    public int getSceneType() {
        return sceneType;
    }

    public void setSceneType(int sceneType) {
        this.sceneType = sceneType;
    }

    public AuthFingerprintResult(int sceneType, int errorCode, String errorMsg) {
        this.sceneType = sceneType;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        String sceneDes = "初始化";
        if (getSceneType() == 1){
            sceneDes = "指纹设置";
        } else {
            sceneDes = "指纹解锁";
        }
        return "AuthFingerprintResult{" +
                "sceneDes='" + sceneDes + '\'' +
                "sceneType='" + sceneType + '\'' +
                "errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
