// IMyAidlInterface.aidl
package com.zsf.m_ipc;

// Declare any non-default types here with import statements
// 跨进程简单基础数据通信

interface IMyBasicDataAidlInterface {

    // 加法计算
    int add(int a, int b);
    // 打印日志
    void log(String tag);
}
