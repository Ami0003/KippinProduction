package com.kippinretail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.OnBarcodeGetListener;
import com.kippinretail.Modal.LoyaltyRedeemPoints.LoyaltyRedeemPointResponse;
import com.kippinretail.Modal.MerchantAsEmployeeList.MerchantAsEmployeeDetail;
import com.kippinretail.Modal.ResponseModal;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.scanning.ScannerActivity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityScanLoyaltyCard extends SuperActivity {
    private final static int REQUEST_SCANNER = 1;
    //Spinner ed_selectPoints;
    EditText etCostOfProduct, etInvoiceNo;
    boolean isPayWithPointClicked;
    com.kippinretail.ApplicationuUlity.BarcodeUtil barcodeUtil;
    LinearLayout layout_dropdownlist;
    ArrayList<String> ar = null;
    private EditText ed_loayltyBarcode, ed_costOfProduct, ed_invoiceNo,
            ed_earnPointBarcode, ed_costOfProduct_retract, ed_loyaltyCardBarCode_retract;
    private String merchantId;
    private int pointReange = 996;
    private int pointDiff = 5;
    private String points = "10";
    private int cnt = 45;
    private boolean reedem_scan, earn_scan, retrac_scan;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_scan_loyalty_card);
        initialiseUI();
        setUpUI();
        setUpListeners();
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        ar = new ArrayList<String>();
        barcodeUtil = new com.kippinretail.ApplicationuUlity.BarcodeUtil(this);
        generateActionBar(R.string.title_user_ActivityScanLoyaltyCard, true, true, false);
