package com.kippinretail;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.LoyaltyRedeemPoints.LoyaltyRedeemPointResponse;
import com.kippinretail.Modal.MerchantAsEmployeeList.MerchantAsEmployeeDetail;
import com.kippinretail.Modal.ResponseModal;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by agnihotri on 19/01/18.
 */

public class LoyaltyRedeemPoints extends SuperActivity {
    TextView textViewPoints, textViewBarcode, textViewCustomerPoints, textViewPointstoDollar, textViewBalancePaid,
            textView_invoicenumber, textView_Cost;
    EditText editTextPoints;
    String barCodeString;
    String merchantIdString, costProduct, invoiceNumber;
    int value = 0;

    int costValue = 0;

    int finalValue = 0;
    Button btnYES, btnNO;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loyalty_scan_card);
        initialiseUI();
        //setUpUI();
        //setUpListeners();
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        generateActionBar(R.string.title_user_ActivityScanLoyaltyCard, true, true, false);
        // textViewPoints = (TextView) findViewById(R.id.textView_SelectedPoint);
        textViewBarcode = (TextView) findViewById(R.id.textView_barcode);
        textViewCustomerPoints = (TextView) findViewById(R.id.textView_CustomerPoints);
        textViewPointstoDollar = (TextView) findViewById(R.id.textView_PointstoDollar);
        textViewBalancePaid = (TextView) findViewById(R.id.textView_BalancePaid);
        textView_Cost = (TextView) findViewById(R.id.textView_Cost);
        textView_invoicenumber = (TextView) findViewById(R.id.textView_invoicenumber);
        editTextPoints = (EditText) findViewById(R.id.edittext_AmountCustomer);
        btnNO = (Button) findViewById(R.id.btnNO);
        btnYES = (Button) findViewById(R.id.btnYES);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            barCodeString = bundle.getString("BarCode");
            merchantIdString = bundle.getString("MerchantId");
            costProduct = bundle.getString("CostProduct");
            invoiceNumber = bundle.getString("InvoiceNumber");
            textView_invoicenumber.setText(invoiceNumber);
            textView_Cost.setText(costProduct);
            Reedem_Points_data(barCodeString, merchantIdString);
        }

        editTextPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != 0) {
                    value = getInt(s.toString());
                    costValue = getInt(costProduct);
                    finalValue = costValue - value;
                    Log.e("finalValue:", "" + finalValue);
                    if (finalValue == 0) {
                        // editTextPoints.setText("" + finalValue);
                        textViewBalancePaid.setText("" + finalValue);
                        Log.e("ZERO", "ZERO");
                        System.out.println("Number is equal to zero");
                    } else if (finalValue > 0) {
                        //editTextPoints.setText("" + finalValue);
                        textViewBalancePaid.setText("" + finalValue);
                        Log.e("positive", "positive");
                        System.out.println("Number is positive");
                    } else {
                        editTextPoints.setText("");
                        textViewBalancePaid.setText("" + costProduct);
                        Log.e("negative", "negative");
                        System.out.println("Number is negative");
                    }
                    finalValue = 0;
                    value = 0;
                    costValue = 0;
                } else {
                    finalValue = 0;
                    value = 0;
                    costValue = 0;
                    textViewBalancePaid.setText("" + costProduct);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        btnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextPoints.getText().length() != 0) {
                    if (textViewPointstoDollar.getText().toString().equals("0") ||
                            textViewPointstoDollar.getText().equals("0.0") ||
                            textViewPointstoDollar.getText().equals("0.000") ||
                            textViewPointstoDollar.getText().equals("0.00") ||
                            textViewPointstoDollar.getText().equals("0.0000")) {
                        MessageDialog.showDialog(LoyaltyRedeemPoints.this, "Insufficient Points", false);

                    } else {
                        ReedemPoints();
                    }

                }
            }
        });
        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int getInt(String editText) throws NumberFormatException {
        return Integer.valueOf(editText);
    }

    public void Reedem_Points_data(String loyaltyCardNo, String MerchantId) {

        LoadingBox.showLoadingDialog(this, "Loading...");
        RestClient.getApiServiceForPojo().RedeemLoyaltyCard(loyaltyCardNo, MerchantId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Type listType = new TypeToken<List<LoyaltyRedeemPointResponse>>() {
                }.getType();
                Gson gson = new Gson();
                LoyaltyRedeemPointResponse loyaltyRedeemPointResponse = gson.fromJson(jsonElement.toString(), LoyaltyRedeemPointResponse.class);
                if (loyaltyRedeemPointResponse.getResponseCode() == 1) {
                    textViewBarcode.setText(barCodeString);
                    textViewCustomerPoints.setText("" + loyaltyRedeemPointResponse.getUserPoints());
                    textViewPointstoDollar.setText("" + loyaltyRedeemPointResponse.getMaxAmountUse());
                    textViewBalancePaid.setText("" + textView_Cost.getText().toString());
                    editTextPoints.setFilters(new InputFilter[]{new InputFilterMinMax("1" /*+ loyaltyRedeemPointResponse.getDollarAmount()*/, "" + loyaltyRedeemPointResponse.getMaxAmountUse())});

//
                    //ReedemDialog(LoyaltyRedeemPoints.this, "" + loyaltyRedeemPointResponse.getUserPoints(), "" + loyaltyRedeemPointResponse.getMaxAmountUse(), "");
                } else {
                    MessageDialog.showDialog(LoyaltyRedeemPoints.this, loyaltyRedeemPointResponse.getResponseMessage(), false);
                }
                //resetView();
                Log.e("", jsonElement.toString());


                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                //resetView();
                MessageDialog.showDialog(LoyaltyRedeemPoints.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }

    public void ReedemPoints() {
        Log.e("EmpId:", "" + String.valueOf(CommonData.getUserData(this).getId()));
        HashMap<String, String> jsonBody = new HashMap<String, String>();
        jsonBody.put("ReedemAmount", editTextPoints.getText().toString());
        jsonBody.put("LoyalityBarCode", barCodeString);
        jsonBody.put("Itemcost", /*etCostOfProduct.getText().toString()*/costProduct);
        jsonBody.put("InvoiceNumber", /*etInvoiceNo.getText().toString()*/invoiceNumber);
        jsonBody.put("MerchantId", merchantIdString);
        jsonBody.put("EmpId", String.valueOf(CommonData.getUserData(this).getId()));
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
                        com.kippinretail.config.Config.screenShow = true;

                        MessageDialog.showDialog(LoyaltyRedeemPoints.this, "Points successfully redeemed from your loyalty card", true);
                    } else {
                        MessageDialog.showDialog(LoyaltyRedeemPoints.this, serverResponse.getResponseMessage(), false);
                    }
                } else {
                    MessageDialog.showDialog(LoyaltyRedeemPoints.this, serverResponse.getResponseMessage(), false);
                }
                //resetView();
                Log.e("", jsonElement.toString());


                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                //resetView();
                MessageDialog.showDialog(LoyaltyRedeemPoints.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }

}
