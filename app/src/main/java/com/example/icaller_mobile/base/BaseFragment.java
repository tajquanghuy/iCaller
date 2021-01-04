package com.example.icaller_mobile.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.icaller_mobile.common.Navigator;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.utils.Utils;


public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment {

    public BaseActivity mActivity;
    public View mRootView;
    public T binding;
    public V mViewModel;
    public boolean mTransitionAnimation = true;


    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    public abstract String getTitle();

    public abstract Constants.ToolbarStyle getToolbarStyle();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity<?, ?>) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = getViewModel();
        setHasOptionsMenu(false);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mRootView = binding.getRoot();
        return mRootView;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(MainViewModel.class);
        binding.setVariable(getBindingVariable(), mViewModel);
        binding.setLifecycleOwner(this);
//        getViewModel().getIsLoading().observe(this, isVisible -> {
//            if ((Boolean) isVisible)
//                LoadingDialogManager.getDefault(requireContext()).showDialog();
//            else
//                LoadingDialogManager.getDefault(requireContext()).hideDialog();
//        });
    }

    public boolean pop() {
        if (getActivity() != null) Utils.hideKeyboard(getActivity());
        return pop(true);
    }

    public boolean pop(boolean animated) {
        if (getFragmentManager() != null) {
            int entryIndex = getFragmentManager().getBackStackEntryCount() - 1;

            if (entryIndex < 0) {
                return false;
            }

            FragmentManager.BackStackEntry entry = getFragmentManager().getBackStackEntryAt(entryIndex);
            Fragment fragment = getFragmentManager().findFragmentByTag(entry.getName());

            if (fragment == null) {
                return false;
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            boolean isAnimDisabled = fragment instanceof Navigator && ((Navigator) fragment).isNavigationAnimationDisabled();
            if (!isAnimDisabled && animated) {
                ft.setCustomAnimations(android.R.anim.fade_out, android.R.anim.slide_out_right, android.R.anim.fade_out, android.R.anim.slide_out_right);
            }
            ft.remove(fragment).commit();
            getFragmentManager().popBackStack();
            return true;
        }
        return false;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!mTransitionAnimation) {
            Animation a = new Animation() {
            };
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    public BaseActivity<?, ?> getBaseActivity() {
        return mActivity;
    }

    public T getViewDataBinding() {
        return binding;
    }


}
