package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.MerchantGiftListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftCard;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by agnihotri on 02/02/18.
 */

public class ActivityMerchantGiftCardList extends SuperActivity implements View.OnClickListener {
    ListView listGiftCard;
    EditText txtSearch;
    MerchantGiftListAdapter merchantGiftListAdapter;
    ArrayList<GiftCard> gift_Cards;
    ArrayList<GiftCard> getGiftCards;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftcardlist);
        gift_Cards = new ArrayList<>();
        getGiftCards = new ArrayList<>();
        getGiftCards.clear();
        gift_Cards.clear();
        initilization();
    }

    private void initilization() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        generateActionBar(R.string.title_user_gift_Card, true, true, false);
        listGiftCard = (ListView) findViewById(R.id.listGiftCard);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        Intent i = getIntent();
        String uId = CommonData.getUserData(this).getId() + "";
        makeGiftList(uId);


        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtSearch.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);

            }
        });

    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        Log.e("char-------Text:", "" + charText);
        getGiftCards.clear();
        if (charText.length() == 0) {
            Log.e("NONKIPPIN-----: ", "" + gift_Cards.size());
            getGiftCards.addAll(gift_Cards);
        } else {
            for (GiftCard wp : gift_Cards) {
                //  Log.e("W-----P:::",""+wp.getFolderName());
                if (wp.getFolderName() != null) {
                    if (wp.getFolderName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        getGiftCards.add(wp);
                        Log.e("W-----P:::", "" + wp.getFolderName());
                        Log.e("W---Logo--P:::", "" + wp.getLogoImage());
                    }
                }

            }
        }

        merchantGiftListAdapter = new MerchantGiftListAdapter(getGiftCards, ActivityMerchantGiftCardList.this, "0");
        listGiftCard.setAdapter(merchantGiftListAdapter);
        merchantGiftListAdapter.notifyDataSetChanged();

    }

    public void makeGiftList(String customerID) {
        LoadingBox.showLoadingDialog(ActivityMerchantGiftCardList.this, "Loading...");
        RestClient.getApiServiceForPojo().GetPhysicalCardByUserId(customerID, "0", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("makeGiftList", jsonElement.toString());

                // USE GIFT CARD MERCHANT LIST MODAL
                Type listtype = new TypeToken<List<GiftCard>>() {
                }.getType();
                Gson gson = new Gson();
                List<GiftCard> giftCards = (List<GiftCard>) gson.fromJson(jsonElement.toString(), listtype);
                Log.e("giftCards:", "" + giftCards.size());
                if (giftCards.size() != 0) {
                    LoadingBox.dismissLoadingDialog();
                    if (giftCards.get(0).getResponseMessage().equals("No physical cards available.")) {
                        MessageDialog.showDialog(ActivityMerchantGiftCardList.this, "No gift cards received. ", false);
                    } else {
                        getGiftCards.addAll(giftCards);
                        gift_Cards.addAll(giftCards);
                        Log.e("MerchantGiftListAdapter", "MerchantGiftListAdapter");
                        merchantGiftListAdapter = new MerchantGiftListAdapter(giftCards, ActivityMerchantGiftCardList.this, "0");
                        listGiftCard.setAdapter(merchantGiftListAdapter);
                    }


                } else {
                    LoadingBox.dismissLoadingDialog();
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("============", error.getMessage());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(ActivityMerchantGiftCardList.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }


}
