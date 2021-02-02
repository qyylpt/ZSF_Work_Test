package com.zsf.m_camera.ui.fragment;

import com.zsf.m_camera.R;

/**
 * @author : zsf
 * @date : 2021/1/29 10:36 AM
 * @desc :
 */
public class SubmitPhotoFragment extends SubmitFragment {


    @Override
    public String getSubmitTitle() {
        return getResources().getString(R.string.m_camera_image_content_type_photo);
    }

    @Override
    public String getSubmitType() {
        return getResources().getString(R.string.m_camera_image_title_photo);
    }

    @Override
    public void refreshStyle() {
        switchStyle(false);
    }

}
