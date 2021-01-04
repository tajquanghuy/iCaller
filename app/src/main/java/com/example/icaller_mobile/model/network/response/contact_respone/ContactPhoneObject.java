package com.example.icaller_mobile.model.network.response.contact_respone;

import com.example.icaller_mobile.model.base.IContactPhoneObject;


public class ContactPhoneObject implements IContactPhoneObject {

    private String number;
    private String type;

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public ContactPhoneObject() {
    }

    public ContactPhoneObject(String number, String type) {
        this.number = number;
        this.type = type;
    }

}
