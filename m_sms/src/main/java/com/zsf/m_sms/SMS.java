package com.zsf.m_sms;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zsf
 * @date 2019/12/9
 * @Usage
 */
public class SMS implements Parcelable {
    private String smsAddress;
    private String smsSendTime;
    private String smsContent;
    private String smsStatus;

    public SMS(String smsAddress, String smsSendTime, String smsContent, String smsStatus) {
        this.smsAddress = smsAddress;
        this.smsSendTime = smsSendTime;
        this.smsContent = smsContent;
        this.smsStatus = smsStatus;
    }

    protected SMS(Parcel in) {
        smsAddress = in.readString();
        smsSendTime = in.readString();
        smsContent = in.readString();
        smsStatus = in.readString();
    }

    public static final Creator<SMS> CREATOR = new Creator<SMS>() {
        @Override
        public SMS createFromParcel(Parcel in) {
            return new SMS(in);
        }

        @Override
        public SMS[] newArray(int size) {
            return new SMS[size];
        }
    };

    public String getSmsAddress() {
        return smsAddress;
    }

    public String getSmsSendTime() {
        return smsSendTime;
    }

    public String getSmsContent() {
        return smsContent;
    }
    public String getSmsStatus(){
        return smsStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(smsAddress);
        dest.writeString(smsSendTime);
        dest.writeString(smsContent);
        dest.writeString(smsStatus);
    }

}
