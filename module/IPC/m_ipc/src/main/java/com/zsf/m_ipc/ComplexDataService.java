package com.zsf.m_ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.zsf.m_ipc.data.Result;
import com.zsf.utils.ZsfLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.annotation.Nullable;

/**
 * Author: zsf
 * Date: 2020-04-24 12:14
 */
public class ComplexDataService extends Service {

    private com.zsf.m_ipc.IMyComplexDataAidlInterface.Stub stub = new com.zsf.m_ipc.IMyComplexDataAidlInterface.Stub() {
        @Override
        public int add(int a, int b) throws RemoteException {
            return a + b;
        }

        @Override
        public Result getResult(long a, long b) throws RemoteException {
            return new Result(a, b, 88, 188);
        }

        @Override
        public List<Result> getListResult(long a, long b) throws RemoteException {
            List<Result> list = new ArrayList<>();
            long addResult = a + b;
            long subResult = a - b;
            long mulResult = a * b;
            double divResult = a / b;
            list.add(new Result(addResult + 100, subResult + 100, mulResult + 100, divResult + 100));
            list.add(new Result(addResult + 200, subResult + 200, mulResult + 200, divResult + 200));
            return list;
        }

        @Override
        public Map getMapResult(Map datas) throws RemoteException {
            Map map = new HashMap();
            map.put("key_one", new Result(5, 6, 7, 8));
            ((Result)datas.get("key_one")).addResult = 101;
            ((Result)datas.get("key_one")).subResult = 102;
            ((Result)datas.get("key_one")).mulResult = 103;
            ((Result)datas.get("key_one")).divResult = 104;
            return map;
        }

        @Override
        public Result putResult(Result result) throws RemoteException {
            result.addResult += 50;
            result.subResult += 50;
            result.mulResult += 50;
            result.divResult += 50;
            return new Result(result.addResult + 100, result.subResult + 100, result.mulResult + 100, result.divResult + 100);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        ZsfLog.d(ComplexDataService.class, "服务端 -> onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        ZsfLog.d(ComplexDataService.class, "服务端 -> onBind");
        return stub;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ZsfLog.d(ComplexDataService.class, "服务端 -> onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        ZsfLog.d(ComplexDataService.class, "服务端 -> onStartCommand");
        super.onDestroy();
    }
}
