package com.zsf.m_sms;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zsf.global.GlobalData;
import com.zsf.utils.ToastUtils;
import com.zsf.utils.ZsfLog;
import com.zsf.view.activity.BaseActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Route(path = "/m_sms/SMSActivity")
public class SMSActivity extends BaseActivity implements OnRefreshListener {
    private Button buttonGetSms;
    private RecyclerView recyclerViewList;
    private Cursor cursor;
    private RefreshLayout refreshLayout;
    private List<SMS> smsList = new ArrayList<>();
    private SMSAdapter smsAdapter = new SMSAdapter(smsList);
    private boolean isClick = true;
    private int countSms = 0;
    /**
     * 广播方式监听短信
     */
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ZsfLog.d(SMSActivity.class, "intent = " + intent.getAction());
            if (intent.getAction() == Telephony.Sms.Intents.SMS_RECEIVED_ACTION){
                ZsfLog.d(SMSActivity.class, "接收到短信广播");

                //短信的数据是pdu的数据,必须对短信的格式很了解才可以解析短信.
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                for(Object obj:objs) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                    String body = smsMessage.getMessageBody();
                    String sender = smsMessage.getOriginatingAddress();
                    ZsfLog.d(SMSActivity.class, "body:" + body);
                    ZsfLog.d(SMSActivity.class, "sender:" + sender);
                }
                loadSms(2);
            }
        }
    };
    /**
     * 数据库变化监听短信变化
     */
    private ContentObserver contentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            ZsfLog.d(SMSActivity.class, "数据库变化，当前数据库：" + getCountsms() + "条");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_sms);
        refreshLayout = findViewById(R.id.m_sms_smartRefreshLayout);
        refreshLayout.setOnRefreshListener(this);
        buttonGetSms = findViewById(R.id.m_sms_button_get_sms);
        recyclerViewList = findViewById(R.id.m_sms_recyclerView_sms_list);
        buttonGetSms.setOnClickListener(this);
        ((TextView)findViewById(R.id.m_sms_textView_hint_info)).setText(GlobalData.getContext().getResources().getString(R.string.m_sms_text_hint_info) + "【 " + Build.BRAND + "; " + Build.MODEL + "; " + Build.VERSION.RELEASE + " 】");
    }

    @Override
    public void initData(Activity activity) {

        AndPermission.with(SMSActivity.this)
                .runtime()
                .permission(Permission.READ_SMS, Permission.RECEIVE_SMS, Permission.READ_CONTACTS)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        loadSms(10000);
                        isClick = true;
                        setCacheCountSms();
                        GlobalData.getContext().getContentResolver().registerContentObserver(Telephony.Sms.CONTENT_URI, true, contentObserver);

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {

                    }
                }).start();
        // 注册短信接收广播
//        GlobalData.getContext().registerReceiver(broadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
        // 注册通知广播
        LocalBroadcastManager.getInstance(GlobalData.getContext()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isClick){
                    smsList.clear();
                    isClick = false;
                }
                String smsStatus = "";
                if (getCountsms() > countSms){
                    smsStatus = "数据库中可以获取到新收到信息, 不需要上报";
                } else {
                    smsStatus = "数据库中不可以获取到新收到信息, 需要上报";
                }
                setCacheCountSms();
                Bundle smsBundle = intent.getExtras();
                SMS sms = (SMS) smsBundle.get("notification_msg");
                smsList.add(new SMS("发件人：" + sms.getSmsAddress(), "时    间：" + timeStamp2Date(sms.getSmsSendTime(), null), sms.getSmsContent(), smsStatus));
                smsAdapter.setData(smsList, false);
                smsAdapter.notifyDataSetChanged();
            }
        }, new IntentFilter("notification_sms"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        recyclerViewList.setAdapter(smsAdapter);
        // 开启通知监听服务
        if (!isNotificationListenerEnabled(this)) {
            openNotificationListenSettings();
        }
        toggleNotificationListenerService();
    }

    private void loadSms(int count){
        cursor = GlobalData.getContext().getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, Telephony.Sms.DATE + " DESC LIMIT " + count);
        smsList.clear();
        if (cursor != null){
            while (cursor.moveToNext()){
                smsList.add(new SMS("发件人：" + cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS)),"时    间：" + timeStamp2Date(cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE)), "yyyy-MM-dd HH:mm:ss"), cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY)), ""));
                ZsfLog.d(SMSActivity.class, "id : " + cursor.getString(cursor.getColumnIndex(Telephony.Sms._ID)) + ";  address : " + cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS)) + ";\n DATE : " +  timeStamp2Date(cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE)), "yyyy-MM-dd HH:mm:ss") + ";\n BODY : " + cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY)));
            }
            cursor.close();
        }
        smsAdapter.setData(smsList, true);
        smsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.m_sms_button_get_sms){
            loadSms(10000);
            isClick = true;
//            getContactNumber("张三");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        GlobalData.getContext().unregisterReceiver(broadcastReceiver);
        GlobalData.getContext().getContentResolver().unregisterContentObserver(contentObserver);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh(2000);
    }



    public boolean isNotificationListenerEnabled(Context context) {
        boolean enable = false;
        String packageName = getPackageName();
        String flat= Settings.Secure.getString(getContentResolver(),"enabled_notification_listeners");
        if (flat != null) {
            enable= flat.contains(packageName);
        }
        return enable;
    }

    //把应用的NotificationListenerService实现类disable再enable，即可触发系统rebind操作
    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(this, SMSNotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(this, SMSNotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private void openNotificationListenSettings() {
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {//普通情况下找不到的时候需要再特殊处理找一次
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                startActivity(intent);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            ToastUtils.showToast(this, "对不起，您的手机暂不支持", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    public void setCacheCountSms(){
        Cursor cursor = GlobalData.getContext().getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        if (cursor != null){
            countSms = cursor.getCount();
        }
        ZsfLog.d(SMSActivity.class,"当前数据库中短信数量：" + countSms);
    }
    public int getCountsms(){
        Cursor cursor = GlobalData.getContext().getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        if (cursor != null){
            return cursor.getCount();
        }
        return 0;
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }


    /**
     * 获取电话号码
     * @param name
     * @return
     */
    public String getContactNumber(String name) {
        String id = "0";
        StringBuffer phoneBuffer = new StringBuffer();
        Cursor cursor = GlobalData.getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE + "='" + name + "'", null, null);
        if (cursor.getCount() == 0){
            return name;
        }
        if(cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        }
        int phoneCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        if (phoneCount <= 0){
            return name;
        }
        Cursor phoneCursor = GlobalData.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
        if (phoneCursor.moveToFirst()){
            do {
                phoneBuffer.append(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + ";");
            } while (phoneCursor.moveToNext());
        }
        ZsfLog.d(SMSActivity.class, "phoneNumber = " + phoneBuffer.toString());
        cursor.close();
        phoneCursor.close();
        return phoneBuffer.toString();
    }
}
