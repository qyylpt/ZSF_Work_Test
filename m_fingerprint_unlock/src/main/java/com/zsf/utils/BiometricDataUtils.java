package com.zsf.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author zsf
 * @date 2019/10/12
 */
public class BiometricDataUtils {

    /**
     * 偏好设置标识
     */
    private static final String SOTER_CACHE_SP = "soter_disk_cache_sp";
    private static final String KEY_IS_FINGERPRINT = "isFingerprintOpened";


    /**
     * 保存指纹ID
     */
    private static final String KEY_FINGERPRINT_ID_SP = "key_disk_fingerprint_id_cache";
    private static final String KEY_FINGERPRINT_ID = "key_fingerprint_id";

    /**
     * 保存指纹解锁阈值
     */
    private static final String UNLOCK_CACHE_SP = "unlock_cache_sp";
    private static final String KEY_UNLOCK_COUNT = "key_desk_unlock_info";
    private static final String KEY_DELAY_ALLOW_UNLOCK_TIME = "delay_allow_unlock_time";

    private static int DEFAULT_UNLOCK_COUNT = 5;
    private static long DELAY_LOCK_TIME = 60000;

    private static BiometricDataUtils sInstance;


    public static BiometricDataUtils getInstance(){
        if (sInstance == null){
            synchronized (BiometricDataUtils.class){
                if (sInstance == null){
                    sInstance = new BiometricDataUtils();
                }
                return sInstance;
            }
        } else {
            return sInstance;
        }
    }

    /**
     * 获取默认失败次数控制
     * @return
     */
    public int getUnlockCount(){
        return DEFAULT_UNLOCK_COUNT;
    }

    public long getDefaultDelayUnlockTime(){
        return DELAY_LOCK_TIME;
    }

    /**
     *  初始化指纹功能开启标记
     * @param context
     */
    public void init(Context context){
        SharedPreferences emmDataPreference = context.getSharedPreferences(SOTER_CACHE_SP, Context.MODE_PRIVATE);
        SharedPreferences unLockDataPreference = context.getSharedPreferences(UNLOCK_CACHE_SP, Context.MODE_PRIVATE);
        unLockDataPreference.edit().putInt(KEY_UNLOCK_COUNT, DEFAULT_UNLOCK_COUNT).apply();
        unLockDataPreference.edit().putInt(KEY_DELAY_ALLOW_UNLOCK_TIME, 0).apply();
    }

    /**
     * 设置生物识别功能开启标记
     * @param context
     * @param isOpened 开启标记
     */
    public void setIsSoterOpened(Context context, boolean isOpened){
        SharedPreferences emmDataPreference = context.getSharedPreferences(SOTER_CACHE_SP, Context.MODE_PRIVATE);
        emmDataPreference.edit().putBoolean(KEY_IS_FINGERPRINT, isOpened).commit();
    }

    /**
     * 获取当前生物识别功能是否开启
     * @return
     */
    public boolean getIsSoterOpened(Context context){
        SharedPreferences emmDataPreference = context.getSharedPreferences(SOTER_CACHE_SP, Context.MODE_PRIVATE);
        return emmDataPreference.getBoolean(KEY_IS_FINGERPRINT, false);
    }

    /**
     * 设置应用指纹锁ID
     * @param context
     * @param fingerprintId
     */
    public void setFingerprintId(Context context, String fingerprintId){
        SharedPreferences emmDataPreference = context.getSharedPreferences(KEY_FINGERPRINT_ID_SP, Context.MODE_PRIVATE);
        emmDataPreference.edit().putString(KEY_FINGERPRINT_ID, fingerprintId).apply();
    }

    /**
     * 获取应用指纹锁ID
     * @param context
     * @return
     */
    public String getFingerprintId(Context context){
        SharedPreferences emmDataPreference = context.getSharedPreferences(KEY_FINGERPRINT_ID_SP, Context.MODE_PRIVATE);
        return emmDataPreference.getString(KEY_FINGERPRINT_ID, null);
    }

    /**
     * 获取当前解锁失败次数
     * @param context
     * @return
     */
    public int getUnlockCount(Context context){
        SharedPreferences emmDataPreference = context.getSharedPreferences(UNLOCK_CACHE_SP, Context.MODE_PRIVATE);
        return emmDataPreference.getInt(KEY_UNLOCK_COUNT, 0);
    }

    /**
     * 设置当前解锁失败剩余次数
     * @param context
     * @return
     */
    public void setUnlockCount(Context context, int count){
        SharedPreferences emmDataPreference = context.getSharedPreferences(UNLOCK_CACHE_SP, Context.MODE_PRIVATE);
        emmDataPreference.edit().putInt(KEY_UNLOCK_COUNT, count).apply();
    }

    /**
     * 重置指纹解锁次数
     * @param context
     */
    public void resetUnlockCount(Context context){
        SharedPreferences emmDataPreference = context.getSharedPreferences(UNLOCK_CACHE_SP, Context.MODE_PRIVATE);
        emmDataPreference.edit().putInt(KEY_UNLOCK_COUNT, DEFAULT_UNLOCK_COUNT).apply();
        emmDataPreference.edit().putInt(KEY_DELAY_ALLOW_UNLOCK_TIME, 0).apply();
    }

    /**
     * 设置触发解锁失败阈值时间
     * @param context
     * @param time
     */
    public void setUnlcokDelayTime(Context context, long time){
        SharedPreferences emmDataPreference = context.getSharedPreferences(UNLOCK_CACHE_SP, Context.MODE_PRIVATE);
        emmDataPreference.edit().putLong(KEY_DELAY_ALLOW_UNLOCK_TIME, time);
    }

    /**
     * 获取触发解锁失败阈值时间
     * @param context
     */
    public long getUnlcokDelayTime(Context context){
        SharedPreferences emmDataPreference = context.getSharedPreferences(UNLOCK_CACHE_SP, Context.MODE_PRIVATE);
        return emmDataPreference.getLong(KEY_DELAY_ALLOW_UNLOCK_TIME, 0);
    }

    /**
     * 重置生物识别缓存信息
     * @param context
     */
    public void releaseFingerprintCache(Context context){
        // 由于init方法只有在应用启动的时候初始化，所以这里直接重置生物识别功能打开标记
        setIsSoterOpened(context, false);
        // 重置生物识别功能是否打开
        SharedPreferences emmDataPreference = context.getSharedPreferences(SOTER_CACHE_SP, Context.MODE_PRIVATE);
        emmDataPreference.edit().putBoolean(KEY_IS_FINGERPRINT, false).commit();
        // 重置当前失败次数
        resetUnlockCount(context);
        // 重置当前保存的指纹id
        SharedPreferences idDataPreference = context.getSharedPreferences(KEY_FINGERPRINT_ID_SP, Context.MODE_PRIVATE);
        idDataPreference.edit().putString(KEY_FINGERPRINT_ID, null).commit();

    }

}
