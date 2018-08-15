package com.kippinretail.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.R;
import com.kippinretail.loadingindicator.LoadingBox;


public class FragmentAboutUs extends Fragment {

    WebView webView_aboutUs;
    //https://www.kippinitsimple.com/AboutUs?platform=1
    String url = "https://www.kippinitsimple.com/index.html#about";
    public static FragmentAboutUs newInstance() {
        FragmentAboutUs fragment = new FragmentAboutUs();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
                    }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_about_us, container, false);
        initUI(v);
        return v;
    }
    private void initUI(View v){
        webView_aboutUs = (WebView)v.findViewById(R.id.webView_aboutUs);
        webView_aboutUs.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LoadingBox.dismissLoadingDialog();

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LoadingBox.showLoadingDialog(FragmentAboutUs.this.getActivity(), "Loading");
            }
        });
        webView_aboutUs.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView_aboutUs.getSettings().setJavaScriptEnabled(true);
        webView_aboutUs.loadUrl(url);
    }

    @Override
    public void onResume() {
        super.onResume();
        webView_aboutUs.reload();
    }
}
