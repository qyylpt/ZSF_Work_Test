package com.zsf.ipc_core;

import android.content.Context;

import com.zsf.ipc_core.annotate.RequestLable;
import com.zsf.ipc_core.callback.IPCRequestError;
import com.zsf.ipc_core.utils.ParamsConvert;

import java.lang.reflect.Proxy;

/**
 * Author: zsf
 * Date: 2020-05-11 14:19
 * Desc: IPC 通信管理类
 */
public class IPCManager {

    public IPCManager() {
    }

    private static class LoadObject{
        static final IPCManager INSTANCE = new IPCManager();
    }

    public static IPCManager getDefault(){
        return LoadObject.INSTANCE;
    }

    /**
     * 服务端调用
     * 注册服务端 处理客户端请求类
     * @param clazz
     */
    public void serviceRegister(Class<?> clazz){
        IPCCache.getDefault().register(clazz);
    }

    /**
     * 服务端调用
     * 注销服务端 处理客户端请求类
     * @param clazz
     */
    public void serviceUnRegiter(Class<?> clazz){
        IPCCache.getDefault().unRegister(clazz);
    }

    /**
     * 客户端调用
     * 绑定默认RemoteService
     * @param context
     * @param packageName
     */
    public void bind(Context context, String packageName){
        bind(context, packageName, "com.zsf.ipc_core.handler.RemoteService");
    }

    /**
     * 客户端调用
     * 绑定自定义Service
     * @param context
     * @param packageName
     * @param serviceName
     */
    public void bind(Context context, String packageName, String serviceName){
        IPCTransport.getDefault().bind(context, packageName, serviceName);
    }

    /**
     * 客户端调用
     * 解绑默认服务RemoteService
     * @param context
     */
    public void unBind(Context context){
        unBind(context, "com.zsf.ipc_core.handler.RemoteService");
    }

    /**
     * 客户端调用
     * 解绑自定义服务
     * @param context
     * @param serviceName
     */
    public void unBind(Context context, String serviceName){
        IPCTransport.getDefault().unBind(context, serviceName);
    }

    /**
     * 客户端调用
     * 绑定RemoteService，必须通过这个方法获取实例再发送请求
     * 获取 远程服务端处理客户端请求 对象
     * @param inter     远程 处理客户端请求的类实现的接口
     * @param params    获取实例方法的参数
     * @param <T>
     * @return
     */
    public <T> T loadRequestHandler(Class<?> inter, Object... params){
        return loadRequestHandler("com.zsf.ipc_core.handler.RemoteService",inter,null,params);
    }

    /**
     * 客户端调用
     * 绑定RemoteService，必须通过该方法获取实例再发送请求
     * @param inter
     * @param ipcRequestError load失败之后通过该接口回调
     * @param params
     * @param <T>
     * @return
     */
    public <T> T loadRequestHander(Class<?> inter, IPCRequestError ipcRequestError, Object... params){
        return loadRequestHandler("com.mango.ipcore.handler.RemoteService",inter, ipcRequestError,params);
    }

    private <T> T loadRequestHandler(String serviceName, Class<?> inter, IPCRequestError ipcRequestError, Object... params) {
        if (!checkService(serviceName)){
            if (ipcRequestError != null){
                ipcRequestError.sendResult(new IPCResponse("", "未绑定远程Service", false));
            }
            return null;
        }
        // 获取处理客户端请求的对象的 注解标签,以此在服务端找出对应的处理者
        String className;
        RequestLable requestLable = inter.getAnnotation(RequestLable.class);
        if (requestLable == null){
            className = inter.getName();
        } else {
            className = requestLable.value();
        }
        IPCRequest ipcRequest = buildRequest(IPCRequest.LOAD_INSTANCE, className, "getDefault", params);

        /**
         * 发送消息给服务端,实例化 处理客户端请求 对象,要求实例化该对象的方法名为 getDefault
         * 如果构造成功 就通过动态代理构造一个实现该接口的对象并返回，以接收客户端后续请求
         */
        IPCResponse ipcResponse = sendRequest(ipcRequest, serviceName);
        if (ipcResponse.isSuccess()){
            return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                    new Class[]{inter},
                    new IPCInvocationHandler(className, serviceName, ipcRequestError));
        } else {
            if (ipcRequestError != null){
                ipcRequestError.sendResult(ipcResponse);
            }
        }

        return null;
    }

    /**
     * 检查服务是否注册
     * @param serviceName
     * @return
     */
    public boolean checkService(String serviceName){
        return IPCCache.getDefault().getRemoteService(serviceName) == null ? false : true;
    }

    public IPCRequest buildRequest(int type, String className, String methodName, Object... params){
        IPCRequest ipcRequest = new IPCRequest();
        ipcRequest.setType(type);
        ipcRequest.setClassName(className);
        ipcRequest.setMethodName(methodName);
        ipcRequest.setParameters(ParamsConvert.serialzationParams(params));
        return ipcRequest;
    }

    /**
     * 客户端调用
     * 自定义远程 Service 通过该方法发送请求，在自定义的 Service 中定义处理逻辑
     * 发送请求给远程Service
     * @param ipcRequest
     * @param serviceName
     * @return
     */
    public IPCResponse sendRequest(IPCRequest ipcRequest, String serviceName){
        if (!checkService(serviceName)){
            return new IPCResponse("", "未绑定远程Service", false);
        }
        return IPCTransport.getDefault().sendRequest(ipcRequest, serviceName);
    }
}



















































