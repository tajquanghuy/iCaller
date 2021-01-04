package com.example.icaller_mobile.model.report;


import com.example.icaller_mobile.common.utils.Utils;

public class CallLogRequestServer {
    private String phone;
    private int type;
    private String time;
    private int duration;
    private int in_contact;

    public CallLogRequestServer() {
    }

    public CallLogRequestServer(String phone, int type, String time, int duration, int in_contact) {
        this.phone = phone;
        this.type = type;
        this.time = time;
        this.duration = duration;
        this.in_contact = in_contact;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIn_contact() {
        return in_contact;
    }

    public void setIn_contact(int in_contact) {
        this.in_contact = in_contact;
    }

    public boolean isOfType(int typeCode) {
        if (Utils.isEmpty(type)) {
            return false;
        }

        return type == typeCode;
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


}
