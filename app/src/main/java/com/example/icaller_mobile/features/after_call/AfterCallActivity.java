//package com.example.icaller_mobile.features.after_call;
//
//import android.graphics.Color;
//import android.graphics.Point;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//
//import android.telephony.TelephonyManager;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.core.content.ContextCompat;
//import androidx.databinding.library.baseAdapters.BR;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.icaller_mobile.R;
//import com.example.icaller_mobile.base.BaseActivity;
//import com.example.icaller_mobile.base.ViewModelProviderFactory;
//import com.example.icaller_mobile.common.constants.Constants;
//import com.example.icaller_mobile.common.constants.FirebaseConstants;
//import com.example.icaller_mobile.common.constants.IntentConstants;
//import com.example.icaller_mobile.common.manager.CallLogManager;
//import com.example.icaller_mobile.common.utils.ContactManager;
//import com.example.icaller_mobile.common.utils.LogEvents;
//import com.example.icaller_mobile.common.utils.Logger;
//import com.example.icaller_mobile.common.utils.PhoneNumberManager;
//import com.example.icaller_mobile.common.utils.Utils;
//import com.example.icaller_mobile.databinding.ActivityAfterCallBinding;
//import com.example.icaller_mobile.model.base.IContactObject;
//import com.example.icaller_mobile.model.base.IContactReportObject;
//import com.example.icaller_mobile.model.callbacks.GetDisplayNameCallBacks;
//import com.example.icaller_mobile.model.local.room.BlockContact;
//import com.example.icaller_mobile.model.network.room.DataBean;
//import com.example.icaller_mobile.model.report.CallLog;
//
//import java.lang.reflect.Field;
//
//public class AfterCallActivity extends BaseActivity<ActivityAfterCallBinding, AfterCallViewModel> {
//    private IContactReportObject contactBlock;
//    private String phoneNumber, mobileNetwork, national, displayName;
//    private CallLog callLog;
//
//
//    @Override
//    public int getBindingVariable() {
//        return BR.viewModel;
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_after_call;
//    }
//
//    @Override
//    public AfterCallViewModel getViewModel() {
//        return new ViewModelProvider(this, new ViewModelProviderFactory(this)).get(AfterCallViewModel.class);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getIntent() != null) {
//            phoneNumber = getIntent().getStringExtra(IntentConstants.KEY_PHONE_NUMBER);
//        }
//        setupDialog();
//    }
//
//    private void setupDialog() {
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;
//        getWindow().setLayout(width, height);
//        getWindow().setGravity(Gravity.CENTER);
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//    }
//
//    private String getMobileNetworkByPhone(String phoneNumber) {
//        return ContactManager.getDefault().isPhoneNumberForeign(phoneNumber) ? getString(R.string.foreign_network) : PhoneNumberManager.getInstance(this).getNetworkMobile(phoneNumber) == null ? getText(R.string.unknown).toString() : (getText(R.string.mobile).toString() + " - " + PhoneNumberManager.getInstance(this).getNetworkMobile(phoneNumber));
//    }
//
//    private String getNationalByPhone(String phoneNumber) {
//        return PhoneNumberManager.getInstance(this).getDisplayNameCountry(AfterCallActivity.this, phoneNumber) == null ? getText(R.string.unknown).toString() :
//                PhoneNumberManager.getInstance(this).getDisplayNameCountry(AfterCallActivity.this, phoneNumber);
//    }
//
//
//    private String getDisplayNameByPhone(String phoneNumber) {
//        IContactObject contactDevice = ContactManager.getDefault().getDeviceContact(phoneNumber);
//        if (contactDevice != null) {
//            return (contactDevice.getName() == null || Utils.isEmpty(contactDevice.getName())) ? Utils.Number(phoneNumber) : contactDevice.getName();
//        } else {
//            ContactManager.getDefault().zipObservableTwo(phoneNumber, new GetDisplayNameCallBacks() {
//                @Override
//                public String getUserFromServer(DataBean dataBean) {
//                    return dataBean.getName();
//                }
//
//                @Override
//                public String getUserFromDevice(BlockContact blockContact) {
//                    return blockContact.getName();
//                }
//            });
//            if (PhoneNumberManager.getInstance(AfterCallActivity.this).switchboard.containsKey(Utils.removepecialCharacters(phoneNumber))) {
//                return PhoneNumberManager.getInstance(AfterCallActivity.this).switchboard.get(Utils.removepecialCharacters(phoneNumber));
//            } else {
//                return Utils.numberPhoneFormat(phoneNumber);
//            }
//        }
//    }
//
//
//    private void displayDialog(String phoneNumber, int callState, boolean isIncoming) {
//        if (Utils.isEmpty(phoneNumber)) return;
//        //contactBlock = ContactManager.getDefault().getBlockedContactWithNumber(phoneNumber);
//        displayName = getDisplayNameByPhone(phoneNumber);
//        national = getNationalByPhone(phoneNumber);
//        mobileNetwork = getMobileNetworkByPhone(phoneNumber);
//
//        callLog = CallLogManager.getDefault(AfterCallActivity.this).getCallLogFirst();
//        if (callLog != null) {
//            duration = callLog.getDuration();
//            IContactObject contactDevice = ContactManager.getDefault().getDeviceContact(phoneNumber);
//            IContactReportObject contactReport = ContactManager.getDefault().getContactReportByPhone(phoneNumber);
//            binding.lineSave.setVisibility(contactDevice == null ? View.VISIBLE : View.GONE);
//            showDialogGravityCenter();
//
//            boolean isAnswered = !duration.equals("0:00") && !duration.equals("00:00") && !duration.equals("00") && !duration.equals("0");
//
//            int type;
//            if (contactDevice != null) {
//                type = Constants.ReportType.NORMAL;
//            } else {
//                if (contactBlock != null) {
//                    type = contactBlock.getWarnType();
//                } else {
//                    if (contactReport != null) {
//                        type = contactReport.getWarn_type();
//                    } else {
//                        type = Constants.ReportType.NORMAL;
//                    }
//                }
//            }
//
//            if (callState == TelephonyManager.CALL_STATE_RINGING) {
//                // Trạng thái gọi nhỡ.
//                // Không nhấc máy khi thực hiện cuộc gọi, khi có người gọi đến
//                showDisplayAfterCall1(type, R.string.call_miss_min_ago);
//            } else if ((duration.equals("0:00") || duration.equals("00:00") || duration.equals("00") || duration.equals("0")) && (callState != TelephonyManager.CALL_STATE_OFFHOOK || !isIncoming)) {
//                showDisplayAfterCall1(type, R.string.call_away_min_ago);
//            } else if (isIncoming) {
//                // Trạng thái gọi có cuộc gọi đến
//                if (contactDevice != null) {
//                    // SĐT có trong danh bạ
//                    showDisplayAfterCall1(type, R.string.call_received_min_ago);
//                } else {
//                    // Chưa từng có trong danh sách lịch sử cuộc gọi
//                    if (!CallLogManager.getDefault(AfterCallActivity.this).isContainsCallLog(phoneNumber)) {
//                        if (contactReport != null) {
//                            // Có trong DB i-Caller
//                            if (!Utils.checkWarnType(contactReport.getWarn_type())) {// sđt có trong list DB của iCaller.
//                                showDisplayAfterCall2(true, true, type);
//                            } else { //sđt có trong Spam list của iCaller
//                                showDisplayAfterCall2(true, false, type);
//                            }
//                        } else {
//                            // Không có trong DB i-Caller
//                            showDisplayAfterCall2(true, false, type);
//                        }
//                    } else {
//                        showDisplayAfterCall1(type, R.string.call_received_min_ago);
//                    }
//                }
//            } else {
//                // Trạng thái gọi đi
//
//                Logger.log(isAnswered);
//
//                if (contactDevice != null) {
//                    // SĐT có trong danh bạ
//                    showDisplayAfterCall1(type, R.string.call_away_min_ago);
//                } else {
//
//                    if (!CallLogManager.getDefault(AfterCallActivity.this).isContainsCallLog(phoneNumber)) {
//                        // Chưa từng có trong danh sách lịch sử cuộc gọi
//                        if (contactReport != null) {
//                            // Có trong DB i-Caller
//                            showDisplayAfterCall2(false, true, type);
//                        } else {
//                            // Không có trong DB i-Caller
//                            showDisplayAfterCall2(false, false, type);
//                        }
//                    } else {
//                        showDisplayAfterCall1(type, R.string.call_away_min_ago);
//                    }
//                }
//            }
//
//            String size = RemoteConfigsConstants.SIZE_BANNER;
//            try {
//                size = AppRemoteConfigs.getInstance().getConfig().getString(RemoteConfigsConstants.KEY_ADSIZE);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            View adContainer = findViewById(R.id.adMobView);
//            AdView mAdView = new AdView(this);
//            mAdView.setAdSize(Utils.getAdsize(size));
//            final String idBannerAfterCall = getString(R.string.adUnitId_scr_after_call);
//            mAdView.setAdUnitId(idBannerAfterCall);
//            ((RelativeLayout) adContainer).addView(mAdView);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            cardViewAdvertising.setVisibility(View.GONE);
//            if (Utils.showAdvertisement(this)) {
//                mAdView.setAdListener(new AdListener() {
//                    @Override
//                    public void onAdLoaded() {
//                        // Code to be executed when an ad finishes loading.
//                        binding.cardViewAdvertising.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(int errorCode) {
//                        // Code to be executed when an ad request fails.
//                        binding.cardViewAdvertising.setVisibility(View.GONE);
//
//                    }
//
//                    @Override
//                    public void onAdOpened() {
//                        // Code to be executed when an ad opens an overlay that
//                        // covers the screen.
//                        binding.cardViewAdvertising.setVisibility(View.VISIBLE);
//
//                    }
//
//                    @Override
//                    public void onAdLeftApplication() {
//                        // Code to be executed when the user has left the app.
//                        binding.cardViewAdvertising.setVisibility(View.GONE);
//
//                    }
//
//                    @Override
//                    public void onAdClosed() {
//                        // Code to be executed when when the user is about to return
//                        // to the app after tapping on an ad.
//                        binding.cardViewAdvertising.setVisibility(View.VISIBLE);
//
//                    }
//                });
//                mAdView.loadAd(adRequest);
//            }
//            binding.cvParrent.setOnTouchListener(onTouchListener);
//        }
//    }
//
//    private void showDisplayAfterCall1(int warnType, int resId) {
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseConstants.PARAM_PHONENUMBER, phoneNumber);
//        LogEvents.getDefault(AfterCallActivity.this).log(FirebaseConstants.EVENT_DISPLAY_AFTER_CALL_1, bundle);
//
//        binding.textDisplayName.setTextColor(getColor(android.R.color.white));
//        binding.textBlockPhoneNumber.setTextColor(getColor(android.R.color.white));
//        binding.textNational.setTextColor(getColor(android.R.color.darker_gray));
//        binding.textMobileNetwork.setTextColor(getColor(android.R.color.darker_gray));
//        if (Utils.checkWarnType(warnType)) {
//            binding.lineAfterCall1.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_end_call_spam));
//            binding.lineButtonAction.setBackgroundColor(Color.parseColor("#68C33E3E"));
//            binding.cardviewTypeSpamAfterCall1.setVisibility(View.VISIBLE);
//            binding.textTypeSpamAfterCall1.setText(Utils.getWarnType(this, warnType));
//            binding.imgEditAfterCall1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit_red));
//            binding.textViewAfterCall1.setTextColor(getColor(R.color.color_red_dark));
//        } else {
//            binding.lineAfterCall1.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_end_call_not_spam));
//            binding.lineButtonAction.setBackgroundColor(Color.parseColor("#687B2F9E"));
//            binding.cardviewTypeSpamAfterCall1.setVisibility(View.INVISIBLE);
//            binding.imgEditAfterCall1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit_purple));
//            binding.textViewAfterCall1.setTextColor(getColor(R.color.color_purple_dark));
//        }
//        binding.lineAfterCall1.setVisibility(View.VISIBLE);
//        binding.lineAfterCall2.setVisibility(View.GONE);
//        binding.textBlockPhoneNumber.setText(Utils.numberPhoneFormat(phoneNumber));
//        binding.textDisplayName.setText(displayName.length() > 15 ? displayName.substring(0, 15) + "..." : displayName);
//
//        binding.textNational.setText(national);
//        binding.textMobileNetwork.setText(mobileNetwork);
//
//        binding.textTime.setText(getString(resId, Utils.convertMinsToHours(Utils.getTimeCallBefore(callLog.getDate()), getApplicationContext())));
//        if (contactBlock != null) {
//            binding.imgBlock.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_unblock_white));
//            binding.textBlock.setText(R.string.key_unblock);
//        } else {
//            binding.imgBlock.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_block_white));
//            binding.textBlock.setText(R.string.BLOCK);
//        }
//
//        // Notice !!!
//        AppNotificationManager.getDefault(AfterCallActivity.this).showNotificationStandard(PhoneNumberManager.getInstance(AfterCallActivity.this).getInfoCallLog(callLog));
//
//    }
//
//    private void showDisplayAfterCall2(boolean isIncoming, boolean isContainsDBiCaller, int warnType) {
//        binding.textBlockNameAfterCall2.setTextColor(getColor(android.R.color.white));
//        binding.textBlockPhoneNumberAfterCall2.setTextColor(getColor(android.R.color.white));
//        binding.textNationalAfterCall2.setTextColor(getColor(android.R.color.darker_gray));
//        binding.textMobileNetworkAfterCall2.setTextColor(getColor(android.R.color.darker_gray));
//        String mCursorDrawableRes = "mCursorDrawableRes";
//        if (Utils.checkWarnType(warnType)) {
//            binding.relaAfterCall2.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_end_call_spam));
//            binding.cardviewTypeSpamAfterCall2.setVisibility(View.VISIBLE);
//            binding.textTypeSpamAfterCall2.setText(Utils.getWarnType(this, warnType));
//            binding.btnCorrect.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_button_full_red));
//            binding.btnIncorrect.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_button_line_red));
//            binding.btnIncorrect.setTextColor(getColor(R.color.color_red_dark));
//
//            binding.btnSpam.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_button_full_red));
//            binding.btnNotSpam.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_button_line_red));
//            binding.btnNotSpam.setTextColor(getColor(R.color.color_red_dark));
//
//            binding.btnWhoThisIs.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_button_full_red));
//
//            binding.btnReport.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_button_full_red));
//            binding.edtWhoIsThis.setBackgroundTintList(getColorStateList(R.color.color_red_dark));
//            binding.edtWhoIsThis.setTextColor(getColor(R.color.color_red_dark));
//            try {
//                Field field = TextView.class.getDeclaredField(mCursorDrawableRes);
//                field.setAccessible(true);
//                field.set(binding.edtWhoIsThis, R.drawable.cursor_red);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        } else {
//            binding.relaAfterCall2.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_end_call_not_spam));
//            binding.cardviewTypeSpamAfterCall2.setVisibility(View.GONE);
//            binding.btnCorrect.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_rectangle_full_15sdp));
//            binding.btnIncorrect.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_button_line_purple));
//            binding.btnIncorrect.setTextColor(getColor(R.color.color_purple_dark));
//
//            binding.btnSpam.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_rectangle_full_15sdp));
//            binding.btnNotSpam.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_button_line_purple));
//            binding.btnNotSpam.setTextColor(getColor(R.color.color_purple_dark));
//
//            binding.btnWhoThisIs.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_rectangle_full_15sdp));
//
//            binding.btnReport.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_rectangle_full_15sdp));
//            binding.edtWhoIsThis.setBackgroundTintList(getColorStateList(R.color.color_purple_dark));
//            binding.edtWhoIsThis.setTextColor(getColor(R.color.color_purple_dark));
//            try {
//                Field field = TextView.class.getDeclaredField(mCursorDrawableRes);
//                field.setAccessible(true);
//                field.set(binding.edtWhoIsThis, R.drawable.cursor_purple);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//
//
//        binding.lineAfterCall1.setVisibility(View.GONE);
//        binding.lineAfterCall2.setVisibility(View.VISIBLE);
//        binding.textBlockNameAfterCall2.setText(displayName);
//        binding.textBlockPhoneNumberAfterCall2.setText(Utils.numberPhoneFormat(phoneNumber));
//        binding.textNationalAfterCall2.setText(national);
//        binding.textMobileNetworkAfterCall2.setText(mobileNetwork);
//
//        binding.lineContentAfterCall2.setVisibility(View.GONE);
//        binding.lineContentAfterCall3.setVisibility(View.GONE);
//        binding.lineContentAfterCall4.setVisibility(View.GONE);
//        binding.lineContentAfterCall5.setVisibility(View.GONE);
//        if (!isIncoming) {
//            if (isContainsDBiCaller) {
//                showDisplayAfterCall2(callLog);
//            } else {
//                Bundle bundle = new Bundle();
//                if (phoneNumber != null && !Utils.isEmpty(phoneNumber))
//                    bundle.putString(FirebaseConstants.PARAM_PHONENUMBER, phoneNumber);
//                LogEvents.getDefault(AfterCallActivity.this).log(FirebaseConstants.EVENT_DISPLAY_AFTER_CALL_4, bundle);
//                binding.lineContentAfterCall4.setVisibility(View.VISIBLE);
//                int timeAnswer = CallLogManager.getDefault(AfterCallActivity.this).getCountAnswer(phoneNumber);
//                binding.textCountTimeAnswer.setText(getString(R.string.you_answered_this_number, timeAnswer));
//                binding.textTimeCall.setText(duration);
//            }
//        } else {
//            if (isContainsDBiCaller) {
//                showDisplayAfterCall2(callLog);
//            } else {
//                Bundle bundle = new Bundle();
//                if (phoneNumber != null && !Utils.isEmpty(phoneNumber))
//                    bundle.putString(FirebaseConstants.PARAM_PHONENUMBER, phoneNumber);
//                LogEvents.getDefault(AfterCallActivity.this).log(FirebaseConstants.EVENT_DISPLAY_AFTER_CALL_3, bundle);
//                binding.lineContentAfterCall3.setVisibility(View.VISIBLE);
//                binding.textInfoCallAfter3.setText(getString(R.string.call_received_min_ago, Utils.convertMinsToHours(Utils.getTimeCallBefore(callLog.getDate()), getApplicationContext())));
//            }
//        }
//    }
//
//    private void showDisplayAfterCall2(CallLog callLog) {
//        Bundle bundle = new Bundle();
//        if (phoneNumber != null && !Utils.isEmpty(phoneNumber))
//            bundle.putString(FirebaseConstants.PARAM_PHONENUMBER, phoneNumber);
//        LogEvents.getDefault(EndCallActivity.this).log(FirebaseConstants.EVENT_DISPLAY_AFTER_CALL_2, bundle);
//
//        binding.lineContentAfterCall2.setVisibility(View.VISIBLE);
//        binding.textInfoCallAfter2.setText(getString(R.string.call_received_min_ago, Utils.convertMinsToHours(Utils.getTimeCallBefore(callLog.getDate()), getApplicationContext())));
//    }
//
//    private void showDialogGravityCenter() {
////        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) Utils.convertDpToPixel(320, CustomDialogEndCall.this), LinearLayout.LayoutParams.WRAP_CONTENT);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.gravity = Gravity.CENTER;
//        binding.cvParrent.setLayoutParams(lp);
//    }
//
//    private void showDialogGravityTop() {
////        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) Utils.convertDpToPixel(320, CustomDialogEndCall.this), LinearLayout.LayoutParams.WRAP_CONTENT);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.gravity = Gravity.CENTER_HORIZONTAL;
//        lp.topMargin = (int) Utils.convertDpToPixel(15, AfterCallActivity.this);
//        binding.cvParrent.setLayoutParams(lp);
//    }
//
//    private void showDialogQuestionWhoisthis() {
//        binding.lineContentAfterCall5.setVisibility(View.VISIBLE);
//        showDialogGravityTop();
//    }
//
//}