package com.example.icaller_mobile.common.components.stickylistview;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by baonv on 19,July,2019
 * Hiworld JSC.
 */
public interface StickyHeadersAdapter<HeaderViewHolder extends RecyclerView.ViewHolder> {

    /**
     * Create a header {@link RecyclerView.ViewHolder ViewHolder} witch encapsulate the header view
     * You can either create a View manually or inflate it from an XML layout file.
     *
     * @param parent the parent {@link ViewGroup ViewGroup}
     * @return the newly created {@link RecyclerView.ViewHolder ViewHolder}
     */
    HeaderViewHolder onCreateViewHolder(ViewGroup parent);

    /**
     * Binds a header view according to the current item data.
     *
     * @param headerViewHolder the ViewHolder containing the view to bind
     * @param position         the current item position
     */
    void onBindViewHolder(HeaderViewHolder headerViewHolder, int position);

    /**
     * Get the header id associated with the specified item position.
     *
     * @param position the current item position
     * @return the header id for the current item
     */
    long getHeaderId(int position);
}
