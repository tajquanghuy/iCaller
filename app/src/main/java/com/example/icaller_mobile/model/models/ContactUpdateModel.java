package com.example.icaller_mobile.model.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactUpdateModel implements Parcelable {

    public ContactUpdateModel() {
    }

    public ContactUpdateModel(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    /**
     * name : chung 3
     * phone : 123456789
     */

    private String name;
    private String phone;

    protected ContactUpdateModel(Parcel in) {
        name = in.readString();
        phone = in.readString();
    }

    public static final Creator<ContactUpdateModel> CREATOR = new Creator<ContactUpdateModel>() {
        @Override
        public ContactUpdateModel createFromParcel(Parcel in) {
            return new ContactUpdateModel(in);
        }

        @Override
        public ContactUpdateModel[] newArray(int size) {
            return new ContactUpdateModel[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
    }
}
