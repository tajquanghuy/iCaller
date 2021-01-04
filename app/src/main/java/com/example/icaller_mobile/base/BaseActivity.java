package com.example.icaller_mobile.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.utils.KeyboardUtils;
import com.example.icaller_mobile.common.widget.LoadingDialogManager;
import com.example.icaller_mobile.features.main.MainActivity;


public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity {

    public T binding;
    public V mViewModel;
    protected boolean canGotoActivity = false;

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDataBinding();
        getViewModel().getIsLoading().observe(this, isVisible -> {
            if ((Boolean) isVisible)
                LoadingDialogManager.getDefault(this).showDialog();
            else
                LoadingDialogManager.getDefault(this).hideDialog();
        });
    }

    public T getViewDataBinding() {
        return binding;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void performDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        binding.setVariable(getBindingVariable(), mViewModel);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        canGotoActivity = true;
    }

    public void push(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, android.R.anim.fade_out, android.R.anim.fade_out, android.R.anim.slide_out_right)
                .add(R.id.fragment_container, fragment, tag)
                .addToBackStack(tag)
                .setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
        getTitleToolbar(((BaseFragment<?, ?>) fragment).getTitle());
        getToolbarStyle(((BaseFragment<?, ?>) fragment).getToolbarStyle());
    }


    public void pop() {
        KeyboardUtils.hideKeyboard(this);
        int entryIndex = getSupportFragmentManager().getBackStackEntryCount() - 1;
        if (entryIndex <= 0) {
            moveTaskToBack(true);
            return;
        }
        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(entryIndex);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(entry.getName());
        if (fragment == null) {
            return;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_out, android.R.anim.slide_out_right, android.R.anim.fade_out, android.R.anim.slide_out_right)
                .remove(fragment)
                .commit();
        getSupportFragmentManager().popBackStack(entry.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().executePendingTransactions();
        if (this instanceof MainActivity) {
            entryIndex = getSupportFragmentManager().getBackStackEntryCount() - 1;
            entry = getSupportFragmentManager().getBackStackEntryAt(entryIndex);
            fragment = getSupportFragmentManager().findFragmentByTag(entry.getName());

            if (fragment != null) {
                getTitleToolbar(((BaseFragment<?, ?>) fragment).getTitle());
                getToolbarStyle(((BaseFragment<?, ?>) fragment).getToolbarStyle());
            }
        }
    }

    public void getTitleToolbar(String title) {

    }

    public void getToolbarStyle(Constants.ToolbarStyle style) {

    }

    @Override
    public void onBackPressed() {
        pop();
    }
}
