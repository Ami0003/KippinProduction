package com.kippinretail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
;

import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.Analytic_PointAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ResponseForPointAnalytics;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MerchantActivity_PointAnalytics extends MerchantActivity_Analytics {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateUI();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    void updateUI() {

        generateActionBar(R.string. tile_merchant_PointAnalytics,true,true,false);
        txt_col1.setText("Date");
        txt_col2.setText("Points");
        txt_col3.setText("Customer");
        layout_country.setVisibility(View.GONE);
    }

    @Override
    public void addListener() {
        super.addListener();
    }

    @Override
    void startSearch() {
        String merchantId = "" + CommonData.getUserData(this).getId();
        LoadingBox.showLoadingDialog(activity, "Loading ...");
        RestClient.getApiServiceForPojo().getPointAnalysis(getmFromDate(), getmToDate(),merchantId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                android.util.Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());
                Gson gson = new Gson();
                List<ResponseForPointAnalytics> responseForPointAnalyticses = gson.fromJson(jsonElement.toString(),new TypeToken<List<ResponseForPointAnalytics>>(){}.getType());


                boolean flag = Utility.isResponseValid(responseForPointAnalyticses)  ;
                if(flag){
                    listForAnalysis.setAdapter(new Analytic_PointAdapter(responseForPointAnalyticses,MerchantActivity_PointAnalytics.this));
                }else{
                    listForAnalysis.setAdapter(null);
                    MessageDialog.showDialog(MerchantActivity_PointAnalytics.this,responseForPointAnalyticses.get(0).getResponseMessage(),false);
                }



            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                android.util.Log.e("RestClient", error.getUrl() + "");
                MessageDialog.showFailureDialog(MerchantActivity_PointAnalytics.this);

            }
        });
    }
}
