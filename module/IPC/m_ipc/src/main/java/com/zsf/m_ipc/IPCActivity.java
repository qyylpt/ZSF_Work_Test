package com.zsf.m_ipc;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.global.GlobalData;
import com.zsf.ipc_core.IPCManager;
import com.zsf.ipc_core.IPCResponse;
import com.zsf.m_ipc.data.Result;
import com.zsf.m_ipc.service.IData;
import com.zsf.view.activity.BaseActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Route(path = "/m_ipc/IPCActivity")
public class IPCActivity extends BaseActivity {
    /**
     * 简单数据
     */
    private Button mIPCButtonBind;
    private Button mIPCButtonUnbind;
    private Button mIPCButtonAdd;
    private Button mIPCButtonLog;
    private ServiceConnection serviceConnection;
    private com.zsf.m_ipc.IMyBasicDataAidlInterface iMyAidlInterface;
    Intent intentBasic = new Intent();


    /**
     * 结果展示
     */
    private TextView mIPCTextViewShowResult;
    private Button mIPCButtonClear;

    /**
     * 复杂数据
     */
    private Button mIPCButtonComplexBind;
    private Button mIPCButtonComplexUnbind;
    private Button mIPCButtonComplexAdd;
    private Button mIPCButtonComplexObject;
    private Button mIPCButtonComplexList;
    private Button mIPCButtonComplexMap;
    private Button mIPCButtonComplexObjectInout;
    private ServiceConnection serviceConnectionComplex;
    private com.zsf.m_ipc.IMyComplexDataAidlInterface iMyComplexDataAidlInterface;
    Intent intentComplex = new Intent();

