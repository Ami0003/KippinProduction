package com.kippinretail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.ResponseModal;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityTermsAndConditions extends SuperActivity {
    TextView txt_termAndCondition;
    WebView wv_terms;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_terms_and_conditions);
        generateActionBar(R.string.terms_conditions_header, true,false,false);
        initUI();
        setUpUI();
    }
    private void initUI(){
        generateActionBar(R.string.title_term_condition, true, false, false);
        generateRightText("", null);
      // txt_termAndCondition = (TextView)findViewById(R.id.txt_termAndCondition);
        wv_terms = (WebView)findViewById(R.id.wv_terms);
    }

    @Override
    public void setUpUI() {
        super.setUpUI();
        RestClient.getApiServiceForPojo().TermsAndConditions(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Gson gson = new Gson();
                ResponseModal serverResponse = gson.fromJson(jsonElement.toString(), ResponseModal.class);
                if (serverResponse != null) {
                    String responseMessage = serverResponse.getResponseMessage();
                    String terms = Html.fromHtml(responseMessage.trim()).toString();

                  // txt_termAndCondition.setText(Html.fromHtml(responseMessage).toString().substring(9));
                 //   txt_termAndCondition.setMovementMethod(LinkMovementMethod.getInstance());
                   wv_terms.loadData(responseMessage.trim(), "text/html", "UTF-8");
                    wv_terms.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                //   wv_terms.loadDataWithBaseURL(null,terms,"text/html" , "utf-8" , null);

                    wv_terms.setWebViewClient(new WebViewClient(){
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            LoadingBox.dismissLoadingDialog();

                        }

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                            LoadingBox.showLoadingDialog(ActivityTermsAndConditions.this , "Loading");
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                MessageDialog.showFailureDialog(ActivityTermsAndConditions.this);
            }
        });
    }

    @Override
    public void setUpListeners() {
        super.setUpListeners();

    }
}
