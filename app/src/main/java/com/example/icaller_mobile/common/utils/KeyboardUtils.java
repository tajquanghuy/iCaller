package com.example.icaller_mobile.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {
    public static void hideKeyboard(Activity activity) {
        if (activity == null) return;
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            currentFocusedView.clearFocus();
            if (inputManager != null)
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
