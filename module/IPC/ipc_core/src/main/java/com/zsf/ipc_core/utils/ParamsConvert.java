package com.zsf.ipc_core.utils;

import com.google.gson.Gson;
import com.zsf.ipc_core.IPCParameter;
import com.zsf.utils.ZsfLog;

/**
 * Author: zsf
 * Date: 2020-05-11 20:21
 * Desc: 方法参数序列化
 */
public class ParamsConvert {
    public static Gson mGson = new Gson();

    /**
     * 参数序列化
     * @param params
     * @return
     */
    public static IPCParameter[] serialzationParams(Object[] params){
        IPCParameter[] p;
        if (params == null){
            p = new IPCParameter[0];
        } else {
            p = new IPCParameter[params.length];
            for (int i = 0; i < params.length; i++){
                Object o = params[i];
                ZsfLog.d(ParamsConvert.class, "参数序列化 : key = " + o.getClass() + "; value = " + mGson.toJson(o));
                p[i] = new IPCParameter(o.getClass(), mGson.toJson(o));
            }
        }
        return p;
    }

    /**
     * 参数反序列化
     * @param parameters
     * @return
     */
    public static Object[] unSerialzationParams(IPCParameter[] parameters){
        Object[] objects;
        if (parameters == null || parameters.length == 0){
            objects = new Object[0];
        } else {
            objects = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++){
                IPCParameter pa = parameters[i];
                objects[i] = mGson.fromJson(pa.getValue(), pa.getType());
            }
        }
        return objects;
    }
}
