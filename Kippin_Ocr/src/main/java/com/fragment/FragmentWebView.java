package com.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kippin.kippin.R;
import com.kippin.superviews.SuperFragment;



/**
 * Created by gaganpreet.singh on 6/14/2016.
 */
public class FragmentWebView extends SuperFragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.webview,null,false) ;
        webview = (WebView)root.findViewById(R.id.webview);
        initialiseUI();
        //webview.loadUrl(url);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // Load the webpage
        webview.loadUrl(url);



        generateActionBar( res, false, true);

        setFragmentTopbarClickListener(R.id.ivLogout,dashboardOpen);

        return root ;
    }

    String url ;
    int res;

   // @Bind(R.id.webview)
    WebView webview;

    public void load(String s,int s1){
        url = s;
        res = s1;
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI(root );
    }

    @Override
    public void setUpListeners() {
    }

}
