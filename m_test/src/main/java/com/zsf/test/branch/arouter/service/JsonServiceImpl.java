package com.zsf.test.branch.arouter.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.google.gson.Gson;
import com.zsf.utils.ZsfLog;

import java.lang.reflect.Type;

/**
 * @author zsf; 2019/7/31
 * 因为实现了ARouter的SerializationService接口，我们自定义的对象即可不用写代码序列化而直接使用
 */

@Route(path = "/service/json")
public class JsonServiceImpl implements SerializationService {

    @Override
    public <T> T json2Object(String input, Class<T> clazz) {
        ZsfLog.d(this.getClass(), "json2Object: input = " + input);
        return new Gson().fromJson(input, clazz);
    }

    @Override
    public String object2Json(Object instance) {
        ZsfLog.d(this.getClass(), "object2Json: instance = " + instance.toString());
        return new Gson().toJson(instance);
    }

    @Override
    public <T> T parseObject(String input, Type clazz) {
        ZsfLog.d(this.getClass(), "parseObject: input = " + input);
        return new Gson().fromJson(input, clazz);
    }

    @Override
    public void init(Context context) {

    }
}
