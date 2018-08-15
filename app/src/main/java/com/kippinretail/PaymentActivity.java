package com.kippinretail;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.customdatepicker.MonthYearPicker;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.DbNew;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.PaymentForm;
import com.kippinretail.Modal.GiftCardList.GiftCard;
import com.kippinretail.Modal.PaymentAcknolegemoage.PaymentDetail;
import com.kippinretail.Modal.ServerResponseForCurrency;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PaymentActivity extends SuperActivity implements View.OnClickListener , PaymentForm{

    private static Calendar exp;
    private EditText editTextCardNo,editTextExpirationDate,editTextCvv,editTextNameOnCard;
    private static final int DATE_PICKER_ID = 1111;
    private Integer year,month,day;
    private String mDOB;
    private Button buttonPay;
    public static final String PUBLISHABLE_KEY = "pk_test_oMsVzjtEjIK36u2UFzwh8KkM";
    private String merchantID ,customerID, amount,stripetoken,giftcardPurchaseId;
    double stripeCharges,kippinCharges, totalAmount;
    String currencySymbol;
    TextView txt_giftCardAmount,txt_kippinFee,txt_stripeFee,txt_totalAmount;
    double price = 0.0;
    private String encrypt_cardNo , encrypt_cVV;
    String giftId;
    MonthYearPicker monthYearPicker;
    private String mExpiryMonth;
    private String mExpiryYear;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
       initilization();
        updateUI();
        Utils.hideKeyboard(this);
        monthYearPicker = new MonthYearPicker(this);
        monthYearPicker.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                editTextExpirationDate.setText(monthName("" + monthYearPicker.getSelectedMonthName())+ "/" + monthYearPicker.getSelectedYear());
                android.util.Log.e("Reuslt:", "" + monthYearPicker.getSelectedMonthName() + " >> " + monthYearPicker.getSelectedYear());
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

    private void initilization()
    {   generateActionBar(R.string.title_payment,true,true,false);
        generateRightText("",null);
        editTextCardNo = (EditText)findViewById(R.id.editTextCardNo);
        editTextExpirationDate = (EditText)findViewById(R.id.editTextExpirationDate);
        editTextCvv = (EditText)findViewById(R.id.editTextCvv);
        editTextNameOnCard = (EditText)findViewById(R.id.editTextNameOnCard);
        buttonPay = (Button)findViewById(R.id.buttonPay);

        txt_giftCardAmount = (TextView)findViewById(R.id.txt_giftCardAmount);
        txt_kippinFee = (TextView)findViewById(R.id.txt_kippinFee);
        txt_stripeFee = (TextView)findViewById(R.id.txt_stripeFee);
        txt_totalAmount = (TextView)findViewById(R.id.txt_totalAmount);
        editTextExpirationDate.setFocusable(false);
        editTextExpirationDate.setClickable(true);
        editTextExpirationDate.setOnClickListener(this);
        buttonPay.setOnClickListener(this);


        customerID = String.valueOf(CommonData.getUserData(PaymentActivity.this).getId());

        Intent i = getIntent();
        if(i!=null){

            amount = i.getStringExtra("amount");
            giftId = i.getStringExtra("giftId");
            giftcardPurchaseId = i.getStringExtra("giftcardPurchaseId");
            if(amount!=null){
                price= Double.parseDouble(amount);

            }

            int temp = (int)price;
            amount = String.valueOf(temp);
            giftcardPurchaseId = i.getStringExtra("giftcardPurchaseId");
            merchantID = i.getStringExtra("merchantID");
        }
    }
    private void updateUI() {
        try {
            RestClient.getApiServiceForPojo().GetMerchantCurrencyDetail(merchantID, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("Output==== -->", jsonElement.toString());
                    Log.e("url -->", response.getUrl());
                    Type listtype = new TypeToken<ServerResponseForCurrency>() {
                    }.getType();
                    Gson gson = new Gson();
                    ServerResponseForCurrency currencyDetail = (ServerResponseForCurrency) gson.fromJson(jsonElement.toString(), listtype);
                    if(currencyDetail!=null){
                        currencySymbol = currencyDetail.getSymbol();
                        stripeCharges = Double.parseDouble(currencyDetail.getStripeCharge());
                        kippinCharges = Double.parseDouble(currencyDetail.getKippinCharge());
                        txt_giftCardAmount.setText("Gift Card Ammount : "+currencySymbol+amount );
                        double sfee = stripeCharges*price;
                        txt_stripeFee.setText("Processing fees: "+currencySymbol+String.valueOf(CommonUtility.round(sfee,2)));
                        txt_kippinFee.setText("KIPPIN FEE for custom card: "+currencySymbol+String.valueOf(CommonUtility.round(kippinCharges*price,2)));
                        totalAmount = stripeCharges*price+kippinCharges*price+price;
                        txt_totalAmount.setText("Total: "+currencySymbol+String.valueOf(CommonUtility.round(totalAmount,2)));
                    }
                    LoadingBox.dismissLoadingDialog();
//
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("url -->", error.getUrl()+"");
                    Log.e("Error Comes", error.getMessage() + "");
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showDialog(PaymentActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
                }
            });
        } catch (Exception ex) {
            MessageDialog.showDialog(PaymentActivity.this, "Fail to Process");
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

    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
       // cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        return sdf.format(date);
    }
    private static String setFormatDate(int year, int month, int day) {

        exp = Calendar.getInstance();
    //    exp.setTimeInMillis(0);
        exp.set(year, month, day);
        Date date = exp.getTime();
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        return sdf.format(date);
    }
    private void checkCardDetail()
    {

        if(getCardNumber().length()==0 || editTextExpirationDate.getText().toString().length()==0 || getCvv().length()==0 || getNameOnCard().length()==0)
        {



            showMessageDialog("Please fill mandatory field.");

            Utils.hideKeyboard(this);
        }
        else
        {   Calendar today = Calendar.getInstance();
            today.roll(Calendar.DATE , -1);
            if(/*(exp.after(today) || exp.equals(today)) && */getCardNumber().length()==16 && getCvv().length()==3){
                payBill();

            }else{
                showMessageDialog("Please enter valid credit card details");

            }

        }
    }

  /*  private void getTokenNumber(PaymentForm form)
    {
       LoadingBox.showLoadingDialog(PaymentActivity.this,"Procssing");
        Card card = new Card(form.getCardNumber(),
                                form.getExpMonth(),
                                form.getExpYear(),
                                form.getCvv());
        card.setCurrency(form.getCurrency());

        if(card.validateCard())
        {
            Log.e("validateCard()", "validateCard()");
            try{

                Stripe stripe = new Stripe(PUBLISHABLE_KEY);
                stripe.createToken(card, PUBLISHABLE_KEY, new TokenCallback() {
                    @Override
                    public void onError(Exception error) {
                        Log.e("error",error.getMessage());
                    }

                    @Override
                    public void onSuccess(Token token) {
                        stripetoken = token.getId();
                        Log.e("Token -->",token.getId());
                        payBill();

                    }
                });
            }catch(AuthenticationException ex)
            {
                Log.e("Authenticati -->",ex.getMessage());
            }
        }
        else if (!card.validateNumber()) {
            LoadingBox.dismissLoadingDialog();
            showMessageDialog("Invalid Card Number");


        } else if (!card.validateExpiryDate()) {
            LoadingBox.dismissLoadingDialog();
            showMessageDialog("Invalid Expiry Date");


        } else if (!card.validateCVC()) {
            LoadingBox.dismissLoadingDialog();
            showMessageDialog("Invalid Cvv Number");

        }

        Utils.hideKeyboard(this);
    }*/
    private  void payBill()
    {
        /*
        ?*

        {
            "UserId":"46",
                "giftcardPurchaseId":"60",
                "amount":"7273.0",
                "MerchantId":"2",
                "CardNumber":"VyhrJjg+YkLrdR011jdyKFgMHofoMAltdltPQ59PWZs=",
                "CVV":"CwbBR/sS6My2UZCeE0C/JQ==",
                "ExpiryMonth":"7",
                "ExpiryYear":"2017"
        }*/
        encrypt_cardNo = Encryption(editTextCardNo.getText().toString());
        encrypt_cVV = Encryption(editTextCvv.getText().toString());
        LoadingBox.showLoadingDialog(PaymentActivity.this,"Processing ...");
        HashMap<String,String> jsonBody = new HashMap<String,String>();
        jsonBody.put("UserId",customerID);
        jsonBody.put("giftcardPurchaseId",giftcardPurchaseId);
        jsonBody.put("amount",String.valueOf(amount));
        jsonBody.put("MerchantId",merchantID);
        jsonBody.put("CardNumber",encrypt_cardNo);
        jsonBody.put("CVV",encrypt_cVV);
        jsonBody.put("ExpiryMonth",mExpiryMonth);
        jsonBody.put("ExpiryYear",mExpiryYear);

        try {

            RestClient.getApiServiceForPojo().Payment(jsonBody, new Callback<JsonElement>() {
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
                        if(paymentDetail.getResponseCode().equals("1")){

                            GiftCard m = gson.fromJson(CommonUtility.GiftCardDetails, GiftCard.class);
                            m.setBarcode(CommonUtility.BarCodeValue);
                            DbNew.getInstance(PaymentActivity.this).CreatePurchasedCard(CommonUtility.BarCodeValue, new Gson().toJson(m));
                            MessageDialog.showDialog(PaymentActivity.this, "Giftcard has been successfully purchased",false,true,UserDashBoardActivity.class);
                        }else{
                            MessageDialog.showDialog(PaymentActivity.this, paymentDetail.getResponseMessage(),false);
                        }

                    }else{

                    }
                    LoadingBox.dismissLoadingDialog();
//                    Utils.hideKeyboard(PaymentActivity.this);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("url -->", error.getUrl());
                    Log.e("Error Comes", error.getMessage() + "");
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showDialog(PaymentActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
                }
            });
        }catch(Exception ex)
        {
            MessageDialog.showDialog(PaymentActivity.this , "Fail to Process");
        }

    }
    @Override
    public String getCardNumber() {
        if(editTextCardNo!=null) {
            return editTextCardNo.getText().toString();
        }
        else
        {
            return null;
        }
    }

    @Override
    public String getCvv() {
        if(editTextCvv!=null) {
            return editTextCvv.getText().toString();
        }
        else
        {
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
        if(editTextNameOnCard!=null) {
            return editTextNameOnCard.getText().toString();
        }
        else
        {
            return null;

        }
    }

    @Override
    public String getCurrency() {
        return "usd";
    }
    private void showMessageDialog(String message)
    {
        final Dialog dialog = new Dialog(this,
                R.style.Theme_AppCompat_Translucent);
        dialog.setContentView(R.layout.dialog_custom_msg);
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView textForMessage = (TextView)dialog.findViewById(R.id.text_msg);
        textForMessage.setText(message);
        Button btnOk =(Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }
    private String Encryption(String editTextValue) {
        return CommonUtility.encryption(editTextValue, PaymentActivity.this);
    }
}
