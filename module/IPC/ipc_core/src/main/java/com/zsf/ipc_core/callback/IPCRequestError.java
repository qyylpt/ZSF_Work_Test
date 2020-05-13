package com.zsf.ipc_core.callback;

import com.zsf.ipc_core.IPCResponse;

/**
 * Author: zsf
 * Date: 2020-05-12 11:59
 * Desc: 客户端发送请求结果
 */
public interface IPCRequestError {

    void sendResult(IPCResponse ipcResponse);

}
