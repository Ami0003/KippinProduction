package com.kippinretail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.kippinretail.Adapter.MyLoayaltyCardListAdapter;
import com.kippinretail.Adapter.MyLoyaltyCardListAdapter_Db;
import com.kippinretail.ApplicationuUlity.AppStatus;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.DbNew;
import com.kippinretail.ApplicationuUlity.NonKippinCardType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import notification.NotificationHandler;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by agnihotri on 11/01/18.
 */

public class CharityDonatedLoyaltySection extends AbstractListActivity implements View.OnClickListener{
    private String country=null;
    private MyLoayaltyCardListAdapter adapter = null;

    private List<Merchant> responsemerchants;
    ArrayList<Merchant> responseToGetLoyaltyCardModels;

   // layout_nonKippin


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        responseToGetLoyaltyCardModels=new ArrayList<>();
        responseToGetLoyaltyCardModels.clear();
        updateToolBar();
        updateUI();
        addListener();;

    }

    @Override
    public void updateToolBar() {
        generateActionBar(R.string.title_user_MyLoayaltyCardListActivity, true, true, false);
    }

    //1) List of Merchants for KIPPIN LOYALTY CARD
    @Override
    public void updateUI() {
        country = CommonData.getUserData(this).getCountry();
        layout_nonKippin.setVisibility(View.GONE);

        // if internet Access is available then load online else from database
        if (isNetworkAvailable()) {
            makeSubcribedMerchantList();
        } else {
            // Fetching data from Database
            //new CharityDonatedLoyaltySection.LoadingDataFromDataBase().execute();
        }

        Utils.hideKeyboard(this);
    }
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        android.util.Log.e("char-------Text:",""+charText);
        responsemerchants.clear();
        if (charText.length() == 0) {
            android.util.Log.e("NONKIPPIN-----: ",""+responseToGetLoyaltyCardModels.size());
            responsemerchants.addAll(responseToGetLoyaltyCardModels);
        }
        else
        {
            for (Merchant wp : responseToGetLoyaltyCardModels)
            {
                android.util.Log.e("W-----P:::",""+wp.getBusinessName());
                if(wp.getBusinessName()!=null){
                    if (wp.getBusinessName().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        responsemerchants.add(wp);
                    }
                }

            }
        }
        android.util.Log.e("responsemerchants:",""+responsemerchants.size());
        adapter = new MyLoayaltyCardListAdapter(responsemerchants, CharityDonatedLoyaltySection.this);
        list_data.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    @Override
    public void addListener() {
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adapter != null) {
                    String text = txtSearch.getText().toString().toLowerCase(Locale.getDefault());
                    filter(text);
                    //adapter.getFilter().filter(s);
                    if ("My Non KIPPIN Loyalty Cards".toLowerCase().contains(s.toString().toLowerCase())) {
                        iv_nonKippin.setVisibility(View.VISIBLE);
                        txtNonKippin.setVisibility(View.VISIBLE);
                        undline_nonKippin.setVisibility(View.VISIBLE);
                    } else {
                        iv_nonKippin.setVisibility(View.GONE);
                        txtNonKippin.setVisibility(View.GONE);
                        undline_nonKippin.setVisibility(View.GONE);
                    }
                }  else{
                    if ("My Non KIPPIN Loyalty Cards".toLowerCase().contains(s.toString().toLowerCase())) {
                        iv_nonKippin.setVisibility(View.VISIBLE);
                        txtNonKippin.setVisibility(View.VISIBLE);
                        undline_nonKippin.setVisibility(View.VISIBLE);
                    } else {
                        iv_nonKippin.setVisibility(View.GONE);
                        txtNonKippin.setVisibility(View.GONE);
                        undline_nonKippin.setVisibility(View.GONE);
                    }
                }
            }
        });
        layout_nonKippin.setOnClickListener(this);
        // list_data.setOnItemClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    private void makeSubcribedMerchantList(){
        try {
            Log.e("makeSubcribantList()", "makeSubcribedMerchantList();");
            String userId = String.valueOf(CommonData.getUserData(this).getId());
            LoadingBox.showLoadingDialog(this, "Loading...");
            RestClient.getApiServiceForPojo().GetSubscribedMerchantListForLoyalityCard(userId, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("output ==>", jsonElement.toString());
                    Log.e("URL -->", response.getUrl());
                    Type listType = new TypeToken<List<Merchant>>() {
                    }.getType();
                    Gson gson = new Gson();
                    responsemerchants = (List<Merchant>) gson.fromJson(jsonElement.toString(), listType);
                    boolean flag = Utility.isResponseValid(responsemerchants);
                    if (flag) {

                        responseToGetLoyaltyCardModels.clear();
                        responseToGetLoyaltyCardModels.addAll(responsemerchants);
                        adapter = new MyLoayaltyCardListAdapter(responsemerchants, CharityDonatedLoyaltySection.this);
                        list_data.setAdapter(adapter);
                        LoadingBox.dismissLoadingDialog();

                    } else {
                        LoadingBox.dismissLoadingDialog();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(error.getUrl(), error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showFailureDialog(CharityDonatedLoyaltySection.this);

                }
            });
        }catch(Exception ex
                ){

        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.layout_nonKippin:
                Intent i = new Intent();
                i.setClass(this, NonKippinGiftCardListActivity.class);
                i.putExtra("NonKippinCardType", NonKippinCardType.LOYALTYCARD);
                startActivity(i);
                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                break;
        }
    }




    // ================================= OFFLINE CACHE ================================
    public boolean isNetworkAvailable() {
        return AppStatus.getInstance(activity).isOnline(activity);
    }




}
