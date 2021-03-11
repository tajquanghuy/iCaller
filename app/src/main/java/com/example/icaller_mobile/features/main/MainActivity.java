package com.example.icaller_mobile.features.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.commonsware.cwac.saferoom.SQLCipherUtils;
import com.example.icaller_mobile.BR;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseActivity;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.constants.FragmentTag;
import com.example.icaller_mobile.common.service.GetDBService;
import com.example.icaller_mobile.common.utils.Logger;
import com.example.icaller_mobile.databinding.ActivityMainBinding;
import com.example.icaller_mobile.features.block_list.BlockListFragment;
import com.example.icaller_mobile.features.contacts.ContactsFragment;
import com.example.icaller_mobile.features.dialpad.DialpadFragment;
import com.example.icaller_mobile.features.history.HistoryFragment;
import com.example.icaller_mobile.features.ocr_machine.TextRecognitionFragment;
import com.example.icaller_mobile.features.search_contacts.SearchContactsFragment;
import com.example.icaller_mobile.features.settings.SettingsFragment;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.io.IOException;

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
        //initializeSQLCipher();
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
                binding.layoutToolbar.imgSearch.setOnClickListener(v -> {
                    openSearchView();
                });
                break;
            case OnlyText:
                binding.layoutToolbar.imgBack.setVisibility(View.GONE);
                binding.layoutToolbar.imgSearch.setVisibility(View.INVISIBLE);
                binding.layoutToolbar.txtTitleToolbar.setVisibility(View.VISIBLE);
                binding.layoutToolbar.viewBG.setVisibility(View.GONE);
                break;
            case None:
                binding.layoutToolbar.viewBG.setVisibility(View.GONE);
                binding.layoutToolbar.imgBack.setVisibility(View.INVISIBLE);
                binding.layoutToolbar.imgSearch.setVisibility(View.INVISIBLE);
                binding.layoutToolbar.txtTitleToolbar.setVisibility(View.INVISIBLE);
                break;
            case SearchBar:
                binding.layoutToolbar.imgBack.setImageResource(R.drawable.ic_arrow_back_purple_24dp);
                binding.layoutToolbar.imgBack.setVisibility(View.VISIBLE);
                binding.layoutToolbar.imgSearch.setVisibility(View.GONE);
                binding.layoutToolbar.viewBG.setVisibility(View.VISIBLE);
                binding.layoutToolbar.imgCameraSearch.setOnClickListener(v -> {
                    openOcrCamera();
                });
                binding.layoutToolbar.edtSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s == null || s.toString().equals("")) {
                            binding.layoutToolbar.imgCameraSearch.setVisibility(View.VISIBLE);
                            binding.layoutToolbar.imgCancel.setVisibility(View.INVISIBLE);
                        } else {
                            binding.layoutToolbar.imgCameraSearch.setVisibility(View.GONE);
                            binding.layoutToolbar.imgCancel.setVisibility(View.VISIBLE);
                            binding.layoutToolbar.imgCancel.setOnClickListener(v -> {
                                binding.layoutToolbar.edtSearch.setText(null);
                            });
                        }

                    }
                });
                binding.layoutToolbar.txtTitleToolbar.setVisibility(View.GONE);
                break;
            default:
                binding.layoutToolbar.viewBG.setVisibility(View.GONE);
                binding.layoutToolbar.imgBack.setImageResource(R.drawable.ic_arrow_back_purple_24dp);
                binding.layoutToolbar.imgBack.setVisibility(View.VISIBLE);
                binding.layoutToolbar.imgSearch.setImageResource(R.drawable.ic_search_purple);
                binding.layoutToolbar.imgSearch.setVisibility(View.INVISIBLE);
                binding.layoutToolbar.txtTitleToolbar.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void openSearchView() {
        SearchContactsFragment fragment = new SearchContactsFragment();
        push(fragment, FragmentTag.FRAGMENT_SEARCH);
    }

    private void openOcrCamera() {
        TextRecognitionFragment fragment = new TextRecognitionFragment();
        push(fragment, FragmentTag.FRAGMENT_TEXT_RECOGNITION);
        //startActivity(new Intent(MainActivity.this, TextRecognitionActivity.class));
    }

    @SuppressLint("NonConstantResourceId")
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                pop();
                break;
            case R.id.imgSearch:
                //openSearchView();
                break;
            case R.id.imgCameraSearch:
                //openOcrCamera();
                break;
        }
    }
}
