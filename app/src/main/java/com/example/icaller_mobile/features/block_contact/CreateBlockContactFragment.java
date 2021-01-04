package com.example.icaller_mobile.features.block_contact;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.BR;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseBottomSheetDialog;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.utils.KeyboardUtils;
import com.example.icaller_mobile.common.widget.bottom_sheet.BottomSheetDescription;
import com.example.icaller_mobile.databinding.FragmentAddBlockContractBinding;
import com.example.icaller_mobile.features.main.MainViewModel;

public class CreateBlockContactFragment extends BaseFragment<FragmentAddBlockContractBinding, CreateBlockContactViewModel> implements BaseBottomSheetDialog.ItemBottomSheetClick<BottomSheetDescription.Description>, View.OnClickListener {
    private MainViewModel mainViewModel;
    private Context mContext;


    public static CreateBlockContactFragment newInstance() {
        return new CreateBlockContactFragment();
    }

    private CreateBlockContactViewModel createBlockContactViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public String getTitle() {
        return getString(R.string.key_block_number).toUpperCase();
    }

    @Override
    public Constants.ToolbarStyle getToolbarStyle() {
        return Constants.ToolbarStyle.Backable;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_block_contract;
    }

    @Override
    public CreateBlockContactViewModel getViewModel() {
        mainViewModel = new ViewModelProvider(requireActivity(), new ViewModelProviderFactory(requireContext())).get(MainViewModel.class);
        return new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(CreateBlockContactViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initOnclickListener();
        initViews();
        initObserve();
    }


    private void initViews() {
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            private boolean backspacingFlag = false;
            private boolean editedFlag = false;
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cursorComplement = s.length() - binding.edtPhone.getSelectionStart();
                backspacingFlag = count > after;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s != null ? s.toString() : null;
                if (string != null && !string.contains("*") && !string.contains("#") && string.length() > 1) {
                    // format: xxx xxx xx xx
                    String phone = string.replaceAll("[^\\d+]", "");
                    if (string.charAt(0) != '+') {
                        if (!editedFlag) {
                            if (phone.length() >= 8 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3, 6) + " " + phone.substring(6, 8) + " " + phone.substring(8);
                                binding.edtPhone.setText(ans);
                                binding.edtPhone.setSelection(binding.edtPhone.getText().length() - cursorComplement);
                            } else if (phone.length() >= 6 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3, 6) + " " + phone.substring(6);
                                binding.edtPhone.setText(ans);
                                binding.edtPhone.setSelection(binding.edtPhone.getText().length() - cursorComplement);
                            } else if (phone.length() >= 3 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3);
                                binding.edtPhone.setText(ans);
                                binding.edtPhone.setSelection(binding.edtPhone.getText().length() - cursorComplement);
                            }
                        } else {
                            editedFlag = false;
                        }
                    } else {
                        if (!editedFlag) {
                            if (phone.length() >= 12 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3, 5) + " " + phone.substring(5, 8) + " " + phone.substring(8, 10) + " " + phone.substring(10);
                                binding.edtPhone.setText(ans);
                                binding.edtPhone.setSelection(binding.edtPhone.getText().length() - cursorComplement);
                            } else if (phone.length() >= 10 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3, 5) + " " + phone.substring(5, 8) + " " + phone.substring(8);
                                binding.edtPhone.setText(ans);
                                binding.edtPhone.setSelection(binding.edtPhone.getText().length() - cursorComplement);
                            } else if (phone.length() >= 8 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3, 5) + " " + phone.substring(5);
                                binding.edtPhone.setText(ans);
                                binding.edtPhone.setSelection(binding.edtPhone.getText().length() - cursorComplement);
                            } else if (phone.length() >= 5 && !backspacingFlag) {
                                editedFlag = true;
                                String ans = phone.substring(0, 3) + " " + phone.substring(3);
                                binding.edtPhone.setText(ans);
                                binding.edtPhone.setSelection(binding.edtPhone.getText().length() - cursorComplement);
                            }
                        } else {
                            editedFlag = false;
                        }
                    }

                }
            }
        });
        binding.edtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private void initObserve() {
        mViewModel.blockSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                mActivity.pop();
                mainViewModel.updateData();
            }
        });

    }

    private void initOnclickListener() {
        binding.viewVVV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        KeyboardUtils.hideKeyboard(requireActivity());
        switch (v.getId()) {
            case R.id.viewVVV:
                BottomSheetDescription bottomSheetDescription = new BottomSheetDescription(mContext, mViewModel.typeDescription.getValue());
                bottomSheetDescription.setOnItemClickListener(this);
                bottomSheetDescription.show(getChildFragmentManager(), "");
                break;
        }

    }

    @Override
    public void onClick(BottomSheetDescription.Description data) {
        binding.txtTypeOfDescription.setText(getString(data.getName()));
        binding.imgDescription.setImageResource(data.getImageRed());
        mViewModel.setTypeOfDescription(data);
    }

    @Override
    public void onItemClick(BottomSheetDescription.Description data, int position) {

    }
}