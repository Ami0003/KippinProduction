package com.kippin.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;


/**
 * Created by kamaljeet.singh on 12/14/2016.
 */

public class WebPdfViewActivity extends SuperActivity {

    private String urlpath;
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_pdf_viewer);
        initlization();
    }

    private void initlization() {

    /*    BackWithHOME(getString(R.string.invoice_previe), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.HomeClick(InvoicePdfViewActivity.this);
            }
        });
*/
        webView = (WebView) findViewById(R.id.webView);
        //urlpath="http://52.60.139.201/Kippin/Finance/KippinStore/2018-04-28--16-25-59--KIPPIN_H7Mo9C.png";
        // LoadUrl(urlpath);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            urlpath = bundle.getString("url_path");
            String url = urlpath;

            if (url.contains(".pdf") || url.contains(".PDF")) {
                Log.e("--pdf url--- ", url);
                urlpath = "https://docs.google.com/viewer?url=" + urlpath;
            }
            if (url.contains(".TIF") || url.contains(".tif")) {
                Log.e("--tif url--- ", url);
                urlpath = "https://docs.google.com/viewer?url=" + urlpath;
            }
            //urlpath = "https://docs.google.com/viewer?url="+"http://52.60.139.201/Kippin/Finance/KippinStore/2018-04-9--00-50-01-Sheraton%20Parking.pdf.PDF";
            LoadUrl(urlpath);
        }
    }

    private void LoadUrl(String urlpath) {

        urlpath=urlpath.replace(" ","%20");
        Log.e("URL PATH", "===" + urlpath);
        webView.setWebViewClient(new myWebClient());
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(urlpath);
    }

    @Override
    public void setUpListeners() {

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
