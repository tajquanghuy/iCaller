package com.example.icaller_mobile.features.block_list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.icaller_mobile.BR;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.components.stickylistview.StickyHeadersItemDecoration;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.constants.FragmentTag;
import com.example.icaller_mobile.common.constants.IntentConstants;
import com.example.icaller_mobile.common.event.EventBusAction;
import com.example.icaller_mobile.common.event.MessageEvent;
import com.example.icaller_mobile.common.manager.SharedPreferencesManager;
import com.example.icaller_mobile.common.widget.dialog.ConfirmDialog;
import com.example.icaller_mobile.databinding.FragmentBlockListBinding;
import com.example.icaller_mobile.features.block_contact.BlockedContactHeaderAdapter;
import com.example.icaller_mobile.features.block_contact.ItemBlockContactAdapter;
import com.example.icaller_mobile.features.block_detail.DetailContactFragment;
import com.example.icaller_mobile.features.main.MainViewModel;
import com.example.icaller_mobile.features.settings.settings_logout.DialogOnclickListener;
import com.example.icaller_mobile.interfaces.ContactBlockOnClickListener;
import com.example.icaller_mobile.model.local.room.BlockContact;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;


public class BlockListFragment extends BaseFragment<FragmentBlockListBinding, BlockListViewModel> implements ContactBlockOnClickListener {
    private ItemBlockContactAdapter itemBlockContactAdapter;
    private MainViewModel mainViewModel;
    private Context mContext;
    private BlockedContactHeaderAdapter stickyHeaderAdapter;
    private StickyHeadersItemDecoration stickyHeadersItemDecoration;


    public static BlockListFragment newInstance() {
        BlockListFragment fragment = new BlockListFragment();
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
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_block).toUpperCase();
    }

    @Override
    public Constants.ToolbarStyle getToolbarStyle() {
        return Constants.ToolbarStyle.BonusSearch;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_block_list;
    }

    @Override
    public BlockListViewModel getViewModel() {
        mainViewModel = new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(MainViewModel.class);
        return new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(BlockListViewModel.class);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initSettingBlock();
        initializeViews();
        initObserve();
    }

    private void initializeViews() {
        itemBlockContactAdapter = new ItemBlockContactAdapter(mContext);
        binding.rvContact.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        binding.rvContact.setHasFixedSize(true);
        binding.rvContact.setAdapter(itemBlockContactAdapter);

        itemBlockContactAdapter.setListener(this);
        mViewModel.getAllBlockContact();
    }

    private void initSettingBlock() {       // Doing
        setStatusSwitch(binding.switchScam);
        setStatusSwitch(binding.switchAdvertising);
        setStatusSwitch(binding.switchNotContact);
        setStatusSwitch(binding.switchForeignNumber);
    }

    private void setOnCheckedChangeListener(SwitchCompat switchCompat) {
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            EventBus.getDefault().postSticky(new MessageEvent(EventBusAction.DATA_BLOCK_CHANGE, null, null));
            switch (switchCompat.getId()) {
                case R.id.switch_scam:
                    SharedPreferencesManager.getDefault().saveBlockScam(isChecked);
                    break;
                case R.id.switch_advertising:
                    SharedPreferencesManager.getDefault().saveBlockAdvertising(isChecked);
                    break;
                case R.id.switch_not_contact:
                    SharedPreferencesManager.getDefault().saveBlockOutContact(isChecked);
                    break;
                case R.id.switch_foreign_number:
                    SharedPreferencesManager.getDefault().saveBlockForeignNumber(isChecked);
                    break;
            }
        });

    }

    private void setStatusSwitch(SwitchCompat switchCompat) {
        boolean status = false;
        switch (switchCompat.getId()) {
            case R.id.switch_scam:
                status = SharedPreferencesManager.getDefault().blockScamEnable();
                break;
            case R.id.switch_advertising:
                status = SharedPreferencesManager.getDefault().blockAdvertisingEnable();
                break;
            case R.id.switch_not_contact:
                status = SharedPreferencesManager.getDefault().blockOutContactEnable();
                break;
            case R.id.switch_foreign_number:
                status = SharedPreferencesManager.getDefault().blockForeignNumberEnable();
                break;
        }
        switchCompat.setChecked(status);
        setOnCheckedChangeListener(switchCompat);
    }

    private void initObserve() {
        mViewModel.listBlockContact.observe(getViewLifecycleOwner(), blockContacts -> {
            itemBlockContactAdapter.updateData(blockContacts);
        });
    }

    @Override
    public void onItemClick(BlockContact contact) {
        DetailContactFragment detailContactFragment = DetailContactFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentConstants.KEY_BLOCK_LIST_FRAGMENT, contact);
        detailContactFragment.setArguments(bundle);
        mActivity.push(detailContactFragment, FragmentTag.FRAGMENT_DETAIL_CONTACT);
    }


    @Override
    public void unblockContact(BlockContact contact) {
        mViewModel.unBlockContact(contact);
        ConfirmDialog confirmDialog = new ConfirmDialog(mContext, mContext.getString(R.string.dialog_unblocked),
                mContext.getString(R.string.key_number_unblocked),
                mContext.getString(R.string.key_ok),
                new DialogOnclickListener() {
                    @Override
                    public void onButtonConfirmClick() {
                        itemBlockContactAdapter.closeItem(itemBlockContactAdapter.getData().indexOf(contact));
                    }

                    @Override
                    public void onImageCloseClick() {
                    }
                });
        confirmDialog.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mContext == null) return;
                ((Activity) mContext).runOnUiThread(() -> {
                    if (confirmDialog.isShowing()) {
                        confirmDialog.dismiss();
                    }
                });
            }
        }, Constants.TIME_AUTO_CLOSE_DIALOG);
        itemBlockContactAdapter.closeItem(itemBlockContactAdapter.getData().indexOf(contact));
    }


}