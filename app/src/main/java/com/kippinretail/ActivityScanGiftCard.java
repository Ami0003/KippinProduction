package com.kippinretail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.OnBarcodeGetListener;
import com.kippinretail.Modal.ResponseModal;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.retrofit.RestClientAdavanced;
import com.kippinretail.scanning.ScannerActivity;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityScanGiftCard extends SuperActivity {
    private final static int REQUEST_SCANNER = 1;
    LinearLayout layout_giftCardDetail;
    TextView btn_ScanGiftCardBarcode, btn_paywithGiftCard, btn_chargeGiftCard;
    EditText ed_itemCost, ed_referenceNumber, ed_barcodeNumber;
    String merchantId;
    com.kippinretail.ApplicationuUlity.BarcodeUtil barcodeUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_scan_gift_card);
        initialiseUI();
        updateToolBar();
        setUpListeners();
        ;
        setUpUI();
        ;
    }

    private void updateToolBar() {
        generateActionBar(R.string.title_user_ActivityScanGiftCard, true, true, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (com.kippinretail.config.Config.scanner) {
            android.util.Log.e("----CONTENT:----:", "" + com.kippinretail.config.Config.CONTENT);
            android.util.Log.e("---FORMAT:-------", "" + com.kippinretail.config.Config.FORMAT);

            com.kippinretail.config.Config.scanner = false;
            if (com.kippinretail.config.Config.CONTENT.equals("")) {

            } else {
                String cardNo = com.kippinretail.config.Config.CONTENT;
                String card_No = com.kippinretail.config.Config.FORMAT;
                android.util.Log.e("----cardNo----:", "" + cardNo);
                android.util.Log.e("---card_No:-------", "" + card_No);
                ed_barcodeNumber.setText(cardNo);
                com.kippinretail.config.Config.FORMAT = "";
                com.kippinretail.config.Config.CONTENT = "";
            }


        }
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        merchantId = getIntent().getStringExtra("merchantId");
        layout_giftCardDetail = (LinearLayout) findViewById(R.id.layout_giftCardDetail);
        btn_ScanGiftCardBarcode = (TextView) findViewById(R.id.btn_ScanGiftCardBarcode);
        btn_paywithGiftCard = (TextView) findViewById(R.id.btn_paywithGiftCard);
        btn_chargeGiftCard = (TextView) findViewById(R.id.btn_chargeGiftCard);
        ed_itemCost = (EditText) findViewById(R.id.ed_itemCost);
        ed_referenceNumber = (EditText) findViewById(R.id.ed_referenceNumber);
        ed_barcodeNumber = (EditText) findViewById(R.id.ed_barcodeNumber);
        barcodeUtil = new com.kippinretail.ApplicationuUlity.BarcodeUtil(this);
    }

    @Override
    public void setUpUI() {
        super.setUpUI();
    }

    @Override
    public void setUpListeners() {
        super.setUpListeners();
    }

    public void scanGiftCardBarcode(View v) {
        layout_giftCardDetail.setVisibility(View.VISIBLE);
        Intent i = new Intent();
        i.setClass(ActivityScanGiftCard.this, ScannerActivity.class);
        //startActivity(i);
        startActivityForResult(i, REQUEST_SCANNER);
       /* barcodeUtil.launchScanner(CommonUtility.REQUEST_CODE_BARCODE,new HandleManualInput(){

            @Override
            public void onManualClick() {
                Intent i = new Intent();
                i.setClass(ActivityScanGiftCard.this ,ActivityINputManualBarcode.class );
                startActivityForResult(i,CommonUtility.REQUEST_MANUAL_BARCODE);

            }
        },"Please scan your gift card");*/
    }

    public void payWithGiftCard(View v) {
        layout_giftCardDetail.setVisibility(View.VISIBLE);
    }

    public void chargeGiftCard(View v) {
        validateCard();

    }

    private void validateCard() {

        if (ed_itemCost.getText().length() != 0 && ed_referenceNumber.getText().toString().length() != 0
                && ed_barcodeNumber.getText().length() != 0) {
            RestClientAdavanced.getApiServiceForPojo(this).CheckGiftCardBarCodeExist(ed_barcodeNumber.getText().toString(), getCallback(new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    if (Boolean.parseBoolean(jsonElement.toString())) {
                        chargeGiftCard();
                    } else MessageDialog.showDialog(activity, "Invalid giftcard barcode", false);
                }

                @Override
                public void failure(RetrofitError error) {
                    MessageDialog.showFailureDialog(ActivityScanGiftCard.this);
                }
            }));
        } else {
            MessageDialog.showDialog(ActivityScanGiftCard.this, "Please fill the empty fields", false);
        }
    }


    private void chargeGiftCard() {
        HashMap<String, String> jsonBody = new HashMap<String, String>();
        jsonBody.put("Itemcost", ed_itemCost.getText().toString());
        jsonBody.put("InvoiceNumber", ed_referenceNumber.getText().toString());
        jsonBody.put("GiftcardBarcode", ed_barcodeNumber.getText().toString());
        jsonBody.put("EmployeeId", String.valueOf(CommonData.getUserData(this).getId()));
        jsonBody.put("MerchantId", merchantId);
        LoadingBox.showLoadingDialog(ActivityScanGiftCard.this, "Loading...");
        RestClient.getApiServiceForPojo().chargeGiftCard(jsonBody, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("result", jsonElement.toString());
                Gson gson = new Gson();
                ResponseModal model = gson.fromJson(jsonElement.toString(), ResponseModal.class);
                if (model.getResponseCode().equals("1")) {
                    MessageDialog.showDialog(ActivityScanGiftCard.this, model.getResponseMessage(), true);
                } else {
                    if (model.getOwnAmount() != 0) {
                        float v = model.getOwnAmount();
                        if (v == 0.0 || v == 0) {
                            MessageDialog.showDialog(ActivityScanGiftCard.this, model.getResponseMessage(), false);
                        } else {
                            showProceedDialog(ActivityScanGiftCard.this, model.getResponseMessage());
                        }
                    } else {
                        MessageDialog.showDialog(ActivityScanGiftCard.this, model.getResponseMessage(), false);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showFailureDialog(ActivityScanGiftCard.this);
            }
        });

    }

    private void charge_Gift_Card() {
        HashMap<String, String> jsonBody = new HashMap<String, String>();
        jsonBody.put("Itemcost", ed_itemCost.getText().toString());
        jsonBody.put("InvoiceNumber", ed_referenceNumber.getText().toString());
        jsonBody.put("GiftcardBarcode", ed_barcodeNumber.getText().toString());
        jsonBody.put("EmployeeId", String.valueOf(CommonData.getUserData(this).getId()));
        jsonBody.put("MerchantId", merchantId);
        jsonBody.put("ProceedToCharge", "true");
        LoadingBox.showLoadingDialog(ActivityScanGiftCard.this, "Loading...");
        RestClient.getApiServiceForPojo().chargeGiftCard(jsonBody, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("result", jsonElement.toString());
                Gson gson = new Gson();
                ResponseModal model = gson.fromJson(jsonElement.toString(), ResponseModal.class);
                if (model.getResponseCode().equals("1")) {

                    //showProceedDialog(ActivityScanGiftCard.this, model.getResponseMessage());
                    MessageDialog.showDialog(ActivityScanGiftCard.this, model.getResponseMessage(), true);
                } else {
                    /*if (model.getOwnAmount() != 0) {
                        float v = model.getOwnAmount();
                        if (v == 0.0 || v == 0) {
                            showProceedDialog(ActivityScanGiftCard.this, model.getResponseMessage());
                        }
                    } else {*/
                    MessageDialog.showDialog(ActivityScanGiftCard.this, model.getResponseMessage(), false);
                    // }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showFailureDialog(ActivityScanGiftCard.this);
            }
        });

    }

    public void showProceedDialog(Activity context, String Message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(com.kippin.kippin.R.layout.dialog_box_yes_no);
        ;
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;


        TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
        textForMessage.setText(Message);
        TextView btnYES = (TextView) dialog.findViewById(R.id.yes_btn);
        btnYES.setText("Proceed");
        TextView btnNO = (TextView) dialog.findViewById(R.id.no_btn);
        btnNO.setText("Cancel");
        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                finish();
            }
        });

        btnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                charge_Gift_Card();

            }
        });
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SCANNER:
                    android.util.Log.e("----CONTENT:----:", "" + data.getStringExtra(com.kippinretail.config.Config.CONTENT));
                    android.util.Log.e("---FORMAT:-------", "" + data.getStringExtra(com.kippinretail.config.Config.FORMAT));
                    if (data != null) {
                        String cardNo = data.getStringExtra(com.kippinretail.config.Config.CONTENT);
                        String card_No = data.getStringExtra(com.kippinretail.config.Config.FORMAT);
                        android.util.Log.e("----cardNo----:", "" + cardNo);
                        android.util.Log.e("---card_No:-------", "" + card_No);
                        ed_barcodeNumber.setText(cardNo);
                        com.kippinretail.config.Config.FORMAT = "";
                        com.kippinretail.config.Config.CONTENT = "";
                    }
                    break;
            }

        } else if (resultCode == RESULT_CANCELED) {
            if (data != null) {
                if (data.getStringExtra("clickedButton") != null) {
                    if (data.getStringExtra("clickedButton").equals("manualInput")) {
                        Intent i = new Intent();
                        i.setClass(ActivityScanGiftCard.this, ActivityINputManualBarcode.class);
                        startActivityForResult(i, CommonUtility.REQUEST_CODE_TAKEMANUAL_INPUT);
                        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_from_right);
                    }
                }
            }
        }
        if (requestCode == CommonUtility.REQUEST_CODE_TAKEMANUAL_INPUT) {

            if (data != null) {
                String cardNo = data.getStringExtra("cardNo");
                ed_barcodeNumber.setText(cardNo);
            }

        } // IF CLOSE FOR MANUAL INPUT
        else {
            barcodeUtil.onActivityResult(requestCode, resultCode, data, new OnBarcodeGetListener() {
                @Override
                public void getBarcode(int req, String s) {
                    switch (req) {
                        case CommonUtility.REQUEST_CODE_BARCODE:
                            ed_barcodeNumber.setText(s);
                            break;
                        case CommonUtility.REQUEST_CODE_LOYALITY:
                            // etLoyalityCard.setText(s);
                            break;

                    }
                }
            });
        }
    }


}
