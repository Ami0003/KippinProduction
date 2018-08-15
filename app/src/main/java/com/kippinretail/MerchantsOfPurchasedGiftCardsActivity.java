package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.MerchantVoucherListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftCardList.GiftCard;
import com.kippinretail.Modal.GiftCardMerchantList.MerchantDetail;
import com.kippinretail.Modal.MerchantAsEmployeeList.MerchantAsEmployeeDetail;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.Modal.punchcard.ModalPunchCard;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.config.Utils;
import com.kippinretail.interfaces.OnLocationGet;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.retrofit.RestClientAdavanced;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MerchantsOfPurchasedGiftCardsActivity extends SuperActivity implements AdapterView.OnItemClickListener , View.OnClickListener , SearchView.OnQueryTextListener {

    private ListView listMerchants;
    private String customerID,parentButton,country;
    private ArrayList<MerchantModal> merchant = null;
    private TextView txtMessage,txtMerchant,txtcancel;
    private SearchView searchView;
    private boolean flag;
    private MerchantVoucherListAdapter adapter = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_purchased_gift_cards);
        initilization();
        listMerchants.setOnItemClickListener(this);
        Log.d("MchasedGivity", "onCreate");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MchasedGivity", "onDestroy");
    }
    private void initilization()
    {
        Log.e("initilization()", "initilization()");
        generateActionBar(R.string.title_merchant_select_user, true,true,false);
        listMerchants = (ListView)findViewById(R.id.listMerchants);
        txtMessage = (TextView)findViewById(R.id.txtMessage);
        txtMerchant = (TextView)findViewById(R.id.txtMerchant);
        searchView = (SearchView)findViewById(R.id.searchView);
        txtcancel = (TextView)findViewById(R.id.txtcancel);
        searchView.setOnQueryTextListener(this);
        txtcancel.setOnClickListener(this);
        country = "india";
        customerID = String.valueOf(CommonData.getUserData(MerchantsOfPurchasedGiftCardsActivity.this).getId());
        merchant = new ArrayList<MerchantModal>();
       // listMerchants.setOnClickListener(this);
        network = new Network(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MerchantsOf", "onResume");
        Intent intent = getIntent();
        if(intent!=null){
            parentButton = intent.getStringExtra("parentButton");
            if(parentButton!=null){
//                if(parentButton.equals("myGiftCard")){
//                    txtMessage.setText("Please select the merchant below to see your gift card for the merchant");
//                    setLogoText("USER GIFT CARDS");
//
//                    makeMerchantList();
//                }
//                  if(parentButton.equals("giftCardFromThirdParty")){
//                    setLogoText("FRIENDS FIFT CARD");
//                    txtMessage.setText("CONGRATS! You have been gifted cards by your friends.They are kippin it simply by the way of giving.");
//                    giftCardsFromKippinFriendsList();
//                }
//                 if(parentButton.equals("myPoint")){
//                    setLogoText("USER MY POINTS");
//                    txtMessage.setText("Currently you have Loyality Cards with the following merchants");
//                    makeSubcribedMerchantList();
//                }
                if(parentButton.equals("MyMerchant") || parentButton.equals("ActivityMyMerchant") ){
                    Log.e("parentButton", parentButton);
                    setLogoText("MERCHANT LIST");
                    txtMessage.setText("Please select the Merchant Id");
                //    GetEmployeeMerchantList();
                }
                else if(parentButton.equals("MyPunchCards")){
                    Log.e("parentButton", parentButton);
                    setLogoText("USER PUNCH CARDS");
                    txtMessage.setText("Please select the merchant below to see your punch card for the merchant");
                    GetEmployeeMerchantListForPunchCard();
                }else if( parentButton.equals("GetNewPunchCard") ){
                    Log.e("parentButton", parentButton);
                    setLogoText("PUNCH CARDS");
                    txtMessage.setText("Please select the Merchant");
                    GetEmployeeMerchantListForPunchCard1();
                }
            }
        }
        Utils.hideKeyboard(this);
        View focusView = getCurrentFocus();
        if(focusView!=null){
            focusView.clearFocus();
        }
    }

    Network network =null;

    private void GetEmployeeMerchantListForPunchCard()   {
        merchant.clear();

        int i = 0;
        {

            if (CommonData.getUserData(MerchantsOfPurchasedGiftCardsActivity.this) != null)
            {
                RestClientAdavanced.getApiServiceForPojo().MyPunchcardMerchantList(customerID, RestClientAdavanced.getCallback(new Callback<JsonElement>() {

                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        Log.e("Output -->", jsonElement.toString());
                        Log.e("Url", response.getUrl());
                        // USE GIFT CARD MERCHANT LIST MODAL
                        Type listtype = new TypeToken<List<ModalPunchCard>>() {
                        }.getType();
                        Gson gson = new Gson();

                        List<ModalPunchCard> merchantDetails = (List<ModalPunchCard>) gson.fromJson(jsonElement.toString(), listtype);

                        if (merchantDetails != null) {
                            if (merchantDetails.size() == 1 && !merchantDetails.get(0).getResponseMessage().equals("Success")) {
                                MessageDialog.showDialog(MerchantsOfPurchasedGiftCardsActivity.this, merchantDetails.get(0).getResponseMessage());
                            } else {
                                for (ModalPunchCard temp : merchantDetails) {
                                    MerchantModal _modal = new MerchantModal();
                                    _modal.setName(temp.getBusinessName());
                                    _modal.setMerchantid(temp.getId() + "");
                                    _modal.setMessage(temp.getResponseMessage());
                                    _modal.setParent(/*"MyPunchCards"*/parentButton);
                                    _modal.setBussinessName(temp.getBusinessName());
                                    _modal.setParentButton(parentButton);
                                    merchant.add(_modal);
                                }
                                System.out.println("Inside else" + merchant.size());
                                adapter = new MerchantVoucherListAdapter(MerchantsOfPurchasedGiftCardsActivity.this, merchant, parentButton);
                                listMerchants.setAdapter(adapter);
                            }

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("============", error.getMessage());
                        MessageDialog.showFailureDialog(MerchantsOfPurchasedGiftCardsActivity.this);

                    }
                }));
            }
        }

    }

        private void GetEmployeeMerchantListForPunchCard1()  {
        merchant.clear();

    int i = 0;
    network.getLocationParam(new OnLocationGet() {
        @Override
        public void onLocationGet(double lattitude, double longitude, String mCountry,String city) {

            if (CommonData.getUserData(MerchantsOfPurchasedGiftCardsActivity.this) != null)
            {
                RestClientAdavanced.getApiServiceForPojo().GetMerchantListByCountryForPunchCard(mCountry, RestClientAdavanced.getCallback(new Callback<JsonElement>() {

                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        Log.e("Output -->", jsonElement.toString());
                        Log.e("Url", response.getUrl());
                        // USE GIFT CARD MERCHANT LIST MODAL
                        Type listtype = new TypeToken<List<ModalPunchCard>>() {
                        }.getType();
                        Gson gson = new Gson();

                        List<ModalPunchCard> merchantDetails = (List<ModalPunchCard>) gson.fromJson(jsonElement.toString(), listtype);

                        if (merchantDetails != null) {
                            if (merchantDetails.size() == 1 && !merchantDetails.get(0).getResponseMessage().equals("Success")) {
                                MessageDialog.showDialog(MerchantsOfPurchasedGiftCardsActivity.this , merchantDetails.get(0).getResponseMessage());
                            } else {
                                for (ModalPunchCard temp : merchantDetails) {
                                    MerchantModal _modal = new MerchantModal();
                                    _modal.setName(temp.getBusinessName());
                                    _modal.setMerchantid(temp.getId() + "");
                                    _modal.setMessage(temp.getResponseMessage());
                                    _modal.setParent(/*"MyPunchCards"*/parentButton);
                                    _modal.setBussinessName(temp.getBusinessName());
                                    _modal.setParentButton(parentButton);
                                    merchant.add(_modal);
                                }
                                System.out.println("Inside else" + merchant.size());
                                adapter = new MerchantVoucherListAdapter(MerchantsOfPurchasedGiftCardsActivity.this, merchant, parentButton);
                                listMerchants.setAdapter(adapter);
                            }

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("============", error.getMessage());
                        MessageDialog.showFailureDialog(MerchantsOfPurchasedGiftCardsActivity.this);
                    }
                }));
            }
        }
    }) ;


    }


//    private void GetEmployeeMerchantListForPunchCard()  {
//        merchant.clear();
//
//    int i = 0;
//    network.getLocationParam(new OnLocationGet() {
//        @Override
//        public void onLocationGet(double lattitude, double longitude, String mCountry) {
//
//            if (CommonData.getUserData(MerchantsOfPurchasedGiftCardsActivity.this) != null)
//            {
//                RestClientAdavanced.getApiServiceForPojo().GetMerchantListByCountryForPunchCard(mCountry, RestClientAdavanced.getCallback(new Callback<JsonElement>() {
//
//                    @Override
//                    public void success(JsonElement jsonElement, Response response) {
//                        Log.e("Output -->", jsonElement.toString());
//                        Log.e("Url", response.getUrl());
//                        // USE GIFT CARD MERCHANT LIST MODAL
//                        Type listtype = new TypeToken<List<ModalPunchCard>>() {
//                        }.getType();
//                        Gson gson = new Gson();
//
//                        List<ModalPunchCard> merchantDetails = (List<ModalPunchCard>) gson.fromJson(jsonElement.toString(), listtype);
//
//                        if (merchantDetails != null) {
//                            if (merchantDetails.size() == 1 && !merchantDetails.get(0).getResponseMessage().equals("Success")) {
//                                MessageDialog.showDialog(MerchantsOfPurchasedGiftCardsActivity.this , merchantDetails.get(0).getResponseMessage());
//                            } else {
//                                for (ModalPunchCard temp : merchantDetails) {
//                                    MerchantModal _modal = new MerchantModal();
//                                    _modal.setName(temp.getBusinessName());
//                                    _modal.setMerchantid(temp.getId() + "");
//                                    _modal.setMessage(temp.getResponseMessage());
//                                    _modal.setParent(/*"MyPunchCards"*/parentButton);
//                                    _modal.setBussinessName(temp.getBusinessName());
//                                    _modal.setParentButton(parentButton);
//                                    merchant.add(_modal);
//                                }
//                                System.out.println("Inside else" + merchant.size());
//                                adapter = new MerchantVoucherListAdapter(MerchantsOfPurchasedGiftCardsActivity.this, merchant, parentButton);
//                                listMerchants.setAdapter(adapter);
//                            }
//
//                        }
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        Log.e("============", error.getMessage());
//                    }
//                }));
//            }
//        }
//    }) ;
//
//
//    }




//    private void giftCardsFromKippinFriendsList()
//    {
//        merchant.clear();
//        if(CommonData.getUserData(MerchantsOfPurchasedGiftCardsActivity.this)!=null) {
//            customerID = String.valueOf(CommonData.getUserData(MerchantsOfPurchasedGiftCardsActivity.this).getId());
//            LoadingBox.showLoadingDialog(MerchantsOfPurchasedGiftCardsActivity.this, "Loading...");
//            RestClient.getApiServiceForPojo().GetMerchantListForRecievedUser(customerID, new Callback<JsonElement>() {
//                @Override
//                public void success(JsonElement jsonElement, Response response) {
//                    Log.e("Output -->", jsonElement.toString());
//                    Log.e("Url", response.getUrl());
//                    // USE GIFT CARD MERCHANT LIST MODAL
//                    Type listtype = new TypeToken<List<MerchantDetail>>() {
//                    }.getType();
//                    Gson gson = new Gson();
//                    List<MerchantDetail> merchantDetails = (List<MerchantDetail>) gson.fromJson(jsonElement.toString(), listtype);
//                    if (merchantDetails != null) {
//                        if (merchantDetails.size() == 1 && !merchantDetails.get(0).getResponseMessage().equals("Success")) {
//                            MessageDialog.showDialog(activity,merchantDetails.get(0).getResponseMessage());
//                        } else {
//                            for (MerchantDetail temp : merchantDetails) {
//                                MerchantModal _modal = new MerchantModal();
//                                _modal.setName(temp.getBusinessName());
//                                _modal.setMerchantid(temp.getId());
//                                _modal.setMessage(temp.getResponseMessage());
//                                _modal.setParent("MerchantsOfPurchasedGiftCardsActivity");
//                                _modal.setBussinessName(temp.getBusinessName());
//                                _modal.setParentButton(parentButton);
//                                merchant.add(_modal);
//
//                            }
//                            System.out.println("Inside else" + merchant.size());
//                            adapter = new MerchantVoucherListAdapter(MerchantsOfPurchasedGiftCardsActivity.this, merchant, parentButton);
//                            listMerchants.setAdapter(adapter);
//
//                        }
//                        LoadingBox.dismissLoadingDialog();
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Log.e("============", error.getMessage());
//                    LoadingBox.dismissLoadingDialog();
//                }
//            });
//        }
//
//    }

    private void makeMerchantList()
    {
        merchant.clear();
        if(CommonData.getUserData(MerchantsOfPurchasedGiftCardsActivity.this)!=null) {
            customerID = String.valueOf(CommonData.getUserData(MerchantsOfPurchasedGiftCardsActivity.this).getId());
            LoadingBox.showLoadingDialog(MerchantsOfPurchasedGiftCardsActivity.this, "Loading...");
            RestClient.getApiServiceForPojo().MyGiftcardMerchantList(customerID, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("Output -->", jsonElement.toString());
                    Log.e("Url",response.getUrl());
                    // USE GIFT CARD MERCHANT LIST MODAL
                    Type listtype = new TypeToken<List<MerchantDetail>>() {
                    }.getType();
                    Gson gson = new Gson();
                    List<MerchantDetail> merchantDetails = (List<MerchantDetail>) gson.fromJson(jsonElement.toString(), listtype);
                    if (merchantDetails != null)
                    {
                        if(merchantDetails.size() == 1 && !merchantDetails.get(0).getResponseMessage().equals("Success")) {
                           System.out.println("Inside If");
                            MerchantModal modal = new MerchantModal();
                            modal.setBussinessName("My Non Kippin Gift Cards");
                            modal.setParent("MerchantsOfPurchasedGiftCardsActivity");
                            modal.setParentButton(parentButton);
                            modal.setChildActivity("MyPoint_FolderListActivity");
                            merchant.add(modal);
                            adapter = new MerchantVoucherListAdapter(MerchantsOfPurchasedGiftCardsActivity.this, merchant,parentButton);
                            listMerchants.setAdapter(adapter);
                            }
                            else
                            {
                                System.out.println("Inside else");
                                MerchantModal modal = new MerchantModal();
                                modal.setBussinessName("My Non Kippin Gift Cards");
                                modal.setParent("MerchantsOfPurchasedGiftCardsActivity");
                                modal.setParentButton(parentButton);
                                modal.setChildActivity("MyPoint_FolderListActivity");
                                merchant.add(modal);

                                for (MerchantDetail temp : merchantDetails) {
                                    MerchantModal _modal = new MerchantModal();
                                    _modal.setName(temp.getBusinessName());
                                    _modal.setMerchantid(temp.getId());
                                    _modal.setMessage(temp.getResponseMessage());
                                    _modal.setParent("MerchantsOfPurchasedGiftCardsActivity");
                                    _modal.setBussinessName(temp.getBusinessName());
                                    _modal.setParentButton(parentButton);
                                    _modal.setChildActivity("PurchasedGiftcardsActivity");
                                    merchant.add(_modal);

                                }
                                System.out.println("Inside else"+merchant.size());
                                adapter = new MerchantVoucherListAdapter(MerchantsOfPurchasedGiftCardsActivity.this, merchant,parentButton);
                                listMerchants.setAdapter(adapter);

                            }
                         LoadingBox.dismissLoadingDialog();
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    Log.e("============",error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showFailureDialog(MerchantsOfPurchasedGiftCardsActivity.this);
                }
            });
        }

    }

    private void makeSubcribedMerchantList(){
        merchant.clear();
        Log.e("makeSubcribantList()","makeSubcribedMerchantList();");
        LoadingBox.showLoadingDialog(this, "Loading...");
        RestClient.getApiServiceForPojo().GetSubscribedMerchantListForLoyalityCard(country, customerID, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output ==>", jsonElement.toString());
                Log.e("URL -->", response.getUrl());
                Type listType = new TypeToken<List<Merchant>>() {
                }.getType();
                Gson gson = new Gson();
                List<Merchant> merchants = (List<Merchant>) gson.fromJson(jsonElement.toString(), listType);
                if (merchants != null) {
                    if (merchants.size() == 1 && !merchants.get(0).getResponseMessage().equals("Success")) {
                        //MessageDialog.showDialog(MerchantsOfPurchasedGiftCardsActivity.this, "No merchantId available for your country.",false);
                        MerchantModal modal = new MerchantModal();
                        modal.setName("My Non Kippin Loyalty Cards");
                        modal.setParent("MyPoint_MerchantListActivity");
                        merchant.add(modal);
                        adapter = new MerchantVoucherListAdapter(MerchantsOfPurchasedGiftCardsActivity.this, merchant, parentButton);
                        listMerchants.setAdapter(adapter);

                    } else {
                        MerchantModal modal = new MerchantModal();
                        modal.setName("My Non Kippin Loyalty Cards");
                        modal.setBussinessName("My Non Kippin Loyalty Cards");
                        modal.setParent("MyPoint_MerchantListActivity");
                        merchant.add(modal);
                        for (Merchant temp : merchants) {
                            MerchantModal _modal = new MerchantModal();
                            _modal.setName(temp.getBusinessName());
                            _modal.setMerchantid(temp.getId());
                            _modal.setMessage(temp.getResponseMessage());
                            _modal.setBussinessName(temp.getBusinessName());
                            _modal.setParent("MyPoint_MerchantListActivity");
                            _modal.setBarcode(temp.getLoyalityBarcode());
                            _modal.setLoyalityCardName(temp.getLoyalityCardName());
                            merchant.add(_modal);
                        }
                        System.out.println(merchants.size());
                        adapter = new MerchantVoucherListAdapter(MerchantsOfPurchasedGiftCardsActivity.this, merchant, parentButton);
                        listMerchants.setAdapter(new MerchantVoucherListAdapter(MerchantsOfPurchasedGiftCardsActivity.this, merchant, parentButton));
                    }
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
            }
        });
    }

