package com.kippinretail.KippinInvoice;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.KippinInvoice.InvoiceAdapter.AdapterForNewItem;
import com.kippinretail.KippinInvoice.InvoiceInterface.InterfaceUpdate;
import com.kippinretail.KippinInvoice.ModalInvoice.ItemModal;
import com.kippinretail.Modal.Invoice.EditCustomer;
import com.kippinretail.Modal.InvoiceLogin.InvoiceLoginData;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
 * Created by kamaljeet.singh on 11/7/2016.
 */

public class CreateInvoiceActivity extends SuperActivity implements View.OnClickListener, InterfaceUpdate {
    private static final int DATE_PICKER_ID = 1111;
    private static Calendar exp;
    Button btnAddNewItem, btn_save, btn_Cancel, btn_Preview, btn_Send;
    AdapterForNewItem adapter;
    TextView etDueDate, tvSubTotal, tvInvoiceNumber, tvInvoiceDate, tvTotal, tvBalanceDue;
    EditText etPaymentTerms, etDocumentRefrence, etSalesPerson, etShippingCost, etTaxAmount,
            etTaxPercentage, etDepostAdvancePayment, etNotes, etTerms, etTaxType;
    ItemModal itemModal = new ItemModal();
    Spinner spinner_ServiceProductType;
    TextView tvServiceOrProductType;
    int itemName = 01;
    String[] TAX_TYPE = {"Tax Type", "HST", "GST", "QST", "PST"};
    private EditCustomer editCustomer;
    private ListView listForNewItem;
    private List<ItemModal> listItems = new ArrayList<>();
    private Integer year, month, day;
    private String inVoiceDate = "";
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
            inVoiceDate = formatDate(year, month, day);
            android.util.Log.e("mDOB", "=" + inVoiceDate);
            tvInvoiceDate.setText(inVoiceDate);
            if (tvInvoiceNumber.getText().toString().length() > 0) {
                etPaymentTerms.setEnabled(true);
            } else {
                etPaymentTerms.setEnabled(false);
            }

            if (etPaymentTerms.getText().length() != 0) {
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
    InvoiceLoginData invoiceLoginData = null;

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

        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        Date steepingdate = null;
        try {
            steepingdate = formatter.parse(inVoiceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(steepingdate);
        ca.add(Calendar.DAY_OF_YEAR, Integer.parseInt(etPaymentTerms.getText().toString()));

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_invoice_activity);
        initlization();
    }

    private void initlization() {


        if (CommonUtility.InvoiceTitle.equals("1")) {
            generateRightText1(getString(R.string.create_invoice));

        } else {
            generateRightText1(getString(R.string.create_proforma));
        }
        // Buttons
        btnAddNewItem = (Button) findViewById(R.id.btnAddNewItem);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_Cancel = (Button) findViewById(R.id.btn_Cancel);
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
        ArrayAdapter<String> adapterTaxType = new ArrayAdapter<String>(CreateInvoiceActivity.this, android.R.layout.simple_list_item_1, TAX_TYPE);
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
                    CreateInvoiceActivity.this.tvTaxTypeSpinner.setTextColor(Color.parseColor("#A1A1A1"));
                    return;
                }
                CreateInvoiceActivity.this.tvTaxTypeSpinner.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                CreateInvoiceActivity.this.taxTypeSpinnerValue = CreateInvoiceActivity.this.spinnerTaxType.getSelectedItem().toString();
                CreateInvoiceActivity.this.tvTaxTypeSpinner.setText(CreateInvoiceActivity.this.taxTypeSpinnerValue);


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

        etPaymentTerms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != 0) {
                    getData();
                } else {
                    etDueDate.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etShippingCost.addTextChangedListener(new DecimalFilter(etShippingCost, activity));
        etShippingCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (dataExist) {
                    if (s.toString().length() != 0) {
                        Log.e("subTotal:", "" + subTotal);
                        totalAmount = Float.parseFloat(s.toString().replace(",", "")) + subTotal;
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
                    CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.add_item_detail));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // Deposit Payment section
        etDepostAdvancePayment.addTextChangedListener(new DecimalFilter(etDepostAdvancePayment, activity));
        etDepostAdvancePayment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tvTotal.getText().toString().length() != 0) {
                    String string = tvTotal.getText().toString().replace(",", "");
                    totalAmount = Float.parseFloat(string);
                    if (s.toString().length() != 0) {
                        if (totalAmount > Float.parseFloat(s.toString())) {
                            depositPayment = totalAmount - Float.parseFloat(s.toString());
                        } else {
                            etDepostAdvancePayment.removeTextChangedListener(this);
                            etDepostAdvancePayment.setText("");
                            tvBalanceDue.setText("");
                            etDepostAdvancePayment.addTextChangedListener(this);
                            CommonDialog.With(CreateInvoiceActivity.this).Show("Deposit must be less than total.");
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
                    CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.deposit_amount_paid));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CustomerId = extras.getString("CustomerId");
            Log.e("CustomerId:", "" + CustomerId);

            invoiceLoginData = CommonData.getInvoiceUserData(this);
            if (invoiceLoginData != null) {
                if (!invoiceLoginData.getIsOnlyInvoice()) {
                    Log.e("getIsOnlyInvoice:", "" + invoiceLoginData.getIsOnlyInvoice());
                    CommonUtility.IsFinance = "true";
                    // getCustomerDetail();
                } else {
                    CommonUtility.IsFinance = "false";
                    getCustomerDetail();

                   /* Log.e("isIsOnlyInvoice:", "" + Singleton.getUser().is_IsOnlyInvoice());
                    if (Singleton.getUser().is_IsOnlyInvoice()) {
                        CommonUtility.IsFinance = "true";
                    } else {
                        CommonUtility.IsFinance = "false";
                        getCustomerDetail();
                    }*/

                }
            } else {
                if (Singleton.getUser().is_IsOnlyInvoice()) {
                    Log.e("is---IsOnly-----Invoice:", "" + Singleton.getUser().is_IsOnlyInvoice());
                    CommonUtility.IsFinance = "true";

                } else {
                    CommonUtility.IsFinance = "false";
                    getCustomerDetail();
                }
            }
        }

