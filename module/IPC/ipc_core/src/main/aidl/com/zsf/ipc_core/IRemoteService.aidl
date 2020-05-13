// IRemoteService.aidl
package com.zsf.ipc_core;

import com.zsf.ipc_core.IPCRequest;
import com.zsf.ipc_core.IPCResponse;

// 模仿HTTP服务器请求

interface IRemoteService {
    // 模仿HTTP请求及返回
    IPCResponse sendRequest(in IPCRequest ipcRequest);
}
