package com.example.icaller_mobile.base;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private String TAG = "SingleLiveEvent";
    private AtomicBoolean mPending = new AtomicBoolean(false);

    @MainThread
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }
        super.observe(owner, it -> {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it);
            }
        });
    }

    @MainThread
    @Override
    public void setValue(T value) {
        mPending.set(true);
        super.setValue(value);
    }

    @MainThread
    public void call() {
        setValue(null);
    }
}
