package com.kippinretail.KippinInvoice;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.ForgotButtonListner;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.Modal.InvoiceLogin.InvoiceLoginData;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.UserDashBoardActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 11/2/2016.
 */

public class Invoice_LoginScreen extends SuperActivity implements View.OnClickListener {
    EditText editText_email, editText_password;
    ImageView login_btn;
    ImageView new_here_register, ivBack1;
    TextView invoiceLoginData;
    private TextView tvInvoiceForgot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_login);
        Initlization();
    }

    private void Initlization() {
        // TextView

        // Edittext
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_password);

        // ImageView
        login_btn = (ImageView) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        new_here_register = (ImageView) findViewById(R.id.new_here_register);
        new_here_register.setOnClickListener(this);

        tvInvoiceForgot = (TextView) findViewById(R.id.tvInvoiceForgot);
        tvInvoiceForgot.setOnClickListener(this);

        ivBack1 = (ImageView) findViewById(R.id.ivBack1);
        ivBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(com.kippin.kippin.R.anim.animation_from_left, com.kippin.kippin.R.anim.animation_to_right);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                if (!editText_email.getText().toString().isEmpty() && !editText_password.getText().toString().isEmpty()) {
                    callLogin();
                } else {
                    CommonDialog.With(Invoice_LoginScreen.this).Show(getResources().getString(R.string.plz_fill_empty_field));
                }
                break;
            case R.id.new_here_register:
                Intent in = new Intent();
                in.setClass(Invoice_LoginScreen.this, InvoiceRegistrationInvoice.class);
                startActivity(in);
                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                break;
            case R.id.tvInvoiceForgot:
                CommonDialog.forgotDialog(Invoice_LoginScreen.this, new ForgotButtonListner() {
                    @Override
                    public void yes(String s, Dialog d, EditText e) {
                        if (e.getText().toString().length() != 0) {
                            ForgotPasswordRequest(s, d, e);
                        } else {
                            CommonDialog.With(Invoice_LoginScreen.this).Show("Please enter Email");
                        }
                    }

                    @Override
                    public void no() {

                    }
                });

                break;


        }
    }

    private void ForgotPasswordRequest(String s, final Dialog dialog, final EditText et) {

        LoadingBox.showLoadingDialog(Invoice_LoginScreen.this, "");
        RestClient.getApiFinanceServiceForPojo().ForgotPasswordInvoice(s, "",
                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement forgotPassword, Response response) {
                        LoadingBox.dismissLoadingDialog();

                        android.util.Log.e("", response.getUrl());
                        android.util.Log.i("Tag", "Request data " + new Gson().toJson(forgotPassword));
                        JsonObject jsonObj = forgotPassword.getAsJsonObject();
                        String strObj = jsonObj.toString();
                        try {
                            JSONObject OBJ = new JSONObject(strObj);
                            String ResponseMessage = OBJ.getString("ResponseMessage");
                            String ResponseCode = OBJ.getString("ResponseCode");
                            if (ResponseCode.equals("1")) {
                                et.setText("");
                                MessageDialog.showDialog(Invoice_LoginScreen.this, "Username & Password has been sent to your email", false);
                                dialog.dismiss();
                                ;
                            } else {
                                MessageDialog.showDialog(Invoice_LoginScreen.this, ResponseMessage, false);
                                 /*CommonDialog.With(Invoice_LoginScreen.this).Show(ResponseMessage);*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            android.util.Log.e("Forgot password", " = " + e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        android.util.Log.e("Forgot Failure => ", " = " + error.getMessage());
                        MessageDialog.showDialog(Invoice_LoginScreen.this, error.getMessage());

                    }

                });


    }

    private void callLogin() {

        HashMap templatePosts = new HashMap();
        templatePosts.put("EmailTo", editText_email.getText().toString());
        templatePosts.put("Password", editText_password.getText().toString());
        LoadingBox.showLoadingDialog(Invoice_LoginScreen.this, "");
        RestClient.getApiFinanceServiceForPojo().InvoiceLogin(getTypedInput(new Gson().toJson(templatePosts)), new Callback<InvoiceLoginData>() {
            @Override
            public void success(final InvoiceLoginData invoiceLoginData, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Tag", "Request data " + new Gson().toJson(invoiceLoginData));

                if (invoiceLoginData.getResponseCode().equals("1")) {
                    //showMessage();
                    CommonDialog.showDialog2Button(Invoice_LoginScreen.this, "Login Successful", new OnDialogDismissListener() {
                        @Override
                        public void onDialogDismiss() {
                            CommonData.saveInvoiceLoginData(Invoice_LoginScreen.this, invoiceLoginData);
                            callDashBoard();
                        }
                    });
                } else {
                    MessageDialog.showDialog(Invoice_LoginScreen.this, invoiceLoginData.getResponseMessage(), false);
                                /*CommonDialog.With(Invoice_LoginScreen.this).Show(invoiceLoginData.getResponseMessage());*/
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(Invoice_LoginScreen.this, error);
            }
        });


    }

    private void showMessage() {

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
                    callDashBoard();
                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callDashBoard() {
        finish();
        Intent in = new Intent();
        in.setClass(Invoice_LoginScreen.this, UserDashBoardActivity.class);
        // This is true because we hav to show 2 items in Menu drawer list
        in.putExtra("InvoiceUserType", "true");
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }
}
