package com.kippinretail.KippinInvoice.InvoiceAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by kamaljeet.singh on 12/21/2016.
 */

public class AdapterForReceivedClassification extends BaseAdapter implements AdapterView.OnItemClickListener {


    private final LayoutInflater mLayoutInflater;
    private final String inRFlowStatus;
    private final String userID;
    private final String pStatus;
    Holder holder;
    List<Revenue> revenueList1 = new ArrayList<>();
    StickyListHeadersListView lvList;
    AdapterReceivedInvoiceClassification adapterReceivedInvoiceClassification;
    Dialog dialog;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            adapterReceivedInvoiceClassification.filter(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private List<ClassificationList> classificationLists;
    private Activity mContext;
    private List<Revenue> revenueList;
    private List<Revenue> revenueListFilter = new ArrayList<>();
    private GoodsAdapter goodsAdapter;
    private String finance;
    private InterfaceUpdateClassification interfaceUpdateClassification;
    private InputMethodManager inputMethodManager;

    public AdapterForReceivedClassification(Activity activity, List<ClassificationList> classificationLists, String rFlowStatus, String userID, String pStatus,
                                            InterfaceUpdateClassification interfaceUpdateClassification) {
        mContext = activity;
        this.classificationLists = classificationLists;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inRFlowStatus = rFlowStatus;
        this.userID = userID;
        this.pStatus = pStatus;
        this.interfaceUpdateClassification = interfaceUpdateClassification;
        inputMethodManager = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        initRevenueList();
    }

    private void initRevenueList() {

        if (CommonData.getInvoiceUserData(mContext) == null) {
            finance = "2";
        } else {
            if (CommonData.getInvoiceUserData(mContext).getIsOnlyInvoice()) {
                finance = "1";
            } else {
                finance = "2";
            }
        }


        //}

        //  }


        //LoadingBox.showLoadingDialog(mContext, "");
        RestClient.getApiFinanceServiceForPojo().getExpenseList(userID, finance, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        //LoadingBox.dismissLoadingDialog();
                        Gson gson = new Gson();
                        Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                        revenueList = gson.fromJson(jsonElement.toString(), new TypeToken<List<Revenue>>() {
                        }.getType());
                        //dialogForRevenuelist(revenueList, mPosition);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //LoadingBox.dismissLoadingDialog();
                        Log.e("Failure", error.getUrl() + "");
                    }
                }

        );
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
        Log.e("classificationLists","HERE");
        Log.e("Iem:",""+classificationLists.get(position).getItem());
        Log.e("classificationLists:",""+classificationLists.get(position).getServiceType());


        if (inRFlowStatus.equalsIgnoreCase("Save") || inRFlowStatus.equalsIgnoreCase("Draft") || pStatus.equalsIgnoreCase("Pending Approval")) {
            holder.ivEdit.setVisibility(View.VISIBLE);
        } else {
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
            dialog = new Dialog(mContext);
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

            EditText etClassificationName = (EditText) dialog.findViewById(R.id.etClassificationName);
            //etClassificationName.setText(serviceType);
            RelativeLayout rlClassification = (RelativeLayout) dialog.findViewById(R.id.rlClassification);
            ImageView ivCrossButton = (ImageView) dialog.findViewById(R.id.ivCrossButton);
            lvList = (StickyListHeadersListView) dialog.findViewById(R.id.lvClassifications);

            etClassificationName.addTextChangedListener(textWatcher);

            if (revenueList.size() > 0) {

                revenueListFilter.clear();

                for (int i = 0; i < revenueList.size(); i++) {
                    if (revenueList.get(i).getCategoryId() == 1 ||
                            revenueList.get(i).getCategoryId() == 2) {
                        revenueListFilter.add(revenueList.get(i));
                    }
                }

                sortArrayElements(revenueListFilter);

                adapterReceivedInvoiceClassification = new AdapterReceivedInvoiceClassification(mContext, revenueListFilter);
                lvList.setAdapter(adapterReceivedInvoiceClassification);
                lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        interfaceUpdateClassification.updateClassication(mPosition, revenueListFilter.get(position).getId(), revenueListFilter.get(position).getClassificationType());
                    }
                });
            }

            /*rlClassification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkRevenue(dialog, mPosition);

                }
            });*/
            ivCrossButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            });

            dialog.show();
            mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
        dialog.setContentView(R.layout.asset_expense_dialog);
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;
        dialog.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        for (int i = 0; i < revenueList.size(); i++) {
            if (revenueList.get(i).getCategoryId() != 3) {
                revenueList1.add(revenueList.get(i));
            }
        }

        ListView listView = (ListView) dialog.findViewById(R.id.pinnedList);
        goodsAdapter = new GoodsAdapter(mContext, revenueList1, 1, 1);
        listView.setAdapter(goodsAdapter);

        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                interfaceUpdateClassification.updateClassication(mPosition, revenueList1.get(position).getId(), revenueList1.get(position).getClassificationType());
            }
        });


       /* } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }*/


    }

    private void sortArrayElements(List<Revenue> revenueListFilter) {
        Collections.sort(revenueListFilter, new Comparator<Revenue>() {
            @Override
            public int compare(Revenue lhs, Revenue rhs) {

                return Integer.valueOf(lhs.getCategoryId()).compareTo(rhs.getCategoryId());
            }
        });
    }

    private void checkRevenue(final Dialog dialog, final int mPosition) {

        if (CommonData.getInvoiceUserData(mContext).getIsOnlyInvoice()) {
            finance = "1";
        } else {
            finance = "2";
        }

        LoadingBox.showLoadingDialog(mContext, "");
        RestClient.getApiFinanceServiceForPojo().getExpenseList(userID, finance, new Callback<JsonElement>() {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dialog.dismiss();
        interfaceUpdateClassification.updateClassication(position, revenueList.get(position).getId(), revenueList.get(position).getClassificationType());
    }


    class Holder {

        TextView label, label1;
        ImageView ivDelete, ivEdit;
        int pos;

    }


}