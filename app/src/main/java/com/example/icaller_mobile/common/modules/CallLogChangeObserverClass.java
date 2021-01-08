package com.example.icaller_mobile.common.modules;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.example.icaller_mobile.common.manager.CallLogManager;


public class CallLogChangeObserverClass extends ContentObserver {
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    private Context context;

    public CallLogChangeObserverClass(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        CallLogManager.getDefault(context).getCallLogGroupsAsync();
    }
}
