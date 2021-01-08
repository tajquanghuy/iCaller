package com.example.icaller_mobile.features.block_list.repository;

import com.example.icaller_mobile.model.local.room.BlockContact;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface BlockContactRepository {
    Observable<List<BlockContact>> getAllBlockContact();

    Observable<List<BlockContact>> getContactDevice(String phone);

    Observable<BlockContact> getContactLocal(String phone);

    Maybe<Long> insert(BlockContact blockContact);

    Single<Integer> delete(BlockContact blockContact);



}
