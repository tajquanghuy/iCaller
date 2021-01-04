package com.example.icaller_mobile.model.network.response.contact_respone;


import com.example.icaller_mobile.model.base.IContactPhoneList;
import com.example.icaller_mobile.model.base.IContactPhoneObject;

import java.util.List;

public class ContactPhoneList implements IContactPhoneList {

    private List<IContactPhoneObject> phones;

    public List<IContactPhoneObject> getPhones() {
        return phones;
    }

    public ContactPhoneList(List<IContactPhoneObject> phones) {
        this.phones = phones;
    }

    @Override
    public void add(IContactPhoneObject phone) {
        phones.add(phone);
    }

    @Override
    public int size() {
        return phones == null ? 0 : phones.size();
    }

    @Override
    public void remove(int index) {
        phones.remove(index);
    }

    @Override
    public IContactPhoneObject get(int index) {
        return phones == null ? null : phones.get(index);
    }
}
