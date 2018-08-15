package com.kippinretail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftCardMerchant.GiftCardMerchantData;
import com.kippinretail.app.Retail;
import com.kippinretail.interfaces.OnLocationGet;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.sharedpreferences.Prefs;
import com.kippinretail.wheel.ArrayWheelAdapter;
import com.kippinretail.wheel.OnWheelChangedListener;
import com.kippinretail.wheel.OnWheelScrollListener;
import com.kippinretail.wheel.WheelView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * Created by kamaljeet.singh on 3/26/2016.
 */
    public class Activate_MerchantCardActivity extends SuperActivity implements View.OnClickListener {

      String[] TemplateAmountArray = {
            "$5", "$10", "$15", "$20",
            "$25", "$30", "$35", "$40", "$45"
    };


    private final  int max = 1000;
    String country = null;
    private double mPrice = 25.00;
    ImageView template_image;
    Button activateGiftCardButton;
    // Wheel scrolled flag
    private boolean wheelScrolled = false;
    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }

        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;

            int id = wheel.getId();
            Log.e("After finish id", " = " + id);
            String temp  = TemplateAmountArray[wheel.getCurrentItem()];
            mPrice = Double.parseDouble(temp.substring(0,temp.length()-1));
            Log.e("mPrice", " = " + mPrice);
        }
    };
    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!wheelScrolled) {
                // updateStatus();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
      /*  Network.With(this).getCurrentLocation(
                new OnLocationGet() {
                    @Override
                    public void onLocationGet(double lattitude, double longitude, String mCountry, String city) {
                        country = mCountry;
                        LoadingBox.dismissLoadingDialog();
                    }
                });*/
        country = CommonData.getUserData(this).getCountry();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate_merchant_activity);
        Initlization();
    }

    private void Initlization() {
        generateActionBar(R.string.tile_activate_template,true,true,false);
        //TemplateAmountArray = new String[max/10 ];
        TemplateAmountArray = new String[99 ];

        //(max/10)
        int startPoint = 20;
        for(int i =0;i<99 ; i++){
          /*  if(i==0){
                continue;
            }*/
                TemplateAmountArray[i] = startPoint+"$" ;
            startPoint+=10;

        }



        template_image = (ImageView) findViewById(R.id.template_image);
        activateGiftCardButton = (Button) findViewById(R.id.activateGiftCardButton);
        activateGiftCardButton.setOnClickListener(this);
        int width = getResources().getDisplayMetrics().widthPixels;
        Picasso.with(Activate_MerchantCardActivity.this)
                .load(CommonUtility.TemplateImagePath)
                .into(template_image);

        initWheel(R.id.slot_1, TemplateAmountArray);
    }

    private void backPressed() {
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activateGiftCardButton:
                ActivateCard();
                break;
        }
    }

    private void ActivateCard() {
        String merchantId = "" + CommonData.getUserData(Activate_MerchantCardActivity.this).getId();

        Network network = new Network(this);
        network.getLocationParam(new OnLocationGet() {
            @Override
            public void onLocationGet(double lattitude, double longitude, String mCountry,String city) {
                country = mCountry;
            }
        }) ;

        Log.e("merchantId", "==" + merchantId);
        Log.e("country", "==" + Retail.app.getCountry());
        Log.e("TemplateIdValue", "==" + CommonUtility.TemplateIdValue);
        Log.e("mPrice", "==" + mPrice);

        String json = "{\"TemplateId\":\"" + CommonUtility.TemplateIdValue + "\",\"Country\":\"" + country + "\",\"Price\":\"" + mPrice + "\"}";
        Log.e("Json",json) ;
        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LoadingBox.showLoadingDialog(Activate_MerchantCardActivity.this, "");
        RestClient.getApiServiceForPojo().QueryToCreateGiftCardsMerchant(merchantId, in, new Callback<JsonElement>() {
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
                    if(ResponseCode.equals("1")) {
                        Show(ResponseMessage);
                    }
                    else{
                        CommonDialog.With(Activate_MerchantCardActivity.this).Show(ResponseMessage);
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
                MessageDialog.showFailureDialog(Activate_MerchantCardActivity.this);

            }

        });
    }
    public void Show(String message) {
        try {

            final Dialog dialog = new Dialog(Activate_MerchantCardActivity.this,
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
                    .findViewById(R.id.text_msg);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText(message);
            textMessage.setTextColor(Color.parseColor("#000000"));
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent();
                    in.setClass(Activate_MerchantCardActivity.this, DashBoardMerchantActivity.class);
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
    /**
     * Initializes wheel
     *
     * @param id the wheel widget Id
     */
    private void initWheel(int id, String cities[]) {

        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
                cities);

        WheelView wheel = getWheel(id);
        wheel.setViewAdapter(adapter);
        //wheel.setCurrentItem((int) (Math.random() * 10));
        wheel.setVisibleItems(0);

        wheel.setCurrentItem(4);


        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        // wheel.setCyclic(true);
        wheel.setEnabled(true);
    }


    /**
     * Returns wheel by Id
     *
     * @param id the wheel Id
     * @return the wheel with passed Id
     */
    private WheelView getWheel(int id) {
        return (WheelView) findViewById(id);
    }


}
