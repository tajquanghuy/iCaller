package com.example.icaller_mobile.common.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Logger {
    public static void log(Object... objects) {
        String tag = "Err";
        if (objects == null || objects.length == 0) {
            Log.e(tag, "Nothing useful");
            return;
        }

        Exception exception = new Exception();
        StringBuilder builder = new StringBuilder();
        for (Object object : objects) {
            builder.append(object)
                    .append(" ");
        }

        Log.e(tag, String.format("%s @ %s", builder.toString().trim(), exception.getStackTrace()[1].toString()));
    }

    public static void toast(Context mContext, String message) {
        if (Utils.isEmpty(message)) return;
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

    }
}
