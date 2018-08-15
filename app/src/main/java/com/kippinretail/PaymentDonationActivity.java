package com.kippinretail;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.customdatepicker.MonthYearPicker;
import com.kippinretail.KippinInvoice.DecimalFilter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.PaymentForm;
import com.kippinretail.Modal.PaymentAcknolegemoage.PaymentDetail;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by kamaljeet.singh on 10/10/2016.
 */

public class PaymentDonationActivity extends SuperActivity implements View.OnClickListener, PaymentForm {
    private static final int DATE_PICKER_ID = 1111;
    private static Calendar exp;
    Button buttonPay;
    TextView txtStripeFeeValue, txtNetDeducationValue, txtForCurrency;
    private EditText editTextCardNo, editTextExpirationDate, editTextCvv, editTextNameOnCard,editTextNameCharityPurpose;
    private Integer year, month, day;
    private String mDOB;
    private String encrypt_cardNo;
    private String encrypt_cVV;
    private EditText editTextDonationAmount;
    private String customerID;
    private String userID;
    private String Currency;
    private String StripePercentage;
    private String StripeCents;
    MonthYearPicker monthYearPicker;
    private String mExpiryMonth;
    private String mExpiryYear;
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
           /* dobEditText.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));*/
            mDOB = formatDate(year, month, day);
            Log.e("mDOB", "=" + mDOB);
            editTextExpirationDate.setText(setFormatDate(year, month, day));

        }
    };
    private String stripeFee;
    private String netDonation,netDonationPayment;


    private static String formatDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
    }

    private static String setFormatDate(int year, int month, int day) {
        exp = Calendar.getInstance();
        exp.set(year, month, day);
        Date date = exp.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        return sdf.format(date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_donation);
        initilization();
        Utils.hideKeyboard(this);
        monthYearPicker = new MonthYearPicker(this);
        monthYearPicker.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                editTextExpirationDate.setText(monthName("" + monthYearPicker.getSelectedMonthName())+ "/" + monthYearPicker.getSelectedYear());
                Log.e("Reuslt:", "" + monthYearPicker.getSelectedMonthName() + " >> " + monthYearPicker.getSelectedYear());
                //  monthname = monthName("" + monthYearPicker.getSelectedMonthName());
                mExpiryMonth=monthName("" + monthYearPicker.getSelectedMonthName());
                mExpiryYear="" + monthYearPicker.getSelectedYear();
            }
        }, null);

    }
    public String monthName(String monthname) {
        if (monthname.equals("January")) {
            monthname = "01";
            return monthname;
        } else if (monthname.equals("February")) {
            monthname = "02";
            return monthname;
        } else if (monthname.equals("March")) {
            monthname = "03";
            return monthname;
        } else if (monthname.equals("April")) {
            monthname = "04";
            return monthname;
        } else if (monthname.equals("May")) {
            monthname = "05";
            return monthname;
        } else if (monthname.equals("June")) {
            monthname = "06";
            return monthname;
        } else if (monthname.equals("July")) {
            monthname = "07";
            return monthname;
        } else if (monthname.equals("August")) {
            monthname = "08";
            return monthname;
        } else if (monthname.equals("September")) {
            monthname = "09";
            return monthname;
        } else if (monthname.equals("October")) {
            monthname = "10";
            return monthname;
        } else if (monthname.equals("November")) {
            monthname = "11";
            return monthname;
        } else if (monthname.equals("December")) {
            monthname = "12";
            return monthname;
        }
        return null;
    }
    private void initilization() {
        generateActionBar(R.string.title_payment, true, true, false);
        generateRightText("", null);
        editTextDonationAmount = (EditText) findViewById(R.id.editTextDonationAmount);
        editTextNameCharityPurpose= (EditText) findViewById(R.id.editTextNameCharityPurpose);
        editTextCardNo = (EditText) findViewById(R.id.editTextCardNo);
        editTextExpirationDate = (EditText) findViewById(R.id.editTextExpirationDate);
        editTextCvv = (EditText) findViewById(R.id.editTextCvv);
        editTextNameOnCard = (EditText) findViewById(R.id.editTextNameOnCard);
        buttonPay = (Button) findViewById(R.id.buttonPay);

        txtStripeFeeValue = (TextView) findViewById(R.id.txtStripeFeeValue);
        txtNetDeducationValue = (TextView) findViewById(R.id.txtNetDeducationValue);
        txtForCurrency = (TextView) findViewById(R.id.txtForCurrency);

        editTextExpirationDate.setFocusable(false);
        editTextExpirationDate.setClickable(true);
        editTextExpirationDate.setOnClickListener(this);
        buttonPay.setOnClickListener(this);

        Intent i = getIntent();
        if (i != null) {
            userID = i.getStringExtra(getString(R.string.user));
            gettinCountryCurrency();
        }
    }

    private void gettinCountryCurrency() {
        String userId = String.valueOf(CommonData.getUserData(this).getId());
        LoadingBox.showLoadingDialog(activity, "Loading...");
        RestClient.getApiServiceForPojo().GetCurrency(userID, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                // [{"Currency":"GBP","StripePercentage":"1.4","StripeCents":"20","ResponseCode":1,"UserId":18,"ResponseMessage":"Available."}]
                Log.e("output ==>", jsonElement.toString());
                Log.e("URL -->", response.getUrl());
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(jsonElement.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Currency = jsonObject.getString("Currency");
                            StripePercentage = jsonObject.getString("StripePercentage");
                            StripeCents = jsonObject.getString("StripeCents");
                            if(!Currency.equals("null")) {
                                txtForCurrency.setText(Currency);
                            }else{
                                txtForCurrency.setText("Currency");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(error.getUrl(), error.getMessage());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(activity, CommonUtility.TIME_OUT_MESSAGE, true);
            }
        });

        editTextDonationAmount.addTextChangedListener(new DecimalFilterOnlyTwoDigit(editTextDonationAmount, activity));
        editTextDonationAmount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Log.e("Donation amount", " == " + s.toString());
                netDonationPayment=s.toString();
                findOutStripeAndNetDeduction(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

    private void findOutStripeAndNetDeduction(String amountEnter) {
        try {
            float stripePercent = (float) ((100 - (Float.parseFloat(StripePercentage))) / 100.0);
            float StripeCent = (float) ((Float.parseFloat(StripeCents)) / 100.0);
            float netStripeFee = (Float.parseFloat(amountEnter)) + StripeCent;
            stripeFee = String.format("%.2f", (netStripeFee / stripePercent) - Float.parseFloat(amountEnter));
            netDonation = String.format("%.2f", (netStripeFee / stripePercent));
            txtStripeFeeValue.setText(stripeFee);
            txtNetDeducationValue.setText(netDonation);
        } catch (Exception e) {
            Log.e("Exception ", " = =" + e.toString());
            txtStripeFeeValue.setText("");
            txtNetDeducationValue.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPay:
//                Utils.hideKeyboard(this);
                checkCardDetail();
                break;
            case R.id.editTextExpirationDate:
                monthYearPicker.show();
               // showDialog(DATE_PICKER_ID);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    private void checkCardDetail() {

        if (getCardNumber().length() == 0 || editTextExpirationDate.getText().toString().length() == 0 || getCvv().length() == 0 || getNameOnCard().length() == 0 || editTextNameCharityPurpose.getText().length()==0) {
            showMessageDialog("Please fill mandatory field.");
            Utils.hideKeyboard(this);
        } else {
            Calendar today = Calendar.getInstance();
            today.roll(Calendar.DATE, -1);
            if (/*(exp.after(today) || exp.equals(today)) && */getCardNumber().length() == 16 && getCvv().length() == 3) {
                payBill();
            } else {
                showMessageDialog("Please enter valid credit card details");
            }
        }
    }

    @Override
    public String getCardNumber() {
        if (editTextCardNo != null) {
            return editTextCardNo.getText().toString();
        } else {
            return null;
        }
    }

    @Override
    public String getCvv() {
        if (editTextCvv != null) {
            return editTextCvv.getText().toString();
        } else {
            return null;
        }
    }

    @Override
    public Integer getExpMonth() {
        return month;
    }

    @Override
    public Integer getExpYear() {
        return year;
    }

    @Override
    public String getNameOnCard() {
        if (editTextNameOnCard != null) {
            return editTextNameOnCard.getText().toString();
        } else {
            return null;
        }
    }

    @Override
    public String getCurrency() {
        return "usd";
    }

    private void showMessageDialog(String message) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_custom_msg);
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
        textForMessage.setText(message);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDialog(String message) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_custom_msg);
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
        textForMessage.setText(message);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CommonUtility.moveToTarget(PaymentDonationActivity.this, UserDashBoardActivity.class);
            }
        });
        dialog.show();
    }

    private String Encryption(String editTextValue) {
        return CommonUtility.encryption(editTextValue, PaymentDonationActivity.this);
    }

    private void payBill() {
        /*
        ?*

        {
            "UserId":"46",
                "giftcardPurchaseId":"40",
                "amount":"7273.0",
                "MerchantId":"2",
                "CardNumber":"VyhrJjg+YkLrdR011jdyKFgMHofoMAltdltPQ59PWZs=",
                "CVV":"CwbBR/sS6My2UZCeE0C/JQ==",
                "ExpiryMonth":"7",
                "ExpiryYear":"2017"
        }*/
        customerID = String.valueOf(CommonData.getUserData(PaymentDonationActivity.this).getId());
        encrypt_cardNo = Encryption(editTextCardNo.getText().toString());
        encrypt_cVV = Encryption(editTextCvv.getText().toString());
        LoadingBox.showLoadingDialog(PaymentDonationActivity.this, "Processing ...");
        HashMap<String, String> jsonBody = new HashMap<String, String>();
        jsonBody.put("UserId", customerID);
        jsonBody.put("CardName", editTextNameOnCard.getText().toString());
        jsonBody.put("Cvv", encrypt_cVV);
        jsonBody.put("ExpiryMonth", mExpiryMonth);
        jsonBody.put("ExpiryYear", mExpiryYear);
        jsonBody.put("CardNumber", encrypt_cardNo);
        jsonBody.put("amount", netDonationPayment);
        jsonBody.put("CharityUserId", userID);
        jsonBody.put("CharityPurpose", editTextNameCharityPurpose.getText().toString());


        try {
            RestClient.getApiServiceForPojo().DonatePayment(jsonBody, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("Output -->", jsonElement.toString());
                    Log.e("url -->", response.getUrl());
                    Type listtype = new TypeToken<PaymentDetail>() {
                    }.getType();
                    Gson gson = new Gson();
                    PaymentDetail paymentDetail = (PaymentDetail) gson.fromJson(jsonElement.toString(), listtype);
                    if (paymentDetail != null) {
                        String txtMessage = paymentDetail.getResponseMessage();
                        if (paymentDetail.getResponseCode().equals("1")) {
                            showDialog("Donation Successful. Thank you for your donation");


                            //MessageDialog.showDialog(PaymentDonationActivity.this, "Donation Successful. Thank you for your donation", false, true, UserDashBoardActivity.class);
                        } else {
                            MessageDialog.showDialog(PaymentDonationActivity.this, paymentDetail.getResponseMessage(), false);
                        }
                    }
                    LoadingBox.dismissLoadingDialog();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("url -->", error.getUrl());
                    Log.e("Error Comes", error.getMessage() + "");
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showDialog(PaymentDonationActivity.this, CommonUtility.TIME_OUT_MESSAGE, true);
                }
            });
        } catch (Exception ex) {
            MessageDialog.showDialog(PaymentDonationActivity.this, "Fail to Process");
        }

    }


}