        // ListView

        itemModal.setItem("" + String.format("%02d", itemName));
        itemModal.setId("" + itemName);
        listItems.add(itemModal);
        adapter = new AdapterForNewItem(CreateInvoiceActivity.this, listItems, this);
        listForNewItem.setAdapter(adapter);
        getInvoiceNumber();
        settingFocus();
    }

    private void settingFocus() {
        ((EditText) findViewById(R.id.etShippingCost)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etShippingCost.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etShippingCost.getText().toString());
                        etShippingCost.setText(getRound(amt));
                        //  etShippingCost.setText(""+amt);
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
                        // etDepostAdvancePayment.setText(getRound(amt));
                        etDepostAdvancePayment.setText("" + amt);
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });

    }

   /* private String getRound(float totalAmount) {
        DecimalFormat df = new DecimalFormat("##.0");
        String dx = df.format(totalAmount);
        return dx;
    }*/


    private void getInvoiceNumber() {
        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().getInvoiceNumberr(getUserId(), "", new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Rest---Client", jsonElement.toString() + " \n " + response.getUrl());
                        //{"ResponseCode":1,"ResponseMessage":"Invoice successfully created.","UserId":1}
                        try {
                            JSONObject jsonObject = new JSONObject(jsonElement.toString());
                            Log.e("jsonObject:", "" + jsonObject);
                            String UserId = jsonObject.getString("UserId");
                            invoiceNumber = Integer.parseInt(UserId);
                            int sLength = UserId.length();
                            UserId = String.format("%05d", invoiceNumber);
                            Log.e("UserId:", "" + UserId);
                            tvInvoiceNumber.setText("" + UserId);
                        } catch (JSONException e) {
                            e.printStackTrace();
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 011) {
            ItemModal itemModal = (ItemModal) data.getSerializableExtra("data");
            String position = data.getStringExtra("position");
            int pos = Integer.parseInt(position);
            listItems.set(pos, itemModal);
            adapter.updateValues(pos, itemModal);
            //adapter.notifyDataSetChanged();
            updateWithCalculations();

        }
    }

    private void updateWithCalculations() {
     /*   subTotal = 0;
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).getSubTotal() != null && !listItems.get(i).getSubTotal().equals("")) {
                dataExist = true;
                subTotal += Float.parseFloat(listItems.get(i).getSubTotal());
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
                CommonDialog.With(CreateInvoiceActivity.this).Show("Deposit must be less than total.");
            }
        } else {
            tvBalanceDue.setText("" + subTotal);
        }
*/
        this.subTotal = 0.0f;
        int i = 0;
        while (i < this.listItems.size()) {
            if (!(((ItemModal) this.listItems.get(i)).getSubTotal() == null || ((ItemModal) this.listItems.get(i)).getSubTotal().equals(""))) {
                this.dataExist = true;
                this.subTotal = Float.parseFloat(((ItemModal) this.listItems.get(i)).getSubTotal()) + this.subTotal;
                this.tvTotal.setText("" + this.subTotal);
            }
            i++;
        }
        if (this.etShippingCost.getText().toString().length() != 0) {
            this.subTotal = Float.parseFloat(this.tvTotal.getText().toString()) + Float.parseFloat(this.etShippingCost.getText().toString());
            this.tvTotal.setText("" + this.subTotal);
            this.tvBalanceDue.setText("" + this.subTotal);
        }
        if (tvTotal.getText().toString() == null && tvTotal.getText().toString().isEmpty()) {
            totalAmount = 0.0f;
        } else {
            String amt = tvTotal.getText().toString().replace(",", "");
            if (amt.length() == 0) {
                totalAmount = 0.0f;
            } else {
                totalAmount = Float.parseFloat(amt);
            }

        }

        //totalAmount = Float.parseFloat(tvTotal.getText().toString());
        if (etDepostAdvancePayment.getText().toString().length() == 0) {
            tvBalanceDue.setText("" + subTotal);
        } else if (totalAmount > Float.parseFloat(this.etDepostAdvancePayment.getText().toString())) {
            this.tvBalanceDue.setText(CommonNumberFormatter.getRound(Float.parseFloat(tvTotal.getText().toString()) - Float.parseFloat(this.etDepostAdvancePayment.getText().toString())));
        } else {
            this.etDepostAdvancePayment.removeTextChangedListener((TextWatcher) this);
            this.etDepostAdvancePayment.setText("");
            this.tvBalanceDue.setText("");
            this.etDepostAdvancePayment.addTextChangedListener((TextWatcher) this);
            CommonDialog.With(this).Show("Deposit must be less than total.");
        }

    }

    @VisibleForTesting
    void clickDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(CreateInvoiceActivity.this)
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

            // Show selected date
           /* dobEditText.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));*/
            inVoiceDate = formatDate(year, month, day);
            android.util.Log.e("mDOB", "=" + inVoiceDate);
            tvInvoiceDate.setText(inVoiceDate);
            if (tvInvoiceNumber.getText().toString().length() > 0) {
                etPaymentTerms.setEnabled(true);
            } else {
                etPaymentTerms.setEnabled(false);
            }

            if (etPaymentTerms.getText().length() != 0) {
                getData();
            }

            // start = new Date(year, monthOfYear, dayOfMonth);
            // dobEditText.setText(formatDate(year,monthOfYear + 1,dayOfMonth));
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAddNewItem:
                itemName++;
                if (itemName <= 99) {
                    ItemModal itemModal1 = new ItemModal();
                    itemModal1.setItem("" + (String.format("%02d", itemName)));
                    itemModal1.setId("" + itemName);
                    listItems.add(itemModal1);
                    adapter.notifyDataSetChanged();
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
                buttonType = "Save";
                if (dataExist) {
                    if (!inVoiceDate.equals("") && !dueDate.equals("") /*&& etDepostAdvancePayment.getText().toString().length() != 0*/) {
                        //  if (etTaxAmount.getText().toString().length() != 0 || etTaxPercentage.getText().toString().length() != 0) {
                        Log.e("HERE", "HERE");
                        createInvoice();
                    /*} else {
                        CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.empty_fields));
                    }*/

                    } else {
                        CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                    }
                } else {
                    CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.add_item_detail));
                }
                break;
            case R.id.btn_Cancel:
                onBackPressed();
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
                        CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                    }
                } else {
                    CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.add_item_detail));
                }
                break;
            case R.id.tvTaxTypeSpinner:
                spinnerTaxType.performClick();
                break;
        }

    }

    private void createInvoice() {

        if (buttonType.equals("Send")) {
            if (CommonUtility.InvoiceTitle.equals("1")) {
                responseMessage = "Invoice Sent Successfully";
            } else {
                responseMessage = "Quote Sent Successfully";
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
        String[] itemArray = new String[listItems.size()];
        String[] itemArrayID = new String[listItems.size()];
        String[] descriptionArray = new String[listItems.size()];
        String[] rateArray = new String[listItems.size()];
        String[] quantityHours = new String[listItems.size()];
        String[] amountArray = new String[listItems.size()];
        String[] discountArray = new String[listItems.size()];
        String[] productTypeArray = new String[listItems.size()];
        String[] productTypeIdArray = new String[listItems.size()];
        String[] cutomerService = new String[listItems.size()];
        String[] cutomerServiceID = new String[listItems.size()];
        String[] gstArray = new String[listItems.size()];
        String[] qstArray = new String[listItems.size()];
        String[] hstArray = new String[listItems.size()];
        String[] pstArray = new String[listItems.size()];
        String[] taxArray = new String[listItems.size()];
        String[] subTotalArray = new String[listItems.size()];
        Log.e("Size:", "" + listItems.size());

        for (int i = 0; i < listItems.size(); i++) {

            if (listItems.get(i).getDescription() != null && !listItems.get(i).getDescription().equals("")) {
                descriptionArray[i] = listItems.get(i).getDescription();
            } else {
                //descriptionArray[i] = "";
                Log.e("descriptionArray:", "descriptionArray");
                CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                return;
            }

            if (listItems.get(i).getQuantityHours() != null && !listItems.get(i).getQuantityHours().equals("")) {
                quantityHours[i] = listItems.get(i).getQuantityHours().toString();
                // quantityHours[i] = Integer.parseInt(number[0]);

            } else {
                Log.e("quantityHours:", "quantityHours");
                CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                return;
            }

            if (listItems.get(i).getRate() != null && !listItems.get(i).getRate().equals("")) {
                rateArray[i] = listItems.get(i).getRate().toString();
                // rateArray[i] = Integer.parseInt(number[0]);
            } else {
                Log.e("rateArray:", "rateArray");
                CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                return;
            }

            // service product id
            if (listItems.get(i).getServiceProductType() != null && !listItems.get(i).getServiceProductType().equals("")) {
                productTypeArray[i] = listItems.get(i).getServiceProductType();
            } else {

                /*CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                return;*/
                productTypeArray[i] = "null";
            }
            if (listItems.get(i).getServiceProductId() != null && !listItems.get(i).getServiceProductId().equals("")) {
                productTypeIdArray[i] = listItems.get(i).getServiceProductId();
            } else {
                //CommonDialog.With(CreateInvoiceActivity.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory));
                //return;
                productTypeIdArray[i] = "0";
            }
            itemArray[i] = listItems.get(i).getItem();
            itemArrayID[i] = listItems.get(i).getId();
            // descriptionArray[i] = listItems.get(i).getDescription();
            //quantityHours[i] = "" + listItems.get(i).getQuantityHours();
            // rateArray[i] = "" + listItems.get(i).getRate();
            amountArray[i] = "" + listItems.get(i).getTotalAmount();
            if (listItems.get(i).getDiscount() != null && !listItems.get(i).getDiscount().equals("")) {
                discountArray[i] = "A_" + listItems.get(i).getDiscount();
            } else if (listItems.get(i).getDiscountPercentage() != null && !listItems.get(i).getDiscountPercentage().equals("")) {
                discountArray[i] = "P_" + listItems.get(i).getDiscountPercentage();
            } else {
                discountArray[i] = "null";
            }
            subTotalArray[i] = listItems.get(i).getSubTotal();
            // Tax and Percentage

            if (listItems.get(i).getTax() != null && !listItems.get(i).getTax().equals("")) {
                taxArray[i] = "A_" + listItems.get(i).getTax();
            } else if (listItems.get(i).getTaxPercentage() != null && !listItems.get(i).getTaxPercentage().equals("")) {
                taxArray[i] = "P_" + listItems.get(i).getTaxPercentage();
            } else {
                taxArray[i] = "null";
            }

            // GST
            if (listItems.get(i).getGST() != null && !listItems.get(i).getGST().equals("")) {
                gstArray[i] = "A_" + listItems.get(i).getGST();
            } else if (listItems.get(i).getGSTPercentage() != null && !listItems.get(i).getGSTPercentage().equals("")) {
                gstArray[i] = "P_" + listItems.get(i).getGSTPercentage();
            } else {
                gstArray[i] = "null";
            }
            // QST
            if (listItems.get(i).getQST() != null && !listItems.get(i).getQST().equals("")) {
                qstArray[i] = "A_" + listItems.get(i).getQST();
            } else if (listItems.get(i).getQSTPercentage() != null && !listItems.get(i).getQSTPercentage().equals("")) {
                qstArray[i] = "P_" + listItems.get(i).getQSTPercentage();
            } else {
                qstArray[i] = "null";
            }

            // HST
            if (listItems.get(i).getHST() != null && !listItems.get(i).getHST().equals("")) {
                hstArray[i] = "A_" + listItems.get(i).getHST();
            } else if (listItems.get(i).getHSTPercentage() != null && !listItems.get(i).getHSTPercentage().equals("")) {
                hstArray[i] = "P_" + listItems.get(i).getHSTPercentage();
            } else {
                hstArray[i] = "null";
            }
            // PST
            if (listItems.get(i).getPST() != null && !listItems.get(i).getPST().equals("")) {
                pstArray[i] = "A_" + listItems.get(i).getPST();
            } else if (listItems.get(i).getPSTPercentage() != null && !listItems.get(i).getPSTPercentage().equals("")) {
                pstArray[i] = "P_" + listItems.get(i).getPSTPercentage();
            } else {
                pstArray[i] = "null";
            }

            cutomerService[i] = "null";
            cutomerServiceID[i] = "0";
        }


        HashMap templatePosts = new HashMap();
        templatePosts.put("InvoiceNumber", "" + invoiceNumber);
        templatePosts.put("InvoiceDate", inVoiceDate);
        templatePosts.put("PaymentTerms", etPaymentTerms.getText().toString());
        templatePosts.put("DueDate", dueDate);
        templatePosts.put("DocumentRef", etDocumentRefrence.getText().toString());
        templatePosts.put("SalesPerson", etSalesPerson.getText().toString());
        templatePosts.put("ShippingCost", etShippingCost.getText().toString());
        templatePosts.put("SubTotal", subTotalArray);
        templatePosts.put("Total", tvTotal.getText().toString());
        templatePosts.put("DepositePayment", etDepostAdvancePayment.getText().toString());
        templatePosts.put("BalanceDue", tvBalanceDue.getText().toString());
        templatePosts.put("Note", etNotes.getText().toString());
        templatePosts.put("Terms", etTerms.getText().toString());
        templatePosts.put("UserId", "" + getUserId());
        templatePosts.put("CustomerId", CustomerId);
        templatePosts.put("ItemId", itemArrayID);
        templatePosts.put("Item", itemArray);
        templatePosts.put("Description", descriptionArray);
        templatePosts.put("ServiceType", productTypeArray);
        templatePosts.put("ServiceTypeId", productTypeIdArray);
        templatePosts.put("Quantity", quantityHours);
        templatePosts.put("GST_Tax", gstArray);
        templatePosts.put("QST_Tax", qstArray);
        templatePosts.put("HST_Tax", hstArray);
        templatePosts.put("PST_Tax", pstArray);
        templatePosts.put("Rate", rateArray);
        templatePosts.put("Amount", amountArray);
        templatePosts.put("Discount", discountArray);
        templatePosts.put("Type", CommonUtility.INVOICE_TYPE);
        templatePosts.put("ButtonType", buttonType);
        templatePosts.put("Tax", taxArray);
        templatePosts.put("Customer_Service", cutomerService);
        templatePosts.put("Customer_ServiceTypeId", cutomerServiceID);

        LoadingBox.showLoadingDialog(CreateInvoiceActivity.this, "");
        RestClient.getApiFinanceServiceForPojo().createInvoice(getTypedInput(new Gson().toJson(templatePosts)), getCallback(new Callback<JsonElement>() {

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
                        CommonDialog.showDialog2Button(CreateInvoiceActivity.this, responseMessage, new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                CommonUtility.HomeClick(CreateInvoiceActivity.this);
                            }
                        });
                    } else {
                        CommonDialog.With(CreateInvoiceActivity.this).Show(ResponseMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(CreateInvoiceActivity.this, error);
            }
        }));


    }

    @Override
    public void updateValues(int x) {
        itemName = x;
        updateWithCalculations();
    }

    private void getCustomerDetail() {
        int mappingid = Integer.parseInt(CustomerId);

        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().getCustomerDetail(mappingid, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                        Gson gson = new Gson();
                        editCustomer = gson.fromJson(jsonElement.toString(), new TypeToken<EditCustomer>() {
                        }.getType());
                        if (editCustomer.getResponseCode() == 1) {
                            if (editCustomer.getIsFinanceUser() != null) {
                                if (editCustomer.getIsFinanceUser()) {
                                    Log.e("If---------Finance", "" + editCustomer.getIsFinanceUser());
                                    CommonUtility.IsFinance = "true";
                                } else {
                                    Log.e("Else-------------Finance", "" + editCustomer.getIsFinanceUser());
                                    CommonUtility.IsFinance = "false";
                                }

                            } else {
                                CommonUtility.IsFinance = "false";
                                Log.e("No--------Finance", "No Finance");
                            }
                            //setData();
                        } else {
                            CommonDialog.With(CreateInvoiceActivity.this).Show(editCustomer.getResponseMessage());
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
