package com.example.icaller_mobile.common.utils;

/**
 * Created by baonv on 17,July,2019
 * Hiworld JSC.
 */
public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;

    public CustomExceptionHandler() {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread t, Throwable e) {
        defaultUEH.uncaughtException(t, e);
    }
}
