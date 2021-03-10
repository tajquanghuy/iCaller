package com.example.icaller_mobile.features.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.common.utils.ContactManager;
import com.example.icaller_mobile.databinding.ItemContactBinding;
import com.example.icaller_mobile.model.base.IContactObject;

import java.util.ArrayList;
import java.util.List;

public class ItemContactsAdapter extends RecyclerSwipeAdapter<ItemContactsAdapter.ViewHolder> {
    private Context mContext;
    private List<IContactObject> mData;

    public ItemContactsAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }

    public void updateData(List<IContactObject> mDatas) {
        this.mData.clear();
        this.mData.addAll(mDatas);
        notifyDataSetChanged();
    }

    @Override
    public ItemContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_contact, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bind();
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        int a = mData.size();
        return mData.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemContactBinding binding;

        public ViewHolder(ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            mData = ContactManager.getDefault().getAllDeviceContacts();
            IContactObject data = mData.get(getAdapterPosition());
            binding.txtTitle.setText(data.getName());
        }
    }
}
