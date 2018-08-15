package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.GiftCardListAdapter;
import com.kippinretail.Adapter.GiftCardListAdapter1;
import com.kippinretail.Adapter.GiftCardListAdapter1Image;
import com.kippinretail.Adapter.MerchantVoucherListAdapter;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftCardMerchantList.MerchantDetail;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.config.Utils;
import com.kippinretail.retrofit.RestClient;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SelectMerchantActivity extends AbstractListActivity implements AdapterView.OnItemClickListener {

    private String country = null;
    private GiftCardListAdapter1Image adapter = null;
    List<MerchantDetail> responsemerchantDetails = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        country = i.getStringExtra("country");
        updateToolBar();
        updateUI();
        addListener();;
        Utils.hideKeyboard(this);
    }
    @Override
    public void updateToolBar() {
        generateActionBar(R.string.title_user_SelectMerchantActivity, true, true, false);
    }

    @Override
    public void updateUI() {

        layout_nonKippin.setVisibility(View.GONE);
    }

    @Override
    public void addListener() {
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // if(adapter!=null)
               // adapter.getFilter().filter(s);
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
        makeMerchantList();
    }

    private void makeMerchantList()
    {
        RestClient.getApiServiceForPojo().GetMerchantListByCountry(country, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output -->", jsonElement.toString());
                Log.e("url -->", response.getUrl());

                Type listtype = new TypeToken<List<MerchantDetail>>() {
                }.getType();
                Gson gson = new Gson();
                 responsemerchantDetails = (List<MerchantDetail>) gson.fromJson(jsonElement.toString(), listtype);
                boolean flag = Utility.isResponseValid(responsemerchantDetails)  ;
                if(flag){
                    adapter = new GiftCardListAdapter1Image(responsemerchantDetails,SelectMerchantActivity.this);
                    list_data.setAdapter(adapter);
                }else{
                    MessageDialog.showDialog(SelectMerchantActivity.this, responsemerchantDetails.get(0).getResponseMessage(), false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("url -->", error.getUrl());
                MessageDialog.showDialog(SelectMerchantActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent();
        i.setClass(this,GiftCardsActivity.class);
        i.putExtra("merchantId", responsemerchantDetails.get(position).getId());
        i.putExtra("terms",responsemerchantDetails.get(position).getTermsConditions());
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }
}
