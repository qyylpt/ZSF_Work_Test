package com.zsf.ipc_core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.zsf.ipc_core.IRemoteService;
import com.zsf.utils.ZsfLog;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: zsf
 * Date: 2020-05-11 15:49
 * Desc: IPC 绑定支持类
 */
public class IPCTransport {

    /**
     * 缓存所有绑定的ServiceConnection 用于多次解绑判断
     */
    private Map<String, IPCServiceConnection> connectionMap = new HashMap<>();

    private static class LoadObject{
        static final IPCTransport INSTANCE = new IPCTransport();
    }

    public static IPCTransport getDefault(){
        return LoadObject.INSTANCE;
    }

    private IPCTransport(){}

    private class IPCServiceConnection implements ServiceConnection{
        String serviceName;

        public IPCServiceConnection(String serviceName) {
            this.serviceName = serviceName;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IRemoteService iRemoteService = IRemoteService.Stub.asInterface(service);
            ZsfLog.d(IPCServiceConnection.class, IPCServiceConnection.class.getSimpleName() + "->onServiceConnected ipcService = " + iRemoteService);
            IPCCache.getDefault().putRemoteService(serviceName, iRemoteService);
            connectionMap.put(serviceName, this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ZsfLog.d(IPCServiceConnection.class, "onServiceDisconnected");
            IPCCache.getDefault().putRemoteService(serviceName, null);
            connectionMap.remove(serviceName);
        }
    }

    public void bind(Context context, String packageName, String serviceName){
        IRemoteService service = IPCCache.getDefault().getRemoteService(serviceName);
        if (service != null){
            return;
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, serviceName));
        if (!connectionMap.containsKey(serviceName) || connectionMap.get(serviceName) == null){
            IPCServiceConnection connection = new IPCServiceConnection(serviceName);
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        } else {
            context.bindService(intent,connectionMap.get(serviceName),Context.BIND_AUTO_CREATE);
        }
    }

    public void unBind(Context context, String serviceName){
        if (!connectionMap.containsKey(serviceName) || connectionMap.get(serviceName) == null){
            return;
        }
        context.unbindService(connectionMap.get(serviceName));
        connectionMap.remove(serviceName);
        IPCCache.getDefault().putRemoteService(serviceName, null);
    }

    public IPCResponse sendRequest(IPCRequest ipcRequest, String serviceName){
        try {
            IRemoteService service = IPCCache.getDefault().getRemoteService(serviceName);
            return service.sendRequest(ipcRequest);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new IPCResponse(e.getMessage(),"请求远程Service出现异常",false);
        }
    }
}
