package com.kippinretail.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.MerchantVoucherListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.R;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class FragmentVoucher extends Fragment implements View.OnClickListener{

    EditText txtSearch;
    ListView list_data;
    LinearLayout layout_nonKippin;
    private String customerId;

    ArrayList<MerchantModal> merchant = null;
    MerchantVoucherListAdapter adapter = null;
    RelativeLayout layout_container_search,layout_dialog;



    public static FragmentVoucher newInstance() {
        FragmentVoucher fragment = new FragmentVoucher();
        return fragment;
    }

    public FragmentVoucher() {
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
        View localview =  inflater.inflate(R.layout.fragment_fragment_voucher, container, false);
        initUI(localview);
        updateUI();
        addListener();
        return  localview;
    }

    private void initUI(View v){
         txtSearch = (EditText)v.findViewById(R.id.txtSearch);
        list_data = (ListView)v.findViewById(R.id.list_data);
        layout_nonKippin = (LinearLayout)v.findViewById(R.id.layout_nonKippin);
        layout_container_search = (RelativeLayout)v.findViewById(R.id.layout_container_search);
        layout_dialog = (RelativeLayout)v.findViewById(R.id.layout_dialog);

    }
    private void updateUI(){
        layout_nonKippin.setVisibility(View.GONE);
        layout_nonKippin.setVisibility(View.GONE);
//        Intent i = new Intent();
    }
    private void addListener(){
        layout_nonKippin.setOnClickListener(this);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence searchText, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(searchText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        list_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.layout_nonKippin:

                break;
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.hideKeyboard(getActivity());
        merchant = new ArrayList<MerchantModal>();
        makeVoucherList();
    }

    private void makeVoucherList()
    {
        merchant.clear();
        customerId = String.valueOf(CommonData.getUserData(getActivity()).getId());
        LoadingBox.showLoadingDialog(getActivity(), "Loading...");
        RestClient.getApiServiceForPojo().GetEnabledMerchantList(customerId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("User My voucher", jsonElement.toString());
                Type listType = new TypeToken<List<Merchant>>() {
                }.getType();
                Gson gson = new Gson();
                List<Merchant> merchants = (List<Merchant>) gson.fromJson(jsonElement.toString(), listType);
                if (merchants != null) {
                    if (merchants.size() == 1 && !merchants.get(0).getResponseMessage().equals("Success")) {
                        MessageDialog.showDialog(getActivity(), "No Merchant has been enabled",false);
                    } else {
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
                        try {
                            adapter = new MerchantVoucherListAdapter(getActivity(), merchant, null);
                            list_data.setAdapter(adapter);
                        }catch(Exception e){

                        }
                    }
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("output", "failore".toString());
                LoadingBox.dismissLoadingDialog();
            }
        });
    }
}
