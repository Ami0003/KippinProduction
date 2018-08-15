package com.kippinretail;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.ApplicationuUlity.imageDownloader.DownloadImageListener;
import com.kippinretail.ApplicationuUlity.imageDownloader.ImageDownloader;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.LoginData.LoginDataClass;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 3/5/2016.
 */
public class SelectUserTypeActivity extends SuperActivity implements View.OnClickListener {
    ImageView userLayout, merchantLayout, charityLayout;
    private String email, password, roleId;
    private boolean flag;
    public static int plus = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_usertype);
        Initlization();
        requestAbsentPermissions(CommonUtility.requiredPermission);
    }

    protected boolean requestAbsentPermissions(String... listPermissionsNeeded) {
        ArrayList<String> requiredPermissions = new ArrayList<>();
        try {

            for (int i = 0; i < listPermissionsNeeded.length; i++) {
                int isGranted = ContextCompat.checkSelfPermission(this, listPermissionsNeeded[i]);
                if (isGranted != PackageManager.PERMISSION_GRANTED) {
                    requiredPermissions.add(listPermissionsNeeded[i]);
                }
            }
            if (requiredPermissions.size() > 0) {
                ActivityCompat.requestPermissions(this, requiredPermissions.toArray(new String[requiredPermissions.size()]), CommonUtility.REQUEST_ID_MULTIPLE_PERMISSIONS);
            }

        } catch (Exception ex) {

        }
        return requiredPermissions.size() == 0;
    }

    private void Initlization() {
        plus = 1;
        generateActionBar(R.string.title_merchant_select_user_type, true, false, false);
        generateRightText("", null);
        userLayout = (ImageView) findViewById(R.id.userLayout);
        merchantLayout = (ImageView) findViewById(R.id.merchantLayout);
        charityLayout = (ImageView) findViewById(R.id.charityLayout);
        userLayout.setOnClickListener(this);
        merchantLayout.setOnClickListener(this);
        charityLayout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        if (i != null) {
            email = i.getStringExtra("email");
            password = i.getStringExtra("password");
            roleId = i.getStringExtra("roleId");
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userLayout:
                flag = checkIntent();
                if (flag) {
                    MultipleLogin(email, password, "2");
                } else {
                    InvokeUserRegistration();
                }
                break;
            case R.id.merchantLayout:
                flag = checkIntent();
                if (flag) {
                    MultipleLogin(email, password, "1");
                } else {
                    InvokeMerchantRegistration();
                }
                break;
            case R.id.charityLayout:
                flag = checkIntent();
                if (flag) {
                    MultipleLogin(email, password, "3");
                } else {
                    InvokeMerchantCharity();
                }
                break;
        }
    }

    private void InvokeMerchantCharity() {
        Intent in = new Intent();
        in.setClass(SelectUserTypeActivity.this, CharityRegistratonActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    private void InvokeMerchantRegistration() {
        Intent in = new Intent();
        in.setClass(SelectUserTypeActivity.this, MerchantRegistrationActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    private void InvokeUserRegistration() {
        Intent in = new Intent();
        in.setClass(SelectUserTypeActivity.this, UserRegistrationActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    private boolean checkIntent() {
        Log.e("checkIntent()", email + " " + password + " " + roleId);
        if (email != null && password != null && roleId != null) {
            return true;
        } else {
            return false;
        }
    }

    private void MultipleLogin(String email, String password, String roleId) {
        LoadingBox.showLoadingDialog(SelectUserTypeActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().MultipleLoginl(email.toString(), password.toString(), roleId,
                new Callback<LoginDataClass>() {
                    @Override
                    public void success(LoginDataClass loginData, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.i("Tag", "Request data " + new Gson().toJson(loginData));
                        CommonData.resetData();
                        CommonData.saveUserData(SelectUserTypeActivity.this, loginData);

                        ImageDownloader imageDownloader = new ImageDownloader(SelectUserTypeActivity.this, loginData.getRefferCodepath(), downloadImageListener);

                        Boolean isFirstLogin2 = CommonData.getUserData(getApplicationContext()).getIsFirstLogin();
                        String responseMessage = CommonData.getUserData(getApplicationContext()).getResponseMessage();
                        String RoleId = "" + CommonData.getUserData(getApplicationContext()).getRoleId();
                        String ResponseCode = "" + CommonData.getUserData(getApplicationContext()).getResponseCode();


                        Boolean isFirstLogin = CommonData.getUserData(getApplicationContext()).getIsFirstLogin();


                        // RoleID "1" means user has merchant account otherwise
                        // RoleID "2" means user has user account otherwise
                        // RoleID "3" means user has charity account otherwise
                        if (ResponseCode.equals("1")) {
                            // isFirstLogin is false when user never perform loggen in operation
                            if (!isFirstLogin && RoleId.equals("2")) {
                                // SHOW REFERAL SCREEN
                                loginAsUser(RoleId);
                            } else {
                                showMessage(RoleId);

                            }
                            //showMessage(RoleId);

                        } else {
                            CommonDialog.With(SelectUserTypeActivity.this).Show(responseMessage);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Login Failure => ", " = " + error.getMessage());

                        MessageDialog.showDialog(SelectUserTypeActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);

                    }

                });
    }

    private void loginAsUser(final String roleId) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(com.kippin.kippin.R.layout.dialogbox_with_msg);
            ;
            TextView textMessage = (TextView) dialog
                    .findViewById(R.id.text_msg);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText("Login Successful");
            TextView btnOk = (TextView) dialog.findViewById(R.id.ok_btn);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SelectUserTypeActivity.this, ReferralActivity.class);
                    i.putExtra("roleId", roleId);
                    i.putExtra("ReferralCode", getIntent().getStringExtra("ReferralCode"));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                    finish();
                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showMessage(final String roleId) {
        try {

            final Dialog dialog = new Dialog(SelectUserTypeActivity.this,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_custom_msg);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            TextView textMessage = (TextView) dialog
                    .findViewById(R.id.textForMessage);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText("Login Successfully");
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Utility.redirectToDashboard(SelectUserTypeActivity.this, roleId);

                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DownloadImageListener downloadImageListener = new DownloadImageListener() {
        @Override
        public void onImageLoaded(Bitmap bitmap) {
            Utility.writeImage(bitmap, CommonData.referPath);
        }

        @Override
        public void onError() {
        }
    };
}
