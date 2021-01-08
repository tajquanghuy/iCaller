package com.example.icaller_mobile.model.callbacks;

import com.example.icaller_mobile.model.local.room.BlockContact;
import com.example.icaller_mobile.model.network.room.DataBean;

public interface GetDisplayNameCallBacks {

    String getUserFromServer(DataBean dataBean);

    String getUserFromDevice(BlockContact blockContact);

    //String getUnknowUser();
}
