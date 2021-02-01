package com.example.icaller_mobile.common.widget.dialog;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseDialog;
import com.example.icaller_mobile.databinding.DialogSelectionBinding;

public class PhoneSelectionDialog extends BaseDialog<DialogSelectionBinding, ContentDialog> {


    public PhoneSelectionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_phone_selection;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public void bindingView(DialogSelectionBinding binding) {

    }

    @Override
    public ContentDialog getData() {
        return null;
    }
}
