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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.KippinInvoice.InvoiceAdapter.AdapterForClassification;
import com.kippinretail.KippinInvoice.InvoiceInterface.InterfaceUpdateClassification;
import com.kippinretail.KippinInvoice.ModalInvoice.ClassificationList;
import com.kippinretail.KippinInvoice.ModalInvoice.EditInvoice;
import com.kippinretail.KippinInvoice.ModalInvoice.EditPerformaDetail;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.UserDashBoardActivity;
import com.kippinretail.interfaces.ButtonYesNoListner;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

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

public class EditSentPerforma extends SuperActivity implements View.OnClickListener, InterfaceUpdateClassification {
    EditText etPaidAmount;
    TextView tvInvoiceDate, tvInvoiceNumber, tvClassification, tvSupplierName, tvCustomerName, tvTaxAmount, tvInvoiceAmount, tvOutstandingBalance, tvFlowStatus, tvStatus;
    Button btn_Send, btn_save, btn_delete, btn_convert, btn_convertOnly;
    LinearLayout bottomLayout;
    AdapterForClassification adapter;
    ListView listForClassification;
    RelativeLayout rlOnlyConvert;
    private EditInvoice editInvoice;
    EditPerformaDetail edit_Invoice;
    private float oustandingAmount = 0;
    private String ButtonType;
    private List<ClassificationList> classificationList;
    private LayoutInflater inflater;
    private ViewGroup header1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_proforma_activity);

        listForClassification = (ListView) findViewById(R.id.listForClassification);
        inflater = getLayoutInflater();
        header1 = (ViewGroup) inflater.inflate(R.layout.edit_invoice_layout, listForClassification,
                false);

        listForClassification.addHeaderView(header1, null, false);
        ArrayAdapter adapter = new ArrayAdapter(EditSentPerforma.this,
                android.R.layout.simple_list_item_1, 0);
        listForClassification.setAdapter(adapter);

        Initlization();

    }

    private void Initlization() {
        //QUOTE
        generateRightText1("EDIT SENT QUOTE");
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
        btn_convert = (Button) findViewById(R.id.btn_convert);
        btn_convertOnly = (Button) findViewById(R.id.btn_convertOnly);

        btn_Send.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_convertOnly.setOnClickListener(this);

        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        rlOnlyConvert = (RelativeLayout) findViewById(R.id.rlOnlyConvert);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String InvoiceData = bundle.getString("InvoiceData");
            editInvoice = gson.fromJson(InvoiceData, EditInvoice.class);
            setData();
        }
        etPaidAmount.addTextChangedListener(new DecimalFilter(etPaidAmount, activity));
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
                        tvOutstandingBalance.setText("" + editInvoice.getBalanceDue());
                        etPaidAmount.addTextChangedListener(this);
                        CommonDialog.With(EditSentPerforma.this).Show("Deposit must be less than total.");
                    }
                    tvOutstandingBalance.setText("" + getRound(oustandingAmount));
                } else {
                    Log.e("getBalanceDue:", "" + editInvoice.getBalanceDue());
                    tvOutstandingBalance.setText("" + (edit_Invoice.getTotal() - edit_Invoice.getDepositePayment()));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void getInvoiceNumber() {
        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().getEditInvoiceData(editInvoice.getId(), new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("getInvoiceNumber RestClient", jsonElement.toString() + " \n " + response.getUrl());
                        Gson gson = new Gson();
                        edit_Invoice = gson.fromJson(jsonElement.toString(), new TypeToken<EditPerformaDetail>() {
                        }.getType());
//                        tvInvoiceDate.setText(editInvoice.getInvoiceDate());
                        tvInvoiceDate.setText(edit_Invoice.getInvoiceDateText());
                        if (editInvoice.getInvoiceNumber() != null) {
                            tvInvoiceNumber.setText(String.format("%05d", Integer.parseInt(editInvoice.getInvoiceNumber())));
                        }
                        tvSupplierName.setText(edit_Invoice.getSupplier());
                        tvCustomerName.setText(edit_Invoice.getCustomer());
//                        if (editInvoice.getTax() != null) {
//                            tvTaxAmount.setText(editInvoice.getTax().substring(2));
//                        }
                        String s = editInvoice.getTotal().toString().replace(",", "");
                        Log.e("s:", "" + s);
                        tvInvoiceAmount.setText(s);
                        tvOutstandingBalance.setText("" + (edit_Invoice.getTotal() - edit_Invoice.getDepositePayment()));
                        etPaidAmount.setText(edit_Invoice.getDepositePaymentText().replace(",",""));
                        tvFlowStatus.setText(edit_Invoice.getFlowStatus());
                        try {
                            if (edit_Invoice.getStatus().equals("Inprogress")) {
                                tvStatus.setText("In Progress");
                            } else {
                                tvStatus.setText(edit_Invoice.getStatus());
                            }
                        } catch (Exception e) {

                        }
                        if (edit_Invoice.getFlowStatus().equals("Deleted") || edit_Invoice.getFlowStatus().equals("Sent") ||
                                edit_Invoice.getFlowStatus().equals("Closed") || edit_Invoice.getFlowStatus().equals("Cancelled")) {
                            bottomLayout.setVisibility(View.GONE);
                            rlOnlyConvert.setVisibility(View.GONE);
                            etPaidAmount.setEnabled(false);
                        } else if (edit_Invoice.getFlowStatus().equals("Draft") || edit_Invoice.getFlowStatus().equals("Sent")) {
                            bottomLayout.setVisibility(View.VISIBLE);
                            rlOnlyConvert.setVisibility(View.GONE);
                            etPaidAmount.setEnabled(true);
                        } else {
                            rlOnlyConvert.setVisibility(View.VISIBLE);
                            bottomLayout.setVisibility(View.GONE);
                            etPaidAmount.setEnabled(false);
                        }


                        getInvoiceItemData();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", error.getMessage() + error.getUrl() + "");
                    }
                }

        );
    }


    private String getRound(float totalAmount) {
        DecimalFormat df = new DecimalFormat("0.00");
        String dx = df.format(totalAmount);
        return dx;
    }

    private void setData() {

        tvInvoiceDate.setText(editInvoice.getInvoiceDate());
        if (editInvoice.getInvoiceNumber() != null) {
            tvInvoiceNumber.setText(String.format("%05d", Integer.parseInt(editInvoice.getInvoiceNumber())));
        }
        tvSupplierName.setText(editInvoice.getUsername());
        tvCustomerName.setText(editInvoice.getFirstName());
        if (editInvoice.getTax() != null) {
            tvTaxAmount.setText(editInvoice.getTax().substring(2));
        }
        String s = editInvoice.getTotal().replace(",", "");
        Log.e("s:::", "" + s);
        tvInvoiceAmount.setText(s);
        tvOutstandingBalance.setText(editInvoice.getBalanceDue());
        etPaidAmount.setText(editInvoice.getDepositePayment());
        tvFlowStatus.setText(editInvoice.getInRFlowStatus());
        try {
            if (editInvoice.getInRStatus().equals("Inprogress")) {
                tvStatus.setText("In Progress");
            } else {
                tvStatus.setText(editInvoice.getInRStatus());
            }
        } catch (Exception e) {

        }
        if (editInvoice.getInRFlowStatus().equals("Deleted") || editInvoice.getInRFlowStatus().equals("Sent") ||
                editInvoice.getInRFlowStatus().equals("Closed") || editInvoice.getInRFlowStatus().equals("Cancelled")) {
            bottomLayout.setVisibility(View.GONE);
            rlOnlyConvert.setVisibility(View.GONE);
            etPaidAmount.setEnabled(false);
        } else if (editInvoice.getInRFlowStatus().equals("Draft") || editInvoice.getInRFlowStatus().equals("Sent")) {
            bottomLayout.setVisibility(View.VISIBLE);
            rlOnlyConvert.setVisibility(View.GONE);
            etPaidAmount.setEnabled(true);
        } else {
            rlOnlyConvert.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.GONE);
            etPaidAmount.setEnabled(false);
        }


        getInvoiceNumber();
    }

    private void getInvoiceItemData() {


        LoadingBox.showLoadingDialog(activity, "Wait ...");
        RestClient.getApiFinanceServiceForPojo().getInvoiceItems(editInvoice.getId(), new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("EditSentPerformaRestClient", jsonElement.toString() + " \n " + response.getUrl());
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

                        adapter = new AdapterForClassification(EditSentPerforma.this, classificationList, editInvoice.getProFlowStatus(),
                                getUserId(), "", (InterfaceUpdateClassification) EditSentPerforma.this);
                        listForClassification.setAdapter(adapter);
                        if (classificationList.size() == 0) {
                            tvClassification.setVisibility(View.GONE);
                        }
                        //}
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
                    SsendInvoice(getResources().getString(R.string.proforma_send_message));
                }
                break;
            case R.id.btn_save:
                ButtonType = "Save";
                if (etPaidAmount.getText().toString().length() != 0) {
                    SsendInvoice(getResources().getString(R.string.proforma_save_message));
                }
                break;
            case R.id.btn_delete:
                showDialogMessage("Are you sure you want to delete this Quote?");

                break;
            case R.id.btn_convert:
                ButtonType = "Convert";
                // if (etPaidAmount.getText().toString().length() != 0) {
                SsendInvoice(getResources().getString(R.string.proforma_convert_message));
                // }
                break;
            case R.id.btn_convertOnly:
                ButtonType = "Convert";
                // if (etPaidAmount.getText().toString().length() != 0) {
                SsendInvoice(getResources().getString(R.string.proforma_convert_message));
                //  }
                break;
        }
    }

    private void showDialogMessage(String s) {

        CommonDialog.DialogDeleteInvoice(EditSentPerforma.this, s, new ButtonYesNoListner() {
            @Override
            public void YesButton() {

                ButtonType = "Delete";
                cancelInvoice(getResources().getString(R.string.proforma_delete_message));


            }

            @Override
            public void NoButton() {

            }
        });


    }

    private void cancelInvoice(final String message) {
        HashMap templatePosts = new HashMap();
        templatePosts.put("Id", "" + editInvoice.getId());
        templatePosts.put("ButtonType", ButtonType);
        templatePosts.put("SectionType", SectionTypeSent);
        templatePosts.put("Type", Performa);


        LoadingBox.showLoadingDialog(EditSentPerforma.this, "");
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
                        CommonDialog.showDialog2Button(EditSentPerforma.this, message, new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                callDashBoard();
                            }
                        });
                    } else {
                        CommonDialog.With(EditSentPerforma.this).Show(ResponseMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(EditSentPerforma.this, error);
            }
        }));


    }

    private void callDashBoard() {

        finish();
        Intent in = new Intent();
        in.setClass(EditSentPerforma.this, UserDashBoardActivity.class);
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
        templatePosts.put("Type", Performa);


        LoadingBox.showLoadingDialog(EditSentPerforma.this, "");
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
                        CommonDialog.showDialog2Button(EditSentPerforma.this, message, new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                finish();
                            }
                        });
                    } else {
                        CommonDialog.With(EditSentPerforma.this).Show(ResponseMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(EditSentPerforma.this, error);
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

