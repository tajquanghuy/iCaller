package com.example.icaller_mobile.features.dialpad;

import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.databinding.FragmentDialpadBinding;

public class DialpadFragment extends BaseFragment<FragmentDialpadBinding,DialpadViewModel> {

    private DialpadViewModel mViewModel;

    public static DialpadFragment newInstance() {
        return new DialpadFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.d("FRAGMENT","DESTROY_VIEW");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*Log.d("FRAGMENT","DESTROY");
        getFragmentManager().beginTransaction().remove(DialpadFragment.newInstance()).commit();*/
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_dialpad).toUpperCase();
    }

    @Override
    public Constants.ToolbarStyle getToolbarStyle() {
        return Constants.ToolbarStyle.None;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dialpad;
    }

    @Override
    public DialpadViewModel getViewModel() {
        return new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(DialpadViewModel.class);
    }
}