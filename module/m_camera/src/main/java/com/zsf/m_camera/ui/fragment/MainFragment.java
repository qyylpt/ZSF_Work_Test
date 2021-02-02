package com.zsf.m_camera.ui.fragment;

import com.zsf.m_camera.ui.BaseFragment;

/**
 * @author : zsf
 * @date : 2021/2/1 5:16 PM
 * @desc :
 */
public class MainFragment extends BaseFragment {

    @Override
    public boolean isNeedAddStack() {
        return false;
    }

    @Override
    public void refreshStyle() {
        switchStyle(false);
    }


}
