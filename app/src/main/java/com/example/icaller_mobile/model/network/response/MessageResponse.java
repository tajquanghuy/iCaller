package com.example.icaller_mobile.model.network.response;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageResponse implements Parcelable {

    /**
     * message : Data has been updated
     */

    private String message;

    protected MessageResponse(Parcel in) {
        message = in.readString();
    }

    public static final Creator<MessageResponse> CREATOR = new Creator<MessageResponse>() {
        @Override
        public MessageResponse createFromParcel(Parcel in) {
            return new MessageResponse(in);
        }

        @Override
        public MessageResponse[] newArray(int size) {
            return new MessageResponse[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
    }

}
