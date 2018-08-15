package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.GiftCardAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.OnSelectionChanged;
import com.kippinretail.Interface.SuperModal;
import com.kippinretail.Modal.GiftCardList.GiftCard;
import com.kippinretail.Modal.ModalResponse;
import com.kippinretail.Modal.VoucherModal;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityGiftCardCharity extends SuperActivity implements View.OnClickListener, AdapterView.OnItemClickListener, OnSelectionChanged {


    private String userId, merchantId;
    private GridView gridViewGiftCards;
    private GiftCardAdapter adapter = null;
    List<VoucherModal> voucherModals = null;
    private TextView txtMessage;
    private Button buynow;
    private VoucherModal selected;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_cards);
        initilization();
        buynow.setText(R.string.donate_the_cards);
    }

    private void initilization() {
        generateActionBar(R.string.gift_cards,true,true,false);
        gridViewGiftCards = (GridView) findViewById(R.id.gridViewGiftCards);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        buynow = (Button) findViewById(R.id.buynow);
        buynow.setOnClickListener(this);
        voucherModals = new ArrayList<VoucherModal>();
        gridViewGiftCards.setOnItemClickListener(this);

        Intent i = getIntent();
        if (i != null) {
            merchantId = i.getStringExtra(getString(R.string.merchant));
            userId = i.getStringExtra(getString(R.string.user));
            getGiftCards();
        }

        if (!isStarted) {
            getGiftCards();
        } else {
            isStarted = false;
        }

    }

    boolean isStarted = true;

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getGiftCards() {


        Log.e("merchantId---->", CommonData.getUserData(this).getId() + "");
        Log.e("merchantId---->", merchantId);

        voucherModals.clear();

        LoadingBox.showLoadingDialog(ActivityGiftCardCharity.this, "Loading...");
//        GiftCard/GetMyPurchasedGiftcardListByMerchantId/
        RestClient.getApiServiceForPojo().GetMyPurchasedGiftcardListByMerchantId(merchantId, CommonData.getUserData(this).getId() + "", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                Log.e("OutPut-->", jsonElement.toString());
                Log.e("URL", response.getUrl());
                Type listtype = new TypeToken<List<GiftCard>>() {
                }.getType();
                Gson gson = new Gson();
                List<GiftCard> giftCards = (List<GiftCard>) gson.fromJson(jsonElement.toString(), listtype);
                if (giftCards != null) {

                    if (giftCards.size() == 1 && !giftCards.get(0).getResponseMessage().equals("Success")) {
//                        MessageDialog.showDialog(ActivityGiftCardCharity.this, "No gift cards available for this merchantId.");
                        MessageDialog.showDialog(ActivityGiftCardCharity.this, giftCards.get(0).getResponseMessage());
                    } else {

                        for (GiftCard giftCard : giftCards) {
                            VoucherModal modal = new VoucherModal();
                            modal.setUrl(giftCard.getGiftcardImage());
                            modal.setBarcode(giftCard.getBarcode());
                            modal.setMessage(giftCard.getResponseMessage());
                            modal.setPrice(giftCard.getPrice());
                            modal.setIscheched(false);
                            modal.setGiftId(giftCard.getId());
                            voucherModals.add(modal);
                        }
                        loadCards();
                    }
                }
                LoadingBox.dismissLoadingDialog();

            }

            @Override
            public void failure(RetrofitError error) {
                MessageDialog.showFailureDialog(ActivityGiftCardCharity.this);

                Log.e("Error Comes ", error.getMessage());

            }
        });
    }

    private void loadCards() {
        adapter = new GiftCardAdapter(ActivityGiftCardCharity.this, voucherModals, false, this);
        gridViewGiftCards.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buynow:

                if (selected != null) {
                    giftGiftcard();
                }else {
                    MessageDialog.showDialog(activity,"Please select any giftcard",false);
                }
                break;
        }
    }


    private void giftGiftcard() {
        Log.e("customerId", merchantId);
        StringBuffer responseText = new StringBuffer();
        List<VoucherModal> rowitem = adapter._voucherModals;

        for (int i = 0; i < rowitem.size(); i++) {
            if (rowitem.get(i).ischeched()) {
                responseText.append(rowitem.get(i).getGiftId() + ",");
            }
        }

        String giftCardIds = responseText.toString().substring(0, responseText.toString().length() - 1);

        LoadingBox.showLoadingDialog(ActivityGiftCardCharity.this, "Loading...");
        RestClient.getApiServiceForPojo().CharityGiftCard_(CommonData.getUserData(ActivityGiftCardCharity.this).getId() + "", userId, selected.getBarcode(), "", new Callback<JsonElement>() {

            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("Output-->", jsonElement.toString());
                Log.e("Output-->", response.getUrl());

                ModalResponse modalResponse = new Gson().fromJson(jsonElement,ModalResponse.class);
                if(modalResponse.getResponseMessage().equals("Success.")){
                    MessageDialog.showDialog(ActivityGiftCardCharity.this, "Card successfully delivered",true);
                }else{
                    MessageDialog.showDialog(ActivityGiftCardCharity.this, "Fail to transfer gift card Try Later",true);
                }

                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.getMessage());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(ActivityGiftCardCharity.this, "We are experiencing technical difficulties but value your business… please try again later");
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("================", "===================");
    }

    @Override
    public void onSelectionChanged(int position, SuperModal voucherModal, boolean isChecked) {
        if(isChecked)
        {
            selected = (VoucherModal) voucherModal;
        }
        else {
            selected = null;
        }
    }

}
