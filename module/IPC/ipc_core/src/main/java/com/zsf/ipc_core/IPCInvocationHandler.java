package com.zsf.ipc_core;

import com.google.gson.Gson;
import com.zsf.ipc_core.callback.IPCRequestError;
import com.zsf.ipc_core.utils.ParamsConvert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Author: zsf
 * Date: 2020-05-12 14:46
 */
public class IPCInvocationHandler implements InvocationHandler {

    private Gson mGson;
    private String className;
    private String serviceName;
    private IPCRequestError ipcRequestError;


    public IPCInvocationHandler(String className, String serviceName, IPCRequestError ipcRequestError) {
        mGson = new Gson();
        this.className = className;
        this.serviceName = serviceName;
        this.ipcRequestError = ipcRequestError;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        IPCRequest ipcRequest = new IPCRequest();
        ipcRequest.setType(IPCRequest.LOAD_METHOD);
        ipcRequest.setClassName(className);
        ipcRequest.setMethodName(method.getName());
        ipcRequest.setParameters(ParamsConvert.serialzationParams(args));
        IPCResponse ipcResponse = IPCManager.getDefault().sendRequest(ipcRequest, serviceName);
        if (ipcResponse != null && ipcResponse.isSuccess()){
            Class<?> returnType = method.getReturnType();
            if (returnType != void.class && returnType != Void.class){
                return mGson.fromJson(ipcResponse.getResult(), returnType);
            }
        } else {
            ipcRequestError.sendResult(ipcResponse);
        }
        return null;
    }
}
