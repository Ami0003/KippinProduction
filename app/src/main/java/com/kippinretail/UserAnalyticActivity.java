package com.kippinretail;


import android.os.Bundle;
import android.view.View;

import com.kippinretail.ApplicationuUlity.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import com.kippinretail.Adapter.Analytics_UserPoint;
import com.kippinretail.ApplicationuUlity.CommonData;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ServerResponseForUserAnalytics;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;


import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserAnalyticActivity extends MerchantActivity_Analytics {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();
        addListener();
    }

    @Override
    void updateUI() {
        generateActionBar(R.string. tile_user_UserAnalyticActivity,true,true,false);
        txt_col1.setText("Loyalty Card Name");
        txt_col2.setText("Points Allocated");
        txt_col3.setText("Points Used");
        layout_container_analytics_search.setVisibility(View.GONE);
        layout_country.setVisibility(View.GONE );
        makeUserAnalyticList();
    }

    private void makeUserAnalyticList()
    {
        String userId =  String.valueOf(CommonData.getUserData(this).getId());
        Log.e("userId:",""+userId);
        LoadingBox.showLoadingDialog(UserAnalyticActivity.this, "Loading ...");
        RestClient.getApiServiceForPojo().getUserAnaytics(userId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                android.util.Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());
                LoadingBox.dismissLoadingDialog();
                Gson gson = new Gson();
                List<ServerResponseForUserAnalytics> serverResponseForUserAnalyticses = gson.fromJson(jsonElement.toString(), new TypeToken<List<ServerResponseForUserAnalytics>>() {
                }.getType());
                boolean flag = Utility.isResponseValid(serverResponseForUserAnalyticses);
                if (flag) {
                    listForAnalysis.setAdapter(new Analytics_UserPoint(serverResponseForUserAnalyticses, UserAnalyticActivity.this));
                } else {
                    MessageDialog.showDialog(UserAnalyticActivity.this, serverResponseForUserAnalyticses.get(0).getResponseMessage(), true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                android.util.Log.e("RestClient", error.getUrl());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(UserAnalyticActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }

    @Override
    public void addListener() {
        super.addListener();
    }

    @Override
    void startSearch() {

    }
}
