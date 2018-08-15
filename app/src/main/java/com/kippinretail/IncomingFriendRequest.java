package com.kippinretail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.FriendAdapter;
import com.kippinretail.Adapter.IncomingFriendRequestAdapter;
import com.kippinretail.Adapter.LoyaltyCardRequestAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.RequestType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.FriendList.ModalFriends;
import com.kippinretail.Modal.ServerResponseForLoyaltyRequest;
import com.kippinretail.config.Utils;
import com.kippinretail.retrofit.RestClientAdavanced;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class IncomingFriendRequest extends AbstractListActivity {


    ArrayList<ModalFriends> modalFriendses;

    RequestType requestType;
    private String userId;
    private LoyaltyCardRequestAdapter incomingLoyaltyRequest;
    private IncomingFriendRequestAdapter incomingFriendRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateToolBar();
        updateUI();
        addListener();;
    }

    @Override
    public void updateToolBar() {

    }
    boolean incomingLoyaltyList = false;
    boolean incomingFriendList = false;

    @Override
    public void addListener() {
        requestType =(RequestType) getIntent().getSerializableExtra("RequestType");
        if(requestType==RequestType.LOYALTCARD){
            txtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(incomingLoyaltyList) {

                        if (incomingLoyaltyRequest != null) {
                            incomingLoyaltyRequest.getFilter().filter(s);
                        }
                    }else if(incomingFriendList){
                        if(incomingFriendRequest!=null){
                            incomingFriendRequest.getFilter().filter(s);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    public void updateUI() {
      layout_nonKippin.setVisibility(View.GONE);

        userId = String.valueOf(CommonData.getUserData(this).getId());

    }

    @Override
    protected void onResume() {
        super.onResume();
         requestType =(RequestType) getIntent().getSerializableExtra("RequestType");
        if(requestType==RequestType.FRIENDREQUEST){
            generateActionBar(R.string.incoming_friend_request, true, true, false);
            layout_container_search.setVisibility(View.GONE);
            getIncomingFriendRequest();
        }else if(requestType==RequestType.LOYALTCARD){
            generateActionBar(R.string.incoming_friend_request1, true, true, false);
            layout_container_search.setVisibility(View.VISIBLE);
            getIncomingLoyaltyCard();
        }
        Utils.hideKeyboard(this);
    }

    public void getIncomingFriendRequest() {
        RestClientAdavanced.getApiServiceForPojo(activity).PendingFriendListForTradePoint(CommonData.getUserData(activity).getId(), getCallback(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {


                Gson gson = new Gson();
                String jsonOutput = jsonElement.toString();
                Type listType = new TypeToken<List<ModalFriends>>() {
                }.getType();


                modalFriendses = gson.fromJson(jsonElement, listType);

                if (modalFriendses.size() == 1 && !modalFriendses.get(0).getResponseMessage().equals("Success")) {
                    MessageDialog.showDialog(activity, "No friend request",true);
                } else {
                     incomingFriendRequest = new IncomingFriendRequestAdapter((SuperActivity) activity, modalFriendses,requestType);
                    list_data.setAdapter(incomingFriendRequest);
                     incomingLoyaltyList = false;
                    incomingFriendList = true;
                }


            }

            @Override
            public void failure(RetrofitError error) {

                MessageDialog.showFailureDialog(IncomingFriendRequest.this);
            }
        }));
    }

    public void getIncomingLoyaltyCard() {
        RestClientAdavanced.getApiServiceForPojo(activity).GetRequestListForTranferLoyaltyCard(userId, getCallback(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                Log.e("output",jsonElement.toString());
                Gson gson = new Gson();
                String jsonOutput = jsonElement.toString();
                Type listType = new TypeToken<List<ServerResponseForLoyaltyRequest>>() {
                }.getType();
                List<ServerResponseForLoyaltyRequest> serverResponseForLoyaltyRequests = gson.fromJson(jsonElement.toString(),listType);
                if (serverResponseForLoyaltyRequests.size() == 1 && !serverResponseForLoyaltyRequests.get(0).getResponseMessage().equals("Success")) {

                } else {
                     incomingLoyaltyRequest = new LoyaltyCardRequestAdapter((SuperActivity) activity, serverResponseForLoyaltyRequests);
                    list_data.setAdapter(incomingLoyaltyRequest);
                     incomingLoyaltyList = true;
                   incomingFriendList = false;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                MessageDialog.showFailureDialog(IncomingFriendRequest.this);

            }
        }));
    }

}
