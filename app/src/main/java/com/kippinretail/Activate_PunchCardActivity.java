package com.kippinretail;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.webclient.model.ArrayListPost;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.sharedpreferences.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class Activate_PunchCardActivity extends SuperActivity implements View.OnClickListener , View.OnFocusChangeListener,AdapterView.OnItemClickListener{


    ImageView template_image;
    TextView tvSelectPunches ;
    private TextView tvDescription;
    Button activateGiftCardButton;
    String[] TemplateAmountArray;
    final int max_punch  =7;
    ListView spinner;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_punch_card);
        Initlization();
        updateUI();
        addListener();
    }

    private void updateUI(){

    }
    private void addListener(){
        tvDescription.setOnFocusChangeListener(this);
    }

    private void Initlization() {
    generateActionBar(R.string.title_punch_card,true,true,false);
        TemplateAmountArray = new String[max_punch];


        for(int i =0;i<(max_punch) ; i++){

            TemplateAmountArray[i] = (i+1)+"" ;

        }

        tvDescription = (TextView)findViewById(R.id.tvDescription) ;
        tvSelectPunches = (TextView)findViewById(R.id.tvDescription) ;
        spinner = (ListView)findViewById(R.id.spList) ;

        template_image = (ImageView) findViewById(R.id.template_image);
        activateGiftCardButton = (Button) findViewById(R.id.activatePunchCardButton );
        activateGiftCardButton.setOnClickListener(this);
        int width = getResources().getDisplayMetrics().widthPixels;
        Picasso.with(Activate_PunchCardActivity.this)
                .load(CommonUtility.TemplateImagePath)
                .into(template_image);


        spinner.setAdapter(new ArrayAdapter<String>(Activate_PunchCardActivity.this, android.R.layout.simple_list_item_1, TemplateAmountArray));
        spinner.setOnItemClickListener(this);


        tvSelectPunches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.activatePunchCardButton:

                try{
                    int i = (Integer.parseInt(tvSelectPunches.getText().toString()));
                    ActivateCard();
                }catch (Exception e){
                    MessageDialog.showDialog(Activate_PunchCardActivity.this , "Please select punches" ,false);
                }

                break;
        }
    }

    private void ActivateCard() {
        String merchantId = "" + CommonData.getUserData(Activate_PunchCardActivity.this).getId();
        String country = Prefs.with(Activate_PunchCardActivity.this).getString("CountryName", "");
        Log.e("merchantId", "==" + merchantId);
        Log.e("country", "==" + country);
        Log.e("TemplateIdValue", "==" + CommonUtility.TemplateIdValue);


        ArrayListPost templatePosts = new ArrayListPost();
        templatePosts.add("TemplateId" , CommonUtility.TemplateIdValue  );
        templatePosts.add("Country" ,  country );
        templatePosts.add("Punches" , tvSelectPunches.getText().toString()  );
        templatePosts.add("FreePunches" ,tvSelectPunches.getText().toString() ) ;
        templatePosts.add("Description" ,tvDescription.getText().toString() ) ;


        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", templatePosts.getJson().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LoadingBox.showLoadingDialog(Activate_PunchCardActivity.this, "");
        RestClient.getApiServiceForPojo().CreatePunchCard(merchantId, in, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement activeData, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.i("Tag", "Request data " + new Gson().toJson(activeData));
                //[{"Id": 6,"TemplateName": "template1","TemplatePath": "http://kippin_retailpreproduction.web1.anzleads.com//template/template1.png",
                //"DateCreated": null,"IsDeleted": null,"UserId": null,"ResponseCode": 1,"ResponseMessage": "Success"}]

                JsonObject jsonObj = activeData.getAsJsonObject();
                String strObj = jsonObj.toString();
                JSONObject obj = new JSONObject();
                try {
                    JSONObject OBJ = new JSONObject(strObj);
                    String ResponseMessage = OBJ.getString("ResponseMessage");
                    String ResponseCode = OBJ.getString("ResponseCode");
                    if (ResponseCode.equals("1")) {
                        Show(ResponseMessage);
                    } else {
                        CommonDialog.With(Activate_PunchCardActivity.this).Show(ResponseMessage);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ActivateCard Tag", " = " + e.toString());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("ActivateCard Failure => ", " = " + error.getMessage());
                Log.e("URL Failure => ", " = " + error.getUrl());
                MessageDialog.showFailureDialog(Activate_PunchCardActivity.this);

            }

        });
    }
    public void Show(String message) {
        try {

            final Dialog dialog = new Dialog(Activate_PunchCardActivity.this,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_custom_msg);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            TextView textMessage = (TextView) dialog
                    .findViewById(R.id.textForMessage);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText(message);
            textMessage.setTextColor(Color.parseColor("#000000"));
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent();
                    in.setClass(Activate_PunchCardActivity.this, DashBoardMerchantActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d("onFocusChange","Focus loose");
        if(!hasFocus){
            Log.d("onFocusChange","Focus loose");
            Utils.hideKeyboard(Activate_PunchCardActivity.this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tvSelectPunches.setText(TemplateAmountArray[position]);
        spinner.setVisibility(View.GONE);
    }
}





