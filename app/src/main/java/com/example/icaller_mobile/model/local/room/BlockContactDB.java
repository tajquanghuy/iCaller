package com.example.icaller_mobile.model.local.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.icaller_mobile.model.network.room.DataBean;
import com.example.icaller_mobile.model.network.room.DataBeanDAO;

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
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }

}
