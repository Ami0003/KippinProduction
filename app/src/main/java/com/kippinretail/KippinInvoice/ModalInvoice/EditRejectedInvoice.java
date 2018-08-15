package com.kippinretail.KippinInvoice.ModalInvoice;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.KippinInvoice.CommonNumberFormatter;
import com.kippinretail.KippinInvoice.DecimalFilter;
import com.kippinretail.KippinInvoice.InvoiceInterface.InterfaceUpdate;
import com.kippinretail.Modal.Invoice.EditCustomer;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.interfaces.ButtonYesNoListner;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kippin.utils.Singleton;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.kippinretail.KippinInvoice.CommonNumberFormatter.getRound;
import static com.kippin.utils.Utility.parseDateByHiphenYMD;

/**
 * Created by Codewix Dev on 7/19/2017.
 */

public class EditRejectedInvoice extends SuperActivity implements View.OnClickListener, InterfaceUpdate {
    private static final int DATE_PICKER_ID = 1111;
    private static Calendar exp;
    Button btnAddNewItem, btn_save, btn_Cancel, btn_Preview, btn_Send;
    AdapterForRejectedNewItem adapter;
    InterfaceUpdate listener;
    TextView etDueDate, tvSubTotal, tvInvoiceNumber, tvInvoiceDate, tvTotal, tvBalanceDue;
    EditText etPaymentTerms, etDocumentRefrence, etSalesPerson, etShippingCost, etTaxAmount,
            etTaxPercentage, etDepostAdvancePayment, etNotes, etTerms, etTaxType;
    ItemModal itemModal = new ItemModal();
    Spinner spinner_ServiceProductType;
    TextView tvServiceOrProductType;
    int itemName = 01;
    String[] TAX_TYPE = {"Tax Type", "HST", "GST", "QST", "PST"};
    List<AddNewItem> listItems1 = new ArrayList<>();
    EditRejectInvoice editRecievedInvoice;
    private EditCustomer editCustomer;
    private ListView listForNewItem;

    private Integer year, month, day;
    private String inVoiceDate = "";

    @VisibleForTesting
    void clickDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(EditRejectedInvoice.this)
                .callback(startDateClick)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    public com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener startDateClick = new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = parseDateByHiphenYMD(dayOfMonth, monthOfYear + 1, year);
            year = year;
            month = monthOfYear;
            day = dayOfMonth;

