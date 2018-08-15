package com.kippinretail.KippinInvoice;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.customdatepicker.MonthYearPicker;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by kamaljeet.singh on 12/21/2016.
 */

public class InvoiceStripePaymentActivity extends SuperActivity {
    private static final int DATE_PICKER_ID = 1111;
    private static Calendar exp;
    TextView tvBalanceAmount;
    EditText etCardNumber, etCvv, etAmounttobePaid, etJVID;
    TextView tvExpirationDate;
    Button btn_save;
    private String BalanceAmount = "";
    private String AmountToBePaid = "";
    private String InvoiceId;
    private String JVID = "";
    private String SupplierId;
    private Integer year, month, day;
    private String mExpiryMonth;
    private String mExpiryYear;
    MonthYearPicker monthYearPicker;
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth + 1;
            day = selectedDay;

            mExpiryMonth = "" + month;
            mExpiryYear = "" + year;

            tvExpirationDate.setText(mExpiryMonth + "/" + mExpiryYear);


        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stripe_new_payment_activity);
        Initlization();
        monthYearPicker = new MonthYearPicker(this);
        monthYearPicker.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                tvExpirationDate.setText(monthName("" + monthYearPicker.getSelectedMonthName())+ "/" + monthYearPicker.getSelectedYear());
                android.util.Log.e("Reuslt:", "" + monthYearPicker.getSelectedMonthName() + " >> " + monthYearPicker.getSelectedYear());
                //  monthname = monthName("" + monthYearPicker.getSelectedMonthName());
                mExpiryMonth=monthName("" + monthYearPicker.getSelectedMonthName());
                mExpiryYear="" + monthYearPicker.getSelectedYear();
            }
        }, null);
    }

    private void Initlization() {
        generateRightText1("STRIPE PAYMENT");

        tvBalanceAmount = (TextView) findViewById(R.id.tvBalanceAmount);
        //  tvAmountToBePaid = (TextView) findViewById(R.id.tvAmountToBePaid);

        etCardNumber = (EditText) findViewById(R.id.etCardNumber);
        tvExpirationDate = (TextView) findViewById(R.id.tvExpirationDate);
        etCvv = (EditText) findViewById(R.id.etCvv);
        etAmounttobePaid = (EditText) findViewById(R.id.etAmounttobePaid);
        etJVID = (EditText) findViewById(R.id.etJVID);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            BalanceAmount = extras.getString("BalanceAmount");
            AmountToBePaid = extras.getString("AmountToBePaid");
            InvoiceId = extras.getString("InvoiceId");
            JVID = extras.getString("JVID");
            SupplierId = extras.getString("SupplierId");
            // String.format("%.2f", BalanceAmount);

            // DecimalFormat df = new DecimalFormat("#.##");
            Log.e("BalanceAmount",""+BalanceAmount);

            String result= CommonNumberFormatter.getRound(Float.parseFloat(BalanceAmount));
            Log.e("result",""+result);
            tvBalanceAmount.setText("Balance Amount : " + result);
            //   tvAmountToBePaid.setText("Amount To Be Paid : " + AmountToBePaid);

        }

        btn_save = (Button) findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCardNumber.getText().toString().length() != 0 && etCvv.getText().toString().length() != 0 && mExpiryMonth.length() != 0
                        && mExpiryYear.length() != 0 && etAmounttobePaid.getText().toString().length() != 0 /*&& etJVID.getText().toString().length() != 0*/) {

                    Calendar today = Calendar.getInstance();
                    today.roll(Calendar.DATE, -1);

                    if (/*(exp.after(today) || exp.equals(today)) &&*/ etCardNumber.getText().toString().length() == 16 && etCvv.getText().toString().length() == 3) {
                        payViaStripe(etAmounttobePaid.getText().toString(), etJVID.getText().toString());
                    } else {
                        CommonDialog.With(InvoiceStripePaymentActivity.this).Show("Please enter valid credit card details");
                    }


                } else {
                    CommonDialog.With(InvoiceStripePaymentActivity.this).Show("Please fill mandatory field.");
                }

            }


        });
        tvExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showDialog(DATE_PICKER_ID);
                monthYearPicker.show();
            }
        });

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
    private void payViaStripe(String amountpaid, String JVIDTEXT) {

        LoadingBox.showLoadingDialog(InvoiceStripePaymentActivity.this, "");
        RestClient.getApiFinanceServiceForPojo().StripePayment(CommonUtility.encryption(etCardNumber.getText().toString(), InvoiceStripePaymentActivity.this),
                CommonUtility.encryption(etCvv.getText().toString(), InvoiceStripePaymentActivity.this),
                mExpiryMonth, mExpiryYear, InvoiceId, amountpaid, /*JVIDTEXT,*/ getCallback(new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Tag", "Request data " + new Gson().toJson(jsonElement));

                        try {
                            JSONObject jsonObject = new JSONObject(jsonElement.toString());
                            String ResponseCode = jsonObject.getString("ResponseCode");
                            String ResponseMessage = jsonObject.getString("ResponseMessage");

                            if (ResponseCode.equals("1")) {
                                CommonDialog.showDialog2Button(InvoiceStripePaymentActivity.this, "Payment Successful", new OnDialogDismissListener() {
                                    @Override
                                    public void onDialogDismiss() {
                                        CommonUtility.HomeClick(InvoiceStripePaymentActivity.this);
                                    }
                                });
                            } else {
                                CommonDialog.With(InvoiceStripePaymentActivity.this).Show(ResponseMessage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("Failure ", " = " + error.getMessage());
                        LoadingBox.dismissLoadingDialog();
                        ErrorCodes.checkCode(InvoiceStripePaymentActivity.this, error);
                    }
                }));

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                exp = Calendar.getInstance();
                exp.set(year, month, day);

                return new DatePickerDialog(this, pickerListener, year, month, day);
        }


        return null;
    }

}
