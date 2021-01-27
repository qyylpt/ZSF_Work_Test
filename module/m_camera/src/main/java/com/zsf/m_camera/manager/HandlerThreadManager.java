package com.zsf.m_camera.manager;

import android.os.HandlerThread;
import android.os.Looper;

/**
 * Created by wangyongli on 17-11-13.
 */

public class HandlerThreadManager {

    private static final Object lock = new Object();

    private static HandlerThread handlerThread;


    public static HandlerThread getHandlerThread() {
        if (handlerThread == null) {
            synchronized (lock) {
                if (handlerThread == null) {
                    handlerThread = new HandlerThread("serviceHandler");
                    handlerThread.start();
                }
            }
        }
        return handlerThread;
    }

    public static Looper getLooper() {
        return getHandlerThread().getLooper();
    }
}
