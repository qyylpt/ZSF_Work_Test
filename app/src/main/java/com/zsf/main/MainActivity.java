package com.zsf.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.zsf_work_test.R;
import com.zsf.utils.ZsfLog;
import com.zsf.view.activity.BaseActivity;


/**
 * @author zsf; 2019/7/31
 */
public class MainActivity extends BaseActivity {

    // 测试模块
    private Button goMTest;
    // 设别管理器
    private Button goMDeviceManager;
    // 自动启动Activity
    private Button goMAutoStart;
    // 保活
    private Button goMKeepAlive;
    // 图文识别
    private Button goMOrc;
    // 指纹解锁
    private Button goMFingerprintUnlock;

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData(Activity activity) {

    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_main);
        goMTest = findViewById(R.id.go_m_test);
        goMTest.setOnClickListener(this);
        goMDeviceManager = findViewById(R.id.go_m_device_manager);
        goMDeviceManager.setOnClickListener(this);
        goMAutoStart = findViewById(R.id.go_m_auto_start);
        goMAutoStart.setOnClickListener(this);
        goMKeepAlive = findViewById(R.id.go_m_keep_alive);
        goMKeepAlive.setOnClickListener(this);
        goMOrc = findViewById(R.id.go_m_orc);
        goMOrc.setOnClickListener(this);
        goMFingerprintUnlock = findViewById(R.id.go_m_fingerprint_unlock);
        goMFingerprintUnlock.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.go_m_test) {
            ARouter.getInstance()
                   .build("/m_test/DemoMainActivity")
                   .navigation();
        } else if (view.getId() == R.id.go_m_device_manager){
            ARouter.getInstance()
                    .build("/m_device_manager/DeviceManagerActivity")
                    .navigation();
        } else if (view.getId() == R.id.go_m_auto_start){
            ARouter.getInstance()
                    .build("/m_auto_start/TransparentActivity")
                    .navigation();
        } else if (view.getId() == R.id.go_m_keep_alive){
            ARouter.getInstance()
                    .build("/m_keep_alive/KeepAliveMainActivity")
                    .navigation();
        } else if (view.getId() == R.id.go_m_orc){
            try {
                textView.setText("111");
            } catch (NullPointerException e){
                ZsfLog.d(MainActivity.class, "1:"+ e.getMessage());
            } catch (Throwable e){
                ZsfLog.d(MainActivity.class, "2:" + e.getMessage());
            }
            ARouter.getInstance()
                    .build("/m_orc/ORCMainActivity")
                    .navigation();
        } else if (view.getId() == R.id.go_m_fingerprint_unlock){
            ARouter.getInstance()
                    .build("/m_fingerprint_unlock/FingerprintActivity")
                    .navigation();
        }
    }
}
