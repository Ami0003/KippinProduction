package com.kippinretail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.AdapterGiftCardsCharity;
import com.kippinretail.ApplicationuUlity.*;
import com.kippinretail.ApplicationuUlity.BarcodeUtil;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftCardList.GiftCard;
import com.kippinretail.Modal.GiftCardModal;
import com.kippinretail.Modal.merchantListByCharity.ModalMerchantList;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by gaganpreet.singh on 4/14/2016.
 */
public class ActivityCharity_GiftCardUsingMerchant extends AbstractListActivity implements AdapterView.OnItemClickListener {

    private BarcodeUtil barcodeUtil;
    public static final String MERCHANT_ID = "MERCHANT_ID";
    private String merchantName = "";
    /*private String merchantId = null;
    private GridView gvGiftCards;



    private View rootView;*/

    private AdapterGiftCardsCharity adapterGiftCardsCharity;
    private String merchantId, userId, businessName;
    private ArrayList<GiftCardModal> giftCardModals;
    private ArrayList<GiftCard> giftCards = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseUI();
        merchantId = getValue(MERCHANT_ID);
        merchantName = getValue("merchantNmae");
        updateToolBar();
        updateUI();
        addListener();
        ;
    }

    @Override
    public void updateToolBar() {
        generateActionBar(R.string.title_purchased_giftcards, true, true, false);
        ;
    }

    @Override
    public void updateUI() {
        barcodeUtil = new BarcodeUtil(this);
        layout_container_search.setVisibility(View.GONE);
        layout_nonKippin.setVisibility(View.GONE);
        layout_txtMercahntName.setVisibility(View.VISIBLE);
        txtMerchantName.setVisibility(View.VISIBLE);

        txtMerchantName.setText(merchantName);
      /*  RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)txtMerchantName.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
        txtMerchantName.setLayoutParams(params);
*/

    }

    @Override
    public void addListener() {
        list_data.setOnItemClickListener(this);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list_data.setVisibility(View.VISIBLE);
             //   Animation animation = AnimationUtils.loadAnimation(ActivityCharity_GiftCardUsingMerchant.this, R.anim.fade_in);
             //   layout_cardDetail.startAnimation(animation);
                layout_cardDetail.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();

    }

    @Override
    public void setUpUI() {
        super.setUpUI();

        //getApiServiceForPojo().GetCharityCardByUserId1(CommonData.getUserData(this).getId() +"", merchantId,getCallback(callback));
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadingBox.showLoadingDialog(this, "Loading");
        getApiServiceForPojo().GetCharityCardByUserId1(merchantId, CommonData.getUserData(this).getId() + "", getCallback(callback));
    }

    public void loadData() {
        adapterGiftCardsCharity = new AdapterGiftCardsCharity(activity, giftCards);
        list_data.setAdapter(adapterGiftCardsCharity);
    }

    Callback<JsonElement> callback = new Callback<JsonElement>() {
        @Override
        public void success(JsonElement jsonElement, Response response) {
            Log.e(jsonElement.toString(), jsonElement.toString());

            Type listType = new TypeToken<ArrayList<GiftCard>>() {
            }.getType();
            giftCards = gson.fromJson(jsonElement.toString(), listType);

            if (Utility.isResponseValid(giftCards)) {
                loadData();
            } else {
                MessageDialog.showDialog(ActivityCharity_GiftCardUsingMerchant.this, giftCards.get(0).getResponseMessage());
            }
            LoadingBox.dismissLoadingDialog();

        }

        @Override
        public void failure(RetrofitError error) {
            MessageDialog.showFailureDialog(ActivityCharity_GiftCardUsingMerchant.this);

            LoadingBox.dismissLoadingDialog();
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        try{
        int i1 = 0;
        RestClient.getApiServiceForPojo().RemoveIsReadTransfer1(giftCards.get(position).getId(),
                giftCards.get(position).getBarcode(), "", new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        Gson gson = new Gson();
                        Log.e(response.getUrl(), response.getUrl());


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        MessageDialog.showFailureDialog(ActivityCharity_GiftCardUsingMerchant.this);
                    }
                });
        //list_data.setAdapter(null);
        list_data.setVisibility(View.GONE);
        View subItemView = (View) list_data.getChildAt(position);


            txt_barcode.setText(giftCards.get(position).getBarcode());
            Drawable drawable = barcodeUtil.generateBarcode(giftCards.get(position).getBarcode());
            iv_barcode.setImageDrawable(drawable);
            layout_cardDetail.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(ActivityCharity_GiftCardUsingMerchant.this, R.anim.fade_in);
            layout_cardDetail.startAnimation(animation);

        }catch(Exception ex){
                Log.e("196-ActivityCharity_GiftCardUsingMerchant",ex.getMessage());
            }



    }
}
