package com.zsf.test;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.DeviceUtils;
import com.zsf.global.GlobalData;
import com.zsf.test.branch.phone.PhoneUtils;
import com.zsf.test.branch.test.TestUtils;
import com.zsf.utils.ToastUtils;
import com.zsf.utils.ZsfLog;
import com.zsf.view.activity.BaseActivity;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * @author zsf; 2019/7/31
 */
@Route(path = "/m_test/DemoMainActivity")
public class DemoMainActivity extends BaseActivity {

    private Button buttonLoader;
    private Button buttonDiffRecyclerview;
    private Button buttonArouter;
    private Button buttonMemory;
    private Button buttonEndCall;
    private Button buttonGetMac;
    private Button buttonFingerprint;

    private String[] perms = {Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET};
    private final int PERMS_REQUEST_CODE = 200;

    /**
     * 显示url请求时候接收参数
     */
    private TextView textGetUrlParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 通过webView打开
     */
    @Override
    public void initData(Activity activity) {
        // 接收参数初始化方法
        ARouter.getInstance().inject(this);
        Uri uri = getIntent().getData();
        if (uri != null){
            ARouter.getInstance()
                    .build(uri)
                    .navigation();
            textGetUrlParams.setText("浏览器url：" + uri.getPath() + ", 参数: " + uri.getQuery());
        }
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_demo_main);
        buttonLoader = findViewById(R.id.button_loader);
        buttonLoader.setOnClickListener(this);
        buttonDiffRecyclerview = findViewById(R.id.button_diff_recycler_view);
        buttonDiffRecyclerview.setOnClickListener(this);
        buttonArouter = findViewById(R.id.button_ARouter);
        buttonArouter.setOnClickListener(this);
        textGetUrlParams = findViewById(R.id.text_get_url_params);
        buttonMemory = findViewById(R.id.button_Memory);
        buttonMemory.setOnClickListener(this);
        buttonEndCall = findViewById(R.id.button_EndCall);
        buttonEndCall.setOnClickListener(this);
        buttonGetMac = findViewById(R.id.button_mac);
        buttonGetMac.setOnClickListener(this);
        buttonFingerprint = findViewById(R.id.button_test);
        buttonFingerprint.setOnClickListener(this);
     }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.button_loader) {
            /**
             * greenChannel();跳过所有拦截器
             */
            ARouter.getInstance()
                    .build("/branch/loader/Loader_MainActivity")
                    .greenChannel()
                    .navigation();
        } else if (i == R.id.button_diff_recycler_view) {
            ARouter.getInstance()
                    .build("/branch/DiffUtil/DiffUtilActivity")
                    .navigation();
        } else if (i == R.id.button_ARouter){
            ARouter.getInstance()
                    .build("/branch/ARouter/ARouter_Activity")
                    .greenChannel()
                    .navigation();
        } else if (i == R.id.button_Memory){
            ARouter.getInstance()
                    .build("/branch/Memory/MemoryOptimizeActivity")
                    .navigation();
        } else if (i == R.id.button_EndCall){
            // Android 6.0以上版本需要获取临时权限
            if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS)) {
                requestPermissions(perms, PERMS_REQUEST_CODE);
            } else {
                PhoneUtils.endCall(DemoMainActivity.this);
            }
        } else if (i == R.id.button_mac){
            if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE)) {
                requestPermissions(perms, PERMS_REQUEST_CODE);
            } else {
                ZsfLog.d(DemoMainActivity.class, "mac = " + DeviceUtils.getMacAddress());
                ToastUtils.showToast(GlobalData.getContext(), getMacAddr());
            }
        } else if (i == R.id.button_test){
            TestUtils.setBluetooth(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {
            case PERMS_REQUEST_CODE:
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storageAccepted) {
                    PhoneUtils.endCall(DemoMainActivity.this);
                    ZsfLog.d(DemoMainActivity.class, "mac = " +getMacAddr());
                    ToastUtils.showToast(GlobalData.getContext(), getMacAddr());
                }
                break;
            default:
                break;
        }
    }

    // 获取MAC地址
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                //nif.getInetAddresses();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }
}
