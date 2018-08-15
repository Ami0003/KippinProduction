package com.kippinretail.fragment;


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

import com.kippinretail.R;
import com.kippinretail.loadingindicator.LoadingBox;


public class FragmentContactUs extends Fragment {
    WebView webView_contactUs;
    //https://www.kippinitsimple.com/ContactUs?platform=1
    String url = "https://kippinitsimple.com/contact/";
    public static FragmentContactUs newInstance() {
        FragmentContactUs fragment = new FragmentContactUs();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_contact_us, container, false);
        initUI(v);
        return v;
    }
    private void initUI(View v){
        webView_contactUs = (WebView)v.findViewById(R.id.webView_contactUs);
        webView_contactUs.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LoadingBox.dismissLoadingDialog();

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LoadingBox.showLoadingDialog(FragmentContactUs.this.getActivity(), "Loading");
            }
        });
        webView_contactUs.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView_contactUs.getSettings().setJavaScriptEnabled(true);
        webView_contactUs.loadUrl(url);
    }
    public void onButtonPressed(Uri uri)
    {

    }


}
