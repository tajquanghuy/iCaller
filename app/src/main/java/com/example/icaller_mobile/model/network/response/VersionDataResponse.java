package com.example.icaller_mobile.model.network.response;

import android.os.Parcel;
import android.os.Parcelable;

public class VersionDataResponse implements Parcelable {

    /**
     * version : 20.02.18.04.02.01
     */

    private String version;

    protected VersionDataResponse(Parcel in) {
        version = in.readString();
    }

    public static final Creator<VersionDataResponse> CREATOR = new Creator<VersionDataResponse>() {
        @Override
        public VersionDataResponse createFromParcel(Parcel in) {
            return new VersionDataResponse(in);
        }

        @Override
        public VersionDataResponse[] newArray(int size) {
            return new VersionDataResponse[size];
        }
    };

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(version);
    }
}
