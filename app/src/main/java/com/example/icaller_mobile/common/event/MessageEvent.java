package com.example.icaller_mobile.common.event;

/**
 * Created by chungnt on 29/7/2019 AD.
 */

public class MessageEvent {
    public String action;
    public Object object;
    public String cookie;

    public MessageEvent(String action, Object object, String cookie) {
        this.action = action;
        this.object = object;
        this.cookie = cookie;
    }
}
