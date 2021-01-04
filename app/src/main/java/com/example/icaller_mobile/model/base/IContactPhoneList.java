package com.example.icaller_mobile.model.base;

public interface IContactPhoneList {
    void add(IContactPhoneObject phone);
    int size();
    void remove(int index);
   IContactPhoneObject get(int index);
}