            inVoiceDate = setFormatDate(year, month, day);
            android.util.Log.e("mDOB", "=" + inVoiceDate);
            tvInvoiceDate.setText(inVoiceDate);
            if (tvInvoiceNumber.getText().toString().length() > 0) {
                etPaymentTerms.setEnabled(true);
            } else {
                etPaymentTerms.setEnabled(false);
            }
            if (etPaymentTerms.getText().length() != 0) {
                Log.e("DatePickerDialog", "DatePickerDialog");
                getData();
            }
        }
    };
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
           /* dobEditText.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));*/
            inVoiceDate = setFormatDate(year, month, day);
            android.util.Log.e("mDOB", "=" + inVoiceDate);
            tvInvoiceDate.setText(inVoiceDate);
            if (tvInvoiceNumber.getText().toString().length() > 0) {
                etPaymentTerms.setEnabled(true);
            } else {
                etPaymentTerms.setEnabled(false);
            }
            if (etPaymentTerms.getText().length() != 0) {
                Log.e("DatePickerDialog", "DatePickerDialog");
                getData();
            }

        }
    };
    private String dueDate = "";
    private float shippingCost;
    private float subTotal = 0;
    private float totalAmount;
    private float depositPayment;
    private float duePayment;
    private boolean dataExist = false;
    private String buttonType;
    private RelativeLayout rlForTaxType;
    private RelativeLayout rlTaxTypeSpinner;
    private Spinner spinnerTaxType;
    private TextView tvTaxTypeSpinner;
    private String taxTypeSpinnerValue;
    private String CustomerId;
    private String tax;
    private int invoiceNumber;
    private RelativeLayout rlTaxLayout;
    private String responseMessage;
    private EditInvoice editInvoice;

    private TextWatcher mDepositAdavancePaymentTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (EditRejectedInvoice.this.tvTotal.getText().toString().length() != 0) {
                String total = tvTotal.getText().toString().replace(",", "");

                EditRejectedInvoice.this.totalAmount = Float.parseFloat(total);
                if (s.toString().length() == 0) {
                    EditRejectedInvoice.this.depositPayment = EditRejectedInvoice.this.totalAmount - 0.0f;
                } else if (EditRejectedInvoice.this.totalAmount > Float.parseFloat(s.toString())) {
                    EditRejectedInvoice.this.depositPayment = EditRejectedInvoice.this.totalAmount - Float.parseFloat(s.toString());
                } else {
                    EditRejectedInvoice.this.etDepostAdvancePayment.removeTextChangedListener(this);
                    EditRejectedInvoice.this.etDepostAdvancePayment.setText("");
                    EditRejectedInvoice.this.tvBalanceDue.setText("");
                    EditRejectedInvoice.this.etDepostAdvancePayment.addTextChangedListener(this);
                    CommonDialog.With(EditRejectedInvoice.this).Show("Deposit must be less than total.");
                }
                double BalanceDue_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble("" + CommonNumberFormatter.getRound(EditRejectedInvoice.this.depositPayment)), 2);
                tvBalanceDue.setText("" + BalanceDue_double);
                // tvBalanceDue.setText();
                return;
            }
            EditRejectedInvoice.this.etDepostAdvancePayment.removeTextChangedListener(this);
            EditRejectedInvoice.this.etDepostAdvancePayment.setText("");
            EditRejectedInvoice.this.tvBalanceDue.setText("");
            EditRejectedInvoice.this.etDepostAdvancePayment.addTextChangedListener(this);
            CommonDialog.With(EditRejectedInvoice.this).Show(EditRejectedInvoice.this.getResources().getString(R.string.deposit_amount_paid));

            /*

            if (tvTotal.getText().toString().length() != 0) {
                totalAmount = Float.parseFloat(tvTotal.getText().toString());
                if (s.toString().length() != 0) {
                    if (totalAmount > Float.parseFloat(s.toString())) {
                        depositPayment = totalAmount - Float.parseFloat(s.toString());
                    } else {
                        etDepostAdvancePayment.removeTextChangedListener(this);
                        etDepostAdvancePayment.setText("");
                        tvBalanceDue.setText("");
                        etDepostAdvancePayment.addTextChangedListener(this);
                        CommonDialog.With(EditRejectedInvoice.this).Show("Deposit must be less than total.");
                    }
                } else {
                    depositPayment = totalAmount - 0;
                }
                tvBalanceDue.setText(getRound(depositPayment));

            } else {
                etDepostAdvancePayment.removeTextChangedListener(this);
                etDepostAdvancePayment.setText("");
                tvBalanceDue.setText("");
                etDepostAdvancePayment.addTextChangedListener(this);
                CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.deposit_amount_paid));
            }*/
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher mEditPayment = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().length() != 0) {
                Log.e("onTextChanged", "onTextChanged");
                getData();
            } else {
                etDueDate.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private TextWatcher mShippingCostTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (EditRejectedInvoice.this.listItems1.size() != 0) {
                if (s.toString().length() != 0) {
                    EditRejectedInvoice.this.totalAmount = Float.parseFloat(s.toString()) + EditRejectedInvoice.this.subTotal;
                    EditRejectedInvoice.this.tvTotal.setText(CommonNumberFormatter.getRound(EditRejectedInvoice.this.totalAmount));
                } else {
                    EditRejectedInvoice.this.totalAmount = 0.0f + EditRejectedInvoice.this.subTotal;
                    EditRejectedInvoice.this.tvTotal.setText(CommonNumberFormatter.getRound(totalAmount));
                }
                if (EditRejectedInvoice.this.etDepostAdvancePayment.getText().length() != 0) {
                    String total = tvTotal.getText().toString().replace(",", "");
                    String DepostAdvancePayment = etDepostAdvancePayment.getText().toString().replace(",", "");
                    double BalanceDue_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble("" + CommonNumberFormatter.getRound(Float.parseFloat(total) - Float.parseFloat(DepostAdvancePayment))), 2);
                    // tvBalanceDue.setText();

                    // EditRejectedInvoice.this.tvBalanceDue.setText();
                    tvBalanceDue.setText("" + BalanceDue_double);
                    return;
                } else {
                    double BalanceDue_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble("" + CommonNumberFormatter.getRound(totalAmount)), 2);
                    // tvBalanceDue.setText();

                    EditRejectedInvoice.this.tvBalanceDue.setText("" + BalanceDue_double);
                    return;
                }
            }
            EditRejectedInvoice.this.etShippingCost.removeTextChangedListener(this);
            EditRejectedInvoice.this.etShippingCost.setText("");

            EditRejectedInvoice.this.etShippingCost.addTextChangedListener(this);
            CommonDialog.With(EditRejectedInvoice.this).Show(EditRejectedInvoice.this.getResources().getString(R.string.add_item_detail));

            /*if (dataExist) {
                if (s.toString().length() != 0) {
                    totalAmount = Float.parseFloat(s.toString()) + subTotal;
                    tvTotal.setText(getRound(totalAmount));
                } else {
                    totalAmount = 0 + subTotal;
                    tvTotal.setText(getRound(totalAmount));
                }

                if (etDepostAdvancePayment.getText().length() != 0) {
                    tvBalanceDue.setText(getRound(Float.parseFloat(tvTotal.getText().toString()) - Float.parseFloat(etDepostAdvancePayment.getText().toString())));

                } else {
                    tvBalanceDue.setText(getRound(totalAmount));
                }

            } else {
                etShippingCost.removeTextChangedListener(this);
                etShippingCost.setText("");
                etShippingCost.addTextChangedListener(this);
                CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.add_item_detail));
            }*/
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private static String formatDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
    }

    private static String setFormatDate(int year, int month, int day) {
        exp = Calendar.getInstance();
        exp.set(year, month, day);
        Date date = exp.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }


        return null;
    }

    public void getData() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date steepingdate = null;
        try {
            steepingdate = formatter.parse(inVoiceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(steepingdate);
        ca.add(Calendar.DAY_OF_YEAR, Integer.parseInt(etPaymentTerms.getText().toString()));

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dueDate = dateFormat.format(ca.getTime());
        etDueDate.setText(dueDate);
        System.out.println(dueDate);
     /*    Date myDate = null;
        try {
            myDate = dateFormat.parse(dueDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy");
        dueDate = timeFormat.format(myDate);
        Log.e("dueDate", "--" + dueDate);*/
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_invoice_activity);
        initlization();
    }


    private void initlization() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String InvoiceData = bundle.getString("InvoiceData");
            editInvoice = gson.fromJson(InvoiceData, EditInvoice.class);
            Log.e("InRFlowStatus:", "" + editInvoice.getInRFlowStatus());


            //editInvoice.

        }

        if (CommonUtility.InvoiceTitle.equals("1")) {
            generateRightText1("UPDATE INVOICE");

        } else {
            generateRightText1("UPDATE QUOTE");
        }
        // Buttons
        btnAddNewItem = (Button) findViewById(R.id.btnAddNewItem);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_Cancel = (Button) findViewById(R.id.btn_Cancel);
        if (editInvoice.getInRFlowStatus() != null) {
            Log.e("InRFlowStatus():", "" + editInvoice.getInRFlowStatus());
            if (editInvoice.getInRFlowStatus().equals("Draft") || editInvoice.getInRFlowStatus().equals("Pending Approval")) {
                btn_Cancel.setText("Cancel");
            } else {
                btn_Cancel.setText("Delete");
            }
        }

        //btn_Preview = (Button) findViewById(R.id.btn_Preview);
        btn_Send = (Button) findViewById(R.id.btn_Send);

        btnAddNewItem.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_Cancel.setOnClickListener(this);
        //   btn_Preview.setOnClickListener(this);
        btn_Send.setOnClickListener(this);

        // EditText
        etPaymentTerms = (EditText) findViewById(R.id.etPaymentTerms);
        etDocumentRefrence = (EditText) findViewById(R.id.etDocumentRefrence);
        etSalesPerson = (EditText) findViewById(R.id.etSalesPerson);
        etShippingCost = (EditText) findViewById(R.id.etShippingCost);
        etTaxAmount = (EditText) findViewById(R.id.etTaxAmount);
        etTaxType = (EditText) findViewById(R.id.etTaxType);
        etTaxPercentage = (EditText) findViewById(R.id.etTaxPercentage);
        etDepostAdvancePayment = (EditText) findViewById(R.id.etDepostAdvancePayment);
        etNotes = (EditText) findViewById(R.id.etNotes);
        etTerms = (EditText) findViewById(R.id.etTerms);
        etDueDate = (TextView) findViewById(R.id.etDueDate);

        // TextViews
        tvSubTotal = (TextView) findViewById(R.id.tvSubTotal);
        tvInvoiceNumber = (TextView) findViewById(R.id.tvInvoiceNumber);
        tvInvoiceDate = (TextView) findViewById(R.id.tvInvoiceDate);
        tvInvoiceDate.setOnClickListener(this);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvBalanceDue = (TextView) findViewById(R.id.tvBalanceDue);

        // RelativeLayout
        rlForTaxType = (RelativeLayout) findViewById(R.id.rlForTaxType);
        rlTaxTypeSpinner = (RelativeLayout) findViewById(R.id.rlTaxTypeSpinner);
        rlTaxLayout = (RelativeLayout) findViewById(R.id.rlTaxLayout);

        rlTaxTypeSpinner.setVisibility(View.GONE);
        rlForTaxType.setVisibility(View.GONE);
        rlTaxLayout.setVisibility(View.GONE);
        tvSubTotal.setVisibility(View.GONE);




       /* if (CommonUtility.UserType.equals("0")) {
            rlForTaxType.setVisibility(View.GONE);
            rlTaxTypeSpinner.setVisibility(View.VISIBLE);
        } else {
            rlForTaxType.setVisibility(View.VISIBLE);
            rlTaxTypeSpinner.setVisibility(View.GONE);
        }*/

        listForNewItem = (ListView) findViewById(R.id.listForNewItem);
        listForNewItem.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        spinnerTaxType = (Spinner) findViewById(R.id.spinnerTaxType);
        tvTaxTypeSpinner = (TextView) findViewById(R.id.tvTaxTypeSpinner);
        tvTaxTypeSpinner.setOnClickListener(this);
        ArrayAdapter<String> adapterTaxType = new ArrayAdapter<String>(EditRejectedInvoice.this, android.R.layout.simple_list_item_1, TAX_TYPE);
        spinnerTaxType.setAdapter(adapterTaxType);

        spinnerTaxType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
               /* if (position == 0) {
                    tvTaxTypeSpinner.setTextColor(Color.parseColor("#A1A1A1"));

                } else {
                    tvTaxTypeSpinner.setTextColor(Color.BLACK);
                    taxTypeSpinnerValue = spinnerTaxType.getSelectedItem().toString();
                    tvTaxTypeSpinner.setText(taxTypeSpinnerValue);

                }*/
                if (position == 0) {
                    EditRejectedInvoice.this.tvTaxTypeSpinner.setTextColor(Color.parseColor("#A1A1A1"));
                    return;
                }
                EditRejectedInvoice.this.tvTaxTypeSpinner.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                EditRejectedInvoice.this.taxTypeSpinnerValue = EditRejectedInvoice.this.spinnerTaxType.getSelectedItem().toString();
                EditRejectedInvoice.this.tvTaxTypeSpinner.setText(EditRejectedInvoice.this.taxTypeSpinnerValue);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        if (tvInvoiceNumber.getText().toString().length() > 0) {
            etPaymentTerms.setEnabled(true);
        } else {
            etPaymentTerms.setEnabled(false);
        }


        etShippingCost.addTextChangedListener(new DecimalFilter(etShippingCost, activity));
        etShippingCost.addTextChangedListener(mShippingCostTextWatcher);

        // Deposit Payment section
        etDepostAdvancePayment.addTextChangedListener(new DecimalFilter(etDepostAdvancePayment, activity));
        etDepostAdvancePayment.addTextChangedListener(mDepositAdavancePaymentTextWatcher);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CustomerId = extras.getString("CustomerId");

        }

        // ListView

        itemModal.setItem("" + String.format("%02d", itemName));
        itemModal.setId("" + itemName);
        //   listItems1.add(itemModal);
        listener = this;
