package com.kippinretail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonData;

import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.Card.CardDetail;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyPoint_UserLoyalityCardActivity extends Activity implements View.OnClickListener {

    private RelativeLayout backArrowLayout;
    private TextView txtLogo;
    String customerId, merchantId, folderName;
    private ImageView frontImage, backImage;

    String name ="";
    String barcode = "";

    TextView loyalityCardName , tvBarcode;

    ImageView ivBarcode ;

    BarcodeUtil barcodeUtil ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        merchantId = getIntent().getStringExtra("merchantId");
        if(merchantId==null || merchantId.length()==0)merchantId=""+0;

        if(merchantId.equalsIgnoreCase("0")) {
            setContentView(R.layout.activity_my_point__user_loyality_card);
        }
        else {
            setContentView(R.layout.user_loyality_card_merchant);
        }
        initilization();
        setUpUI();
    }

    private void setUpUI() {

        if(merchantId.equalsIgnoreCase("0")) {
            getAllCards();
        }
        else  {
            name = getIntent().getStringExtra("name") ;
            barcode = getIntent().getStringExtra("barcode") ;



            barcodeUtil = new BarcodeUtil(this,findViewById(R.id.root)) ;
            loyalityCardName = (TextView)findViewById(R.id.tvName) ;
            ivBarcode  = (ImageView)findViewById(R.id.ivBarcode) ;
            tvBarcode  = (TextView)findViewById(R.id.tvPunches) ;

            loyalityCardName.setText(name);
            tvBarcode.setText(barcode);
            ivBarcode.setImageBitmap(((BitmapDrawable) barcodeUtil.generateBarcode(barcode)).getBitmap());

        };
    }

    private void initilization() {
        backArrowLayout = (RelativeLayout) findViewById(R.id.backArrowLayout);
        txtLogo = (TextView) findViewById(R.id.txtLogo);
        frontImage = (ImageView) findViewById(R.id.frontImage);
        backImage = (ImageView) findViewById(R.id.backImage);
        backArrowLayout.setOnClickListener(this);
        customerId = String.valueOf(CommonData.getUserData(MyPoint_UserLoyalityCardActivity.this).getId());
        Intent intent = getIntent();
        if (intent != null) {

            folderName = intent.getStringExtra("folderName");
        }
        txtLogo.setText("LOYALITY CARDS");
    }

    private void getAllCards() {
        LoadingBox.showLoadingDialog(MyPoint_UserLoyalityCardActivity.this, "Loading...");


        switch (getIntent().getStringExtra(MyPoint_FolderListActivity.OPERATION)){
            case MyPoint_FolderListActivity.LOYALITY_CARD:
                RestClient.getApiServiceForPojo().LoyalityGiftcardImages(customerId, merchantId, folderName,callback);
                break;
            case MyPoint_FolderListActivity.GIFTCARD:
                RestClient.getApiServiceForPojo().GetPhysicalCardUploadedImages(customerId, merchantId, folderName,callback);
                break;
        }
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArrowLayout:
                Utils.backPressed(MyPoint_UserLoyalityCardActivity.this);
                break;
        }
    }

    Callback callback =  new Callback<JsonElement>() {
        @Override
        public void success(JsonElement jsonElement, Response response) {
            Log.e("Url", response.getUrl());
            Log.e("OUTPUT -->", jsonElement.toString());
            Type listType = new TypeToken<List<CardDetail>>() {
            }.getType();
            Gson gson = new Gson();
            List<CardDetail> cardDetails = (List<CardDetail>) gson.fromJson(jsonElement.toString(), listType);
            LoadingBox.dismissLoadingDialog();
            if (cardDetails != null) {
                String frontImagePath = cardDetails.get(0).getFrontImagePath();
                String backImagePath = cardDetails.get(0).getBackImagePath();
                if (frontImagePath != null) {
                    Picasso.with(MyPoint_UserLoyalityCardActivity.this)
                            .load(frontImagePath)
                            .into(frontImage);
                }
                if (backImagePath != null) {
                    Picasso.with(MyPoint_UserLoyalityCardActivity.this)
                            .load(backImagePath)
                            .into(backImage);
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            LoadingBox.dismissLoadingDialog();
            MessageDialog.showFailureDialog(MyPoint_UserLoyalityCardActivity.this);

        }
    };

}
