package com.kippinretail.KippinInvoice;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.ApplicationuUlity.PhoneWatcher;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.Interface.EmailListInterface;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.KippinInvoice.InvoiceAdapter.GoodsAdapter;
import com.kippinretail.KippinInvoice.ModalInvoice.ModalForIndutry;
import com.kippinretail.Modal.Invoice.EditCustomer;
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

import static com.kippinretail.R.id.etCorprationAddress;

/**
 * Created by kamaljeet.singh on 11/22/2016.
 */

public class EditCustomerActivity extends SuperActivity implements View.OnClickListener {
    EditText etCompanyname, etContactPerson, etMobileNumber, etWebsite, etEmail, etTelephone;
    RelativeLayout rlAddButon, rlCorporationAddress, rlShippingAddress;
    ArrayList<String> listForEmails = new ArrayList();
    TextView tvForCorporationAddress, tvForShippingAddress;
    private Button btn_submit;
    private String emailCC = "";
    private PhoneWatcher phoneWatcher;
    private ModalDataForAddress modalDataForAddress;
    private String CustomerData;
    private EditCustomer editCustomer;
    private String roleId;
    private String responseMessage;
    //    Spinner spinner_TypesofGoods;
//    TextView tvTypesofGoods;
    private String mTypesAndGoods;
    private List<ModalForIndutry> modalForIndustry;
    GoodsAdapter goodsAdapter;
    RelativeLayout relTypesofGoods;
    TextView tvStar2;
    private String CustomerAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_customer_supplier);
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
        etEmail.setEnabled(false);
        //Button
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        // Relativelayout
        rlAddButon = (RelativeLayout) findViewById(R.id.rlAddButon);
        rlAddButon.setOnClickListener(this);


        rlCorporationAddress = (RelativeLayout) findViewById(R.id.rlCorporationAddress);
        rlCorporationAddress.setOnClickListener(this);

        rlShippingAddress = (RelativeLayout) findViewById(R.id.rlShippingAddress);
        rlShippingAddress.setOnClickListener(this);

        //TextViewsz
        tvForCorporationAddress = (TextView) findViewById(etCorprationAddress);
        tvForShippingAddress = (TextView) findViewById(R.id.etShippingAddress);
        tvForCorporationAddress.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvForShippingAddress.setMovementMethod(ScrollingMovementMethod.getInstance());

//        spinner_TypesofGoods = (Spinner) findViewById(R.id.spinner_TypesofGoods);
//        tvTypesofGoods = (TextView) findViewById(R.id.tvTypesofGoods);
//        tvTypesofGoods.setOnClickListener(this);

        phoneWatcher = new PhoneWatcher(etTelephone);
        etTelephone.addTextChangedListener(phoneWatcher);
        phoneWatcher = new PhoneWatcher(etMobileNumber);
        etMobileNumber.addTextChangedListener(phoneWatcher);
        tvStar2 = (TextView) findViewById(R.id.tvStar2);

        relTypesofGoods = (RelativeLayout) findViewById(R.id.relTypesofGoods);

        modalDataForAddress = new ModalDataForAddress();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            CustomerData = extras.getString("CustomerData");
            editCustomer = gson.fromJson(CustomerData, EditCustomer.class);
            roleId = extras.getString("roleId");

        }

        if (roleId.equals("2")) {
            rlAddButon.setVisibility(View.VISIBLE);
            etCompanyname.setHint("Company/Customer Name");
            CustomerAddress = "Customer Address:";
//            generateRightText1(getString(R.string.edit_customer));
            generateRightText1(getString(R.string.view_customer));
            relTypesofGoods.setVisibility(View.GONE);

            //  enableDisableUI();

        } else {
            rlAddButon.setVisibility(View.GONE);
            rlShippingAddress.setVisibility(View.GONE);
            CustomerAddress = "Supplier Address";
            etCompanyname.setHint("Company/Supplier Name");
            tvForCorporationAddress.setHint("Supplier Address");

            //generateRightText1(getString(R.string.edit_supplier));
            generateRightText1(getString(R.string.view_supplier));
            relTypesofGoods.setVisibility(View.VISIBLE);
            tvStar2.setVisibility(View.VISIBLE);
            checkTypesAndGoods();
        }

        getCustomerList();

