package com.example.icaller_mobile.features.block_contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.databinding.ItemBlockContactBinding;
import com.example.icaller_mobile.interfaces.ContactBlockOnClickListener;
import com.example.icaller_mobile.model.local.room.BlockContact;

import java.util.ArrayList;
import java.util.List;

public class ItemBlockContactAdapter extends RecyclerSwipeAdapter<ItemBlockContactAdapter.ViewHolder> {
    private List<BlockContact> blockContactList;
    private Context mContext;
    private ContactBlockOnClickListener listener;

    public void setListener(ContactBlockOnClickListener listener) {
        this.listener = listener;
    }

    public ItemBlockContactAdapter(Context context) {
        this.mContext = context;
        blockContactList = new ArrayList<>();
    }

    public void updateData(List<BlockContact> blockContactList) {
        this.blockContactList.clear();
        this.blockContactList.addAll(blockContactList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemBlockContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBlockContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_block_contact, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();
        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(holder.itemView, position);
    }


    @Override
    public int getItemCount() {
        return blockContactList == null ? 0 : blockContactList.size();
    }

    public List<BlockContact> getData() {
        return blockContactList;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemBlockContactBinding binding;

        public ViewHolder(ItemBlockContactBinding itemBlockContactBinding) {
            super(itemBlockContactBinding.getRoot());
            this.binding = itemBlockContactBinding;
        }

        public void bind() {
            BlockContact blockContact = blockContactList.get(getAdapterPosition());
            binding.setViewModel(blockContact);
            binding.textName.setText((blockContact.getName() == null || Utils.isEmpty(blockContact.getName().trim())) ? blockContact.getPhoneNumber() : blockContact.getName());
            int type = blockContact.getTypeReport();
            switch (type) {
                case 1:
                    break;
                case 2:
                    binding.textTypeSpam.setText(mContext.getString(R.string.advertising));
                    binding.imgTypeSpam.setImageResource(R.drawable.ic_red_advertising);
                    break;
                case 3:
                    binding.textTypeSpam.setText(mContext.getString(R.string.financial_service));
                    binding.imgTypeSpam.setImageResource(R.drawable.ic_red_financial_service);
                    break;
                case 4:
                    binding.textTypeSpam.setText(mContext.getString(R.string.loan_collection));
                    binding.imgTypeSpam.setImageResource(R.drawable.ic_red_loan_collection);
                    break;
                case 5:
                    binding.textTypeSpam.setText(mContext.getString(R.string.scam));
                    binding.imgTypeSpam.setImageResource(R.drawable.ic_red_scam);
                    break;
                case 6:
                    binding.textTypeSpam.setText(mContext.getString(R.string.real_estate));
                    binding.imgTypeSpam.setImageResource(R.drawable.ic_red_real_estate);
                    break;
                case 7:
                    binding.textTypeSpam.setText(mContext.getString(R.string.other));
                    binding.imgTypeSpam.setImageResource(R.drawable.ic_red_other);
                    break;

            }
            binding.swipe.setShowMode(SwipeLayout.ShowMode.PullOut);
            binding.swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                    //binding.lineUnblock.callOnClick();
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                    if (layout.getOpenStatus() == SwipeLayout.Status.Open) {
                        layout.close();
                    }
                }
            });
            binding.setListener(listener);
            binding.executePendingBindings();
        }

        public void close() {
            binding.swipe.close();
        }
    }


}
