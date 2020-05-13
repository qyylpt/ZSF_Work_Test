package com.zsf.ipc_core;

import android.text.TextUtils;

import com.zsf.ipc_core.annotate.RequestLable;
import com.zsf.utils.ZsfLog;
import org.xml.sax.helpers.XMLReaderAdapter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: zsf
 * Date: 2020-05-11 14:25
 * Desc: 缓存服务
 */
public class IPCCache {

    /**
     * 保存服务端处理客户端请求的Class和内部方法
     */
    private Map<String, Class<?>> mClassMap = new HashMap<>();
    private Map<Class<?>, HashMap<String, Method>> mMethodMap = new HashMap<>();

    /**
     * key      远程服务Service全限定名称
     * value    远程Service实力
     */
    private Map<String, WeakReference<com.zsf.ipc_core.IRemoteService>> mIPCService = new HashMap<>();

    /**
     * 保存服务端处理客户端请求的实例
     */
    private Map<String , WeakReference<Object>> mInstance = new HashMap<>();

    private static class LoadCache{
        static final IPCCache IPC_CACHE = new IPCCache();
    }

    public static IPCCache getDefault(){
        return LoadCache.IPC_CACHE;
    }

    private IPCCache(){}

    /**
     * 服务注册
     * 缓存服务端 服务实现类、服务实现类中方法
     * @param clazz 服务实现类
     */
    public void register(Class<?> clazz){
        // 获取服务端提供的服务实现类标签,用于客户端请求服务时方便映射
        RequestLable requestLable = clazz.getAnnotation(RequestLable.class);
        String className;
        if (requestLable == null){
            className = clazz.getName();
        } else {
            className = requestLable.value();
        }
        ZsfLog.d(IPCCache.class, "注册服务 标签 :" + requestLable.value() + "; 服务注册className = " + requestLable.value());
        mClassMap.put(className, clazz);
        // 缓存服务端 服务实现类Method
        HashMap<String, Method> method = new HashMap<>();
        Method[] methods = clazz.getMethods();
        for (Method m : methods){
            method.put(m.getName(), m);
        }
        mMethodMap.put(clazz, method);
    }

    /**
     * 服务反注册
     * 取消服务端服务能力
     * @param clazz
     */
    public void unRegister(Class<?> clazz){
        // 获取服务注解标签
        RequestLable requestLable = clazz.getAnnotation(RequestLable.class);
        String className;
        if (requestLable == null){
            className = clazz.getName();
        } else {
            className = requestLable.value();
        }
        mClassMap.remove(className);
        mMethodMap.remove(clazz);
    }

    /**
     * 获取服务端注册的 服务实现类
     * @param className
     * @return
     */
    public Class<?> getClass(String className){
        if (TextUtils.isEmpty(className)){
            return null;
        }
        Class<?> clazz = mClassMap.get(className);
        if (clazz == null){
            try{
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        return clazz;
    }

    /**
     * 获取服务端注册的 服务实现类方法
     * @param clszz
     * @param method
     * @return
     */
    public Method getMethod(Class<?> clszz, String method){
        HashMap<String, Method> methods = mMethodMap.get(clszz);
        return methods == null ? null : methods.get(method);
    }

    public Object getObject(String classType){
        return mInstance.containsKey(classType) ? mInstance.get(classType).get() : null;
    }

    public void putObject(String classType, Object object){
        if (object == null){
            mInstance.remove(classType);
        } else {
            mInstance.put(classType, new WeakReference<>(object));
        }
    }


    public void putRemoteService(String serviceName, com.zsf.ipc_core.IRemoteService service){
        if (service == null){
            mIPCService.remove(serviceName);
        } else {
            mIPCService.put(serviceName, new WeakReference<>(service));
        }
    }

    public com.zsf.ipc_core.IRemoteService getRemoteService(String serviceName){
        return mIPCService.containsKey(serviceName) ? mIPCService.get(serviceName).get() : null;
    }


}
