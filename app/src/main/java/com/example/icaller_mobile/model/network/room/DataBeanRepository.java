package com.example.icaller_mobile.model.network.room;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface DataBeanRepository {
    Maybe<Long> insert(DataBean dataBean);

    Maybe<List<Long>> insertAll(List<DataBean> dataBeans);

    Observable<List<DataBean>> getAllDataBean();

    Single<Integer> delete(DataBean dataBean);

    Observable<List<DataBean>> getContactServer(String phoneNumber);

}
