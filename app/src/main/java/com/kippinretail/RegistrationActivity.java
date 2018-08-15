package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kippinretail.config.Config;
import com.kippin.storage.SharedPref;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;

import com.kippinretail.KippinInvoice.Invoice_LoginScreen;
import com.kippinretail.Modal.webclient.Utility;

import com.pack.kippin.LoginScreen;


/**
 * Created by kamaljeet.singh on 3/5/2016.
 */
public class RegistrationActivity extends SuperActivity implements View.OnClickListener {
    ImageView keepInWallet, keepInAccounts, keepInvoice;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        Config.SCREEN_Reg=1;
        Initlization();

    }


    private void Initlization() {
        generateActionBar(R.string.title_merchant_select_user, false, false, false);
        generateRightText("", null);
        keepInWallet = (ImageView) findViewById(R.id.keepInWallet);
        keepInAccounts = (ImageView) findViewById(R.id.keepInAccounts);
        keepInvoice = (ImageView) findViewById(R.id.keepInvoice);
        keepInWallet.setOnClickListener(this);
        keepInAccounts.setOnClickListener(this);
        keepInvoice.setOnClickListener(this);
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
            case R.id.keepInWallet:
                CommonUtility.UserType = "1";
                if (checkIfUserLoggedForWallet())
                    AlreadyLoginForWallet();
                else
                    InvokeKeepInWallet();
                break;
            case R.id.keepInAccounts:
                CommonUtility.UserType = "0";
                if (checkIfUserLoggedForFinance())
                    AlreadyLoginForFinance();
                else
                    InvokeKeepInAccounts();
                break;
            case R.id.keepInvoice:
                CommonUtility.UserType = "2";
                if (checkIfUserLoggedForInvoice()) {
                    finish();
                    Intent in = new Intent();
                    in.setClass(RegistrationActivity.this, UserDashBoardActivity.class);
                    in.putExtra("InvoiceUserType","true");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                } else {
                    InvokeForInvoice();
                }
                break;
        }
    }


    private void InvokeForInvoice() {
        Intent in = new Intent();
        in.setClass(RegistrationActivity.this, Invoice_LoginScreen.class);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    private void InvokeKeepInAccounts() {
        Intent in = new Intent();
        in.setClass(RegistrationActivity.this, LoginScreen.class);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    private void InvokeKeepInWallet() {
        Intent in = new Intent();
        in.setClass(RegistrationActivity.this, LoginActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }


    public Boolean checkIfUserLoggedForInvoice() {
        if (CommonData.getInvoiceUserData(getApplicationContext()) != null) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkIfUserLoggedForWallet() {
        if (CommonData.getUserData(getApplicationContext()) != null) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkIfUserLoggedForFinance() {
        return SharedPref.isLoggedIn();
    }

    private void AlreadyLoginForWallet() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                String RoleId = "" + CommonData.getUserData(getApplicationContext()).getRoleId();
                Utility.redirectToDashboard(RegistrationActivity.this, RoleId);


            }
        }, 1000);
    }

    private void AlreadyLoginForFinance() {
        if (!com.kippin.utils.Utility.checkInternetConnection(RegistrationActivity.this)) {
            com.kippin.utils.Utility.login(this, SharedPref.getString(SharedPref.USERNAME, ""), SharedPref.getString(SharedPref.PASSWORD, ""), false, true);
        } else {
            com.kippin.utils.Utility.login(this, SharedPref.getString(SharedPref.USERNAME, ""), SharedPref.getString(SharedPref.PASSWORD, ""), false, false);
        }
    }


}
