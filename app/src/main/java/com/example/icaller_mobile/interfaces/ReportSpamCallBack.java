package com.example.icaller_mobile.interfaces;

public interface ReportSpamCallBack {
    default void reportSpamSuccess() {
    }

    default void reportSpamFail(String message) {
    }

    default void unauthorized(){
    }
}
