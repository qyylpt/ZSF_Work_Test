package com.zsf.m_auto_start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/m_auto_start/TransparentActivity")
public class TransparentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 0.2f;
        getWindow().setAttributes(layoutParams);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_transparent);
        Intent intent = new Intent(this, StartActivityService.class);
        startService(intent);
    }
}
