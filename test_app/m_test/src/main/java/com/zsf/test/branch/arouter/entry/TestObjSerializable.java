package com.zsf.test.branch.arouter.entry;

import java.io.Serializable;

/**
 * ARouter传递对象
 * @author zsf; 2019/7/31
 */
public class TestObjSerializable implements Serializable {
    public String name;
    public int age;
    public TestObjSerializable(){}
    public TestObjSerializable(String name, int age){
        this.name = name;
        this.age = age;
    }
}