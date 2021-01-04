package com.example.icaller_mobile.model.base;


public interface IContactReportObject {
    int getId();

    void setId(int id);

    String getCode();

    void setCode(String code);

    String getPhone();

    void setPhone(String phone);

    String getName();

    void setName(String name);

    int getWarn_type();

    void setWarn_type(int warn_type);

    void setUpdatedAt(String updatedAt);

    String getUpdatedAt();

}
