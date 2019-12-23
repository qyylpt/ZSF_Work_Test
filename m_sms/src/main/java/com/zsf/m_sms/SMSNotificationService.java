package com.zsf.m_sms;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.zsf.global.GlobalData;
import com.zsf.utils.RomUtils;
import com.zsf.utils.ZsfLog;
/**
 * @author zsf
 * @date 2019/12/9
 * @Usage
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
@SuppressLint("OverrideAbstract")
public class SMSNotificationService extends NotificationListenerService {
    /**
     * false: 一条信息
     * true: 四条信息(两条群组,两条真实信息)
     */
    private boolean xiaomiSign = false;
    /**
     * 四条信息上报标记：false:未上报 true:已上报
     */
    private boolean xiaomiIsReort = false;
    private boolean honorSign = false;
    public SMSNotificationService() {
        ZsfLog.d(SMSNotificationService.class, "初始化");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        getNotificationInfo(sbn, 0);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        getNotificationInfo(sbn, 1);
    }

    private void getNotificationInfo(StatusBarNotification statusBarNotification, int type){
        if (type == 1){
            return;
        }
        // SMS应用过滤
        if (romSMSPackageAdaptive(statusBarNotification)){
            return;
        }
        Notification notification = statusBarNotification.getNotification();
        if (notification == null){
            return;
        }
        // Rom适配
        if (romNotificationInfoAdaptive(statusBarNotification)){
            return;
        }
        // 获取通知信息
        getNotificationInfo(statusBarNotification);
    }

    /**
     * 获取过滤后的通知信息
     * @param statusBarNotification
     */
    public void getNotificationInfo(StatusBarNotification statusBarNotification){

        Bundle bundle = statusBarNotification.getNotification().extras;
        ZsfLog.d(SMSNotificationService.class, "getGroupKey:" + statusBarNotification.getGroupKey() + "; getKey: " + statusBarNotification.getKey() + "; getTag" + statusBarNotification.getTag() + "; getId" + statusBarNotification.getId());
        ZsfLog.d(SMSNotificationService.class, "bundle = " + bundle.toString());
        // 通知title
        Object titleObject = bundle.get(Notification.EXTRA_TITLE);
        if (titleObject == null){
            return;
        }
        String title = titleObject.toString();
        if (TextUtils.isEmpty(title)){
            return;
        }

        // 通知内容
        Object contentObject = bundle.get(Notification.EXTRA_TEXT);
        if (contentObject == null){
            return;
        }
        String content = contentObject.toString();
        if (TextUtils.isEmpty(content)){
            return;
        }
        ZsfLog.d(SMSNotificationService.class, "原始数据： " + content);
        // 适配 内容包含发送号码
        Intent intent = new Intent("notification_sms");
        intent.putExtra("notification_msg", new SMS(title, String.valueOf(statusBarNotification.getPostTime()), content, ""));
        LocalBroadcastManager.getInstance(GlobalData.getContext()).sendBroadcast(intent);
        // 需要根据 不同机型 适配 内容截取
        ZsfLog.d(SMSNotificationService.class, " 包名 = " + statusBarNotification.getPackageName() + "; 标题: = " + bundle.getString(Notification.EXTRA_TITLE, "") + "; 内容 = " + content);

        // 设置过滤标记
        honorSign = true;
    }

    /**
     * 不同rom短信应用包名适配
     * @param statusBarNotification
     * @return
     */
    public boolean romSMSPackageAdaptive(StatusBarNotification statusBarNotification){
        String packageName = statusBarNotification.getPackageName();
        if (packageName.equals("com.google.android.apps.messaging") || packageName.equals("com.google.android.talk")){
            return false;
        }
        if (packageName.equals("com.android.mms")){
            return false;
        }
        // vivo X9S; 8.1.0
        if (packageName.equals("com.android.mms.service")){
            return false;
        }
        return true;
    }

    /**
     * Rom 通知信息适配
     * @return
     */
    public boolean romNotificationInfoAdaptive(StatusBarNotification statusBarNotification){
        // 屏幕状态
        PowerManager pm = (PowerManager) GlobalData.getContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        Bundle bundle = statusBarNotification.getNotification().extras;
        if (bundle == null){
            return true;
        }
        // google适配
        if (RomUtils.isGoogle()){
            String sortKey = statusBarNotification.getNotification().getSortKey();
            if (sortKey == null || !sortKey.equals("00")){
                return true;
            }
        }
        /**
         * 小米 红米（型号暂无）          信息顺序: 1.先两条群组<信息一样>; 2.再两条真实信息<信息一样> 注：亮灭屏信息都一样
         *      小米（MI MAX 2; 7.1.1）   信息顺序: 只有一条真实信息
         */
        if (RomUtils.isXiaomi()){
            // bundle字段差异对比: 过滤群组信息
            if (bundle.get("miui.showAction") == null){
                xiaomiSign = true;
                xiaomiIsReort = false;
                return true;
            }
            if (xiaomiSign){
                if (!xiaomiIsReort){
                    xiaomiIsReort = true;
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
        // 华为 信息顺序: 1.先一条真实信息; 2.再一条群组信息<与真实信息一样> (1.灭屏显示一条数据; 2.收到到一条短信亮屏通知一次立即锁屏第二条仍会发送)
        if (RomUtils.isHuawei()){
            String groupKey = statusBarNotification.getGroupKey();
            String[] groupKeyArray = groupKey.split("\\|");
            if (groupKeyArray.length <= 3 && !honorSign && !isScreenOn){
                return false;
            } else if (groupKeyArray.length <= 3 && honorSign){
                honorSign = false;
                return true;
            } else if (groupKeyArray.length > 3){
                return false;
            }
        }
        // 为了统计尽量多信息，在不适配下一律上报
        return false;
    }


}
