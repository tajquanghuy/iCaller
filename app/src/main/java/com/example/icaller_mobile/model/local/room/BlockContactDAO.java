package com.example.icaller_mobile.model.local.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface BlockContactDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> insert(BlockContact blockContact);

    @Query("SELECT * FROM tbl_block_contact ORDER BY  id DESC ")
    Observable<List<BlockContact>> getAllBlockContact();

    @Query("SELECT * FROM tbl_block_contact WHERE phone_number =:phone ")
    Observable<List<BlockContact>> getContactDevice(String phone);

    @Delete
    Single<Integer> delete(BlockContact contact);

    @Delete
    void unBlock(BlockContact contact);

    @Query("DELETE FROM tbl_block_contact")
    void deleteAll();


}
