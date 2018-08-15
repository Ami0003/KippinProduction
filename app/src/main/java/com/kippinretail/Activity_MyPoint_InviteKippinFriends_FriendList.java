package com.kippinretail;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.FriendAdapter;
import com.kippinretail.Adapter.MerchantVoucherListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.FriendList.ModalFriends;
import com.kippinretail.Modal.GiftCardMerchant.GiftCardMerchantData;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.config.Utils;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.retrofit.RestClientAdavanced;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Activity_MyPoint_InviteKippinFriends_FriendList extends AbstractListActivity {

   
    ArrayList<ModalFriends> modalFriendses;// COMMAN VARIABLE TO STORE DATA FOR LIST
    ArrayList<ModalFriends> frientList;  // VARIABLE TO STORE DATA OF INVITED FRIENDS
    ArrayList<ModalFriends> suggestedFriendList; // variable to store data for all kippin user
    FriendAdapter friendAdapter;
    private SearchView searchView;
    private TextView txtcancel;
    private boolean flag = false;
    private String userId = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      updateToolBar();
        updateUI();
        addListener();
    }

    @Override
    public void updateToolBar() {
        generateActionBar(R.string.friend_list, true, true, false);
    }

    @Override
    public void updateUI() {
        layout_nonKippin.setVisibility(View.GONE);
        userId = String.valueOf(CommonData.getUserData(Activity_MyPoint_InviteKippinFriends_FriendList.this).getId());
        loadData();
    }
    @Override
    public void addListener() {
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(friendAdapter != null)
                friendAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(txtSearch.getText().toString()!=null){
                    getAllKippinUsrs(txtSearch.getText().toString().trim());
                }

                return false;
            }
        });
    }

    protected void onResume() {
        super.onResume();
        View focus = getCurrentFocus();
        if(focus!=null){
            focus.clearFocus();
        }
        Utils.hideKeyboard(this);

    }
    private void loadData() {

        RestClientAdavanced.getApiServiceForPojo(this).GetFriendList(CommonData.getUserData(this).getId(), getCallback(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e(jsonElement.toString(), jsonElement.toString());
                Gson gson = new Gson();
                String jsonOutput = jsonElement.toString();
                Type listType = new TypeToken<List<ModalFriends>>() {
                }.getType();
                modalFriendses = gson.fromJson(jsonElement, listType);


                for(int i = 0 ; i<modalFriendses.size() ; i ++){
                    modalFriendses.get(i).setIsInvited(true);
                }

                frientList =modalFriendses;
                if (Utility.isResponseValid(modalFriendses)) {
                    friendAdapter = new FriendAdapter(Activity_MyPoint_InviteKippinFriends_FriendList.this, modalFriendses,ListType.SHOW_FRIENDS);
                    list_data.setAdapter(friendAdapter);
                } else
                    MessageDialog.showDialog(Activity_MyPoint_InviteKippinFriends_FriendList.this, modalFriendses.get(0).getResponseMessage(), false);


            }

            @Override
            public void failure(RetrofitError error) {
                MessageDialog.showFailureDialog(Activity_MyPoint_InviteKippinFriends_FriendList.this);
            }
        }));

    }

    private void getAllKippinUsrs(String searchText)
    {
        RestClient.getApiServiceForPojo().GetFriendSuggestionListByUserId(userId, searchText, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.d("getAllKippinUsrs",response.getUrl());
                Gson gson = new Gson();
                String jsonOutput = jsonElement.toString();
                Type listType = new TypeToken<List<ModalFriends>>() {
                }.getType();
                modalFriendses =  suggestedFriendList = gson.fromJson(jsonOutput,listType);
                if (Utility.isResponseValid(modalFriendses)) {
                    Log.d("modalFriendses","iNSIDE IF");
                    friendAdapter = new FriendAdapter(Activity_MyPoint_InviteKippinFriends_FriendList.this, modalFriendses,ListType.INVITE_FRIENDS);
                    list_data.setAdapter(friendAdapter);
                } else {
                    Log.d("modalFriendses","iNSIDE ELSE");
                  //  MessageDialog.showDialog(Activity_MyPoint_InviteKippinFriends_FriendList.this, modalFriendses.get(0).getResponseMessage(), false);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                MessageDialog.showFailureDialog(Activity_MyPoint_InviteKippinFriends_FriendList.this);
                Log.d("getAllKippinUsrs",error.getUrl());
            }
        });
    }

    public enum ListType
    {
        SHOW_FRIENDS,INVITE_FRIENDS
    }

}
