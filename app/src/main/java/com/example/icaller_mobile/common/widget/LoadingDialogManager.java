
package com.example.icaller_mobile.common.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.common.utils.Utils;

import java.lang.ref.WeakReference;

import dmax.dialog.SpotsDialog;

public class LoadingDialogManager {
    private static LoadingDialogManager loadingDialogManager;
    private WeakReference<Context> weakContext;
    private AlertDialog dialog;

    public LoadingDialogManager(Context mContext) {
        weakContext = new WeakReference<>(mContext);
        dialog = getDialog(mContext, null, 0, true, null);
    }

    public AlertDialog getDialog(Context mContext, String message, int theme, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        return new SpotsDialog.Builder()
                .setContext(mContext)
                .setMessage(message == null || Utils.isEmpty(message) ? mContext.getString(R.string.loading) : message)
                .setTheme(theme == 0 ? R.style.CustomSpotDialog : theme)
                .setCancelable(cancelable)
                .setCancelListener(cancelListener).build();
    }

    public static synchronized LoadingDialogManager getDefault(Context mContext) {
        if (loadingDialogManager == null) {
            loadingDialogManager = new LoadingDialogManager(mContext);
        } else {
            loadingDialogManager.weakContext = new WeakReference<>(mContext);
        }
        return loadingDialogManager;
    }

    public void setMessage(String message) {
        if (dialog == null) {
            dialog = getDialog(weakContext.get(), message, 0, true, null);
        }
        dialog.setMessage(message);
    }

    public void showDialog() {
        if (dialog == null) {
            dialog = getDialog(weakContext.get(), null, 0, true, null);
        }
        dialog.show();
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.hide();
            dialog = null;
        }
    }
}
