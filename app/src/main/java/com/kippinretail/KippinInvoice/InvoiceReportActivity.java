package com.kippinretail.KippinInvoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.KippinInvoice.InvoiceAdapter.Report.CustomAdapterForFirstlistReport;
import com.kippinretail.KippinInvoice.InvoiceAdapter.Report.CustomAdapterForSecondListReport;
import com.kippinretail.KippinInvoice.InvoiceAdapter.Report.CustomAdapterForSecondListReportSupplier;
import com.kippinretail.KippinInvoice.InvoiceInterface.InterfacePayWithStripe;
import com.kippinretail.KippinInvoice.ModalInvoice.ReportList;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippin.bankstatement.ScrollSync;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by kamaljeet.singh on 11/7/2016.
 */
public class InvoiceReportActivity extends SuperActivity implements InterfacePayWithStripe {
    public static String[] Invoicetype_IDs = new String[]{"0", "1", "2"};
    public static String[] Flowstatus_IDs = new String[]{"All", "Draft", "Sent", "Pending Payment", "Partial payment", "Paid", "Approved", "Declined", "Cancelled", "Closed"};
    ListView dates, desc;
    Spinner spStatus, spInvoiceType;
    TextView tvStatus, tvInvoiceType, tvReportCustomerSupplier;
    Button btn_PayViaStripe;
    String status_selected_id = Invoicetype_IDs[0];
    private String flow_StatusID = Flowstatus_IDs[0];
    private List<ReportList> reportList;
    private ArrayList<ReportList> reportList_;
    AdapterView.OnItemSelectedListener Spinner_InvoiceType = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            status_selected_id = Invoicetype_IDs[position];
            tvInvoiceType.setText("" + spInvoiceType.getSelectedItem());
            if (!status_selected_id.equals("0")) {
                loadInvoiceTypeList(status_selected_id);
            } else {
                loadAdapter(reportList, tvReportCustomerSupplier.getText().toString());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    AdapterView.OnItemSelectedListener Spinner_Status = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            flow_StatusID = Flowstatus_IDs[position];
            if (!flow_StatusID.equalsIgnoreCase("All")) {
                loadFlowTypeList(flow_StatusID);
            } else {
                loadAdapter(reportList, tvReportCustomerSupplier.getText().toString());
            }
            //tvInvoiceType.setText("" + spInvoiceType.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private String roleId = "";
    private int reportId = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_report_activity);
        initlization();
    }

    private void initlization() {
        tvReportCustomerSupplier = (TextView) findViewById(R.id.tvReportCustomerSupplier);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            roleId = extras.getString("roleId");
            reportId = extras.getInt("reportId");
        }

        if (roleId.equals("1")) {
            tvReportCustomerSupplier.setText("Supplier");
            BackWithHOME(getString(R.string.received_report_list), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CommonUtility.HomeClick(InvoiceReportActivity.this);

                }
            });

        } else {
            tvReportCustomerSupplier.setText("Customer");
            BackWithHOME(getString(R.string.sent_report_list), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CommonUtility.HomeClick(InvoiceReportActivity.this);

                }
            });
        }

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        spStatus = (Spinner) findViewById(R.id.spStatus);

        tvInvoiceType = (TextView) findViewById(R.id.tvInvoiceType);
        spInvoiceType = (Spinner) findViewById(R.id.spInvoiceType);
        //btn_PayViaStripe = (Button) findViewById(R.id.btn_PayViaStripe);

        dates = (ListView) findViewById(R.id.listView1);
        desc = (ListView) findViewById(R.id.hListView1);
        ScrollSync scrollSync = new ScrollSync();
        scrollSync.addList(dates);
        scrollSync.addList(desc);

        tvInvoiceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spInvoiceType.performClick();
            }
        });

        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spStatus.performClick();
            }
        });

      /*  btn_PayViaStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }

    private void loadInvoiceTypeList(String status_id) {
        reportList_ = new ArrayList<>();
        for (int i = 0; i < reportList.size(); i++) {
            // ALL invoice
            if (reportList.get(i).getType() == Integer.parseInt(status_id)) {
                reportList_.add(reportList.get(i));
            }

        }
        dates.setAdapter(new CustomAdapterForFirstlistReport(InvoiceReportActivity.this, reportList_));
        dates.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        desc.setAdapter(new CustomAdapterForSecondListReportSupplier(InvoiceReportActivity.this, reportList_));
    }

    private void loadFlowTypeList(String flow_statusID) {
        reportList_ = new ArrayList<>();

        for (int i = 0; i < reportList.size(); i++) {

            if (reportList.get(i).getProStatus().equals("Pending Approval")) {
            } else if (reportList.get(i).getProStatus().equals("Accepted")) {
                if (Float.parseFloat(reportList.get(i).getBalanceDue()) == 0.0) {
                    reportList.get(i).setProFlowStatus("Paid");
                } else if (Float.parseFloat(reportList.get(i).getDepositPayment().toString()) > 0.0) {
                    reportList.get(i).setProStatus("Partial Payment");
                } else {
                    reportList.get(i).setProStatus("Pending Payment");
                }
            }
            Log.e("flow_statusID:",""+flow_statusID);
            Log.e("ProStatus:",""+reportList.get(i).getFlowStatus());

            // ALL invoice
            if (reportList.get(i).getFlowStatus().equalsIgnoreCase(flow_statusID)) {
                reportList_.add(reportList.get(i));
            }

        }
        dates.setAdapter(new CustomAdapterForFirstlistReport(InvoiceReportActivity.this, reportList_));
        dates.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        desc.setAdapter(new CustomAdapterForSecondListReport(InvoiceReportActivity.this, reportList_));

    }

    @Override
    protected void onResume() {
        super.onResume();
        getReportList();
    }

    private void getReportList() {

        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().getReportList(Integer.parseInt(getUserId()), reportId, new Callback<JsonElement>() {
            @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Rest------Client", jsonElement.toString() + " \n " + response.getUrl());
                        Gson gson = new Gson();
                        reportList = gson.fromJson(jsonElement.toString(), new TypeToken<List<ReportList>>() {
                        }.getType());
                        if (reportList.get(0).getResponseCode().equals("1")) {
                            spInvoiceType.setOnItemSelectedListener(Spinner_InvoiceType);
                            spStatus.setOnItemSelectedListener(Spinner_Status);
                            loadAdapter(reportList, tvReportCustomerSupplier.getText().toString());
                        } else {
                            CommonDialog.With(InvoiceReportActivity.this).Show(reportList.get(0).getResponseMessage());
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

    private void loadAdapter(List<ReportList> reportList, String name) {
        if (name.equals("Supplier")) {
            dates.setAdapter(new CustomAdapterForFirstlistReport(InvoiceReportActivity.this, reportList));
            dates.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
            desc.setAdapter(new CustomAdapterForSecondListReportSupplier(InvoiceReportActivity.this, reportList));
        } else {
            dates.setAdapter(new CustomAdapterForFirstlistReport(InvoiceReportActivity.this, reportList));
            dates.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
            desc.setAdapter(new CustomAdapterForSecondListReport(InvoiceReportActivity.this, reportList));
        }
    }

    @Override
    public void PaymentToStripe() {

    }
}

