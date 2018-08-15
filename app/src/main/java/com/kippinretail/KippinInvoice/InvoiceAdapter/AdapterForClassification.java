package com.kippinretail.KippinInvoice.InvoiceAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.KippinInvoice.InvoiceInterface.InterfaceUpdateClassification;
import com.kippinretail.KippinInvoice.ModalInvoice.ClassificationList;
import com.kippinretail.KippinInvoice.ModalInvoice.Revenue;
import com.kippinretail.R;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 12/2/2016.
 */

public class AdapterForClassification extends BaseAdapter {


    private final LayoutInflater mLayoutInflater;
    private final String inRFlowStatus;
    private final String userID;
    private final String pStatus;
    Holder holder;
    private List<ClassificationList> classificationLists;
    private Activity mContext;
    private List<Revenue> revenueList;
    private GoodsAdapter goodsAdapter;
    private String finance;
    private InterfaceUpdateClassification interfaceUpdateClassification;

    public AdapterForClassification(Activity activity, List<ClassificationList> classificationLists, String rFlowStatus, String userID, String pStatus,
                                    InterfaceUpdateClassification interfaceUpdateClassification) {
        mContext = activity;
        this.classificationLists = classificationLists;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inRFlowStatus = rFlowStatus;
        this.userID = userID;
        this.pStatus = pStatus;
        this.interfaceUpdateClassification = interfaceUpdateClassification;
    }


    @Override
    public int getCount() {
        return classificationLists.size();
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
                    R.layout.sub_item_invoice_classification, null);
            holder = new Holder();

            holder.label = (TextView) convertView.findViewById(R.id.label);
            holder.label1 = (TextView) convertView.findViewById(R.id.label1);
            holder.ivEdit = (ImageView) convertView.findViewById(R.id.ivEdit);

            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.label.setText(classificationLists.get(position).getItem());
        holder.label1.setText(classificationLists.get(position).getServiceType());

        try {
            if (inRFlowStatus.equalsIgnoreCase("Save") || inRFlowStatus.equalsIgnoreCase("Draft") || pStatus.equalsIgnoreCase("Pending Approval")) {
                holder.ivEdit.setVisibility(View.VISIBLE);
            } else {
                holder.ivEdit.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            holder.ivEdit.setVisibility(View.GONE);
        }
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogToEdit(classificationLists.get(position).getItem(), classificationLists.get(position).getServiceTypeId(), classificationLists.get(position).getServiceType(), position);
            }
        });

       /* if(){

            holder.ivEdit.setVisibility(View.VISIBLE);
        }else{
            holder.ivEdit.setVisibility(View.GONE);
        }*/

        return convertView;
    }

    private void dialogToEdit(String item, Integer serviceTypeId, String serviceType, final int mPosition) {
        try {
            final Dialog dialog = new Dialog(mContext);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_for_revenue_list);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            TextView tvItemName = (TextView) dialog.findViewById(R.id.tvItemName);
            tvItemName.setText("Item Name: " + item);

            TextView tvClassificationName = (TextView) dialog.findViewById(R.id.tvClassificationName);
            tvClassificationName.setText(serviceType);
            RelativeLayout rlClassification = (RelativeLayout) dialog.findViewById(R.id.rlClassification);
            ImageView ivCrossButton = (ImageView) dialog.findViewById(R.id.ivCrossButton);

            rlClassification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkRevenue(dialog, mPosition);

                }
            });
            ivCrossButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }

    }

    private void dialogForRevenuelist(final List<Revenue> revenueList, final int mPosition) {

        // try {
        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.revenue_dialog);
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;
        dialog.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ListView listView = (ListView) dialog.findViewById(R.id.listView_revenue);
        goodsAdapter = new GoodsAdapter(mContext, revenueList, 1, 1);
        listView.setAdapter(goodsAdapter);

        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
                interfaceUpdateClassification.updateClassication(mPosition, revenueList.get(position).getId(), revenueList.get(position).getClassificationType());
            }
        });


       /* } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }*/


    }

    private void checkRevenue(final Dialog dialog, final int mPosition) {

        if (CommonData.getInvoiceUserData(mContext).getIsOnlyInvoice()) {
            finance = "1";
        } else {
            finance = "2";
        }

        LoadingBox.showLoadingDialog(mContext, "");
        RestClient.getApiFinanceServiceForPojo().getRevenueList(userID, finance, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        dialog.dismiss();
                        Gson gson = new Gson();
                        Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                        revenueList = gson.fromJson(jsonElement.toString(), new TypeToken<List<Revenue>>() {
                        }.getType());
                        dialogForRevenuelist(revenueList, mPosition);


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Failure", error.getUrl() + "");
                    }
                }

        );

    }

    class Holder {
        TextView label, label1;
        ImageView ivDelete, ivEdit;
        int pos;

    }
}