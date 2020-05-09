package com.zsf.test.branch.arouter.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zsf.test.R;
import com.zsf.test.StatusCode;
import com.zsf.utils.ZsfLog;
import com.zsf.view.activity.BaseActivity;

/**
 * @author zsf
 * @date 2019/08/02
 */
@Route(path = "/branch/ARouter/LoginArouterActivity")
public class LoginArouterActivity extends BaseActivity {

    private Button buttonArouterLogin;

    @Autowired
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // 初始化ARouter
        ARouter.getInstance().inject(this);
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_logn_arouter);
        buttonArouterLogin = findViewById(R.id.button_ARouter_login);
        buttonArouterLogin.setOnClickListener(this);
    }

    @Override
    public void initData(Activity activity) {

    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.button_ARouter_login){
            StatusCode.IS_LOGIN = true;
            ARouter.getInstance()
                    .build(url)
                    .navigation();
            ZsfLog.d(LoginArouterActivity.class, Thread.currentThread().getName());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }
    }
}
