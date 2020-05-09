// Result.aidl
package com.zsf.m_ipc.data;

// 这个文件的作用是引入一个序列化对象 Result 供其他AIDL文件调用
// 注意: Result.aidl 与 Result.java 的包名应当是一样的
// 这里声明任何非默认类型 注意 parcelable 是小写
parcelable Result;
