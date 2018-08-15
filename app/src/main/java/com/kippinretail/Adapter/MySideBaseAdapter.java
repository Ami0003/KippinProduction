package com.kippinretail.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.ServerResponseForNotification.GetAllNotification.ResponseToGettAllNotification;
import com.kippinretail.R;
import com.kippinretail.callbacks.DialogListener;
import com.kippinretail.callbacks.OnNOtificationDelete;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by agnihotri on 21/01/18.
 */

public class MySideBaseAdapter extends BaseAdapter {


    LayoutInflater inflater;
    List<ResponseToGettAllNotification> listData;
    Activity mContext;

    int hei;
    int wid;
    OnNOtificationDelete delete;


    public MySideBaseAdapter(List<ResponseToGettAllNotification> listData, Activity activity, OnNOtificationDelete delete) {
        this.mContext = activity;
        inflater = LayoutInflater.from(this.mContext);

        this.listData = listData;
        this.mContext = activity;
      //  inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wid = CommonUtility.dpToPx(mContext, 45);
        hei = CommonUtility.dpToPx(mContext, 35);
        this.delete = delete;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public ResponseToGettAllNotification getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

       // if (convertView == null) {
            convertView = inflater.inflate(R.layout.subitem_notification_list, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
       // } else {
       //     mViewHolder = (MyViewHolder) convertView.getTag();
      //  }

        ResponseToGettAllNotification response = getItem(position);


        if(response.getProfileImage()!=null && response.getProfileImage().equals("")){
            Picasso.with(mContext).load(response.getProfileImage())
//                    .resize(wid,hei)
                    .into(mViewHolder.iv_profileImage);
        }else{
            Picasso.with(mContext).load(response.getProfileImage())
//                    .resize(wid,hei)
                    .into(mViewHolder.iv_profileImage);
        }
        String cardType = null;
        if(response.isFirstGiftcard()){
            cardType = "Gift Cards";
        }
        else if(response.isFirstPunchCard()){
            cardType = "Punch Cards";
        }
        else if(response.isFirstLoyaltyCard()){
            cardType = "Loyalty Cards";
        }
        mViewHolder.txt_name.setText(response.getBusinessName()+"  "+cardType);
        mViewHolder.iv_delete.setTag(response);
        mViewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseToGettAllNotification temp  = (ResponseToGettAllNotification)v.getTag();
                String mid = temp.getMerchantId();
                String Todelete = null;
                if(temp.isFirstGiftcard()){
                    Todelete = "IsFirstGiftcard";
                }
                else if(temp.isFirstPunchCard()){
                    Todelete = "IsFirstPunchCard";
                }
                else if(temp.isFirstLoyaltyCard()){
                    Todelete = "IsFirstLoyaltyCard";
                }
                deleteNotification(mid,Todelete);
            }
        });

        return convertView;
    }
    private void deleteNotification(final String mid,final String todelete) {

        try {
            MessageDialog.showVerificationDialog(mContext, "Are you sure you want to delete this Notification?", new DialogListener() {
                @Override
                public void handleYesButton() {
                    String userId = String.valueOf(CommonData.getUserData(mContext).getId());
                    LoadingBox.showLoadingDialog(mContext, "Loading ...");
                    RestClient.getApiServiceForPojo().deleteNotification(userId,mid, todelete, "", new Callback<JsonElement>() {
                        @Override
                        public void success(JsonElement jsonElement, Response response) {
                            Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());
                            LoadingBox.dismissLoadingDialog();
                            Gson gson = new Gson();
                            Boolean falg = gson.fromJson(jsonElement.toString(), Boolean.class);
                            if (falg.booleanValue()) {
                                MessageDialog.showDialog(mContext,"Notification Deleted" ,false);
                                delete.afterDelete();
                            } else {

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e("RestClient", error.getUrl() + "");
                            LoadingBox.dismissLoadingDialog();
                        }
                    });
                }
            });

        }catch (Exception ex){
            Log.e("136","Notification_Adapter");
        }
    }
    private class MyViewHolder {
        TextView txt_name;
        ImageView iv_profileImage,iv_delete;

        public MyViewHolder(View item) {
            txt_name = (TextView) item.findViewById(R.id.txt_name);
            iv_delete= (ImageView) item.findViewById(R.id.iv_delete);
            iv_profileImage = (ImageView) item.findViewById(R.id.iv_profileImage);
        }
    }
}
