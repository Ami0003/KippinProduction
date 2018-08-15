package com.kippinretail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.ServerResponseModal.ServerResponse;
import com.kippinretail.Modal.webclient.model.ArrayListPost;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 3/5/2016.
 */
public class ReferralActivity extends SuperActivity implements View.OnClickListener {
    EditText refrelCodedEditText;
    Button submitButton, skipButton;
    String roleId,referralCode;
    private String currentUserId = "";
    TextView thankuTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        Initilization();
        hideKeyboard();
    }
    // function to hide keyboard on login screen
    private void hideKeyboard() {

        // Check if no view has focus:
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }
    private void Initilization() {
        // EditText Id's
        refrelCodedEditText = (EditText) findViewById(R.id.refrelCodedEditText);
        //Buttons Id's
        currentUserId = String.valueOf(CommonData.getUserData(this).getId());
        submitButton = (Button) findViewById(R.id.submitButton);
        skipButton = (Button) findViewById(R.id.skipButton);
        thankuTextView = (TextView)findViewById(R.id.thankuTextView);
        submitButton.setOnClickListener(this);
        skipButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        if(i!=null){
            roleId = i.getStringExtra("roleId");
            referralCode = i.getStringExtra("ReferralCode");
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.submitButton:
                if(refrelCodedEditText.getText().toString().length()!=0){
                    availOffer();
                }
                else{
                    MessageDialog.showDialog(ReferralActivity.this , "Please enter  referral code",false);
                }
                break;
            case R.id.skipButton:
                skipOffer();
        }



    }

    private void availOffer(){

        HashMap<String , String> jsonBody = new HashMap<String,String>();
        jsonBody.put("userId",currentUserId);
        jsonBody.put("ReferralCode", refrelCodedEditText.getText().toString());
        LoadingBox.showLoadingDialog(this,"");
        RestClient.getApiServiceForPojo().UpdateReferralId(jsonBody, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Gson gson = new Gson();
                ServerResponse resp = gson.fromJson(jsonElement.toString(), new TypeToken<ServerResponse>() {

                }.getType());
                if(resp.getResponseMessage().equals("Success.")){
                    MessageDialog.showDialog(ReferralActivity.this, "Sucess", UserDashBoardActivity.class);


                }else{
                    MessageDialog.showDialog(ReferralActivity.this,"Invalid referral code.",false);

                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(ReferralActivity.this, error);
            }
        });

    }


    private void skipOffer(){

        RestClient.getApiServiceForPojo().UpdateIsFirstLogint(currentUserId, "",new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                android.util.Log.e("=====",response.getUrl());
                Gson gson = new Gson();
                ServerResponse resp = gson.fromJson(jsonElement.toString(), new TypeToken<ServerResponse>() {

                }.getType());
                if(resp.getResponseMessage().equals("Success.")){

                    moveToDashBoard();

                }else{

                    moveToDashBoard();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                android.util.Log.e(error.getMessage(), error.getUrl());
                MessageDialog.showDialog(ReferralActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }
    private void moveToDashBoard(){
        Intent in = new Intent();
        in.setClass(ReferralActivity.this, UserDashBoardActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }
}
