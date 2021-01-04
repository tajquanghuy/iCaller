package com.example.icaller_mobile.common.widget.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.icaller_mobile.BR;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseDialog;
import com.example.icaller_mobile.databinding.DialogConfirmBinding;
import com.example.icaller_mobile.features.settings.settings_logout.DialogOnclickListener;


public class ConfirmDialog extends BaseDialog<DialogConfirmBinding, ContentDialog> implements View.OnClickListener {
    private ContentDialog contentDialog;
    private DialogOnclickListener listener;

    public ConfirmDialog(@NonNull Context context, String title, String message, String textConfirmButton, DialogOnclickListener listener) {
        super(context);
        this.context = context;
        this.contentDialog = new ContentDialog(title, null, message, textConfirmButton, null);
        this.listener = listener;
    }

    public void setListener(DialogOnclickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_confirm;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public void bindingView(DialogConfirmBinding binding) {
        binding.layoutTitle.imgClose.setOnClickListener(this);
        binding.btnConfirm.setOnClickListener(this);
    }

    @Override
    public ContentDialog getData() {
        return contentDialog;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgClose:
                if (listener != null) listener.onImageCloseClick();
                dismiss();
                break;
            case R.id.btnConfirm:
                if (listener != null) listener.onButtonConfirmClick();
                dismiss();
                break;
        }
    }
}
