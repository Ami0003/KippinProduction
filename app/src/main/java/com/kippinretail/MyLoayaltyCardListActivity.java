package com.kippinretail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.MerchantVoucherListAdapter;
import com.kippinretail.Adapter.MyLoayaltyCardListAdapter;
import com.kippinretail.Adapter.MyLoyaltyCardListAdapter_Db;
import com.kippinretail.ApplicationuUlity.AppStatus;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.DbNew;
import com.kippinretail.ApplicationuUlity.NonKippinCardType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.callbacks.NotificationREveiver;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.io.ByteArrayOutputStream;
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

public class MyLoayaltyCardListActivity extends AbstractListActivity implements View.OnClickListener ,NotificationREveiver{

    private String country=null;
    private MyLoayaltyCardListAdapter adapter = null;
    private MyLoyaltyCardListAdapter_Db adapter_db = null;
    private List<Merchant> responsemerchants;
    ArrayList<Merchant>responseToGetLoyaltyCardModels;

    // ================================= OFFLINE CACHE ================================
    Bitmap profileBitmap = null;
    byte[] bytes = null;
    List<Merchant> responsemerchants_ = new ArrayList<>();

    private byte[] imageInByte_gallery;
    private Bitmap barCodeBitmap;
    private String punchCardimage;
    private List<Merchant> listCards;
    private List<Merchant> listForMerchant = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        responseToGetLoyaltyCardModels=new ArrayList<>();
        responseToGetLoyaltyCardModels.clear();
        updateToolBar();
        updateUI();
        addListener();;

    }

    @Override
    public void updateToolBar() {
        generateActionBar(R.string.title_user_MyLoayaltyCardListActivity, true, true, false);
    }

    //1) List of Merchants for KIPPIN LOYALTY CARD
    @Override
    public void updateUI() {
        country = CommonData.getUserData(this).getCountry();
        layout_nonKippin.setVisibility(View.VISIBLE);
        txtNonKippin.setText("My Non KIPPIN Loyalty Cards");
        // if internet Access is available then load online else from database
        if (isNetworkAvailable()) {
            makeSubcribedMerchantList();
        } else {
            // Fetching data from Database
            new LoadingDataFromDataBase().execute();
        }

        Utils.hideKeyboard(this);
    }
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        android.util.Log.e("char-------Text:",""+charText);
        responsemerchants.clear();
        if (charText.length() == 0) {
            android.util.Log.e("NONKIPPIN-----: ",""+responseToGetLoyaltyCardModels.size());
            responsemerchants.addAll(responseToGetLoyaltyCardModels);
        }
        else
        {
            for (Merchant wp : responseToGetLoyaltyCardModels)
            {
                android.util.Log.e("W-----P:::",""+wp.getBusinessName());
                if(wp.getBusinessName()!=null){
                    if (wp.getBusinessName().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        responsemerchants.add(wp);
                    }
                }

            }
        }
        android.util.Log.e("responsemerchants:",""+responsemerchants.size());
        adapter = new MyLoayaltyCardListAdapter(responsemerchants, MyLoayaltyCardListActivity.this);
        list_data.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    public void filter_local(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        android.util.Log.e("char-------Text:",""+charText);
        responsemerchants.clear();
        if (charText.length() == 0) {
            android.util.Log.e("NONKIPPIN-----: ",""+responseToGetLoyaltyCardModels.size());
            responsemerchants.addAll(responseToGetLoyaltyCardModels);
        }
        else
        {
            for (Merchant wp : responseToGetLoyaltyCardModels)
            {
                android.util.Log.e("W-----P:::",""+wp.getBusinessName());
                if(wp.getBusinessName()!=null){
                    if (wp.getBusinessName().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        responsemerchants.add(wp);
                    }
                }

            }
        }
        android.util.Log.e("responsemerchants:",""+responsemerchants.size());
        adapter_db = new MyLoyaltyCardListAdapter_Db(responsemerchants, MyLoayaltyCardListActivity.this);
        list_data.setAdapter(adapter_db);
        adapter_db.notifyDataSetChanged();


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
                    //adapter.getFilter().filter(s);
                    if ("My Non KIPPIN Loyalty Cards".toLowerCase().contains(s.toString().toLowerCase())) {
                        iv_nonKippin.setVisibility(View.VISIBLE);
                        txtNonKippin.setVisibility(View.VISIBLE);
                        undline_nonKippin.setVisibility(View.VISIBLE);
                    } else {
                        iv_nonKippin.setVisibility(View.GONE);
                        txtNonKippin.setVisibility(View.GONE);
                        undline_nonKippin.setVisibility(View.GONE);
                    }
                } else if(adapter_db!=null){
                    String text = txtSearch.getText().toString().toLowerCase(Locale.getDefault());
                    filter_local(text);
                    //adapter_db.getFilter().filter(s);
                    if ("My Non KIPPIN Loyalty Cards".toLowerCase().contains(s.toString().toLowerCase())) {
                        iv_nonKippin.setVisibility(View.VISIBLE);
                        txtNonKippin.setVisibility(View.VISIBLE);
                        undline_nonKippin.setVisibility(View.VISIBLE);
                    } else {
                        iv_nonKippin.setVisibility(View.GONE);
                        txtNonKippin.setVisibility(View.GONE);
                        undline_nonKippin.setVisibility(View.GONE);
                    }
                } else{
                    if ("My Non KIPPIN Loyalty Cards".toLowerCase().contains(s.toString().toLowerCase())) {
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
       // list_data.setOnItemClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        NotificationHandler.getInstance().getNotificationForCards(this, this);

    }

    @SuppressLint("LongLogTag")
    private void makeSubcribedMerchantList(){
        try {
            Log.e("make---Subcribant---List()", "makeS----ubcribedMerchantList();");
            String userId = String.valueOf(CommonData.getUserData(this).getId());
            LoadingBox.showLoadingDialog(this, "Loading...");
            RestClient.getApiServiceForPojo().GetSubscribedMerchantListForLoyalityCard(userId, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("output ==>", jsonElement.toString());
                    Log.e("URL -->", response.getUrl());
                    Type listType = new TypeToken<List<Merchant>>() {
                    }.getType();
                    Gson gson = new Gson();
                    responsemerchants = (List<Merchant>) gson.fromJson(jsonElement.toString(), listType);
                    boolean flag = Utility.isResponseValid(responsemerchants);
                    ;   // variable clear before storing data in database

                    //
                    DbNew.getInstance(MyLoayaltyCardListActivity.this).DeleteAllUser();
                    if (flag) {
                        responseToGetLoyaltyCardModels.clear();
                        responseToGetLoyaltyCardModels.addAll(responsemerchants);
                        adapter = new MyLoayaltyCardListAdapter(responsemerchants, MyLoayaltyCardListActivity.this);
                        list_data.setAdapter(adapter);
                        adapter_db = null;
                        downLoadingImages();
                        LoadingBox.dismissLoadingDialog();
                        /*adapter = new MyLoayaltyCardListAdapter(responsemerchants, MyLoayaltyCardListActivity.this);
                        list_data.setAdapter(adapter);*/
                    } else {
                        LoadingBox.dismissLoadingDialog();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(error.getUrl(), error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showFailureDialog(MyLoayaltyCardListActivity.this);

                }
            });
        }catch(Exception ex
                ){

        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.layout_nonKippin:
                Intent i = new Intent();
                i.setClass(this, NonKippinGiftCardListActivity.class);
                i.putExtra("NonKippinCardType", NonKippinCardType.LOYALTYCARD);
                startActivity(i);
                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                break;
        }
    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         Merchant cardDetail =  responsemerchants.get(position);
        Intent i = new Intent();
        i.setClass(this, ActivityMyPunchCards.class);
        i.putExtra("cardDetail", cardDetail);
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }*/

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

    }



    // ================================= OFFLINE CACHE ================================
    public boolean isNetworkAvailable() {
        return AppStatus.getInstance(activity).isOnline(activity);
    }

    class LoadingDataFromDataBase extends AsyncTask<String, String, List<Merchant>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingBox.showLoadingDialog(activity,"Loading...");
        }

        @Override
        protected List<Merchant> doInBackground(String... params) {
           //ArrayList<Bitmap> bitmaps = DbNew.getInstance(MyLoayaltyCardListActivity.this).getbitmaps();
           // Log.e("bitmaps"," == " + bitmaps);
            listCards = DbNew.getInstance(MyLoayaltyCardListActivity.this).getLoyalityCard();
            return listCards;
        }

        @Override
        protected void onPostExecute(List<Merchant> aVoid) {
            super.onPostExecute(aVoid);
            responseToGetLoyaltyCardModels.clear();
            responseToGetLoyaltyCardModels.addAll(aVoid);
            adapter_db = new MyLoyaltyCardListAdapter_Db(aVoid, MyLoayaltyCardListActivity.this);
            list_data.setAdapter(adapter_db);
            adapter = null;
            LoadingBox.dismissLoadingDialog();
        }
    }
  int  downloadedRows = 0 ;
   /* private void downloadImages() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (responsemerchants.size() > 0) {


                    try {
                        for(int i = downloadedRows ; i < responsemerchants.size() ; i++) {
                            Merchant merchant = responsemerchants.get(i);
                            if (merchant != null) {
                                if (merchant.getProfileImage() != null) {
                                    profileBitmap = BitmapFactory.decodeStream((InputStream) new URL(merchant.getProfileImage()).getContent());
                                    merchant.setProfileImage(Base64Image(profileBitmap));
                                }
                                if (merchant.getObjPunchCard() != null) {
                                    barCodeBitmap = BitmapFactory.decodeStream((InputStream) new URL(merchant.getObjPunchCard().getPunchcardImage()).getContent());
                                    merchant.getObjPunchCard().setPunchcardImage(Base64Image(barCodeBitmap));
                                } else {
                                    merchant.setObjPunchCard(null);
                                }
                                Log.e("profileBitmap", "=" + profileBitmap);
                                Log.e(" barCodeBitmap", "=" + barCodeBitmap);
                            }

                            DbNew.getInstance(MyLoayaltyCardListActivity.this).CreateLoyalityCardEntry(merchant.getId(), new Gson().toJson(merchant));
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    catch(Error ex){
                        ex.printStackTrace();
                    }
                }
                *//*if (responsemerchants_.size() > 0) {
                    responsemerchants_.remove(0);
                    downloadImages();

                }*//*
                else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            }
        }).start();

    }*/

    private void downLoadingImages() {

        if(responsemerchants.size()>0) {
            new ImageDownloader(this,responsemerchants).execute();
        }
    }


    class ImageDownloader extends AsyncTask<Void  , Void , Void>{
        Activity context ;
        List<Merchant> dataToDownload;

        public ImageDownloader(Activity context, List<Merchant> dataToDownload) {
            this.context = context;
            this.dataToDownload = dataToDownload;
        }
        @Override
        protected Void doInBackground(Void... params) {
            byte[] byteArray = new byte[0];
            if (dataToDownload.size() > 0) {
                try {
                    for(int i = downloadedRows ; i < dataToDownload.size() ; i++) {
                        Merchant merchant = dataToDownload.get(i);
                        if (merchant != null) {
                            if (merchant.getProfileImage() != null) {
                                String profileImage = merchant.getProfileImage().replace(" ", "%20");
                                profileBitmap = BitmapFactory.decodeStream((InputStream) new URL(profileImage).getContent());
                                merchant.setProfileImage(Base64Image(profileBitmap));
                                //CommonUtility.dpToPx(mContext, 60);
                            /*    merchant.setProfileImage(Base64Image(profileBitmap,CommonUtility.dpToPx(context, 60),
                                        CommonUtility.dpToPx(context, 60),
                                        context));*/

                              /*  Bitmap targetBitmap = CommonUtility.decodeSampledBitmapFromResource(profileBitmap , CommonUtility.dpToPx(context, 60),
                                        CommonUtility.dpToPx(context, 60));
                         *//*       ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                targetBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                                byteArray = stream.toByteArray();*//*
                           *//*     ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                targetBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();*//*
                              *//*  encodedImageString = Base64.encodeToString(b, Base64.DEFAULT);

                                byte[] bytarray = Base64.decode(encodedImageString, Base64.DEFAULT);
                                Bitmap bmimage = BitmapFactory.decodeByteArray(bytarray, 0,
                                        bytarray.length);*/



                                //Log.e("profileBitmap", "=" + targetBitmap);
                                
                            }
                            if (merchant.getObjPunchCard() != null) {
                                barCodeBitmap = BitmapFactory.decodeStream((InputStream) new URL(merchant.getObjPunchCard().getPunchcardImage()).getContent());
                                merchant.getObjPunchCard().setPunchcardImage(Base64Image(barCodeBitmap));
                            } else {
                                merchant.setObjPunchCard(null);
                            }

                            Log.e(" barCodeBitmap", "=" + barCodeBitmap);
                        }

                        DbNew.getInstance(MyLoayaltyCardListActivity.this).CreateLoyalityCardEntry(merchant.getId(), new Gson().toJson(merchant),byteArray);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                catch(Error ex){
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
