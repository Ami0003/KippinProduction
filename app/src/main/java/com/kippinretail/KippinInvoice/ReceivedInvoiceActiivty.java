package com.kippinretail.KippinInvoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.KippinInvoice.InvoiceAdapter.CustomAdapterForSecondList;
import com.kippinretail.KippinInvoice.InvoiceAdapter.Report.CustomerAdapterSentInvoiceFirst;
import com.kippinretail.KippinInvoice.ModalInvoice.ReceivedInvoiceList;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippin.bankstatement.ScrollSync;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 11/30/2016.
 */

public class ReceivedInvoiceActiivty extends SuperActivity {
    ListView dates, desc;
    private String CustomerId;
    private List<ReceivedInvoiceList> receivedLists;
    private String roleId;
    TextView tv_date, tv_number,tv_amount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.received_invoice_activity);
        Initlization();
    }

    private void Initlization() {
        //generateRightText1(getString(R.string.received_invoice));

        dates = (ListView) findViewById(com.kippin.kippin.R.id.listView1);
        desc = (ListView) findViewById(com.kippin.kippin.R.id.hListView1);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_amount= (TextView) findViewById(R.id.tv_amount);
        ScrollSync scrollSync = new ScrollSync();
        scrollSync.addList(dates);
        scrollSync.addList(desc);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            roleId = extras.getString("roleId");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (roleId.equals("1")) {
            tv_date.setText("Invoice Date");
            tv_number.setText("Invoice Number");
            tv_amount.setText("Invoice Amount");
            BackWithHOME(getString(R.string.received_invoice), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtility.HomeClick(ReceivedInvoiceActiivty.this);
                }
            });
            getReceivedList();

        } else {
            tv_date.setText("Quote Date");
            tv_number.setText("Quote Number");
            tv_amount.setText("Quote Amount");
            BackWithHOME("RECEIVED QUOTE", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtility.HomeClick(ReceivedInvoiceActiivty.this);
                }
            });
            getReceivedPerforma();
        }
    }

    private void getReceivedPerforma() {

        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().getPerformaList(getUserId(), new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                        Gson gson = new Gson();
                        receivedLists = gson.fromJson(jsonElement.toString(), new TypeToken<List<ReceivedInvoiceList>>() {
                        }.getType());
                        if (receivedLists.get(0).getResponseCode() == 1) {
                            dates.setAdapter(new CustomerAdapterSentInvoiceFirst(ReceivedInvoiceActiivty.this, receivedLists));
                            dates.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
                            desc.setAdapter(new CustomAdapterForSecondList(ReceivedInvoiceActiivty.this, receivedLists, roleId));
                        } else {
                            CommonDialog.With(ReceivedInvoiceActiivty.this).Show(receivedLists.get(0).getResponseMessage());
                            receivedLists.clear();
                            dates.setAdapter(new CustomerAdapterSentInvoiceFirst(ReceivedInvoiceActiivty.this, receivedLists));
                            dates.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
                            desc.setAdapter(new CustomAdapterForSecondList(ReceivedInvoiceActiivty.this, receivedLists, roleId));

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", error.getUrl() + "");
                    }
                }

        );

    }

    private void getReceivedList() {

        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().getReceivedList(getUserId(), new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                        Gson gson = new Gson();
                        receivedLists = gson.fromJson(jsonElement.toString(), new TypeToken<List<ReceivedInvoiceList>>() {
                        }.getType());
                        if (receivedLists.get(0).getResponseCode() == 1) {
                            dates.setAdapter(new CustomerAdapterSentInvoiceFirst(ReceivedInvoiceActiivty.this, receivedLists));
                            dates.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
                            desc.setAdapter(new CustomAdapterForSecondList(ReceivedInvoiceActiivty.this, receivedLists, roleId));
                        } else {
                            CommonDialog.With(ReceivedInvoiceActiivty.this).ShowFinish(receivedLists.get(0).getResponseMessage());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", error.getUrl() + "");
                    }
                }

        );

    }


}