//        spinner_TypesofGoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //tvTypesofGoods.setText(CommonData.getInvoiceUserData(EditCustomerActivity.this).getGoodsType());
//                //mTypesAndGoods = "" + CommonData.getInvoiceUserData(EditCustomerActivity.this).getId();
//                if (position == 0) {
//                    tvTypesofGoods.setTextColor(Color.BLACK);
//                } else {
//                    tvTypesofGoods.setTextColor(Color.BLACK);
//                    tvTypesofGoods.setText(editCustomer.getServiceOffered());
//                    //mTypesAndGoods = "" + CommonData.getInvoiceUserData(EditCustomerActivity.this).getId();
//                }
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    private void setData() {

        if (editCustomer.getCustomerId() == null) {
            disableUI();
        } else {
            enableUI();
        }
//        if(editCustomer.getServiceOffered()!=null) {
//            tvTypesofGoods.setText(editCustomer.getServiceOffered());
//        }
        if (editCustomer.getCompanyName() != null) {
            etCompanyname.setText(editCustomer.getCompanyName());
        }
        if (editCustomer.getContactPerson() != null) {
            etContactPerson.setText(editCustomer.getContactPerson());
        }
        if (editCustomer.getMobile() != null) {
            etMobileNumber.setText(editCustomer.getMobile());
        }
        if (editCustomer.getTelephone() != null) {
            etTelephone.setText(editCustomer.getTelephone());
        }
        if (editCustomer.getEmail() != null) {
            etEmail.setText(editCustomer.getEmail());
        }
        if (editCustomer.getWebsite() != null) {
            etWebsite.setText(editCustomer.getWebsite());
        }
        if (editCustomer.getStreet() != null) {

            modalDataForAddress.setCorporateAptNo(editCustomer.getAptNo());
            modalDataForAddress.setCorporateHouseNo(editCustomer.getHouseNo());
            modalDataForAddress.setCorporateStreet(editCustomer.getStreet());

        }
        modalDataForAddress.setCorporateCity(editCustomer.getCity());
        modalDataForAddress.setCorporateState(editCustomer.getState());
        modalDataForAddress.setCorporatePostalCode(editCustomer.getPostalCode());
        String add = modalDataForAddress.getCorporateAptNo() + "-" + modalDataForAddress.getCorporateHouseNo();
        if (modalDataForAddress.getCorporateAptNo() != null) {
            if (modalDataForAddress.getCorporateAptNo().length() == 0)
                add = add.substring(1);
        }

        tvForCorporationAddress.setText(CustomerAddress + "\n" + add + " " + modalDataForAddress.getCorporateStreet() + "\n" + modalDataForAddress.getCorporateCity()
                + "," + modalDataForAddress.getCorporateState() + "\n" + modalDataForAddress.getCorporatePostalCode());


        if (editCustomer.getShippingState() != null) {

            modalDataForAddress.setShippingAptNo(editCustomer.getShippingAptNo());
            modalDataForAddress.setShippingHouseNo(editCustomer.getShippingHouseNo());
            modalDataForAddress.setShippingStreet(editCustomer.getShippingStreet());

        }
        modalDataForAddress.setShippingCity(editCustomer.getShippingCity());
        modalDataForAddress.setShippingState(editCustomer.getShippingState());
        modalDataForAddress.setShippingPostalCode(editCustomer.getShippingPostalCode());


        tvForShippingAddress.setText("Shipping Address:" + "\n" + modalDataForAddress.getCorporateAptNo() + " " + modalDataForAddress.getShippingStreet() + "\n" + modalDataForAddress.getShippingCity()
                + "," + modalDataForAddress.getShippingState() + "\n" + modalDataForAddress.getShippingPostalCode());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (etCompanyname.getText().toString().length() != 0 && etTelephone.getText().toString().length() != 0 &&
                        etEmail.getText().toString().length() != 0) {
                    if (roleId.equals("2")) {
                        EditCustomer();
                    } else {
                        editSupplier();
                    }
                } else {
                    CommonDialog.With(EditCustomerActivity.this).Show("Please fill mandatory fields.");
                }
                break;
            case R.id.rlAddButon:
                CommonDialog.With(activity).dialogToAddEmail(EditCustomerActivity.this, listForEmails, new EmailListInterface() {
                    @Override
                    public void emailList(ArrayList<String> l) {
                        listForEmails.clear();
                        listForEmails.addAll(l);
                    }
                });
                break;
            case R.id.rlCorporationAddress:
                modalDataForAddress.setDialogType(true);
                CommonDialog.With(activity).dialogForAddress(EditCustomerActivity.this, modalDataForAddress, tvForCorporationAddress, tvForShippingAddress, CustomerAddress);
                break;
            case R.id.rlShippingAddress:
                modalDataForAddress.setDialogType(false);
                CommonDialog.With(activity).dialogForAddress(EditCustomerActivity.this, modalDataForAddress, tvForCorporationAddress, tvForShippingAddress, CustomerAddress);
                break;
//            case R.id.tvTypesofGoods:
//                spinner_TypesofGoods.performClick();
//                break;
        }
    }


    private void getCustomerList() {


        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().getCustomerDetail(editCustomer.getCustomerUserMappingId(), new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                        Gson gson = new Gson();
                        editCustomer = gson.fromJson(jsonElement.toString(), new TypeToken<EditCustomer>() {
                        }.getType());
                        if (editCustomer.getResponseCode() == 1) {
                            setData();
                        } else {
                            CommonDialog.With(EditCustomerActivity.this).Show(editCustomer.getResponseMessage());
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

    private void disableUI() {

        etCompanyname.setFocusable(false);
        etContactPerson.setFocusable(false);
        rlCorporationAddress.setOnClickListener(null);
        rlShippingAddress.setOnClickListener(null);
        etTelephone.setFocusable(false);
        etMobileNumber.setFocusable(false);
        etEmail.setFocusable(false);
        rlAddButon.setOnClickListener(null);
        rlAddButon.setAlpha((float) 0.3);
        etWebsite.setFocusable(false);
        btn_submit.setVisibility(View.GONE);
    }

    private void enableUI() {
        etCompanyname.setFocusable(true);
        etContactPerson.setFocusable(true);
        rlCorporationAddress.setOnClickListener(this);
        rlShippingAddress.setOnClickListener(this);
        etTelephone.setFocusable(true);
        etMobileNumber.setFocusable(true);
        etEmail.setFocusable(true);
        rlAddButon.setOnClickListener(this);
        rlAddButon.setAlpha((float) 1.0);
        etWebsite.setFocusable(true);
        btn_submit.setVisibility(View.VISIBLE);
    }


    public void editSupplier() {
        for (int i = 0; i < listForEmails.size(); i++) {
            if (emailCC.equals("")) {
                emailCC = listForEmails.get(i);
            } else {
                emailCC = emailCC + "," + listForEmails.get(i);
            }
        }


        ArrayListPost templatePosts = new ArrayListPost();
        templatePosts.addInt("SupplierId", editCustomer.getCustomerUserMappingId());
        templatePosts.add("CompanyName", etCompanyname.getText().toString());
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
        templatePosts.add("UserId", "" + getUserId());
        templatePosts.add("Telephone", etTelephone.getText().toString());
        templatePosts.add("CreatedByUserID", "" + getUserId());

        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", templatePosts.getJson().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e("Data", "" + new Gson().toJson(templatePosts));
        LoadingBox.showLoadingDialog(EditCustomerActivity.this, "");
        RestClient.getApiFinanceServiceForPojo().editSupplierDetail(in, getCallback(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Tag", "Request data " + new Gson().toJson(jsonElement));

                if (roleId.equals("1")) {
                    responseMessage = "Supplier Updated Successfully";
                } else {
                    responseMessage = "Customer Updated Successfully";
                }

                // {"ResponseCode":1,"ResponseMessage":"Please check your mail for email confirmation.","UserId":3}
                try {
                    JSONObject jsonObject = new JSONObject(jsonElement.toString());
                    String ResponseCode = jsonObject.getString("ResponseCode");


                    if (ResponseCode.equals("1")) {
                        final EditCustomer editCustomer1 = gson.fromJson(jsonElement, EditCustomer.class);


                        CommonDialog.showDialog2Button(EditCustomerActivity.this, responseMessage, new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                Intent intent = new Intent();
                                intent.putExtra("data", editCustomer1);

                                setResult(011, intent);
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(EditCustomerActivity.this, error);
            }
        }));


    }

    private void EditCustomer() {
        for (int i = 0; i < listForEmails.size(); i++) {
            if (emailCC.equals("")) {
                emailCC = listForEmails.get(i);
            } else {
                emailCC = emailCC + "," + listForEmails.get(i);
            }
        }


        ArrayListPost templatePosts = new ArrayListPost();
        templatePosts.add("CustomerId", "" + editCustomer.getCustomerId());
        templatePosts.add("CompanyName", etCompanyname.getText().toString());
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
        templatePosts.add("UserId", "" + getUserId());
        templatePosts.add("Telephone", etTelephone.getText().toString());
        templatePosts.add("CreatedByUserID", "" + getUserId());

        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", templatePosts.getJson().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e("Data", "" + new Gson().toJson(templatePosts));
        LoadingBox.showLoadingDialog(EditCustomerActivity.this, "");
        RestClient.getApiFinanceServiceForPojo().editCustomerDetail(in, getCallback(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Tag", "Request data " + new Gson().toJson(jsonElement));

                if (roleId.equals("1")) {
                    responseMessage = "Supplier Updated Successfully";
                } else {
                    responseMessage = "Customer Updated Successfully";
                }

                // {"ResponseCode":1,"ResponseMessage":"Please check your mail for email confirmation.","UserId":3}
                try {
                    JSONObject jsonObject = new JSONObject(jsonElement.toString());
                    String ResponseCode = jsonObject.getString("ResponseCode");


                    if (ResponseCode.equals("1")) {
                        final EditCustomer editCustomer1 = gson.fromJson(jsonElement, EditCustomer.class);


                        CommonDialog.showDialog2Button(EditCustomerActivity.this, responseMessage, new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                Intent intent = new Intent();
                                intent.putExtra("data", editCustomer1);

                                setResult(011, intent);
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(EditCustomerActivity.this, error);
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
                                                                        /* ModalForIndutry modalForIndustry1 = new ModalForIndutry();
                                                                         modalForIndustry1.setIndustryName("Types of goods/service offered");
                                                                         modalForIndustry1.setIndustryId("0");
                                                                         modalForIndustry1.setUserId(0);
                                                                         modalForIndustry.add(0, modalForIndustry1);*/
//                                                                         goodsAdapter = new GoodsAdapter(EditCustomerActivity.this, modalForIndustry, 0);
//                                                                         spinner_TypesofGoods.setAdapter(goodsAdapter);

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
