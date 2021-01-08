package com.example.icaller_mobile.model.report;

import java.util.List;

public class CallLogGroup {
    public String title;
    public List<CallLog> list;

    public CallLogGroup() {
        this.title = title;
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CallLog> getList() {
        return list;
    }

    public void setList(List<CallLog> list) {
        this.list = list;
    }
}
