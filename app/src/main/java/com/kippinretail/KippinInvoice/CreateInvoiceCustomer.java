package com.kippinretail.KippinInvoice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.ApplicationuUlity.PhoneWatcher;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.Interface.EmailListInterface;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.KippinInvoice.InvoiceAdapter.GoodsAdapter;
import com.kippinretail.KippinInvoice.ModalInvoice.ModalForIndutry;
import com.kippinretail.Modal.Invoice.ModalDataForAddress;
import com.kippinretail.Modal.webclient.model.ArrayListPost;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;


/**
 * Created by kamaljeet.singh on 11/15/2016.
 */
public class CreateInvoiceCustomer extends SuperActivity implements View.OnClickListener {
    EditText etCompanyname, etContactPerson, etMobileNumber, etWebsite, etEmail, etTelephone;
    RelativeLayout rlAddButon, rlCorporationAddress, rlShippingAddress;
    TextView tvForCorporationAddress, tvForShippingAddress;
    ArrayList<String> listForEmails = new ArrayList<>();
    GoodsAdapter goodsAdapter;
    Spinner spinner_TypesofGoods;
    TextView tvTypesofGoods;
    private Button btn_submit;
    private String emailCC = "";
    private PhoneWatcher phoneWatcher;
    private ModalDataForAddress modalDataForAddress;
    private int flagEmail = 0;
    private String roleId;
    private String responseMessage;
    private String CustomerAddress;
    private RelativeLayout rlTypeOfGoodsAndService;
    private List<ModalForIndutry> modalForIndustry;
    private String mTypesAndGoods;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_invoice_customer);
        Initlization();
    }

    private void Initlization() {

        //EditText
        etCompanyname = (EditText) findViewById(R.id.etCompanyname);
        etContactPerson = (EditText) findViewById(R.id.etContactPerson);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etTelephone = (EditText) findViewById(R.id.etTelephone);
        etWebsite = (EditText) findViewById(R.id.etWebsite);
        etEmail = (EditText) findViewById(R.id.etEmail);

        // Relativelayout
        rlAddButon = (RelativeLayout) findViewById(R.id.rlAddButon);
        rlAddButon.setOnClickListener(this);

        //Button
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        rlTypeOfGoodsAndService = (RelativeLayout) findViewById(R.id.rlTypeOfGoodsAndService);

        rlCorporationAddress = (RelativeLayout) findViewById(R.id.rlCorporationAddress);
        rlCorporationAddress.setOnClickListener(this);

        rlShippingAddress = (RelativeLayout) findViewById(R.id.rlShippingAddress);
        rlShippingAddress.setOnClickListener(this);

        //TextViewsz
        tvForCorporationAddress = (TextView) findViewById(R.id.etCorprationAddress);
        tvForShippingAddress = (TextView) findViewById(R.id.etShippingAddress);
        tvForCorporationAddress.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvForShippingAddress.setMovementMethod(ScrollingMovementMethod.getInstance());

        phoneWatcher = new PhoneWatcher(etTelephone);
        etTelephone.addTextChangedListener(phoneWatcher);
        phoneWatcher = new PhoneWatcher(etMobileNumber);
        etMobileNumber.addTextChangedListener(phoneWatcher);
        modalDataForAddress = new ModalDataForAddress();

        spinner_TypesofGoods = (Spinner) findViewById(R.id.spinner_TypesofGoods);
        tvTypesofGoods = (TextView) findViewById(R.id.tvTypesofGoods);
        tvTypesofGoods.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            roleId = extras.getString("roleId");
            String Supplier12 = extras.getString("Supplier");
            String  onlysupplier=extras.getString("onlysupplier");
            generateRightAddButton("");
            if(roleId!=null){
                if (roleId.equals("2")) {
                    generateRightText1(getString(R.string.create_customer));
                    //  generateRightAddButton("",roleId);
                    etCompanyname.setHint("Company/Customer Name");
                    responseMessage = "Customer created successfully";
                    rlTypeOfGoodsAndService.setVisibility(View.GONE);
                    CustomerAddress = "Customer Address:";
                } else if (roleId.equals("5")) {

                    if (Supplier12.equals("2")) {
                        generateRightText1(getString(R.string.create_customer));
                        etCompanyname.setHint("Company/Customer Name");
                        responseMessage = "Customer created successfully";
                        rlTypeOfGoodsAndService.setVisibility(View.GONE);
                        CustomerAddress = "Customer Address:";
                    } else {
                        //generateRightText1(getString(R.string.create_customer));
                        generateRightText1(getString(R.string.create_supplier));
                        etCompanyname.setHint("Company/Supplier Name");
                        responseMessage = "Supplier created successfully";
                        rlAddButon.setVisibility(View.GONE);
                        rlShippingAddress.setVisibility(View.GONE);
                        CustomerAddress = "Suppllier Address";
                        tvForCorporationAddress.setHint(CustomerAddress);
                        rlTypeOfGoodsAndService.setVisibility(View.VISIBLE);
                        Customer = Supplier12;
                        checkTypesAndGoods();
                    }
                } else if (roleId.equals("6")) {
                    if(onlysupplier.equals("onlysupplier")){
                        generateRightText1(getString(R.string.create_supplier));
                        etCompanyname.setHint("Supplier/Customer Name");
                        responseMessage = "Supplier created successfully";
                        rlAddButon.setVisibility(View.VISIBLE);
                        rlShippingAddress.setVisibility(View.VISIBLE);
                        CustomerAddress = "Supplier Address";
                        tvForCorporationAddress.setHint(CustomerAddress);
                        rlTypeOfGoodsAndService.setVisibility(View.GONE);
                    }
                    else {
                        generateRightText1(getString(R.string.create_customer));
                        etCompanyname.setHint("Company/Customer Name");
                        responseMessage = "Customer created successfully";
                        rlAddButon.setVisibility(View.VISIBLE);
                        rlShippingAddress.setVisibility(View.VISIBLE);
                        CustomerAddress = "Customer Address";
                        tvForCorporationAddress.setHint(CustomerAddress);
                        rlTypeOfGoodsAndService.setVisibility(View.GONE);
                        //
                    }
                }
                else{
                    generateRightText1(getString(R.string.create_customer));
                    etCompanyname.setHint("Company/Customer Name");
                    responseMessage = "Customer created successfully";
                    rlAddButon.setVisibility(View.GONE);
                    rlShippingAddress.setVisibility(View.GONE);
                    CustomerAddress = "Customer Address";
                    tvForCorporationAddress.setHint(CustomerAddress);
                    rlTypeOfGoodsAndService.setVisibility(View.GONE);
                }
            }
           else {
                //generateRightText1(getString(R.string.create_supplier));
                generateRightText1(getString(R.string.create_customer));
                etCompanyname.setHint("Company/Customer Name");
                responseMessage = "Customer created successfully";
                rlAddButon.setVisibility(View.GONE);
                rlShippingAddress.setVisibility(View.GONE);
                CustomerAddress = "Customer Address";
                tvForCorporationAddress.setHint(CustomerAddress);
                rlTypeOfGoodsAndService.setVisibility(View.GONE);
            }
        } else {
            if (CommonUtility.InvoiceTitle.equals("1")) {
                CustomerAddress = "Customer Address:";
                generateRightText1(getString(R.string.create_customer));
                etCompanyname.setHint("Company/Customer Name");
                responseMessage = "Customer created successfully";
                rlTypeOfGoodsAndService.setVisibility(View.GONE);
            } else {
                CustomerAddress = "Customer Address:";
                //generateRightText1(getString(R.string.create_supplier));
                generateRightText1(getString(R.string.create_customer));
                etCompanyname.setHint("Company/Customer Name");
                responseMessage = "Customer created successfully";
                rlTypeOfGoodsAndService.setVisibility(View.GONE);
            }
        }


        spinner_TypesofGoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //tvTypesofGoods.setText(CommonData.getInvoiceUserData(EditCustomerActivity.this).getGoodsType());
                //mTypesAndGoods = "" + CommonData.getInvoiceUserData(EditCustomerActivity.this).getId();
                if (position == 0) {
                    tvTypesofGoods.setTextColor(Color.BLACK);
                } else {
                    tvTypesofGoods.setTextColor(Color.BLACK);
                    tvTypesofGoods.setText(modalForIndustry.get(position).getIndustryName());
                    mTypesAndGoods = "" + modalForIndustry.get(position).getIndustryId();
                    // mTypesAndGoods = "" + CommonData.getInvoiceUserData(CreateInvoiceCustomer.this).getId();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (etCompanyname.getText().toString().length() != 0 && etTelephone.getText().toString().length() != 0 &&
                        etEmail.getText().toString().length() != 0) {

                    if (modalDataForAddress.getCorporateHouseNo() != null && modalDataForAddress.getCorporateStreet() != null && modalDataForAddress.getCorporateCity() != null && modalDataForAddress.getCorporateState() != null && modalDataForAddress.getCorporatePostalCode() != null) {
                       // if ((modalDataForAddress.getCorporatePostalCode().toString().length() == 8)) {
                        if (modalDataForAddress.getCorporatePostalCode().toString().length() <= 8 && modalDataForAddress.getCorporatePostalCode().toString().length() >= 6) {
                            if (getGenerateRightText1().equals(getString(R.string.create_customer))) {
                                CreateCustomer();
                            } else {
                                createSupplier();
                            }
                        } else {
                            CommonDialog.With(CreateInvoiceCustomer.this).Show(getResources().getString(R.string.postal_code_must_be));
                        }
                    } else {
                        CommonDialog.With(CreateInvoiceCustomer.this).Show("Please fill Corporate address all fields");
                    }
                } else {
                    CommonDialog.With(CreateInvoiceCustomer.this).Show("Please fill mandatory fields.");
                }
                break;
            case R.id.rlAddButon:
                CommonDialog.With(activity).dialogToAddEmail(CreateInvoiceCustomer.this, listForEmails, new EmailListInterface() {
                    @Override
                    public void emailList(ArrayList<String> l) {
                        listForEmails.clear();
                        listForEmails.addAll(l);
                    }
                });
                break;
            case R.id.rlCorporationAddress:
                modalDataForAddress.setDialogType(true);
                CommonDialog.With(activity).dialogForAddress(CreateInvoiceCustomer.this, modalDataForAddress, tvForCorporationAddress, tvForShippingAddress, CustomerAddress);
                break;
            case R.id.rlShippingAddress:
                modalDataForAddress.setDialogType(false);
                CommonDialog.With(activity).dialogForAddress(CreateInvoiceCustomer.this, modalDataForAddress, tvForCorporationAddress, tvForShippingAddress, CustomerAddress);
                break;
            case R.id.tvTypesofGoods:
                spinner_TypesofGoods.performClick();
                break;
        }
    }

    private void CreateCustomer() {
        // InvoiceUserApi/Registration/RegisterInvoiceCustomerSupplier
        for (int i = 0; i < listForEmails.size(); i++) {
            if (emailCC.equals("")) {
                emailCC = listForEmails.get(i);
            } else {
                emailCC = emailCC + "," + listForEmails.get(i);
            }
        }


        ArrayListPost templatePosts = new ArrayListPost();
        templatePosts.add("CompanyName", etCompanyname.getText().toString());
        //   templatePosts.add("FirstName", etContactPerson.getText().toString());
        templatePosts.add("ContactPerson", etContactPerson.getText().toString());

        templatePosts.add("AptNo", modalDataForAddress.getCorporateAptNo());
        templatePosts.add("HouseNo", modalDataForAddress.getCorporateHouseNo());
        templatePosts.add("Street", modalDataForAddress.getCorporateStreet());
        templatePosts.add("City", modalDataForAddress.getCorporateCity());
        templatePosts.add("State", modalDataForAddress.getCorporateState());
        templatePosts.add("PostalCode", modalDataForAddress.getCorporatePostalCode());


        templatePosts.add("ShippingAptNo", modalDataForAddress.getShippingAptNo());
        templatePosts.add("ShippingHouseNo", modalDataForAddress.getShippingHouseNo());
        templatePosts.add("ShippingStreet", modalDataForAddress.getShippingStreet());
        templatePosts.add("ShippingCity", modalDataForAddress.getShippingCity());
        templatePosts.add("ShippingState", modalDataForAddress.getShippingState());
        templatePosts.add("ShippingPostalCode", modalDataForAddress.getShippingPostalCode());

        templatePosts.add("Mobile", etMobileNumber.getText().toString());

        templatePosts.add("Email", etEmail.getText().toString());
        templatePosts.add("AdditionalEmail", emailCC);
        templatePosts.add("Website", etWebsite.getText().toString());
        // templatePosts.add("UserId", "" + getUserId());
        templatePosts.add("CreatedByUserID", "" + getUserId());

        templatePosts.add("Telephone", etTelephone.getText().toString());
//        templatePosts.add("RoleId", Customer);
//        templatePosts.add("Id", "");

        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", templatePosts.getJson().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e("Data", "" + new Gson().toJson(templatePosts));
        LoadingBox.showLoadingDialog(CreateInvoiceCustomer.this, "");
//        RestClient.getApiFinanceServiceForPojo().customerRegistration(getUserId(),etCompanyname.getText().toString(),etContactPerson.getText().toString(),modalDataForAddress.getCorporateAptNo(),modalDataForAddress.getCorporateHouseNo(),modalDataForAddress.getCorporateStreet(),modalDataForAddress.getCorporateCity(),modalDataForAddress.getCorporateState(),modalDataForAddress.getCorporatePostalCode(),modalDataForAddress.getShippingAptNo(),
//                modalDataForAddress.getShippingHouseNo(),modalDataForAddress.getShippingStreet(),modalDataForAddress.getShippingCity(),modalDataForAddress.getShippingState(),modalDataForAddress.getShippingPostalCode(),modalDataForAddress.getCorporatePostalCode(),etMobileNumber.getText().toString(),etEmail.getText().toString(),etWebsite.getText().toString(),emailCC, getCallback(new Callback<JsonElement>() {

        RestClient.getApiFinanceServiceForPojo().customerRegistration(in, getCallback(new Callback<JsonElement>() {

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
                        CommonDialog.showDialog2Button(CreateInvoiceCustomer.this, responseMessage, new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                finish();
                            }
                        });
                    } else {
                        CommonDialog.With(CreateInvoiceCustomer.this).Show(ResponseMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(CreateInvoiceCustomer.this, error);
            }
        }));

    }

    public void createSupplier() {
        for (int i = 0; i < listForEmails.size(); i++) {
            if (emailCC.equals("")) {
                emailCC = listForEmails.get(i);
            } else {
                emailCC = emailCC + "," + listForEmails.get(i);
            }
        }


        ArrayListPost templatePosts = new ArrayListPost();
        templatePosts.add("CompanyName", etCompanyname.getText().toString());
        //   templatePosts.add("FirstName", etContactPerson.getText().toString());
        templatePosts.add("ContactPerson", etContactPerson.getText().toString());

        templatePosts.add("AptNo", modalDataForAddress.getCorporateAptNo());
        templatePosts.add("HouseNo", modalDataForAddress.getCorporateHouseNo());
        templatePosts.add("Street", modalDataForAddress.getCorporateStreet());
        templatePosts.add("City", modalDataForAddress.getCorporateCity());
        templatePosts.add("State", modalDataForAddress.getCorporateState());
        templatePosts.add("PostalCode", modalDataForAddress.getCorporatePostalCode());


        templatePosts.add("ShippingAptNo", modalDataForAddress.getShippingAptNo());
        templatePosts.add("ShippingHouseNo", modalDataForAddress.getShippingHouseNo());
        templatePosts.add("ShippingStreet", modalDataForAddress.getShippingStreet());
        templatePosts.add("ShippingCity", modalDataForAddress.getShippingCity());
        templatePosts.add("ShippingState", modalDataForAddress.getShippingState());
        templatePosts.add("ShippingPostalCode", modalDataForAddress.getShippingPostalCode());

        templatePosts.add("Mobile", etMobileNumber.getText().toString());

        templatePosts.add("Email", etEmail.getText().toString());
        templatePosts.add("AdditionalEmail", emailCC);
        templatePosts.add("Website", etWebsite.getText().toString());
        // templatePosts.add("UserId", "" + getUserId());
        templatePosts.add("CreatedByUserID", "" + getUserId());

        templatePosts.add("Telephone", etTelephone.getText().toString());
//        templatePosts.add("RoleId", Customer);
//        templatePosts.add("Id", "");

        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", templatePosts.getJson().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e("Data", "" + new Gson().toJson(templatePosts));
        LoadingBox.showLoadingDialog(CreateInvoiceCustomer.this, "");
//        RestClient.getApiFinanceServiceForPojo().customerRegistration(getUserId(),etCompanyname.getText().toString(),etContactPerson.getText().toString(),modalDataForAddress.getCorporateAptNo(),modalDataForAddress.getCorporateHouseNo(),modalDataForAddress.getCorporateStreet(),modalDataForAddress.getCorporateCity(),modalDataForAddress.getCorporateState(),modalDataForAddress.getCorporatePostalCode(),modalDataForAddress.getShippingAptNo(),
//                modalDataForAddress.getShippingHouseNo(),modalDataForAddress.getShippingStreet(),modalDataForAddress.getShippingCity(),modalDataForAddress.getShippingState(),modalDataForAddress.getShippingPostalCode(),modalDataForAddress.getCorporatePostalCode(),etMobileNumber.getText().toString(),etEmail.getText().toString(),etWebsite.getText().toString(),emailCC, getCallback(new Callback<JsonElement>() {

        RestClient.getApiFinanceServiceForPojo().supplierRegistration(in, getCallback(new Callback<JsonElement>() {

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
                        CommonDialog.showDialog2Button(CreateInvoiceCustomer.this, responseMessage, new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                finish();
                            }
                        });
                    } else {
                        CommonDialog.With(CreateInvoiceCustomer.this).Show(ResponseMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(CreateInvoiceCustomer.this, error);
            }
        }));

    }


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
                                                                        ModalForIndutry modalForIndustry1 = new ModalForIndutry();
                                                                         modalForIndustry1.setIndustryName("Please Select");
                                                                         modalForIndustry1.setIndustryId("0");
                                                                         modalForIndustry1.setUserId(0);
                                                                         modalForIndustry.add(0, modalForIndustry1);
                                                                         goodsAdapter = new GoodsAdapter(CreateInvoiceCustomer.this, modalForIndustry, 0);
                                                                         spinner_TypesofGoods.setAdapter(goodsAdapter);

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
