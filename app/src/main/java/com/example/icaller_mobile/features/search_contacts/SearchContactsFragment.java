package com.example.icaller_mobile.features.search_contacts;

import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.constants.IntentConstants;
import com.example.icaller_mobile.common.utils.ContactManager;
import com.example.icaller_mobile.databinding.FragmentSearchContactsBinding;
import com.example.icaller_mobile.features.block_list.BlockListViewModel;
import com.example.icaller_mobile.features.main.MainViewModel;
import com.example.icaller_mobile.model.base.IContactObject;

import java.util.ArrayList;

public class SearchContactsFragment extends BaseFragment<FragmentSearchContactsBinding, SearchContactsViewModel> {
    private MainViewModel mainViewModel;
    private SearchContactsViewModel mViewModel;
    private Context mContext;

    public static SearchContactsFragment newInstance() {
        return new SearchContactsFragment();
    }


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
        return null;
    }

    @Override
    public Constants.ToolbarStyle getToolbarStyle() {
        return Constants.ToolbarStyle.SearchBar;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_contacts;
    }

    @Override
    public SearchContactsViewModel getViewModel() {
        mainViewModel = new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(MainViewModel.class);
        return new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(SearchContactsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> phones = getArguments().getStringArrayList(IntentConstants.KEY_PHONE_NUMBER_RECOGNITION);


    }


    private void getPhoneNumberFromOCR(String phoneNumber) {
        IContactObject contactDevice = ContactManager.getDefault().getDeviceContact(phoneNumber);
        if (contactDevice != null) {

        }
    }


}