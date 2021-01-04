package com.example.icaller_mobile.features.settings.settings_about;

import android.content.Context;


import androidx.lifecycle.MutableLiveData;

import com.example.icaller_mobile.BuildConfig;
import com.example.icaller_mobile.base.BaseViewModel;

public class AboutViewModel extends BaseViewModel {
    public MutableLiveData<String> txtVersion = new MutableLiveData<>();

    public AboutViewModel(Context context) {
        super(context);
        txtVersion.setValue(BuildConfig.VERSION_NAME);
    }
}
