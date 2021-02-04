package com.zsf.m_camera.ui.fragment;

import com.zsf.m_camera.R;

/**
 * @author : zsf
 * @date : 2021/2/4 6:31 PM
 * @desc :
 */
public class SubmitVideoFragment extends SubmitFragment {

    @Override
    public String getSubmitTitle() {
        return getResources().getString(R.string.m_camera_image_content_type_video);
    }

    @Override
    public String getSubmitType() {
        return getResources().getString(R.string.m_camera_image_title_video);
    }

    @Override
    public void refreshStyle() {
        switchStyle(false);
    }

    @Override
    public void back() {
        getActivity().getSupportFragmentManager().popBackStack(MainFragment.class.getSimpleName(), 0);
        MainFragment mainFragment = (MainFragment)getActivity().getSupportFragmentManager().findFragmentByTag(MainFragment.class.getName());
        if (mainFragment != null) {
            mainFragment.refreshStyle();
        }
    }
}
