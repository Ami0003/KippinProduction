package com.kippinretail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.FriendListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.FriendList.FriendDetail;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FriendListActivity extends AbstractListActivity implements AdapterView.OnItemClickListener{

    private List<FriendDetail> friendDetails;
    private FriendListAdapter adapter;
    private ShareType shareType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateToolBar();
        updateUI();
        addListener();;
    }

    @Override
    public void updateToolBar() {
        generateActionBar(R.string.title_user_friend_list, true, true, false);
    }

    @Override
    public void updateUI() {
        layout_nonKippin.setVisibility(View.GONE);
        makeFriendList();
        Utils.hideKeyboard(this);
        shareType = (ShareType) getIntent().getSerializableExtra("shareType");
        if(shareType == ShareType.LOYALTY){


           }
            else if(shareType == ShareType.TRADEPOINT){

        }
        //else if(nonKippinCardType == NonKippinCardType.GIFTCARD){
//            tvTopbarTitle.setText("Non Kippin Gift Card");
//            makeListOfNonKippinGiftCard();
//        }

    }

    @Override
    public void addListener() {
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adapter!=null){
                    adapter.getFilter().filter(s);

                }
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

    }

    private void makeFriendList()
    {
        String userId = String.valueOf(CommonData.getUserData(this).getId());
        LoadingBox.showLoadingDialog(FriendListActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().GetFriendList(userId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output -->", jsonElement.toString());
                Log.e("url -SUCCESS->", response.getUrl());
                Type listtype = new TypeToken<List<FriendDetail>>() {
                }.getType();
                Gson gson = new Gson();
                friendDetails = (List<FriendDetail>) gson.fromJson(jsonElement.toString(), listtype);
                boolean flag = Utility.isResponseValid(friendDetails)  ;
                if(flag){
                    adapter = new FriendListAdapter(friendDetails, FriendListActivity.this);
                    list_data.setAdapter(adapter);
                }else{
                    MessageDialog.showDialog(FriendListActivity.this,"No friends available.",false);
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showFailureDialog(FriendListActivity.this);

            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(shareType == ShareType.LOYALTY){
            Intent i = new Intent();
            i.setClass(this, TransferGiftCards_FriendListActivity.class);
            i.putExtra("shareType", shareType);
            i.putExtra("friendId",friendDetails.get(position).getId());
            startActivity(i);
            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

        }
        else if(shareType == ShareType.TRADEPOINT){
            Intent intent = new Intent(this,ActivityMerchantList.class ) ;
            intent.putExtra("shareType" , shareType) ;
            intent.putExtra(getString(R.string.user) , friendDetails.get(position).getId() ) ;
            startActivity(intent);
            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }
        else if(shareType == ShareType.DONATEGIFTCARD){
            Intent intent = new Intent(this,ActivityMerchantList.class ) ;
            intent.putExtra("shareType" , shareType) ;
            intent.putExtra(getString(R.string.user) , friendDetails.get(position).getId()) ;
            CommonUtility.friendId = friendDetails.get(position).getId();
            startActivity(intent);
           overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }
        else if(shareType == ShareType.SHAREGIFTCARD){
            Intent intent = new Intent(this,GiftCardListActivity.class ) ;
            intent.putExtra("shareType" , shareType) ;

            intent.putExtra(getString(R.string.user) , friendDetails.get(position).getId() ) ;
            CommonUtility.friendId = friendDetails.get(position).getId();
            startActivity(intent);
           overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }else{

        }


    }


}