//        ed_selectPoints = (EditText)findViewById(R.id.ed_selectPoints);
        // ed_selectPoints = (Spinner) findViewById(R.id.ed_selectPoints);
        etCostOfProduct = (EditText) findViewById(R.id.etCostOfProduct);
        etInvoiceNo = (EditText) findViewById(R.id.etInvoiceNo);
        ed_earnPointBarcode = (EditText) findViewById(R.id.ed_earnPointBarcode);
        ed_loayltyBarcode = (EditText) findViewById(R.id.ed_loayltyBarcode);
        ed_costOfProduct = (EditText) findViewById(R.id.ed_costOfProduct);
        ed_invoiceNo = (EditText) findViewById(R.id.ed_invoiceNo);
        layout_dropdownlist = (LinearLayout) findViewById(R.id.layout_dropdownlist);
        ed_costOfProduct_retract = (EditText) findViewById(R.id.ed_costOfProduct_retract);
        ed_loayltyBarcode = (EditText) findViewById(R.id.ed_loayltyBarcode);
        ed_loyaltyCardBarCode_retract = (EditText) findViewById(R.id.ed_loyaltyCardBarCode_retract);
        merchantId = getIntent().getStringExtra("merchantId");
        for (int i = 0; cnt < pointReange; i++) {

            ar.add(String.valueOf(cnt));
            cnt = cnt + pointDiff;
            if (cnt >= 1000)
                break;
        }
        ar.set(0, "Select Points");
    }

    @Override
    public void setUpUI() {
        super.setUpUI();//R.layout.spinner_item

        /*ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, ar);

        ed_selectPoints.setAdapter(monthAdapter);*/
    }

    @Override
    public void setUpListeners() {
        super.setUpListeners();
//        ed_selectPoints.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                layout_dropdownlist.setVisibility(View.VISIBLE);
//            }
//        });

       /* ed_selectPoints.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                points = ed_selectPoints.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    // REEDEM POINT
    public void payWithPoints(View v) {
        if (!isPayWithPointClicked) {
            //  ed_selectPoints.setVisibility(View.VISIBLE);
            etInvoiceNo.setVisibility(View.VISIBLE);
            etCostOfProduct.setVisibility(View.VISIBLE);
            ed_costOfProduct.setVisibility(View.GONE);
            ed_invoiceNo.setVisibility(View.GONE);
            ed_earnPointBarcode.setVisibility(View.GONE);
            ed_costOfProduct_retract.setVisibility(View.GONE);
            ed_loyaltyCardBarCode_retract.setVisibility(View.GONE);
            isPayWithPointClicked = true;
        } else {
            // ed_selectPoints.setVisibility(View.GONE);
            etInvoiceNo.setVisibility(View.GONE);
            etCostOfProduct.setVisibility(View.GONE);
            isPayWithPointClicked = false;
        }

    }

    public void scanLoyaltyCard(View v) {
        reedem_scan = true;
        Intent i = new Intent();
        i.setClass(ActivityScanLoyaltyCard.this, ScannerActivity.class);
        //startActivity(i);
        startActivityForResult(i, REQUEST_SCANNER);
        /*barcodeUtil.launchScanner(CommonUtility.REQUEST_CODE_BARCODE,new HandleManualInput(){

            @Override
            public void onManualClick() {
                Intent i = new Intent();
                i.setClass(ActivityScanLoyaltyCard.this ,ActivityINputManualBarcode.class );
                startActivityForResult(i,CommonUtility.REQUEST_MANUAL_BARCODE);

            }
        },"Please scan your loyalty card for earning or redeeming points");*/
        ed_loayltyBarcode.setVisibility(View.VISIBLE);
        ed_costOfProduct.setVisibility(View.GONE);
        ed_invoiceNo.setVisibility(View.GONE);
        ed_earnPointBarcode.setVisibility(View.GONE);
        ed_costOfProduct_retract.setVisibility(View.GONE);
        ed_loyaltyCardBarCode_retract.setVisibility(View.GONE);


    }

    public void redeemPoints(View v) {
        Log.e("merchantId:", "" + merchantId);
        // ed_loayltyBarcode.setText("3594514389");
        //
        if (etCostOfProduct.getText().toString().length() != 0 && etInvoiceNo.getText().toString().length() != 0 && ed_loayltyBarcode.getText().toString().length() != 0) {
            Intent intent = new Intent(ActivityScanLoyaltyCard.this, LoyaltyRedeemPoints.class);
            intent.putExtra("BarCode", ed_loayltyBarcode.getText().toString());
            intent.putExtra("MerchantId", "" + merchantId);
            intent.putExtra("InvoiceNumber", "" + etInvoiceNo.getText().toString());
            intent.putExtra("CostProduct", "" + etCostOfProduct.getText().toString());
            startActivity(intent);

            // Reedem_Points_data(ed_loayltyBarcode.getText().toString(), merchantId);
            // ReedemPoints();
        } else {
            MessageDialog.showDialog(this, "Please select point", false);
        }
    }

    // EARN POINT
    public void earnPoints(View v) {

        ed_costOfProduct.setVisibility(View.VISIBLE);
        ed_invoiceNo.setVisibility(View.VISIBLE);
        // ed_selectPoints.setVisibility(View.GONE);
        etInvoiceNo.setVisibility(View.GONE);
        etCostOfProduct.setVisibility(View.GONE);
        ed_loayltyBarcode.setVisibility(View.GONE);
        ed_costOfProduct_retract.setVisibility(View.GONE);
        ed_loyaltyCardBarCode_retract.setVisibility(View.GONE);

    }

    public void scanLoyaltyCard1(View v) {
        earn_scan = true;
        Intent i = new Intent();
        i.setClass(ActivityScanLoyaltyCard.this, ScannerActivity.class);
        //startActivity(i);
        startActivityForResult(i, REQUEST_SCANNER);
      /*  barcodeUtil.launchScanner(CommonUtility.REQUEST_CODE_BARCODE, new HandleManualInput() {

            @Override
            public void onManualClick() {
                Intent i = new Intent();
                i.setClass(ActivityScanLoyaltyCard.this, ActivityINputManualBarcode.class);
                startActivityForResult(i, CommonUtility.REQUEST_MANUAL_BARCODE);

            }
        },"Please scan your loyalty card for earning or redeeming points");*/
        ed_earnPointBarcode.setVisibility(View.VISIBLE);
        //  ed_selectPoints.setVisibility(View.GONE);
        etInvoiceNo.setVisibility(View.GONE);
        etCostOfProduct.setVisibility(View.GONE);
        ed_loayltyBarcode.setVisibility(View.GONE);
        ed_costOfProduct_retract.setVisibility(View.GONE);
        ed_loyaltyCardBarCode_retract.setVisibility(View.GONE);
    }

    public void assignPoints(View v) {
        if (ed_costOfProduct.getText().toString().length() != 0 && ed_invoiceNo.getText().toString().length() != 0 &&
                ed_earnPointBarcode.getText().toString().length() != 0) {
            AssignPoints();
        } else {
            MessageDialog.showDialog(this, "Please fill the empty fields", false);
        }
    }


    // RETRACT POINT
    public void scanLoyaltyCard_ReedemPoint(View v) {
        retrac_scan = true;

        Intent i = new Intent();
        i.setClass(ActivityScanLoyaltyCard.this, ScannerActivity.class);
        //startActivity(i);
        startActivityForResult(i, REQUEST_SCANNER);

        /*barcodeUtil.launchScanner(CommonUtility.REQUEST_CODE_BARCODE,new HandleManualInput(){

            @Override
            public void onManualClick() {
                Intent i = new Intent();
                i.setClass(ActivityScanLoyaltyCard.this ,ActivityINputManualBarcode.class );
                startActivityForResult(i,CommonUtility.REQUEST_MANUAL_BARCODE);

            }
        },"Please scan your loyalty card for earning or redeeming points");*/
        ed_costOfProduct_retract.setVisibility(View.VISIBLE);
        ed_loyaltyCardBarCode_retract.setVisibility(View.VISIBLE);
        //ed_selectPoints.setVisibility(View.GONE);
        etInvoiceNo.setVisibility(View.GONE);
        etCostOfProduct.setVisibility(View.GONE);
        ed_loayltyBarcode.setVisibility(View.GONE);
        ed_costOfProduct.setVisibility(View.GONE);
        ed_invoiceNo.setVisibility(View.GONE);
        ed_earnPointBarcode.setVisibility(View.GONE);

    }

    public void retractPoints(View v) {
        if (ed_costOfProduct_retract.getText().toString().length() != 0 && ed_loyaltyCardBarCode_retract.getText().toString().length() != 0) {
            RetractPoints();
        } else {
            MessageDialog.showDialog(this, "Please fill cost", false);
        }
    }
