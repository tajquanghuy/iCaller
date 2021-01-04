package com.example.icaller_mobile.model.network.response;

import android.os.Parcel;
import android.os.Parcelable;

public class NumberContactBlockResponse implements Parcelable {

    /**
     * data : 147
     */

    private int data;

    protected NumberContactBlockResponse(Parcel in) {
        data = in.readInt();
    }

    public static final Creator<NumberContactBlockResponse> CREATOR = new Creator<NumberContactBlockResponse>() {
        @Override
        public NumberContactBlockResponse createFromParcel(Parcel in) {
            return new NumberContactBlockResponse(in);
        }

        @Override
        public NumberContactBlockResponse[] newArray(int size) {
            return new NumberContactBlockResponse[size];
        }
    };

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(data);
    }
}
