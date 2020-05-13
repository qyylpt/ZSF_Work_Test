package com.zsf.m_ipc.service;

import com.zsf.ipc_core.annotate.RequestLable;
import com.zsf.utils.ZsfLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:mango
 * Time:2019/6/19 17:00
 * Version:1.0.0
 * Desc:注解值和客户端接口中的注解值保持一致
 * 需要提供一个公共静态且方法名为getDefault的方法 返回实例
 */
@RequestLable("IData")
public class DataManager implements IData {
    List<String> list = new ArrayList<>();
    private static DataManager ourInstance;

    public static DataManager getDefault(String params) {
        ourInstance = new DataManager(params);
        ZsfLog.d(DataManager.class, "DataManager 初始化对象 params = " + params);
        return ourInstance;
    }

    private DataManager() {}

    private DataManager(String params){
        list.add(params);
    }


    @Override
    public List sendData(String params) {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add(params);
        return list;
    }

    @Override
    public Student getStudent(String name) {
        return new Student(name);
    }
}
