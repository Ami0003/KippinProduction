package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.LoginData.LoginDataClass;
import com.kippinretail.Modal.ModalResponse;
import com.kippinretail.Modal.webclient.model.ArrayListPost;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClientAdavanced;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

public class ActivityEmployeeAuthentication extends SuperActivity implements View.OnClickListener {

    String merchantId;
    String operation = null;
    private EditText etEnterPrivateKey;
    private Button btnAuthenticate;
    private int cardType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_authentication);
        generateActionBar(R.string.employee_authenticate, true, true, false);
        initialiseUI();
        setUpListeners();
        setUpUI();

    }


    @Override
    public void initialiseUI() {
        super.initialiseUI();

        etEnterPrivateKey = (EditText) findViewById(R.id.etEnterPrivateKey);
        btnAuthenticate = (Button) findViewById(R.id.btnAuthenticate);
        merchantId = getIntent().getStringExtra("merchantId");
        operation = getIntent().getStringExtra("operation");

    }


    @Override
    public void setUpListeners() {
        super.setUpListeners();
        btnAuthenticate.setOnClickListener(this);
    }


    @Override
    public void setUpUI() {
        super.setUpUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        cardType = i.getIntExtra("cardType", -1);
        Utils.hideKeyboard(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnAuthenticate:

                if (etEnterPrivateKey.getText().toString().length() == 0) {
                    MessageDialog.showDialog(this, "Please enter private key which you received from merchant", false);
                    return;
                }
                authenticate();
                CommonUtility.removeFocus(this);
                break;
        }

    }

    private void authenticate() {
        String key = etEnterPrivateKey.getText().toString();
        ArrayListPost templatePosts = new ArrayListPost();
        templatePosts.add("Email", CommonData.getUserData(activity).getEmail());
        templatePosts.add("merchantId", merchantId);
        templatePosts.add("EmployeeUniqueNumber", etEnterPrivateKey.getText().toString());

        TypedInput typedInput = getTypedInput(templatePosts.getJson());

        if (!etEnterPrivateKey.getText().toString().startsWith(" ") && !etEnterPrivateKey.getText().toString().endsWith(" ")) {
            RestClientAdavanced.getApiServiceForPojo(this).AuthenticateUserForEmployee(merchantId, String.valueOf(CommonData.getUserData(activity).getId()), etEnterPrivateKey.getText().toString(), "", new Callback<JsonElement>() {

                @Override
                public void success(final JsonElement jsonElement, Response response) {

                    ModalResponse modalResponse = new Gson().fromJson(jsonElement, ModalResponse.class);


                    if (modalResponse.getResponseCode().equals("1")) {
                        Log.e("data", jsonElement.toString());
                        LoginDataClass loginDataClass = CommonData.getUserData(activity);
                        loginDataClass.setIsAuthenticated(true);
                        CommonData.saveUserData(activity, loginDataClass);

                        Intent intent = getIntent();
                        switch (cardType) {
                            case 0:
                                System.out.print("gift");
                                intent.setClass(activity, ActivityScanLoyaltyCard.class);
                                //intent.setClass(activity, Employee_ScanItem.class);
                                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                                break;

                            case 1:
                                System.out.print("punchcard");
                                intent.setClass(activity, ActivityScanPunchCard.class);
                                //intent.setClass(activity, ActivityPunchCards_Scan.class);
                                break;
                            case 3:
                                intent.setClass(activity, ActivityScanGiftCard.class);
                                break;
                        }
                        startActivity(intent);
                        finish();


                    } else {
                        etEnterPrivateKey.setText("");
                        MessageDialog.showDialog(activity, "Invalid Key and Email.", false);
                    }
                    LoadingBox.dismissLoadingDialog();
                    ;

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("==============", error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showFailureDialog(ActivityEmployeeAuthentication.this);

                    ;
                }
            });
        } else {
            MessageDialog.showDialog(this, "Private Key is Invalid");
        }
    }
}
