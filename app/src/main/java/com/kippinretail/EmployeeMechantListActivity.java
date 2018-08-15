package com.kippinretail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.EmplyeeMerchantListAdapter;
import com.kippinretail.Adapter.MerchantVoucherListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.MerchantAsEmployeeList.MerchantAsEmployeeDetail;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EmployeeMechantListActivity extends AbstractListActivity implements AdapterView.OnItemClickListener {

    private  int cardType;
    private List<MerchantAsEmployeeDetail> merchantsAsEmployee;
    private EmplyeeMerchantListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateToolBar();
        updateUI();
        addListener();
    }

    @Override
    public void updateToolBar() {
        generateActionBar(R.string.title_user_EnableMerchantListActivity, true, true, false);
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
                if(adapter!=null)
                adapter.getFilter().filter(s);
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
        Intent i = getIntent();
        ActivityMyMerchant.ScanCardType result = (ActivityMyMerchant.ScanCardType) i.getSerializableExtra("CardType");
        if (result == ActivityMyMerchant.ScanCardType.GIFT) {
            cardType = 3;
            getMerchantListForAnEmployee();
        } else if (result == ActivityMyMerchant.ScanCardType.PUNCH) {
            cardType = 1;
            getMerchantListForAnEmployee();
        } else if (result == ActivityMyMerchant.ScanCardType.LOYALTY) {
            cardType = 0;
            getMerchantListForAnEmployee();
        }
    }

    private void getMerchantListForAnEmployee() {
        String userId = String.valueOf(CommonData.getUserData(this).getId());
        LoadingBox.showLoadingDialog(this, "Loading...");
        RestClient.getApiServiceForPojo().GetEmployeeMerchantList(userId, String.valueOf(cardType), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Type listType = new TypeToken<List<MerchantAsEmployeeDetail>>() {
                }.getType();
                Gson gson = new Gson();
                 merchantsAsEmployee = (List<MerchantAsEmployeeDetail>) gson.fromJson(jsonElement.toString(), listType);
                if (merchantsAsEmployee != null) {
                    if (merchantsAsEmployee.size() == 1 && !merchantsAsEmployee.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(EmployeeMechantListActivity.this, "Service not enabled", false);

                    } else {
                        adapter = new EmplyeeMerchantListAdapter(EmployeeMechantListActivity.this, merchantsAsEmployee);
                        list_data.setAdapter(adapter);
                    }
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.getMessage());
                Log.e("error", error.getUrl());
                LoadingBox.dismissLoadingDialog();
                 MessageDialog.showFailureDialog(EmployeeMechantListActivity.this);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MerchantAsEmployeeDetail merchantAsEmployeeDetail = merchantsAsEmployee.get(position);
        if(merchantAsEmployeeDetail.isAuthenticated()){
            Intent intent = getIntent();
            switch (cardType) {
                case 0:
                    intent.putExtra("merchantId",merchantAsEmployeeDetail.getMerchantId());
                    intent.setClass(activity, ActivityScanLoyaltyCard.class);
                    break;

                case 1:
                    intent.putExtra("merchantId",merchantAsEmployeeDetail.getMerchantId());
                    intent.setClass(activity, ActivityScanPunchCard.class);
                    break;
                case 3:
                    intent.putExtra("merchantId",merchantAsEmployeeDetail.getMerchantId());
                    intent.setClass(activity, ActivityScanGiftCard.class);
                    break;
            }
            startActivity(intent);
            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }
        else{
            Intent i = new Intent();
            i.setClass(this, ActivityEmployeeAuthentication.class);
            i.putExtra("merchantId",merchantsAsEmployee.get(position).getMerchantId());
            i.putExtra("cardType",cardType);
            startActivity(i);
            overridePendingTransition(R.anim.animation_from_right,R.anim.animation_to_left);
        }

    }
}
