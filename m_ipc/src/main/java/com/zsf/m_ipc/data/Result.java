package com.zsf.m_ipc.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Author: zsf
 * Date: 2020-04-24 12:14
 */
public class Result implements Parcelable {
    /**
     * 加
     */
    public long addResult;

    /**
     * 减
     */
    public long subResult;

    /**
     * 乘
     */
    public long mulResult;

    /**
     * 除
     */
    public double divResult;

    public Result(long addResult, long subResult, long mulResult, double divResult) {
        this.addResult = addResult;
        this.subResult = subResult;
        this.mulResult = mulResult;
        this.divResult = divResult;
    }

    /**
     * 从Parcel对象获取数据，拆包
     * @param in
     */
    protected Result(Parcel in) {
        addResult = in.readLong();
        subResult = in.readLong();
        mulResult = in.readLong();
        divResult = in.readDouble();
    }

    // 实现静态公共字段Creator, 用来使用Parcel对象构造AllResult对象
    public static final Creator<Result> CREATOR = new Creator<Result>() {

        /**
         * 将序列化的数据进行反序列化操作
         * @param in
         * @return
         */
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 打包函数
     * 将result类内部数据按照特定顺序写入parcel对象 序列化操作
     * 写入顺序必须与拆包函数读取顺序一致
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(addResult);
        dest.writeLong(subResult);
        dest.writeLong(mulResult);
        dest.writeDouble(divResult);
    }

    /**
     * 默认生成的模板类的对象只支持 in 的定向 tag,因为只有 writeToParcel 方法
     * 如果要支持 out 或者 inout 的定向 tag, 还需要实现 readFormParcel 方法
     * 参数是一个parcel, 用它来存储和传输数据
     * 注意: 此处读取顺序应当和 writeToParcel 方法中一致。
     * @param dest
     */
    public void readFromParcel(Parcel dest){
        addResult = dest.readLong();
        subResult = dest.readLong();
        mulResult = dest.readLong();
        divResult = dest.readDouble();
    }

    @NonNull
    @Override
    public String toString() {
        String result = "Result = { addResult : " + addResult + ", subResult :" + subResult + ", mulResult :" + mulResult + ", divResult" + divResult + "}";
        return result;
    }
}
