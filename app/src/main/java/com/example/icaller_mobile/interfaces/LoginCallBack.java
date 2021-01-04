package com.example.icaller_mobile.interfaces;


import com.example.icaller_mobile.model.network.response.User;

public interface LoginCallBack {
    default void loginSuccess(User user) {
    }

    default void loginFail(String message) {
    }

    default void unauthorized(){
    }
}
