package com.example.icaller_mobile.model.base;

public interface IContactList {
    int size();
    IContactObject get(int index);
    void remove(int index);
    void add(IContactObject contact);
}
