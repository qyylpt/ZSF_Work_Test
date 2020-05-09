package com.zsf.test.branch.arouter.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.test.R;
import com.zsf.test.StatusCode;
import com.zsf.view.activity.BaseActivity;

/**
 * @author zsf
 * @date 2019/08/02
 */
@Route(path = "/branch/ARouter/NeedLoginActivity", extras = StatusCode.IS_NEED_LOGIN)
public class NeedLoginActivity extends BaseActivity {

    private TextView textViewExitApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_need_login);
        textViewExitApp = findViewById(R.id.text_exit_app);
        textViewExitApp.setOnClickListener(this);

    }

    @Override
    public void initData(Activity activity) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.text_exit_app){
            finish();
        }
    }
}
