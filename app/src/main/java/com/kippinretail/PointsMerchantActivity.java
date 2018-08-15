package com.kippinretail;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.AdapterPointsMerchant;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.PointsData.PointsDataa;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.kippin.utils.Utility.parseDateByHiphenYMD;

/**
 * Created by kamaljeet.singh on 3/22/2016.
 */
public class PointsMerchantActivity extends SuperActivity implements View.OnClickListener {
    Button searchButton;
    public List<PointsDataa> PointsList;
    RelativeLayout toDateRelativeLayout, fromDateRelativeLayout;
    private int year;
    private int month;
    private int day;
    TextView fromDateTextView, toDateTextView;
    static final int DATE_PICKER_ID = 1111;
    private String dateValue;
    private String mFromDate;
    private String mToDate;
    ListView listForPoints;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_merchant);
        Initilization();
    }

    private void Initilization() {
        generateActionBar(R.string.tile_merchant_point,true,true,false);
        //Buttons
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        fromDateRelativeLayout = (RelativeLayout) findViewById(R.id.fromDateRelativeLayout);
        toDateRelativeLayout = (RelativeLayout) findViewById(R.id.toDateRelativeLayout);
        fromDateRelativeLayout.setOnClickListener(this);
        toDateRelativeLayout.setOnClickListener(this);

        fromDateTextView = (TextView) findViewById(R.id.fromDateTextView);
        toDateTextView = (TextView) findViewById(R.id.toDateTextView);
        // ListView
        listForPoints = (ListView) findViewById(R.id.listForPoints);


        getPoints();
    }

    private void DateTime() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    private void getPoints() {
        String merchantId = "" + CommonData.getUserData(PointsMerchantActivity.this).getId();
        Log.e("merchantId", "==" + merchantId);
        LoadingBox.showLoadingDialog(PointsMerchantActivity.this, "");
        RestClient.getApiServiceForPojo().QueryToGetPoints(merchantId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement loginData, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.i("Tag", "Request data " + new Gson().toJson(loginData));
                //[{"Point":75,"Month":3,"Year":2016,"ResponseCode":1,"UserId":0,"ResponseMessage":"Success"}]
                Gson gson = new Gson();
                String jsonOutput = loginData.toString();
                Log.e("jsonOutput", "=" + jsonOutput);
                Type listType = new TypeToken<List<PointsDataa>>() {
                }.getType();
                PointsList = (List<PointsDataa>) gson.fromJson(jsonOutput, listType);
                if (PointsList.get(0).getResponseCode() != 5) {
                    AdapterPointsMerchant adap = new AdapterPointsMerchant(PointsMerchantActivity.this, PointsList);
                    listForPoints.setAdapter(adap);
                } else {
                    listForPoints.setAdapter(null);
                    CommonDialog.With(PointsMerchantActivity.this).Show(PointsList.get(0).getResponseMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Login Failure => ", " = " + error.getMessage());
                MessageDialog.showDialog(PointsMerchantActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);

            }

        });


    }

    private void backPressed() {
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
                case R.id.searchButton:
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                Date from = null;
                Date to = null;
                Log.e("mFromDate", "=" + mFromDate);
                Log.e("mToDate", "=" + mToDate);
                if (mFromDate != null) {
                    if (mToDate != null) {
                        try {
                            from = sdf.parse(mFromDate);
                            to = sdf.parse(mToDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (!from.equals(to)) {
                            if (to.after(from)) {
                                searchPoints();
                            } else {
                                CommonDialog.With(PointsMerchantActivity.this).Show("Start date cannot be greater then end date");
                            }
                        } else {
                            CommonDialog.With(PointsMerchantActivity.this).Show("Start date cannot be equal to end date");
                        }

                    } else {
                        CommonDialog.With(PointsMerchantActivity.this).Show("Please select end date");
                    }
                } else {
                    CommonDialog.With(PointsMerchantActivity.this).Show("Please select start date");
                }
                break;
            case R.id.toDateRelativeLayout:
                DateTime();
                dateValue = "To";
                Calendar c1 = Calendar.getInstance();
                int year1 = c1.get(Calendar.YEAR);
                int month1 = c1.get(Calendar.MONTH);
                int day1 = c1.get(Calendar.DAY_OF_MONTH);
                //int month_day=month+1;
                clickDate(year1, month1, day1, R.style.Date_Picker_Spinner);
                //showDialog(DATE_PICKER_ID);
                break;
            case R.id.fromDateRelativeLayout:
                dateValue = "From";
                Log.d("fromDateRelativeLayout:","fromDateRelativeLayout:");
                Calendar c2 = Calendar.getInstance();
                int year2= c2.get(Calendar.YEAR);
                int month2 = c2.get(Calendar.MONTH);
                int day2 = c2.get(Calendar.DAY_OF_MONTH);
                //int month_day=month+1;
                clickDate(year2, month2, day2, R.style.Date_Picker_Spinner);
                //showDialog(DATE_PICKER_ID);
                break;
        }
    }
    @VisibleForTesting
    void clickDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(PointsMerchantActivity.this)
                .callback(startDateClick)
                .spinnerTheme(spinnerTheme)
                //.maxDate(year_Set,month_Set,day_Set)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    public com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener startDateClick = new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = parseDateByHiphenYMD(dayOfMonth, monthOfYear + 1, year);
            Log.d("OnDateSetListener","OnDateSetListener");
            year = year;
            month = monthOfYear;
            day = dayOfMonth;

            if (dateValue.equals("From")) {
                mFromDate = formatDate(year, (month ), day);
                Log.e("mFromDate", "=" + mFromDate);

                fromDateTextView.setText(setFormatDate(year, (month), day));
            } else {
                mToDate = formatDate(year, (month), day);
                Log.e("mToDate", "=" + mToDate);
                toDateTextView.setText(setFormatDate(year, (month), day));
            }

        }
    };
    private void searchPoints() {
        String MerchantID = "" + CommonData.getUserData(PointsMerchantActivity.this).getId();
        Log.e("MerchantID", "=" + MerchantID);
        Log.e("mFromDate", "=" + mFromDate);
        Log.e("mToDate", "=" + mToDate);
        LoadingBox.showLoadingDialog(PointsMerchantActivity.this, "");
        RestClient.getApiServiceForPojo().QueryToGetSearchedPoints(MerchantID, mFromDate, mToDate, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement loginData, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.i("Tag", "Request data " + new Gson().toJson(loginData));
                //[{"Point":75,"Month":3,"Year":2016,"ResponseCode":1,"UserId":0,"ResponseMessage":"Success"}]
                Gson gson = new Gson();
                String jsonOutput = loginData.toString();
                Log.e("jsonOutput", "=" + jsonOutput);
                Type listType = new TypeToken<List<PointsDataa>>() {
                }.getType();
                PointsList = (List<PointsDataa>) gson.fromJson(jsonOutput, listType);
                if (PointsList.get(0).getResponseCode() != 5) {
                    AdapterPointsMerchant adap = new AdapterPointsMerchant(PointsMerchantActivity.this, PointsList);
                    listForPoints.setAdapter(adap);
                } else {
                    listForPoints.setAdapter(null);
                    CommonDialog.With(PointsMerchantActivity.this).Show(PointsList.get(0).getResponseMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Login Failure => ", " = " + error.getMessage());
                Log.e("URLLLL Failure => ", " = " + error.getUrl());
                MessageDialog.showDialog(PointsMerchantActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);

            }

        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Log.d("onCreateDialog","onCreateDialog");
        switch (id) {
            case DATE_PICKER_ID:
                //start changes...
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month, day);
                //c.add(Calendar.DATE,1);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dialog;
            //end changes...
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            Log.d("OnDateSetListener","OnDateSetListener");
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            if (dateValue.equals("From")) {
                mFromDate = formatDate(year, (month ), day);
                Log.e("mFromDate", "=" + mFromDate);

                fromDateTextView.setText(setFormatDate(year, (month), day));
            } else {
                mToDate = formatDate(year, (month), day);
                Log.e("mToDate", "=" + mToDate);
                toDateTextView.setText(setFormatDate(year, (month), day));
            }
        }
    };

    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();

        //SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private static String setFormatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        return sdf.format(date);
    }
}
