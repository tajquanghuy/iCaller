package com.example.icaller_mobile.model.local.room;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import javax.annotation.Nonnull;

@Entity(tableName = "tbl_block_contact")
public class BlockContact implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @Nonnull
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @ColumnInfo(name = "type_report")
    private int typeReport;

    public BlockContact(String name, String phoneNumber, int typeReport) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.typeReport = typeReport;
    }

    protected BlockContact(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phoneNumber = in.readString();
        typeReport = in.readInt();
    }

    public static final Creator<BlockContact> CREATOR = new Creator<BlockContact>() {
        @Override
        public BlockContact createFromParcel(Parcel in) {
            return new BlockContact(in);
        }

        @Override
        public BlockContact[] newArray(int size) {
            return new BlockContact[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getTypeReport() {
        return typeReport;
    }

    public void setTypeReport(int typeReport) {
        this.typeReport = typeReport;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeInt(typeReport);
    }
}
