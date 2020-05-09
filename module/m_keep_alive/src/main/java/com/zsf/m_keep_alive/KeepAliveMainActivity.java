package com.zsf.m_keep_alive;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zsf.view.activity.BaseActivity;

@Route(path = "/m_keep_alive/KeepAliveMainActivity")
public class KeepAliveMainActivity extends BaseActivity {

    /**
     * 进入JobScheduler
     */
    private Button buttonJobScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView(Activity activity) {
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=1.0f;
        getWindow().setAttributes(lp);
        setContentView(R.layout.activity_keep_alive_main);
        buttonJobScheduler = findViewById(R.id.button_m_keep_alive_job_scheduler);
        buttonJobScheduler.setOnClickListener(this);
    }

    @Override
    public void initData(Activity activity) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_m_keep_alive_job_scheduler){
            ARouter.getInstance()
                    .build("/keep_alive/JobSchedulerActivity")
                    .navigation();
        }
    }
}
