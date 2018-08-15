package com.pack.kippin;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.TemplateData;


/**
 * Created by dilip.singh on 1/27/2016.
 */
public class TermsAndCondition extends SuperActivity {

   // @Bind(R.id.text_loreipsum)
    WebView textViewLoremTerms;

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        textViewLoremTerms =(WebView)findViewById(R.id.text_loreipsum);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.checkCrashTracker(TermsAndCondition.this);
        setContentView(R.layout.layout_terms_condition);
        initialiseUI();
        setUpListeners();;
      //  ButterKnife.bind(TermsAndCondition.this);

        WSUtils.hitServiceGet(this, Url.getTermsAndConditions(), new ArrayListPost(), TemplateData.class, new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {
//                textViewLoremTerms.getSettings().setJavaScriptEnabled(true);


                String myParagraph = data.getResponseMsg().trim();

//                myParagraph = myParagraph.replace("<strong>", "<b>");
                myParagraph = myParagraph.replace("’", "'");

//                Spanned s = Html.fromHtml(myParagraph) ;


                textViewLoremTerms.loadData(myParagraph, "text/html", "UTF-8");

//                textViewLoremTerms.setText(s);
//                textViewLoremTerms.setMovementMethod(LinkMovementMethod.getInstance());

                textViewLoremTerms.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        LoadingBox.dismissLoadingDialog();

                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        LoadingBox.showLoadingDialog(TermsAndCondition.this, "Loading");
                    }
                });

            }
        });


        generateActionBar(R.string.terms_of_use, true, false);

    }

  //  @OnClick(R.id.tvSendAgreement)
    public void onClick(View v){
        DialogBox.SendAgreement(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        TermsAndCondition.this.overridePendingTransition(R.anim.push_out_to_left,
                R.anim.push_out_to_right);

    }


    @Override
    public void setUpListeners() {

    }
}
