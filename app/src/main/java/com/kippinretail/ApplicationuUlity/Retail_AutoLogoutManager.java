package com.kippinretail.ApplicationuUlity;

import android.app.Activity;
import android.os.Handler;
import android.util.*;

import com.kippin.utils.Singleton;
import com.kippin.utils.Utility;
import com.kippinretail.LoginActivity;
import com.kippinretail.RegistrationActivity;
import com.kippinretail.sharedpreferences.Prefs;

/**
 * Created by sandeep.singh on 8/30/2016.
 */
public class Retail_AutoLogoutManager {
    private static Retail_AutoLogoutManager autoLogoutManager = null;
    private final long LOGOUT_INTERVAL = 1000*60*15;//30 min  in millis  1000*60*30
    //    private final long LOGOUT_INTERVAL = 172800000 ;//2days in millis
    private static Activity currentActivity;
    private static boolean isRegister;
    private long requestExecutedAt=-1;

    public static boolean isRegister() {
        return isRegister;
    }

    public static void setIsRegister(boolean isRegister) {
        Retail_AutoLogoutManager.isRegister = isRegister;
    }

    public static Retail_AutoLogoutManager getActiveSession(Activity activity){
        currentActivity = activity;
        if(autoLogoutManager ==null)
            autoLogoutManager = new Retail_AutoLogoutManager();

        return autoLogoutManager;
    }

    public void  clearActiveSession(){
        currentActivity=null;
        autoLogoutManager =null;
    }

    public  void registerForAutoLogout(Activity activity){

        if(CommonData.getUserData(currentActivity)==null) return;


        registerForAutoLogout();

    }
    private void registerForAutoLogout(){
        isRegister = true;
        handler.removeCallbacks(runnable);
        requestExecutedAt = System.currentTimeMillis();
        handler.postDelayed(runnable, LOGOUT_INTERVAL);
        android.util.Log.e("hello", "" + System.currentTimeMillis());
    }
    public  void unregisterForAutoLogout(){

        if(CommonData.getUserData(currentActivity)==null) return;
        currentActivity=null;
        isRegister = false;
        handler.removeCallbacks(runnable);
    }

    private Retail_AutoLogoutManager(){
    }

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {


            CommonUtility.isCharityDashBoard = false;
            CommonUtility.isMerchantdashBoard = false;
            CommonUtility.isUserDashBoard = false;
            CommonUtility.setIsPerformAutoLogout(true);
            android.util.Log.e("" + requestExecutedAt, "" + System.currentTimeMillis());
            Log.e("Session Expire", "DATA RESET");
          //  Prefs.with(currentActivity).removeAll();
            handler.removeCallbacks(runnable);
            CommonUtility.autoLogoutInBackground(currentActivity);
        //    CommonUtility.moveToTarget(currentActivity, RegistrationActivity.class);
            clearActiveSession();
        }
    };

}
