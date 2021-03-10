package com.example.icaller_mobile.model.report;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.icaller_mobile.common.utils.Utils;

import java.util.Objects;


public class CallLog implements Parcelable {
    private String name;
    private String phone;
    private String nationalNumber;
    private String type;
    private long date;
    private String duration;
    private String group;

    public CallLog(String name, String phone, String nationalNumber, String type, long date, String duration, String group) {
        this.name = name;
        this.phone = phone;
        this.nationalNumber = nationalNumber;
        this.type = type;
        this.date = date;
        this.duration = duration;
        this.group = group;
    }

    public CallLog(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallLog callLog = (CallLog) o;
        if (callLog.nationalNumber == null || nationalNumber == null)
            return false;
        return nationalNumber.equals(callLog.nationalNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nationalNumber);
    }

    protected CallLog(Parcel in) {
        name = in.readString();
        phone = in.readString();
        nationalNumber = in.readString();
        type = in.readString();
        date = in.readLong();
        duration = in.readString();
        group = in.readString();
    }

    public static final Creator<CallLog> CREATOR = new Creator<CallLog>() {
        @Override
        public CallLog createFromParcel(Parcel in) {
            return new CallLog(in);
        }

        @Override
        public CallLog[] newArray(int size) {
            return new CallLog[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationalNumber() {
        return nationalNumber;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isOfType(int typeCode) {
        if (Utils.isEmpty(type)) {
            return false;
        }

        return type.equals("" + typeCode);
    }

    public boolean isMissed() {
        return isOfType(android.provider.CallLog.Calls.MISSED_TYPE);
    }

    public boolean isIncoming() {
        return isOfType(android.provider.CallLog.Calls.INCOMING_TYPE);
    }

    public boolean isBlocked() {
        //TODO: why calls blocked by screening stored as missed in call log???
        return isMissed();
    }

    public boolean isOutgoing() {
        return isOfType(android.provider.CallLog.Calls.OUTGOING_TYPE);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(nationalNumber);
        parcel.writeString(type);
        parcel.writeLong(date);
        parcel.writeString(duration);
        parcel.writeString(group);
    }
}
