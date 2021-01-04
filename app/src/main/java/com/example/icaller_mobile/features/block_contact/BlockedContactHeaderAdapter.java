package com.example.icaller_mobile.features.block_contact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.common.components.stickylistview.StickyHeadersAdapter;
import com.example.icaller_mobile.model.base.IContactList;


/**
 * Created by baonv on 19,July,2019
 * Hiworld JSC.
 */
public class BlockedContactHeaderAdapter implements StickyHeadersAdapter<BlockedContactHeaderAdapter.ViewHolder> {

    private IContactList mListHeader;
    private static boolean isAdapterForCountries = false;

    public BlockedContactHeaderAdapter() {
    }

    public BlockedContactHeaderAdapter(IContactList mListHeader) {
        this.mListHeader = mListHeader;

    }

    public void Update(IContactList newList) {
        this.mListHeader = newList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (position < mListHeader.size()) {
            viewHolder.txtHeaderTitle.setText(mListHeader.get(position).getGroup());
            if (isAdapterForCountries) {
                viewHolder.txtHeaderTitle.setPadding(25, 0, 0, 0);
            }

            viewHolder.itemView.setTag(mListHeader.get(position).getGroup());
        }
    }

    @Override
    public long getHeaderId(int position) {
        if (position < mListHeader.size())
            return mListHeader.get(position).getGroup().hashCode();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtHeaderTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            txtHeaderTitle = itemView.findViewById(R.id.section_text);
        }

    }
}

