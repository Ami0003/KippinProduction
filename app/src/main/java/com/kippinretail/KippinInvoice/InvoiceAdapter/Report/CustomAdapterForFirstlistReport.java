package com.kippinretail.KippinInvoice.InvoiceAdapter.Report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.KippinInvoice.InvoicePdfViewActivity;
import com.kippinretail.KippinInvoice.ModalInvoice.ReportList;
import com.kippinretail.R;

import java.util.List;

import static com.kippinretail.config.Config.Invoice_PDF_URL;

/**
 * Created by kamaljeet.singh on 12/14/2016.
 */

public class CustomAdapterForFirstlistReport extends BaseAdapter {
    private final Activity activity;
    private final List<ReportList> reportLists;
    ViewHolder viewHolder;
    private String invoiceNumber;

    public CustomAdapterForFirstlistReport(Activity activity, List<ReportList> reportLists) {
        this.activity = activity;
        this.reportLists = reportLists;
    }

    @Override
    public int getCount() {
        return reportLists.size();
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
            convertView = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_bank_statement_date, parent, false);
            viewHolder.ivDate = (ImageView) convertView.findViewById(R.id.ivDate);
            viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.tvDescription);

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPdfPath("" + reportLists.get(position).getId());
            }
        });


        viewHolder.tvDesc.setText(reportLists.get(position).getInvoiceNumber());
        return convertView;
    }

    private void getPdfPath(String invoiceID) {

        /**
         *  We have to load PDF directly in webview without hit any web api.
         */
        Intent in = new Intent();
        in.setClass(activity, InvoicePdfViewActivity.class);
        in.putExtra("urlpath", (Invoice_PDF_URL + invoiceID));
        activity.startActivity(in);
        activity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

        //((SuperActivity)activity).getUserId()
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

    class ViewHolder {
        ImageView ivDate;
        TextView tvDesc;
    }
}
