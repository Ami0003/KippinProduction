package com.kippinretail;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.AdapterDonatePoints;
import com.kippinretail.Adapter.MerchantListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.OnSelectionChanged;
import com.kippinretail.Interface.SuperModal;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.Modal.Modal_PostTradePoint.DataToTradePoint;
import com.kippinretail.Modal.Modal_PostTradePoint.ObjList;
import com.kippinretail.Modal.ResponseModal;
import com.kippinretail.Modal.merchantListByCharity.ModalMerchantList;
import com.kippinretail.callbacks.CommonCallback;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

public class ActivityMerchantList extends AbstractListActivity implements CommonCallback, AdapterView.OnItemClickListener, View.OnClickListener, OnSelectionChanged {

    boolean isCountryNeeded = false;
    String mUserId = null;
    HashMap<Integer, MerchantModal> merchantModals = new HashMap<>();
    private List<ModalMerchantList> list;
    private MerchantListAdapter adapterDonateToCharity = null;
    private TextView tvDonatePoints;
    private AdapterDonatePoints adapterPoints;
    private int pointToDonate;
    private String merchantId, friendId, loyalitybarcode;
    private String mCountry;
    private ShareType shareType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserId = String.valueOf(CommonData.getUserData(ActivityMerchantList.this).getId());
        isCountryNeeded = getIntent().getBooleanExtra(getString(R.string.country), false);
        updateUI();
        addListener();
        updateToolBar();

        if (isCountryNeeded) {
            tvDonatePoints.setVisibility(View.VISIBLE);
            tvDonatePoints.setOnClickListener(this);
        }
    }

    @Override
    public void updateToolBar() {
        generateActionBar(R.string.merchants_list, true, true, false);
    }

    @Override
    public void updateUI() {
        shareType = (ShareType) getIntent().getSerializableExtra("shareType");
        if (shareType == ShareType.TRADEPOINT) {
            list_data.setDivider(new ColorDrawable(getResources().getColor(R.color.grey)));
            layout_nonKippin.setVisibility(View.GONE);
            layout_container_search.setVisibility(View.GONE);
            btn_upload.setVisibility(View.VISIBLE);
           // btn_upload.setText("Trade Points");

            btn_upload.setOnClickListener(this);
        } else if (shareType == ShareType.DONATEPOINT) {
            layout_nonKippin.setVisibility(View.GONE);
            layout_container_search.setVisibility(View.GONE);
            btn_upload.setVisibility(View.VISIBLE);
            //btn_upload.setText("Donate Points");
           // btn_upload.setBackgroundResource();
            btn_upload.setOnClickListener(this);
        } else if (shareType == ShareType.DONATEGIFTCARD) {

        }

    }

    @Override
    public void addListener() {
        list_data.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        if (i != null) {
            friendId = i.getStringExtra("USER");
        }
        shareType = (ShareType) getIntent().getSerializableExtra("shareType");

        Log.e("", String.valueOf(CommonData.getUserData(this).getId()));
        makeMerchantListToTradePoint();
        Utils.hideKeyboard(this);
    }

    private void makeMerchantListToTradePoint() {

        Log.e("makeMerchantListToTradePoint", "makeMerchantListToTradePoint");
        LoadingBox.showLoadingDialog(this, "Loading...");
        RestClient.getApiServiceForPojo().GetMerchantWithPoints("india", CommonData.getUserData(ActivityMerchantList.this).getId() + "", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("Output       ==>", jsonElement.toString());
                Log.e("URL", response.getUrl());
                Type listType = new TypeToken<List<ModalMerchantList>>() {
                }.getType();
                Gson gson = new Gson();
                list = (List<ModalMerchantList>) gson.fromJson(jsonElement.toString(), listType);
                if (list != null) {
                    if (list.size() == 1 && !list.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(ActivityMerchantList.this, list.get(0).getResponseMessage(), UserTradePointActivity.class);
                    } else {

                        ArrayList<MerchantModal> merchantModals = new ArrayList<MerchantModal>();
                        for (int i = 0; i < list.size(); i++) {
//                    for (int i = 0; i < 10 ; i++) {
                            MerchantModal merchantModal = new MerchantModal();
                            merchantModal.setMerchantid(list.get(i).getId());
                            merchantModal.setBussinessName(list.get(i).getBusinessName());
                            merchantModal.setPoints(list.get(i).getMerchantPoint());
                            merchantModal.setLoyalityBarcode(list.get(i).getLoyalityBarcode());
                            merchantModals.add(merchantModal);
                        }
                        adapterPoints = new AdapterDonatePoints(ActivityMerchantList.this, merchantModals, true, ActivityMerchantList.this);
                        list_data.setAdapter(adapterPoints);


                    }
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showFailureDialog(ActivityMerchantList.this);

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(this,ActivityGiftCardCharity.class) ;
//        intent.putExtra(this.getResources().getString(R.string.user), mUserId) ;
//        intent.putExtra(this.getResources().getString(R.string.merchant), list.get(position).getId()) ;
//        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upload:
                if (merchantModals.size() > 0) {
                    donate();
                } else {
                    MessageDialog.showDialog(this, "Please provide data for trading points.", UserTradePointActivity.class);
                }

                break;
        }
    }

    private void donate() {

        ObjList objList = new ObjList();
        objList.setPoints(pointToDonate);
        objList.setFriendId(Integer.parseInt(friendId));
        objList.setMerchantId(Integer.parseInt(merchantId));
        objList.setUserId(Integer.parseInt(mUserId));
        objList.setLoyalityBarCode(loyalitybarcode);
        List<ObjList> objLists = new ArrayList<ObjList>();
        objLists.add(objList);
        DataToTradePoint dataToTradePoint = new DataToTradePoint();
        dataToTradePoint.setObjListList(objLists);
        TypedInput in = Utils.getTypedInput(dataToTradePoint);


        RestClient.getApiServiceForPojo().TradePoints(in, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("success", jsonElement.toString());
                Log.d("URL", response.getUrl());
                Gson gson = new Gson();
                ResponseModal responseModal = gson.fromJson(jsonElement, ResponseModal.class);
                if (responseModal.getResponseMessage().equals("Success.")) {
                    MessageDialog.showDialog(ActivityMerchantList.this, "Points has been successfully transferred", UserTradePointActivity.class);
                } else {
                    MessageDialog.showDialog(ActivityMerchantList.this, "Sorry Fail to Donate Try Again", UserTradePointActivity.class);
                }


            }

            @Override
            public void failure(RetrofitError error) {
                MessageDialog.showFailureDialog(ActivityMerchantList.this);
            }
        });
    }

    @Override
    public void onSelectionChanged(int position, SuperModal superAdapter, boolean isChecked) {
        MerchantModal merchantModal = (MerchantModal) superAdapter;
        int id = Integer.parseInt(merchantModal.getMerchantid());
        if (isChecked) {
            double temp = Double.parseDouble(merchantModal.getPoints());
            pointToDonate = (int) temp;
            merchantId = merchantModal.getMerchantid();
            loyalitybarcode = merchantModal.getLoyalityBarcode();
            merchantModals.put(id, merchantModal);
        } else {
            merchantModals.remove(id);
        }
    }


}