//        adapter = new AdapterForRejectedNewItem(EditRejectedInvoice.this, listItems1, this);
//        listForNewItem.setAdapter(adapter);
        getInvoiceNumber();
        settingFocus();
    }


    private void getInvoiceNumber() {
        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().getEditInvoiceData1(editInvoice.getId(), new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                        Gson gson = new Gson();
                        editRecievedInvoice = gson.fromJson(jsonElement.toString(), new TypeToken<EditRejectInvoice>() {
                        }.getType());
                        tvInvoiceDate.setText(editRecievedInvoice.getInvoiceDate());
                        inVoiceDate = editRecievedInvoice.getDueDate().toString();
                        etDueDate.setText(editRecievedInvoice.getDueDate());
                        if (editRecievedInvoice.getTotal().toString().length() > 0) {
                            double subtotal_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(editRecievedInvoice.getTotal().toString()), 2);
                            tvSubTotal.setText("" + subtotal_double);
                            tvTotal.setText("" + subtotal_double);
                            Log.e("subtotal_double:", "" + subtotal_double);
                        }
                        tvInvoiceNumber.setText(editRecievedInvoice.getInvoiceNumber());
                        if (tvInvoiceNumber.getText().toString().length() > 0) {
                            etPaymentTerms.setEnabled(true);
                        } else {
                            etPaymentTerms.setEnabled(false);
                        }
                        if (editRecievedInvoice.getBalanceDue() != null) {
                            if (editRecievedInvoice.getBalanceDue().toString().length() > 0) {
                                double BalanceDue_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(editRecievedInvoice.getBalanceDue().toString()), 2);
                                tvBalanceDue.setText("" + BalanceDue_double);
                            }
                        }

                        etPaymentTerms.setText(editRecievedInvoice.getPaymentTerms().toString());
                        etDocumentRefrence.setText(editRecievedInvoice.getDocumentRef().toString());
                        etPaymentTerms.addTextChangedListener(mEditPayment);
                        try {
                            etSalesPerson.setText(editRecievedInvoice.getSalesPerson().toString());
                        } catch (Exception e) {
                            etSalesPerson.setText("");
                        }

                        etShippingCost.removeTextChangedListener(mShippingCostTextWatcher);
                        etShippingCost.setText(getRound(Float.parseFloat(editRecievedInvoice.getShippingCost().toString())));
                        etShippingCost.addTextChangedListener(mShippingCostTextWatcher);

                        etTaxAmount.setText(editRecievedInvoice.getTax().toString());
                        etTaxPercentage.setText(editRecievedInvoice.getTax().toString());

                        etDepostAdvancePayment.removeTextChangedListener(mDepositAdavancePaymentTextWatcher);
                        etDepostAdvancePayment.setText(getRound(Float.parseFloat(editRecievedInvoice.getDepositePayment().toString())));
                        etDepostAdvancePayment.addTextChangedListener(mDepositAdavancePaymentTextWatcher);
                        totalAmount = Float.parseFloat(editRecievedInvoice.getDepositePayment().toString());
                        etNotes.setText(editRecievedInvoice.getNote().toString());
                        try {
                            etTerms.setText(editRecievedInvoice.getTerms().toString());

                        } catch (Exception e) {
                            etTerms.setText("");
                        }

                        try {
                            etTaxType.setText(editRecievedInvoice.getTaxType().toString());

                        } catch (Exception e) {
                            etTaxType.setText("");
                        }
                        getItemDetail();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", error.getMessage() + error.getUrl() + "");
                    }
                }

        );
    }


    public void getItemDetail() {
        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().getInvoiceItems(editInvoice.getId(), new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Gson gson = new Gson();
                        listItems1 = gson.fromJson(jsonElement.toString(), new TypeToken<List<AddNewItem>>() {
                        }.getType());
//                        if (!getInvoiceOnly()) {
//                            for (int i = 0; i < classificationList.size(); i++) {
//                                // if (classificationList.get(i).getServiceType().equals("")) {
//                                classificationList.get(i).setServiceType("Please Select");
//                                classificationList.get(i).setServiceTypeId(0);
//                                // }
//                            }
                        adapter = new AdapterForRejectedNewItem(EditRejectedInvoice.this, listItems1, listener);
                        listForNewItem.setAdapter(adapter);
                        listForNewItem.setSelection(listForNewItem.getAdapter().getCount() - 1);
                        if (listItems1.size() != 0) {
                            itemName = listItems1.size();
                            // String.format("%02d", itemName)
                        } else {
                            itemName = listItems1.size();
                            //listItems1
                        }
                        updateWithCalculations();
                        if (editRecievedInvoice.getCustomerId() != null) {
                            CustomerId = editRecievedInvoice.getCustomerId().toString();
                        }
                        Log.e("CustomerId:", "" + CustomerId);
                        if (Singleton.getUser() != null) {
                            Log.e("is_IsOnlyInvoice:", "" + Singleton.getUser().is_IsOnlyInvoice());
                            if (Singleton.getUser().is_IsOnlyInvoice()) {
                                CommonUtility.IsFinance = "true";
                            }

                            /*if (Singleton.getUser().isIsOnlyInvoice() != null) {
                                if (Singleton.getUser().is_IsOnlyInvoice()) {
                                    Log.e("CustomerId:", "" + CustomerId);
                                    CommonUtility.IsFinance = "true";
                                } else {
                                    CommonUtility.IsFinance = "false";
                                }

                            }*/
                            else {
                                CommonUtility.IsFinance = "false";
                                getCustomerDetail(editRecievedInvoice.getCustomerId());
                            }
                        } else {
                            CommonUtility.IsFinance = "false";
                            getCustomerDetail(editRecievedInvoice.getCustomerId());
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", error.getMessage() + error.getUrl() + "");
                    }
                }

        );
    }


    @Override
    public void onClick(View v) {

        if (listItems1.size() != 0) {
            dataExist = true;
        } else {
            dataExist = false;
        }

        initDueDate();

        switch (v.getId()) {
            case R.id.btnAddNewItem:

                itemName++;
                if (itemName <= 99) {
                    AddNewItem itemModal1 = new AddNewItem();
                    itemModal1.setItem("" + (String.format("%02d", itemName)));
                    itemModal1.setId(itemName);
                    listItems1.add(itemModal1);

                    adapter = new AdapterForRejectedNewItem(EditRejectedInvoice.this, listItems1, this);
                    listForNewItem.setAdapter(adapter);
                    listForNewItem.setSelection(listForNewItem.getAdapter().getCount() - 1);
                }
                break;

            case R.id.tvInvoiceDate:
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                //int month_day=month+1;
                clickDate(year, month, day, R.style.Date_Picker_Spinner);
                // showDialog(DATE_PICKER_ID);
                break;

            case R.id.btn_save:
                Log.e("here", "here");
                buttonType = "Save";
                if (dataExist) {
                    if (!inVoiceDate.equals("") && !dueDate.equals("") /*&& etDepostAdvancePayment.getText().toString().length() != 0*/) {
                        //  if (etTaxAmount.getText().toString().length() != 0 || etTaxPercentage.getText().toString().length() != 0) {
                        createInvoice();
                    /*} else {
                        CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.empty_fields));
                    }*/

                    } else {
                        CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                    }
                } else {
                    CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.add_item_detail));
                }
                break;
            case R.id.btn_Cancel:
                if (btn_Cancel.getText().toString().equals("Cancel")) {
                    onBackPressed();
                } else {
                    Log.e("Dialog", "Dialog call");
                    showDialogMessage("Are you sure you want to delete?");
                }

                //    onBackPressed();
                break;
          /*  case R.id.btn_Preview:
                break;*/
            case R.id.btn_Send:
                buttonType = "Send";
                if (dataExist) {
                    if (!inVoiceDate.equals("") && !dueDate.equals("") /*&& etDepostAdvancePayment.getText().toString().length() != 0*/) {
                        //if (!inVoiceDate.equals("") && !dueDate.equals("") && etTaxType.getText().toString().length() != 0) {
                        //if (etTaxAmount.getText().toString().length() != 0 || etTaxPercentage.getText().toString().length() != 0) {
                        createInvoice();
                    /*} else {
                        CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.empty_fields));
                    }*/

                    } else {
                        CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                    }
                } else {
                    CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.add_item_detail));
                }
                break;
            case R.id.tvTaxTypeSpinner:
                spinnerTaxType.performClick();
                break;
        }

    }

    private void initDueDate() {
        if (dueDate.equals("")) {
            dueDate = etDueDate.getText().toString();
            return;
        }
       /* SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date steepingdate = null;
        try {
            steepingdate = formatter.parse(inVoiceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar ca = Calendar.getInstance();
        ca.setTime(steepingdate);
        ca.add(Calendar.DAY_OF_YEAR, Integer.parseInt(etPaymentTerms.getText().toString()));

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dueDate = dateFormat.format(ca.getTime());
        etDueDate.setText(dueDate);*/
    }

    @Override
    public void updateValues(int x) {
        itemName = x;
        updateWithCalculations();
    }

   /* private String getRound(float totalAmount) {
        DecimalFormat df = new DecimalFormat("##.0");
        String dx = df.format(totalAmount);
        return dx;
    }*/

    private void settingFocus() {
        ((EditText) findViewById(R.id.etShippingCost)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etShippingCost.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etShippingCost.getText().toString());
                        etShippingCost.setText(getRound(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });
        ((EditText) findViewById(R.id.etDepostAdvancePayment)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etDepostAdvancePayment.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etDepostAdvancePayment.getText().toString());
                        etDepostAdvancePayment.setText(getRound(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });

    }


    private void createInvoice() {
        if (buttonType.equals("Send")) {
            if (CommonUtility.InvoiceTitle.equals("1")) {
                responseMessage = "Invoice Sent Successfully";
            } else {
                responseMessage = "Quote Sent Successfully";
            }
        } else if (buttonType.equals("Delete")) {
            if (CommonUtility.InvoiceTitle.equals("1")) {
                responseMessage = "Invoice Delete Successfully";
            } else {
                responseMessage = "Quote Delete Successfully";
            }
        } else {
            if (CommonUtility.InvoiceTitle.equals("1")) {
                responseMessage = "Invoice Saved Successfully";
            } else {
                responseMessage = "Quote Saved Successfully";
            }
        }

       /* if (CommonUtility.UserType.equals("0")) {
            taxType = taxTypeSpinnerValue;
        } else {
            taxType = etTaxType.getText().toString();
        }
        if (etTaxAmount.getText().toString().length() != 0) {
            tax = "A_" + etTaxAmount.getText().toString();
        } else {
            tax = "P_" + etTaxPercentage.getText().toString();
        }*/

        String[] itemArray = new String[listItems1.size()];
        String[] itemArrayID = new String[listItems1.size()];
        String[] descriptionArray = new String[listItems1.size()];
        String[] rateArray = new String[listItems1.size()];
        String[] quantityHours = new String[listItems1.size()];
        String[] amountArray = new String[listItems1.size()];
        String[] discountArray = new String[listItems1.size()];
        String[] productTypeArray = new String[listItems1.size()];
        String[] productTypeIdArray = new String[listItems1.size()];
        String[] cutomerService = new String[listItems1.size()];
        String[] cutomerServiceID = new String[listItems1.size()];
        String[] gstArray = new String[listItems1.size()];
        String[] qstArray = new String[listItems1.size()];
        String[] hstArray = new String[listItems1.size()];
        String[] pstArray = new String[listItems1.size()];
        String[] taxArray = new String[listItems1.size()];
        String[] subTotalArray = new String[listItems1.size()];

        for (int i = 0; i < listItems1.size(); i++) {

            // service product id
            if (listItems1.get(i).getServiceType() != null && !listItems1.get(i).getServiceType().equals("")) {
                productTypeArray[i] = listItems1.get(i).getServiceType();
            } else {
                productTypeArray[i] = "null";
                // CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                //return;
            }
            if (listItems1.get(i).getServiceTypeId() != null && !listItems1.get(i).getServiceTypeId().equals("")) {
                productTypeIdArray[i] = listItems1.get(i).getServiceTypeId().toString();
                // productTypeIdArray[i] = Integer.parseInt(number[0]);
            } else {
                productTypeIdArray[i] = "0";
                // CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                //return;
            }

            if (listItems1.get(i).getDescription() != null && !listItems1.get(i).getDescription().equals("")) {
                descriptionArray[i] = listItems1.get(i).getDescription();
            } else {
                //descriptionArray[i] = "";
                CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                return;
            }
            if (listItems1.get(i).getQuantity() != null && !listItems1.get(i).getQuantity().equals("")) {
                quantityHours[i] = listItems1.get(i).getQuantity().toString();
                // quantityHours[i] = Integer.parseInt(number[0]);

            } else {
                CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                return;
            }

            if (listItems1.get(i).getRate() != null && !listItems1.get(i).getRate().equals("")) {
                rateArray[i] = listItems1.get(i).getRate().toString();
                // rateArray[i] = Integer.parseInt(number[0]);
            } else {
                CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                return;
            }
            if (listItems1.get(i).getItem() != null && !listItems1.get(i).getItem().equals("")) {
                itemArray[i] = listItems1.get(i).getItem();
            } else {
                //itemArray[i] = "";
            }

            if (listItems1.get(i).getId() != null && !listItems1.get(i).getId().equals("")) {
                itemArrayID[i] = "" + listItems1.get(i).getId();
            } else {

            }

            if (listItems1.get(i).getAmount() != null && !listItems1.get(i).getAmount().equals("")) {
                amountArray[i] = listItems1.get(i).getAmount().toString();
                // Log.e("number:", "" + number.length);
                // Log.e("split:", "" + listItems1);
                //  amountArray[i] = Integer.parseInt(number[0]);
            } else {

            }


            if (listItems1.get(i).getDiscount() != null && !listItems1.get(i).getDiscount().equals("")) {
                discountArray[i] = "" + listItems1.get(i).getDiscount();
            } else {
                discountArray[i] = "null";
            }

            if (listItems1.get(i).getSubTotal() != null && !listItems1.get(i).getSubTotal().equals("")) {
                subTotalArray[i] = listItems1.get(i).getSubTotal().toString();
                //subTotalArray[i] = Integer.parseInt(number[0]);
            } else {

            }

            if (listItems1.get(i).getTax() != null && !listItems1.get(i).getTax().equals("")) {
                taxArray[i] = listItems1.get(i).getTax().toString();
            } else {
                taxArray[i] = "null";
            }

            // GST
            if (listItems1.get(i).getGSTTax() != null && !listItems1.get(i).getGSTTax().equals("")) {
                gstArray[i] = listItems1.get(i).getGSTTax().toString();
            } else {
                gstArray[i] = "null";
            }
            // QST
            if (listItems1.get(i).getQSTTax() != null && !listItems1.get(i).getQSTTax().equals("")) {
                qstArray[i] = "" + listItems1.get(i).getQSTTax();
            } else {
                qstArray[i] = "null";
            }

            // HST
            if (listItems1.get(i).getHSTTax() != null && !listItems1.get(i).getHSTTax().equals("")) {
                hstArray[i] = "" + listItems1.get(i).getHSTTax();
            } else {
                hstArray[i] = "null";
            }
            // PST
            if (listItems1.get(i).getPSTTax() != null && !listItems1.get(i).getPSTTax().equals("")) {
                pstArray[i] = "" + listItems1.get(i).getPSTTax();
            } else {
                pstArray[i] = "null";
            }

            cutomerService[i] = "";
            cutomerServiceID[i] = "0";
        }

        HashMap templatePosts = new HashMap();
        templatePosts.put("InvoiceNumber", "" + editRecievedInvoice.getInvoiceNumber());
        templatePosts.put("InvoiceDate", editRecievedInvoice.getInvoiceDate());
        templatePosts.put("PaymentTerms", etPaymentTerms.getText().toString());
        templatePosts.put("DueDate", dueDate);
        templatePosts.put("DocumentRef", etDocumentRefrence.getText().toString());
        templatePosts.put("SalesPerson", etSalesPerson.getText().toString());
        templatePosts.put("ShippingCost", etShippingCost.getText().toString());
        //   templatePosts.put("SubTotal", subTotalArray);
        templatePosts.put("Total", tvTotal.getText().toString());
        templatePosts.put("DepositePayment", etDepostAdvancePayment.getText().toString());
        templatePosts.put("BalanceDue", tvBalanceDue.getText().toString());
        templatePosts.put("Note", etNotes.getText().toString());
        templatePosts.put("Terms", etTerms.getText().toString());
        templatePosts.put("UserId", getUserId());
        templatePosts.put("SectionType", SectionTypeReceived);
        templatePosts.put("Customer_Service", cutomerService);
        templatePosts.put("Customer_ServiceTypeId", cutomerServiceID);
        templatePosts.put("CustomerId", editRecievedInvoice.getCustomerId());


        if (responseMessage.equals("Quote Delete Successfully")) {
            templatePosts.put("ButtonType", "Decline");
        } else {
            templatePosts.put("ButtonType", buttonType);
        }


        templatePosts.put("Id", "" + editRecievedInvoice.getId());

        templatePosts.put("ItemId", itemArrayID);
        templatePosts.put("RejectReason", "");

        templatePosts.put("Type", CommonUtility.InvoiceTitle);
        templatePosts.put("ShippingCity", "");
        templatePosts.put("Item", itemArray);
        templatePosts.put("ServiceTypeId", productTypeIdArray);
        templatePosts.put("ServiceType", productTypeArray);
        templatePosts.put("Description", descriptionArray);
        templatePosts.put("Quantity", quantityHours);

        templatePosts.put("Rate", rateArray);
        templatePosts.put("Amount", amountArray);
        templatePosts.put("SubTotal", subTotalArray);
        templatePosts.put("Discount", discountArray);
        templatePosts.put("Tax", taxArray);
        templatePosts.put("GST_Tax", gstArray);
        templatePosts.put("QST_Tax", qstArray);
        templatePosts.put("HST_Tax", hstArray);
        templatePosts.put("PST_Tax", pstArray);


        LoadingBox.showLoadingDialog(EditRejectedInvoice.this, "");
        Log.e("templatePosts", "" + new Gson().toJson(templatePosts));
        if (buttonType.equals("Delete")) {
            RestClient.getApiFinanceServiceForPojo().UpdateInvoice(getTypedInput(new Gson().toJson(templatePosts)), getCallback(new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    LoadingBox.dismissLoadingDialog();
                    Log.e("Tag", "Request data " + new Gson().toJson(jsonElement));
                    // {"ResponseCode":1,"ResponseMessage":"Please check your mail for email confirmation.","UserId":3}
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String ResponseMessage = jsonObject.getString("ResponseMessage");
                        String ResponseCode = jsonObject.getString("ResponseCode");
                        String UserId = jsonObject.getString("UserId");

                        if (ResponseCode.equals("1")) {
                            CommonDialog.showDialog2Button(EditRejectedInvoice.this, responseMessage, new OnDialogDismissListener() {
                                @Override
                                public void onDialogDismiss() {
                                    CommonUtility.HomeClick(EditRejectedInvoice.this);
                                }
                            });
                        } else {
                            CommonDialog.With(EditRejectedInvoice.this).Show(ResponseMessage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Failure ", " = " + error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(EditRejectedInvoice.this, error);
                }
            }));
        } else if (editInvoice.getInRFlowStatus().equals("Rejected")) {
            RestClient.getApiFinanceServiceForPojo().EditInvoice(getTypedInput(new Gson().toJson(templatePosts)), getCallback(new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    LoadingBox.dismissLoadingDialog();
                    Log.e("Tag", "Request data " + new Gson().toJson(jsonElement));
                    // {"ResponseCode":1,"ResponseMessage":"Please check your mail for email confirmation.","UserId":3}
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String ResponseMessage = jsonObject.getString("ResponseMessage");
                        String ResponseCode = jsonObject.getString("ResponseCode");
                        String UserId = jsonObject.getString("UserId");

                        if (ResponseCode.equals("1")) {
                            CommonDialog.showDialog2Button(EditRejectedInvoice.this, responseMessage, new OnDialogDismissListener() {
                                @Override
                                public void onDialogDismiss() {
                                    CommonUtility.HomeClick(EditRejectedInvoice.this);
                                }
                            });
                        } else {
                            CommonDialog.With(EditRejectedInvoice.this).Show(ResponseMessage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Failure ", " = " + error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(EditRejectedInvoice.this, error);
                }
            }));
        } else if (editInvoice.getInRFlowStatus().equals("Draft")) {
            RestClient.getApiFinanceServiceForPojo().EditInvoice(getTypedInput(new Gson().toJson(templatePosts)), getCallback(new Callback<JsonElement>() {

                @Override
                public void success(JsonElement jsonElement, Response response) {
                    LoadingBox.dismissLoadingDialog();
                    Log.e("Tag", "Request data " + new Gson().toJson(jsonElement));
                    // {"ResponseCode":1,"ResponseMessage":"Please check your mail for email confirmation.","UserId":3}
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String ResponseMessage = jsonObject.getString("ResponseMessage");
                        String ResponseCode = jsonObject.getString("ResponseCode");
                        String UserId = jsonObject.getString("UserId");

                        if (ResponseCode.equals("1")) {
                            CommonDialog.showDialog2Button(EditRejectedInvoice.this, responseMessage, new OnDialogDismissListener() {
                                @Override
                                public void onDialogDismiss() {
                                    CommonUtility.HomeClick(EditRejectedInvoice.this);
                                }
                            });
                        } else {
                            CommonDialog.With(EditRejectedInvoice.this).Show(ResponseMessage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Failure ", " = " + error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(EditRejectedInvoice.this, error);
                }
            }));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 011) {
            AddNewItem itemModal = (AddNewItem) data.getSerializableExtra("data");
            String position = data.getStringExtra("position");
            int pos = Integer.parseInt(position);
            listItems1.set(pos, itemModal);
            adapter.updateValues(pos, itemModal);
            //adapter.notifyDataSetChanged();
            updateWithCalculations();


        }
    }

    private void updateWithCalculations() {
       /* subTotal = 0;

        for (int i = 0; i < listItems1.size(); i++) {
            if (listItems1.get(i).getSubTotal() != null && !listItems1.get(i).getSubTotal().equals("")) {
                dataExist = true;
                subTotal += Float.parseFloat(listItems1.get(i).getSubTotal().toString());
                tvTotal.setText("" + subTotal);
            }
        }

        if (etShippingCost.getText().toString().length() != 0) {
            subTotal = Float.parseFloat(tvTotal.getText().toString()) + Float.parseFloat(etShippingCost.getText().toString());
            tvTotal.setText("" + subTotal);
            tvBalanceDue.setText("" + subTotal);
        }
        totalAmount = Float.parseFloat(tvTotal.getText().toString());
        if (etDepostAdvancePayment.getText().toString().length() != 0) {
            if (totalAmount > Float.parseFloat(etDepostAdvancePayment.getText().toString())) {
                tvBalanceDue.setText(getRound(Float.parseFloat(tvTotal.getText().toString()) - Float.parseFloat(etDepostAdvancePayment.getText().toString())));
            } else {
                etDepostAdvancePayment.removeTextChangedListener((TextWatcher) this);
                etDepostAdvancePayment.setText("");
                tvBalanceDue.setText("");
                etDepostAdvancePayment.addTextChangedListener((TextWatcher) this);
                CommonDialog.With(EditRejectedInvoice.this).Show("Deposit must be less than total.");
            }
        } else {
            tvBalanceDue.setText("" + subTotal);
        }*/
        this.subTotal = 0.0f;
        int i = 0;
        while (i < this.listItems1.size()) {
            if (!(((AddNewItem) this.listItems1.get(i)).getSubTotal() == null || ((AddNewItem) this.listItems1.get(i)).getSubTotal().equals(""))) {
                this.dataExist = true;
                this.subTotal = Float.parseFloat(((AddNewItem) this.listItems1.get(i)).getSubTotal().toString()) + this.subTotal;
                this.tvTotal.setText("" + this.subTotal);
                if (tvTotal.getText().toString().length() > 0) {
                    double subtotal_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(tvTotal.getText().toString()), 2);
                    tvTotal.setText("" + subtotal_double);
                    Log.e("subtotal_double:", "" + subtotal_double);
                }
            }
            i++;
        }
        if (this.etShippingCost.getText().toString().length() != 0) {
            this.subTotal = Float.parseFloat(this.tvTotal.getText().toString().replace(",", "")) + Float.parseFloat(this.etShippingCost.getText().toString().replace(",", ""));
            this.tvTotal.setText("" + this.subTotal);
            double BalanceDue_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble("" + subTotal), 2);
            tvBalanceDue.setText("" + BalanceDue_double);

            if (tvTotal.getText().toString().length() > 0) {
                double subtotal_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(tvTotal.getText().toString()), 2);
                tvTotal.setText("" + subtotal_double);
                Log.e("subtotal_double:", "" + subtotal_double);
            }

        }
        String amt = tvTotal.getText().toString().replace(",", "");
        totalAmount = Float.parseFloat(amt);
        String advance = etDepostAdvancePayment.getText().toString().replace(",", "");
        if (this.etDepostAdvancePayment.getText().toString().length() == 0) {
            double BalanceDue_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble("" + subTotal), 2);
            tvBalanceDue.setText("" + BalanceDue_double);
            //  this.tvBalanceDue.setText("" + this.subTotal);
        } else if (this.totalAmount > Float.parseFloat(advance)) {
            String commaReplaceTotal = tvTotal.getText().toString().replace(",", "");
            String commadeposit = etDepostAdvancePayment.getText().toString().replace(",", "");
            String balance = CommonNumberFormatter.getRound(Float.parseFloat(commaReplaceTotal) - Float.parseFloat(commadeposit));
            Log.e("balance:", "" + balance);
            double BalanceDue_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble("" + balance.replace(",", "")), 2);
            tvBalanceDue.setText("" + BalanceDue_double);

        } else {
            // EditRejectedInvoice.this.etDepostAdvancePayment.removeTextChangedListener((TextWatcher) this);
            // EditRejectedInvoice.this.etDepostAdvancePayment.setText("");
            tvBalanceDue.setText("");
            // EditRejectedInvoice.this.etDepostAdvancePayment.addTextChangedListener((TextWatcher) this);
            etDepostAdvancePayment.removeTextChangedListener(mDepositAdavancePaymentTextWatcher);
            etDepostAdvancePayment.setText("");
            tvBalanceDue.setText("");
            etDepostAdvancePayment.addTextChangedListener(mDepositAdavancePaymentTextWatcher);


            CommonDialog.With(this).Show("Deposit must be less than total.");
        }
    }


    private void showDialogMessage(String s) {

        CommonDialog.DialogDeleteInvoice(EditRejectedInvoice.this, s, new ButtonYesNoListner() {
            @Override
            public void YesButton() {
                buttonType = "Delete";
                if (dataExist) {
                    if (!inVoiceDate.equals("") && !dueDate.equals("") /*&& etDepostAdvancePayment.getText().toString().length() != 0*/) {
                        //  if (etTaxAmount.getText().toString().length() != 0 || etTaxPercentage.getText().toString().length() != 0) {
                        createInvoice();
                    /*} else {
                        CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.empty_fields));
                    }*/

                    } else {
                        CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                    }
                } else {
                    CommonDialog.With(EditRejectedInvoice.this).Show(getResources().getString(R.string.add_item_detail));
                }


            }

            @Override
            public void NoButton() {

            }
        });


    }

    private void getCustomerDetail(int id) {
        int mappingid = id;

        //   LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().getCustomerDetail(mappingid, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        // LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                        Gson gson = new Gson();
                        editCustomer = gson.fromJson(jsonElement.toString(), new TypeToken<EditCustomer>() {
                        }.getType());
                        if (editCustomer.getResponseCode() == 1) {
                            if (editCustomer.getIsFinanceUser() != null) {
                                if (editCustomer.getIsFinanceUser()) {
                                    Log.e("If-------Finance", "" + editCustomer.getIsFinanceUser());
                                    CommonUtility.IsFinance = "true";
                                } else {
                                    Log.e("Else--------Finance", "" + editCustomer.getIsFinanceUser());
                                    CommonUtility.IsFinance = "false";
                                }

                            } else {
                                CommonUtility.IsFinance = "false";
                                Log.e("No Finance", "No Finance");
                            }
                            //setData();
                        } else {
                            CommonDialog.With(EditRejectedInvoice.this).Show(editCustomer.getResponseMessage());
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
