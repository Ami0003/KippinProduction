package com.kippinretail.KippinInvoice;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.KippinInvoice.InvoiceAdapter.AdapterForReceivedClassification;
import com.kippinretail.KippinInvoice.InvoiceInterface.InterfaceUpdateClassification;
import com.kippinretail.KippinInvoice.ModalInvoice.ClassificationList;
import com.kippinretail.KippinInvoice.ModalInvoice.EditInvoice;
import com.kippinretail.KippinInvoice.ModalInvoice.EditRecievedInvoice;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.UserDashBoardActivity;
import com.kippinretail.interfaces.ButtonYesNoListner;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 12/8/2016.
 */

public class EditReceivedInvoice extends SuperActivity implements View.OnClickListener, InterfaceUpdateClassification {
    EditText etPaidAmount;
    TextView tvInvoiceDate, tvInvoiceNumber, tvClassification, tvSupplierName, tvCustomerName, tvTaxAmount, tvInvoiceAmount, tvOutstandingBalance, tvFlowStatus, tvStatus;
    Button btn_accept, btn_Cancel, btn_delete, btn_convert;
    LinearLayout bottomLayout;
    AdapterForReceivedClassification adapter;
    ListView listForClassification;
    ViewGroup header1;
    private EditInvoice editInvoice;
    private float oustandingAmount = 0;
    private String ButtonType;
    private List<ClassificationList> classificationList;
    private String messageR;
    private LayoutInflater inflater;

