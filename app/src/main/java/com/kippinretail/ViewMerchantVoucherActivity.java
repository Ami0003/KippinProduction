package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.MerchantVoucherListAdapterModifyImage;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.Adapter.MerchantVoucherListAdapter;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ViewMerchantVoucherActivity extends AbstractListActivity implements View.OnClickListener //, AdapterView.OnItemClickListener
{

    private RelativeLayout backArrowLayout;
    private String customerId;

    ArrayList<MerchantModal> merchant = null;
    MerchantVoucherListAdapter adapter = null;


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
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(this);
        makeVoucherList();
    }
    public void updateUI(){
        merchant = new ArrayList<MerchantModal>();
        customerId = String.valueOf(CommonData.getUserData(ViewMerchantVoucherActivity.this).getId());
        layout_nonKippin.setVisibility(View.GONE);
    }



    public void addListener(){
        txtSearch.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence searchText, int start, int before, int count) {
               if(adapter!=null) {
                   adapter.getFilter().filter(searchText);
               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
       // list_data.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

        }
    }

    private void makeVoucherList()
    {
        merchant.clear();
        LoadingBox.showLoadingDialog(ViewMerchantVoucherActivity.this,"Loading...");
        RestClient.getApiServiceForPojo().GetEnabledMerchantList(customerId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("User My voucher", jsonElement.toString());
                Type listType = new TypeToken<List<Merchant>>() {
                }.getType();
                Gson gson = new Gson();
                List<Merchant> merchants = (List<Merchant>) gson.fromJson(jsonElement.toString(), listType);
                if(merchants!=null ) {
                    if(merchants.size()==1 && !merchants.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(ViewMerchantVoucherActivity.this,"No Merchant has been enabled");
                    }
                    else
                    {
                        for (Merchant temp : merchants) {
                            MerchantModal modal = new MerchantModal();
                            modal.setName(temp.getBusinessName());
                            modal.setMerchantid(temp.getId());
                            modal.setMessage(temp.getResponseMessage());
                            modal.setProfileImage(temp.getProfileImage());
                           modal.setParent("ViewMerchantVoucherActivity");
                            modal.setIsRead(temp.isRead());
                            merchant.add(modal);
                        }
                        adapter = new MerchantVoucherListAdapter(ViewMerchantVoucherActivity.this, merchant,null);
                        list_data.setAdapter(adapter);

                    }
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("output", "failore".toString());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(ViewMerchantVoucherActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this,"onItemClick",Toast.LENGTH_LONG).show();
//        MerchantModal modal = merchant.get(position);
//        Intent i = new Intent();
//        i.setClass(this, VoucherListActivity.class);
//        i.putExtra("merchantId", modal.getMerchantid());
//        startActivity(i);
//    }
}
