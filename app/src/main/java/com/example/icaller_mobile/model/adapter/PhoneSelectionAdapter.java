package com.example.icaller_mobile.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.databinding.ItemBlockContactBinding;
import com.example.icaller_mobile.features.block_contact.ItemBlockContactAdapter;

import java.util.ArrayList;

public class PhoneSelectionAdapter extends RecyclerView.Adapter<PhoneSelectionAdapter.ViewHolder> {
    private ArrayList<String> phoneNumber;
    private Context mContext;


    public PhoneSelectionAdapter(Context mContext) {
        phoneNumber = new ArrayList<>();
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        
    }

    @Override
    public int getItemCount() {
        return phoneNumber.size();
    }


    @NonNull
    @Override
    public PhoneSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBlockContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_block_contact, parent, false);
        return new PhoneSelectionAdapter.ViewHolder(binding);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        .
    }
    .
}
