package com.example.icaller_mobile.base;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel {

    public MutableLiveData<Boolean> progressLoading = new MutableLiveData<>();

    private CompositeDisposable mCompositeDisposable;

    protected WeakReference<Context> context;

    public BaseViewModel(Context context) {
        this.mCompositeDisposable = new CompositeDisposable();
        this.context = new WeakReference<>(context);
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return progressLoading;
    }

}
