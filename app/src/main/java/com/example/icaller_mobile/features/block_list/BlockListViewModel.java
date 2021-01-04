package com.example.icaller_mobile.features.block_list;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.icaller_mobile.base.BaseActivity;
import com.example.icaller_mobile.base.BaseViewModel;
import com.example.icaller_mobile.common.constants.FragmentTag;
import com.example.icaller_mobile.common.utils.Logger;
import com.example.icaller_mobile.features.block_contact.CreateBlockContactFragment;
import com.example.icaller_mobile.features.block_list.repository.BlockContactRepository;
import com.example.icaller_mobile.features.block_list.repository.BlockContactRepositoryImp;
import com.example.icaller_mobile.features.current_data.CurrentDataFragment;
import com.example.icaller_mobile.model.local.room.BlockContact;
import com.example.icaller_mobile.model.local.room.BlockContactDAO;
import com.example.icaller_mobile.model.local.room.BlockContactDB;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class BlockListViewModel extends BaseViewModel {
    private BlockContactRepository repository;
    public MutableLiveData<List<BlockContact>> listBlockContact = new MutableLiveData<>();
    public MutableLiveData<BlockContact> blockContacts = new MutableLiveData<>();


    public BlockListViewModel(Context context) {
        super(context);
        repository = new BlockContactRepositoryImp(context);
    }

    public void getAllBlockContact() {
        Disposable disposable = repository.getAllBlockContact()
                .subscribe(blockContacts -> {
                    listBlockContact.setValue(blockContacts);
                }, throwable -> {
                    int a = 1;
                });
    }


    public void getContactByPhoneNumber(String phone) {
        BlockContactDB blockContactDB = BlockContactDB.getDatabase(context.get());
        BlockContactDAO blockContactDAO = blockContactDB.blockContactDAO();
        Disposable disposable = blockContactDAO.getContactDevice(phone)
                .subscribe(contact -> {
                    int a = 1;
                }, throwable -> {
                    Logger.log("GET_USER_BY_ID_FAIL");
                });
    }

    public void unBlockContact(BlockContact contact) {
        Disposable disposable = (Disposable) repository.delete(contact).subscribe((integer, throwable) -> {
            Log.d("TAG", "onError: ");
        });
    }


    public void addBlockContract() {
        ((BaseActivity<?, ?>) context.get()).push(CreateBlockContactFragment.newInstance(), FragmentTag.FRAGMENT_ADD_BLOCK_CONTRACT);
    }

    public void openCurrentData() {
        ((BaseActivity<?, ?>) context.get()).push(CurrentDataFragment.newInstance(), FragmentTag.FRAGMENT_CURRENT_DATA);
        // Doing
    }


}