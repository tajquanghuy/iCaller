package com.example.icaller_mobile.model.network.room;

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
public interface DataBeanDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> insert(DataBean dataBean);

    @Insert
    Maybe<List<Long>> insertAll(List<DataBean> user);

    @Query("SELECT * FROM table_bean ORDER BY  id DESC ")
    Observable<List<DataBean>> getAllBlockContact();

    @Delete
    Single<Integer> delete(DataBean dataBean);

    @Delete
    void unBlock(DataBean dataBean);

    @Query("DELETE FROM table_bean")
    void deleteAll();

    @Query("SELECT * FROM table_bean WHERE phone = :phoneNumber")
    Observable<List<DataBean>> getContactServer(String phoneNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> insertFromServer(DataBean dataBean);
}
