package com.kippinretail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.AdapterDonateToCharity;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.OnDonateToCharity;
import com.kippinretail.Modal.donateToCharity.ModalDonateToCharity;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityDonateToCharity extends AbstractListActivity implements OnDonateToCharity {


    private List<ModalDonateToCharity> list;
    ArrayList<ModalDonateToCharity> donateToCharities;
    private AdapterDonateToCharity adapterDonateToCharity = null;
    Callback<JsonElement> elementCallback = new Callback<JsonElement>() {
        @Override
        public void success(JsonElement jsonElement, Response response) {
            Log.e("Output ==>", jsonElement.toString());
            Type listType = new TypeToken<List<ModalDonateToCharity>>() {
            }.getType();
            Gson gson = new Gson();

            list = (List<ModalDonateToCharity>) gson.fromJson(jsonElement.toString(), listType);
            boolean flag = Utility.isResponseValid(list);
            if (flag) {
                donateToCharities.addAll(list);
                adapterDonateToCharity = new AdapterDonateToCharity(ActivityDonateToCharity.this, list, ActivityDonateToCharity.this);
                list_data.setAdapter(adapterDonateToCharity);
            } else {
                MessageDialog.showDialog(ActivityDonateToCharity.this, list.get(0).getResponseMessage(), false);
            }

            LoadingBox.dismissLoadingDialog();
        }

        @Override
        public void failure(RetrofitError error) {

            MessageDialog.showDialog(ActivityDonateToCharity.this, "We are experiencing technical difficulties but value your business… please try again later");
        }
    };





    private ShareType shareType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        donateToCharities=new ArrayList<>();
        donateToCharities.clear();
        updateToolBar();
        updateUI();
        addListener();

    }

    @Override
    public void updateToolBar() {
        generateActionBar(R.string.title_user_charitable, true, true, false);
    }

    @Override
    public void updateUI() {
        shareType = (ShareType) getIntent().getSerializableExtra("shareType");
        layout_nonKippin.setVisibility(View.GONE);
        String userId = CommonData.getUserData(this).getId() + "";
        getApiServiceForPojo().GetDonateToCharityUserList(getCallback(elementCallback));
        Utils.hideKeyboard(this);

    }

    @Override
    public void addListener() {
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //if (adapterDonateToCharity != null)
                String text = txtSearch.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);
                    //adapterDonateToCharity.getFilter().filter(s.toString().toLowerCase(Locale.getDefault()));
            }
        });

    }
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        android.util.Log.e("charText:",""+charText);
        list.clear();
        if (charText.length() == 0) {
            android.util.Log.e("SIIZZEE: ",""+donateToCharities.size());
            list.addAll(donateToCharities);
        }
        else
        {
            for (ModalDonateToCharity wp : donateToCharities)
            {
                android.util.Log.e("WP:::",""+wp.getBusinessName());
                if(wp.getBusinessName()!=null){
                    if (wp.getBusinessName().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        list.add(wp);
                    }
                }

            }
        }
        android.util.Log.e("filtereddata:",""+list.size());
        adapterDonateToCharity = new AdapterDonateToCharity(ActivityDonateToCharity.this, list, ActivityDonateToCharity.this);
        list_data.setAdapter(adapterDonateToCharity);
        adapterDonateToCharity.notifyDataSetChanged();

    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onDonate(String userId) {
        if (shareType == ShareType.DONATEPOINT) {
            Intent intent = new Intent(this, ActivityMerchantList.class);
            intent.putExtra("shareType", shareType);
            intent.putExtra(getString(R.string.user), userId);
            startActivity(intent);
            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        } else if (shareType == ShareType.DONATEGIFTCARD) {
            // Check Stripe account is available or not
            checkForIsStripeAccountAvailable(userId);
        }

    }

    private void checkForIsStripeAccountAvailable(final String userId) {

        RestClient.getApiServiceForPojo().StripeAvailability(userId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement uploadVoucher, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Stripe Check  Tag", "Request data " + new Gson().toJson(uploadVoucher));
                //[{"Secretkey":null,"Privatekey":null,"ResponseCode":5,"UserId":15,"ResponseMessage":"Not Available."}]
                try {
                    JSONArray jsonarray = new JSONArray(uploadVoucher.toString());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String ResponseCode = jsonobject.getString("ResponseCode");

                        if (ResponseCode.equals("1")) {
                            Intent intent = new Intent(ActivityDonateToCharity.this, SelectCharityTypeActivity.class);
                            intent.putExtra("shareType", shareType);
                            intent.putExtra(getString(R.string.user), userId);
                            //intent.putExtra(getString(R.string.user), list.get(pos));
                            startActivity(intent);
                            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        } else {
                            CommonDialog.With(ActivityDonateToCharity.this).Show(getResources().getString(R.string.stripe_account_not_setup));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Stripe Check Tag", " = " + e.toString());
                }


            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Stripe Check Get Failure => ", " = " + error.getMessage());
                MessageDialog.showDialog(ActivityDonateToCharity.this, CommonUtility.TIME_OUT_MESSAGE, true);

            }

        });


    }


    @Override
    public void onDonate(ModalDonateToCharity merModalDonateToCharity) {

    }


}
