package com.kippinretail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.GiftCardListAdapter;
import com.kippinretail.Adapter.NonKippinGiftCardListAdapter1_DB;
import com.kippinretail.Adapter.PurchasedGigtcardListAdapler;
import com.kippinretail.ApplicationuUlity.AppStatus;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.DbNew;
import com.kippinretail.ApplicationuUlity.GiftCardType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftCardList.GiftCard;
import com.kippinretail.Modal.GiftCardModal;
import com.kippinretail.Modal.ResponseToGetLoyaltyCard;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PurchasedGiftcardsActivity extends AbstractListActivity implements AdapterView.OnItemClickListener {
    List<GiftCard> giftCard;
    private String merchantId, userId, businessName;
    private ArrayList<GiftCardModal> giftCardModals;
    private Bitmap giftCardImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initilization();
        updateToolBar();
        updateUI();
        addListener();
    }

    @Override
    public void updateToolBar() {
        generateActionBar(R.string.title_purchased_giftcards, true, true, false);
        ;
    }


    private void initilization() {
        giftCardModals = new ArrayList<GiftCardModal>();
        userId = String.valueOf(CommonData.getUserData(PurchasedGiftcardsActivity.this).getId());
    }

    public void updateUI() {

        layout_container_search.setVisibility(View.GONE);
        layout_nonKippin.setVisibility(View.GONE);
        layout_txtMercahntName.setVisibility(View.VISIBLE);
        Intent i = getIntent();
        merchantId = i.getStringExtra("merchantId");
        businessName = i.getStringExtra("businessName");
        txtMerchantName.setText(businessName);
        GiftCardType gidtcard = (GiftCardType) i.getSerializableExtra("GiftCardType");
        if (gidtcard == GiftCardType.MYGIFTCARD) {
            layout_txtMercahntName.setVisibility(View.VISIBLE);
            txtMerchantName.setVisibility(View.VISIBLE);
            txtMerchantName.setText(businessName);
            //makePurchasedGiftCardList();
            if (AppStatus.getInstance(activity).isOnline(activity)) {
                makePurchasedGiftCardList();
            } else {
                new LoadGiftCardData().execute();

            }
        }

    }

    @Override
    public void addListener() {
        list_data.setOnItemClickListener(this);
    }

    private void makePurchasedGiftCardList() {
        LoadingBox.showLoadingDialog(PurchasedGiftcardsActivity.this, "Loading");
        RestClient.getApiServiceForPojo().GetMyPurchasedGiftcardListByMerchantId(merchantId, userId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("URL  :: ", response.getUrl());
                Type listTYpe = new TypeToken<List<GiftCard>>() {
                }.getType();
                Gson gson = new Gson();
                List<GiftCard> giftCards = (List<GiftCard>) gson.fromJson(jsonElement.toString(), listTYpe);
                if (giftCards != null) {
                    if (giftCards.size() == 1 && !giftCards.get(0).getResponseCode().equals("1")) {
                        MessageDialog.showDialog(PurchasedGiftcardsActivity.this, giftCards.get(0).getResponseMessage(), false);
                    } else {
                        for (GiftCard giftCard : giftCards) {
                            GiftCardModal giftCardModal = new GiftCardModal();
                            giftCardModal.setGiftCardId(giftCard.getId());
                            giftCardModal.setGetftCardPrice(giftCard.getPrice());
                            giftCardModal.setGiftCardImage(giftCard.getGiftcardImage());
                            giftCardModal.setGiftCardBarCodeNo(/*generateBarcode(giftCard.getId())*/giftCard.getBarcode());
                            giftCardModals.add(giftCardModal);
                            Log.e("Barcode ", generateBarcode(giftCard.getId()));
                        }
                        // Clear table before insert
                        DbNew.getInstance(PurchasedGiftcardsActivity.this).DeletePurchasedTable();
                        // Adding Giftcard Details into Database

                        downLoadingImages(giftCards);
                        /*for(int i=0;i<giftCards.size();i++) {
                            DbNew.getInstance(PurchasedGiftcardsActivity.this).CreatePurchasedCard(giftCards.get(i).getBarcode(),
                                    new Gson().toJson(giftCards.get(i)));
                        }*/

                        /*list_data.setAdapter(new PurchasedGigtcardListAdapler(giftCardModals, PurchasedGiftcardsActivity.this , findViewById(R.id.root)));*/
                    }

                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Error ", error.getMessage());
                MessageDialog.showDialog(PurchasedGiftcardsActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });


    }

    private void downLoadingImages(List<GiftCard> giftCards) {

        if (giftCards.size() > 0) {
            new ImageDownloader(this, giftCards).execute();


        }

    }


    class ImageDownloader extends AsyncTask<Void, Void, String> {
        Activity context;
        List<? extends Object> dataToDownload;


        public ImageDownloader(Activity context, List<? extends Object> dataToDownload) {
            this.context = context;
            this.dataToDownload = dataToDownload;

        }

        @Override
        protected String doInBackground(Void... params) {
            if (dataToDownload.size() > 0) {
                try {

                    for (int i = 0; i < dataToDownload.size(); i++) {
                        GiftCard giftCardModal = (GiftCard) dataToDownload.get(i);
                        if (giftCardModal != null) {
                            //String to = responseToGetLoyaltyCard.getFrontImage().replaceAll("(?<!(http:|https:))[//]+", "/");
                            //String profile = responseToGetLoyaltyCard.getLogoImage().replaceAll("(?<!(http:|https:))[//]+", "/");

                            String profileImage = giftCardModal.getGiftcardImage().replace(" ", "%20");


                            giftCardImage = BitmapFactory.decodeStream((InputStream) new URL(profileImage).getContent());


                            giftCardModal.setGiftcardImage(Base64Image(giftCardImage));


                        }

                        DbNew.getInstance(PurchasedGiftcardsActivity.this).CreatePurchasedCard(giftCardModal.getBarcode(),
                                new Gson().toJson(giftCardModal));

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
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
           /* nonKippinListAdapter_db = new NonKippinGiftCardListAdapter1_DB(serverresponse, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
            list_data.setAdapter(nonKippinListAdapter_db);*/
            list_data.setAdapter(new PurchasedGigtcardListAdapler(giftCardModals, PurchasedGiftcardsActivity.this , findViewById(R.id.root)));
             LoadingBox.dismissLoadingDialog();
        }
    }










  /*  private void makeGiftedGiftCardList() {
        Log.e("Customer Id -->", userId);
        Log.e("merchantId -->", merchantId);
        LoadingBox.showLoadingDialog(PurchasedGiftcardsActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().GetRecievedGiftcardByMerchantId(merchantId, userId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("Output -->", jsonElement.toString());
                Log.e("URL  :: ", response.getUrl());
                Type listTYpe = new TypeToken<List<GiftCard>>() {
                }.getType();
                Gson gson = new Gson();
                List<GiftCard> giftCards = (List<GiftCard>) gson.fromJson(jsonElement.toString(), listTYpe);
                if (giftCards != null) {
                    if (giftCards.size() == 1 && !giftCards.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(PurchasedGiftcardsActivity.this, "No gift cards available.");
                    } else {
                        for (GiftCard giftCard : giftCards) {

                            GiftCardModal giftCardModal = new GiftCardModal();
                            giftCardModal.setGiftCardId(giftCard.getId());
                            giftCardModal.setGetftCardPrice(giftCard.getPrice());
                            giftCardModal.setGiftCardImage(giftCard.getGiftcardImage());
//                            giftCardModal.setGiftCardBarCodeNo(generateBarcode(giftCard.getId()));
                            giftCardModal.setGiftCardBarCodeNo(giftCard.getBarcode());
                            giftCardModals.add(giftCardModal);
                            Log.e("Barcode ", generateBarcode(giftCard.getId()));
                        }
                        list_data.setAdapter(new GiftCardListAdapter(giftCardModals, PurchasedGiftcardsActivity.this, findViewById(R.id.root)));
                    }

                }

                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Error ", error.getMessage());
                MessageDialog.showDialog(PurchasedGiftcardsActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }*/

    private String generateBarcode(String giftCardId) {
        String barCode;
        Random r = new Random();
        barCode = merchantId + userId + giftCardId + String.valueOf(100000 + r.nextInt(900000));
        return barCode;
    }

    private void importPhysicalcard() {
        Intent i = getIntent() == null ? new Intent() : getIntent();
        //  i.setClass(PurchasedGiftcardsActivity.this , ImportPhysicalCardActivity.class);
        //i.putExtra("merchantId", modal.getMerchantid());
        i.setClass(this, MyPoint_FolderListActivity.class);
        i.putExtra("merchantId", merchantId);
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId())
//        {
//            case R.id.btnImport:
//                importPhysicalcard();
//                break;
//
//        }
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RestClient.getApiServiceForPojo().RemoveIsReadTransfer(giftCardModals.get(position).getGiftCardId(),
                giftCardModals.get(position).getGiftCardBarCodeNo(), new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
        Intent i = new Intent();
        i.setClass(this, CardDetail.class);
        i.putExtra("merchantName", businessName);
        i.putExtra("barcode", giftCardModals.get(position).getGiftCardBarCodeNo());
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    class LoadGiftCardData extends AsyncTask<String, String, ArrayList<GiftCardModal>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingBox.showLoadingDialog(PurchasedGiftcardsActivity.this, "");
        }

        @Override
        protected ArrayList<GiftCardModal> doInBackground(String... params) {
            giftCard = DbNew.getInstance(PurchasedGiftcardsActivity.this).purchasedCardData(merchantId);
            for (int x = 0; x < giftCard.size(); x++) {
                GiftCardModal giftCardModal = new GiftCardModal();
                giftCardModal.setGiftCardId(giftCard.get(x).getId());
                giftCardModal.setGetftCardPrice(giftCard.get(x).getPrice());
                giftCardModal.setGiftCardImage(giftCard.get(x).getGiftcardImage());
                giftCardModal.setGiftCardBarCodeNo(giftCard.get(x).getBarcode());
                giftCardModals.add(giftCardModal);
                Log.e("Barcode ", generateBarcode(giftCard.get(x).getId()));
                Log.e("Price ", generateBarcode(giftCard.get(x).getPrice()));
            }
            return giftCardModals;
        }

        @Override
        protected void onPostExecute(ArrayList<GiftCardModal> s) {
            super.onPostExecute(s);
            list_data.setAdapter(new GiftCardListAdapter(giftCardModals, PurchasedGiftcardsActivity.this, findViewById(R.id.root)));
            LoadingBox.dismissLoadingDialog();

        }
    }


}
