package com.kippinretail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftCardMerchant.GiftCardMerchantData;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.squareup.picasso.Picasso;
import java.lang.reflect.Type;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 3/23/2016.
 */
public class GiftCardsMerchantActivity extends SuperActivity implements View.OnClickListener {

    public static final String PUNCHCARD = "punchcard";
    public static final String GIFTCARD = "giftcard";
    public static final String OPERATION = "operation";
    GridView gridView1;
    Button selectTemplateButton;
    private List<GiftCardMerchantData> giftCardsList;
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftcard_merchant);
        Initlization();
    }

    private void Initlization() {
        generateActionBar(R.string.title_merchant_template_giftcard,true,true,false);
        CommonUtility.TemplateIdValue="";
        gridView1 = (GridView) findViewById(R.id.gridView1);
        selectTemplateButton = (Button) findViewById(R.id.selectTemplateButton);
        selectTemplateButton.setOnClickListener(this);
        TemplateList();
    }

    private void TemplateList() {

        String merchantId = "" + CommonData.getUserData(GiftCardsMerchantActivity.this).getId();
        Log.e("merchantId", "==" + merchantId);
        LoadingBox.showLoadingDialog(GiftCardsMerchantActivity.this, "");

     //   if(getIntent().getStringExtra(OPERATION).equals(GIFTCARD))
            RestClient.getApiServiceForPojo().QueryToGiftCardsMerchant(callback);
     //   else RestClient.getApiServiceForPojo().GetFreePunchCardTemplatesList(callback);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    Callback<JsonElement> callback =new Callback<JsonElement>() {
        @Override
        public void success(JsonElement loginData, Response response) {
            LoadingBox.dismissLoadingDialog();
            Log.i("Tag", "Request data " + new Gson().toJson(loginData));
            Gson gson = new Gson();
            String jsonOutput = loginData.toString();
            Type listType = new TypeToken<List<GiftCardMerchantData>>() {
            }.getType();

            giftCardsList = (List<GiftCardMerchantData>) gson.fromJson(jsonOutput, listType);
            Log.e("giftCardsList SIZEEE", "==" + giftCardsList.size());
            AdapterForGiftCardMerchant adap = new AdapterForGiftCardMerchant(GiftCardsMerchantActivity.this, giftCardsList);
            gridView1.setAdapter(adap);
        }

        @Override
        public void failure(RetrofitError error) {
            LoadingBox.dismissLoadingDialog();
            Log.e("GiftCard Get Failure => ", " = " + error.getMessage());
            MessageDialog.showFailureDialog(GiftCardsMerchantActivity.this);

        }

    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.selectTemplateButton:

                if (!CommonUtility.TemplateIdValue.equals("")) {
                    Intent in = new Intent();

                  //  if(getIntent().getStringExtra(OPERATION).equals(GIFTCARD))
                    in.setClass(GiftCardsMerchantActivity.this,Activate_MerchantCardActivity.class);
                 //   else
                //        in.setClass(GiftCardsMerchantActivity.this, Activate_PunchCardActivity.class);

                    startActivity(in);
                    overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                }
                else{
                    CommonDialog.With(GiftCardsMerchantActivity.this).Show("Please select any template");
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }


    public class AdapterForGiftCardMerchant extends BaseAdapter {
        Integer selected_position = -1;
        Holder holder;
        private final GiftCardsMerchantActivity mContext;
        private final List<GiftCardMerchantData> mGiftCardsList;
        private final LayoutInflater mLayoutInflater;
        boolean[] Check_Tick;

        public AdapterForGiftCardMerchant(GiftCardsMerchantActivity giftCardsMerchantActivity, List<GiftCardMerchantData> giftCardsList) {
            mContext = giftCardsMerchantActivity;
            mGiftCardsList = giftCardsList;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Check_Tick = new boolean[mGiftCardsList.size()];
            for (int l = 0; l < mGiftCardsList.size(); l++) {
                Check_Tick[l] = false;
            }

        }

        @Override
        public int getCount() {
            return mGiftCardsList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(
                        R.layout.rowitem_gift_card_merchant, null);
                holder = new Holder();


                holder.checkBox1 = (CheckBox) convertView.findViewById(R.id.checkBox1);
                holder.grid_image = (ImageView) convertView.findViewById(R.id.grid_image);

                convertView.setTag(holder);


            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.pos = position;
            holder.checkBox1.setTag(holder);
            holder.grid_image.setTag(holder);

            int width = mContext.getResources().getDisplayMetrics().widthPixels;
            Picasso.with(mContext)
                    .load(mGiftCardsList.get(position).getTemplatePath()).resize(width / 2, width / 2)
                     .into(holder.grid_image);


            holder.checkBox1.setId(position);
            holder.checkBox1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String temp_TemplateIdValue="";
                    for (int i = 0; i < Check_Tick.length; i++) {

                           if(!Check_Tick[i]) {
                               if (v.getId() == i)
                               {
                                   Check_Tick[i] = true;
                                   Log.v("check", "" + position);
                                    temp_TemplateIdValue = "" + mGiftCardsList.get(position).getId();
                                   CommonUtility.TemplateImagePath = mGiftCardsList.get(position).getTemplatePath();
                               } else {
                                   Check_Tick[i] = false;

                               }
                           }
                        else{
                               Check_Tick[i] = false;
                               CommonUtility.TemplateIdValue="";// Goint to clear value for previous check
                           }


                    }
                    CommonUtility.TemplateIdValue = temp_TemplateIdValue;// sTORE VALUE FOR CHECK;
                    notifyDataSetChanged();
                }
            });
            if (Check_Tick[position]) {
                holder.checkBox1.setChecked(true);
            } else {
                holder.checkBox1.setChecked(false);
            }

            return convertView;
        }

        class Holder {
            public ImageView grid_image;
            CheckBox checkBox1;
            int pos;

        }
    }


}
