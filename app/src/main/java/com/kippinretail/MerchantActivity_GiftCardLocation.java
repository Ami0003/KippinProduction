package com.kippinretail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.Analytics_LocationAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ResponseForGiftCardUsageAnalytics;
import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ServerResponseForLOcationAnalysis;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MerchantActivity_GiftCardLocation extends MerchantActivity_Analytics {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();
        addListener();
    }



    @Override
    void updateUI() {
        generateActionBar(R.string.tile_merchant_GiftCardLocation,true,true,false);
        txt_col1.setText("Country");
        txt_col2.setText("Amount");
        txt_col3.setVisibility(View.GONE);

          layout_container_analytics_search.setVisibility(View.GONE);
    }

    @Override
    public void addListener() {
        super.addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeListForGiftCardLocationAnalysis();
    }

    private void makeListForGiftCardLocationAnalysis() {
        String merchantId = "" + CommonData.getUserData(this).getId();
        LoadingBox.showLoadingDialog(activity, "Loading ...");
        RestClient.getApiServiceForPojo().getGiftCardLocationAnalysis(merchantId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                ;
                android.util.Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());
                Gson gson = new Gson();
                List<ServerResponseForLOcationAnalysis> serverResponseForLOcationAnalysises = gson.fromJson(jsonElement.toString(), new TypeToken<List<ServerResponseForLOcationAnalysis>>() {
                }.getType());
                if (serverResponseForLOcationAnalysises != null && serverResponseForLOcationAnalysises.size() > 0) {
                  for(ServerResponseForLOcationAnalysis _response : serverResponseForLOcationAnalysises){
                    listForAnalysis.setAdapter(new Analytics_LocationAdapter( serverResponseForLOcationAnalysises,MerchantActivity_GiftCardLocation.this ));
                  }

                }
            }
                @Override
                public void failure (RetrofitError error){
                    LoadingBox.dismissLoadingDialog();
                    ;
                    android.util.Log.e("RestClient", error.getUrl() + "");
                    MessageDialog.showFailureDialog(MerchantActivity_GiftCardLocation.this);
                }
            }

            );
        }

        @Override
    void startSearch() {

    }
}
