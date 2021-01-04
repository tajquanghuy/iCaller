package com.example.icaller_mobile.common.utils;

import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BindingUtils {
    @BindingAdapter({"visible_or_gone"})
    public static void setVisibleOrGone(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter({"show_swipe"})
    public static void setSwipe(SwipeRefreshLayout view, boolean visible) {
        view.setRefreshing(visible);
    }

    @BindingAdapter({"show_image_clear_edittext"})
    public static void setVisibleOrGone(ImageView imageView, String str) {
        if (Utils.isEmpty(str)) {
            imageView.setVisibility(View.GONE);
            return;
        }
        imageView.setVisibility(View.VISIBLE);
    }

    @BindingAdapter({"set_text_html"})
    public static void setTextHtml(TextView textView, String str) {
        if (Utils.isEmpty(str)) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(str));
        }
    }

}
