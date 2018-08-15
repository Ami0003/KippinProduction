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
import com.kippinretail.Adapter.VoucherListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.VoucherList.VoucherDetail;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class VoucherListActivity extends SuperActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private String merchantId,customeId,voucherId;
    private ListView listVoucher;
    private VoucherListAdapter adapter = null;
    private List<VoucherDetail> voucher;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_list);
        initilization();
        updateUI();
        addListener();
    }
    private void initilization()
    {
        /*BackWithHOME(getString(R.string.title_user_voucher_detail), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.HomeClick(VoucherListActivity.this);
            }
        });*/
        generateActionBar(R.string.title_user_voucher_detail, true, true,false);
        merchantId = getIntent().getStringExtra("merchantId");
        customeId = String.valueOf(CommonData.getUserData(this).getId());
        listVoucher = (ListView)findViewById(R.id.listVoucher);
        listVoucher.setOnItemClickListener(this);
    }

    private void updateUI(){

    }
    private void addListener(){

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        makeVoucherList();
        Utils.hideKeyboard(this);
    }

    private void makeVoucherList(){

        LoadingBox.showLoadingDialog(VoucherListActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().GetVouchers(merchantId, CommonData.getUserData(this).getId() + "", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("Voucher List -->", jsonElement.toString());
                Type listType = new TypeToken<List<VoucherDetail>>() {
                }.getType();
                Gson gson = new Gson();
                voucher = (List<VoucherDetail>) gson.fromJson(jsonElement.toString(), listType);
                Log.d("Size", voucher.size() + "");
                if (voucher != null) {
                    if (voucher.size() == 1 && !voucher.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(VoucherListActivity.this, "No Promotions Available.");
                    } else {
                        adapter = new VoucherListAdapter(VoucherListActivity.this, voucher);
                        listVoucher.setAdapter(adapter);
                    }
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(VoucherListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        VoucherDetail voucherDetail =voucher.get(position);
        voucherId = voucherDetail.getId();
        setAsRead();
        Intent i = new Intent();
        i.setClass(VoucherListActivity.this, VoucherDetailActivity.class);
        i.putExtra("voucherDetail", voucherDetail);
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    private void setAsRead(){
        RestClient.getApiServiceForPojo().RemoveIsRead(customeId, merchantId, voucherId, "", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.d("RemoveIsRead",jsonElement.toString());
                Log.d("URL",response.getUrl());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("URL",error.getUrl());
                MessageDialog.showDialog(VoucherListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }
}
