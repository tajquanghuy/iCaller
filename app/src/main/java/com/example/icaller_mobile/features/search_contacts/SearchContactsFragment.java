package com.example.icaller_mobile.features.search_contacts;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.constants.IntentConstants;
import com.example.icaller_mobile.common.utils.ContactManager;
import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.databinding.FragmentSearchContactsBinding;
import com.example.icaller_mobile.features.main.MainViewModel;
import com.example.icaller_mobile.model.base.IContactObject;

public class SearchContactsFragment extends BaseFragment<FragmentSearchContactsBinding, SearchContactsViewModel> {
    private MainViewModel mainViewModel;
    private SearchContactsViewModel mViewModel;
    private Context mContext;

    public static SearchContactsFragment newInstance() {
        SearchContactsFragment fragment = new SearchContactsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
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
        //String phoneNumber = getArguments().getString(IntentConstants.KEY_PHONE_NUMBER_RECOGNITION);
        String phoneNumber = "0364849979";
        if (!phoneNumber.isEmpty()){
            getPhoneNumberFromOCR(phoneNumber);
        }

        //ArrayList<String> phones = getArguments().getStringArrayList(IntentConstants.KEY_PHONE_NUMBER_RECOGNITION);

    }


    private void getPhoneNumberFromOCR(String phoneNumber) {
        String displayName;
        IContactObject contactDevice = ContactManager.getDefault().getDeviceContact(phoneNumber);
        if (contactDevice != null) {
            displayName = (contactDevice.getName() == null || Utils.isEmpty(contactDevice.getName())) ? Utils.Number(phoneNumber) : contactDevice.getName();
            binding.txtName.setText(displayName);
        }
    }


}