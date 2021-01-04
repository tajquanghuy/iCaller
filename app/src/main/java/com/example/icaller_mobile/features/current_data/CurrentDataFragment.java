package com.example.icaller_mobile.features.current_data;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.databinding.FragmentCurrentDataBinding;

public class CurrentDataFragment extends BaseFragment<FragmentCurrentDataBinding, CurrentDataViewModel> {
    private CurrentDataViewModel mViewModel;
    public static CurrentDataFragment newInstance() {
        return new CurrentDataFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public Constants.ToolbarStyle getToolbarStyle() {
        return Constants.ToolbarStyle.Backable;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_current_data;
    }

    @Override
    public CurrentDataViewModel getViewModel() {
        return new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(CurrentDataViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews();
    }

    protected void initializeViews() {
//        String versionData = SharedPreferencesManager.getDefault().getDateUpdateData();
//        int id = SharedPreferencesManager.getDefault().getIDLastestCurrent();
//        String txtVersionData = getText(R.string.version).toString() + ": " + versionData;
        binding.textVersionData.setText("txtVersionData");
//        String txtID = getText(R.string.id_lastest).toString() + ": " + id;
        binding.textPageDownloading.setText("txtID");
        //String txtNumber = getText(R.string.number).toString() + ": " + ContactManager.getDefault().getAllPhoneReport();
        String txtNumber = "unknow";
        binding.textNumberData.setText(txtNumber);
    }
}