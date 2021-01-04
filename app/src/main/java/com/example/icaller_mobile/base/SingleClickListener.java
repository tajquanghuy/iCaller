package com.example.icaller_mobile.base;

import android.os.Handler;
import android.os.SystemClock;
import android.view.View;

public abstract class SingleClickListener implements View.OnClickListener {
    private static final long MIN_CLICK_INTERVAL = 600;
    private long mLastClickTime;
    public static boolean isViewClicked = false;

    public abstract void onSingleClick(View view);

    @Override
    public void onClick(View view) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;
        if (elapsedTime <= MIN_CLICK_INTERVAL)
            return;
        if (!isViewClicked) {
            isViewClicked = true;
            startTimer();
        } else {
            return;
        }
        onSingleClick(view);
    }

    private void startTimer() {
        Handler handler = new Handler();
        handler.postDelayed(() -> isViewClicked = false, MIN_CLICK_INTERVAL);

    }
}
