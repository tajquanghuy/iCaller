package com.example.icaller_mobile.features.contacts;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.utils.ContactManager;
import com.example.icaller_mobile.databinding.FragmentContactsBinding;
import com.example.icaller_mobile.model.base.IContactObject;

import java.util.List;

public class ContactsFragment extends BaseFragment<FragmentContactsBinding, ContactsViewModel> {
    private ItemContactsAdapter itemContactsAdapter;
    //    private ContactsViewModel mViewModel;
    private Context mContext;
    private List<IContactObject> contactObject;
//    private StickyHeadersItemDecoration stickyHeadersItemDecoration;
//    private StickyHeaderAdapter stickyHeaderAdapter;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_contact).toUpperCase();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    public Constants.ToolbarStyle getToolbarStyle() {
        return Constants.ToolbarStyle.BonusSearch;
    }

    @Override
    public ContactsViewModel getViewModel() {
        return new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(ContactsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        reLoad(contactObject);
    }

    private void initialize() {
        ContactManager.init(mContext);
        contactObject = ContactManager.getDefault().getAllDeviceContacts();
        itemContactsAdapter = new ItemContactsAdapter(mContext);
        binding.rvContacts.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        binding.rvContacts.setHasFixedSize(true);
        itemContactsAdapter.updateData(contactObject);
        binding.rvContacts.setAdapter(itemContactsAdapter);
        loadData();
//        stickyHeaderAdapter = new StickyHeaderAdapter(new ArrayList<>());
//        stickyHeadersItemDecoration = new StickyHeadersBuilder()
//                .setAdapter(itemContactsAdapter)
//                .setRecyclerView(binding.rvContacts)
//                .setStickyHeadersAdapter(stickyHeaderAdapter)
//                .build();
//        binding.rvContacts.addItemDecoration(stickyHeadersItemDecoration);
    }

    private void loadData() {
        boolean isLoading = ContactManager.getDefault().getDeviceContactAsync();
        if (itemContactsAdapter.getItemCount() == 0) {
            updateViews(isLoading);
        }
    }

    private void updateViews(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.imgNoPermission.setVisibility(View.GONE);
            binding.textNoData.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.imgNoPermission.setVisibility(View.VISIBLE);
            binding.textNoData.setVisibility(View.VISIBLE);

            if (ContactManager.getDefault().isContactAccessible()) {
                binding.imgNoPermission.setVisibility(View.GONE);
            } else {
                binding.textNoData.setVisibility(View.GONE);
            }

            if (itemContactsAdapter.getItemCount() > 0) {
                binding.textNoData.setVisibility(View.GONE);
            }
        }
    }

    private void reLoad(List<IContactObject> contacts) {
        //binding.rvContacts.removeItemDecoration(stickyHeadersItemDecoration);
        //stickyHeaderAdapter.Update(contacts);
        itemContactsAdapter.updateData(contacts);
        //binding.rvContacts.addItemDecoration(stickyHeadersItemDecoration);
    }
}