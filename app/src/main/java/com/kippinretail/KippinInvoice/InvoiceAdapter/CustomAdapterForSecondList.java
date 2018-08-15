package com.kippinretail.KippinInvoice.InvoiceAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.KippinInvoice.EditReceivedInvoice;
import com.kippinretail.KippinInvoice.EditReceivedPerforma;
import com.kippinretail.KippinInvoice.EditSentInvoice;
import com.kippinretail.KippinInvoice.EditSentPerforma;
import com.kippinretail.KippinInvoice.InvoicePdfViewActivity;
import com.kippinretail.KippinInvoice.ModalInvoice.EditRejectedInvoice;
import com.kippinretail.KippinInvoice.ModalInvoice.ReceivedInvoiceList;
import com.kippinretail.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.kippinretail.config.Config.Invoice_PDF_URL;

/**
 * Created by kamaljeet.singh on 11/30/2016.
 */

public class CustomAdapterForSecondList extends BaseAdapter {
    private final Activity activity;
    private final List<ReceivedInvoiceList> receivedLists;
    private final String roleId;
    ViewHolder viewHolder;
    DecimalFormat formatter = new DecimalFormat("#,###,###");

    public CustomAdapterForSecondList(Activity activity, List<ReceivedInvoiceList> receivedLists, String roleId) {

        this.activity = activity;
        this.receivedLists = receivedLists;
        this.roleId = roleId;

    }

    @Override
    public int getCount() {
        return receivedLists.size();
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
        if (true) {
            viewHolder = new ViewHolder();
            convertView = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sub_item_list, parent, false);

            viewHolder.tvSupplierName = (TextView) convertView.findViewById(R.id.tvSupplierName);
            viewHolder.tvInvtAmount = (TextView) convertView.findViewById(R.id.tvInvtAmount);
            viewHolder.tvFlowStatus = (TextView) convertView.findViewById(R.id.tvFlowStatus);
            viewHolder.ivEdit = (ImageView) convertView.findViewById(R.id.ivEdit);
            viewHolder.ivPreview = (ImageView) convertView.findViewById(R.id.ivPreview);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvInvtAmount.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(receivedLists.get(position).getTotal()));

        if (!CommonUtility.Sent_Receiver.equals("1")) {
            viewHolder.tvSupplierName.setText(receivedLists.get(position).getFirstName());
            //  if (!receivedLists.get(position).getProFlowStatus().equals("Pending Approval")&&receivedLists.get(position).getProFlowStatus().toString()!=null) {
            viewHolder.tvFlowStatus.setText(receivedLists.get(position).getInRFlowStatus());
//            } else {
//                viewHolder.tvFlowStatus.setText("Sent");
//            }
        } else {
            viewHolder.tvSupplierName.setText(receivedLists.get(position).getUsername());
            viewHolder.tvFlowStatus.setText(receivedLists.get(position).getProFlowStatus());
        }

        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtility.Sent_Receiver.equals("1")) {
                    Log.e("roleId:", "" + roleId);
                    if (roleId.equals("1")) {
                        if (receivedLists.get(position).getInRFlowStatus() != null) {
                            if (!receivedLists.get(position).getInRFlowStatus().equals("Pending Approval")&&!receivedLists.get(position).getInRFlowStatus().equals("Sent")&&!receivedLists.get(position).getInRFlowStatus().equals("Approved")) {
                                Log.e("IF","IF");
                                Log.e("Status:",""+receivedLists.get(position).getInRFlowStatus());
                                CommonUtility.InvoiceTitle = "1";
                                callNext(roleId, EditRejectedInvoice.class, new Gson().toJson(receivedLists.get(position)));

                            } else {
                                Log.e("else","else");
                                Log.e("ELSE Status:",""+receivedLists.get(position).getInRFlowStatus());
                                callNext(roleId, EditSentInvoice.class, new Gson().toJson(receivedLists.get(position)));
                            }
                        } else {
                            Log.e("finalelse","finalelse");

                            callNext(roleId, EditSentInvoice.class, new Gson().toJson(receivedLists.get(position)));
                        }
                       /* if (!receivedLists.get(position).getProFlowStatus().equals("Sent")) {
                            callNext(roleId, CreateInvoiceActivity.class, new Gson().toJson(receivedLists.get(position)));

                        } else {
                            callNext(roleId, EditSentInvoice.class, new Gson().toJson(receivedLists.get(position)));
                        }*/
                    } else {
                        if (!receivedLists.get(position).getInRFlowStatus().equals("Sent")&&!receivedLists.get(position).getInRFlowStatus().equals("Approved")) {
                            CommonUtility.InvoiceTitle = "2";
                            callNext(roleId, EditRejectedInvoice.class, new Gson().toJson(receivedLists.get(position)));

                        } else {
                            callNext(roleId, EditSentPerforma.class, new Gson().toJson(receivedLists.get(position)));  }

                    }

                } else {
                    if (roleId.equals("1")) {
                        callNext(roleId, EditReceivedInvoice.class, new Gson().toJson(receivedLists.get(position)));
                    } else {
                        callNext(roleId, EditReceivedPerforma.class, new Gson().toJson(receivedLists.get(position)));
                    }
                }

            }
        });


        viewHolder.ivPreview.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                String isCutomer;
                if (receivedLists.get(position).getIsCustomer().equals("true")) {
                    isCutomer = "1";
                } else {
                    isCutomer = "0";
                }
                getPdfPath("" + receivedLists.get(position).getId(), isCutomer);
            }
        });

        return convertView;
    }

    private void getPdfPath(String invoiceID, String isCustomer) {
        //((SuperActivity)activity).getUserId()

        /**
         *  We have to load PDF directly in webview without hit any web api.
         */
        Intent in = new Intent();
        in.setClass(activity, InvoicePdfViewActivity.class);
        in.putExtra("urlpath", (Invoice_PDF_URL + invoiceID));
        activity.startActivity(in);
        activity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

       /* LoadingBox.showLoadingDialog(activity, "Wait ...");
        RestClient.getApiFinanceServiceForPojo().getNewInvoicePDF(invoiceID, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", jsonElement.toString().replace("\"", "") + " \n " + response.getUrl());
                        //"D:\\websites\\kippinfinance\\MobileApi\\InvoicePdf\\2016-12-14--11-17-52-5.pdf"
                        try {
                            String[] bits = jsonElement.toString().replace("\"", "").split("\\\\");
                            String lastOne = bits[bits.length - 1];
                            String urlpath = Config.getFinanceBaseUrl() + "/" + "InvoicePdf" + "/" + lastOne;

                            Intent in = new Intent();
                            in.setClass(activity, InvoicePdfViewActivity.class);
                            in.putExtra("urlpath", urlpath);
                            activity.startActivity(in);
                            activity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

                        } catch (Exception e) {
                            Log.e("Exception ", " -== " + e.toString());
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", error.getUrl() + "");
                        CommonDialog.showDialog2Button(activity, error.getMessage(), new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                activity.finish();
                            }
                        });
                    }
                }

        );*/

    }

    private void callNext(String roleId, Class c, String s) {

        Intent in = new Intent();
        in.setClass(activity, c);
        in.putExtra("InvoiceData", s);
        in.putExtra("roleId", roleId);
        activity.startActivity(in);
        activity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

    }

    class ViewHolder {
        TextView tvSupplierName, tvInvtAmount, tvFlowStatus;
        ImageView ivEdit, ivPreview;

    }
}
