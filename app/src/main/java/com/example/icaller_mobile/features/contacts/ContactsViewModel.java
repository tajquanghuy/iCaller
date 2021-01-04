package com.example.icaller_mobile.features.contacts;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.icaller_mobile.base.BaseViewModel;
import com.example.icaller_mobile.model.base.IContactObject;

import java.util.List;

public class ContactsViewModel extends BaseViewModel {
    MutableLiveData<List<IContactObject>> listIContactObject = new MutableLiveData<>();

    public ContactsViewModel(Context context) {
        super(context);
    }

    public void getAllContactDevice(){
    }
    // TODO: Implement the ViewModel
}