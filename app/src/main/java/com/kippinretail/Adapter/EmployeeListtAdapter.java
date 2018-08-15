package com.kippinretail.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.ServerResponseForEmployeeList.EmployeeListServerResponse;
import com.kippinretail.R;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sandeep.singh on 7/19/2016.
 */
public class EmployeeListtAdapter extends BaseAdapter implements Filterable {
    List<EmployeeListServerResponse> listData;
    Activity mContext ;
    LayoutInflater inflater;
    int hei,wei;
    private ItemFilter mfilter = new ItemFilter();
    private String merchantId;
    private List<EmployeeListServerResponse> originalData_merchant;

    public EmployeeListtAdapter(List<EmployeeListServerResponse> listData , Activity activity,String merchantId){
        this.originalData_merchant = this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext,70);
        wei = CommonUtility.dpToPx(mContext,70);
    }

    @Override
    public int getCount() {
        if(listData!=null){
            return listData.size();
        }
        else{
            return  0;
        }
    }
    @Override
    public Object getItem(int position) {
        return
                listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    View lastView;
    int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = (View)inflater.inflate(R.layout.item_friend_list, parent, false);
            holder.profile_image =(CircleImageView) convertView.findViewById(R.id.profile_image);
            holder.tvName =(TextView) convertView.findViewById(R.id.tvName);
            holder.tvInvite =(TextView) convertView.findViewById(R.id.tv_Invite);
            convertView.setTag(holder);

        }
        else{
            holder = (ViewHolder)convertView.getTag();
            holder.tvName.setText("");
            holder.tvInvite.setText("");
        }
        EmployeeListServerResponse response = listData.get(position);
        if(response.getProfileImage()!=null && !response.getProfileImage().equals("")){
            Picasso.with(mContext).load(response.getProfileImage()).resize(hei, wei).placeholder(R.drawable.icon_placeholder).into(holder.profile_image);
        }
        else{
            Picasso.with(mContext).load(response.getProfileImage()).resize(hei,wei).placeholder(R.drawable.icon_placeholder).into(holder.profile_image);
        }

        holder.tvName.setText(response.getFirstname() + " " + response.getLastname());
        holder.tvInvite.setBackgroundColor(mContext.getResources().getColor(R.color.b_click));
        holder.tvInvite.setText("Delete");
        holder.tvInvite.setTag(new Integer(response.getId()));
        holder.tvInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = (Integer) v.getTag();
                deleteEmployee(id.toString());
            }
        });

        final View finalView = convertView;
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (lastView != null && lastPosition != position) {
                    lastView.setBackgroundColor(Color.WHITE);
                }

                if (lastPosition != position) {
                    lastPosition = position;
                    lastView = finalView;
                    finalView.setBackgroundColor(Color.LTGRAY);
                }

                return false;
            }
        });


        if(lastPosition ==position){
            convertView.setBackgroundColor(Color.LTGRAY);
        }else if(lastPosition!=position && lastPosition>=0){
            convertView.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    class ViewHolder{
        CircleImageView profile_image;
        TextView tvName;
        TextView tvInvite;
    }
    class ItemFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<EmployeeListServerResponse> temp = originalData_merchant;

            List<EmployeeListServerResponse> filterdata = new ArrayList<EmployeeListServerResponse>();
            FilterResults results = new FilterResults();

            for(int cnt = 0;cnt<temp.size() ; cnt++)
            {
                Log.e("===" + temp.get(cnt).getUsername(), constraint.toString());
                if(temp.get(cnt).getUsername().contains(constraint.toString())){
                    filterdata.add(temp.get(cnt));
                }
            }
            Log.e("filterdata",filterdata.size()+"");
            results.values = filterdata;
            results.count = filterdata.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listData = (ArrayList <EmployeeListServerResponse>)results.values;
            notifyDataSetChanged();
        }
    }

    private void deleteEmployee(final String empId){

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(com.kippin.kippin.R.layout.dialog_box_yes_no);;
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;


        TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
        textForMessage.setText("Are you sure you want to delete this employee?");
        TextView btnYES = (TextView) dialog.findViewById(R.id.yes_btn);
        btnYES.setText("YES");
        TextView btnNO = (TextView) dialog.findViewById(R.id.no_btn);
        btnNO.setText("NO");
        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                LoadingBox.showLoadingDialog(mContext, "Please Wait ...");
                RestClient.getApiServiceForPojo().deleteEmployeeFromList(merchantId, empId, "", new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());
                        LoadingBox.dismissLoadingDialog();
                        Gson gson = new Gson();
                        Boolean temp =  gson.fromJson(jsonElement.toString(), Boolean.class);
                        if(temp.booleanValue()){
                            MessageDialog.showDialog(mContext,"Employee successfully deleted",true);
                        }else{
                            MessageDialog.showDialog(mContext,"Fail",true);
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("RestClient", error.getUrl() + " : " + error.getMessage());
                        LoadingBox.dismissLoadingDialog();
                    }
                });
            }
        });
        dialog.show();


/*
        MessageDialog.showVerificationDialog(mContext, "Are you sure you want to delete this employee?", new DialogListener() {
            @Override
            public void handleYesButton() {
                LoadingBox.showLoadingDialog(mContext, "Please Wait ...");
                RestClient.getApiServiceForPojo().deleteEmployeeFromList(merchantId, empId, "", new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        android.util.Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());
                        LoadingBox.dismissLoadingDialog();
                        Gson gson = new Gson();
                       Boolean temp =  gson.fromJson(jsonElement.toString(), Boolean.class);
                        if(temp.booleanValue()){
                            MessageDialog.showDialog(mContext,"Employee successfully deleted",true);
                        }else{
                            MessageDialog.showDialog(mContext,"Fail",true);
                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        android.util.Log.e("RestClient", error.getUrl() + " : " + error.getMessage());
                        LoadingBox.dismissLoadingDialog();
                    }
                });
            }
        });
*/

    }
}
