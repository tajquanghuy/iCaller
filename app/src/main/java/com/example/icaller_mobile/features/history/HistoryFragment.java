package com.example.icaller_mobile.features.history;

import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.databinding.FragmentHistoryBinding;

public class HistoryFragment extends BaseFragment<FragmentHistoryBinding,HistoryViewModel> {

    private HistoryViewModel mViewModel;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_history).toUpperCase();
    }

    @Override
    public Constants.ToolbarStyle getToolbarStyle() {
        return Constants.ToolbarStyle.OnlyText;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    public HistoryViewModel getViewModel() {
        return new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(HistoryViewModel.class);
    }
}