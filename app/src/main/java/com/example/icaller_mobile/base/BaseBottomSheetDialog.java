package com.example.icaller_mobile.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.example.icaller_mobile.R;

public abstract class BaseBottomSheetDialog<T extends ViewDataBinding, V> extends BottomSheetDialogFragment {
    public T binding;

    public V data;

    public abstract
    @LayoutRes
    int getLayoutId();

    public abstract int getBindingVariable();

    public abstract void bindingView(T binding);

    public abstract V getData();

    public Context context;

    public ItemBottomSheetClick<V> listener;

    public BaseBottomSheetDialog(@NonNull Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(ItemBottomSheetClick<V> listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutId(), null, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
    }


    private void initLayout() {
        binding.setVariable(getBindingVariable(), getData());
        binding.executePendingBindings();
        bindingView(binding);
        data = getData();
    }

    protected boolean cancelable() {
        return true;
    }

    public interface ItemBottomSheetClick<V> {
        default void onClick(V data) {
        }

        default void onItemClick(V data, int position) {
        }
    }
}


