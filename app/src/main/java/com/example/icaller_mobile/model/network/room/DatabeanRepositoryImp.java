package com.example.icaller_mobile.model.network.room;

import android.content.Context;

import com.example.icaller_mobile.model.local.room.BlockContactDB;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DatabeanRepositoryImp implements DataBeanRepository {
    private DataBeanDAO dataBeanDAO;

    public DatabeanRepositoryImp(Context context) {
        BlockContactDB dataBeanDB = BlockContactDB.getDatabase(context);
        dataBeanDAO = dataBeanDB.dataBeanDAO();
    }

    @Override
    public Maybe<Long> insert(DataBean dataBean) {
        return dataBeanDAO.insert(dataBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Maybe<List<Long>> insertAll(List<DataBean> dataBeans) {
        return dataBeanDAO.insertAll(dataBeans)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<DataBean>> getAllDataBean() {
        return null;
    }

    @Override
    public Single<Integer> delete(DataBean dataBean) {
        return null;
    }

    @Override
    public Observable<List<DataBean>> getContactServer(String phoneNumber) {
        return dataBeanDAO.getContactServer(phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
