package com.example.icaller_mobile.features.popup;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseActivity;
import com.example.icaller_mobile.common.utils.ContactManager;
import com.example.icaller_mobile.common.utils.Logger;
import com.example.icaller_mobile.common.utils.PhoneNumberManager;
import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.databinding.NotificationPhoneCallNotSpamBinding;
import com.example.icaller_mobile.model.base.IContactObject;
import com.example.icaller_mobile.model.callbacks.CallBacks;
import com.example.icaller_mobile.model.callbacks.GetRoomDataCallBacks;
import com.example.icaller_mobile.model.local.room.BlockContact;
import com.example.icaller_mobile.model.network.room.DataBean;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

public class CallInfoOverlay implements CallBacks {
    private String phoneNumber;
    private static CallInfoOverlay callInfoOverlay;
    private View callNotificationView;
    private float deltaX = 0;
    private float deltaY = 0;
    private WindowManager windowManager;
    private int width;
    private Context context;
    private NotificationPhoneCallNotSpamBinding binding;


    public static CallInfoOverlay newInstance(Context context) {
        if (callInfoOverlay == null)
            callInfoOverlay = new CallInfoOverlay(context);
        return callInfoOverlay;
    }

    public CallInfoOverlay(Context context) {
        String number = "";
        Logger.log(number);
        this.context = context;
        ContactManager.init(context);
        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        phoneNumber = number;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            callNotificationView = inflater.inflate(R.layout.notification_phone_call_not_spam, null);
            binding = NotificationPhoneCallNotSpamBinding.bind(callNotificationView);
            @SuppressLint("ClickableViewAccessibility") View.OnTouchListener onTouchListener = (v, event) -> {
                if (v.getParent() == null) {
                    return false;
                }
                WindowManager.LayoutParams params = (WindowManager.LayoutParams) v.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        deltaX = event.getRawX() - params.x;
                        deltaY = event.getRawY() - params.y;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = Math.round(event.getRawX() - deltaX);
                        params.x = params.x < 0 || params.x > 0 ? 0 : params.x;
                        params.y = Math.round(event.getRawY() - deltaY);
                        windowManager.updateViewLayout(v, params);
                        return true;
                    case MotionEvent.ACTION_UP:
                        deltaX = 0;
                        deltaY = 0;
                        return true;
                }
                return false;
            };
            callNotificationView.setOnTouchListener(onTouchListener);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            width = displayMetrics.widthPixels;

        }

    }

    private void showPopUp() {
        binding.textLineHorizontal.setVisibility(View.VISIBLE);
        if (callNotificationView.getParent() != null) {
            return;
        }
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        int type;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M && keyguardManager != null && keyguardManager.isDeviceLocked()) {
                type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
            }
        }
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                width - 16,
                WindowManager.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.RGBA_8888);
        params.gravity = Gravity.CENTER;
        windowManager.addView(callNotificationView, params);
    }

    public void showOverlayNotification(String phoneNumber) {
        final int colorTextSpam = context.getColor(R.color.color_red_dark);
        final int colorTextNotSpam = context.getColor(R.color.color_purple_dark);
        String national = PhoneNumberManager.getInstance(context).getDisplayNameCountry(context, phoneNumber) == null ? context.getString(R.string.unknown) : PhoneNumberManager.getInstance(context).getDisplayNameCountry(context, phoneNumber);
        String mobileNetwork = PhoneNumberManager.getInstance(context).getNetworkMobile(phoneNumber) == null ? context.getString(R.string.unknown) : (context.getString(R.string.mobile) + " - " + PhoneNumberManager.getInstance(context).getNetworkMobile(phoneNumber));
        String displayName;
        int warn;
        // Contact
        IContactObject contactDevice = ContactManager.getDefault().getDeviceContact(phoneNumber);
        if (contactDevice != null) {
            displayName = (contactDevice.getName() == null || Utils.isEmpty(contactDevice.getName())) ? Utils.Number(phoneNumber) : contactDevice.getName();
            binding.textBlockName.setText(displayName);
            binding.viewIcon.setVisibility(View.GONE);
            binding.textTypeSpam.setText(R.string.key_not_spam);
            binding.textTypeSpam.setTextColor(colorTextNotSpam);
            showPopUp();
        } else {
            ContactManager.getDefault().zipObservable(phoneNumber, new GetRoomDataCallBacks() {
                // Server
                @Override
                public void getUserFromServer(DataBean dataBean) {
                    binding.textBlockName.setText(dataBean.getName());
                    binding.viewBackGround.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_end_call_spam));
                    int warnType = dataBean.getWarn_type();
                    switch (warnType) {
                        case 1:
                            binding.viewBackGround.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_end_call_not_spam));
                            binding.viewIcon.setVisibility(View.GONE);
                            binding.textTypeSpam.setText(R.string.key_not_spam);
                            binding.textTypeSpam.setTextColor(colorTextNotSpam);
                        case 2:
                            binding.viewIcon.setVisibility(View.VISIBLE);
                            binding.imgTypeSpam.setImageResource(R.drawable.ic_white_advertising);
                            binding.textTypeSpam.setText(R.string.advertising);
                            binding.textTypeSpam.setTextColor(colorTextSpam);
                            break;
                        case 3:
                            binding.viewIcon.setVisibility(View.VISIBLE);
                            binding.imgTypeSpam.setImageResource(R.drawable.ic_white_financial_service);
                            binding.textTypeSpam.setText(R.string.financial_service);
                            binding.textTypeSpam.setTextColor(colorTextSpam);
                            break;
                        case 4:
                            binding.viewIcon.setVisibility(View.VISIBLE);
                            binding.imgTypeSpam.setImageResource(R.drawable.ic_white_loan_collection);
                            binding.textTypeSpam.setText(R.string.loan_collection);
                            binding.textTypeSpam.setTextColor(colorTextSpam);
                            break;
                        case 5:
                            binding.viewIcon.setVisibility(View.VISIBLE);
                            binding.imgTypeSpam.setImageResource(R.drawable.ic_white_scam);
                            binding.textTypeSpam.setText(R.string.scam);
                            binding.textTypeSpam.setTextColor(colorTextSpam);
                            break;
                        case 6:
                            binding.viewIcon.setVisibility(View.VISIBLE);
                            binding.imgTypeSpam.setImageResource(R.drawable.ic_white_real_estate);
                            binding.textTypeSpam.setText(R.string.real_estate);
                            binding.textTypeSpam.setTextColor(colorTextSpam);
                            break;
                        case 7:
                            binding.viewIcon.setVisibility(View.VISIBLE);
                            binding.imgTypeSpam.setImageResource(R.drawable.ic_white_other);
                            binding.textTypeSpam.setText(R.string.other);
                            binding.textTypeSpam.setTextColor(colorTextSpam);
                            break;
                    }
                    showPopUp();
                }

                // Local
                @Override
                public void getUserFromDevice(BlockContact blockContact) {
                    Utils.endCall(context);
                }


                // Unknow number
                @Override
                public void getUnknowUser() {
                    binding.viewBackGround.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_end_call_not_spam));
                    binding.viewIcon.setVisibility(View.GONE);
                    binding.textBlockName.setText(phoneNumber);
                    binding.textBlockPhoneNumber.setVisibility(View.GONE);
                    binding.textTypeSpam.setText(R.string.key_not_spam);
                    showPopUp();
                }
            });
        }

        binding.textBlockPhoneNumber.setText(phoneNumber);
        binding.textMobileNetwork.setText(mobileNetwork);
        binding.textNational.setText(national);
        binding.imgClose.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_SENDTO);
            ((BaseActivity) context).startActivity(intent1);
            windowManager.removeView(callNotificationView);
            Logger.log("REMOVIEW");
        });
    }

    @Override
    public void onDestroyOverPlay(String phone) {

        Logger.log("CALLBACKS" + phone);
        if (callNotificationView.getWindowToken() !=  null){
            windowManager.removeView(callNotificationView);
        }
    }


    @Override
    public void onExtractCallerInformation(String phone) {
        (new Thread() {
            @Override
            public void run() {
                super.run();
                if (Utils.isEmpty(phone)) {
                    return;
                }
                new Handler(Looper.getMainLooper()).post(() ->
                        showOverlayNotification(phone));
            }
        }).start();
    }


}


