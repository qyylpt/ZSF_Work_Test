package com.zsf.m_camera.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.m_camera.R;
import com.zsf.m_camera.manager.FragmentStack;
import com.zsf.m_camera.ui.BaseCollectionActivity;
import com.zsf.m_camera.ui.fragment.CameraFragment;

/**
 * @author zhangzhang
 */
@Route(path = "/m_camera/CameraActivity")
public class CameraActivity extends BaseCollectionActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initView();
        initData();
    }

    private void initView() {
        frameLayout = findViewById(R.id.camera_content);
    }

    private void initData() {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentStack.BUNDLE_KEY_TAG, "PhotoFragment");
        FragmentStack.addFragment(getSupportFragmentManager(), CameraFragment.class, R.id.camera_content, bundle);
    }

    @Override
    public int getFragmentContainer() {
        return R.id.camera_content;
    }
}