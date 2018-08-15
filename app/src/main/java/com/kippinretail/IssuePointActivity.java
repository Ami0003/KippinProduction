package com.kippinretail;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.JsonElement;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class IssuePointActivity extends Activity implements View.OnClickListener {

    private RelativeLayout backArrowLayout;
    private Button btnAuthenticate;
    private EditText privateKey;
    String customerId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_point);
        initilization();
    }
    private void initilization() {
        backArrowLayout = (RelativeLayout) findViewById(R.id.backArrowLayout);
        btnAuthenticate = (Button)findViewById(R.id.btnAuthenticate);
        privateKey = (EditText)findViewById(R.id.privateKey);
        backArrowLayout.setOnClickListener(this);
        btnAuthenticate.setOnClickListener(this);
        customerId = String.valueOf(CommonData.getUserData(IssuePointActivity.this).getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArrowLayout:
                Utils.backPressed(IssuePointActivity.this);
                break;
            case R.id.btnAuthenticate:
                authenticateUser();
                break;
        }
    }

    private void authenticateUser() {
        if (privateKey.getText().toString().length() == 0) {
            MessageDialog.showDialog(IssuePointActivity.this,"Enter Private Key");
        } else {
            LoadingBox.showLoadingDialog(IssuePointActivity.this, "Loading...");
            RestClient.getApiServiceForPojo().AuthenticateUserForEmployee(customerId, privateKey.getText().toString(), "",new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("Output-->",jsonElement.toString());
                    Log.e("url",response.getUrl());
                    LoadingBox.dismissLoadingDialog();
                }

                @Override
                public void failure(RetrofitError error) {
                    LoadingBox.dismissLoadingDialog();
                    Log.e("url", error.getUrl() + "=====");
                    MessageDialog.showFailureDialog(IssuePointActivity.this);
                    MessageDialog.showDialog(IssuePointActivity.this, "We are experiencing technical difficulties but value your business… please try again later");

                }
            });
        }

    }
}
