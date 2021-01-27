package com.zsf.m_camera.manager;

import com.google.android.cameraview.CameraView;

/**
 * Created by wangyongli on 2018/1/11.
 */

public interface IMediaTelecontroller extends ITelecontroller {

    void startRecord(CameraView cameraView, String path);
    void setOrientationHint(int orientationHint);
}
