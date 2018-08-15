package com.kippinretail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.GiftCardAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.OnSelectionChanged;
import com.kippinretail.Interface.SuperModal;
import com.kippinretail.Modal.GiftCardList.GiftCard;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.PurchasedGiftCardDetail.PurchasedGiftCard;
import com.kippinretail.Modal.ResponseModal;
import com.kippinretail.Modal.ServerResponseModal.ServerResponse;
import com.kippinretail.Modal.VoucherModal;
import com.kippinretail.config.BarCodeGenerator;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GiftCardsActivity extends SuperActivity implements View.OnClickListener,AdapterView.OnItemClickListener{


    private String merchantID ,userId ,friendId,parent;
    private GridView gridViewGiftCards;
    private GiftCardAdapter adapter = null;
    private List<VoucherModal> voucherModals = null;
    private TextView txtMessage;
    private Button buynow;
    private String giftcardPurchaseId;
    private String amount = "";
    private String barcodeToGift = null;
    CheckBox checkBoxPolicy;
    LinearLayout layout_checkBoxPolicy;
    private String giftcardIdToshare;
    private String giftCardId;
    TextView termsConditionsTextView;
    View terms;
    RelativeLayout lalayout_ivBack;
    String termsCond = null;
    private String barcode;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_cards);
        initilization();
        setUpUI();
        addListener();

    }

    @Override
    public void setUpUI() {
        super.setUpUI();


        if(getIntent().getSerializableExtra("shareType") == ShareType.DONATEGIFTCARD){
            generateActionBar(R.string.title_user_SelectMerchantActivity, true, true, false);
            buynow.setText("SEND CARD AS GIFT");
            buynow.setEnabled(true);
            buynow.setBackgroundColor(Color.parseColor("#e7423c"));
            friendId = getIntent().getStringExtra("friendId");
            layout_checkBoxPolicy.setVisibility(
                    View.GONE);
            layout_checkBoxPolicy.setVisibility(View.GONE);
            String merchantId = getIntent().getStringExtra("merchantID");

            getUserGiftCards(merchantId);
        }
        else if(getIntent().getSerializableExtra("shareType") == ShareType.SHAREGIFTCARD){
            generateActionBar(R.string.title_user_SelectMerchantActivity, true, true, false);
            friendId = getIntent().getStringExtra("friendId");
            buynow.setText("SEND CARD AS GIFT");
            buynow.setEnabled(true);
            buynow.setBackgroundColor(Color.parseColor("#e7423c"));
            layout_checkBoxPolicy.setVisibility(View.GONE);
            String merchantId = getIntent().getStringExtra("merchantID");

            getUserGiftCards(merchantId);
        }else{
            Intent i = getIntent();
            if(i!=null) {
                generateActionBar(R.string.title_user_SelectMerchantActivity, true, true, false);
                merchantID = i.getStringExtra("merchantId");
                checkBoxPolicy.setChecked(false);
                buynow.setEnabled(false);
                termsCond = getIntent().getStringExtra("terms");
                buynow.setBackgroundColor(Color.parseColor("#a4a3a3"));
                txtMessage.setText("Please select the gift card you want to purchase");
            }

            getMerchantCards(merchantID);
        }

    }

    private void addListener() {
        buynow.setOnClickListener(this);
        termsConditionsTextView.setOnClickListener(this);
        lalayout_ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(GiftCardsActivity.this , R.anim.translate_down);
                terms.startAnimation(animation);
                terms.setVisibility(View.GONE);
                buynow.setVisibility(View.VISIBLE);

            }
        });
        checkBoxPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    buynow.setEnabled(true);
                    buynow.setBackgroundColor(Color.parseColor("#e7423c"));

                } else {
                    buynow.setEnabled(false);
                    try {
                        buynow.setBackgroundColor(Color.parseColor("#a4a3a3"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
       /* lalayout_ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(GiftCardsActivity.this , R.anim.translate_down);
                terms.startAnimation(animation);
              //  terms.findViewById(R.id.txt_accept).setVisibility(View.GONE);
                terms.setVisibility(View.GONE);
            }
        });*/
    }

    private void initilization()
    {
        gridViewGiftCards = (GridView)findViewById(R.id.gridViewGiftCards);
        terms = findViewById(R.id.terms);
        lalayout_ivBack = (RelativeLayout)terms.findViewById(R.id.lalayout_ivBack);
        txtMessage = (TextView)findViewById(R.id.txtMessage);
        buynow = (Button)findViewById(R.id.buynow);
        checkBoxPolicy = (CheckBox)findViewById(R.id.checkBoxPolicy);
        termsConditionsTextView = (TextView)findViewById(R.id.termsConditionsTextView);
        voucherModals = new ArrayList<VoucherModal>();
        layout_checkBoxPolicy = (LinearLayout)findViewById(R.id.layout_checkBoxPolicy);
        gridViewGiftCards.setOnItemClickListener(this);
        setLogoText("GIFT CARDS");
        String terms_conditions = getString(R.string.terms_conditions);

      //  lalayout_ivBack = (RelativeLayout)terms.findViewById(R.id.lalayout_ivBack);
        SpannableString ss = new SpannableString(terms_conditions);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                //CommonUtility.startNewActivity(GiftCardsActivity.this , ActivityTermsAndConditions.class);
                terms.setVisibility(View.VISIBLE);
                terms.findViewById(R.id.txt_accept).setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(GiftCardsActivity.this,R.anim.translate_up);
                terms.startAnimation(animation);
                WebView w = (WebView)terms.findViewById(R.id.wv_terms);
                w.loadData( termsCond,"text/html", "UTF-8");
                buynow.setVisibility(View.GONE);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 26, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsConditionsTextView.setText(ss);
        termsConditionsTextView.setMovementMethod(LinkMovementMethod.getInstance());
        termsConditionsTextView.setLinkTextColor(Color.parseColor("#3195DB"));


    }
    @Override
    protected void onResume()
    {
        super.onResume();



    }

    // GET GIFT CARDS W.R.T TO ERCHANT ID THEN PURCHASE IT
    private void getMerchantCards(String merchantID){
        voucherModals.clear();
        int i = 0;
        String userId = String.valueOf(CommonData.getUserData(GiftCardsActivity.this).getId());
        LoadingBox.showLoadingDialog(GiftCardsActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().GetGiftcardListByMerchantId(merchantID, userId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                LoadingBox.dismissLoadingDialog();
                Log.e("Gift Card with bar code", "Gift Card with bar code");
                Log.e("OutPut-->", jsonElement.toString());
                Log.e("URL", response.getUrl());
                Type listtype = new TypeToken<List<GiftCard>>() {
                }.getType();
                Gson gson = new Gson();
                final List<GiftCard> giftCards = (List<GiftCard>) gson.fromJson(jsonElement.toString(), listtype);

                if (giftCards != null) {
                    if (giftCards.size() == 1 && !giftCards.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(GiftCardsActivity.this, "No gift cards available for this merchantId.");
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
                        adapter = new GiftCardAdapter(GiftCardsActivity.this, voucherModals, false, new OnSelectionChanged() {
                            @Override
                            public void onSelectionChanged(int position, SuperModal superAdapter, boolean isChecked) {

                                generateBarcode(voucherModals.get(position).getGiftId());
                                if (isChecked) {
                                    amount = voucherModals.get(position).getPrice();
                                    giftCardId =      giftcardIdToshare = voucherModals.get(position).getGiftId() ;
                                    barcode = voucherModals.get(position).getBarcode();
                                    CommonUtility.GiftCardDetails = new Gson().toJson(giftCards.get(position));
                                    Log.e(giftcardIdToshare,""+giftcardIdToshare);
                                } else {
                                    amount = "";
                                    giftcardPurchaseId = "";
                                }


                            }
                        });
                        gridViewGiftCards.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Error Comes ", error.getMessage());

                MessageDialog.showFailureDialog(GiftCardsActivity.this);

            }
        });
    }

    // GET GIFT CARDS W.R.T TO USER AND SHARE IT
    private void getUserGiftCards(String merchantID){
        voucherModals.clear();
        String userId = String.valueOf(CommonData.getUserData(GiftCardsActivity.this).getId());
        LoadingBox.showLoadingDialog(GiftCardsActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().GetMyPurchasedGiftcardListByMerchantId(merchantID, userId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Gift Card with bar code", "Gift Card with bar code");
                Log.e("OutPut-->", jsonElement.toString());
                Log.e("URL", response.getUrl());
                Type listtype = new TypeToken<List<GiftCard>>() {
                }.getType();
                Gson gson = new Gson();
                List<GiftCard> giftCards = (List<GiftCard>) gson.fromJson(jsonElement.toString(), listtype);

                if (giftCards != null) {
                    if (giftCards.size() == 1 && !giftCards.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(GiftCardsActivity.this, "No gift cards available for this merchantId.");
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
                            Log.e("giftCard.getId()",giftCard.getId()+"");
                        }
                        adapter = new GiftCardAdapter(GiftCardsActivity.this, voucherModals, false, new OnSelectionChanged() {
                            @Override
                            public void onSelectionChanged(int position, SuperModal superAdapter, boolean isChecked) {

                                generateBarcode(voucherModals.get(position).getGiftId());

                                if (isChecked) {
                                    amount = voucherModals.get(position).getPrice();
                                    giftCardId =      giftcardIdToshare = voucherModals.get(position).getGiftId() ;
                                    barcode = voucherModals.get(position).getBarcode();
                                  Log.e(giftcardIdToshare,""+giftcardIdToshare);
                                } else {
                                    amount = "";
                                    giftcardPurchaseId = "";
                                }

                            }
                        });
                        gridViewGiftCards.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Error Comes ", error.getMessage());
                MessageDialog.showFailureDialog(GiftCardsActivity.this);

            }
        });
    }


    private void sendKippinGiftCard(){
        //{"ResponseCode":2,"UserId":0,"ResponseMessage":"Unable to transfer giftcard. Please try again later"}
//http://kippinretail.web1.anzleads.com.kippinretailApi/GiftCard/TransferGiftCard/33/3/27
       String userId = String.valueOf(CommonData.getUserData(this).getId());
        try {
            LoadingBox.showLoadingDialog(GiftCardsActivity.this, "Loading...");
            RestClient.getApiServiceForPojo().TransferGiftCard(userId,friendId,barcode,"", new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("output ==>", jsonElement.toString());
                    Log.e("URL -->", response.getUrl());
                    Type listType = new TypeToken<List<Merchant>>() {
                    }.getType();
                    Gson gson = new Gson();
                    LoadingBox.dismissLoadingDialog();
                    ResponseModal responseModal = gson.fromJson(jsonElement.toString(), ResponseModal.class);
                    if(responseModal.getResponseCode().equals("1")){
                        if(getIntent().getSerializableExtra("shareType") == ShareType.DONATEGIFTCARD) {
                            MessageDialog.showDialog(GiftCardsActivity.this, "Gift Card has been successfully transferred", ActivityDonateToCharity.class, ShareType.DONATEGIFTCARD);
                        }else if(getIntent().getSerializableExtra("shareType") == ShareType.SHAREGIFTCARD){
                            MessageDialog.showDialog(GiftCardsActivity.this, "Gift Card has been successfully transferred", FriendListActivity.class, ShareType.SHAREGIFTCARD);
                        }
                    }else{
                        MessageDialog.showDialog(GiftCardsActivity.this,responseModal.getResponseMessage(),false);
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(error.getUrl(), error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showFailureDialog(GiftCardsActivity.this);
                }
            });

        }catch (Exception ex)
        {
            Log.e("608",ex.getMessage());
        }
    }


//    private void getGiftCards()
//    {
//        voucherModals.clear();
//        LoadingBox.showLoadingDialog(GiftCardsActivity.this, "Loading...");
//    userId = String.valueOf(CommonData.getUserData(this).getId());
//        RestClient.getApiServiceForPojo().GetMyPurchasedGiftcardListByMerchantId(merchantID, userId, new Callback<JsonElement>() {
//            @Override
//            public void success(JsonElement jsonElement, Response response) {
//                Log.e("URL", response.getUrl());
//                Type listtype = new TypeToken<List<GiftCard>>() {
//                }.getType();
//                Gson gson = new Gson();
//                List<GiftCard> giftCards = (List<GiftCard>) gson.fromJson(jsonElement.toString(), listtype);
//                LoadingBox.dismissLoadingDialog();
//                if (giftCards != null) {
//                    if (giftCards.size() == 1 && !giftCards.get(0).getResponseMessage().equals("Success")) {
//                        MessageDialog.showDialog(GiftCardsActivity.this, "No gift cards available for this merchantId.");
//                    } else {
//                        for (GiftCard giftCard : giftCards) {
//                            VoucherModal modal = new VoucherModal();
//                            modal.setUrl(giftCard.getGiftcardImage());
//                            modal.setBarcode(giftCard.getBarcode());
//                            modal.setMessage(giftCard.getResponseMessage());
//                            modal.setPrice(giftCard.getPrice());
//                            modal.setIscheched(false);
//                            modal.setGiftId(giftCard.getId());
//                            voucherModals.add(modal);
//                        }
//                        adapter = new GiftCardAdapter(GiftCardsActivity.this, voucherModals, false, new OnSelectionChanged() {
//                            @Override
//                            public void onSelectionChanged(int position, SuperModal superAdapter, boolean isChecked) {
//
//                                generateBarcode(voucherModals.get(position).getGiftId());
//
//                                if (isChecked) {
//                                    amount = voucherModals.get(position).getPrice();
//                                    barcodeToGift =voucherModals.get(position).getBarcode();
//                                } else {
//                                    amount = "";
//                                    giftcardPurchaseId = "";
//                                }
//
//                            }
//                        });
//                        gridViewGiftCards.setAdapter(adapter);
//                    }
//                }
//
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                LoadingBox.dismissLoadingDialog();
//                Log.e("Error Comes ", error.getMessage());
//
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buynow :

                if(getIntent().getSerializableExtra("shareType") == ShareType.DONATEGIFTCARD){
                    if(amount!=null && !amount.equals("")){
                        sendKippinGiftCard();
                    }else{
                        MessageDialog.showDialog(this, "Please select any giftcard", false);
                    }

                }
                else if(getIntent().getSerializableExtra("shareType") == ShareType.SHAREGIFTCARD){
                    if(amount!=null && !amount.equals("")){
                        sendKippinGiftCard();
                    }else{
                        MessageDialog.showDialog(this, "Please select any giftcard", false);
                    }
                }else{
                    if(amount!=null && !amount.equals("")){
                        CreatePurchaseGiftCardBarcode();
                    }else{

                        MessageDialog.showDialog(this, "Please select any giftcard", false);
                    }

                }
                break;
            case R.id.termsConditionsTextView:
                terms.setVisibility(View.VISIBLE);
                terms.findViewById(R.id.txt_accept).setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.translate_up);
                terms.startAnimation(animation);
                WebView w = (WebView)terms.findViewById(R.id.wv_terms);
                w.loadData( termsCond,"text/html", "UTF-8");
                buynow.setVisibility(View.GONE);
               // CommonUtility.startNewActivity(this , ActivityTermsAndConditions.class);
                break;
        }
    }
    private void giftGiftcard()
    {
        LoadingBox.showLoadingDialog(GiftCardsActivity.this, "Loading...");
     userId = String.valueOf(CommonData.getUserData(this).getId());
        RestClient.getApiServiceForPojo().TransferGiftCard(userId, friendId, barcodeToGift, "",new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("Output-->", jsonElement.toString());
                Log.e("URL-->", response.getUrl());
                Gson gsonj = new Gson();
                Type listtype = new TypeToken<ServerResponse>() {
                }.getType();
                ServerResponse serverResponse = gson.fromJson(jsonElement.toString(), listtype);
                if(serverResponse!=null){
                    Log.d("======", "" + serverResponse.getResponseCode().equals("Success."));
                    if (serverResponse.getResponseMessage().equals("Success.")){
                        MessageDialog.showDialog(GiftCardsActivity.this,"Gift Card has been successfully transfered",true);
                    }
                    else
                    {
                        MessageDialog.showDialog(GiftCardsActivity.this,"Fail to transfer gift card Try again Later",true);
                    }
                }

                LoadingBox.dismissLoadingDialog();;
            }
            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();;
                Log.e("URL-->", error.getUrl());
                MessageDialog.showFailureDialog(GiftCardsActivity.this);
            }
        });

    }


    // CREATE PURCHASE GIFTCARD ID BEFORE PURCHASDING OF GIFTCARD
    private void CreatePurchaseGiftCardBarcode(){
//        String giftId=null;
//        StringBuffer responseText = new StringBuffer();
//        List<VoucherModal> rowitem = adapter._voucherModals;
//        for(int i = 0 ; i<rowitem.size() ; i++)
//        {
//            if (rowitem.get(i).ischeched())
//            {
//                giftId = rowitem.get(i).getGiftId();
//                amount = rowitem.get(i).getPrice();
//                break;
//            }
//        }
        userId = String.valueOf(CommonData.getUserData(this).getId());
       String giftcardBarcode = BarCodeGenerator.generateBarCode(merchantID, userId, giftCardId);
        CommonUtility.BarCodeValue = BarCodeGenerator.generateBarCode(merchantID, userId, giftCardId);
        LoadingBox.showLoadingDialog(GiftCardsActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().CreatePurchaseGiftCardBarcode(userId, giftCardId, giftcardBarcode, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output-->", jsonElement.toString());
                Log.e("URL-->", response.getUrl());
                Type listtype = new TypeToken<PurchasedGiftCard>() {
                }.getType();
                Gson gson = new Gson();
                PurchasedGiftCard purchasedGiftCard = (PurchasedGiftCard) gson.fromJson(jsonElement.toString(), listtype);
                if (purchasedGiftCard != null) {
                    if (purchasedGiftCard.getResponseMessage().equals("Giftcard barcode successfully generated.")) {
                        giftcardPurchaseId = purchasedGiftCard.getUserId();
                        purchaseGiftCard();
                    }
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showFailureDialog(GiftCardsActivity.this);
            }
        });

    }

    private String generateBarcode(String giftCardId){
        String barCode;
        Random r = new Random();
        barCode = merchantID+CommonData.getUserData(this).getId()+giftCardId+String.valueOf(100000 + r.nextInt(900000));
        giftcardPurchaseId = barCode;
        return  barCode;
    }
    private  void purchaseGiftCard()
    {
        if(amount!=null && !amount.equals("")){
            Intent i = new Intent();
            i.setClass(GiftCardsActivity.this, PaymentActivity.class);
            i.putExtra("merchantID", merchantID);
            i.putExtra("giftcardPurchaseId",giftcardPurchaseId);
            i.putExtra("giftId",giftCardId);
            i.putExtra("amount",amount);
            startActivity(i);
            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }
        else{
            MessageDialog.showDialog(this,"Please select any giftcard",false);
        }

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Log.e("================","===================");
    }
}
