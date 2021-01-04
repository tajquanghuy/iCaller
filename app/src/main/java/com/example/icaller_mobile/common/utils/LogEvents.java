package com.example.icaller_mobile.common.utils;

import android.content.Context;
import android.os.Bundle;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.lang.ref.WeakReference;

public class LogEvents {
    public static LogEvents logEvents;
    private WeakReference<Context> weakContext;

    public LogEvents(Context mContext) {
        weakContext = new WeakReference<>(mContext);
    }

    public static synchronized LogEvents init(Context mContext) {
        if (logEvents == null) {
            logEvents = new LogEvents(mContext);
        }
        return logEvents;
    }

    public static synchronized LogEvents getDefault() {
        return logEvents;
    }

    public void log(String event, Bundle bundle) {
        if (weakContext.get() == null || event == null || Utils.isEmpty(event)) return;
        FirebaseAnalytics.getInstance(weakContext.get()).logEvent(event, bundle);
        AppEventsLogger.newLogger(weakContext.get()).logEvent(event, bundle);
    }
}
