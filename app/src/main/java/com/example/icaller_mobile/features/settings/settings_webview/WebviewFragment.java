package com.example.icaller_mobile.features.settings.settings_webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.BR;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.databinding.FragmentWebviewBinding;

public class WebviewFragment extends BaseFragment<FragmentWebviewBinding, WebviewViewModel> {
    public String title;
    public String url;

    public static Fragment newInstance() {
        WebviewFragment fragment = new WebviewFragment();
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
        return title;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_webview;
    }

    @Override
    public Constants.ToolbarStyle getToolbarStyle() {
        return Constants.ToolbarStyle.Backable;
    }

    @Override
    public WebviewViewModel getViewModel() {
        return new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(WebviewViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.loadUrl(url);
        binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.progressBarLoading.setVisibility(View.VISIBLE);
                binding.webview.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.webview.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                try {
                    if (request != null) binding.webview.loadUrl(request.getUrl().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }
}