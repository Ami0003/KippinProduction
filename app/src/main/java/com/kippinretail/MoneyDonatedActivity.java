package com.kippinretail;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.CharityDataAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.CharityData;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.kippinretail.R.id.flag;


/**
 * Created by kamaljeet.singh on 10/10/2016.
 */

public class MoneyDonatedActivity extends SuperActivity {
    ListView listView;
    private List<CharityData> charityData;
    ArrayList<CharityData> charity_Data;
    CharityDataAdapter adapter;
    RelativeLayout layout_container_search,layout_search;
    EditText txtSearch;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_donated);
        charity_Data=new ArrayList<>();
        charityData=new ArrayList<>();
        charityData.clear();
        charity_Data.clear();
        listView = (ListView) findViewById(R.id.listPoints);
        txtSearch=(EditText)findViewById(R.id.txtSearch);
        layout_container_search=(RelativeLayout)findViewById(R.id.layout_container_search);
        layout_search=(RelativeLayout)findViewById(R.id.layout_search);
        addListener();
        generateActionBar(R.string.title_user_list,true,true,false);
        getAllCharityData();
    }
    private void setAdapter() {
        adapter = new CharityDataAdapter(MoneyDonatedActivity.this,charityData);
        listView.setAdapter(adapter);
    }
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
                //if (adapterDonateToCharity != null)
                String text = txtSearch.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);
                //adapterDonateToCharity.getFilter().filter(s.toString().toLowerCase(Locale.getDefault()));
            }
        });

    }
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        android.util.Log.e("charText:",""+charText);
        charityData.clear();
        if (charText.length() == 0) {
            android.util.Log.e("pointDetails: ",""+charityData.size());
            charityData.addAll(charity_Data);
        }
        else
        {
            for (CharityData wp : charity_Data)
            {
                android.util.Log.e("getFreindName:::",""+wp.getUserName());
                if(wp.getUserName()!=null){
                    if (wp.getUserName().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        charityData.add(wp);
                    }
                }

            }
        }
        android.util.Log.e("charityData:",""+charityData.size());
        setAdapter();


    }
    private void getAllCharityData() {

        String userId = String.valueOf(CommonData.getUserData(this).getId());
        LoadingBox.showLoadingDialog(this, "Loading...");
        RestClient.getApiServiceForPojo().GetAllCharityData(userId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output ==>", jsonElement.toString());
                Log.e("URL -->", response.getUrl());
                Type listType = new TypeToken<List<CharityData>>() {
                }.getType();
                Gson gson = new Gson();
                charityData = (List<CharityData>) gson.fromJson(jsonElement.toString(), listType);
                LoadingBox.dismissLoadingDialog();
                Log.e("Response:",""+charityData.get(0).getResponseMessage());

                if(charityData.get(0).getResponseCode()==1){
                    charity_Data.addAll(charityData);
                    adapter = new CharityDataAdapter(MoneyDonatedActivity.this,charityData);
                    listView.setAdapter(adapter);
                }else{
                      MessageDialog.showDialog(MoneyDonatedActivity.this, charityData.get(0).getResponseMessage(), false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(error.getUrl(), error.getMessage());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(MoneyDonatedActivity.this, "We are experiencing technical difficulties but value your business… please try again later");
            }
        });

    }
}
