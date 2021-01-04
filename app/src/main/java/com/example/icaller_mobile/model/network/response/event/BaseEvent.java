package com.example.icaller_mobile.model.network.response.event;

public abstract class BaseEvent {
    private BaseEvent() {}

    public BaseEvent(String name) {
        this.name = name;
    }

    public String name;
}
