package com.kippinretail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.kippinretail.Adapter.GiftCardListAdapter1Image;
import com.kippinretail.ApplicationuUlity.AppStatus;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.DbNew;
import com.kippinretail.ApplicationuUlity.GiftCardType;
import com.kippinretail.ApplicationuUlity.NonKippinCardType;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftCardMerchantList.MerchantDetail;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.callbacks.NotificationREveiver;
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

public class GiftCardListActivity extends AbstractListActivity implements View.OnClickListener, AdapterView.OnItemClickListener, NotificationREveiver {

    ImageView iv_star;
    Bitmap profileBitmap = null;
    List<MerchantDetail> getListCards;
    ArrayList<MerchantDetail> responseToGetLoyaltyCardModels;
    int downloadedRows = 0;
    private GiftCardListAdapter1Image adapter = null;
    private List<MerchantDetail> responsemerchantDetails;
    private List<MerchantDetail> listCards;
    private String friendId;
    private GiftCardListAdapter1Image adapter_db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        responseToGetLoyaltyCardModels = new ArrayList<>();
        responseToGetLoyaltyCardModels.clear();
        getListCards = new ArrayList<>();
        getListCards.clear();
        Log.e("GiftCardListActivity","GiftCardListActivity");
        updateToolBar();
        updateUI();
        addListener();
    }

    @Override
    public void updateToolBar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(this);
        NotificationHandler.getInstance().getNotificationForCards(this, this);

    }

    // 1) List of Merchants for KIPPIN LOYALTY CARD -->
    @Override
    public void updateUI() {
        iv_star = (ImageView) findViewById(R.id.iv_star);
        layout_nonKippin.setVisibility(View.VISIBLE);
        if (getIntent().getSerializableExtra("shareType") == ShareType.DONATEGIFTCARD) {
            friendId = getIntent().getStringExtra(getString(R.string.user));
            generateActionBar(R.string.title_user_GiftCardListActivity1, true, true, false);
            makeMyGiftCardList();
        } else if (getIntent().getSerializableExtra("shareType") == ShareType.SHAREGIFTCARD) {
            friendId = getIntent().getStringExtra(getString(R.string.user));
            generateActionBar(R.string.title_user_GiftCardListActivity1, true, true, false);
            makeMyGiftCardList();
        } else {
            generateActionBar(R.string.title_user_GiftCardListActivity, true, true, false);
            if (AppStatus.getInstance(activity).isOnline(activity)) {
                makeMyGiftCardList();
            } else {
                new LoadingDataFromDataBase().execute();
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        CommonUtility.hideKeyboard(this);
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        Log.e("char-------Text:", "" + charText);
        getListCards.clear();
        if (charText.length() == 0) {
            Log.e("NONKIPPIN-----: ", "" + responseToGetLoyaltyCardModels.size());
            getListCards.addAll(responseToGetLoyaltyCardModels);
        } else {
            for (MerchantDetail wp : responseToGetLoyaltyCardModels) {
               // Log.e("getUsername: ",""+wp.getUsername().toLowerCase(Locale.getDefault()));
                if (wp.getUsername() != null) {
                    if (wp.getBusinessName().toLowerCase(Locale.getDefault()).contains(charText.toLowerCase(Locale.getDefault()))) {
                        getListCards.add(wp);
                    }
                }

            }
        }
        if (adapter != null) {
            adapter = new GiftCardListAdapter1Image(getListCards, GiftCardListActivity.this);
            list_data.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            adapter_db = new GiftCardListAdapter1Image(getListCards, GiftCardListActivity.this);
            list_data.setAdapter(adapter_db);
            adapter_db.notifyDataSetChanged();
        }


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
                    if ("My Non KIPPIN Gift Cards".toLowerCase().contains(s.toString().toLowerCase())) {
                        iv_nonKippin.setVisibility(View.VISIBLE);
                        txtNonKippin.setVisibility(View.VISIBLE);
                        undline_nonKippin.setVisibility(View.VISIBLE);
                    } else {
                        iv_nonKippin.setVisibility(View.GONE);
                        txtNonKippin.setVisibility(View.GONE);
                        undline_nonKippin.setVisibility(View.GONE);
                    }
                } else {
                    String text = txtSearch.getText().toString().toLowerCase(Locale.getDefault());
                    filter(text);
                    if ("My Non KIPPIN Gift Cards".toLowerCase().contains(s.toString().toLowerCase())) {
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
        list_data.setOnItemClickListener(this);
    }


    private void makeMyGiftCardList() {
        if (CommonData.getUserData(GiftCardListActivity.this) != null) {
            String userId = String.valueOf(CommonData.getUserData(GiftCardListActivity.this).getId());
            LoadingBox.showLoadingDialog(GiftCardListActivity.this, "Loading...");
            RestClient.getApiServiceForPojo().MyGiftcardMerchantList(userId, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("--------Output -->", jsonElement.toString());
                    Log.e("Url--------", response.getUrl());
                    // USE GIFT CARD MERCHANT LIST MODAL
                    Type listtype = new TypeToken<List<MerchantDetail>>() {
                    }.getType();
                    Gson gson = new Gson();

                    responsemerchantDetails = (List<MerchantDetail>) gson.fromJson(jsonElement.toString(), listtype);
                    boolean flag = Utility.isResponseValid(responsemerchantDetails);
                    DbNew.getInstance(GiftCardListActivity.this).DeleteAllGiftMerchant();

                    if (flag) {

                        getListCards.clear();
                        getListCards = responsemerchantDetails;
                        responseToGetLoyaltyCardModels.clear();
                        responseToGetLoyaltyCardModels.addAll(responsemerchantDetails);
                        adapter = new GiftCardListAdapter1Image(responsemerchantDetails, GiftCardListActivity.this);
                        list_data.setAdapter(adapter);
                        downLoadingImages();
                        LoadingBox.dismissLoadingDialog();
                    } else {
                        LoadingBox.dismissLoadingDialog();
                        // MessageDialog.showDialog(GiftCardListActivity.this, responsemerchantDetails.get(0).getResponseMessage(), false);
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("============", error.getMessage());
                    LoadingBox.dismissLoadingDialog();

                    MessageDialog.showFailureDialog(GiftCardListActivity.this);
                }
            });
        }
    }

    private void downLoadingImages() {

        if (responsemerchantDetails.size() > 0) {
            new ImageDownloader(this, responsemerchantDetails).execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_nonKippin:
                Intent i = new Intent();
                if (getIntent().getSerializableExtra("shareType") == ShareType.DONATEGIFTCARD) {
                    i.setClass(this, NonKippinGiftCardListActivity.class);
                    i.putExtra("friendId", friendId);
                    i.putExtra("shareType", ShareType.DONATEGIFTCARD);
                } else if (getIntent().getSerializableExtra("shareType") == ShareType.SHAREGIFTCARD) {
                    i.setClass(this, NonKippinGiftCardListActivity.class);
                    i.putExtra("friendId", friendId);
                    i.putExtra("shareType", ShareType.SHAREGIFTCARD);
                } else {
                    i.setClass(this, NonKippinGiftCardListActivity.class);
                    i.putExtra("NonKippinCardType", NonKippinCardType.GIFTCARD);
                }


                startActivity(i);
                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent();
        if (getIntent().getSerializableExtra("shareType") == ShareType.DONATEGIFTCARD) {
            i.setClass(this, GiftCardsActivity.class);
            i.putExtra("merchantID", responsemerchantDetails.get(position).getId());
            i.putExtra("friendId", getIntent().getStringExtra(getString(R.string.user)));
            i.putExtra("shareType", ShareType.DONATEGIFTCARD);
        } else if (getIntent().getSerializableExtra("shareType") == ShareType.SHAREGIFTCARD) {
            i.setClass(this, GiftCardsActivity.class);
            i.putExtra("friendId", getIntent().getStringExtra(getString(R.string.user)));
            i.putExtra("merchantID", responsemerchantDetails.get(position).getId());
            i.putExtra("shareType", ShareType.SHAREGIFTCARD);
        } else {
            if (AppStatus.getInstance(GiftCardListActivity.this).isOnline(GiftCardListActivity.this)) {
                i.setClass(this, PurchasedGiftcardsActivity.class);
                i.putExtra("merchantId", responsemerchantDetails.get(position).getId());
                i.putExtra("businessName", responsemerchantDetails.get(position).getBusinessName());
                i.putExtra("GiftCardType", GiftCardType.MYGIFTCARD);
            } else {
                i.setClass(this, PurchasedGiftcardsActivity.class);
                i.putExtra("merchantId", DbNew.getInstance(GiftCardListActivity.this).merchantGiftCard().get(position).getId());
                i.putExtra("businessName", DbNew.getInstance(GiftCardListActivity.this).merchantGiftCard().get(position).getBusinessName());
                i.putExtra("GiftCardType", GiftCardType.MYGIFTCARD);
            }
        }


        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {
        if (IsVoucher) {

        }
        if (IsTradePoint) {

        }
        if (IsFriendRequest) {

        }
        if (IstransferGiftCard) {

        }
        if (IsNewMerchant) {

        }
        if (IsNonKippinPhysical) {
            iv_star.setVisibility(View.VISIBLE);
        }
        if (IsNonKippinLoyalty) {

        }
    }

    class LoadingDataFromDataBase extends AsyncTask<String, String, List<MerchantDetail>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingBox.showLoadingDialog(activity, "Loading...");
        }

        @Override
        protected List<MerchantDetail> doInBackground(String... params) {
            //ArrayList<Bitmap> bitmaps = DbNew.getInstance(MyLoayaltyCardListActivity.this).getbitmaps();
            // Log.e("bitmaps"," == " + bitmaps);
            listCards = DbNew.getInstance(GiftCardListActivity.this).merchantGiftCard();
            return listCards;
        }

        @Override
        protected void onPostExecute(List<MerchantDetail> aVoid) {
            super.onPostExecute(aVoid);
            getListCards.clear();
            responseToGetLoyaltyCardModels.clear();
            responseToGetLoyaltyCardModels.addAll(aVoid);
            getListCards = aVoid;
            adapter_db = new GiftCardListAdapter1Image(aVoid, GiftCardListActivity.this);
            list_data.setAdapter(adapter_db);
            adapter = null;
            LoadingBox.dismissLoadingDialog();
        }
    }

    class ImageDownloader extends AsyncTask<Void, Void, Void> {
        Activity context;
        List<MerchantDetail> dataToDownload;

        public ImageDownloader(Activity context, List<MerchantDetail> dataToDownload) {
            this.context = context;
            this.dataToDownload = dataToDownload;
        }

        @Override
        protected Void doInBackground(Void... params) {
            byte[] byteArray = new byte[0];
            if (dataToDownload.size() > 0) {
                try {
                    for (int i = downloadedRows; i < dataToDownload.size(); i++) {
                        MerchantDetail merchant = dataToDownload.get(i);
                        if (merchant != null) {
                            if (merchant.getProfileImage() != null) {
                                String profileImage = merchant.getProfileImage().replace(" ", "%20");
                                profileBitmap = BitmapFactory.decodeStream((InputStream) new URL(profileImage).getContent());
                                merchant.setProfileImage(Base64Image(profileBitmap));


                            }

                        }

                        DbNew.getInstance(GiftCardListActivity.this).CreateMerchantGiftCard(merchant.getId(), new Gson().toJson(merchant));
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Error ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
