package com.example.icaller_mobile.features.settings.settings_logout;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.icaller_mobile.BR;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseDialog;
import com.example.icaller_mobile.common.widget.dialog.ContentDialog;
import com.example.icaller_mobile.databinding.DialogSelectionBinding;


public class SelectionDialog extends BaseDialog<DialogSelectionBinding, ContentDialog> implements View.OnClickListener {
    private ContentDialog contentDialog;
    private DialogOnclickListener listener;

    public SelectionDialog(@NonNull Context context, String title, String message, String textStartButton, String textEndButton, DialogOnclickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        contentDialog = new ContentDialog(title, null, message, textStartButton, textEndButton);
    }

    public void setListener(DialogOnclickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_selection;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public void bindingView(DialogSelectionBinding binding) {
        binding.layoutTitle.imgClose.setOnClickListener(this);
        binding.btnStart.setOnClickListener(this);
        binding.btnEnd.setOnClickListener(this);
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
            case R.id.btnStart:
                if (listener != null) listener.onButtonStartClick();
                dismiss();
                break;
            case R.id.btnEnd:
                if (listener != null) listener.onButtonEndClick();
                dismiss();
                break;
        }
    }
}
