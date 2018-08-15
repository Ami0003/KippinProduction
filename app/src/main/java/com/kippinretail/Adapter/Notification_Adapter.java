package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
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
 * Created by sandeep.singh on 7/20/2016.
 */
public class Notification_Adapter extends BaseAdapter {
    List<ResponseToGettAllNotification> listData;
    Activity mContext;
    LayoutInflater inflater;
    int hei;
    int wid;
    OnNOtificationDelete delete;

    public Notification_Adapter(List<ResponseToGettAllNotification> listData, Activity activity,OnNOtificationDelete delete) {
        this.listData = listData;
        this.mContext = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wid = CommonUtility.dpToPx(mContext, 45);
        hei = CommonUtility.dpToPx(mContext, 35);
        this.delete = delete;
    }

    @Override
    public int getCount() {
        if (listData != null) {
            return listData.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = (View) inflater.inflate(R.layout.subitem_notification_list, parent, false);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.iv_profileImage = (ImageView) convertView.findViewById(R.id.iv_profileImage);
            holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.txt_name.setText("");
        }
        ResponseToGettAllNotification response = listData.get(position);
        if(response.getProfileImage()!=null && response.getProfileImage().equals("")){
            Picasso.with(mContext).load(response.getProfileImage())
//                    .resize(wid,hei)
                    .into(holder.iv_profileImage);
        }else{
            Picasso.with(mContext).load(response.getProfileImage())
//                    .resize(wid,hei)
                    .into(holder.iv_profileImage);
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
        holder.txt_name.setText(response.getBusinessName()+"  "+cardType);
        holder.iv_delete.setTag(response);
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
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

    class ViewHolder {

        ImageView iv_profileImage,iv_delete;
        TextView txt_name;
    }
}
