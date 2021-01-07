package com.example.icaller_mobile.features.after_call;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;

import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseActivity;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.utils.ContactManager;
import com.example.icaller_mobile.common.utils.PhoneNumberManager;
import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.databinding.ActivityAfterCallBinding;
import com.example.icaller_mobile.model.base.IContactObject;
import com.example.icaller_mobile.model.base.IContactReportObject;

public class AfterCallActivity extends BaseActivity<ActivityAfterCallBinding, AfterCallViewModel> {

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_after_call;
    }

    @Override
    public AfterCallViewModel getViewModel() {
        return new ViewModelProvider(this, new ViewModelProviderFactory(this)).get(AfterCallViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDialog();
    }

    private void setupDialog() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        getWindow().setLayout(width, height);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private String getDisplayNameByPhone(String phoneNumber) {
        IContactObject contactDevice = ContactManager.getDefault().getDeviceContact(phoneNumber);
        if (contactDevice != null) {
            return (contactDevice.getName() == null || Utils.isEmpty(contactDevice.getName())) ? Utils.numberPhoneFormat(phoneNumber) : contactDevice.getName();
        } else {
            IContactObject contactBlock = ContactManager.getDefault().getBlockedContactWithNumber(phoneNumber);
            if (contactBlock != null) {
                return (contactBlock.getName() == null || Utils.isEmpty(contactBlock.getName())) ? Utils.numberPhoneFormat(phoneNumber) : contactBlock.getName();
            } else {
                IContactReportObject contactReport = ContactManager.getDefault().getContactReportByPhone(phoneNumber);
                if (contactReport == null || contactReport.getName() == null || Utils.isEmpty(contactReport.getName())) {
                    if (PhoneNumberManager.getInstance(this).switchboard.containsKey(Utils.removepecialCharacters(phoneNumber))) {
                        return PhoneNumberManager.getInstance(this).switchboard.get(Utils.removepecialCharacters(phoneNumber));
                    } else {
                        return Utils.numberPhoneFormat(phoneNumber);
                    }
                } else {
                    return contactReport.getName();
                }
            }
        }
    }

    private String getNationalByPhone(String phoneNumber) {
        return PhoneNumberManager.getInstance(this).getDisplayNameCountry(AfterCallActivity.this, phoneNumber) == null ? getText(R.string.unknown).toString() :
                PhoneNumberManager.getInstance(this).getDisplayNameCountry(AfterCallActivity.this, phoneNumber);
    }

    private String getMobileNetworkByPhone(String phoneNumber) {
        return ContactManager.getDefault().isPhoneNumberForeign(phoneNumber) ? getString(R.string.foreign_network) : PhoneNumberManager.getInstance(this).getNetworkMobile(phoneNumber) == null ? getText(R.string.unknown).toString() : (getText(R.string.mobile).toString() + " - " + PhoneNumberManager.getInstance(this).getNetworkMobile(phoneNumber));
    }
}