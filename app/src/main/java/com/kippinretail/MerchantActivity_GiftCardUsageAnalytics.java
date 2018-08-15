package com.kippinretail;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.Analytic_GiftAdapter;
import com.kippinretail.Adapter.Analytics_GiftCardUsageAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ResponseForGiftAnalytics;
import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ResponseForGiftCardUsageAnalytics;
import com.kippinretail.Modal.CountryList.Countries;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MerchantActivity_GiftCardUsageAnalytics extends MerchantActivity_Analytics  {
    ArrayList<String> country = null;
    private ArrayAdapter<String> adapter;

    private String mCountry;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();
        addListener();
    }

    @Override
    void updateUI() {
        try {
            generateActionBar(R.string.tile_merchant_GiftCardUsageAnalytics, true, true, false);
            layout_searchButton.setVisibility(View.GONE);
            layout_country.setVisibility(View.VISIBLE);
            txt_col1.setText("Country");
            txt_col2.setText("Amount");
            txt_col2.setTypeface(null,Typeface.NORMAL);
            txt_col3.setVisibility(View.GONE);
            getCountrtList();
        }catch(Exception ex){
            Log.e("Exception",ex.getMessage());
        }
    }

    private void getCountrtList() {

        LoadingBox.showLoadingDialog(MerchantActivity_GiftCardUsageAnalytics.this, "Loading...");
        RestClient.getApiServiceForPojo().GetAllCountries(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output ==>", jsonElement.toString());
                Type listtype = new TypeToken<List<Countries>>() {
                }.getType();
                Gson gson = new Gson();
                List<Countries> countries = (List<Countries>) gson.fromJson(jsonElement.toString(), listtype);
                ArrayList<String> list = new ArrayList<String>();
                if (countries != null) {
                    for (Countries _countries : countries) {

                        //country.add(_countries.getCountryName());
                        list.add(_countries.getCountryName());
                    }
                    LoadingBox.dismissLoadingDialog();
                /*    adapter = new ArrayAdapter<String>(SelectCountryActivity.this, R.layout.subitem_select_country, country);
                    listCountries.setAdapter(adapter);*/
                   // list.set(0, "Select country");
                    list.add(0, "Select country");

                    ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(MerchantActivity_GiftCardUsageAnalytics.this, R.layout.spinner_item, list);
                    sp_selectPoints.setAdapter(monthAdapter);
                    sp_selectPoints.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //mMonthValue = monthSpinner.getSelectedItem().toString();
                            mCountry = sp_selectPoints.getSelectedItem().toString();

//                            if (!mCountry.equals("Select country")) {
//                                Intent i = (getIntent() == null ? new Intent() : getIntent());
//                                if (i.getStringExtra("parentButton") != null && i.getStringExtra("parentButton").equals("GetNewPunchCard"))
//                                    i.setClass(MerchantActivity_GiftCardUsageAnalytics.this, MerchantsOfPurchasedGiftCardsActivity.class);
//                                else
//                                    i.setClass(MerchantActivity_GiftCardUsageAnalytics.this, SelectMerchantActivity.class);
//                                i.putExtra("country", mCountry);
//                                startActivity(i);
//                                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
//
//
//                                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
//
//                            } else {
//                                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
//                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });


                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("output ==>", error.getMessage());
                LoadingBox.dismissLoadingDialog();

                MessageDialog.showFailureDialog(MerchantActivity_GiftCardUsageAnalytics.this);
            }
        });
    }
    @Override
    public void addListener() {
        super.addListener();
        searchButtonForRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                Date from = null;
                Date to = null;
              if (getmFromDate() != null) {
                        if (getmToDate() != null) {
                            try {
                                from = sdf.parse(getmFromDate());
                                to = sdf.parse(getmToDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (!from.equals(to)) {
                                if (to.after(from)) {
                                    if(mCountry.equals("Select country")){
                                        MessageDialog.showDialog(MerchantActivity_GiftCardUsageAnalytics.this,"Please select date range and country",false);
                                    }else{
                                        searchDatyaForRevenue();
                                    }

                                } else {
                                    CommonDialog.With(MerchantActivity_GiftCardUsageAnalytics.this).Show("Start date cannot be greater then end date");
                                }
                            } else {
                                CommonDialog.With(MerchantActivity_GiftCardUsageAnalytics.this).Show("Start date cannot be equal to end date");
                            }

                        } else {
                            CommonDialog.With(MerchantActivity_GiftCardUsageAnalytics.this).Show("Please select end date");
                        }
                    } else {
                        CommonDialog.With(MerchantActivity_GiftCardUsageAnalytics.this).Show("Please select date range and country");
            }

            }
        });

    }

    private void searchDatyaForRevenue() {
        String merchantId = "" + CommonData.getUserData(this).getId();
        HashMap<String,String> jsonBody = new HashMap<String,String>();
        jsonBody.put("Country",mCountry);
        jsonBody.put("MerchantId",merchantId);
        jsonBody.put("StartDate",getmFromDate());
        jsonBody.put("EndDate", getmToDate());
        LoadingBox.showLoadingDialog(activity, "Loading ...");
        RestClient.getApiServiceForPojo().GiftCardRevenue(jsonBody, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());
                Gson gson = new Gson();
                List<ResponseForGiftCardUsageAnalytics> responseForGiftCardUsageAnalyticses = gson.fromJson(jsonElement.toString(), new TypeToken<List<ResponseForGiftCardUsageAnalytics>>() {
               }.getType());
               boolean flag = Utility.isResponseValid(responseForGiftCardUsageAnalyticses);
               if (flag) {
                    listForAnalysis.setAdapter(new Analytics_GiftCardUsageAdapter(responseForGiftCardUsageAnalyticses, MerchantActivity_GiftCardUsageAnalytics.this));
                } else {
                   listForAnalysis.setAdapter(null);
                   MessageDialog.showDialog(MerchantActivity_GiftCardUsageAnalytics.this, responseForGiftCardUsageAnalyticses.get(0).getResponseMessage(), false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("RestClient", error.getUrl());
                MessageDialog.showFailureDialog(MerchantActivity_GiftCardUsageAnalytics.this);
            }
        });
    }

    @Override
    void startSearch() {
        int i = 0;

    }
}
