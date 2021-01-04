package com.example.icaller_mobile.model.network.response;

import android.os.Parcel;
import android.os.Parcelable;

public class StatusTokenResponse implements Parcelable {

    /**
     * valid : 0
     */

    private int valid;

    protected StatusTokenResponse(Parcel in) {
        valid = in.readInt();
    }

    public static final Creator<StatusTokenResponse> CREATOR = new Creator<StatusTokenResponse>() {
        @Override
        public StatusTokenResponse createFromParcel(Parcel in) {
            return new StatusTokenResponse(in);
        }

        @Override
        public StatusTokenResponse[] newArray(int size) {
            return new StatusTokenResponse[size];
        }
    };

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(valid);
    }
}
