package com.zsf.ipc_core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: zsf
 * Date: 2020-05-11 11:13
 * Desc: 跨进程参数封装
 */
public class IPCParameter implements Parcelable {

    // 参数class类型
    private Class type;
    // 参数值 序列化后的字符串
    private String value;

    public IPCParameter(Class type, String value) {
        this.type = type;
        this.value = value;
    }

    protected IPCParameter(Parcel in) {
        type = (Class) in.readSerializable();
        value = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(type);
        dest.writeString(value);
    }

    public static final Creator<IPCParameter> CREATOR = new Creator<IPCParameter>() {
        @Override
        public IPCParameter createFromParcel(Parcel in) {
            return new IPCParameter(in);
        }

        @Override
        public IPCParameter[] newArray(int size) {
            return new IPCParameter[size];
        }
    };

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "IPCParameter{" +
                "type='" + type + '\'' +
                ", name='" + value + '\'' +
                '}';
    }
}
