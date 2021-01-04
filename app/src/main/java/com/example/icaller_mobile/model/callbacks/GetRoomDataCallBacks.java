package com.example.icaller_mobile.model.callbacks;

import com.example.icaller_mobile.model.local.room.BlockContact;
import com.example.icaller_mobile.model.network.room.DataBean;

public interface GetRoomDataCallBacks {

    void getUserFromServer(DataBean dataBean);

    void getUserFromDevice(BlockContact blockContact);

    void getUnknowUser();
}
