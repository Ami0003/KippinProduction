package com.kippinretail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MerchantActivity_WeekdayActivity extends MerchantActivity_Analytics {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();
        addListener();
    }

    @Override
    void updateUI() {
        generateActionBar(R.string.tile_merchant_WeekdayActivity,true,true,false);
        txt_col1.setText("Gift Card WEEKDAY ACTIVITY");
        txt_col2.setVisibility(View.GONE);
        txt_col3.setVisibility(View.GONE);

        layout_container_analytics_search.setVisibility(View.GONE);

    }

    @Override
    public void addListener() {

    }
    @Override
    protected void onResume() {
        super.onResume();
        makeListForGiftCardWeekDaysAnalysis();
    }

    private void makeListForGiftCardWeekDaysAnalysis() {
        String merchantId = "" + CommonData.getUserData(this).getId();
        Log.e("merchantId",merchantId);
        LoadingBox.showLoadingDialog(activity, "Loading ...");
        RestClient.getApiServiceForPojo().getGiftCardWeekDaysAnalysis(merchantId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("RestClient", error.getUrl() + "");
                MessageDialog.showFailureDialog(MerchantActivity_WeekdayActivity.this);
            }
        });
    }

    @Override
    void startSearch() {

    }
}
