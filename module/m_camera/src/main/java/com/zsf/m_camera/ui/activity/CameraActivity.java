package com.zsf.m_camera.ui.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.m_camera.R;
import com.zsf.m_camera.manager.FragmentStack;
import com.zsf.m_camera.ui.BaseCollectionActivity;
import com.zsf.m_camera.ui.fragment.CameraFragment;
import com.zsf.m_camera.ui.fragment.MainFragment;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

/**
 * @author zhangzhang
 */
@Route(path = "/m_camera/CameraActivity")
public class CameraActivity extends BaseCollectionActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initView();
        initData();
    }

    private void initView() {
    }

    private void initData() {
        Bundle bundle = new Bundle();
        FragmentStack.addFragment(getSupportFragmentManager(), MainFragment.class, R.id.camera_content, bundle);
    }

    @Override
    public int getFragmentContainer() {
        return R.id.camera_content;
    }

    @Override
    public void refreshStyle() {
        switchStyle(false);
    }

    @Override
    public void goBackFirstPage() {
        getSupportFragmentManager().popBackStackImmediate(MainFragment.class.getName(), POP_BACK_STACK_INCLUSIVE);
    }

}