package com.example.icaller_mobile.interfaces;

import com.example.icaller_mobile.model.local.room.BlockContact;

public interface ContactBlockOnClickListener {
    void onItemClick(BlockContact contact);

    default void unblockContact(BlockContact contact){};
}
