package com.kippinretail.KippinInvoice;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kippinretail.ActivityTermsAndConditions;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.ApplicationuUlity.Validation;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.UploadImageDialog;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.KippinInvoice.InvoiceAdapter.GoodsAdapter;
import com.kippinretail.KippinInvoice.ModalInvoice.ModalForIndutry;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippin.app.Kippin;
import com.kippin.performbook.adapter.CurrencyAdapter;
import com.kippin.selectdate.ModelProvinceArrayData;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.model.ModelCurrency;
import com.kippin.webclient.model.ModelCurrencys;
import com.kippin.webclient.model.ProvinceModel;
import com.kippin.webclient.model.TemplateData;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 10/26/2016.
 */

public class InvoiceViaFinanceRegistration extends SuperActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public ArrayList<ModelCurrency> arrayListCurrencies;
    ImageView ivLogo;
    EditText etUsername, etCompanyname, etContactPerson, etMobileNumber, etTypesofGoods, etBusinessNumber, etEmail, etWebsite;
    EditText etPassword, etComfirmPassword;
    Button btn_Submit;
    ImageView ivArrowCorporationAddress, ivArrowShippingAddress;
    RelativeLayout rlAddButon;
    ArrayList<String> listForEmails = new ArrayList();
    boolean dialogType;
    RelativeLayout rlCorporationAddress, rlShippingAddress;
    TextView tvForCorporationAddress, tvForShippingAddress;
    // Adapter
    CurrencyAdapter currencyAdapter;
    // Spinner
    Spinner spinnerCurrency, spinner_TypesofGoods;
    ModelCurrency modelCurrency;
    PhoneWatcher phoneWatcher = null;
    CheckBox checkBox;
    ScrollView topLayout;
    ArrayList<ProvinceModel> arrayListSpinnerProvince;
    ProvinceModel provinceModel;
    private List<ModalForIndutry> modalForIndustry;
    // Strings
    private String CorporateAptNo;
    private String CorporateHouseNo;
    private String CorporateStreet;
    private String CorporateCity;
    private String CorporateState;
    private String CorporatePostalCode;
    private String ShippingAptNo;
    private String ShippingHouseNo;
    private String ShippingStreet;
    private String ShippingCity;
    private String ShippingState;
    private String ShippingPostalCode;
    private String encodedString;
    private String emailCC = "";
    private TextView textViewCurrency, textview_term_condition, tvTypesofGoods;
    private String mCurrency;
    private String registrationType = "0";
    private TextView currency;
    private GoodsAdapter goodsAdapter;
    private String mTypesAndGoods;
    private String myProvice;
    private int indexValue;
    private int firstTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_voice_registration);
        Initlization();
    }

    private void Initlization() {
        String s = getString(R.string.invoice_registartion);
        generateRightText1(s);

        //EditText
        etUsername = (EditText) findViewById(R.id.etUsername);
        etUsername.setEnabled(false);
        etCompanyname = (EditText) findViewById(R.id.etCompanyname);
        etContactPerson = (EditText) findViewById(R.id.etContactPerson);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        // etTypesofGoods = (EditText) findViewById(R.id.etTypesofGoods);
        etBusinessNumber = (EditText) findViewById(R.id.etBusinessNumber);
        etWebsite = (EditText) findViewById(R.id.etWebsite);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etEmail.setEnabled(false);

        /**** Password and Confirm Passwords not require if user have in Finance section. ***/
        etPassword = (EditText) findViewById(R.id.etPassword);
        etComfirmPassword = (EditText) findViewById(R.id.etComfirmPassword);

        etPassword.setVisibility(View.GONE);
        etComfirmPassword.setVisibility(View.GONE);

        /**** Password and Confirm Passwords not require if user have in Finance section. ***/

        textview_term_condition = (TextView) findViewById(R.id.textview_term_condition);
        textview_term_condition.setOnClickListener(this);
        // Spinner
        spinnerCurrency = (Spinner) findViewById(R.id.spinner_currency);
        spinner_TypesofGoods = (Spinner) findViewById(R.id.spinner_TypesofGoods);

        //Button
        btn_Submit = (Button) findViewById(R.id.btn_Submit);
        btn_Submit.setOnClickListener(this);

        // ImageView
        ivArrowCorporationAddress = (ImageView) findViewById(R.id.ivArrowCorporationAddress);
        ivArrowShippingAddress = (ImageView) findViewById(R.id.ivArrowShippingAddress);

        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivLogo.setOnClickListener(this);

        // CheckBox
        checkBox = (CheckBox) findViewById(R.id.checkbox);

        //TextViewsz
        tvForCorporationAddress = (TextView) findViewById(R.id.etCorprationAddress);
        tvForShippingAddress = (TextView) findViewById(R.id.etShippingAddress);
        tvForCorporationAddress.setMovementMethod(new ScrollingMovementMethod());
        tvForShippingAddress.setMovementMethod(new ScrollingMovementMethod());


        topLayout = (ScrollView) findViewById(R.id.topLayout);
        topLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                tvForCorporationAddress.getParent().requestDisallowInterceptTouchEvent(false);
                tvForShippingAddress.getParent().requestDisallowInterceptTouchEvent(false);

                return false;
            }
        });

        tvForCorporationAddress.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                tvForCorporationAddress.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        tvForShippingAddress.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                tvForShippingAddress.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });


        textViewCurrency = (TextView) findViewById(R.id.currency);
        textViewCurrency.setOnClickListener(this);

        tvTypesofGoods = (TextView) findViewById(R.id.tvTypesofGoods);
        tvTypesofGoods.setOnClickListener(this);

        currency = (TextView) findViewById(R.id.currency);
        // Relativelayout
        rlAddButon = (RelativeLayout) findViewById(R.id.rlAddButon);
        rlAddButon.setOnClickListener(this);

        rlCorporationAddress = (RelativeLayout) findViewById(R.id.rlCorporationAddress);
        rlShippingAddress = (RelativeLayout) findViewById(R.id.rlShippingAddress);
        rlCorporationAddress.setOnClickListener(this);
        rlShippingAddress.setOnClickListener(this);

        phoneWatcher = new PhoneWatcher(etMobileNumber);
        etMobileNumber.addTextChangedListener(phoneWatcher);

        //Province Spinner
        arrayListSpinnerProvince = new ArrayList<ProvinceModel>();
        callProvinceApi(arrayListSpinnerProvince);

        checkCurrencyData();
        setData();

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (textViewCurrency.getText().toString().equalsIgnoreCase(getResources().getString(R.string.trading_currency))) {
                        textViewCurrency.setTextColor(Color.parseColor("#A1A1A1"));
                    } else {
                        textViewCurrency.setTextColor(Color.BLACK);
                    }

                } else {
                    textViewCurrency.setTextColor(Color.BLACK);
                    textViewCurrency.setText(arrayListCurrencies.get(position).getCurrencyType());
                    //mCurrency = arrayListCurrencies.get(position).getId();
                    mCurrency=arrayListCurrencies.get(position).getCurrencyType();
                    //Log.e("CURREEEEEE","===" +  getIndex(spinnerCurrency, Singleton.getUser().getCurrencyId()));

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_TypesofGoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (tvTypesofGoods.getText().toString().equalsIgnoreCase(getResources().getString(R.string.typesoffood))) {
                        tvTypesofGoods.setTextColor(Color.parseColor("#A1A1A1"));
                    } else {
                        tvTypesofGoods.setTextColor(Color.BLACK);
                    }
                } else {
                    tvTypesofGoods.setTextColor(Color.BLACK);
                    tvTypesofGoods.setText(modalForIndustry.get(position).getIndustryName());
                    mTypesAndGoods = "" + modalForIndustry.get(position).getIndustryId();

                }

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setData() {

        if (Singleton.getUser() != null) {

            if (Singleton.getUser().getUsername() != null) {
                etUsername.setText(Singleton.getUser().getUsername());
            }

            if (Singleton.getUser().getCompanyName() != null) {
                etCompanyname.setText(Singleton.getUser().getCompanyName());
                etCompanyname.setEnabled(false);
            }
            if (Singleton.getUser().getFirstName() != null) {
                etContactPerson.setText(Singleton.getUser().getFirstName() + " " + Singleton.getUser().getLastName());
                etContactPerson.setEnabled(false);
            }
            if (Singleton.getUser().getMobileNumber() != null) {
                etMobileNumber.setText(Singleton.getUser().getMobileNumber());
                etMobileNumber.setEnabled(false);
            }
            if (Singleton.getUser().getGoodsType() != null) {
                etTypesofGoods.setText(Singleton.getUser().getGoodsType());
                etTypesofGoods.setEnabled(false);
            }
            if (Singleton.getUser().getBusinessNumber() != null) {
                etBusinessNumber.setText(Singleton.getUser().getBusinessNumber());
                etBusinessNumber.setEnabled(false);
            }

            if (Singleton.getUser().getEmail() != null) {
                etEmail.setText(Singleton.getUser().getEmail());
                etEmail.setEnabled(false);
            }
            //etWebsite.setText(Singleton.getUser().getWebsite());

            if (Singleton.getUser().getCompanyLogo() != null) {
                Picasso.with(InvoiceViaFinanceRegistration.this).load(Singleton.getUser().getCompanyLogo()).placeholder(R.drawable.icon_placeholder).into(ivLogo);
            }


        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Submit:
                if (etUsername.getText().toString().length() != 0 && etCompanyname.getText().toString().length() != 0 && etBusinessNumber.getText().toString().length() != 0
                        && etEmail.getText().toString().length() != 0) {
                    if (CorporateStreet != null && CorporateCity != null && CorporateState != null && CorporatePostalCode != null) {
                        if (ShippingStreet != null && ShippingCity != null && ShippingState != null && ShippingPostalCode != null) {
                            if (mCurrency != null) {
                                if (checkBox.isChecked()) {
                                    callInvoiceRegistration();
                                } else {
                                    CommonDialog.With(InvoiceViaFinanceRegistration.this).Show(getResources().getString(R.string.terms_and_condition));
                                }
                            } else {
                                CommonDialog.With(InvoiceViaFinanceRegistration.this).Show("Please select country");
                            }
                        } else {
                            CommonDialog.With(InvoiceViaFinanceRegistration.this).Show("Please fill Corporate address all fields");
                        }
                    } else {
                        CommonDialog.With(InvoiceViaFinanceRegistration.this).Show("Please fill Corporate address all fields");
                    }
                } else {
                    CommonDialog.With(InvoiceViaFinanceRegistration.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory_));
                }
                break;
            case R.id.rlCorporationAddress:
                dialogType = true;
                dialogForAddress();
                break;
            case R.id.rlShippingAddress:
                dialogType = false;
                dialogForAddress();
                break;
            case R.id.rlAddButon:
                dialogToAddEmail();
                break;
            case R.id.ivLogo:
                uploadImage();
                break;
            case R.id.currency:
                spinnerCurrency.performClick();
                break;
            case R.id.tvTypesofGoods:
                spinner_TypesofGoods.performClick();
                break;
            case R.id.textview_term_condition:
                Intent in = new Intent();
                in.setClass(InvoiceViaFinanceRegistration.this, ActivityTermsAndConditions.class);
                startActivity(in);
                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }
    }

    private void uploadImage() {
        UploadImageDialog.showUploadImageDialog(InvoiceViaFinanceRegistration.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CommonUtility.REQUEST_CODE_GALLERY:
                try {
                    String imgPath = null;
                    if (data != null) {
                        imgPath = data.getStringExtra("imagePath");
                    }
                    if (imgPath != null) {
                        CommonUtility.VoucherBitMap = BitmapFactory.decodeFile(imgPath);
                        encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        ivLogo.setImageBitmap(CommonUtility.VoucherBitMap);
                    }
                } catch (Exception e) {

                }
                break;
            case CommonUtility.REQUEST_CODE_TAKE_PICTURE:
                try {
                    if (data != null) {
                        CommonUtility.VoucherBitMap = (Bitmap) data.getExtras().get("data");
                        encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        ivLogo.setImageBitmap(CommonUtility.VoucherBitMap);
                    }
                } catch (Exception ex) {

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void dialogToAddEmail() {

        try {

            final Dialog dialog = new Dialog(activity,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_for_email);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            final EditText etEmail = (EditText) dialog.findViewById(R.id.etEmail);
            Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);

            ImageView ivForCross = (ImageView) dialog.findViewById(R.id.ivForCross);
            final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.row_list_item, listForEmails);
            ListView listView = (ListView) dialog.findViewById(R.id.lvForEmails);
            listView.setAdapter(adapter);


            ivForCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Validation.isEmailAddress(etEmail, true)) {
                        if (!listForEmails.contains(etEmail.getText().toString())) {
                            listForEmails.add(etEmail.getText().toString());
                            adapter.notifyDataSetChanged();
                            etEmail.setText("");
                        } else {
                            Toast.makeText(InvoiceViaFinanceRegistration.this, "Email already exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dialogForAddress() {
        try {
            final Dialog dialog = new Dialog(activity,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_for_address);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            final EditText etAptNumber = (EditText) dialog.findViewById(R.id.etAptNumber);
            final EditText etHouseNumber = (EditText) dialog.findViewById(R.id.etHouseNumber);
            final EditText etStreet = (EditText) dialog.findViewById(R.id.etStreet);
            final EditText etCity = (EditText) dialog.findViewById(R.id.etCity);
            final EditText etState = (EditText) dialog.findViewById(R.id.etState);
            final EditText etPostalCode = (EditText) dialog.findViewById(R.id.etPostalCode);

            Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
            ImageView ivForCross = (ImageView) dialog.findViewById(R.id.ivForCross);

            if (dialogType) {
                etAptNumber.setText(CorporateAptNo);
                etHouseNumber.setText(CorporateHouseNo);
                etStreet.setText(CorporateStreet);
                etCity.setText(CorporateCity);
                etState.setText(CorporateState);
                etPostalCode.setText(CorporatePostalCode);
            } else {
                etAptNumber.setText(ShippingAptNo);
                etHouseNumber.setText(ShippingHouseNo);
                etStreet.setText(ShippingStreet);
                etCity.setText(ShippingCity);
                etState.setText(ShippingState);
                etPostalCode.setText(ShippingPostalCode);
            }

            ivForCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (dialogType) {
                        CorporateAptNo = etAptNumber.getText().toString();
                        CorporateHouseNo = etHouseNumber.getText().toString();
                        CorporateStreet = etStreet.getText().toString();
                        CorporateCity = etCity.getText().toString();
                        CorporateState = etState.getText().toString();
                        CorporatePostalCode = etPostalCode.getText().toString();

                        if (CorporateStreet.toString().length() != 0 && CorporateCity.toString().length() != 0
                                && CorporateState.toString().length() != 0 && CorporatePostalCode.toString().length() != 0) {
                            if (CorporatePostalCode.toString().length() == 8) {
                                tvForCorporationAddress.setVisibility(View.VISIBLE);
                                tvForShippingAddress.setVisibility(View.VISIBLE);
                                String add = CorporateAptNo + "-" + CorporateHouseNo;

                                if (CorporateAptNo.toString().length() == 0)
                                    add = add.substring(1);
                                tvForCorporationAddress.setText("Corporation Address:" + "\n" + add + "\n" + CorporateStreet + "," + CorporateCity
                                        + "\n" + CorporateState + "," + CorporatePostalCode);
                                if (firstTime == 0) {
                                    firstTime = 1;
                                    ShippingAptNo = etAptNumber.getText().toString();
                                    ShippingHouseNo = etHouseNumber.getText().toString();
                                    ShippingStreet = etStreet.getText().toString();
                                    ShippingCity = etCity.getText().toString();
                                    ShippingState = etState.getText().toString();
                                    ShippingPostalCode = etPostalCode.getText().toString();
                                    tvForShippingAddress.setText("Shipping Address:" + "\n" + add + "\n" + ShippingStreet + "," + ShippingCity
                                            + "\n" + ShippingState + "," + ShippingPostalCode);
                                }


                                dialog.dismiss();
                            } else {
                                CommonDialog.With(InvoiceViaFinanceRegistration.this).Show(getResources().getString(R.string.postal_code_must_be));
                            }
                        } else {
                            CommonDialog.With(InvoiceViaFinanceRegistration.this).Show(getResources().getString(R.string.empty_fields));
                        }
                    } else {
                        ShippingAptNo = etAptNumber.getText().toString();
                        ShippingHouseNo = etHouseNumber.getText().toString();
                        ShippingStreet = etStreet.getText().toString();
                        ShippingCity = etCity.getText().toString();
                        ShippingState = etState.getText().toString();
                        ShippingPostalCode = etPostalCode.getText().toString();

                        if (ShippingStreet.toString().length() != 0 && ShippingCity.toString().length() != 0
                                && ShippingState.toString().length() != 0 && ShippingPostalCode.toString().length() != 0) {
                            if (ShippingPostalCode.toString().length() == 8) {
                                tvForShippingAddress.setVisibility(View.VISIBLE);
                                String add = ShippingAptNo + "," + ShippingHouseNo;
                                if (ShippingAptNo.toString().length() == 0)
                                    add = add.substring(1);
                                tvForShippingAddress.setText("Shipping Address:" + "\n" + add + "\n" + ShippingStreet + "," + ShippingState
                                        + "\n" + ShippingCity + "," + ShippingPostalCode);
                                dialog.dismiss();
                            } else {
                                CommonDialog.With(InvoiceViaFinanceRegistration.this).Show(getResources().getString(R.string.postal_code_must_be));
                            }
                        } else {
                            CommonDialog.With(InvoiceViaFinanceRegistration.this).Show(getResources().getString(R.string.empty_fields));
                        }
                    }
                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void checkCurrencyData() {
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.wsInterface = new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {
                arrayListCurrencies = ((ModelCurrencys) data.getData(ModelCurrencys.class)).modelCurrencies;
                modelCurrency = new ModelCurrency();
                // modelCurrency.setId("0");
                //modelCurrency.setCurrencyType(" Trading Currency ");
                // arrayListCurrencies.add(0, modelCurrency);
                currencyAdapter = new CurrencyAdapter(InvoiceViaFinanceRegistration.this, com.kippin.kippin.R.layout.layout_spinner_textview, arrayListCurrencies);
                spinnerCurrency.setAdapter(currencyAdapter);
                for (int position = 0; position < arrayListCurrencies.size(); position++) {
                    if (arrayListCurrencies.get(position).getId().equals(Singleton.getUser().getCurrencyId())) {
                        String name = arrayListCurrencies.get(position).getCurrencyType();
                        mCurrency = arrayListCurrencies.get(position).getId();
                        mCurrency=arrayListCurrencies.get(position).getCurrencyType();
                        textViewCurrency.setTextColor(Color.BLACK);
                        textViewCurrency.setText(name);
                        break;
                    }
                }

                checkTypesAndGoods();
            }
        };
        wsTemplate.url = Url.URL_CURRENCY;
        wsTemplate.aClass = ModelCurrencys.class;
        wsTemplate.methods = WSMethods.GET;
        wsTemplate.context = this;
        new WSHandler(wsTemplate).execute();

    }

 /*   private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getAdapter().getItemId(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }*/


    private void checkTypesAndGoods() {
        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().getIndustryList(new Callback<JsonElement>() {
                                                                     @Override
                                                                     public void success(JsonElement jsonElement, Response response) {
                                                                         LoadingBox.dismissLoadingDialog();
                                                                         Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                                                                         Gson gson = new Gson();
                                                                         modalForIndustry = gson.fromJson(jsonElement.toString(), new TypeToken<List<ModalForIndutry>>() {
                                                                         }.getType());
                                                                        /* ModalForIndutry modalForIndustry1 = new ModalForIndutry();
                                                                         modalForIndustry1.setIndustryName("Types of goods/service offered");
                                                                         modalForIndustry1.setIndustryId("0");
                                                                         modalForIndustry1.setUserId(0);
                                                                         modalForIndustry.add(0, modalForIndustry1);*/
                                                                         goodsAdapter = new GoodsAdapter(InvoiceViaFinanceRegistration.this, modalForIndustry, 0);
                                                                         spinner_TypesofGoods.setAdapter(goodsAdapter);


                                                                         for (int position = 0; position < modalForIndustry.size(); position++) {

                                                                             if (modalForIndustry.get(position).getIndustryId().equals(Singleton.getUser().getIndustryId())) {
                                                                                 /*indexValue = Integer.parseInt(modalForIndustry.get(position).getIndustryId());*/
                                                                                 String name = modalForIndustry.get(position).getIndustryName();
                                                                                 mTypesAndGoods = modalForIndustry.get(position).getIndustryId();
                                                                                 tvTypesofGoods.setTextColor(Color.BLACK);
                                                                                 tvTypesofGoods.setText(name);
                                                                                 /*indexValue = position;
                                                                                 spinner_TypesofGoods.setSelection(indexValue);*/
                                                                                 break;
                                                                             }

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

    private void callInvoiceRegistration() {
        for (int i = 0; i < listForEmails.size(); i++) {
            if (emailCC.equals("")) {
                emailCC = listForEmails.get(i);
            } else {
                emailCC = emailCC + "," + listForEmails.get(i);
            }
        }
        if (ShippingAptNo.equals("")) {
            ShippingAptNo = null;
        }
        if (ShippingHouseNo.equals("")) {
            ShippingHouseNo = null;
        }
        LoadingBox.showLoadingDialog(InvoiceViaFinanceRegistration.this, "");
        RestClient.getApiFinanceServiceForPojo().InvoiceRegistrationViaFinance(etUsername.getText().toString(), encodedString, etCompanyname.getText().toString(), etContactPerson.getText().toString(),
                etMobileNumber.getText().toString(), CorporateAptNo, CorporateHouseNo, CorporateStreet, CorporateCity, CorporateState, CorporatePostalCode,
                ShippingAptNo, ShippingHouseNo, ShippingStreet, ShippingCity, ShippingState, ShippingPostalCode, mTypesAndGoods,
                etBusinessNumber.getText().toString(), mCurrency, etEmail.getText().toString(), emailCC, etWebsite.getText().toString(), registrationType,
                Singleton.getUser().getUsername(), Singleton.getUser().getPassword(), getCallback(new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Tag", "Request data " + new Gson().toJson(jsonElement));
                        // {"ResponseCode":1,"ResponseMessage":"Please check your mail for email confirmation.","UserId":3}
                        try {
                            JSONObject jsonObject = new JSONObject(jsonElement.toString());
                            String ResponseMessage = jsonObject.getString("ResponseMessage");
                            String ResponseCode = jsonObject.getString("ResponseCode");

                            if (ResponseCode.equals("1")) {
                                CommonDialog.showDialog2Button(InvoiceViaFinanceRegistration.this, ResponseMessage, new OnDialogDismissListener() {
                                    @Override
                                    public void onDialogDismiss() {
                                        Singleton.getUser().setIsOnlyInvoice(true);
                                        Singleton.getUser().set_Is_OnlyInvoice(true);
                                        Intent in = new Intent();
                                        in.setClass(InvoiceViaFinanceRegistration.this, Kippin.invoiceDashBoardClass);
                                        in.putExtra("RegistrationType", "0");
                                        in.putExtra("InvoiceUserType", "true");
                                        startActivity(in);
                                        overridePendingTransition(com.kippin.kippin.R.anim.push_in_to_left,
                                                com.kippin.kippin.R.anim.push_in_to_right);
                                    }
                                });
                            }
                            else if (ResponseCode.equals("2")){
                                Singleton.getUser().setIsOnlyInvoice(true);
                                Singleton.getUser().set_Is_OnlyInvoice(true);
                                Intent in = new Intent();
                                in.setClass(InvoiceViaFinanceRegistration.this, Kippin.invoiceDashBoardClass);
                                in.putExtra("RegistrationType", "0");
                                in.putExtra("InvoiceUserType", "true");
                                startActivity(in);
                                overridePendingTransition(com.kippin.kippin.R.anim.push_in_to_left,
                                        com.kippin.kippin.R.anim.push_in_to_right);
                            } else {
                                CommonDialog.With(InvoiceViaFinanceRegistration.this).Show(ResponseMessage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("Failure ", " = " + error.getMessage());
                        LoadingBox.dismissLoadingDialog();
                        ErrorCodes.checkCode(InvoiceViaFinanceRegistration.this, error);
                    }
                }));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            textViewCurrency.setTextColor(Color.parseColor("#A1A1A1"));
        } else
            textViewCurrency.setTextColor(Color.BLACK);
        textViewCurrency.setText(arrayListCurrencies.get(position).getCurrencyType());
        mCurrency = arrayListCurrencies.get(position).getId();
        mCurrency=arrayListCurrencies.get(position).getCurrencyType();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Method use for call Province api
     *
     * @param arrayList
     */
    private void callProvinceApi(final ArrayList<ProvinceModel> arrayList) {
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.aClass = ModelProvinceArrayData.class;
        wsTemplate.context = InvoiceViaFinanceRegistration.this;
        wsTemplate.message_id = com.kippin.kippin.R.string.app_name;
        wsTemplate.methods = WSMethods.GET;
        wsTemplate.requestCode = 1001;
        wsTemplate.url = Url.URL_PROVINCE;

        wsTemplate.wsInterface = new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {

                switch (requestCode) {
                    case 1001:
                        ModelProvinceArrayData modelProvinceArrayData = data.getData(ModelProvinceArrayData.class);
                        for (int i = 0; i < modelProvinceArrayData.modelProvinces.size(); i++) {

                            if (modelProvinceArrayData.modelProvinces.get(i).getId().equals(Singleton.getUser().getProvinceId())) {
                                myProvice = modelProvinceArrayData.modelProvinces.get(i).getProvinceName();
                                Log.e("myProvice ", " == " + myProvice);
                                setAddresss(myProvice);
                                break;
                            }

                        }


                        break;
                }

            }
        };
        new WSHandler(wsTemplate).execute();

    }

    private void setAddresss(String myProvice) {

        CorporateAptNo = ShippingAptNo = Singleton.getUser().getCorporateAptNo();
        CorporateHouseNo = ShippingHouseNo = Singleton.getUser().getCorporateHouseNo();
        CorporateStreet = ShippingStreet = Singleton.getUser().getCorporationAddress();
        CorporateCity = ShippingCity = Singleton.getUser().getCity();
        CorporateState = ShippingState = myProvice;
        CorporatePostalCode = ShippingPostalCode = Singleton.getUser().getPostalCode();


        if (Singleton.getUser().getCorporateAptNo() == null) {
            CorporateAptNo = "";
            ShippingAptNo = "";
        }
        if (Singleton.getUser().getCorporateHouseNo() == null) {
            CorporateHouseNo = "";
            ShippingHouseNo = "";
        }


        String add = CorporateAptNo + "-" + CorporateHouseNo;
        if (!CorporateAptNo.equals("")) {
            if (CorporateAptNo.toString().length() == 0)
                add = add.substring(1);
        }
        tvForCorporationAddress.setText("Corporation Address:" + "\n" + add + "\n" + CorporateStreet + "," + CorporateCity
                + "\n" + CorporateState + "," + CorporatePostalCode);


        tvForShippingAddress.setVisibility(View.VISIBLE);
        String add1 = ShippingAptNo + "-" + ShippingHouseNo;
        if (!ShippingAptNo.equals("")) {
            if (ShippingAptNo.toString().length() == 0)
                add1 = add1.substring(1);
        }
        tvForShippingAddress.setText("Shipping Address:" + "\n" + add1 + "\n" + ShippingStreet + "," + ShippingState
                + "\n" + ShippingCity + "," + ShippingPostalCode);


    }

    class PhoneWatcher implements TextWatcher {

        final char concater = '-';

        EditText editText;

        public PhoneWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String data = s.toString();

            int length = data.length();

            if (length == 4) {
                String pre = data.substring(0, 3);

                if (!data.endsWith(concater + "")) {
                    char post = data.charAt(3);
                    setText(pre + concater + post);
                } else {
                    setText(pre);
                }
                editText.setSelection(editText.getText().toString().length());
            } else if (length > 4 && data.charAt(3) != concater) {
                data = data.replace(concater + "", "");
                String pre = data.substring(0, 3);
                String post = data.substring(3, data.length());
                int cursor = 0;
                setText(pre + concater + post);
                editText.setSelection(editText.getText().toString().length());
            }

        }

        private void setText(String text) {
            editText.removeTextChangedListener(this);
            editText.setText(text);
            editText.addTextChangedListener(this);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
