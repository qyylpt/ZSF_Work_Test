package com.zsf.aop.m_aspectj;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zsf.utils.ZsfLog;
import com.zsf.view.activity.BaseActivity;

public class AspectJActivity extends BaseActivity {

    private static final Class TAG = AspectJActivity.class;

    private Button aspectjButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_aspect_j);
        aspectjButton = findViewById(R.id.m_aspectj_button);
        aspectjButton.setOnClickListener(this);

    }

    @Override
    public void initData(Activity activity) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.m_aspectj_button){
            testAspectJ(v);
        }
    }

<<<<<<< HEAD
    @CustomAnnotation(value = "test", type = 1)
    private void testAspectJ(View v) {
        ZsfLog.d(TAG, "testAspectJ");
//        String s = null;
//        s.contains("制造一个异常用于 afterThrowing 捕获");
=======
    private void testAspectJ(View v) {
        ZsfLog.d(TAG, "testAspectJ");
>>>>>>> 038b10dfdfdffcf55b02cd5c7c4faefb481492e8
    }
}
