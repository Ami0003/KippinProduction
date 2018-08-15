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
import com.kippinretail.Adapter.GiftCardListAdapter1;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.GiftCardType;
import com.kippinretail.ApplicationuUlity.NonKippinCardType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftCardMerchantList.MerchantDetail;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.MyPoint_FolderListActivity;
import com.kippinretail.NonKippinGiftCardListActivity;
import com.kippinretail.PurchasedGiftcardsActivity;
import com.kippinretail.R;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class FragmentGiftCard extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{
    private LinearLayout layout_nonKippin;
    private TextView txtNonKippin;
    private ImageView iv_updateProfile;
    private GiftCardListAdapter1 adapter = null;
    private List<MerchantDetail> responsemerchantDetails;
    ListView list_data;
    public static FragmentGiftCard newInstance() {
        FragmentGiftCard fragment = new FragmentGiftCard();
        return fragment;
    }

    public FragmentGiftCard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View localview =  inflater.inflate(R.layout.fragment_fragment_gift_card, container, false);
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
        
        txtNonKippin.setText("My Non KIPPIN Gift Cards");
        makeMyGiftCardList();
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
        i.putExtra("NonKippinCardType", NonKippinCardType.GIFTCARD);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
//        Intent i = new Intent(getActivity() , MyPoint_FolderListActivity.class);
//        i.putExtra("heading","MY GIFTS CARDS");
//        startActivity(i);
//        getActivity().overridePendingTransition(R.anim.animation_from_right,R.anim.animation_to_left);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        iv_updateProfile = (ImageView)getActivity().findViewById(R.id.iv_updateProfile);
//        iv_updateProfile.setVisibility(View.GONE);
    }

    private void makeMyGiftCardList() {
        if(CommonData.getUserData(getActivity())!=null) {
            String userId = String.valueOf(CommonData.getUserData(getActivity()).getId());
            LoadingBox.showLoadingDialog(getActivity(), "Loading...");
            RestClient.getApiServiceForPojo().MyGiftcardMerchantList(userId, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("Output -->", jsonElement.toString());
                    Log.e("Url",response.getUrl());
                    // USE GIFT CARD MERCHANT LIST MODAL
                    Type listtype = new TypeToken<List<MerchantDetail>>() {
                    }.getType();
                    Gson gson = new Gson();
                    responsemerchantDetails = (List<MerchantDetail>) gson.fromJson(jsonElement.toString(), listtype);
                    boolean flag = Utility.isResponseValid(responsemerchantDetails)  ;
                    if(flag){
                        adapter = new GiftCardListAdapter1(responsemerchantDetails,getActivity());
                        list_data.setAdapter(adapter);
                    }else{
                        // MessageDialog.showDialog(getActivity(), responsemerchantDetails.get(0).getResponseMessage(), false);
                    }

                    LoadingBox.dismissLoadingDialog();

                }
                @Override
                public void failure(RetrofitError error) {
                    Log.e("============",error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showDialog(getActivity(), "We are experiencing technical difficulties but value your business… please try again later");
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent();
        i.setClass(getActivity(), PurchasedGiftcardsActivity.class);
        i.putExtra("merchantId",responsemerchantDetails.get(position).getId());
        i.putExtra("businessName", responsemerchantDetails.get(position).getBusinessName());
        i.putExtra("GiftCardType", GiftCardType.MYGIFTCARD);

    startActivity(i);
    getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }
}
