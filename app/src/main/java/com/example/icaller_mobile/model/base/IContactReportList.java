package com.example.icaller_mobile.model.base;

public interface IContactReportList {
    int size();
    IContactReportObject get(int index);
    void remove(int index);
    void add(IContactReportObject contact);
}
