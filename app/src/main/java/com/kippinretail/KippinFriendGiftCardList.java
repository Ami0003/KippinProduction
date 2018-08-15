package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.GiftCardListAdapter1;
import com.kippinretail.Adapter.GiftCardListAdapter1Image;
import com.kippinretail.Adapter.MerchantListAdapterModifiedImage;
import com.kippinretail.Adapter.MerchantVoucherListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftCardMerchantList.MerchantDetail;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class KippinFriendGiftCardList extends AbstractListActivity implements View.OnClickListener ,AdapterView.OnItemClickListener {

    private MerchantListAdapterModifiedImage adapter;
    List<MerchantDetail> responsemerchantDetails;
    private ArrayList<MerchantModal> merchant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateToolBar();
        updateUI();
        addListener();;
    }
    @Override
    public void updateToolBar() {merchant = new ArrayList<MerchantModal>();
        generateActionBar(R.string.title_user_KippinFriendGiftCardList, true, true, false);
    }

    @Override
    public void updateUI() {
        layout_nonKippin.setVisibility(View.GONE);
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

            }
        });
        list_data.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        giftCardsFromKippinFriendsList();
    }

    private void giftCardsFromKippinFriendsList()
    {
        
        if(CommonData.getUserData(KippinFriendGiftCardList.this)!=null) {
            String userId = String.valueOf(CommonData.getUserData(KippinFriendGiftCardList.this).getId());
            LoadingBox.showLoadingDialog(KippinFriendGiftCardList.this, "Loading...");
            RestClient.getApiServiceForPojo().GetMerchantListForRecievedUser(userId, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("Output -->", jsonElement.toString());
                    Log.e("Url", response.getUrl());
                    // USE GIFT CARD MERCHANT LIST MODAL
                    Type listtype = new TypeToken<List<MerchantDetail>>() {
                    }.getType();
                    merchant.clear();
                    Gson gson = new Gson();
                    responsemerchantDetails = (List<MerchantDetail>) gson.fromJson(jsonElement.toString(), listtype);
                    boolean flag = Utility.isResponseValid(responsemerchantDetails)  ;
                    // Make adapter Common with Charity Merchant List
                    /*if(flag){
                        adapter = new GiftCardListAdapter1Image(responsemerchantDetails,KippinFriendGiftCardList.this);
                        list_data.setAdapter(adapter);
                    }else{
                        MessageDialog.showDialog(KippinFriendGiftCardList.this, responsemerchantDetails.get(0).getResponseMessage(), true);
                    }*/
                    if (responsemerchantDetails != null) {
                        if (responsemerchantDetails.size() == 1 && !responsemerchantDetails.get(0).getResponseMessage().equals("Success")) {
                            MessageDialog.showDialog(activity, responsemerchantDetails.get(0).getResponseMessage());
                        } else {
                            for (MerchantDetail temp : responsemerchantDetails) {
                                MerchantModal _modal = new MerchantModal();
                                _modal.setName(temp.getBusinessName());
                                _modal.setMerchantid(temp.getId());
                                _modal.setMessage(temp.getResponseMessage());
                                _modal.setProfileImage(temp.getProfileImage());
                                _modal.setBussinessName(temp.getBusinessName());
                                _modal.setIsRead(temp.getIsRead());
                                merchant.add(_modal);

                            }
                          /*  System.out.println("Inside else" + merchant.size());
                            adapter = new MerchantVoucherListAdapter(KippinFriendGiftCardList.this, merchant, parentButton);
                            listMerchants.setAdapter(adapter);*/

                        }

                    if(flag){
                        adapter = new MerchantListAdapterModifiedImage(KippinFriendGiftCardList.this,merchant,false);
                        list_data.setAdapter(adapter);
                    }else{
                        MessageDialog.showDialog(KippinFriendGiftCardList.this, responsemerchantDetails.get(0).getResponseMessage(), true);
                    }
                        LoadingBox.dismissLoadingDialog();
                 }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("============", error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showFailureDialog(KippinFriendGiftCardList.this);


                }
            });
        }

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Bundle bundle = new Bundle() ;
        bundle.putString(ActivityCharity_GiftCardUsingMerchant.MERCHANT_ID , responsemerchantDetails.get(position).getId());
        bundle.putString("merchantNmae", responsemerchantDetails.get(position).getBusinessName());
        Utility.enterActivity(this,ActivityCharity_GiftCardUsingMerchant.class , bundle);

      /*  Intent i = new Intent();
        i.setClass(this,GiftCardFromKippinFriend.class);
        i.putExtra("merchantId", responsemerchantDetails.get(position).getId());
        String id11 = String.valueOf(CommonData.getUserData(this).getId());
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);*/
    }
}
