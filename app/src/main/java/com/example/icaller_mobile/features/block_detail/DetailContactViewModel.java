package com.example.icaller_mobile.features.block_detail;

import android.content.Context;
import android.util.Log;

import com.example.icaller_mobile.base.BaseViewModel;
import com.example.icaller_mobile.features.block_list.repository.BlockContactRepository;
import com.example.icaller_mobile.features.block_list.repository.BlockContactRepositoryImp;
import com.example.icaller_mobile.model.local.room.BlockContact;

import io.reactivex.disposables.Disposable;

public class DetailContactViewModel extends BaseViewModel {
    private Context mContext;
    private BlockContactRepository repository;

    public DetailContactViewModel(Context context) {
        super(context);
        this.mContext = context;
        repository = new BlockContactRepositoryImp(context);
    }

    public void unBlockContact(BlockContact contact){
        Disposable disposable = (Disposable) repository.delete(contact).subscribe((integer, throwable) -> {
            Log.d("TAG", "onError: ");
        });
    }
}