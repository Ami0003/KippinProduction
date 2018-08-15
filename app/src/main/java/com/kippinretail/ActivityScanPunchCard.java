package com.kippinretail;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.kippinretail.scanning.ScannerActivity;
import com.dm.zbar.android.scanner.HandleManualInput;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.EmplyeeMerchantListAdapter;
import com.kippinretail.ApplicationuUlity.*;
import com.kippinretail.ApplicationuUlity.BarcodeUtil;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.OnBarcodeGetListener;
import com.kippinretail.Modal.MerchantAsEmployeeList.MerchantAsEmployeeDetail;
import com.kippinretail.Modal.ResponseModal;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityScanPunchCard extends SuperActivity {
    BarcodeUtil barcodeUtil;
    EditText ed_barcodeNumber;
    private String merchantId;
    private final static int REQUEST_SCANNER = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_scan_punch_card);
        initialiseUI();
        updateToolBar();
        setUpListeners();;
        setUpUI();;
    }
    private void updateToolBar() {
        generateActionBar(R.string.title_user_ActivityScanPunchCard, true, true, false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(this);
        if (com.kippinretail.config.Config.scanner) {
            Log.e("----CONTENT:----:", "" + com.kippinretail.config.Config.CONTENT);
            Log.e("---FORMAT:-------", "" + com.kippinretail.config.Config.FORMAT);
            com.kippinretail.config.Config.scanner = false;
            if (com.kippinretail.config.Config.CONTENT.equals("")) {

            } else {
                String cardNo = com.kippinretail.config.Config.CONTENT;
                String card_No = com.kippinretail.config.Config.FORMAT;

                Log.e("----cardNo----:", "" + cardNo);
                Log.e("---card_No:-------", "" + card_No);
                ed_barcodeNumber.setText(cardNo);
                com.kippinretail.config.Config.FORMAT="";
                com.kippinretail.config.Config.CONTENT="";
            }
        }
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        ed_barcodeNumber = (EditText)findViewById(R.id.ed_barcodeNumber);
        barcodeUtil = new BarcodeUtil(this);
        merchantId = getIntent().getStringExtra("merchantId");
    }

    public void scanPunchCard(View v){
        Intent i = new Intent();
        i.setClass(ActivityScanPunchCard.this, ScannerActivity.class);
        //startActivity(i);
        startActivityForResult(i, REQUEST_SCANNER);
      /*  barcodeUtil.launchScanner(CommonUtility.REQUEST_CODE_BARCODE,new HandleManualInput(){

            @Override
            public void onManualClick() {
                Intent i = new Intent();
                i.setClass(ActivityScanPunchCard.this ,ActivityINputManualBarcode.class );
                startActivityForResult(i,CommonUtility.REQUEST_MANUAL_BARCODE);

            }
        },"Please scan your punch card");*/
    }
    public void stampPunchCard(View v){
        if(ed_barcodeNumber.getText().toString().length()==0){
            MessageDialog.showDialog(this,"Please scan customer number",false);
        }
        else{
            usePunchCard();
        }

    }

    private void usePunchCard() {
        String userId = String.valueOf(CommonData.getUserData(this).getId());
        LoadingBox.showLoadingDialog(this, "Loading...");
        RestClient.getApiServiceForPojo().IssuePunchCard(userId,ed_barcodeNumber.getText().toString(),merchantId,"", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Type listType = new TypeToken<List<MerchantAsEmployeeDetail>>() {
                }.getType();
                Gson gson = new Gson();
                Log.e("",jsonElement.toString());
                ResponseModal serverResponse =  gson.fromJson(jsonElement.toString(), ResponseModal.class);

                if(serverResponse.getResponseCode().equals("1")){
                    MessageDialog.showDialog(ActivityScanPunchCard.this, serverResponse.getResponseMessage(),true);
                    ed_barcodeNumber.setText("");
                }
                else{
                    MessageDialog.showDialog(ActivityScanPunchCard.this, serverResponse.getResponseMessage(),true);
                    ed_barcodeNumber.setText("");
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showFailureDialog(ActivityScanPunchCard.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SCANNER:
                    Log.e("----CONTENT:----:", "" + data.getStringExtra(com.kippinretail.config.Config.CONTENT));
                    Log.e("---FORMAT:-------", "" + data.getStringExtra(com.kippinretail.config.Config.FORMAT));
                    if (data != null) {
                        String cardNo = data.getStringExtra(com.kippinretail.config.Config.CONTENT);
                        String card_No = data.getStringExtra(com.kippinretail.config.Config.FORMAT);
                        Log.e("----cardNo----:", "" + cardNo);
                        Log.e("---card_No:-------", "" + card_No);
                        //  ed_barcodeNumber.setText(cardNo);
                        ed_barcodeNumber.setText(cardNo);
                        com.kippinretail.config.Config.FORMAT="";
                        com.kippinretail.config.Config.CONTENT="";

                    }
                    break;
            }
            if (requestCode == CommonUtility.REQUEST_CODE_TAKEMANUAL_INPUT)
            {
                if (data != null) {
                    String cardNo = data.getStringExtra("cardNo");
                    ed_barcodeNumber.setText(cardNo);
                }
            }
            else {
                barcodeUtil.onActivityResult(requestCode, resultCode, data, new OnBarcodeGetListener() {
                    @Override
                    public void getBarcode(int req, String s) {
                        switch (req) {
                            case CommonUtility.REQUEST_CODE_BARCODE:
                                ed_barcodeNumber.setText(s);
                                break;
                        }
                    }
                });
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (data != null) {
                if(data.getStringExtra("clickedButton")!=null) {
                    if (data.getStringExtra("clickedButton").equals("manualInput")) {
                        Intent i = new Intent();
                        i.setClass(ActivityScanPunchCard.this, ActivityINputManualBarcode.class);
                        startActivityForResult(i, CommonUtility.REQUEST_CODE_TAKEMANUAL_INPUT);
                        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_from_right);
                    }
                }
            }
        }
    } // function finish

}

