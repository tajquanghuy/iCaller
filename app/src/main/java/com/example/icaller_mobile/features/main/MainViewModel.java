package com.example.icaller_mobile.features.main;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.icaller_mobile.base.BaseViewModel;

public class MainViewModel extends BaseViewModel {

    public MutableLiveData<Boolean> updateDataLiveData = new MutableLiveData<>();

    public MainViewModel(Context context) {
        super(context);
    }

    public void updateData() {
        if (updateDataLiveData.getValue() == null)
            updateDataLiveData.setValue(true);
        else
            updateDataLiveData.setValue(!updateDataLiveData.getValue());
    }


}
