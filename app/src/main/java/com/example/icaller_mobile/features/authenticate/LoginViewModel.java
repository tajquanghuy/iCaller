package com.example.icaller_mobile.features.authenticate;

import android.content.Context;

import com.example.icaller_mobile.base.BaseViewModel;
import com.mukesh.OnOtpCompletionListener;

public class LoginViewModel extends BaseViewModel implements OnOtpCompletionListener {

    public LoginViewModel(Context context) {
        super(context);
    }


    public void onSignIn(){

    }

    public void onDismiss(){

    }

    public void onClearPhoneNumber(){

    }

    @Override
    public void onOtpCompleted(String otp) {

    }
}
