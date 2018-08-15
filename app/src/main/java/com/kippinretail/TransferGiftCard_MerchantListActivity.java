package com.kippinretail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.FriendListAdapter;
import com.kippinretail.Adapter.MerchantVoucherListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftCardMerchantList.MerchantDetail;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TransferGiftCard_MerchantListActivity extends SuperActivity implements AdapterView.OnItemClickListener ,SearchView.OnQueryTextListener, View.OnClickListener{

    private TextView txtMessage,txtLogo;
    private ListView listMerchants;
    private String customerID;
    ArrayList<MerchantModal> merchant = null;
    String friendId = null;
    private SearchView searchView;
    private TextView txtcancel;
    private boolean flag;
    private MerchantVoucherListAdapter adapter = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_purchased_gift_cards);
        initilization();
    }
    private void initilization()
    {
        generateActionBar(R.string.title_user_friend_list,true,true,false);
        txtMessage = (TextView)findViewById(R.id.txtMessage);
        txtLogo = (TextView) findViewById(R.id.txtLogo);
        searchView = (SearchView)findViewById(R.id.searchView);
        txtcancel = (TextView)findViewById(R.id.txtcancel);
        txtMessage.setText("Please Select the merchant");
        listMerchants = (ListView)findViewById(R.id.listMerchants);
        //listMerchants.setOnClickListener(this);
        merchant = new ArrayList<MerchantModal>();
        searchView.setFocusable(true);
        searchView.setOnQueryTextListener(this);
        txtcancel.setOnClickListener(this);
        Intent i = getIntent();
        if(i!=null){
            friendId = i.getStringExtra("friendId");
        }

        makeMerchantList();

    }

    private void makeMerchantList()
    {

        if(CommonData.getUserData(TransferGiftCard_MerchantListActivity.this)!=null) {
            customerID = String.valueOf(CommonData.getUserData(TransferGiftCard_MerchantListActivity.this).getId());

            LoadingBox.showLoadingDialog(TransferGiftCard_MerchantListActivity.this, "Loading...");
            RestClient.getApiServiceForPojo().MyGiftcardMerchantList(customerID, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("makeMerchantList ---Output -->", jsonElement.toString());
                    // USE GIFT CARD MERCHANT LIST MODAL
                    Type listtype = new TypeToken<List<MerchantDetail>>() {
                    }.getType();
                    Gson gson = new Gson();
                    List<MerchantDetail> merchantDetails = (List<MerchantDetail>) gson.fromJson(jsonElement.toString(), listtype);
                    if (merchantDetails != null)
                    {
                        if(merchantDetails.size() == 1 && !merchantDetails.get(0).getResponseMessage().equals("Success")) {
                            MessageDialog.showDialog(TransferGiftCard_MerchantListActivity.this, "You haven't purchased any gift card.");
                        }
                        else
                        {       for (MerchantDetail temp : merchantDetails) {
                                MerchantModal modal = new MerchantModal();
                                modal.setName(temp.getBusinessName());
                                modal.setMerchantid(temp.getId());
                                modal.setMessage(temp.getResponseMessage());
                                modal.setParent("TransferGiftCard_MerchantListActivity");
                                modal.setBussinessName(temp.getBusinessName());
                                modal.setFriendId(friendId);
                                merchant.add(modal);
                            }
                            adapter = new MerchantVoucherListAdapter(TransferGiftCard_MerchantListActivity.this, merchant,null);
                            listMerchants.setAdapter(adapter);
                        }
                        LoadingBox.dismissLoadingDialog();
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    Log.e("============",error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showDialog(TransferGiftCard_MerchantListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txtcancel:
                cancelSearch();
                break;
        }
    }
    private void cancelSearch()
    {
        if(flag){
            // searchView.clearFocus();
            searchView.setQuery("",false);
            adapter =  new MerchantVoucherListAdapter(TransferGiftCard_MerchantListActivity.this, merchant,null);
            listMerchants.setAdapter(adapter);

            //
            flag = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent i = new Intent();
        i.setClass(this , GiftCardsActivity.class);
//        i.putExtra("merchantId", modal.getMerchantid());
//        i.putExtra("merchantName", modal.getBussinessName());
//        i.putExtra("parent","TransferGiftCard_MerchantListActivity");
//        i.putExtra("friendId",modal.getFriendId());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {
        if(adapter==null)
            return false;
        adapter.getFilter().filter(searchText);
        flag = true;
        return false;

    }
}
