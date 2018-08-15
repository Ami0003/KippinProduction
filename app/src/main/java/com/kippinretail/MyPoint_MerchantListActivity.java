package com.kippinretail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.MerchantVoucherListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyPoint_MerchantListActivity extends Activity implements View.OnClickListener , AdapterView.OnItemClickListener {

    private TextView txtLogo,txtMessage;
    private RelativeLayout backArrowLayout;
    private ListView listMerchant;
    private String customerID,country,parentButton;
    private MerchantVoucherListAdapter adapter = null;
    private List<MerchantModal> mymerchantModals = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_point__merchant_list);
        initilization();
    }
    private void initilization() {
        mymerchantModals = new ArrayList<MerchantModal>();
        listMerchant = (ListView) findViewById(R.id.listMerchant);
        backArrowLayout = (RelativeLayout) findViewById(R.id.backArrowLayout);
        txtLogo = (TextView) findViewById(R.id.txtLogo);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        backArrowLayout.setOnClickListener(this);
        listMerchant.setOnItemClickListener(this);
        customerID = String.valueOf(CommonData.getUserData(MyPoint_MerchantListActivity.this).getId());
        country="india";
        Intent intent = getIntent();
        if(intent!=null){
            parentButton = intent.getStringExtra("parentButton");
            if(parentButton.equals("myPoint")){
                txtLogo.setText("USER MY POINTS");
                txtMessage.setText("Currently you have Loyality Cards with the following merchants");
                makeMerchantList();
            }else if(parentButton.equals("tradeWithFriends")){

            }
        }

    }



    private void makeMerchantList()
    {
        Log.e("country -->", country);
        Log.e("customerID -->", customerID);
        LoadingBox.showLoadingDialog(this, "Loading...");
        RestClient.getApiServiceForPojo().GetSubscribedMerchantListForLoyalityCard(country, customerID, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output ==>", jsonElement.toString());
                Type listType = new TypeToken<List<Merchant>>() {
                }.getType();
                Gson gson = new Gson();
                List<Merchant> merchants = (List<Merchant>) gson.fromJson(jsonElement.toString(), listType);
                if (merchants != null) {
                    if (merchants.size() == 1 && !merchants.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(MyPoint_MerchantListActivity.this, "No merchantId available for your country.");

                    } else {
                        for (Merchant temp : merchants) {
                            MerchantModal modal = new MerchantModal();
                            modal.setName(temp.getUsername());
                            modal.setMerchantid(temp.getId());
                            modal.setMessage(temp.getResponseMessage());
                            modal.setBussinessName(temp.getBusinessName());
                            modal.setParent("MyPoint_MerchantListActivity");
                            mymerchantModals.add(modal);
                        }
                        adapter = new MerchantVoucherListAdapter(MyPoint_MerchantListActivity.this, mymerchantModals,parentButton);
                        listMerchant.setAdapter(adapter);
                    }
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();

                MessageDialog.showFailureDialog(MyPoint_MerchantListActivity.this);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backArrowLayout:
                Utils.backPressed(MyPoint_MerchantListActivity.this);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
