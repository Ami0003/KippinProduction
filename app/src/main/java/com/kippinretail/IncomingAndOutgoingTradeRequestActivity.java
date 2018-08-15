package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.PointListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.PointList.PointDetail;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class IncomingAndOutgoingTradeRequestActivity extends SuperActivity implements AdapterView.OnClickListener {
    private String parentButton,currentId;
    ListView listPoints;
    View terms;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_incoming_and_outgoing_trade_request);
        initilization();

    }

    private void initilization(){
        listPoints = (ListView)findViewById(R.id.listPoints);
        terms = findViewById(R.id.terms);
        currentId = String.valueOf(CommonData.getUserData(IncomingAndOutgoingTradeRequestActivity.this).getId());
        Intent intent = getIntent();
        if(intent!=null){
            parentButton = intent.getStringExtra("parentButton");
            if(parentButton!=null){
                if(parentButton.equals("incomingTrade")){
                    generateActionBar(R.string.incoing_request, true,true,false);
                    getIncomingPointList();
                }else if(parentButton.equals("outGoingTrade")){
                    generateActionBar(R.string.outgoing_request, true, true, false);
                    getOutgoingPointList();
                }
            }
        }
    }

    private void getIncomingPointList(){
        Log.e("currentId",currentId);
        LoadingBox.showLoadingDialog(IncomingAndOutgoingTradeRequestActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().GetIncomingPointList(currentId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("Output -->",jsonElement.toString());
                Type listType = new TypeToken<List<PointDetail>>() {
                }.getType();
                Gson gson = new Gson();
                List<PointDetail> pointDetails = (List<PointDetail>) gson.fromJson(jsonElement.toString(), listType);
                if (pointDetails != null) {
                    if (pointDetails.size() == 1 && !pointDetails.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(IncomingAndOutgoingTradeRequestActivity.this, "No records found.");
                        listPoints.setAdapter(null);

                    } else {

                        listPoints.setAdapter(new PointListAdapter(IncomingAndOutgoingTradeRequestActivity.this,pointDetails,false));
                    }
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showFailureDialog(IncomingAndOutgoingTradeRequestActivity.this);
            }
        });

    }

    private void getOutgoingPointList(){
        Log.e("currentId",currentId);
        LoadingBox.showLoadingDialog(IncomingAndOutgoingTradeRequestActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().GetOutgoingPointList(currentId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("Output -->",jsonElement.toString());
                Type listType = new TypeToken<List<PointDetail>>() {
                }.getType();
                Gson gson = new Gson();
                List<PointDetail> pointDetails = (List<PointDetail>) gson.fromJson(jsonElement.toString(), listType);
                if (pointDetails != null) {
                    if (pointDetails.size() == 1 && !pointDetails.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(IncomingAndOutgoingTradeRequestActivity.this, "No records found.");
                        listPoints.setAdapter(null);

                    } else {

                        listPoints.setAdapter(new PointListAdapter(IncomingAndOutgoingTradeRequestActivity.this,pointDetails,true,false,true));
                    }
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showFailureDialog(IncomingAndOutgoingTradeRequestActivity.this);
            }
        });
    }
    @Override
    public void onClick(View v) {

    }
}
