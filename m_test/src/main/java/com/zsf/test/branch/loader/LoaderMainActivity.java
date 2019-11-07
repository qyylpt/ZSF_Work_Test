package com.zsf.test.branch.loader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.test.R;
import com.zsf.view.activity.BaseActivity;


/**
 * @author zsf; 2019/7/31
 */
@Route(path = "/branch/loader/Loader_MainActivity")
public class LoaderMainActivity extends BaseActivity {

    private Button btTelPhoneNum;
    private CursorLoaderListFragment loaderListFragment = new CursorLoaderListFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.branch_loader_activity);
        btTelPhoneNum = findViewById(R.id.tel_phone_num);
        btTelPhoneNum.setOnClickListener(this);
    }

    @Override
    public void initData(Activity activity) {

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tel_phone_num) {
            loaderListFragment.show(getSupportFragmentManager(), R.id.content);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
