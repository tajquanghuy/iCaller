package com.example.icaller_mobile.common.custom_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.icaller_mobile.R;


public class CustomSpinnerWarntypeAdapter extends BaseAdapter {
    private Context mContext;
    private int iconWarntype[];
    private String[] textWarntype;

    public CustomSpinnerWarntypeAdapter(Context mContext, int[] iconWarntype, String[] textWarntype) {
        this.mContext = mContext;
        this.iconWarntype = iconWarntype;
        this.textWarntype = textWarntype;
    }

    @Override
    public int getCount() {
        return iconWarntype.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_spinner_warntype, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgWarntype = convertView.findViewById(R.id.img_warn_type);
            viewHolder.textWarntype = convertView.findViewById(R.id.text_warn_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imgWarntype.setImageDrawable(mContext.getDrawable(iconWarntype[position]));
        viewHolder.textWarntype.setText(textWarntype[position]);
        return convertView;
    }

    public class ViewHolder {
        AppCompatImageView imgWarntype;
        AppCompatTextView textWarntype;
    }
}
