package com.kippinretail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.AddressCheck.AddressCheck;
import com.kippinretail.Modal.PaymentAcknolegemoage.PaymentDetail;
import com.kippinretail.Modal.UpdateAddress.UpdateAddress;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 10/10/2016.
 */

public class SelectCharityTypeActivity extends SuperActivity implements View.OnClickListener {

    RelativeLayout donateLayout, donateGiftCardLayout, cancelLayout;
    private String userID;
    private String shareType;
    private String customerID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog__donate);

        donateLayout = (RelativeLayout) findViewById(R.id.donateLayout);
        donateGiftCardLayout = (RelativeLayout) findViewById(R.id.donateGiftCardLayout);
        cancelLayout = (RelativeLayout) findViewById(R.id.cancelLayout);
        donateLayout.setOnClickListener(this);
        donateGiftCardLayout.setOnClickListener(this);
        cancelLayout.setOnClickListener(this);
        generateActionBar(R.string.title_user_charitable, true, false, false);
        Intent i = getIntent();
        if (i != null) {
            userID = i.getStringExtra(getString(R.string.user));
            shareType = i.getStringExtra("shareType");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.donateLayout:
                addressCheck(1);

                break;
            case R.id.donateGiftCardLayout:
                addressCheck(2);

                break;
            case R.id.cancelLayout:
                finish();
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                break;
        }

    }

    private void addressCheck(final int value) {
        try {
            customerID = String.valueOf(CommonData.getUserData(SelectCharityTypeActivity.this).getId());
            RestClient.getApiServiceForPojo().CheckUserAddress(customerID, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    LoadingBox.dismissLoadingDialog();
                    Log.e("Output -->", jsonElement.toString());
                    Log.e("url -->", response.getUrl());
                    Type listtype = new TypeToken<AddressCheck>() {
                    }.getType();
                    Gson gson = new Gson();
                    AddressCheck addressCheck = (AddressCheck) gson.fromJson(jsonElement.toString(), listtype);
                    if (addressCheck.getResponseCode() == 1) {
                        if (value == 1) {
                            Intent in = new Intent();
                            in.setClass(SelectCharityTypeActivity.this, PaymentDonationActivity.class);
                            in.putExtra(getString(R.string.user), userID);
                            startActivity(in);
                            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        } else if (value == 2) {
                            Intent intent = new Intent(SelectCharityTypeActivity.this, GiftCardListActivity.class);
                            intent.putExtra("shareType", shareType);
                            intent.putExtra(getString(R.string.user), userID);
                            intent.putExtra("shareType", ShareType.DONATEGIFTCARD);
                            //intent.putExtra(getString(R.string.user), list.get(pos));
                            startActivity(intent);
                            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        }
                    } else {
                        if (addressCheck.getResponseCode() == 4) {
                            showDialogAddress(value);
                        }
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("url -->", error.getUrl());
                    Log.e("Error Comes", error.getMessage() + "");
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showDialog(SelectCharityTypeActivity.this, CommonUtility.TIME_OUT_MESSAGE, true);
                }
            });
        } catch (Exception ex) {
            MessageDialog.showDialog(SelectCharityTypeActivity.this, "Fail to Process");
        }

    }


    private void updateAddressCheck(final int value, String address) {
        try {
            customerID = String.valueOf(CommonData.getUserData(SelectCharityTypeActivity.this).getId());
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("Id", customerID);
            hmap.put("Address", address);
            RestClient.getApiServiceForPojo().UpdateAddress(hmap, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    LoadingBox.dismissLoadingDialog();
                    Log.e("Output -->", jsonElement.toString());
                    Log.e("url -->", response.getUrl());
                    Type listtype = new TypeToken<UpdateAddress>() {
                    }.getType();
                    Gson gson = new Gson();
                    UpdateAddress updateAddress = (UpdateAddress) gson.fromJson(jsonElement.toString(), listtype);
                    if (updateAddress.getResponseCode() == 1) {
                        if (value == 1) {
                            Intent in = new Intent();
                            in.setClass(SelectCharityTypeActivity.this, PaymentDonationActivity.class);
                            in.putExtra(getString(R.string.user), userID);
                            startActivity(in);
                            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        } else if (value == 2) {
                            Intent intent = new Intent(SelectCharityTypeActivity.this, GiftCardListActivity.class);
                            intent.putExtra("shareType", shareType);
                            intent.putExtra(getString(R.string.user), userID);
                            intent.putExtra("shareType", ShareType.DONATEGIFTCARD);
                            //intent.putExtra(getString(R.string.user), list.get(pos));
                            startActivity(intent);
                            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        }
                    } else {

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("url -->", error.getUrl());
                    Log.e("Error Comes", error.getMessage() + "");
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showDialog(SelectCharityTypeActivity.this, CommonUtility.TIME_OUT_MESSAGE, true);
                }
            });
        } catch (Exception ex) {
            MessageDialog.showDialog(SelectCharityTypeActivity.this, "Fail to Process");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume","onResume");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                hideKeyboard();
            }
        }, 100);

    }

    public void showDialogAddress(final int value) {
        try {
            final Dialog dialog = new Dialog(SelectCharityTypeActivity.this);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.add_address_dialog);
            final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

            final EditText etAddress = (EditText) dialog.findViewById(R.id.etAddress);
            imm.hideSoftInputFromWindow(etAddress.getWindowToken(), 0);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            Button btnOk = (Button) dialog.findViewById(R.id.btnAdd);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            imm.hideSoftInputFromWindow(etAddress.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                           // hideKeyboard();
                           // dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                          //  dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            if (etAddress.getText().length() == 0) {

                            } else {
                                updateAddressCheck(value, etAddress.getText().toString());
                            }
                            dialog.dismiss();
                        }
                    }, 500);



                }
            });

            dialog.show();
        } catch (Exception ex) {
            Log.e("Exception in Dialog", ex.getMessage() + "");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause","onPause");
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void hideKeyboard() {

        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
