package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.zbar.android.scanner.HandleManualInput;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kippinretail.ApplicationuUlity.*;
import com.kippinretail.ApplicationuUlity.BarcodeUtil;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.ModalResponse;
import com.kippinretail.Modal.webclient.model.ArrayListPost;
import com.kippinretail.retrofit.RestClientAdavanced;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class Employee_ScanItem extends SuperActivity implements View.OnClickListener {

    EditText etItemCost;
    EditText etReferenceNumber,etLoyalityCard;
    TextView btnScanBarcode;
    TextView btnScanLoyalityCard;
    TextView btnAssignPoints,btnPayByPoints;
    Spinner spPoints;
    EditText etCustomerNumber;
    String barcode = null;
    String loyality_barcode = null;
    ArrayList<Integer> integers = new ArrayList<>();
    BarcodeUtil barcodeUtil;
    final int REQUEST_CODE_BARCODE = 0;
    final int REQUEST_CODE_LOYALITY = 1;
    ArrayAdapter arrayAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_scan_item);
        initialiseUI();
        setUpListeners();
        addCounter();
        generateActionBar(R.string.issue_points , true,true,false);
    }

    private void addCounter() {
        for (int i = 0; i <= 990; i++) {
            integers.add(i);
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, integers);
        spPoints.setAdapter(arrayAdapter);
    }


    @Override
    public void setUpListeners() {
        super.setUpListeners();
        barcodeUtil = new BarcodeUtil(this);
        btnScanBarcode.setOnClickListener(this);
        btnScanLoyalityCard.setOnClickListener(this);
        btnAssignPoints.setOnClickListener(this);
        btnPayByPoints.setOnClickListener(this);
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        spPoints = (Spinner) findViewById(R.id.spPoints);
        btnScanBarcode = (TextView) findViewById(R.id.btnScanBarcode);
        btnPayByPoints = (TextView) findViewById(R.id.btnPayByPoints);
        btnScanLoyalityCard = (TextView) findViewById(R.id.btnScanLoyalityCard);
        btnAssignPoints = (TextView) findViewById(R.id.btnAssignPoints);
        etItemCost = (EditText) findViewById(R.id.etItemCost);
        etReferenceNumber= (EditText) findViewById(R.id.etReferenceNumber);
        etCustomerNumber= (EditText) findViewById(R.id.etCustomerNumber);
        etLoyalityCard= (EditText) findViewById(R.id.etLoyalityCardBarcode);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScanBarcode:
                barcodeUtil.launchScanner(REQUEST_CODE_BARCODE,new HandleManualInput(){

                    @Override
                    public void onManualClick() {
                        Intent i = new Intent();
                        i.setClass(Employee_ScanItem.this ,ActivityINputManualBarcode.class );
                        startActivityForResult(i,CommonUtility.REQUEST_MANUAL_BARCODE);

                    }
                });
                break;

            case R.id.btnScanLoyalityCard:
                barcodeUtil.launchScanner(REQUEST_CODE_LOYALITY,new HandleManualInput(){

                    @Override
                    public void onManualClick() {
                        Intent i = new Intent();
                        i.setClass(Employee_ScanItem.this ,ActivityINputManualBarcode.class );
                        startActivityForResult(i,CommonUtility.REQUEST_MANUAL_BARCODE);

                    }
                });
                break;

            case R.id.btnPayByPoints:
                spPoints.setVisibility(View.VISIBLE);
                break;

            case R.id.btnAssignPoints:
                validateCard();
                break;


        }
    }


    private void assignPoints(){

        int a = 0;
        if(etItemCost.getText().length()!=0 && etReferenceNumber.getText().toString().length()!=0
                && etCustomerNumber.getText().length()!=0) {
            ArrayListPost templatePosts = new ArrayListPost();

            templatePosts.add("Barcode", barcode);
            templatePosts.add("EmployeeId", CommonData.getUserData(this).getId() + "");
            templatePosts.add("InvoiceNumber", etReferenceNumber.getText().toString());
            templatePosts.add("IsReedemPoint", ((loyality_barcode == null || loyality_barcode.length() == 0) ? false : true) + "");
            templatePosts.add("LoyalityBarCode", loyality_barcode == null ? "" : loyality_barcode);
            templatePosts.add("ReedemPoint", spPoints.getSelectedItemPosition() + "");
            templatePosts.add("Itemcost", (Integer.parseInt(etItemCost.getText().toString())) + "");


            TypedInput in = null;
            try {
                in = new TypedByteArray("application/json", templatePosts.getJson().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            RestClientAdavanced.getApiServiceForPojo(this).ManagePoint(in, getCallback(new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    ModalResponse modalResponse = (new Gson().fromJson(jsonElement.toString(), ModalResponse.class));
                    MessageDialog.showDialog(Employee_ScanItem.this, modalResponse.getResponseMessage());
                }

                @Override
                public void failure(RetrofitError error) {
                }
            }));
        }else{
            MessageDialog.showDialog(Employee_ScanItem.this,"Please Fill All the Field",false);
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
//                    case REQUEST_CODE_LOYALITY:
//                        loyality_barcode= s;
//                        etLoyalityCard.setText(s);
//                        break;
//
//                }
//            }
//        });
    }

    private void validateCard() {

        if(etItemCost.getText().length()!=0 && etReferenceNumber.getText().toString().length()!=0
                && etCustomerNumber.getText().length()!=0) {
            RestClientAdavanced.getApiServiceForPojo(this).CheckGiftCardBarCodeExist(etCustomerNumber.getText().toString(), getCallback(new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    if (Boolean.parseBoolean(jsonElement.toString())) {
                        assignPoints();
                    } else MessageDialog.showDialog(activity, "Invalid Gift Card");
                }

                @Override
                public void failure(RetrofitError error) {
                }
            }));
        }else{
            MessageDialog.showDialog(Employee_ScanItem.this,"Please Fill All the Field",false);
        }
    }

}

