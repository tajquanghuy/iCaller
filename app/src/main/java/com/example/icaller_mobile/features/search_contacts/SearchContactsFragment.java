package com.example.icaller_mobile.features.search_contacts;

import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.databinding.FragmentSearchContactsBinding;

public class SearchContactsFragment extends BaseFragment<FragmentSearchContactsBinding,SearchContactsViewModel> {

    private SearchContactsViewModel mViewModel;

    public static SearchContactsFragment newInstance() {
        return new SearchContactsFragment();
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
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_contacts;
    }

    @Override
    public SearchContactsViewModel getViewModel() {
        return null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_contacts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchContactsViewModel.class);
        // TODO: Use the ViewModel
    }

}