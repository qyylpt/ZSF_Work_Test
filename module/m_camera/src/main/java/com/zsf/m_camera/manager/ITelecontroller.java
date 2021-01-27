package com.zsf.m_camera.manager;

/**
 * Created by wangyongli on 2018/1/11.
 */

public interface ITelecontroller {

    void start();
    void stop();
    void pause();
    void resume();
    boolean isStart();
    boolean isResume();
    boolean stopEnable();
}
