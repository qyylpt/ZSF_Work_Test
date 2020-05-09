package com.zsf.m_ipc;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.zsf.utils.ZsfLog;

import androidx.annotation.Nullable;

/**
 * Author: zsf
 * Date: 2020-04-22 15:37
 * 基础简单数据IPC
 */
public class BasisDataService extends BaseService {

    private int flag = 0;
    private com.zsf.m_ipc.IMyBasicDataAidlInterface.Stub stub;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        ZsfLog.d(BasisDataService.class, "服务端绑定 -> onBind");
        return stub;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        ZsfLog.d(BasisDataService.class, "服务端解绑 -> onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ZsfLog.d(BasisDataService.class, "服务端 -> onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stub = null;
        ZsfLog.d(BasisDataService.class, "服务端 -> onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ZsfLog.d(BasisDataService.class, "服务端 -> onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void initData() {
        flag = 0;
        stub = new com.zsf.m_ipc.IMyBasicDataAidlInterface.Stub() {
            @Override
            public int add(int a, int b) throws RemoteException {
                ZsfLog.d(BasisDataService.class, "服务端add操作" + flag++);
                return a + b;
            }

            @Override
            public void log(String tag) throws RemoteException {
                ZsfLog.d(BasisDataService.class, "服务端log操作" + flag++);
            }
        };
    }

}
