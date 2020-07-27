package com.zsf.m_suspended_window;

/**
 * @Author: zsf
 * @Date: 2020-07-10 16:11
 */
public abstract class Singleton<T> {

    private volatile T mInstance;

    protected abstract T create();

    public final T get(){
        if (mInstance == null){
            synchronized (this){
                if (mInstance == null){
                    mInstance = create();
                }
            }
        }
        return mInstance;
    }
}