    /**
     * 跨进程 框架 测试
     */
    private Button mIPCButtonCoreRegisterInterface;
    private Button mIPCButtonCoreRegisterService;
    private Button mIPCButtonCoreUnRegisterInterface;
    private Button mIPCButtonCoreUnRegisterService;
    private Button mIPCButtonCoreRegisterRequestInterface;
    private Button mIPCButtonCoreRegitserRequestService;
    /**
     * 远程服务的包名和类名
     */
    private String mRemotePackageName = "com.zsf.m_ipc.service";
    private String mRemoteServiceName = "com.zsf.m_ipc.service.MyRemoteService";
    private IData iData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_ipc);
    }

    @Override
    public void initData(Activity activity) {
        intentBasic.setAction("com.zsf.m_ipc_BasisDataService");
        intentBasic.setPackage(getPackageName());
        mIPCButtonBind = findViewById(R.id.m_ipc_button_basis_data_bind);
        mIPCButtonBind.setOnClickListener(this);
        mIPCButtonUnbind = findViewById(R.id.m_ipc_button_basis_data_unbind);
        mIPCButtonUnbind.setOnClickListener(this);
        mIPCButtonAdd = findViewById(R.id.m_ipc_button_basis_data_add);
        mIPCButtonAdd.setOnClickListener(this);
        mIPCButtonLog = findViewById(R.id.m_ipc_button_basis_data_log);
        mIPCButtonLog.setOnClickListener(this);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                iMyAidlInterface = com.zsf.m_ipc.IMyBasicDataAidlInterface.Stub.asInterface(service);
                setResultText("简单数据: 服务绑定成功!");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // 在服务死亡才可以调用
                iMyAidlInterface = null;
                setResultText("简单数据: 服务死亡断开连接!");
            }
        };

        intentComplex.setAction("com.zsf.m_ipc_ComplexDataService");
        intentComplex.setPackage(getPackageName());
        mIPCTextViewShowResult = findViewById(R.id.m_ipc_textview_show_result);
        mIPCTextViewShowResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        mIPCButtonComplexBind = findViewById(R.id.m_ipc_button_complex_data_bind);
        mIPCButtonComplexBind.setOnClickListener(this);
        mIPCButtonComplexUnbind = findViewById(R.id.m_ipc_button_complex_data_unbind);
        mIPCButtonComplexUnbind.setOnClickListener(this);
        mIPCButtonComplexAdd = findViewById(R.id.m_ipc_button_complex_data_add);
        mIPCButtonComplexAdd.setOnClickListener(this);
        mIPCButtonComplexObject = findViewById(R.id.m_ipc_button_complex_data_back_object);
        mIPCButtonComplexObject.setOnClickListener(this);
        mIPCButtonComplexList = findViewById(R.id.m_ipc_button_complex_data_about_list);
        mIPCButtonComplexList.setOnClickListener(this);
        mIPCButtonComplexMap = findViewById(R.id.m_ipc_button_complex_data_about_map);
        mIPCButtonComplexMap.setOnClickListener(this);
        mIPCButtonComplexObjectInout = findViewById(R.id.m_ipc_button_complex_data_about_object_inout);
        mIPCButtonComplexObjectInout.setOnClickListener(this);
        serviceConnectionComplex = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                setResultText("复杂数据: 服务绑定成功!");
                iMyComplexDataAidlInterface = com.zsf.m_ipc.IMyComplexDataAidlInterface.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                iMyComplexDataAidlInterface = null;
                setResultText("复杂数据: 服务死亡断开连接");
            }
        };

        mIPCButtonClear = findViewById(R.id.m_ipc_button_complex_data_clear);
        mIPCButtonClear.setOnClickListener(this);

        mIPCButtonCoreRegisterService = findViewById(R.id.m_ipc_button_core_register_service);
        mIPCButtonCoreRegisterService.setOnClickListener(this);
        mIPCButtonCoreRegisterInterface = findViewById(R.id.m_ipc_button_core_register_interface);
        mIPCButtonCoreRegisterInterface.setOnClickListener(this);
        mIPCButtonCoreUnRegisterService = findViewById(R.id.m_ipc_button_core_unregister_service);
        mIPCButtonCoreUnRegisterService.setOnClickListener(this);
        mIPCButtonCoreUnRegisterInterface = findViewById(R.id.m_ipc_button_core_unregister_interface);
        mIPCButtonCoreUnRegisterInterface.setOnClickListener(this);
        mIPCButtonCoreRegitserRequestService = findViewById(R.id.m_ipc_button_core_request_service);
        mIPCButtonCoreRegitserRequestService.setOnClickListener(this);
        mIPCButtonCoreRegisterRequestInterface = findViewById(R.id.m_ipc_button_core_request_interface);
        mIPCButtonCoreRegisterRequestInterface.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        // 简单数据: 绑定服务
        if (id == R.id.m_ipc_button_basis_data_bind){
            bindService(intentBasic, serviceConnection, Service.BIND_AUTO_CREATE);
        }

        // 简单数据: 解绑服务
        if (id == R.id.m_ipc_button_basis_data_unbind){
            try {
                iMyAidlInterface = null;
                unbindService(serviceConnection);
                setResultText("简单数据: 服务解绑成功！");
            } catch (Exception e){
                e.printStackTrace();
            }

        }

        // 简单数据: 服务端加法
        if (id == R.id.m_ipc_button_basis_data_add){
            try {
                if (iMyAidlInterface == null){
                    setResultText("简单数据: 没有绑定服务,请绑定!");
                    return;
                }
               int result = iMyAidlInterface.add(8, 8);
               setResultText("简单数据: add 结果 " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 简单数据: 服务端日志打印
        if (id == R.id.m_ipc_button_basis_data_log){
            try {
                if (iMyAidlInterface == null){
                    setResultText("简单数据: 没有绑定服务,请绑定!");
                    return;
                }
                iMyAidlInterface.log("来自客户端打印!");
                setResultText("简单数据: log 通知服务端打印 ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 清空日志
        if (id == R.id.m_ipc_button_complex_data_clear){
            mIPCTextViewShowResult.setText("");
        }

        // 复杂数据: 服务绑定
        if (id == R.id.m_ipc_button_complex_data_bind){
            bindService(intentComplex, serviceConnectionComplex, Service.BIND_AUTO_CREATE);
        }

        // 复杂数据: 服务解绑
        if (id == R.id.m_ipc_button_complex_data_unbind){
            try {
                unbindService(serviceConnectionComplex);
                iMyComplexDataAidlInterface = null;
                setResultText("复杂数据: 服务解绑成功!");
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        // 复杂数据: add
        if (id == R.id.m_ipc_button_complex_data_add){
            if (iMyComplexDataAidlInterface == null){
                setResultText("复杂数据: 没有绑定服务,请绑定!");
                return;
            }
            try {
                int result = iMyComplexDataAidlInterface.add(100, 100);
                setResultText("复杂数据: add 成功 结果 = " + result);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        // 复杂数据: object
        if (id == R.id.m_ipc_button_complex_data_back_object){
            if (iMyComplexDataAidlInterface == null){
                setResultText("复杂数据: 没有绑定服务,请绑定!");
                return;
            }
            try {
                setResultText("复杂数据-对象: getResult 发送数据 100 100 ");
                Result result = iMyComplexDataAidlInterface.getResult(100, 100);
                setResultText("复杂数据-对象: getResult 结果 = " + result.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        // 复杂数据: list
        if (id == R.id.m_ipc_button_complex_data_about_list){
            if (iMyComplexDataAidlInterface == null){
                setResultText("复杂数据: 没有绑定服务,请绑定!");
                return;
            }
            try {
                setResultText("复杂数据-LIST: getListResult 发送数据 2 、 1");
                List<Result> list = iMyComplexDataAidlInterface.getListResult(2, 1);
                for (int i = 0; i < list.size(); i++){
                    setResultText("复杂数据-LIST: getListResult 结果（ " + i + " )  = " + list.get(i).toString());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        // 复杂数据: map
        if (id == R.id.m_ipc_button_complex_data_about_map){
            if (iMyComplexDataAidlInterface == null){
                setResultText("复杂数据: 没有绑定服务,请绑定!");
                return;
            }
            Result resultSend = new Result(1, 2, 3,4);
            Map<String, Result> mapIn = new HashMap<>();
            mapIn.put("key_one", resultSend);
            setResultText("复杂数据-MAP: getMapResult 发送数据 = key_one : " + resultSend.toString());
            try {
                Map map = iMyComplexDataAidlInterface.getMapResult(mapIn);
                setResultText("复杂数据-MAP: getMapResult 返回数据 = value_one : " + ((Result)map.get("key_one")).toString());
                setResultText("复杂数据-MAP: getMapResult 修改数据(注意inout这里是起不到作用的) = value_one : " + resultSend.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        // 复杂数据: object inout
        if (id == R.id.m_ipc_button_complex_data_about_object_inout){
            if (iMyComplexDataAidlInterface == null){
                setResultText("复杂数据: 没有绑定服务,请绑定!");
                return;
            }
            Result resultSend = new Result(1, 2, 3,4);
            setResultText("复杂数据: putResult 发送数据 = " + resultSend.toString());
            try {
                Result resultBack = iMyComplexDataAidlInterface.putResult(resultSend);
                setResultText("复杂数据: putResult 返回数据 = " + resultBack.toString());
                setResultText("复杂数据: putResult 修改数据 = " + resultSend.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        // 框架: 绑定默认 Service
        if (id == R.id.m_ipc_button_core_register_interface){
            IPCManager.getDefault().bind(GlobalData.getContext(), mRemotePackageName);
//            IPCManager.getDefault().serviceRegister(DataManager.class);
            setResultText("IPC框架: 绑定默认 Service 成功!");
        }

        // 框架: 解绑默认 Service
        if (id == R.id.m_ipc_button_core_unregister_interface){
            IPCManager.getDefault().unBind(GlobalData.getContext());
//            IPCManager.getDefault().serviceUnRegiter(DataManager.class);
            setResultText("IPC框架: 解绑默认 Service 成功!");
        }

        // 框架: 绑定自定义 Service
        if (id == R.id.m_ipc_button_core_register_service){
            IPCManager.getDefault().bind(GlobalData.getContext(), mRemotePackageName, mRemoteServiceName);
            setResultText("IPC框架: 绑定自定义 Service 成功!");
        }

        // 框架: 解绑自定义 Service
        if (id == R.id.m_ipc_button_core_unregister_service){
            IPCManager.getDefault().unBind(GlobalData.getContext(), mRemoteServiceName);
            setResultText("IPC框架: 绑定自定义 Service 成功!");
        }

        // 框架: 默认 Service 请求
        if (id == R.id.m_ipc_button_core_request_interface){
            //如果服务端会执行耗时操作，这里需要放在子线程
            //发送请求给框架提供的RemoteService
            if (iData == null) {
                iData = IPCManager.getDefault()
                        .loadRequestHandler(IData.class,"伙计");
                setResultText("IPC框架: 请求默认 Service 发送数据 : 伙计");
            }
            if (iData != null) {
                List list = iData.sendData("小米");
                setResultText("IPC框架: 请求默认 Service-> sendData 发送数据 : 小米");
                setResultText("IPC框架: 请求默认 Service-> sendData 接收结果 : " + list);
                Object student = iData.getStudent("阿菜");
                setResultText("IPC框架: 请求默认 Service->getStudent  发送数据 : 阿菜");
                setResultText("IPC框架: 请求默认 Service->getStudent  接收数据 : " + student.toString());
            }
        }

        // 框架: 自定义 Service 请求
        if (id == R.id.m_ipc_button_core_request_service){
            IPCResponse ipcResponse = IPCManager.getDefault().sendRequest(
                    IPCManager.getDefault().buildRequest(0,null,null),
                    mRemoteServiceName);
            setResultText("IPC框架: 请求自定义 Service->sendRequest  接收数据 : " + ipcResponse.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       try {
           unbindService(serviceConnection);
           unbindService(serviceConnectionComplex);
       } catch (Exception e){
           e.printStackTrace();
       }
    }

    public void setResultText(final String result){
        mIPCTextViewShowResult.post(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat alldate = new SimpleDateFormat("yy/MM/dd HH:mm:ss");//获取日期时间
                mIPCTextViewShowResult.append("\n" + alldate.format(new Date()) + "   " + result);
                int scrollAmount = mIPCTextViewShowResult.getLayout().getLineTop(mIPCTextViewShowResult.getLineCount()) - mIPCTextViewShowResult.getHeight();
                if (scrollAmount > 0)
                    mIPCTextViewShowResult.scrollTo(0, scrollAmount);
                else
                    mIPCTextViewShowResult.scrollTo(0, 0);
            }
        });
    }
}
