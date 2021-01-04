package com.example.icaller_mobile.interfaces;


import com.example.icaller_mobile.model.network.response.NumberContactBlockResponse;

public interface GetNumberPhoneDBCallBack {

    default void getNumberPhoneDBSuccess(NumberContactBlockResponse numberContactBlockResponse) {
    }

    default void getNumberPhoneDBFail(String message) {
    }

    default void unauthorized() {
    }
}
