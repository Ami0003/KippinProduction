package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.FriendListAdapter;
import com.kippinretail.Adapter.NonKippinGiftCardListAdapter;
import com.kippinretail.Adapter.ShareUnshareNonKippinLoyaltyCardAdapter;
import com.kippinretail.ApplicationuUlity.CheckBoxType;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.OnSelectionChange;
import com.kippinretail.Modal.FriendList.FriendDetail;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.ResponseModal;
import com.kippinretail.Modal.ResponseToGetLoyaltyCard;
import com.kippinretail.Modal.ResponseToGetLoyaltyCardModel;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.callbacks.CheckBoxClickHandler;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TransferGiftCards_FriendListActivity extends AbstractListActivity implements View.OnClickListener,AdapterView.OnItemClickListener {


    private String customerID,parentButton;

    FriendListAdapter adapter = null;
    private List<FriendDetail> friendDetails;
    private ShareType shareType;
    private  List<ResponseToGetLoyaltyCard> serverresponse;
    private List<ResponseToGetLoyaltyCardModel> temp;
    private int index;
    HashMap<Integer , String > shareDate = null;
    HashMap<Integer , String > unshareDate = null;
    private ShareUnshareNonKippinLoyaltyCardAdapter lo0yaltyCardAdaper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateToolBar();
        updateUI();
        addListener();

    }

    @Override
    public void updateToolBar() {

    }

    @Override
    public void updateUI() {
        shareDate = new HashMap<Integer,String>();
        unshareDate = new HashMap<Integer,String>();
        temp = new ArrayList<ResponseToGetLoyaltyCardModel>();
        customerID = String.valueOf(CommonData.getUserData(TransferGiftCards_FriendListActivity.this).getId());
        layout_nonKippin.setVisibility(View.GONE);
        btn_upload.setVisibility(View.VISIBLE);
        shareType = (ShareType) getIntent().getSerializableExtra("shareType");
        if(shareType == ShareType.LOYALTY){
              generateActionBar(R.string.title_user_TransferGiftCards_FriendListActivity1, true, true, false);
            getAllLOyaltyCard();
            layout_txtMercahntName.setVisibility(View.VISIBLE);
            txt_share.setVisibility(View.VISIBLE);
            txt_unshare.setVisibility(View.VISIBLE);
            //btn_upload.setText("Share/Unshare Loyalty Card");
            btn_upload.setBackgroundResource(R.drawable.x_share_unshare_loyalty_card_red);
            btn_upload.setOnClickListener(this);
        }
        else if(shareType == ShareType.GIFTCARD){
           generateActionBar(R.string.title_user_TransferGiftCards_FriendListActivity, true, true, false);
            btn_upload.setText("Share Gift Card");
        }
        else if(shareType == ShareType.TRADEPOINT){
            makeFriendListToTrade();
            btn_upload.setBackgroundResource(R.drawable.x_trade_points_red);
            //btn_upload.setText("Trade Points");
        }

    }

    @Override
    public void addListener() {
        btn_upload.setOnClickListener(this);

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(lo0yaltyCardAdaper!=null){
                    lo0yaltyCardAdaper.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getAllLOyaltyCard(){
        String userId = String.valueOf(CommonData.getUserData(this).getId());
        String friendId = getIntent().getStringExtra("friendId");
        LoadingBox.showLoadingDialog(TransferGiftCards_FriendListActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().GetSharedPhysicalLoyalityCardByUserId(userId, friendId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output ==>", jsonElement.toString());
                Log.e("URL -->", response.getUrl());
                Type listType = new TypeToken<List<Merchant>>() {
                }.getType();
                Gson gson = new Gson();
                LoadingBox.dismissLoadingDialog();
                serverresponse = gson.fromJson(jsonElement.toString(), new TypeToken<List<ResponseToGetLoyaltyCard>>() {
                }.getType());

                for (ResponseToGetLoyaltyCard r : serverresponse) {
                    ResponseToGetLoyaltyCardModel m = new ResponseToGetLoyaltyCardModel();
                    m.setId(r.getId());
                    m.setBackImage(r.getBackImage());
                    m.setFolderName(r.getFolderName());
                    m.setCountry(r.getCountry());
                    m.setFrontImage(r.getFrontImage());
                    m.setActualUserId(r.getActualUserId());
                    m.setLogoImage(r.getLogoImage());
                    m.setMerchantId(r.getMerchantId());
                    m.setIsChecked(false);
                    m.setCardId(r.getCardId());
                    m.setIsShared(r.getIsShared());
                    m.setSenderId(r.getSenderId());
                    temp.add(m);
                }
                boolean flag = Utility.isResponseValid(serverresponse);
                if (flag) {
                     lo0yaltyCardAdaper=   new ShareUnshareNonKippinLoyaltyCardAdapter(temp, TransferGiftCards_FriendListActivity.this, false, new CheckBoxClickHandler() {

                        @Override
                        public void onChechkBoxClick(int id, String folderName, boolean isChecked, CheckBoxType checkboxtype) {
                            if (isChecked && (checkboxtype == CheckBoxType.SHARE)) {
                                // share data
                                shareDate.put(new Integer(id), folderName);
                            } else if (!isChecked && (checkboxtype == CheckBoxType.SHARE)) {
                                // delete share data
                                shareDate.remove(new Integer((id)));
                            } else if (isChecked && (checkboxtype == CheckBoxType.UNSHARE)) {
                                // unshare share data
                                unshareDate.put(new Integer(id), folderName);
                            } else if (!isChecked && (checkboxtype == CheckBoxType.UNSHARE)) {
                                // delete unshare share data
                                unshareDate.remove(new Integer(id));
                            }
                        }
                    });
                    list_data.setAdapter(lo0yaltyCardAdaper);
                } else {
                    /*MessageDialog.showDialog(TransferGiftCards_FriendListActivity.this, serverresponse.get(0).getResponseMessage(), false);*/
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(error.getUrl(), error.getMessage());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(TransferGiftCards_FriendListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }

//    private void getAllPhysicalCard(){
//        String userId = String.valueOf(CommonData.getUserData(this).getId());
//        LoadingBox.showLoadingDialog(TransferGiftCards_FriendListActivity.this, "Loading...");
//        RestClient.getApiServiceForPojo().GetPhysicalCardByUserId(userId, "0", new Callback<JsonElement>() {
//            @Override
//            public void success(JsonElement jsonElement, Response response) {
//                Log.e("output ==>", jsonElement.toString());
//                Log.e("URL -->", response.getUrl());
//                Type listType = new TypeToken<List<Merchant>>() {
//                }.getType();
//                Gson gson = new Gson();
//                LoadingBox.dismissLoadingDialog();
//                serverresponse = gson.fromJson(jsonElement.toString(), new TypeToken<List<ResponseToGetLoyaltyCard>>() {
//                }.getType());
//
//
//                boolean flag = Utility.isResponseValid(serverresponse);
//                for (ResponseToGetLoyaltyCard r : serverresponse) {
//                    ResponseToGetLoyaltyCardModel m = new ResponseToGetLoyaltyCardModel();
//                    m.setId(r.getId());
//                    m.setBackImage(r.getBackImage());
//                    m.setFolderName(r.getFolderName());
//                    m.setCountry(r.getCountry());
//                    m.setFrontImage(r.getFrontImage());
//                    m.setActualUserId(r.getActualUserId());
//                    m.setLogoImage(r.getLogoImage());
//                    m.setMerchantId(r.getMerchantId());
//                    m.setIsChecked(false);
//                    temp.add(m);
//                }
//                if (flag) {
//                    list_data.setAdapter(new NonKippinGiftCardListAdapter(temp, TransferGiftCards_FriendListActivity.this, false, new OnSelectionChange() {
//                        @Override
//                        public void onSelectionChanged(int position, boolean isChecked) {
//
//                        }
//                    }));
//                } else {
//                    MessageDialog.showDialog(TransferGiftCards_FriendListActivity.this, serverresponse.get(0).getResponseMessage(), false);
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.e(error.getUrl(), error.getMessage());
//                LoadingBox.dismissLoadingDialog();
//            }
//        });
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if(intent!=null){
            // NEED TO CHANGE
            parentButton = intent.getStringExtra("parentButton");
//            if(parentButton.equals("tradeWithFriends")){
//            //    makeFriendListToTrade();
//                setLogoText("KIPPIN FRIENDS");
//            }
//            else if(parentButton.equals("transferGiftcard")){
//           //     makeFriendList();
//            }



        }
        Utils.hideKeyboard(this);
    }
    //MAKE AcceptedFriendListForTradePoint
    private void makeFriendListToTrade(){
        Log.e("customerID -->", customerID);
        LoadingBox.showLoadingDialog(TransferGiftCards_FriendListActivity.this,"Loading...");
        RestClient.getApiServiceForPojo().GetAcceptedFriendListForTradePoint(customerID, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output -->", jsonElement.toString());
                Log.e("url -SUCCESS->", response.getUrl());
                Type listtype = new TypeToken<List<FriendDetail>>() {
                }.getType();
                Gson gson = new Gson();
                friendDetails = (List<FriendDetail>) gson.fromJson(jsonElement.toString(), listtype);
                if (friendDetails != null) {
                    if (friendDetails.size() == 1 && !friendDetails.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(TransferGiftCards_FriendListActivity.this, "No friend invited by you.");
                    } else {
                        adapter = new FriendListAdapter(friendDetails, TransferGiftCards_FriendListActivity.this);
                        list_data.setAdapter(adapter);
                    }

                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("url -FAILURE->", error.getUrl());
                MessageDialog.showDialog(TransferGiftCards_FriendListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }
    private void makeFriendList()
    {
        Log.e("customerID -->", customerID);
        LoadingBox.showLoadingDialog(TransferGiftCards_FriendListActivity.this,"Loading...");
        RestClient.getApiServiceForPojo().GetFriendList(customerID, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output -->", jsonElement.toString());
                Log.e("url -SUCCESS->", response.getUrl());
                Type listtype = new TypeToken<List<FriendDetail>>() {
                }.getType();
                Gson gson = new Gson();
                friendDetails = (List<FriendDetail>) gson.fromJson(jsonElement.toString(), listtype);
                if (friendDetails != null) {
                    if (friendDetails.size() == 1 && !friendDetails.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(TransferGiftCards_FriendListActivity.this, "No friend invited by you.");
                    } else {
                        adapter = new FriendListAdapter(friendDetails, TransferGiftCards_FriendListActivity.this);
                        list_data.setAdapter(adapter);
                    }

                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("url -FAILURE->", error.getUrl());
                MessageDialog.showDialog(TransferGiftCards_FriendListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });

    }
    @Override
    public void onClick(View v) {
        Log.e("","===============");
        int i =0;
        switch (v.getId())
        {
            case R.id.btn_upload:
                handleButtonClick();
                break;

        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Log.e("Inside on click", parentButton);
        if(parentButton.equals("transferGiftcard")) {
            Intent i = new Intent(TransferGiftCards_FriendListActivity.this, TransferGiftCard_MerchantListActivity.class);
            String friendId = friendDetails.get(position).getId();
            i.putExtra("friendId", friendId);
            i.putExtra("parentButton","transferGiftcard");
            startActivity(i);
            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }else if(parentButton.equals("tradeWithFriends")){
            Intent i = new Intent(TransferGiftCards_FriendListActivity.this, EnableMerchantVoucherActivity.class);
            String friendId = friendDetails.get(position).getId();
            i.putExtra("friendId", friendId);
            i.putExtra("parentButton","tradeWithFriends");
            startActivity(i);
            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }
    }
    private void handleButtonClick() {
        if(shareType == ShareType.LOYALTY){
            if(shareDate.size()>0){
                shareLoyaltyCard();
            }else if(unshareDate.size()>0){
                unShareLoyaltyCard();
            }
            else if(shareDate.size()==0 && unshareDate.size()==0){
                MessageDialog.showDialog(this,"Please select any physical card",false);

            }

        }
        else if(shareType == ShareType.GIFTCARD){

            shareGiftCard();
        }
        else if(shareType == ShareType.TRADEPOINT){
            shareTradeCard();
        }
    }

    private void shareLoyaltyCard(){
        Set<Integer> set  =  shareDate.keySet();
        StringBuilder cardId = new StringBuilder();
        StringBuilder folderName = new StringBuilder();
        for(Integer key : set) {
            cardId.append(String.valueOf(key.intValue()));
            cardId.append(",");
            folderName.append(shareDate.get(key));
            folderName.append(",");
        }
        cardId.deleteCharAt(cardId.length() - 1);
        folderName.deleteCharAt(folderName.length()-1);

        LoadingBox.showLoadingDialog(TransferGiftCards_FriendListActivity.this, "Loading...");
        String userId = String.valueOf(CommonData.getUserData(this).getId());
        String friendId = getIntent().getStringExtra("friendId");
        RestClient.getApiServiceForPojo().transferLoyaltyCard(userId, friendId, cardId.toString(), folderName.toString(), "", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output -->", jsonElement.toString());
                Log.e("url -SUCCESS->", response.getUrl());
                Type listtype = new TypeToken<ResponseModal>() {
                }.getType();
                Gson gson = new Gson();
                ResponseModal responseToShareLoyaltyCard = (ResponseModal) gson.fromJson(jsonElement.toString(), listtype);

                if (responseToShareLoyaltyCard != null) {

                    if (unshareDate.size() > 0) {
                        unShareLoyaltyCard();
                    } else {
                        LoadingBox.dismissLoadingDialog();
                        MessageDialog.showDialog(TransferGiftCards_FriendListActivity.this, "Your Non Kippin loyalty card has been shared with your selected friends", true);
                    }
                }

            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("url -FAILURE->", error.getUrl());
                MessageDialog.showDialog(TransferGiftCards_FriendListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }


    private void unShareLoyaltyCard(){
        Set<Integer> set  =  unshareDate.keySet();
        StringBuilder cardId = new StringBuilder();
        StringBuilder folderName = new StringBuilder();
        for(Integer key : set) {
            cardId.append(String.valueOf(key.intValue()));
            cardId.append(",");
            folderName.append(unshareDate.get(key));
            folderName.append(",");
        }
        cardId.deleteCharAt(cardId.length() - 1);
        folderName.deleteCharAt(folderName.length() - 1);
        String userId = String.valueOf(CommonData.getUserData(this).getId());
        String friendId = getIntent().getStringExtra("friendId");
        if(!LoadingBox.isDialogShowing()){
            LoadingBox.showLoadingDialog(TransferGiftCards_FriendListActivity.this, "Loading...");
        }
        RestClient.getApiServiceForPojo().UnShareLoyaltyCard(userId, friendId, cardId.toString(), folderName.toString(), "", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output -->", jsonElement.toString());
                Log.e("url -SUCCESS->", response.getUrl());
                Type listtype = new TypeToken<ResponseModal>() {
                }.getType();
                Gson gson = new Gson();
                ResponseModal responseToShareLoyaltyCard = (ResponseModal) gson.fromJson(jsonElement.toString(), listtype);
                if (responseToShareLoyaltyCard != null) {
                    if (shareDate.size() > 0) {
                        LoadingBox.dismissLoadingDialog();
                        MessageDialog.showDialog(TransferGiftCards_FriendListActivity.this, "Your Non Kippin loyalty card has been Shared and Unshared with your selected friends", true);
                    } else {
                        LoadingBox.dismissLoadingDialog();
                        MessageDialog.showDialog(TransferGiftCards_FriendListActivity.this, "Your Non Kippin loyalty card has been unshared with your selected friends", true);
                    }


                }

            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("url -FAILURE->", error.getUrl());
                MessageDialog.showDialog(TransferGiftCards_FriendListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }






    private void shareGiftCard(){

    }
    private void shareTradeCard(){

    }


}
