package com.example.icaller_mobile.model.local.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.commonsware.cwac.saferoom.SQLCipherUtils;
import com.commonsware.cwac.saferoom.SafeHelperFactory;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.model.network.room.DataBean;
import com.example.icaller_mobile.model.network.room.DataBeanDAO;

import java.io.IOException;

@Database(entities = {BlockContact.class, DataBean.class}, version = 1, exportSchema = false)
public abstract class BlockContactDB extends RoomDatabase {
    public abstract BlockContactDAO blockContactDAO();
    public abstract DataBeanDAO dataBeanDAO();
    public static volatile BlockContactDB instance;

    public static BlockContactDB getDatabase(final Context context) {
        if (instance == null) {
            synchronized (BlockContactDB.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), BlockContactDB.class, "db_contact_block")
                            .openHelperFactory(new SafeHelperFactory(context.getText(R.string.key_decrypt_room_db).toString().toCharArray()))
                            .allowMainThreadQueries()
                            .build();

                }
            }
        }
        return instance;
    }

}
