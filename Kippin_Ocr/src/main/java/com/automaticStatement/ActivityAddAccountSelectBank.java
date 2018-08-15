package com.automaticStatement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;



// SCREEN USED TO LINK ACCOUNT USING WEBVIEW
public class ActivityAddAccountSelectBank extends SuperActivity implements View.OnClickListener {

  //  @Bind(R.id.ivRbc)
    ImageView ivRbc;

   // @Bind(R.id.ivScotia)
    ImageView ivScotia;

  //  @Bind(R.id.ivTD)
    ImageView ivTD;

  //  @Bind(R.id.ivBmo)
    ImageView ivBmo;

  //  @Bind(R.id.ivOtherBank)
    ImageView ivOtherBank;

    WebView webViewAddAccount;


 //   @Bind(R.id.ivCIBC)
    ImageView ivCIBC;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_select_bank);
        generateActionBar(R.string.add_account, true, true);
        initialiseUI();
      //  setUpListeners();
        updateUI();
         /* Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url.BASE_URL_WITHOUT_MOBILE_API+"/AutomaticUploadProcess/AddAccountwithKippin?userId=" + Singleton.getUser().getId()));
             startActivity(myIntent);*/
    }

    public void initialiseUI(){
       /* ivRbc = (ImageView) findViewById(R.id.ivRbc);
        ivScotia = (ImageView) findViewById(R.id.ivScotia);
        ivTD = (ImageView) findViewById(R.id.ivTD);
        ivBmo = (ImageView) findViewById(R.id.ivBmo);
        ivOtherBank = (ImageView) findViewById(R.id.ivOtherBank);
        ivCIBC = (ImageView) findViewById(R.id.ivCIBC);*/
        webViewAddAccount = (WebView)findViewById(R.id.webViewAddAccount);
    }

    private void updateUI(){

        webViewAddAccount.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Utility.dismissDialog();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Utility.showDialog(ActivityAddAccountSelectBank.this);
            }
        });
        webViewAddAccount.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webViewAddAccount.getSettings().setJavaScriptEnabled(true);
        webViewAddAccount.loadUrl(Url.URL_ACTIVITY_AUTOATIC_CONNECTION+Singleton.getUser().getId());
    }
    
    @Override
    public void setUpListeners() {
        ivRbc.setOnClickListener(this);
        ivScotia.setOnClickListener(this);
        ivTD.setOnClickListener(this);
        ivBmo.setOnClickListener(this);
        ivCIBC.setOnClickListener(this);
        ivOtherBank.setOnClickListener(this);
    }


    
    @Override
    public void onClick(View v) {
        setResult(v.getId());
       finish();
    }
}
