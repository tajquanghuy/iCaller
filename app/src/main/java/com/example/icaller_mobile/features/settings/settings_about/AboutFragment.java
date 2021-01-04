package com.example.icaller_mobile.features.settings.settings_about;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.BR;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.databinding.FragmentAboutBinding;

import java.util.Locale;

public class AboutFragment extends BaseFragment<FragmentAboutBinding, AboutViewModel> {

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public String getTitle() {
        return getString(R.string.key_about).toUpperCase();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    public Constants.ToolbarStyle getToolbarStyle() {
        return Constants.ToolbarStyle.Backable;
    }

    @Override
    public AboutViewModel getViewModel() {
        return new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(AboutViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String url;
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("vi")) {
            url = "file:///android_asset/about_vn.html";
        } else {
            url = "file:///android_asset/about_en.html";
        }
        binding.wvAbout.loadUrl(url);
        binding.wvAbout.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (Utils.isEmpty(url)) return;
                binding.wvAbout.loadUrl(url);
            }
        });
    }


}
