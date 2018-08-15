package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dm.zbar.android.scanner.HandleManualInput;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.OnBarcodeGetListener;
import com.kippinretail.Modal.ModalResponse;
import com.kippinretail.retrofit.RestClientAdavanced;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityPunchCards_Scan extends SuperActivity implements View.OnClickListener {

    TextView btnScanBarcode;
    TextView btnAssignPoints;
    EditText etCustomerNumber;
    String barcode = null;
    com.kippinretail.ApplicationuUlity.BarcodeUtil barcodeUtil;
    final int REQUEST_CODE_BARCODE = 0;
    String merchantId = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_cards);
        initialiseUI();
        setUpListeners();

        merchantId = getIntent().getStringExtra("merchantId");

        generateActionBar(R.string.issue_points, true, true, false);
    }

    @Override
    public void setUpListeners() {
        super.setUpListeners();
        barcodeUtil = new com.kippinretail.ApplicationuUlity.BarcodeUtil(this, findViewById(R.id.root));
        btnScanBarcode.setOnClickListener(this);
        btnAssignPoints.setOnClickListener(this);
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        btnScanBarcode = (TextView) findViewById(R.id.btnScanPunchcard);
        btnAssignPoints = (TextView) findViewById(R.id.btnAssignPoints);
        etCustomerNumber = (EditText) findViewById(R.id.etCustomerNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScanPunchcard:
                barcodeUtil.launchScanner(REQUEST_CODE_BARCODE,new HandleManualInput(){

                    @Override
                    public void onManualClick() {
                        Intent i = new Intent();
                        i.setClass(ActivityPunchCards_Scan.this ,ActivityINputManualBarcode.class );
                        startActivityForResult(i, CommonUtility.REQUEST_MANUAL_BARCODE);

                    }
                });
                break;


            case R.id.btnAssignPoints:
          //      RestClientAdavanced.getApiServiceForPojo(this).IssuePunchCard(merchantId, etCustomerNumber.getText().toString(),"", getCallback(new Callback<JsonElement>() {
//                    @Override
//                    public void success(JsonElement jsonElement, Response response) {
//                        ModalResponse modalResponse = (new Gson().fromJson(jsonElement.toString(), ModalResponse.class));
//                        MessageDialog.showDialog(ActivityPunchCards_Scan.this, modalResponse.getResponseMessage());
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                    }
//                }));

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

//        barcodeUtil.onActivityResult(requestCode, resultCode, data, new OnBarcodeGetListener() {
//            @Override
//            public void getBarcode(int req, String s) {
//                switch (req) {
//                    case REQUEST_CODE_BARCODE:
//                        barcode = s;
//                        etCustomerNumber.setText(s);
//                        break;
//                }
//            }
//        });
    }

}
