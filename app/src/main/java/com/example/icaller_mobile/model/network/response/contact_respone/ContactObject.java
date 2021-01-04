package com.example.icaller_mobile.model.network.response.contact_respone;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.icaller_mobile.model.base.IContactObject;
import com.example.icaller_mobile.model.base.IContactPhoneList;

import java.util.ArrayList;



public class ContactObject implements Parcelable, IContactObject {
    private String id;
    private String name;
    private String lookupKey;
    private String group;
    private IContactPhoneList numbers = new ContactPhoneList(new ArrayList<>());
    private int warnType;

    protected ContactObject(Parcel in) {
        id = in.readString();
        name = in.readString();
        lookupKey = in.readString();
        group = in.readString();
        warnType = in.readInt();
    }

    public static final Creator<ContactObject> CREATOR = new Creator<ContactObject>() {
        @Override
        public ContactObject createFromParcel(Parcel in) {
            return new ContactObject(in);
        }

        @Override
        public ContactObject[] newArray(int size) {
            return new ContactObject[size];
        }
    };

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String getLookupKey() {
        return lookupKey;
    }

    @Override
    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public IContactPhoneList getNumbers() {
        return this.numbers;
    }

    @Override
    public void setNumbers(IContactPhoneList numbers) {
        this.numbers = numbers;
    }

    @Override
    public int getWarnType() {
        return warnType;
    }

    @Override
    public void setWarnType(int warnType) {
        this.warnType = warnType;
    }

    public ContactObject() {
    }

    public ContactObject(String id, String name, String lookupKey) {
        this.id = id;
        this.name = name;
        this.lookupKey = lookupKey;
    }
    public ContactObject(String id, String name, String lookupKey, String group, IContactPhoneList numbers) {
        this.id = id;
        this.name = name;
        this.lookupKey = lookupKey;
        this.group = group;
        this.numbers = numbers;
    }


    public ContactObject(String id, String name, String lookupKey, int warnType) {
        this.id = id;
        this.name = name;
        this.lookupKey = lookupKey;
        this.warnType = warnType;
    }

    public void addNumber(String number, String type) {
        this.numbers.add(new ContactPhoneObject(number, type));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(lookupKey);
        dest.writeString(group);
        dest.writeInt(warnType);
    }
}