    private Integer igetTotal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_received_activity);
        listForClassification = (ListView) findViewById(R.id.listForClassification);
        inflater = getLayoutInflater();
        header1 = (ViewGroup) inflater.inflate(R.layout.edit_invoice_layout, listForClassification,
                false);

        listForClassification.addHeaderView(header1, null, false);
        ArrayAdapter adapter = new ArrayAdapter(EditReceivedInvoice.this,
                android.R.layout.simple_list_item_1, 0);
        listForClassification.setAdapter(adapter);
        Initlization();
    }

    private void Initlization() {
        generateRightText1("EDIT RECEIVED INVOICE");
        // TextView
        tvInvoiceDate = (TextView) header1.findViewById(R.id.tvInvoiceDate);
        tvClassification = (TextView) header1.findViewById(R.id.tvClassification);
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
        btn_accept = (Button) findViewById(R.id.btn_accept);
        btn_Cancel = (Button) findViewById(R.id.btn_Cancel);
        btn_delete = (Button) findViewById(R.id.btn_delete);

        btn_accept.setOnClickListener(this);
        btn_Cancel.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String InvoiceData = bundle.getString("InvoiceData");
            editInvoice = gson.fromJson(InvoiceData, EditInvoice.class);
            setData();
        }
        //etPaidAmount.addTextChangedListener(new DecimalFilter(etPaidAmount, activity));
        etPaidAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() != 0) {
                    if (Float.parseFloat(tvInvoiceAmount.getText().toString()) > Float.parseFloat(s.toString())) {
                        oustandingAmount = Float.parseFloat(tvInvoiceAmount.getText().toString()) - Float.parseFloat(s.toString());
                    }

                    /*if (Float.parseFloat(editRecievedInvoice.getTotal())*/
                    /*if (((float) igetTotal) > Float.parseFloat(s.toString())) {
                        oustandingAmount = Float.parseFloat(tvInvoiceAmount.getText().toString()) - Float.parseFloat(s.toString());
                        oustandingAmount = ((float) igetTotal)*//*Float.parseFloat(tvInvoiceAmount.getText().toString())*//* - Float.parseFloat(s.toString());
                    }*/ else {
                        etPaidAmount.removeTextChangedListener(this);
                        etPaidAmount.setText("");
                        tvOutstandingBalance.setText(editInvoice.getBalanceDue());
                        etPaidAmount.addTextChangedListener(this);
                        CommonDialog.With(EditReceivedInvoice.this).Show("Deposit must be less than total.");
                    }
                    Log.e("oustandingAmount:", "" + oustandingAmount);
                    tvOutstandingBalance.setText("" + oustandingAmount);
                } else {
                    tvOutstandingBalance.setText(editInvoice.getBalanceDue());
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
        getInvoiceItemData();
        getInvoiceData();
    }

    private void getInvoiceData() {

        LoadingBox.showLoadingDialog(activity, "Wait ...");
        RestClient.getApiFinanceServiceForPojo().getInvoiceData(editInvoice.getId(), new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                        Gson gson = new Gson();
                        EditRecievedInvoice editRecievedInvoice = gson.fromJson(jsonElement.toString(), new TypeToken<EditRecievedInvoice>() {
                        }.getType());
                        tvInvoiceDate.setText(editRecievedInvoice.getInvoiceDateText());
                        if (editInvoice.getInvoiceNumber() != null) {
                            tvInvoiceNumber.setText(String.format("%05d", Integer.parseInt(editRecievedInvoice.getInvoiceNumber())));
                        }
                        tvSupplierName.setText(editRecievedInvoice.getSupplier());
                        tvCustomerName.setText(editRecievedInvoice.getCustomer());
                        //  if (editInvoice.getTax() != null) {
                        tvTaxAmount.setText("dad");
                        //   }
                        igetTotal = editRecievedInvoice.getTotal().intValue();
                        Log.e("totoal:", "" + editRecievedInvoice.getTotal());
                        Double getBalanceDue = editRecievedInvoice.getBalanceDue();
                        Log.e("getBalanceDue:", "" + getBalanceDue);
                        Log.e("BalanceDue-----:", "" + editRecievedInvoice.getBalanceDue());
                        tvInvoiceAmount.setText("" + editRecievedInvoice.getTotal());
                        // tvInvoiceAmount.setText(getCurrencyFormat(Double.parseDouble("" + igetTotal)));
                        tvOutstandingBalance.setText("" + getBalanceDue);
                        if (editRecievedInvoice.getDepositePayment() != null) {
                            etPaidAmount.setText("" + editRecievedInvoice.getDepositePayment());
                        } else {
                            etPaidAmount.setText("0.00");
                        }

                        tvFlowStatus.setText("" + editRecievedInvoice.getFlowStatus());
                        etPaidAmount.setEnabled(false);

                        tvStatus.setText(editRecievedInvoice.getStatus());

                        if (editRecievedInvoice.getFlowStatus().equals("Deleted") || !editRecievedInvoice.getFlowStatus().equals("Pending Approval")) {
                            bottomLayout.setVisibility(View.GONE);
                            etPaidAmount.setEnabled(false);
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
                        if (!getInvoiceOnly()) {
                            for (int i = 0; i < classificationList.size(); i++) {
                                // if (classificationList.get(i).getServiceType().equals("")) {
                                classificationList.get(i).setServiceType("Please Select");
                                classificationList.get(i).setServiceTypeId(0);
                                // }
                            }
                            adapter = new AdapterForReceivedClassification(EditReceivedInvoice.this, classificationList, editInvoice.getProFlowStatus(), getUserId(),
                                    editInvoice.getProFlowStatus(), (InterfaceUpdateClassification) EditReceivedInvoice.this);
                            listForClassification.setAdapter(adapter);
                            if (classificationList.size() == 0) {
                                tvClassification.setVisibility(View.GONE);
                            }
                        } else {
                            //if(classificationList.size()==0){
                            tvClassification.setVisibility(View.GONE);
                            //}
                            //   header1.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accept:
                for (int i = 0; i < classificationList.size(); i++) {
                    if (classificationList.get(i).getServiceType().equals("Please Select")) {
                        CommonDialog.With(EditReceivedInvoice.this).Show("Please add Classification for each item.");
                        return;
                    }
                }
                ButtonType = "Accept";
                if (etPaidAmount.getText().toString().length() != 0) {
                    SsendInvoice(getResources().getString(R.string.accept_message));
                }
                break;
            case R.id.btn_Cancel:
                /*ButtonType = "Cancel";
                cancelInvoice(getResources().getString(R.string.cancel_message));*/
                this.ButtonType = "Reject";
                try {
                    final Dialog dialog = new Dialog(this.activity);
                    dialog.setCancelable(false);
                    dialog.requestWindowFeature(1);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog.setContentView(R.layout.dialog_reject);
                    final EditText editTextEmail = (EditText) dialog.findViewById(R.id.editText_emailid);
                    editTextEmail.setMaxLines(1);
                    editTextEmail.setSingleLine(true);
                    editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                dialog.getWindow().setSoftInputMode(3);
                            }
                        }
                    });
                    TextView textViewSubmit = (TextView) dialog.findViewById(R.id.submit_btn);
                    ((TextView) dialog.findViewById(R.id.cancel_btn)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                            editTextEmail.setText("");
                        }
                    });
                    textViewSubmit.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (editTextEmail.getText().toString().equals("")) {
                                Toast.makeText(EditReceivedInvoice.this, "Please provide reason", Toast.LENGTH_SHORT).show();
                            } else {
                                cancelInvoice(editTextEmail.getText().toString());
                            }
                        }
                    });
                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                } catch (Error e2) {
                    e2.printStackTrace();
                    return;
                }
                break;
            case R.id.btn_delete:
                showDialogMessage("Are you sure you want to delete this Invoice?");
                break;

        }
    }
    private void showDialogMessage(String s) {

        CommonDialog.DialogDeleteInvoice(EditReceivedInvoice.this, s, new ButtonYesNoListner() {
            @Override
            public void YesButton() {
                ButtonType = "Delete";
                cancelInvoice(getResources().getString(R.string.delete_message));



            }

            @Override
            public void NoButton() {

            }
        });


    }

    private void cancelInvoice(String message) {
        HashMap templatePosts = new HashMap();
        templatePosts.put("Id", "" + editInvoice.getId());
        templatePosts.put("ButtonType", ButtonType);
        templatePosts.put("SectionType", SectionTypeReceived);
        templatePosts.put("Type", Invoice);
        templatePosts.put("UserId", getUserId());
        String[] cutomerService = new String[1];
        String[] cutomerServiceID = new String[]{""};
        cutomerServiceID[0] = "";
        templatePosts.put("Customer_Service", cutomerService);
        templatePosts.put("Customer_ServiceTypeId", cutomerServiceID);
        if (ButtonType.equals("Cancel")) {
            messageR = "Invoice cancelled successfully";
        } else if (ButtonType.equals("Reject")) {
            messageR = "Invoice Rejected successfully";
            templatePosts.put("RejectReason", message);
        } else {
            messageR = "Invoice deleted successfully";
        }

        LoadingBox.showLoadingDialog(EditReceivedInvoice.this, "");
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
                        CommonDialog.showDialog2Button(EditReceivedInvoice.this, messageR, new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                finish();
                                overridePendingTransition(com.kippin.kippin.R.anim.animation_from_left, com.kippin.kippin.R.anim.animation_to_right);
                            }
                        });
                    } else {
                        CommonDialog.With(EditReceivedInvoice.this).Show(ResponseMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(EditReceivedInvoice.this, error);
            }
        }));


    }

    private void callDashBoard() {

        finish();
        Intent in = new Intent();
        in.setClass(EditReceivedInvoice.this, UserDashBoardActivity.class);
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
        templatePosts.put("Customer_Service", serviceTypeArray);
        templatePosts.put("Customer_ServiceTypeId", serviceIdArray);
        templatePosts.put("ButtonType", ButtonType);
        templatePosts.put("SectionType", SectionTypeReceived);
        templatePosts.put("Type", Invoice);


        LoadingBox.showLoadingDialog(EditReceivedInvoice.this, "");
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
                        CommonDialog.showDialog2Button(EditReceivedInvoice.this, message, new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                finish();
                            }
                        });
                    } else {
                        CommonDialog.With(EditReceivedInvoice.this).Show(ResponseMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(EditReceivedInvoice.this, error);
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

