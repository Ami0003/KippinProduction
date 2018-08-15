package com.kippinretail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.view.View;

import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.CustomAutoCompleteTextView;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.AddEmployeeSearch.SearchDataa;
import com.kippinretail.Modal.LoginData.LoginDataClass;
import com.kippinretail.Modal.webclient.model.ArrayListPost;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.sharedpreferences.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;


/**
 * Created by kamaljeet.singh on 3/21/2016.
 */
public class AddEmployeeMerchantActivity extends SuperActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    AutoCompleteTextView searchUserEditText;
    EditText employeeIdEditText, emailEditText;
    Button sendKeyToEmployeeButton,btnEmployee;
      private List<SearchDataa> searchList;

    String[] str = {"Andoid", "Jelly Bean", "Froyo",
            "Ginger Bread", "Eclipse Indigo", "Eclipse Juno"};
    String[] strr = {"rini.mittal@smartbuzz.net", "gurleen.singh@smartbuzz.net", "monika@smartbuzz.net", "swati.thakur@smartbuzz.net"};
    String item[] = {
            "rini.mittal@smartbuzz.com", "gurleen.c@b.com", "b@c.copm", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
    };
    List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
    private String mId;
    private String mEmail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_employee_merchant);
        mInitlization();

    }
    private void mInitlization() {
        generateActionBar(R.string.title_merchant_add_employee, true, true, false);
        employeeIdEditText = (EditText) findViewById(R.id.employeeIdEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);

        // Buttons
        sendKeyToEmployeeButton = (Button) findViewById(R.id.sendKeyToEmployeeButton);
        btnEmployee = (Button) findViewById(R.id.btnEmployee);
        sendKeyToEmployeeButton.setOnClickListener(this);
        btnEmployee.setOnClickListener(this);


       // QueryToGetSearchUser();
//        searchUserEditText.setOnItemSelectedListener(this);
//        searchUserEditText.setOnItemClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(this);
    }

    // ServerCall
    private void QueryToGetSearchUser() {
        String merchantId = "" + CommonData.getUserData(AddEmployeeMerchantActivity.this).getId();
        String country = Prefs.with(AddEmployeeMerchantActivity.this).getString("CountryName", "");
        LoadingBox.showLoadingDialog(AddEmployeeMerchantActivity.this, "");
        RestClient.getApiServiceForPojo().queryToGetEmployeeList(merchantId, country,
                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement loginData, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.i("Tag", "Request data " + new Gson().toJson(loginData));
                        //[{"Id":2,"Username":"qauser","Email":"rini.mittal@smartbuzz.net","RoleId":2,"Age":"19","Gender":"Female",
                        // "Dob":"1998-03-16T00:00:00","Location":"Chandigarh,India","Mobile":"123456789","Password":"123456",
                        // "DateModified":"2016-03-18T17:34:26.18","Country":"India","ReferralCode":"riniH7py",
                        // "RefferCodepath":"http://kippin_retailpreproduction.web1.anzleads.com//ReferCode/riniH7py.png",
                        // "MerchantPoint":0.0,"ResponseCode":1,"UserId":0,"ResponseMessage":"Success"}]
                        Gson gson = new Gson();
                        String jsonOutput = loginData.toString();
                        Type listType = new TypeToken<List<SearchDataa>>() {
                        }.getType();

                        searchList = (List<SearchDataa>) gson.fromJson(jsonOutput, listType);
                        for (int i = 0; i < searchList.size(); i++) {

                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("Email", searchList.get(i).getEmail());
                            hm.put("ID", "" + searchList.get(i).getId());
                            aList.add(hm);

                        }

                        // Keys used in Hashmap
                        String[] from = {"ID", "Email"};
                        int[] to = {R.id.flag, R.id.txt};
                        SimpleAdapter adapter = new SimpleAdapter(AddEmployeeMerchantActivity.this, aList, R.layout.autocomplete_layout, from, to);
                        searchUserEditText.setAdapter(adapter);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        MessageDialog.showFailureDialog(AddEmployeeMerchantActivity.this);


                    }

                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendKeyToEmployeeButton:
                if(employeeIdEditText.getText().toString().length()!=0 && emailEditText.getText().toString().length()!=0)
                {
                    if (emailValidator(emailEditText.getText().toString().trim())) {
                        sendKeyToEmployee();
                    } else {
                        CommonDialog.With(AddEmployeeMerchantActivity.this).Show("Please enter valid email id.");
                    }


                }else {
                    MessageDialog.showDialog(AddEmployeeMerchantActivity.this , "Please fill the empty fields.",false);
                }

                break;
            case R.id.btnEmployee:
                showEmployeeList();
                break;

        }
    }

    private void showEmployeeList() {
        Intent i = new Intent();
        i.setClass(this, ActivityEmployeeList.class);
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    private void sendKeyToEmployee() {

//        "Email":(self.TextFieldEmail.text)! as String,
//                "merchantId":"\(MerchantID)",
//                "EmployeeUniqueNumber":(self.TextFieldEmployeeID.text)! as String

        String merchantId = "" + CommonData.getUserData(AddEmployeeMerchantActivity.this).getId();
        LoadingBox.showLoadingDialog(AddEmployeeMerchantActivity.this, "");
        Log.e("mEmail", "=" + mEmail);
        Log.e("merchantId", "=" + merchantId);
        Log.e("mId", "=" + mId);


        HashMap<String , String> jsonBody = new HashMap<String,String>();
        jsonBody.put("Email", emailEditText.getText().toString());
        jsonBody.put("merchantId", merchantId);
        jsonBody.put("EmployeeUniqueNumber", employeeIdEditText.getText().toString());


        RestClient.getApiServiceForPojo().SendPrivateKey(jsonBody,
                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement SendKEy, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.i("SendKEy Tag", "Request data " + new Gson().toJson(SendKEy));
                        Log.e("URL", response.getUrl());
                        JsonObject jsonObj = SendKEy.getAsJsonObject();
                        String strObj = jsonObj.toString();
                        try {
                            JSONObject OBJ = new JSONObject(strObj);
                            String ResponseMessage = OBJ.getString("ResponseMessage");
                            String ResponseCode = OBJ.getString("ResponseCode");
                            if (ResponseCode.equals("1")) {
                                MessageDialog.showDialog(AddEmployeeMerchantActivity.this, "Email with the key has been sent to Employee", true);
                            } else if (ResponseCode.equals("2")) {
                                MessageDialog.showDialog(AddEmployeeMerchantActivity.this,ResponseMessage , false);/*"User not registered with KIPPIN Please ask user to download KIPPIN app and complete registration*/
                            } else if (ResponseCode.equals("9")) {
                                MessageDialog.showDialog(AddEmployeeMerchantActivity.this, ResponseMessage, false);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("SendKey password", " = " + e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        MessageDialog.showFailureDialog(AddEmployeeMerchantActivity.this);
                    }

                });
    }

    private void backPressed() {
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("Email Postion", "==" + parent.getItemAtPosition(position));
        //Log.e("ID Postion","==" + view.getItemAtPosition(position));
        HashMap<String, String> hm = (HashMap<String, String>) parent.getAdapter().getItem(position);
        //{ID=4, Email=gurleen.singh@smartbuzz.net}
        JSONObject j = new JSONObject(hm);
        try {
            mId = j.getString("ID");
            mEmail = j.getString("Email");
            Log.e("ID", "==" + mId);
            Log.e("Email", "==" + mEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Email validation
    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
