package com.example.icaller_mobile.common.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.event.EventBusAction;
import com.example.icaller_mobile.common.event.MessageEvent;
import com.example.icaller_mobile.common.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;


public class DialogManager {

    public static void setSizeDialog(Context mContext, Window window) {
        if (mContext == null || window == null) return;
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int screen_width = size.x;
        window.setLayout((int) (screen_width * 0.9), LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
    }

    public static void showDialogNotInternet(Context mContext) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_warning);
        if (dialog.getWindow() != null) {
            setSizeDialog(mContext, dialog.getWindow());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
            AppCompatImageView imgClose = dialog.findViewById(R.id.img_close_dialog);
            AppCompatTextView textMessageDialog = dialog.findViewById(R.id.text_message_dialog);
            textMessageDialog.setText(R.string.msg_no_internet_access);
            imgClose.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        }
    }

    public static void showDialog(Context mContext, Drawable icon, String title, String message, String textPositiveButton, DialogInterface.OnClickListener positiveButtonListener,
                                  String textNegativeButton, DialogInterface.OnClickListener negativeButtonListener, boolean cancelOutside) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(textPositiveButton, positiveButtonListener)
                .setNegativeButton(textNegativeButton, negativeButtonListener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(cancelOutside);
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(mContext.getColor(R.color.color_purple_dark));
        }
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(mContext.getColor(R.color.color_purple_dark));
        }
    }


    public static void showDialogConfirmSuccess(Context mContext, Drawable icon, String message, String textButton, View.OnClickListener onClickListener) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_comfirm);
        if (dialog.getWindow() != null) {
            setSizeDialog(mContext, dialog.getWindow());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
            AppCompatButton btnOK = dialog.findViewById(R.id.btn_ok);
            AppCompatTextView textMessage = dialog.findViewById(R.id.text_message_confirm);
            AppCompatImageView imgIcon = dialog.findViewById(R.id.img_icon_dialog);
            textMessage.setText(message);
            btnOK.setText((textButton != null && !Utils.isEmpty(textButton)) ? textButton : mContext.getText(android.R.string.ok).toString());
            imgIcon.setImageDrawable(icon);
            if (onClickListener != null) {
                btnOK.setOnClickListener(onClickListener);
            } else {
                btnOK.setOnClickListener(v -> dialog.dismiss());
            }
            dialog.show();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ((Activity) mContext).runOnUiThread(() -> {
                        if (dialog.isShowing()) {
                            btnOK.setOnClickListener(v -> dialog.dismiss());
                        }
                    });
                }
            }, Constants.TIME_AUTO_CLOSE_DIALOG);
        }
    }

    public static void showDialogConfirm(Context mContext, Drawable icon, String message, String textButton, View.OnClickListener onClickListener) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_comfirm);
        if (dialog.getWindow() != null) {
            setSizeDialog(mContext, dialog.getWindow());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
            AppCompatButton btnOK = dialog.findViewById(R.id.btn_ok);
            AppCompatTextView textMessage = dialog.findViewById(R.id.text_message_confirm);
            AppCompatImageView imgIcon = dialog.findViewById(R.id.img_icon_dialog);
            textMessage.setText(message);
            btnOK.setText((textButton != null && !Utils.isEmpty(textButton)) ? textButton : mContext.getText(android.R.string.ok).toString());
            imgIcon.setImageDrawable(icon);
            if (onClickListener != null) {
                btnOK.setOnClickListener(onClickListener);
            } else {
                btnOK.setOnClickListener(v -> dialog.dismiss());
            }
            dialog.show();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ((Activity) mContext).runOnUiThread(() -> {
                        if (dialog.isShowing()) {
                            if (onClickListener != null) {
                                btnOK.setOnClickListener(onClickListener);
                            } else {
                                btnOK.setOnClickListener(v -> dialog.dismiss());
                            }
                        }
                    });
                }
            }, Constants.TIME_AUTO_CLOSE_DIALOG);
        }
    }

    public static void showDialogUnblockSuccess(Context mContext) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_comfirm);
        if (dialog.getWindow() != null) {
            setSizeDialog(mContext, dialog.getWindow());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
            AppCompatButton btnOK = dialog.findViewById(R.id.btn_ok);
            AppCompatTextView textMessage = dialog.findViewById(R.id.text_message_confirm);
            textMessage.setText(mContext.getText(R.string.key_number_unblocked).toString());
            btnOK.setOnClickListener(v -> {
                dialog.dismiss();
                EventBus.getDefault().postSticky(new MessageEvent(EventBusAction.DATA_BLOCK_CHANGE, null, null));
            });
            dialog.show();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ((Activity) mContext).runOnUiThread(() -> {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            EventBus.getDefault().postSticky(new MessageEvent(EventBusAction.DATA_BLOCK_CHANGE, null, null));
                        }
                    });
                }
            }, Constants.TIME_AUTO_CLOSE_DIALOG);
        }
    }

}
