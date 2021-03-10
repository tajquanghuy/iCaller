package com.example.icaller_mobile.features.authenticate;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseActivity;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.constants.FirebaseConstants;
import com.example.icaller_mobile.common.manager.DialogManager;
import com.example.icaller_mobile.common.manager.SharedPreferencesManager;
import com.example.icaller_mobile.common.utils.LogEvents;
import com.example.icaller_mobile.common.utils.Logger;
import com.example.icaller_mobile.common.utils.NetworkUtils;
import com.example.icaller_mobile.common.utils.PhoneNumberManager;
import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.common.widget.LoadingDialogManager;
import com.example.icaller_mobile.common.widget.dialog.MessageDialog;
import com.example.icaller_mobile.databinding.ActivityLoginBinding;
import com.example.icaller_mobile.features.main.MainActivity;
import com.example.icaller_mobile.interfaces.LoginCallBack;
import com.example.icaller_mobile.model.network.ServiceHelper;
import com.example.icaller_mobile.model.network.response.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mukesh.OnOtpCompletionListener;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements OnOtpCompletionListener {
    private String codeNational;
    private String countryISO;
    private LoginActivity mLoginActivity;
    private boolean booleanCodeSent;
    private String phoneNumber;
    FirebaseAuth auth;
    private String verificationCode;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private CountDownTimer countDownTimer;
    private boolean check;
    private String gToken;
    private String notiToken;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        return new ViewModelProvider(this, new ViewModelProviderFactory(this)).get(LoginViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginActivity = LoginActivity.this;
        initializeNation();
        onSignIn();
    }


    private void initializeNation() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            try {
                binding.countryCodePicker.setDefaultCountryUsingNameCode(telephonyManager.getNetworkCountryIso());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        codeNational = Utils.isEmpty(binding.countryCodePicker.getDefaultCountryCode()) ? Constants.COUNTRY_CODE_VN : binding.countryCodePicker.getDefaultCountryCode();
        countryISO = Utils.isEmpty(binding.countryCodePicker.getDefaultCountryNameCode()) ? Constants.ISO_VIETNAM : binding.countryCodePicker.getDefaultCountryNameCode();
        SharedPreferencesManager.getDefault(mLoginActivity).saveCurrentCountry(countryISO);
        SharedPreferencesManager.getDefault(mLoginActivity).saveCountryCodeWithPlus("+" + codeNational);

        binding.countryCodePicker.setOnCountryChangeListener(country -> {
            if (country != null) {
                codeNational = country.getPhoneCode();
                countryISO = country.getIso();
                SharedPreferencesManager.getDefault(mLoginActivity).saveCurrentCountry(countryISO);
                SharedPreferencesManager.getDefault(mLoginActivity).saveCountryCodeWithPlus("+" + codeNational);
            }
        });
    }


    private void onSignIn() {
        StartFirebaseLogin();
        binding.otpView.setOtpCompletionListener(this);
        binding.imgCancelPhoneNumber.setOnClickListener(v -> {
            binding.edtPhoneNumber.setText("");
        });
        binding.btnSignIn.setOnClickListener(v -> {
            if (check) {
                String otp = binding.otpView.getText().toString();
                LoadingDialogManager.getDefault(mLoginActivity).hideDialog();
                if (Utils.isEmpty(otp)) {
                    MessageDialog messageDialog = new MessageDialog(
                            mLoginActivity,
                            mLoginActivity.getString(R.string.otp_is_empty),
                            mLoginActivity.getString(R.string.content_empty_otp),
                            null);
                    messageDialog.show();
                } else {
                    if (!Utils.isEmpty(binding.otpView.getText())) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                        SigninWithPhone(credential);
                        LoadingDialogManager.getDefault(mLoginActivity).showDialog();
                    }
                }
            } else {
                phoneNumber = (binding.edtPhoneNumber.getText() == null || binding.edtPhoneNumber.getText().toString().trim().isEmpty()) ? "" : binding.edtPhoneNumber.getText().toString().trim();
                getCodeVerify(phoneNumber);
            }

        });
        onGetPhoneNumber();
        binding.textResendCode.setOnClickListener(v -> {
            phoneNumber = binding.edtPhoneNumber.getText() != null ? binding.edtPhoneNumber.getText().toString().trim() : "";
            if (phoneNumber.equals("")) {
                binding.edtPhoneNumber.setError(getString(R.string.error_phone_number));
                return;
            }
            if (NetworkUtils.isOn(this)) {
                mLoginActivity.getCodeVerify(phoneNumber);
            } else {
                DialogManager.showDialogNotInternet(this);
            }
            Utils.hideKeyboard(LoginActivity.this);
        });
    }

    private void onGetPhoneNumber() {
        phoneNumber = (binding.edtPhoneNumber.getText() == null || binding.edtPhoneNumber.getText().toString().trim().isEmpty()) ? "" : binding.edtPhoneNumber.getText().toString().trim();
        binding.imgCancelPhoneNumber.setVisibility(phoneNumber.equals("") ? View.GONE : View.VISIBLE);
        binding.edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            boolean backspacingFlag = false;
            boolean editedFlag = false;
            int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cursorComplement = s.length() - binding.edtPhoneNumber.getSelectionStart();
                backspacingFlag = count > after;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.imgCancelPhoneNumber.setVisibility(s == null || s.toString().equals("") ? View.GONE : View.VISIBLE);
                String string = s != null ? s.toString() : null;
                if (string != null && !string.contains("*") && !string.contains("#") && string.length() > 1) {
                    // format: xxx xxx xx xx
                    String phone = string.replaceAll("[^\\d+]", "");
                    if (string.charAt(0) != '+') {
                        if (!editedFlag) {
                            if (phone.length() >= 8 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3, 6) + " " + phone.substring(6, 8) + " " + phone.substring(8);
                                binding.edtPhoneNumber.setText(ans);
                                binding.edtPhoneNumber.setSelection(binding.edtPhoneNumber.getText().length() - cursorComplement);
                            } else if (phone.length() >= 6 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3, 6) + " " + phone.substring(6);
                                binding.edtPhoneNumber.setText(ans);
                                binding.edtPhoneNumber.setSelection(binding.edtPhoneNumber.getText().length() - cursorComplement);
                            } else if (phone.length() >= 3 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3);
                                binding.edtPhoneNumber.setText(ans);
                                binding.edtPhoneNumber.setSelection(binding.edtPhoneNumber.getText().length() - cursorComplement);
                            }
                        } else {
                            editedFlag = false;
                        }
                    } else {
                        if (!editedFlag) {
                            if (phone.length() >= 12 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3, 5) + " " + phone.substring(5, 8) + " " + phone.substring(8, 10) + " " + phone.substring(10);
                                binding.edtPhoneNumber.setText(ans);
                                binding.edtPhoneNumber.setSelection(binding.edtPhoneNumber.getText().length() - cursorComplement);
                            } else if (phone.length() >= 10 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3, 5) + " " + phone.substring(5, 8) + " " + phone.substring(8);
                                binding.edtPhoneNumber.setText(ans);
                                binding.edtPhoneNumber.setSelection(binding.edtPhoneNumber.getText().length() - cursorComplement);
                            } else if (phone.length() >= 8 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3, 5) + " " + phone.substring(5);
                                binding.edtPhoneNumber.setText(ans);
                                binding.edtPhoneNumber.setSelection(binding.edtPhoneNumber.getText().length() - cursorComplement);
                            } else if (phone.length() >= 5 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3);
                                binding.edtPhoneNumber.setText(ans);
                                binding.edtPhoneNumber.setSelection(binding.edtPhoneNumber.getText().length() - cursorComplement);
                            }
                        } else {
                            editedFlag = false;
                        }
                    }
                }
            }
        });
        binding.edtPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }


    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                try {
                    Logger.log("code: " + phoneAuthCredential.getSmsCode());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Logger.log("Đạt 09 " + e);
                e.printStackTrace();
                showDialogSigninFail();
                binding.btnSignIn.setClickable(true);
                if (Utils.isEmpty(binding.otpView.getText())) {
                    LoadingDialogManager.getDefault(mLoginActivity).hideDialog();
                    MessageDialog messageDialog = new MessageDialog(
                            mLoginActivity, "Fail", "Failure", "ANC"
                    );
                    messageDialog.show();
                }
            }

            @Override
            public void onCodeSent(String vertificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(vertificationId, forceResendingToken);
                check = true;
                Toast.makeText(LoginActivity.this, "Code sent", Toast.LENGTH_SHORT).show();
                Logger.log(verificationCode);
                booleanCodeSent = true;
                binding.otpView.setVisibility(View.VISIBLE);
                binding.textResendCode.setVisibility(View.VISIBLE);
                if (countDownTimer != null) countDownTimer.start();
                verificationCode = vertificationId;
                LoadingDialogManager.getDefault(mLoginActivity).hideDialog();
                binding.btnSignIn.setClickable(true);
                binding.otpView.requestFocus();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@androidx.annotation.NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Toast.makeText(LoginActivity.this, "time out", Toast.LENGTH_SHORT).show();
                LoadingDialogManager.getDefault(mLoginActivity).hideDialog();
                if (binding.btnSignIn != null) {
                    binding.btnSignIn.setClickable(true);
                }

            }
        };
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.textResendCode.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.textResendCode.setText(Html.fromHtml(getString(R.string.valid_code, millisUntilFinished / 1000), Html.FROM_HTML_MODE_COMPACT));
                    Logger.log(Html.fromHtml(getString(R.string.valid_code, millisUntilFinished / 1000), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.textResendCode.setText(Html.fromHtml(getString(R.string.valid_code, millisUntilFinished / 1000)));
                }
            }

            @Override
            public void onFinish() {
                binding.textResendCode.setEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.textResendCode.setText(Html.fromHtml(getString(R.string.resend_verification_code), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.textResendCode.setText(Html.fromHtml(getString(R.string.resend_verification_code)));
                }
            }
        };

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Logger.log(task.getException());
                return;
            }
            if (task.getResult() != null) {
                notiToken = task.getResult().getToken();
                Logger.log("token: " + notiToken);
            }

        });

    }

    private void getCodeVerify(String phoneNumber) {
        if (phoneNumber == null || Utils.isEmpty(phoneNumber)) {
            binding.viewDismiss.setVisibility(View.VISIBLE);
            binding.txtDismiss.setOnClickListener(v1 -> {
                binding.viewDismiss.setVisibility(View.GONE);
            });
        }

        String sPhoneNumber = PhoneNumberManager.getInstance(mLoginActivity).convertNationalNumber(phoneNumber);
        Logger.log(sPhoneNumber);
        if (sPhoneNumber != null) {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(sPhoneNumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

            LoadingDialogManager.getDefault(mLoginActivity).showDialog();
            binding.btnSignIn.setClickable(false);
        }

//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            return;
//                        }
//                        // Get new FCM registration token
//                        notiToken = task.getResult();
//                        Logger.log("token_messaging: " + notiToken);
//                        // Log and toast
//                    }
//                });
//
//        FirebaseInstallations.getInstance().getId().addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Logger.log(task.getException());
//                return;
//            }
//            if (task.getResult() != null) {
//                notiToken = task.getResult();
//                Logger.log("token new: " + notiToken);
//            }
//
//        });
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                                @Override
                                public void onSuccess(GetTokenResult getTokenResult) {
                                    gToken = getTokenResult.getToken();
                                    Logger.log(gToken);
                                    if (!Utils.isEmpty(phoneNumber) && !Utils.isEmpty(gToken)) {
                                        ServiceHelper.getDefault(mLoginActivity).loginServer(
                                                PhoneNumberManager.getInstance(mLoginActivity).convertNationalNumber(phoneNumber),
                                                gToken,
                                                notiToken,
                                                new LoginCallBack() {
                                                    @Override
                                                    public void loginSuccess(User user) {
                                                        Logger.log("CHECK");
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString(FirebaseConstants.PARAM_PHONENUMBER, phoneNumber);
                                                        LogEvents.getDefault(LoginActivity.this).log(FirebaseConstants.EVENT_LOGIN_SUCCESSED, bundle);
                                                        LoadingDialogManager.getDefault(mLoginActivity).hideDialog();
                                                        hideOtpView();

                                                        SharedPreferencesManager.getDefault(mLoginActivity).saveApiToken(user.getProfile().getAuth_token());
                                                        Constants.API_TOKEN = user.getProfile().getAuth_token();
                                                    }

                                                    @Override
                                                    public void loginFail(String message) {
                                                        if (!mLoginActivity.isFinishing()) {
                                                            showDialogSigninFail();
                                                        }
                                                    }
                                                });
                                        LoadingDialogManager.getDefault(mLoginActivity).showDialog();
                                    }
                                }
                            });
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            LoadingDialogManager.getDefault(mLoginActivity).hideDialog();
                            MessageDialog messageDialog = new MessageDialog(
                                    mLoginActivity, "Error", "Incorrect OTP", null
                            );
                            messageDialog.show();
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (mLoginActivity == null) return;
                                    ((Activity) mLoginActivity).runOnUiThread(() -> {
                                        if (messageDialog.isShowing()) {
                                            messageDialog.dismiss();
                                        }
                                    });
                                }
                            }, Constants.TIME_AUTO_CLOSE_DIALOG);
                        }
                    }
                });
    }


    private void showDialogSigninFail() {
        binding.btnSignIn.setOnClickListener(v -> {
            if (Utils.isEmpty(binding.otpView.getText())) {
                LoadingDialogManager.getDefault(mLoginActivity).hideDialog();
                MessageDialog messageDialog = new MessageDialog(
                        mLoginActivity,
                        mLoginActivity.getString(R.string.otp_is_empty),
                        mLoginActivity.getString(R.string.content_empty_otp),
                        null
                );
                messageDialog.show();
            }
        });
        clearLineAuth();
        LoadingDialogManager.getDefault(mLoginActivity).hideDialog();
        DialogManager.showDialogConfirm(this, ContextCompat.getDrawable(this, R.drawable.ic_error_white), getString(R.string.sign_in_fail), getString(android.R.string.ok), null);
    }

    private void clearLineAuth() {
        if (binding.otpView.getVisibility() == View.VISIBLE) {
            binding.otpView.setText("");
        }
        hideOtpView();
    }

    private void hideOtpView() {
        binding.otpView.setVisibility(View.GONE);
        binding.textResendCode.setVisibility(View.GONE);
        if (countDownTimer != null) countDownTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }

    @Override
    public void onOtpCompleted(String otp) {

    }
}
