package com.kippinretail.KippinInvoice;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.KippinInvoice.InvoiceAdapter.AdapterForClassification;
import com.kippinretail.KippinInvoice.InvoiceInterface.InterfaceUpdateClassification;
import com.kippinretail.KippinInvoice.ModalInvoice.ClassificationList;
import com.kippinretail.KippinInvoice.ModalInvoice.EditInvoice;
import com.kippinretail.KippinInvoice.ModalInvoice.EditSentInvoiceModel;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.UserDashBoardActivity;
import com.kippinretail.interfaces.ButtonYesNoListner;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.kippinretail.KippinInvoice.CommonNumberFormatter.getCurrencyFormat;


/**
 * Created by kamaljeet.singh on 12/1/2016.
 */

public class EditSentInvoice extends SuperActivity implements View.OnClickListener, InterfaceUpdateClassification {
    EditText etPaidAmount;
    TextView tvInvoiceDate, tvInvoiceNumber, tvClassification, tvSupplierName, tvCustomerName, tvTaxAmount, tvInvoiceAmount, tvOutstandingBalance, tvFlowStatus, tvStatus;
    Button btn_Send, btn_save, btn_delete;
    LinearLayout bottomLayout;
    AdapterForClassification adapter;
    ListView listForClassification;
    ViewGroup.LayoutParams params1;
    int totalHeight;
    View listItem;
    ViewGroup header1;
    private EditInvoice editInvoice;
    EditSentInvoiceModel editSentInvoice;
    private float oustandingAmount = 0;
    private String ButtonType;
    private List<ClassificationList> classificationList;
    private LayoutInflater inflater;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        if (listAdapter.getCount() <= 1) return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_invoice_activity);
        listForClassification = (ListView) findViewById(R.id.listForClassification);
        inflater = getLayoutInflater();
        header1 = (ViewGroup) inflater.inflate(R.layout.edit_invoice_layout, listForClassification,
                false);

        listForClassification.addHeaderView(header1, null, false);
        ArrayAdapter adapter = new ArrayAdapter(EditSentInvoice.this,
                android.R.layout.simple_list_item_1, 0);
        listForClassification.setAdapter(adapter);
        Initlization();
    }

    private void Initlization() {
        generateRightText1("EDIT SENT INVOICE");
        // TextView
        tvClassification = (TextView) header1.findViewById(R.id.tvClassification);
        tvInvoiceDate = (TextView) header1.findViewById(R.id.tvInvoiceDate);
        tvInvoiceNumber = (TextView) header1.findViewById(R.id.tvInvoiceNumber);
        tvSupplierName = (TextView) header1.findViewById(R.id.tvSupplierName);
        tvCustomerName = (TextView) header1.findViewById(R.id.tvCustomerName);
        tvTaxAmount = (TextView) header1.findViewById(R.id.tvTaxAmount);
        tvInvoiceAmount = (TextView) header1.findViewById(R.id.tvInvoiceAmount);
        tvOutstandingBalance = (TextView) header1.findViewById(R.id.tvOutstandingBalance);
        tvFlowStatus = (TextView) header1.findViewById(R.id.tvFlowStatus);
        tvStatus = (TextView) header1.findViewById(R.id.tvStatus);

        // EditText
        etPaidAmount = (EditText) header1.findViewById(R.id.etPaidAmount);

        // Button
        btn_Send = (Button) findViewById(R.id.btn_Send);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_delete = (Button) findViewById(R.id.btn_delete);

        btn_Send.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String InvoiceData = bundle.getString("InvoiceData");
            editInvoice = gson.fromJson(InvoiceData, EditInvoice.class);


            setData();
        }

        etPaidAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() != 0) {

                    if (Float.parseFloat(tvInvoiceAmount.getText().toString()) > Float.parseFloat(s.toString())) {
                        oustandingAmount = Float.parseFloat(tvInvoiceAmount.getText().toString()) - Float.parseFloat(s.toString());
                    } else {
                        etPaidAmount.removeTextChangedListener(this);
                        etPaidAmount.setText("");
                        tvOutstandingBalance.setText(getCurrencyFormat(Double.parseDouble(""+editSentInvoice.getBalanceDue())));
                        etPaidAmount.addTextChangedListener(this);
                        CommonDialog.With(EditSentInvoice.this).Show("Deposit must be less than total.");
                    }

                    tvOutstandingBalance.setText(getCurrencyFormat(Double.parseDouble("" + oustandingAmount)));
                } else {
                    if (editSentInvoice.getBalanceDue()!=null){
                        tvOutstandingBalance.setText(getCurrencyFormat(Double.parseDouble(""+editSentInvoice.getBalanceDue())));
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private String getRound(float totalAmount) {
        DecimalFormat df = new DecimalFormat("0.00");
        String dx = df.format(totalAmount);
        return dx;
    }

    private void setData() {


        getEditSentInvoice();
    }


    private void getEditSentInvoice() {
        LoadingBox.showLoadingDialog(activity, "Wait ...");
        Log.e("getEditSentInvoice", "getEditSentInvoice");
        Log.e("editInvoice.getId():", "" + editInvoice.getId());
        RestClient.getApiFinanceServiceForPojo().getEditSentInvoice(editInvoice.getId(), new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        getInvoiceItemData();
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                        Gson gson = new Gson();
                         editSentInvoice = gson.fromJson(jsonElement.toString(), new TypeToken<EditSentInvoiceModel>() {
                        }.getType());

                        tvInvoiceDate.setText(editSentInvoice.getInvoiceDateText());
                        if (editSentInvoice.getInvoiceNumber() != null) {
                            tvInvoiceNumber.setText(String.format("%05d", Integer.parseInt(editSentInvoice.getInvoiceNumber())));
                        }
                        tvSupplierName.setText(editSentInvoice.getSupplier());
                        tvCustomerName.setText(editSentInvoice.getCustomer());
//                        if (editInvoice.getTax() != null) {
//                            tvTaxAmount.setText(editSentInvoice.getTax().substring(2));
//                        }
                        tvInvoiceAmount.setText("" + editSentInvoice.getTotal());

                        tvOutstandingBalance.setText("" + editSentInvoice.getBalanceDue());
                        etPaidAmount.setText("" + editSentInvoice.getDepositePayment());
                        tvFlowStatus.setText("" + editSentInvoice.getFlowStatus());
//        if (editInvoice.getInRStatus().equals("Inprogress")) {
//            tvStatus.setText("In Progress");
//        } else {
                        tvStatus.setText(editSentInvoice.getStatus());
                        //    }
                        if (editSentInvoice.getFlowStatus().equals("Pending Approval")||editSentInvoice.getFlowStatus().equals("Deleted") || editSentInvoice.getFlowStatus().equals("Sent") ||
                                editSentInvoice.getFlowStatus().equals("Closed") || editSentInvoice.getFlowStatus().equals("Cancelled")) {
                            bottomLayout.setVisibility(View.GONE);
                            etPaidAmount.setEnabled(false);
                            //tvClassification.setVisibility(View.GONE);
                        }
                        else{
                            etPaidAmount.addTextChangedListener(new DecimalFilter(etPaidAmount, activity));
                        }
                        if (editSentInvoice.getFlowStatus().equals("Draft")) {
                            btn_delete.setText("Cancel");
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


    private void getInvoiceItemData() {
        LoadingBox.showLoadingDialog(activity, "Wait ...");
        RestClient.getApiFinanceServiceForPojo().getInvoiceItems(editInvoice.getId(), new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                        Gson gson = new Gson();
                        classificationList = gson.fromJson(jsonElement.toString(), new TypeToken<List<ClassificationList>>() {
                        }.getType());
                        // if (!getInvoiceOnly()) {
                        for (int i = 0; i < classificationList.size(); i++) {
                            if (classificationList.get(i).getServiceType() == null) {
                                classificationList.get(i).setServiceType("Please Select");
                                classificationList.get(i).setServiceTypeId(0);
                            }
                        }
                        adapter = new AdapterForClassification(EditSentInvoice.this, classificationList, editInvoice.getProFlowStatus(), getUserId(), "",
                                (InterfaceUpdateClassification) EditSentInvoice.this);
                        listForClassification.setAdapter(adapter);
                        // tvClassification.setVisibility(View.GONE);

                        if (classificationList.size() == 0) {
                            tvClassification.setVisibility(View.GONE);
                        }
                        // } else {

                        // }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", error.getUrl() + "");
                    }
                }

        );


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Send:
                ButtonType = "Send";
                if (etPaidAmount.getText().toString().length() != 0) {
                    SsendInvoice(getResources().getString(R.string.send_message));
                }
                break;
            case R.id.btn_save:
                Log.e("TEST", "TEST");
                ButtonType = "Save";
                if (etPaidAmount.getText().toString().length() != 0) {
                    SsendInvoice(getResources().getString(R.string.save_message));
                }
                break;
            case R.id.btn_delete:
                if (btn_delete.getText().toString().equals("Cancel")) {
                    finish();
                } else {
                    showDialogMessage("Are you sure you want to delete this Invoice?");
                }

                break;
        }
    }
    private void showDialogMessage(String s) {

        CommonDialog.DialogDeleteInvoice(EditSentInvoice.this, s, new ButtonYesNoListner() {
            @Override
            public void YesButton() {

                ButtonType = "Delete";
                cancelInvoice();


            }

            @Override
            public void NoButton() {

            }
        });


    }

    private void cancelInvoice() {
        HashMap templatePosts = new HashMap();
        templatePosts.put("Id", "" + editInvoice.getId());
        templatePosts.put("ButtonType", ButtonType);
        templatePosts.put("SectionType", SectionTypeSent);
        templatePosts.put("Type", Invoice);


        LoadingBox.showLoadingDialog(EditSentInvoice.this, "");
        RestClient.getApiFinanceServiceForPojo().UpdateInvoice(getTypedInput(new Gson().toJson(templatePosts)), getCallback(new Callback<JsonElement>() {

            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Tag", "Request data " + new Gson().toJson(jsonElement));

                try {
                    JSONObject jsonObject = new JSONObject(jsonElement.toString());
                    String ResponseMessage = jsonObject.getString("ResponseMessage");
                    String ResponseCode = jsonObject.getString("ResponseCode");
                    String UserId = jsonObject.getString("UserId");

                    if (ResponseCode.equals("1")) {
                        CommonDialog.showDialog2Button(EditSentInvoice.this, getResources().getString(R.string.delete_message), new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                callDashBoard();
                            }
                        });
                    } else {
                        CommonDialog.With(EditSentInvoice.this).Show(ResponseMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(EditSentInvoice.this, error);
            }
        }));


    }

    private void callDashBoard() {

        finish();
        Intent in = new Intent();
        in.setClass(EditSentInvoice.this, UserDashBoardActivity.class);
        in.putExtra("InvoiceUserType", "true");
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

    }

    private void SsendInvoice(final String message) {

        int[] itemIDArray = new int[classificationList.size()];
        int[] serviceIdArray = new int[classificationList.size()];
        String[] serviceTypeArray = new String[classificationList.size()];
        for (int i = 0; i < classificationList.size(); i++) {
            itemIDArray[i] = classificationList.get(i).getItemId();
            serviceIdArray[i] = classificationList.get(i).getServiceTypeId();
            serviceTypeArray[i] = classificationList.get(i).getServiceType();
        }

        HashMap templatePosts = new HashMap();
        templatePosts.put("Id", "" + editInvoice.getId());
        templatePosts.put("DepositePayment", etPaidAmount.getText().toString());
        templatePosts.put("BalanceDue", tvOutstandingBalance.getText().toString());
        templatePosts.put("ItemId", itemIDArray);
        templatePosts.put("ServiceType", serviceTypeArray);
        templatePosts.put("ServiceTypeId", serviceIdArray);
        templatePosts.put("ButtonType", ButtonType);
        templatePosts.put("SectionType", SectionTypeSent);
        templatePosts.put("Type", Invoice);


        LoadingBox.showLoadingDialog(EditSentInvoice.this, "");
        RestClient.getApiFinanceServiceForPojo().UpdateInvoice(getTypedInput(new Gson().toJson(templatePosts)), getCallback(new Callback<JsonElement>() {

            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Tag", "Request data " + new Gson().toJson(jsonElement));

                try {
                    JSONObject jsonObject = new JSONObject(jsonElement.toString());
                    String ResponseMessage = jsonObject.getString("ResponseMessage");
                    String ResponseCode = jsonObject.getString("ResponseCode");
                    String UserId = jsonObject.getString("UserId");

                    if (ResponseCode.equals("1")) {
                        CommonDialog.showDialog2Button(EditSentInvoice.this, message, new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                finish();
                            }
                        });
                    } else {
                        CommonDialog.With(EditSentInvoice.this).Show(ResponseMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(EditSentInvoice.this, error);
            }
        }));


    }

    @Override
    public void updateClassication(int position, int serviceId, String serviceType) {
        classificationList.get(position).setServiceType(serviceType);
        classificationList.get(position).setServiceTypeId(serviceId);
        adapter.notifyDataSetChanged();
    }
}
