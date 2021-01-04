package com.example.icaller_mobile.features.settings.settings_faq;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.databinding.ItemFaqBinding;
import com.example.icaller_mobile.model.local.obj.QAObject;

import java.util.ArrayList;
import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder> implements FAQItemClickListener {
    private List<QAObject> itemList;
    private Context mContext;
    private boolean[] select;


    public FAQAdapter(Context context) {
        this.mContext = context;
        itemList = new ArrayList<>();
        select = new boolean[itemList == null ? 0 : itemList.size()];
    }

    public void updateData(List<QAObject> qaObjectList) {
        itemList.clear();
        itemList.addAll(qaObjectList);
        select = new boolean[itemList == null ? 0 : itemList.size()];
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFaqBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_faq, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();
        final Drawable arrow_down = ContextCompat.getDrawable(mContext, R.drawable.ic_faq_open);
        final Drawable arrow_top = ContextCompat.getDrawable(mContext, R.drawable.ic_faq_close);
        holder.binding.tvQTitle.setText((position + 1) + ". " + itemList.get(position).getQuestion());
        holder.binding.tvA.setText(itemList.get(position).getAnswer());
        String drawText = mContext.getText(Utils.isMIUI() ? R.string.key_faq_draw_miui : R.string.key_faq_draw).toString();
        int linkIndex = itemList.get(position).getAnswer().indexOf(drawText);
        if (linkIndex >= 0) {
            SpannableString s = Utils.toSpannableString(itemList.get(position).getAnswer().trim());
            ClickableSpan cs = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Utils.showOverlaySettingsForResult((Activity) mContext);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint textPaint) {
                    super.updateDrawState(textPaint);
                    textPaint.setColor(holder.binding.tvA.getResources().getColor(R.color.green_700));
                    textPaint.setUnderlineText(true);
                }
            };
            s.setSpan(cs, linkIndex, linkIndex + drawText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.binding.tvA.setText(s);
            holder.binding.tvA.setMovementMethod(LinkMovementMethod.getInstance());
        }

        if (position == 4 && !Utils.canDrawOverlays(mContext)) {
            holder.binding.badge.setVisibility(View.VISIBLE);
        } else {
            holder.binding.badge.setVisibility(View.GONE);
        }


        if (select.length > 0) {
            if (!select[position]) {
                holder.binding.tvA.setVisibility(View.GONE);
                holder.binding.imgStatus.setImageDrawable(arrow_down);
            } else {
                holder.binding.tvA.setVisibility(View.VISIBLE);
                holder.binding.imgStatus.setImageDrawable(arrow_top);
            }
            holder.binding.tvQTitle.setTag(position);
            holder.binding.tvQTitle.setOnClickListener(v -> {
                select[position] = !select[position];
                if (!select[position]) {
                    holder.binding.tvA.setVisibility(View.GONE);
                    holder.binding.imgStatus.setImageDrawable(arrow_down);
                } else {
                    holder.binding.tvA.setVisibility(View.VISIBLE);
                    holder.binding.imgStatus.setImageDrawable(arrow_top);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public void onItemClick(QAObject qaObject) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemFaqBinding binding;

        public ViewHolder(ItemFaqBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            binding.setViewModel(itemList.get(getAdapterPosition()));
            binding.executePendingBindings();
        }

    }
}
