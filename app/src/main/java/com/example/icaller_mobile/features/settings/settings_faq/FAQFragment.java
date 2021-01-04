package com.example.icaller_mobile.features.settings.settings_faq;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.BR;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.databinding.FragmentFaqBinding;


public class FAQFragment extends BaseFragment<FragmentFaqBinding, FAQViewModel> {
    private FAQAdapter faqAdapter;

    public static FAQFragment newInstance() {
        FAQFragment fragment = new FAQFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public String getTitle() {
        return getString(R.string.key_FAQ).toUpperCase();
    }

    @Override
    public Constants.ToolbarStyle getToolbarStyle() {
        return Constants.ToolbarStyle.Backable;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_faq;
    }

    @Override
    public FAQViewModel getViewModel() {
        return new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(FAQViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        faqAdapter = new FAQAdapter(requireContext());
        binding.rvFaq.setAdapter(faqAdapter);
        mViewModel.qaObjectLiveData.observe(getViewLifecycleOwner(), qaObjectList -> faqAdapter.updateData(qaObjectList));
    }
}