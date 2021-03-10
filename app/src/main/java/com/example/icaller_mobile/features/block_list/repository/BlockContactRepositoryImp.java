package com.example.icaller_mobile.features.block_list.repository;

import android.content.Context;

import com.commonsware.cwac.saferoom.SQLCipherUtils;
import com.example.icaller_mobile.model.local.room.BlockContact;
import com.example.icaller_mobile.model.local.room.BlockContactDAO;
import com.example.icaller_mobile.model.local.room.BlockContactDB;

import java.io.File;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BlockContactRepositoryImp implements BlockContactRepository {
    private BlockContactDAO blockContactDAO;


    public BlockContactRepositoryImp(Context context) {
        BlockContactDB blockContactDB = BlockContactDB.getDatabase(context);
        blockContactDAO = blockContactDB.blockContactDAO();
    }


    @Override
    public Observable<List<BlockContact>> getAllBlockContact() {
        return blockContactDAO.getAllBlockContact()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<BlockContact>> getContactDevice(String phone) {
        return blockContactDAO.getListContactLocal(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BlockContact> getContactLocal(String phone) {
        return blockContactDAO.getContactLocal(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Maybe<Long> insert(BlockContact blockContact) {
        return blockContactDAO.insert(blockContact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Single<Integer> delete(BlockContact blockContact) {
        return blockContactDAO.delete(blockContact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
