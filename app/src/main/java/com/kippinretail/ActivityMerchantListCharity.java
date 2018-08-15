package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.MerchantListAdapterModifiedImage;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.Modal.merchantListByCharity.ModalMerchantList;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.callbacks.NotificationREveiver;
import com.kippinretail.config.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;

import notification.NotificationHandler;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityMerchantListCharity extends AbstractListActivity implements AdapterView.OnItemClickListener {


    MerchantListAdapterModifiedImage merchantListAdapterModified;
    ArrayList<ModalMerchantList> modalMerchantLists = new ArrayList<>();
    ArrayList<MerchantModal> merchantModals = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_reusable_layout_list);


        updateToolBar();
        updateUI();
        addListener();
    }

    @Override
    public void updateToolBar() {
        generateActionBar(R.string.title_user_gift_Card, true, true, false);
    }

    @Override
    public void updateUI() {

        layout_nonKippin.setVisibility(View.VISIBLE);
        layout_nonKippin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMerchantListCharity.this, ActivityMerchantGiftCardList.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void addListener() {
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (merchantListAdapterModified != null) {
                    merchantListAdapterModified.getFilter().filter(s);
                }
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
        Utils.hideKeyboard(this);
        String uId = CommonData.getUserData(this).getId() + "";
        getApiServiceForPojo().GetCharityMerchantListByUserId1(uId, getCallback(elementCallback));
        NotificationHandler.getInstance().getNotificationForCards(this, new NotificationREveiver() {
            @Override
            public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

            }
        });
    }


    private void loadData() {

        refreshModals();

        merchantListAdapterModified = new MerchantListAdapterModifiedImage(this, merchantModals, false, -1);
        list_data.setAdapter(merchantListAdapterModified);
    }

    private void refreshModals() {

        merchantModals = new ArrayList<>();

        for (int i = 0; i < modalMerchantLists.size(); i++) {

            ModalMerchantList modalMerchantList = modalMerchantLists.get(i);

            MerchantModal merchantModal = new MerchantModal();
            merchantModal.setMerchantid(modalMerchantList.getId());
            merchantModal.setBussinessName(modalMerchantList.getBusinessName());
            merchantModal.setProfileImage(modalMerchantList.getProfileImage());
            merchantModal.setIsRead(modalMerchantList.getIsRead());

            merchantModals.add(merchantModal);
        }

    }


    @Override
    public void setUpListeners() {
        super.setUpListeners();

    }

    Callback<JsonElement> elementCallback = new Callback<JsonElement>() {
        @Override
        public void success(JsonElement jsonElement, Response response) {
            Type listType = new TypeToken<ArrayList<ModalMerchantList>>() {
            }.getType();
            modalMerchantLists = gson.fromJson(jsonElement.toString(), listType);

            if (Utility.isResponseValid(modalMerchantLists)) {
                loadData();
            } else {
                MessageDialog.showDialog(ActivityMerchantListCharity.this, "No gift cards received. ", false);
            }

        }

        @Override
        public void failure(RetrofitError error) {
            MessageDialog.showFailureDialog(ActivityMerchantListCharity.this);
        }
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e(merchantModals.get(position).getMerchantid() + "", merchantModals.get(position).getMerchantid() + "");
        Bundle bundle = new Bundle();
        bundle.putString(ActivityCharity_GiftCardUsingMerchant.MERCHANT_ID, merchantModals.get(position).getMerchantid());
        bundle.putString("merchantNmae", merchantModals.get(position).getBussinessName());
        Utility.enterActivity(this, ActivityCharity_GiftCardUsingMerchant.class, bundle);

        // display All the charity card
    }

}
