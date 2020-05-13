package com.zsf.ipc_core.handler;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.zsf.ipc_core.IPCCache;
import com.zsf.ipc_core.IPCParameter;
import com.zsf.ipc_core.IPCRequest;
import com.zsf.ipc_core.IPCResponse;
import com.zsf.ipc_core.IRemoteService;
import com.zsf.ipc_core.utils.ParamsConvert;
import com.zsf.utils.ZsfLog;

import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import androidx.annotation.Nullable;

/**
 * Author: zsf
 * Date: 2020-05-11 16:39
 */
public class RemoteService extends Service {

    private Lock mLock = new ReentrantLock();

    private IPCCache mIpcCache = IPCCache.getDefault();

    private IRemoteService.Stub stub = new IRemoteService.Stub() {
        @Override
        public IPCResponse sendRequest(IPCRequest ipcRequest) {
            mLock.lock();
            switch (ipcRequest.getType()){
                case IPCRequest.LOAD_INSTANCE:
                    try {
                        /**
                         * 找到对应的处理客户端请求的Class和构造方法
                         * 然后通过反射实例化对象 保存起来
                         */
                        Class<?> clazz = mIpcCache.getClass(ipcRequest.getClassName());
                        Method method = mIpcCache.getMethod(clazz, ipcRequest.getMethodName());
                        if (mIpcCache.getObject(ipcRequest.getClassName()) == null){
                            Object object = method.invoke(null, ParamsConvert.unSerialzationParams(ipcRequest.getParameters()));
                            mIpcCache.putObject(ipcRequest.getClassName(), object);
                            ZsfLog.d(RemoteService.class, "服务端提供服务实现类对象 object = " + object.toString());
                        }
                        return new IPCResponse(null, "服务端 处理对象 初始化成功！", true);
                    } catch (Exception e){
                        e.printStackTrace();
                        return new IPCResponse(e.getMessage(), "服务端 处理对象 初始化失败！", false);
                    } finally {
                        mLock.unlock();
                    }
                case IPCRequest.LOAD_METHOD:
                    try {
                        /**
                         * 找到对应的处理客户端请求的Class和执行请求的方法
                         */
                        Class<?> clazz = mIpcCache.getClass(ipcRequest.getClassName());
                        Method method = mIpcCache.getMethod(clazz, ipcRequest.getMethodName());
                        Object object = mIpcCache.getObject(ipcRequest.getClassName());
                        if (object == null){
                            return new IPCResponse(null, "服务端 处理对象 未初始化！", false);
                        }
                        Object[] params = ParamsConvert.unSerialzationParams(ipcRequest.getParameters());
                        Object result = method.invoke(object, params);
                        String r = ParamsConvert.mGson.toJson(result);
                        return new IPCResponse(r, "执行方法成功", true);
                    } catch (Exception e){
                        e.printStackTrace();
                        return new IPCResponse(e.getMessage(), "执行方法失败", false);
                    } finally {
                        mLock.unlock();
                    }
            }

            return new IPCResponse(null, "位置类型请求,请指定type", false);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }
}