//    private void GetEmployeeMerchantList(){
//        Log.e("GetEmployeeMerchantList", parentButton);
//        merchant =  new ArrayList<>();
//        LoadingBox.showLoadingDialog(MerchantsOfPurchasedGiftCardsActivity.this,"Loading...");
//        RestClient.getApiServiceForPojo().GetEmployeeMerchantList(customerID, new Callback<JsonElement>() {
//            @Override
//            public void success(JsonElement jsonElement, Response response) {
//                Log.e("output-->", jsonElement.toString());
//                Log.e("Url-->", response.getUrl());
//                Type listType = new TypeToken<List<MerchantAsEmployeeDetail>>() {
//                }.getType();
//                Gson gson = new Gson();
//                List<MerchantAsEmployeeDetail> merchantsAsEmployee = (List<MerchantAsEmployeeDetail>) gson.fromJson(jsonElement.toString(), listType);
//                if (merchantsAsEmployee != null) {
//                    if (merchantsAsEmployee.size() == 1 && !merchantsAsEmployee.get(0).getResponseMessage().equals("Success")) {
//                        MessageDialog.showDialog(MerchantsOfPurchasedGiftCardsActivity.this, "No Merchant added you as a employee.", false);
//
//                    } else {
//                        for (MerchantAsEmployeeDetail temp : merchantsAsEmployee) {
//                            MerchantModal modal = new MerchantModal();
//                            modal.setIsAuthenticated(temp.getIsAuthenticated());
//                            modal.setMerchantid(temp.getMerchantId());
//                            modal.setMessage(temp.getResponseMessage());
//                            modal.setBussinessName(temp.getMerchantUsername());
//
//                            if (getIntent().getStringExtra("operation").equalsIgnoreCase("giftCard")) {
//                                modal.setParent("MerchantAsEmployee_GiftCard");
//                            }else{
//                                modal.setParent("MerchantAsEmployee_PunchCard");
//                            }
//
//                            merchant.add(modal);
//                        }
//                        listMerchants.setAdapter(new MerchantVoucherListAdapter(MerchantsOfPurchasedGiftCardsActivity.this, merchant, parentButton));
//                        listMerchants.setOnItemClickListener(MerchantsOfPurchasedGiftCardsActivity.this);
//                    }
//                }
//                LoadingBox.dismissLoadingDialog();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.e("error", error.getMessage());
//                Log.e("error", error.getUrl());
//                LoadingBox.dismissLoadingDialog();
//            }
//        });
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.txtcancel:
                cancelSearch();
                break;
        }
    }

    private void cancelSearch()
    {

        if(flag){
            // searchView.clearFocus();
            searchView.setQuery("",false);
            adapter = new MerchantVoucherListAdapter(MerchantsOfPurchasedGiftCardsActivity.this, merchant,parentButton);
            listMerchants.setAdapter(adapter);
            flag = false;
        }
    }

    private void nonKippinGiftCardList()
    {
        Intent i = new Intent();
        i.setClass(MerchantsOfPurchasedGiftCardsActivity.this , MyPoint_FolderListActivity.class);
        i.putExtra(MyPoint_FolderListActivity.OPERATION, getIntent().getStringExtra(MyPoint_FolderListActivity.OPERATION));
        i.putExtra("merchantId", "1");
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {
        if(adapter==null)return false;
        searchText = searchText ==null ? "" : searchText;
        System.out.println("searchText "+searchText);

        adapter.getFilter().filter(searchText);
        flag = true;
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parentButton.equals("myPoint")){
            Bundle bundle = new Bundle();
            bundle.putString(MyPoint_FolderListActivity.OPERATION,MyPoint_FolderListActivity.LOYALITY_CARD);
            Utility.enterActivity(MerchantsOfPurchasedGiftCardsActivity.this,MyPoint_FolderListActivity.class, bundle); // Gor Non Kippin Module
        }else
        if(parentButton.equals("myGiftCard")){
            if(position==0){
                Bundle bundle = new Bundle();
                bundle.putString(MyPoint_FolderListActivity.OPERATION,MyPoint_FolderListActivity.GIFTCARD);
                Utility.enterActivity(MerchantsOfPurchasedGiftCardsActivity.this,MyPoint_FolderListActivity.class, bundle); // Gor Non Kippin Module
            }
            else{
                Utility.enterActivity(MerchantsOfPurchasedGiftCardsActivity.this,Employee_ScanItem.class); // Gor Non Kippin Module
            }
        }else{
            Utility.enterActivity(MerchantsOfPurchasedGiftCardsActivity.this,Employee_ScanItem.class); // Gor Non Kippin Module
        }
    }
}
