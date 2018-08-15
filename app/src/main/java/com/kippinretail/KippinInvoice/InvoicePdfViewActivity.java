package com.kippinretail.KippinInvoice;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;

/**
 * Created by kamaljeet.singh on 12/14/2016.
 */

public class InvoicePdfViewActivity extends SuperActivity {

    private String urlpath;
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_pdf_viewer);
        initlization();
    }

    private void initlization() {

        BackWithHOME(getString(R.string.invoice_previe), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.HomeClick(InvoicePdfViewActivity.this);
            }
        });

        webView = (WebView) findViewById(R.id.webView);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            urlpath = bundle.getString("urlpath");
            LoadUrl(urlpath);
        }
    }

    private void LoadUrl(String urlpath) {
        Log.e("URL PATH", "===" + urlpath);
        webView.setWebViewClient(new myWebClient());
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("https://docs.google.com/viewer?url=" + urlpath);
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }
    }

}
