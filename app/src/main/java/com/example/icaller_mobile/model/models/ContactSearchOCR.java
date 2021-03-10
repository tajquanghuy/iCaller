package com.example.icaller_mobile.model.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactSearchOCR implements Parcelable {
    private String phone;

    public ContactSearchOCR(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static Creator<ContactSearchOCR> getCREATOR() {
        return CREATOR;
    }

    protected ContactSearchOCR(Parcel in) {
    }

    public static final Creator<ContactSearchOCR> CREATOR = new Creator<ContactSearchOCR>() {
        @Override
        public ContactSearchOCR createFromParcel(Parcel in) {
            return new ContactSearchOCR(in);
        }

        @Override
        public ContactSearchOCR[] newArray(int size) {
            return new ContactSearchOCR[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
