package com.kippinretail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.CountryList.Countries;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SelectCountryActivity extends SuperActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
//    private ListView listCountries;
//    ArrayList<String> country = null;
//    private ArrayAdapter<String> adapter;
//    private boolean flag = false;
//    private TextView txtheading;
//    LinearLayout layout_listView;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_country);
//        initilization();
//    }
//
//    private void initilization() {
//        generateActionBar(R.string.tile_country,true,true,false);
//        listCountries = (ListView) findViewById(R.id.listContries);
//        txtheading = (TextView) findViewById(R.id.txtheading);
//        country = new ArrayList<String>();
//        layout_listView = (LinearLayout)findViewById(R.id.layout_listView);
//        listCountries.setOnItemClickListener(this);
//        getCountrtList();
//    }
//
//    private void getCountrtList() {
//
//        LoadingBox.showLoadingDialog(SelectCountryActivity.this, "Loading...");
//        RestClient.getApiServiceForPojo().GetAllCountries(new Callback<JsonElement>() {
//            @Override
//            public void success(JsonElement jsonElement, Response response) {
//                Log.e("output ==>", jsonElement.toString());
//                Type listtype = new TypeToken<List<Countries>>() {
//                }.getType();
//                Gson gson = new Gson();
//                List<Countries> countries = (List<Countries>) gson.fromJson(jsonElement.toString(), listtype);
//                if (countries != null) {
//                    for (Countries _countries : countries) {
//
//                        country.add(_countries.getCountryName());
//                    }
//
//                    adapter = new ArrayAdapter<String>(SelectCountryActivity.this, R.layout.subitem_select_country, country);
//                    listCountries.setAdapter(adapter);
//                    LoadingBox.dismissLoadingDialog();
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.e("output ==>", error.getMessage());
//                LoadingBox.dismissLoadingDialog();
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.txtheading:
//                layout_listView.setVisibility(View.VISIBLE);
//                listCountries.setVisibility(View.VISIBLE);
//                //showMerchant();
//                break;
//        }
//    }
//
//    public void showCountryList(View v) {
//        layout_listView.setVisibility(View.VISIBLE);
//        listCountries.setVisibility(View.VISIBLE);
//    }
//
//
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        String country = parent.getItemAtPosition(position).toString();
//        txtheading.setText(country);
//        layout_listView.setVisibility(View.INVISIBLE);
//        listCountries.setVisibility(View.INVISIBLE);
//
//        Intent i= ( getIntent()==null ? new Intent() : getIntent()) ;
//
//        if(i.getStringExtra("parentButton")!=null && i.getStringExtra("parentButton").equals("GetNewPunchCard"))
//            i.setClass(SelectCountryActivity.this, MerchantsOfPurchasedGiftCardsActivity.class);
//            else
//        i.setClass(SelectCountryActivity.this, SelectMerchantActivity.class);
//
//        i.putExtra("country", country);
//        startActivity(i);
//        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
//    }

    private ListView listCountries;
    ArrayList<String> country = null;
    private ArrayAdapter<String> adapter;
    private boolean flag = false;
    //  private TextView txtheading;
    LinearLayout layout_listView;
    private Spinner sp_selectPoints;
    private String mCountry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        initilization();
    }

    private void initilization() {
        generateActionBar(R.string.tile_country,true,true,false);
       // listCountries = (ListView) findViewById(R.id.listContries);
        //txtheading = (TextView) findViewById(R.id.txtheading);
        country = new ArrayList<String>();
   //     layout_listView = (LinearLayout)findViewById(R.id.layout_listView);
        sp_selectPoints = (Spinner) findViewById(R.id.sp_selectPoints);
      //  listCountries.setOnItemClickListener(this);
        getCountrtList();
    }

    private void getCountrtList() {

        LoadingBox.showLoadingDialog(SelectCountryActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().GetAllCountries(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output ==>", jsonElement.toString());
                Type listtype = new TypeToken<List<Countries>>() {
                }.getType();
                Gson gson = new Gson();
                List<Countries> countries = (List<Countries>) gson.fromJson(jsonElement.toString(), listtype);
                ArrayList<String> list = new ArrayList<>();

                if (countries != null) {
                    for (Countries _countries : countries) {

                        //country.add(_countries.getCountryName());
                        list.add(_countries.getCountryName());
                    }
                    LoadingBox.dismissLoadingDialog();
                /*    adapter = new ArrayAdapter<String>(SelectCountryActivity.this, R.layout.subitem_select_country, country);
                    listCountries.setAdapter(adapter);*/
                    list.set(0,"Select country");

                    ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(SelectCountryActivity.this, R.layout.spinner_item, list);

                    sp_selectPoints.setAdapter(monthAdapter);

                    sp_selectPoints.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //mMonthValue = monthSpinner.getSelectedItem().toString();
                            mCountry = sp_selectPoints.getSelectedItem().toString();

                            if (!mCountry.equals("Select country")) {
                                Intent i= ( getIntent()==null ? new Intent() : getIntent()) ;
                                if(i.getStringExtra("parentButton")!=null && i.getStringExtra("parentButton").equals("GetNewPunchCard"))
                                    i.setClass(SelectCountryActivity.this, MerchantsOfPurchasedGiftCardsActivity.class);
                                else
                                    i.setClass(SelectCountryActivity.this, SelectMerchantActivity.class);
                                i.putExtra("country", mCountry);
                                startActivity(i);
                                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);


                                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

                            } else {
                                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                            }
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
                MessageDialog.showDialog(SelectCountryActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

           /* case R.id.txtheading:
                layout_listView.setVisibility(View.VISIBLE);
                listCountries.setVisibility(View.VISIBLE);
                //showMerchant();
                break;*/
        }
    }

    public void showCountryList(View v) {
        layout_listView.setVisibility(View.VISIBLE);
        listCountries.setVisibility(View.VISIBLE);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String country = parent.getItemAtPosition(position).toString();
        //   txtheading.setText(country);
        layout_listView.setVisibility(View.INVISIBLE);
        listCountries.setVisibility(View.INVISIBLE);

     /*   Intent i= ( getIntent()==null ? new Intent() : getIntent()) ;

        if(i.getStringExtra("parentButton")!=null && i.getStringExtra("parentButton").equals("GetNewPunchCard"))
            i.setClass(SelectCountryActivity.this, MerchantsOfPurchasedGiftCardsActivity.class);
            else
        i.setClass(SelectCountryActivity.this, SelectMerchantActivity.class);
        i.putExtra("country", mCountry);
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);*/
    }
}
