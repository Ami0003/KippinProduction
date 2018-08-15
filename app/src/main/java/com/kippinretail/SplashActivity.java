package com.kippinretail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kippin.storage.SharedPref;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.app.Retail;
import com.kippinretail.interfaces.OnLocationGet;
import com.pack.kippin.LoginScreen;

/**
 * Created by kamaljeet.singh on 3/9/2016.
 */
public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 003;
    SplashActivity splashActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashActivity=this;


        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 3 seconds
                    sleep(2000);
                }
                catch (InterruptedException e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally
                {
                    splashActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            //  if(CommonUtility.isNetworkAvailable(this)){
                            boolean isFinanceLoggedIn = checkIfUserLoggedForFinance();
                            boolean isWalletLoggedIn = checkIfUserLoggedForWallet();

                            boolean isInVoiceLogin = checkIfUserLoggedForInvoice();

                            if ((!isFinanceLoggedIn && !isWalletLoggedIn && !isInVoiceLogin) || (isFinanceLoggedIn && isWalletLoggedIn) || (isFinanceLoggedIn && isInVoiceLogin)
                                    || (isWalletLoggedIn && isInVoiceLogin)) {
                                GoToLoginPage();
                            } else if (isWalletLoggedIn) {
                                CommonUtility.UserType = "1";
                                Log.d("AlreadyLogin()", " AlreadyLogin()");
                                AlreadyLoginForWallet();
                            } else if (isFinanceLoggedIn) {
                                CommonUtility.UserType = "0";
                                AlreadyLoginForFinance();
                            } else if (isInVoiceLogin) {
                                CommonUtility.UserType = "2";
                                AlreadyLoginForInvoice();
                            }
                        }
                    });

                }
            }
        };
        timer.start();




    }

    // Invoice user login
    public Boolean checkIfUserLoggedForInvoice() {
        if (CommonData.getInvoiceUserData(getApplicationContext()) != null) {
            return true;
        } else {
            return false;
        }
    }

    private void AlreadyLoginForInvoice() {
        finish();
        Intent in = new Intent();
        in.setClass(SplashActivity.this, UserDashBoardActivity.class);
        in.putExtra("InvoiceUserType", "true");
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

    }

    private void AlreadyLoginForFinance() {
        if (!com.kippin.utils.Utility.checkInternetConnection(SplashActivity.this)){

            com.kippin.utils.Utility.login(this, SharedPref.getString(SharedPref.USERNAME, ""), SharedPref.getString(SharedPref.PASSWORD, ""), false, true);
        }
        else {
            com.kippin.utils.Utility.login(this, SharedPref.getString(SharedPref.USERNAME, ""), SharedPref.getString(SharedPref.PASSWORD, ""), false, false);
        }
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
                Utility.redirectToDashboard(SplashActivity.this,RoleId) ;


            }
        }, SPLASH_TIME_OUT);
    }

    // Check User is already login or not?

    public Boolean checkIfUserLoggedForWallet() {
        if(CommonData.getUserData(getApplicationContext()) != null){
            return true;
        }else{
            return false;
        }

    }
    public Boolean checkIfUserLoggedForFinance() {
        return SharedPref.isLoggedIn();

    }

    // Calling Login page
    public void GoToLoginPage() {

        // Now First Show the choice where user is going to login
        /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);*/
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }
}
