package com.kippinretail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kippin.storage.SharedPrefWallet;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.ForgotButtonListner;
import com.kippinretail.Modal.LoginData.LoginDataClass;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends Activity implements View.OnClickListener {

    ImageView registerButton;
    ImageView loginButton;
    TextView forgotpasswordTextView;
    EditText usernameEditText, passwordEditText;
    private EditText emailAddressEditText;
    private Dialog dialogForgotPasswrd;
    private String ReferralCode;
    CheckBox checkBoxPolicy;
    ImageView ivBack1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Initilization();
        hideKeyboard();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        usernameEditText.setText("");
        passwordEditText.setText("");
        checkBoxPolicy.setChecked(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(SharedPrefWallet.getBoolean(SharedPrefWallet.REMEMBER_ME, false)){
            usernameEditText.setText(SharedPrefWallet.getString(SharedPrefWallet.USERNAME, ""));
            passwordEditText.setText(SharedPrefWallet.getString(SharedPrefWallet.PASSWORD, ""));
            checkBoxPolicy.setChecked(true);
        }else {
            usernameEditText.setText("");
            passwordEditText.setText("");
            checkBoxPolicy.setChecked(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void hideKeyboard() {

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void Initilization() {

        forgotpasswordTextView = (TextView) findViewById(R.id.forgotpasswordTextView);
        // Buttons
        registerButton = (ImageView) findViewById(R.id.registerButton);
        loginButton = (ImageView) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        forgotpasswordTextView.setOnClickListener(this);
        ivBack1 = (ImageView) findViewById(com.kippin.kippin.R.id.ivBack1);
        // Edittext
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.oldPasswordEditText);
        checkBoxPolicy = (CheckBox) findViewById(R.id.checkBoxPolicy);

        ivBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
            }
        });
   /*     String sourceString = " New here?" + "<b>" + " Register" + "</b> ";
        registerButton.setText(Html.fromHtml(sourceString));*/

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButton:
                InvokeRegistration();
                break;
            case R.id.loginButton:
                //if(Validation.hasText(usernameEditText) && Validation.hasText(passwordEditText)) {
                if (usernameEditText.getText().toString().length() != 0 && passwordEditText.getText().toString().length() != 0) {
                    if (checkBoxPolicy.isChecked()) {
                        SharedPrefWallet.put(SharedPrefWallet.REMEMBER_ME, true);
                        SharedPrefWallet.put(SharedPrefWallet.USERNAME, usernameEditText.getText().toString());
                        SharedPrefWallet.put(SharedPrefWallet.PASSWORD, passwordEditText.getText().toString());
                        CommonUtility.setIsPerformAutoLogout(false);
                    } else {
                        Log.e("setIsPerformAutoLogout","setIsPerformAutoLogout");
                        SharedPrefWallet.put(SharedPrefWallet.REMEMBER_ME, false);
                        CommonUtility.setIsPerformAutoLogout(true);
                    }
                    LoginCall();
                } else {
                    CommonDialog.With(LoginActivity.this).Show("Please fill empty fields.");
                }
                break;
            case R.id.forgotpasswordTextView:
                dialogForgetPassword();
                break;
        }

    }

    private void dialogForgetPassword() {

        CommonDialog.forgotDialog(LoginActivity.this, new ForgotButtonListner() {
            @Override
            public void yes(String s, Dialog d, EditText e) {
                if (e.getText().toString().length() != 0) {
                    if (emailAddressEditText.getText().toString().length() != 0) {
                        if (emailValidator(emailAddressEditText.getText().toString().trim())) {
                            ForgotPassword();
                        } else {
                            emailAddressEditText.setText("");
                            CommonDialog.With(LoginActivity.this).Show("No user exist with this email.");
                        }
                    } else {
                        emailAddressEditText.setText("");
                        CommonDialog.With(LoginActivity.this).Show("Please insert email used during registration");
                    }
                } else {
                    CommonDialog.With(LoginActivity.this).Show("Please enter Email");
                }
            }

            @Override
            public void no() {

            }
        });

       /* dialogForgotPasswrd = new Dialog(LoginActivity.this,
                R.style.Theme_AppCompat_Translucent);
        dialogForgotPasswrd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForgotPasswrd.setContentView(R.layout.dialog_forget_password);

        WindowManager.LayoutParams layoutParams = dialogForgotPasswrd.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;
        dialogForgotPasswrd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialogForgotPasswrd.setCancelable(false);
        dialogForgotPasswrd.setCanceledOnTouchOutside(false);
        emailAddressEditText = (EditText) dialogForgotPasswrd
                .findViewById(R.id.username);


        emailAddressEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialogForgotPasswrd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            }
        });
        Button btnYES = (Button) dialogForgotPasswrd.findViewById(R.id.btnYES);
        btnYES.setText("CANCEL");
        Button btnNO = (Button) dialogForgotPasswrd.findViewById(R.id.btnNO);
        btnNO.setText("SUBMIT");
        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailAddressEditText.getText().toString().length() != 0){
                if (emailValidator(emailAddressEditText.getText().toString().trim())) {
                    ForgotPassword();
                } else {
                    emailAddressEditText.setText("");
                    CommonDialog.With(LoginActivity.this).Show("No user exist with this email.");
                }
                }else{
                    emailAddressEditText.setText("");
                    CommonDialog.With(LoginActivity.this).Show("Please insert email used during registration");
                }

            }
        });

        btnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogForgotPasswrd.dismiss();
            }
        });
        dialogForgotPasswrd.show();*/

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

    private void ForgotPassword() {

        LoadingBox.showLoadingDialog(LoginActivity.this, "");
        RestClient.getApiServiceForPojo().ForgotPassword(emailAddressEditText.getText().toString(), "",
                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement forgotPassword, Response response) {
                        LoadingBox.dismissLoadingDialog();

                        Log.e("", response.getUrl());
                        Log.i("Tag", "Request data " + new Gson().toJson(forgotPassword));
                        JsonObject jsonObj = forgotPassword.getAsJsonObject();
                        String strObj = jsonObj.toString();
                        try {
                            JSONObject OBJ = new JSONObject(strObj);
                            String ResponseMessage = OBJ.getString("ResponseMessage");
                            String ResponseCode = OBJ.getString("ResponseCode");
                            if (ResponseCode.equals("1")) {
                                emailAddressEditText.setText("");

                                CommonDialog.With(LoginActivity.this).Show("Username & Password has been sent to your email");
                                dialogForgotPasswrd.dismiss();
                                ;
                            } else {

                                emailAddressEditText.setText("");
                                CommonDialog.With(LoginActivity.this).Show("No user exit with this email.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Forgot password", " = " + e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Forgot Failure => ", " = " + error.getMessage());
                        MessageDialog.showFailureDialog(LoginActivity.this);

                    }

                });
    }

    // Login Call function
    private void LoginCall() {
        LoadingBox.showLoadingDialog(LoginActivity.this, "");
        // Merchant  Role Id 1
        RestClient.getApiServiceForPojo().LoginWithEmail(usernameEditText.getText().toString(), passwordEditText.getText().toString(),
                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement loginData_json, Response response) {
                        Log.e(loginData_json.toString(), response.getUrl());
                        LoginDataClass loginData = new Gson().fromJson(loginData_json, LoginDataClass.class);
                        LoadingBox.dismissLoadingDialog();
                        Log.i("Tag", "Request data " + loginData_json);
                        JsonObject jsonObj = loginData_json.getAsJsonObject();
                        String strObj = jsonObj.toString();
                        try {
                            JSONObject OBJ = new JSONObject(strObj);
                            ReferralCode = OBJ.getString("ReferralCode");
                            String ResponseMessage = OBJ.getString("ResponseMessage");
                            String ResponseCode = OBJ.getString("ResponseCode");
                            String RoleId = OBJ.getString("RoleId");
                            String MultipleRole = OBJ.getString("MultipleRole");
                            if (!ResponseMessage.equals("Invalid Email and Password.")) {
                                if (ResponseCode.equals("1") || ResponseCode.equals("9") || ResponseCode.equals("3")) {
                                    if (!MultipleRole.equals("null")) {
                                        if (MultipleRole.equals("Yes")) {
                                            Intent i = new Intent(LoginActivity.this, SelectUserTypeActivity.class);
                                            i.putExtra("email", usernameEditText.getText().toString());
                                            i.putExtra("password", passwordEditText.getText().toString());
                                            i.putExtra("roleId", RoleId);
                                            i.putExtra("ReferralCode", ReferralCode);
                                            i.putExtra("isFirst", loginData.getIsFirstLogin());
                                            startActivity(i);
                                            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

                                        }
                                    } else if (RoleId.equals(CommonUtility.userRoleId)) {
                                        boolean isFirstLogin = loginData.getIsFirstLogin();
                                        if (isFirstLogin) {
                                            // User is Already Loged In
                                            showMessage(RoleId);
                                        } else {
                                            // User Is Login As First Time
                                            loginAsUser(RoleId);
                                        }

                                        CommonData.saveUserData(LoginActivity.this, loginData);

                                    } else {
                                        showMessage(RoleId);
                                        CommonData.saveUserData(LoginActivity.this, loginData);
                                    }

                                }
                            } else {
                                MessageDialog.showDialog(LoginActivity.this, ResponseMessage, false);
                            }
                        } catch (JSONException ex) {

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        MessageDialog.showFailureDialog(LoginActivity.this);
                    }

                });

    }

    private void loginAsUser(String roleId) {
        //
        //ReferralActivity
        Intent i = new Intent(LoginActivity.this, UserDashBoardActivity.class);
        i.putExtra("roleId", roleId);
        i.putExtra("ReferralCode", ReferralCode);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        finish();

    }

    private void showMessage(final String roleId) {

        try {

            final Dialog dialog = new Dialog(this);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_custom_msg);
            TextView textMessage = (TextView) dialog
                    .findViewById(R.id.text_msg);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText("Login Successful");
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Utility.redirectToDashboard(LoginActivity.this, roleId);
                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InvokeRegistration() {
        Intent in = new Intent();
        in.setClass(LoginActivity.this, SelectUserTypeActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);


    }

}
