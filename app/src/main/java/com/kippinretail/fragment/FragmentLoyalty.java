package com.kippinretail.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ActivityMyPunchCards;
import com.kippinretail.ActivityUpdateNonKippinLoyaltyCard;
import com.kippinretail.Adapter.MyLoayaltyCardListAdapter;
import com.kippinretail.Adapter.NonKippinGiftCardListAdapter1;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.NonKippinCardType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.ResponseToGetLoyaltyCard;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.MyPoint_FolderListActivity;
import com.kippinretail.NonKippinGiftCardListActivity;
import com.kippinretail.R;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FragmentLoyalty extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    private LinearLayout layout_nonKippin;
    private TextView txtNonKippin;
    private ImageView iv_updateProfile,iv_home;
    private MyLoayaltyCardListAdapter adapter;
    private List<Merchant> responsemerchants;
    ListView list_data;
    public static FragmentLoyalty newInstance() {
        FragmentLoyalty fragment = new FragmentLoyalty();
        return fragment;
    }

    public FragmentLoyalty() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View localview =  inflater.inflate(R.layout.fragment_fragment_loyalty, container, false);
        initUI(localview);
        updateUI();
        addListener();
        return  localview;
    }

    private void initUI(View v){
        layout_nonKippin = (LinearLayout)v.findViewById(R.id.layout_nonKippin);
        list_data = (ListView)v.findViewById(R.id.list_data);
        txtNonKippin = (TextView)v.findViewById(R.id.txtNonKippin);
    }
    private void updateUI(){
        layout_nonKippin.setVisibility(View.VISIBLE);
        txtNonKippin.setText("My Non KIPPIN Loyalty Cards");
        makeSubcribedMerchantList();
    }
        private void addListener(){
            layout_nonKippin.setOnClickListener(this);
            list_data.setOnItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.layout_nonKippin:
                    nonKippin();
                    break;
        }
    }

    private void nonKippin(){
        Intent i = new Intent();
        i.setClass(getActivity(), NonKippinGiftCardListActivity.class);
        i.putExtra("NonKippinCardType", NonKippinCardType.LOYALTYCARD);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iv_home = (ImageView)getActivity().findViewById(R.id.iv_home_retail);
        iv_updateProfile = (ImageView)getActivity().findViewById(R.id.iv_updateProfile);
        iv_updateProfile.setVisibility(View.GONE);
        iv_home.setVisibility(View.VISIBLE);

    }
    private void makeSubcribedMerchantList(){
        Log.e("makeSubcribantList()", "makeSubcribedMerchantList();");
        String userId = String.valueOf(CommonData.getUserData(getActivity()).getId());
        LoadingBox.showLoadingDialog(getActivity(), "Loading...");
        RestClient.getApiServiceForPojo().GetSubscribedMerchantListForLoyalityCard(userId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output ==>", jsonElement.toString());
                Log.e("URL -->", response.getUrl());
                Type listType = new TypeToken<List<Merchant>>() {
                }.getType();
                Gson gson = new Gson();
                responsemerchants = (List<Merchant>) gson.fromJson(jsonElement.toString(), listType);
                LoadingBox.dismissLoadingDialog();
                boolean flag = Utility.isResponseValid(responsemerchants);
                if (flag) {
                    adapter = new MyLoayaltyCardListAdapter(responsemerchants, getActivity());
                    list_data.setAdapter(adapter);
                } else {
                    //  MessageDialog.showDialog(MgetActivity(), responsemerchants.get(0).getResponseMessage(), false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(error.getUrl(), error.getMessage());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(getActivity(), "We are experiencing technical difficulties but value your business… please try again later");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Merchant cardDetail =  responsemerchants.get(position);
        Intent i = new Intent();
        i.setClass(getActivity(), ActivityMyPunchCards.class);
        i.putExtra("cardDetail", cardDetail);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }
 
}
