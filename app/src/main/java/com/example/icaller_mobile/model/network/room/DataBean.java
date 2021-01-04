package com.example.icaller_mobile.model.network.room;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.icaller_mobile.model.base.IContactReportObject;

/**
 *
 */
@Entity(tableName = "table_bean")
public class DataBean implements IContactReportObject, Parcelable {
    /**
     * id : 93731
     * code : +84
     * phone : +84982171696
     * name : Hồng Nhạn
     * warn_type : 1
     * updated_at : 2020-05-04 06:58:52
     * user : null
     */


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "code")
    private String code;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "warn_type")
    private int warn_type;

    @ColumnInfo(name = "updated_at")
    protected String updated_at;

//    public DataBean() {
//    }

    public DataBean(String code, String phone, String name, int warn_type, String updated_at) {
        this.code = code;
        this.phone = phone;
        this.name = name;
        this.warn_type = warn_type;
        this.updated_at = updated_at;
    }

    protected DataBean(Parcel in) {
        id = in.readInt();
        code = in.readString();
        phone = in.readString();
        name = in.readString();
        warn_type = in.readInt();
        updated_at = in.readString();
    }

    public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
        @Override
        public DataBean createFromParcel(Parcel in) {
            return new DataBean(in);
        }

        @Override
        public DataBean[] newArray(int size) {
            return new DataBean[size];
        }
    };

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
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
    public int getWarn_type() {
        return warn_type;
    }

    @Override
    public void setWarn_type(int warn_type) {
        this.warn_type = warn_type;
    }

    @Override
    public void setUpdatedAt(String updatedAt) {
        this.updated_at = updatedAt;
    }

    @Override
    public String getUpdatedAt() {
        return updated_at;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(code);
        dest.writeString(phone);
        dest.writeString(name);
        dest.writeInt(warn_type);
        dest.writeString(updated_at);
    }
}