//RedeemLoyaltyCard

    public void Reedem_Points_data(String loyaltyCardNo, String MerchantId) {
        HashMap<String, String> jsonBody = new HashMap<String, String>();
        jsonBody.put("Points", points);
        jsonBody.put("LoyalityBarCode", ed_loayltyBarcode.getText().toString());
        jsonBody.put("Itemcost", etCostOfProduct.getText().toString());
        jsonBody.put("InvoiceNumber", etInvoiceNo.getText().toString());
        jsonBody.put("MerchantId", merchantId);
        LoadingBox.showLoadingDialog(this, "Loading...");
        RestClient.getApiServiceForPojo().RedeemLoyaltyCard(loyaltyCardNo, MerchantId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Type listType = new TypeToken<List<LoyaltyRedeemPointResponse>>() {
                }.getType();
                Gson gson = new Gson();
                LoyaltyRedeemPointResponse loyaltyRedeemPointResponse = gson.fromJson(jsonElement.toString(), LoyaltyRedeemPointResponse.class);
                if (loyaltyRedeemPointResponse.getResponseCode() == 1) {
                    ReedemDialog(ActivityScanLoyaltyCard.this, "" + loyaltyRedeemPointResponse.getUserPoints(), "" + loyaltyRedeemPointResponse.getMaxAmountUse(), "");
                } else {
                    MessageDialog.showDialog(ActivityScanLoyaltyCard.this, loyaltyRedeemPointResponse.getResponseMessage(), false);
                }
                resetView();
                Log.e("", jsonElement.toString());


                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                resetView();
                MessageDialog.showDialog(ActivityScanLoyaltyCard.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }

    public void ReedemPoints() {
        HashMap<String, String> jsonBody = new HashMap<String, String>();
        jsonBody.put("Points", points);
        jsonBody.put("LoyalityBarCode", ed_loayltyBarcode.getText().toString());
        jsonBody.put("Itemcost", etCostOfProduct.getText().toString());
        jsonBody.put("InvoiceNumber", etInvoiceNo.getText().toString());
        jsonBody.put("MerchantId", merchantId);
        LoadingBox.showLoadingDialog(this, "Loading...");
        RestClient.getApiServiceForPojo().RedeemPointsByLoyaltyCard(jsonBody, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Type listType = new TypeToken<List<MerchantAsEmployeeDetail>>() {
                }.getType();
                Gson gson = new Gson();
                ResponseModal serverResponse = gson.fromJson(jsonElement.toString(), ResponseModal.class);
                if (serverResponse.getResponseCode().equals("1")) {
                    if (serverResponse.getResponseMessage().equals("Success.")) {
                        MessageDialog.showDialog(ActivityScanLoyaltyCard.this, "Points successfully redeemed from your loyalty card", false);
                    } else {
                        MessageDialog.showDialog(ActivityScanLoyaltyCard.this, serverResponse.getResponseMessage(), false);
                    }
                } else {
                    MessageDialog.showDialog(ActivityScanLoyaltyCard.this, serverResponse.getResponseMessage(), false);
                }
                resetView();
                Log.e("", jsonElement.toString());


                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                resetView();
                MessageDialog.showDialog(ActivityScanLoyaltyCard.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }

    public void AssignPoints() {
        try {
            HashMap<String, String> jsonBody = new HashMap<String, String>();
            jsonBody.put("Itemcost", ed_costOfProduct.getText().toString());
            jsonBody.put("InvoiceNumber", ed_invoiceNo.getText().toString());
            jsonBody.put("LoyalityBarCode", ed_earnPointBarcode.getText().toString());
            jsonBody.put("MerchantId", merchantId);
            jsonBody.put("EmpId", String.valueOf(CommonData.getUserData(this).getId()));
            LoadingBox.showLoadingDialog(this, "Loading...");
            RestClient.getApiServiceForPojo().PayByPoints(jsonBody, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Type listType = new TypeToken<List<MerchantAsEmployeeDetail>>() {
                    }.getType();
                    Gson gson = new Gson();
                    Log.e("", jsonElement.toString());
                    ResponseModal serverResponse = gson.fromJson(jsonElement.toString(), ResponseModal.class);
                    if (serverResponse.getResponseCode().equals("1")) {
                        MessageDialog.showDialog(ActivityScanLoyaltyCard.this, "Points successfully assigned to your loyalty card", true);
                    } else {
                        MessageDialog.showDialog(ActivityScanLoyaltyCard.this, serverResponse.getResponseMessage(), false);
                    }
                    resetView();
                    LoadingBox.dismissLoadingDialog();
                }

                @Override
                public void failure(RetrofitError error) {
                    resetView();
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showFailureDialog(ActivityScanLoyaltyCard.this);
                }
            });
        } catch (Exception ex) {

        }
    }

    public void RetractPoints() {
        try {
            HashMap<String, String> jsonBody = new HashMap<String, String>();
            jsonBody.put("Itemcost", ed_costOfProduct_retract.getText().toString());
            jsonBody.put("LoyalityBarCode", ed_loyaltyCardBarCode_retract.getText().toString());
            jsonBody.put("MerchantId", merchantId);
            LoadingBox.showLoadingDialog(this, "Loading...");
            RestClient.getApiServiceForPojo().retractPOint(jsonBody, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Type listType = new TypeToken<List<MerchantAsEmployeeDetail>>() {
                    }.getType();
                    Gson gson = new Gson();
                    Log.e("", jsonElement.toString());

                    ResponseModal serverResponse = gson.fromJson(jsonElement.toString(), ResponseModal.class);
                    if (serverResponse.getResponseCode().equals("1")) {
                        MessageDialog.showDialog(ActivityScanLoyaltyCard.this, "Points successfully retract from your loyalty card ", true);

                    } else {
                        MessageDialog.showDialog(ActivityScanLoyaltyCard.this, serverResponse.getResponseMessage(), false);
                    }
                    resetView();
                    LoadingBox.dismissLoadingDialog();
                }

                @Override
                public void failure(RetrofitError error) {
                    resetView();
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showFailureDialog(ActivityScanLoyaltyCard.this);
                }
            });
        } catch (Exception ex) {

        }
    }

    public void ReedemDialog(Context activity, String customPoint, String Pointtodollar, String balancePaid) {

        try {

            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_loyalty_scan_card);

            Button btnNo = (Button) dialog.findViewById(R.id.btnNO);
            Button btnYES = (Button) dialog.findViewById(R.id.btnYES);
            TextView textView_CustomerPoints = (TextView) dialog.findViewById(R.id.textView_CustomerPoints);
            TextView textView_PointstoDollar = (TextView) dialog.findViewById(R.id.textView_PointstoDollar);
            TextView textView_BalancePaid = (TextView) dialog.findViewById(R.id.textView_BalancePaid);
            EditText edittext_AmountCustomer = (EditText) dialog.findViewById(R.id.edittext_AmountCustomer);

            textView_CustomerPoints.setText(customPoint);
            textView_PointstoDollar.setText(Pointtodollar);
            textView_BalancePaid.setText(balancePaid);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();


                }

            });

            btnYES.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();


                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (com.kippinretail.config.Config.screenShow) {
            resetView();
            com.kippinretail.config.Config.screenShow = false;
        }
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
                if (reedem_scan) {
                    ed_loayltyBarcode.setText(cardNo);
                    reedem_scan = false;
                } else if (earn_scan) {
                    ed_earnPointBarcode.setText(cardNo);
                    earn_scan = false;
                } else if (retrac_scan) {
                    ed_loyaltyCardBarCode_retract.setText(cardNo);
                    retrac_scan = false;
                }
                com.kippinretail.config.Config.FORMAT = "";
                com.kippinretail.config.Config.CONTENT = "";
            }


        }

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
                        if (reedem_scan) {
                            ed_loayltyBarcode.setText(cardNo);
                            reedem_scan = false;
                        } else if (earn_scan) {
                            ed_earnPointBarcode.setText(cardNo);
                            earn_scan = false;
                        } else if (retrac_scan) {
                            ed_loyaltyCardBarCode_retract.setText(cardNo);
                            retrac_scan = false;
                        }
                        com.kippinretail.config.Config.FORMAT = "";
                        com.kippinretail.config.Config.CONTENT = "";
                    }
                    break;
            }
            if (requestCode == CommonUtility.REQUEST_CODE_TAKEMANUAL_INPUT) {
                if (data != null) {
                    String cardNo = data.getStringExtra("cardNo");
                    if (reedem_scan) {
                        ed_loayltyBarcode.setText(cardNo);
                        reedem_scan = false;
                    } else if (earn_scan) {
                        ed_earnPointBarcode.setText(cardNo);
                        earn_scan = false;
                    } else if (retrac_scan) {
                        ed_loyaltyCardBarCode_retract.setText(cardNo);
                        retrac_scan = false;
                    }
                }
            } else {
                barcodeUtil.onActivityResult(requestCode, resultCode, data, new OnBarcodeGetListener() {
                    @Override
                    public void getBarcode(int req, String s) {
                        switch (req) {
                            case CommonUtility.REQUEST_CODE_BARCODE:
                                if (reedem_scan) {
                                    ed_loayltyBarcode.setText(s);
                                    reedem_scan = false;
                                } else if (earn_scan) {
                                    ed_earnPointBarcode.setText(s);
                                    earn_scan = false;
                                } else if (retrac_scan) {
                                    ed_loyaltyCardBarCode_retract.setText(s);
                                    retrac_scan = false;
                                }
                                break;
                            case CommonUtility.REQUEST_CODE_LOYALITY:
                                // etLoyalityCard.setText(s);
                                break;

                        }
                    }
                });
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (data != null) {
                if (data.getStringExtra("clickedButton") != null) {
                    if (data.getStringExtra("clickedButton").equals("manualInput")) {
                        Intent i = new Intent();
                        i.setClass(ActivityScanLoyaltyCard.this, ActivityINputManualBarcode.class);
                        startActivityForResult(i, CommonUtility.REQUEST_CODE_TAKEMANUAL_INPUT);
                        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_from_right);
                    }
                }
            }

        }
    } // FUNCTION FINISH


    public void resetView() {

        ed_loayltyBarcode.setText("");
        ed_costOfProduct.setText("");
        ed_invoiceNo.setText("");
        ed_earnPointBarcode.setText("");
        ed_costOfProduct_retract.setText("");
        ed_loyaltyCardBarCode_retract.setText("");
        etInvoiceNo.setText("");
        etCostOfProduct.setText("");
        //ed_selectPoints.setVisibility(View.GONE);
        etInvoiceNo.setVisibility(View.GONE);
        etCostOfProduct.setVisibility(View.GONE);
        ed_loayltyBarcode.setVisibility(View.GONE);
        ed_costOfProduct.setVisibility(View.GONE);
        ed_invoiceNo.setVisibility(View.GONE);
        ed_earnPointBarcode.setVisibility(View.GONE);
        ed_costOfProduct_retract.setVisibility(View.GONE);
        ed_loyaltyCardBarCode_retract.setVisibility(View.GONE);
    }
}
