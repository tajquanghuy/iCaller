package com.example.icaller_mobile.features.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.BR;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseActivity;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.constants.FragmentTag;
import com.example.icaller_mobile.common.service.GetDBService;
import com.example.icaller_mobile.databinding.ActivityMainBinding;
import com.example.icaller_mobile.features.block_list.BlockListFragment;
import com.example.icaller_mobile.features.contacts.ContactsFragment;
import com.example.icaller_mobile.features.dialpad.DialpadFragment;
import com.example.icaller_mobile.features.history.HistoryFragment;
import com.example.icaller_mobile.features.settings.SettingsFragment;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements View.OnClickListener {
    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainViewModel getViewModel() {
        return new ViewModelProvider(this, new ViewModelProviderFactory(this)).get(MainViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.bottomNavigation.setItemSelected(R.id.blockList, true);
        push(BlockListFragment.newInstance(), FragmentTag.FRAGMENT_BLOCKED);
        onClickItemBottomNavigation();
        GetDBService.getDefault(getApplicationContext()).startServiceDB(getApplicationContext());

    }

    @Override
    public void getToolbarStyle(Constants.ToolbarStyle style) {
        super.getToolbarStyle(style);
        if (style == null) {
            binding.layoutToolbar.imgBack.setImageResource(R.drawable.ic_arrow_back_purple_24dp);
            binding.layoutToolbar.imgBack.setVisibility(View.VISIBLE);
            binding.layoutToolbar.imgSearch.setImageResource(R.drawable.ic_search_purple);
            binding.layoutToolbar.imgSearch.setVisibility(View.INVISIBLE);
            binding.layoutToolbar.txtTitleToolbar.setVisibility(View.VISIBLE);
            return;
        }

        switch (style) {
            case BonusSearch:
                binding.layoutToolbar.imgBack.setVisibility(View.GONE);
                binding.layoutToolbar.imgSearch.setImageResource(R.drawable.ic_search_purple);
                binding.layoutToolbar.imgSearch.setVisibility(View.VISIBLE);
                binding.layoutToolbar.txtTitleToolbar.setVisibility(View.VISIBLE);
                break;
            case OnlyText:
                binding.layoutToolbar.imgBack.setVisibility(View.GONE);
                binding.layoutToolbar.imgSearch.setVisibility(View.INVISIBLE);
                binding.layoutToolbar.txtTitleToolbar.setVisibility(View.VISIBLE);
                break;
            case None:
                binding.layoutToolbar.imgBack.setVisibility(View.INVISIBLE);
                binding.layoutToolbar.imgSearch.setVisibility(View.INVISIBLE);
                binding.layoutToolbar.txtTitleToolbar.setVisibility(View.INVISIBLE);
                break;
            default:
                binding.layoutToolbar.imgBack.setImageResource(R.drawable.ic_arrow_back_purple_24dp);
                binding.layoutToolbar.imgBack.setVisibility(View.VISIBLE);
                binding.layoutToolbar.imgSearch.setImageResource(R.drawable.ic_search_purple);
                binding.layoutToolbar.imgSearch.setVisibility(View.INVISIBLE);
                binding.layoutToolbar.txtTitleToolbar.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void onClickItemBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(position -> {
            switch (position) {
                case R.id.dialpad:
                    push(DialpadFragment.newInstance(), FragmentTag.FRAGMENT_DIALPAD);
                    break;
                case R.id.history:
                    push(HistoryFragment.newInstance(), FragmentTag.FRAGMENT_HISTORY);
                    break;
                case R.id.contact:
                    push(ContactsFragment.newInstance(), FragmentTag.FRAGMENT_CONTACTS);
                    break;
                case R.id.blockList:
                    push(BlockListFragment.newInstance(), FragmentTag.FRAGMENT_BLOCKED);
                    break;
                case R.id.settings:
                    push(SettingsFragment.newInstance(), FragmentTag.FRAGMENT_SETTINGS);

                    break;
            }
        });
        binding.layoutToolbar.imgBack.setOnClickListener(this);
        binding.layoutToolbar.imgSearch.setOnClickListener(this);
    }

    @Override
    public void getTitleToolbar(String title) {
        if (title != null) {
            binding.layoutToolbar.txtTitleToolbar.setText(title);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if ((fragment instanceof DialpadFragment)
                || (fragment instanceof HistoryFragment)
                || (fragment instanceof ContactsFragment)
                || (fragment instanceof BlockListFragment)
                || (fragment instanceof SettingsFragment)) {
            moveTaskToBack(true);
        } else {
            pop();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                pop();
                break;
            case R.id.imgSearch:        // Doing
        }
    }
}
