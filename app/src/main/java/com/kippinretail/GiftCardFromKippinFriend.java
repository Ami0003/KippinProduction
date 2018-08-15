package com.kippinretail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftedGiftCard;
import com.kippinretail.Modal.MerchantAsEmployeeList.MerchantAsEmployeeDetail;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GiftCardFromKippinFriend extends AbstractListActivity implements AdapterView.OnItemClickListener {
    String merchantId = null;
    String userId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card_from_kippin_friend);
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
        merchantId = getIntent().getStringExtra("merchantId");
        userId = String.valueOf(CommonData.getUserData(this).getId());
    }

    @Override
    public void addListener() {
        list_data.setOnItemClickListener(this);
    }

    public void getGiftcardFromFriends(){
        LoadingBox.showLoadingDialog(this,"Loading");
        RestClient.getApiServiceForPojo().GetRecievedGiftcardByMerchantId(merchantId, userId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Type listType = new TypeToken<List<GiftedGiftCard>>() {
                }.getType();
                Gson gson = new Gson();
                List<GiftedGiftCard> serverresponse = gson.fromJson(jsonElement.toString() ,listType);
                if(serverresponse!=null ){
                    if(serverresponse.size()>0){

                    }else{

                    }

                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();

                MessageDialog.showFailureDialog(GiftCardFromKippinFriend.this);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
