package com.zsf.ipc_core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: zsf
 * Date: 2020-05-11 11:03
 */
public class IPCResponse implements Parcelable {

    // 方法返回值序列化结果
    private String result;

    // 本次请求执行情况的描述信息
    private String message;

    // 是否执行成功
    private boolean success;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public IPCResponse(String result, String message, boolean success) {
        this.result = result;
        this.message = message;
        this.success = success;
    }

    protected IPCResponse(Parcel in) {
        result = in.readString();
        message = in.readString();
        success = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result);
        dest.writeString(message);
        dest.writeByte((byte) (success ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IPCResponse> CREATOR = new Creator<IPCResponse>() {
        @Override
        public IPCResponse createFromParcel(Parcel in) {
            return new IPCResponse(in);
        }

        @Override
        public IPCResponse[] newArray(int size) {
            return new IPCResponse[size];
        }
    };

    @Override
    public String toString() {
        return "IPCResponse{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
