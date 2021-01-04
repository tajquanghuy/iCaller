package com.example.icaller_mobile.common.widget.bottom_sheet;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseBottomSheetDialog;
import com.example.icaller_mobile.databinding.BottomSheetDescriptionBinding;

public class BottomSheetDescription extends BaseBottomSheetDialog<BottomSheetDescriptionBinding, BottomSheetDescription.Description> {
    private Description description;

    public BottomSheetDescription(@NonNull Context context, Description description) {
        super(context);
        this.description = description;
    }

    @Override
    public int getLayoutId() {
        return R.layout.bottom_sheet_description;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public void bindingView(BottomSheetDescriptionBinding binding) {
        binding.txtAdvertising.setOnClickListener(v -> {
            dismissAllowingStateLoss();
            if (listener != null)
                listener.onClick(Description.REPORT_ADVERTISING);
        });

        binding.txtFinancialService.setOnClickListener(v -> {
            dismissAllowingStateLoss();
            if (listener != null)
                listener.onClick(Description.REPORT_FINANCIAL_SERVICE);
        });
        binding.txtLoan.setOnClickListener(v -> {
            dismissAllowingStateLoss();
            if (listener != null)
                listener.onClick(Description.REPORT_LOAN_COLLECTION);
        });
        binding.txtScam.setOnClickListener(v -> {
            dismissAllowingStateLoss();
            if (listener != null)
                listener.onClick(Description.REPORT_SCAM);
        });
        binding.txtRealEstate.setOnClickListener(v -> {
            dismissAllowingStateLoss();
            if (listener != null)
                listener.onClick(Description.REPORT_REAL_ESTATE);
        });
        binding.txtOther.setOnClickListener(v -> {
            dismissAllowingStateLoss();
            if (listener != null)
                listener.onClick(Description.REPORT_OTHER);
        });

        binding.imgClose.setOnClickListener(v -> dismissAllowingStateLoss());
        if (description != null) {
            switch (description) {
                case REPORT_ADVERTISING:
                    setSelected(binding.imgAdvertising);
                    break;
                case REPORT_FINANCIAL_SERVICE:
                    setSelected(binding.imgFinancialService);
                    break;
                case REPORT_LOAN_COLLECTION:
                    setSelected(binding.imgLoan);
                    break;
                case REPORT_SCAM:
                    setSelected(binding.imgScam);
                    break;
                case REPORT_REAL_ESTATE:
                    setSelected(binding.imgRealEstate);
                    break;
                case REPORT_OTHER:
                    setSelected(binding.imgOther);
                    break;

            }
        }

    }

    private void setSelected(View selected) {
        binding.imgAdvertising.setVisibility(View.GONE);
        binding.imgFinancialService.setVisibility(View.GONE);
        binding.imgLoan.setVisibility(View.GONE);
        binding.imgScam.setVisibility(View.GONE);
        binding.imgRealEstate.setVisibility(View.GONE);
        binding.imgOther.setVisibility(View.GONE);
        selected.setVisibility(View.VISIBLE);
    }

    @Override
    public Description getData() {
        return null;
    }

    public enum Description {
        NORMAL(1),
        REPORT_ADVERTISING(2, R.string.advertising, R.drawable.ic_red_advertising, R.drawable.ic_white_advertising),
        REPORT_FINANCIAL_SERVICE(3, R.string.financial_service, R.drawable.ic_red_financial_service, R.drawable.ic_white_financial_service),
        REPORT_LOAN_COLLECTION(4, R.string.loan_collection, R.drawable.ic_red_loan_collection, R.drawable.ic_white_loan_collection),
        REPORT_SCAM(5, R.string.scam, R.drawable.ic_red_scam, R.drawable.ic_white_scam),
        REPORT_REAL_ESTATE(6, R.string.real_estate, R.drawable.ic_red_real_estate, R.drawable.ic_white_real_estate),
        REPORT_OTHER(7, R.string.other, R.drawable.ic_red_other, R.drawable.ic_white_other);

        private int type;
        private int name;
        private int imageRed;
        private int imageWhite;

        Description() {

        }

        Description(int type) {
            this.type = type;
        }

        Description(int type, int name, int imageRed, int imageWhite) {
            this.type = type;
            this.name = name;
            this.imageRed = imageRed;
            this.imageWhite = imageWhite;
        }

        public int getType() {
            return type;
        }

        public int getName() {
            return name;
        }

        public int getImageRed() {
            return imageRed;
        }

        public int getImageWhite() {
            return imageWhite;
        }

    }
}
