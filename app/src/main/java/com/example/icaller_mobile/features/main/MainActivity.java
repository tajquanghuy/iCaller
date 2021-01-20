package com.example.icaller_mobile.features.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.example.icaller_mobile.common.constants.FirebaseConstants;
import com.example.icaller_mobile.common.constants.FragmentTag;
import com.example.icaller_mobile.common.service.GetDBService;
import com.example.icaller_mobile.common.utils.LogEvents;
import com.example.icaller_mobile.common.utils.Logger;
import com.example.icaller_mobile.databinding.ActivityMainBinding;
import com.example.icaller_mobile.features.block_list.BlockListFragment;
import com.example.icaller_mobile.features.contacts.ContactsFragment;
import com.example.icaller_mobile.features.dialpad.DialpadFragment;
import com.example.icaller_mobile.features.history.HistoryFragment;
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


    private void initializeSQLCipher() {
        Logger.log("Key", "decryptDatabase: " + getText(R.string.key_decrypt_room_db).toString());
        SQLiteDatabase.loadLibs(this);
        File databaseFile = getDatabasePath("db_contact_block");
        SQLCipherUtils.State state = SQLCipherUtils.getDatabaseState(databaseFile);
        if (state.equals(SQLCipherUtils.State.ENCRYPTED)) {
            try {
                Logger.log("Database", "decryptDatabase: " + SQLCipherUtils.getDatabaseState(databaseFile));
                SQLCipherUtils.decrypt(this, databaseFile, getText(R.string.key_decrypt_room_db).toString().toCharArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

                });
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


    private void openSearchView() {
        SearchFragmentContactDevice fragment = new SearchFragmentContactDevice();
        push(fragment, Constants.FragmentTag.FRAGMENT_SEARCH, false);
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
            case R.id.imgSearch:        // Doing
        }
    }
}
