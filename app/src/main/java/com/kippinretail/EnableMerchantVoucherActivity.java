package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.AdapterDonatePoints;
import com.kippinretail.Adapter.MerchantListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.OnSelectionChanged;
import com.kippinretail.Interface.SuperModal;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.Modal.Modal_PostTradePoint.DataToTradePoint;
import com.kippinretail.Modal.Modal_PostTradePoint.ObjList;
import com.kippinretail.Modal.ServerResponseModal.ServerResponse;
import com.kippinretail.app.Retail;
import com.kippinretail.config.Utils;
import com.kippinretail.interfaces.OnLocationGet;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;


public class EnableMerchantVoucherActivity extends SuperActivity implements View.OnClickListener,AdapterView.OnItemClickListener,SearchView.OnQueryTextListener {



    private Button btn;
    private ArrayList<MerchantModal> mymerchantModals = null;
    private MerchantListAdapter adapter = null;
    private String customerID, merchantId, parentButton,friendId,points;
    AdapterDonatePoints adapterDonatePoints = null;
    boolean adpterFlag=false;
    boolean adapterDonatePointsFlag = false;
    private String loyalityBarcode;
    private int pointsToTrade;
    View terms;
    RelativeLayout lalayout_ivBack;
    WebView wv_terms;
    EditText txtSearch;
    ListView list_data;
    LinearLayout layout_nonKippin;
    RelativeLayout layout_container_search,layout_dialog,layout_txtMercahntName;
    Button btn_upload;
    TextView txtMerchantName;
    TextView  txtNonKippin,txt_accept;;
    boolean adapterFlagLoyalty = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_merchant_voucher);
        initUI();
        updateUI();
        updateToolBar();
        addListener();
        }


    public void updateToolBar() {
        generateActionBar(R.string.title_user_EnableMerchantListActivity, true, true, false);
    }
    private void initUI(){
        wv_terms = (WebView)findViewById(R.id.wv_terms);
        txtSearch = (EditText)findViewById(R.id.txtSearch);
        txt_accept = (TextView)findViewById(R.id.txt_accept);
        list_data = (ListView)findViewById(R.id.list_data);
        layout_nonKippin = (LinearLayout)findViewById(R.id.layout_nonKippin);
        layout_txtMercahntName = (RelativeLayout)findViewById(R.id.layout_txtMercahntName);
        terms = findViewById(R.id.terms);
        layout_dialog = (RelativeLayout)findViewById(R.id.layout_dialog);
        txtNonKippin = (TextView)findViewById(R.id.txtNonKippin);
        lalayout_ivBack = (RelativeLayout)terms.findViewById(R.id.lalayout_ivBack);
    }

    private void updateUI() {
        try{
        layout_nonKippin.setVisibility(View.GONE);
        mymerchantModals = new ArrayList<MerchantModal>();
        customerID = String.valueOf(CommonData.getUserData(EnableMerchantVoucherActivity.this).getId());
        }catch(Exception ex){

        }
    }



    private void addListener() {
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence searchText, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence searchText, int start, int before, int count) {

                if (adapterDonatePointsFlag) {
                    if (adapterDonatePoints != null)
                        adapterDonatePoints.getFilter().filter(searchText);
                } else if (adpterFlag) {
                    if (adapter != null)
                        adapter.getFilter().filter(searchText);
                }else if(adapterFlagLoyalty){
                    adapter.getFilter().filter(searchText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       // lalayout_ivBack.setOnClickListener(this);
        txt_accept.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(this);
        Intent intent = getIntent();
        if (intent != null) {
            parentButton = intent.getStringExtra("parentButton");
            friendId = intent.getStringExtra("friendId");
            if (parentButton != null)
            {
                if (parentButton.equals("loyaltyProgramSignUp")) {
                    makeMerchantListForLoyalitySignup();
                }
                    else if (parentButton.equals("enableMerchanFlitVouchers") || parentButton.equals("enableMerchantVouchers")) {

                        makeMerchantList();
                    }
                    else if (parentButton.equals("tradeWithFriends")) {
                        getMerchantWithPoints();
                    }

                }
            } else {
                makeMerchantList();
            }

    }

    private void getMerchantWithPoints(){
        mymerchantModals.clear();
        LoadingBox.showLoadingDialog(EnableMerchantVoucherActivity.this, "Loading...");
        Network.With(this).getLocationParam(new OnLocationGet() {
            @Override
            public void onLocationGet(double lattitude, double longitude, String country,String city) {

                RestClient.getApiServiceForPojo().GetMerchantWithPoints(country, customerID, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        Log.e("Url -->", response.getUrl());
                        Log.e("Output -->", jsonElement.toString());
                        Type listType = new TypeToken<List<Merchant>>() {
                        }.getType();
                        Gson gson = new Gson();
                        final List<Merchant> merchants = (List<Merchant>) gson.fromJson(jsonElement.toString(), listType);
                        if (merchants != null) {
                            if (merchants.size() == 1 && !merchants.get(0).getResponseMessage().equals("Success")) {
                                MessageDialog.showDialog(EnableMerchantVoucherActivity.this, "You haven't earn any points yet.");

                            } else {
                                for (Merchant temp : merchants) {
                                    MerchantModal modal = new MerchantModal();
                                    modal.setName(temp.getUsername());
                                    modal.setBarcode(temp.getLoyalityBarcode());
                                    Log.e("temp.getUserId()", temp.getId() + "");
                                    modal.setMerchantid(temp.getId());
                                    modal.setPoints(temp.getMerchantPoint());
                                    modal.setBussinessName(temp.getBusinessName());
                                    modal.setLoyalityBarcode(temp.getLoyalityBarcode());
                                    modal.setChecked(false);
                                    mymerchantModals.add(modal);
                                }
                                adapterDonatePoints = new AdapterDonatePoints(EnableMerchantVoucherActivity.this, mymerchantModals, false,

                                        new OnSelectionChanged() {
                                            @Override
                                            public void onSelectionChanged(int position, SuperModal superAdapter, boolean isChecked) {

                                                if (isChecked) {
                                                    merchantId = ((MerchantModal) superAdapter).getMerchantid();
                                                    loyalityBarcode = ((MerchantModal) superAdapter).getLoyalityBarcode();
                                                    double temp = Double.parseDouble(((MerchantModal) superAdapter).getPoints());
                                                    pointsToTrade = (int) temp;
                                                } else merchantId = null;

                                            }
                                        });

                                list_data.setAdapter(adapterDonatePoints);
                                adapterDonatePointsFlag = true;
                                adpterFlag = false;
                                adapterFlagLoyalty = false;
                            }
                        }
                        LoadingBox.dismissLoadingDialog();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Url -->", error.getUrl());
                        MessageDialog.showFailureDialog(EnableMerchantVoucherActivity.this);

                    }
                });
            }
        });

    }
    private void makeMerchantList()
    {
        mymerchantModals.clear();
        LoadingBox.showLoadingDialog(this, "Loading...");
        String country = CommonData.getUserData(this).getCountry();
            RestClient.getApiServiceForPojo().GetMerchantList(country, customerID, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.d("URL ==>", response.getUrl());

                    Type listType = new TypeToken<List<Merchant>>() {
                    }.getType();
                    Gson gson = new Gson();
                    List<Merchant> merchants = (List<Merchant>) gson.fromJson(jsonElement.toString(), listType);
                    if (merchants != null) {
                        if (merchants.size() == 1 && !merchants.get(0).getResponseMessage().equals("Success")) {
                            MessageDialog.showDialog(EnableMerchantVoucherActivity.this, "There are no registered merchants in your country");

                        } else {
                            for (Merchant temp : merchants) {
                                MerchantModal modal = new MerchantModal();
                                modal.setName(temp.getUsername());
                                Log.e("temp.getUserId()", temp.getId() + "");
                                modal.setMerchantid(temp.getId());
                                modal.setBussinessName(temp.getBusinessName());
                                modal.setParent("enableMerchantVouchers");
                                modal.setIsSubscribedMerchant(temp.isSubscribedMerchant());
                                modal.setChecked(false);
                                modal.setProfileImage(temp.getProfileImage());
                                modal.setIsSubscribedMerchant(temp.isSubscribedMerchant());
                                mymerchantModals.add(modal);
                            }
                            adapter = new MerchantListAdapter(EnableMerchantVoucherActivity.this, mymerchantModals, customerID);
                            list_data.setAdapter(adapter);
                            adpterFlag = true;
                            adapterDonatePointsFlag = false;
                            adapterFlagLoyalty = false;
                        }
                    }
                    LoadingBox.dismissLoadingDialog();
                }

                @Override
                public void failure(RetrofitError error) {
                    LoadingBox.dismissLoadingDialog();
                    Log.e("URL ==>", error.getUrl());
                    MessageDialog.showFailureDialog(EnableMerchantVoucherActivity.this);
                }
            });



    }

    private void makeMerchantListForLoyalitySignup() {
        mymerchantModals.clear();
        LoadingBox.showLoadingDialog(EnableMerchantVoucherActivity.this, "Loading...");
        String country = CommonData.getUserData(this).getCountry();

        RestClient.getApiServiceForPojo().GetMerchantListForLoyalityCard(country, customerID, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("Output -->", jsonElement.toString());
                Log.e("Url :: ", response.getUrl());
                Type listType = new TypeToken<List<Merchant>>() {
                }.getType();
                Gson gson = new Gson();
                List<Merchant> merchants = (List<Merchant>) gson.fromJson(jsonElement.toString(), listType);
                if (merchants != null) {
                    if (merchants.size() == 1 && !merchants.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(EnableMerchantVoucherActivity.this, merchants.get(0).getResponseMessage());
                    } else {
                        for (Merchant temp : merchants) {
                            MerchantModal modal = new MerchantModal();
                            modal.setName(temp.getUsername());
                            Log.e("temp.getUserId()", temp.getId() + "");
                            modal.setMerchantid(temp.getId());
                            modal.setBussinessName(temp.getBusinessName());
                            modal.setParent("loyaltyProgramSignUp");
                            modal.setIsSubscribedMerchant(temp.isSubscribedMerchant());
                            modal.setChecked(false);
                            modal.setProfileImage(temp.getProfileImage());
                            modal.setTermsConditions(temp.isTermsConditions());
                            mymerchantModals.add(modal);
                        }
                        adapter = new MerchantListAdapter(EnableMerchantVoucherActivity.this, mymerchantModals, customerID, terms);
                        list_data.setAdapter(adapter);
                    }
                }
                adapterFlagLoyalty = true;
                adpterFlag = false;
                adapterDonatePointsFlag = false;
                LoadingBox.dismissLoadingDialog();
            }


            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e(error.getMessage(), error.getUrl());
                MessageDialog.showFailureDialog(EnableMerchantVoucherActivity.this);
            }
        });


    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
       /*     case R.id.btn:
            if(parentButton.equals("enableMerchantVouchers")){
                optVoucher();
            }
//            else if(parentButton.equals("loyaltyProgramSignUp")) {
//                subscribe();
//            }
            else if(parentButton.equals("tradeWithFriends")){
              if( merchantId==null ) {
                  MessageDialog.showDialog(EnableMerchantVoucherActivity.this, "Please select one");
              }
                else {
                    tradePoint();
              }
            }
            break;*/
            case R.id.txt_accept:
                break;
            case R.id.lalayout_ivBack:
                Animation animation = AnimationUtils.loadAnimation(this , R.anim.translate_down);
                terms.startAnimation(animation);
                terms.setVisibility(View.GONE);
                break;

        }
    }


    private void tradePoint(){
        ObjList objList = new ObjList();
        objList.setPoints(pointsToTrade);
        objList.setFriendId(Integer.parseInt(friendId));
        objList.setMerchantId(Integer.parseInt(merchantId));
        objList.setUserId(Integer.parseInt(customerID));
        objList.setLoyalityBarCode(loyalityBarcode);
        List<ObjList> objLists = new ArrayList<ObjList>();
        objLists.add(objList);
        DataToTradePoint dataToTradePoint = new DataToTradePoint();
        dataToTradePoint.setObjListList(objLists);
        TypedInput in = Utils.getTypedInput(dataToTradePoint);
        RestClient.getApiServiceForPojo().TradePoints( in,new Callback<JsonElement>(){
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("OutPut -->",jsonElement.toString());
                 Gson gson = new Gson();
                ServerResponse resp = gson.fromJson(jsonElement.toString(), new TypeToken<ServerResponse>() {
                }.getType());

                if(resp.getResponseMessage().equals("Success.")){
                    MessageDialog.showDialog(EnableMerchantVoucherActivity.this,"Points has been successfully transferred",true);

                }else{
                    MessageDialog.showDialog(EnableMerchantVoucherActivity.this,"Sorry fail to transfer Points Try Later",true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("URL -->", error.getUrl());
                MessageDialog.showFailureDialog(EnableMerchantVoucherActivity.this);
            }
        });

    }
//    private void subscribe()
//    {
//        merchantId = getMerchantId();
//        LoadingBox.showLoadingDialog(EnableMerchantVoucherActivity.this , "Loading...");
//        RestClient.getApiServiceForPojo().SubscribeMerchant(customerID, merchantId, "", new Callback<JsonElement>() {
//            @Override
//            public void success(JsonElement jsonElement, Response response) {
//                Log.e("output -->", jsonElement.toString());
//                LoadingBox.dismissLoadingDialog();
//                MessageDialog.showDialog(EnableMerchantVoucherActivity.this, "Merchant has been sucessfully selected for loayality card");
//                finish();
//                Intent i = new Intent();
//                i.setClass(EnableMerchantVoucherActivity.this, ViewMerchantVoucherActivity.class);
//                startActivity(i);
//                LoadingBox.dismissLoadingDialog();
//                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                LoadingBox.dismissLoadingDialog();
//                Log.e("Error Comes ", error.getMessage());
//            }
//        });
//    }

    private void optVoucher()
    {
       try {

//            merchantId = getMerchantId();
           Log.e("merchantId",merchantId);
            LoadingBox.showLoadingDialog(EnableMerchantVoucherActivity.this,"Loading...");
            RestClient.getApiServiceForPojo().EnableMerchant(customerID, merchantId,"",new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("Response", response.getUrl());
                    Log.e("Output ==>", jsonElement.toString());
                    MessageDialog.showDialog(EnableMerchantVoucherActivity.this,"Selected merchantId has been Successfully OptIn for Vouchers");
                    finish();
                    Intent i = new Intent();
                    i.setClass(EnableMerchantVoucherActivity.this , ViewMerchantVoucherActivity.class);
                    startActivity(i);
                    LoadingBox.dismissLoadingDialog();
                    overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                }

                @Override
                public void failure(RetrofitError error) {
                    LoadingBox.dismissLoadingDialog();
                    Log.e("Error Comes", error.getMessage() + " " + error.getUrl());
                    MessageDialog.showFailureDialog(EnableMerchantVoucherActivity.this);

                }
            });

        }catch(Exception ex)
        {
            Log.e("Message",ex.getMessage());
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MerchantModal rowitem = (MerchantModal)parent.getItemAtPosition(position);
        Toast.makeText(EnableMerchantVoucherActivity.this,"Cliked On"+rowitem.getName(),Toast.LENGTH_LONG).show();
    }

//    public String getMerchantId()
//    {
//        String _merchantId="";
//        StringBuffer responseText = new StringBuffer();
//        List<MerchantModal> rowitem = adapter.originalData_merchantModals;
//        for(int i = 0 ; i<rowitem.size() ; i++)
//        {
//            if (rowitem.get(i).isChecked())
//            {
//                responseText.append(rowitem.get(i).getMerchantid()+",");
//            }
//        }
//        String temp = responseText.toString();
//        if(temp!=null)
//        {
//            _merchantId = temp.substring(0,temp.length()-1);
//        }
//        return _merchantId;
//    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {
        System.out.println(searchText);


        return false;
    }
}
