package com.pack.kippin;

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;



import com.kippin.kippin.R;
import com.kippin.storage.SharedPref;
import com.kippin.utils.Utility;

public class Splash extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.checkCrashTracker(Splash.this);
        setContentView(R.layout.splash_screen);

        if(SharedPref.isLoggedIn()){
//        if(false){//as now autologin not available
            if (!Utility.checkInternetConnection(Splash.this)){

                Utility.login(this,SharedPref.getString(SharedPref.USERNAME,""),SharedPref.getString(SharedPref.PASSWORD,""),false,true);
            }
            else {
                Utility.login(this, SharedPref.getString(SharedPref.USERNAME, ""), SharedPref.getString(SharedPref.PASSWORD,""),false,false);
            }

        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utility.startActivityFinish(Splash.this, LoginScreen.class, true);

                }
            }, 1500);
        }


    }


}
