package com.kippin.utils;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;

/**
 * Created by gaganpreet.singh on 3/18/2016.
 */
public class AutoLogoutManager {

    private static AutoLogoutManager autoLogoutManager = null;
//    private final long LOGOUT_INTERVAL = 1000*60*15;//10sec  in millis
//    private final long LOGOUT_INTERVAL = 172800000 ;//2days in millis

    private final long LOGOUT_INTERVAL = 15*60000;//15 minutes in millis
    public static  Activity currentActivity;

    private long requestExecutedAt=-1;

    public static AutoLogoutManager getActiveSession(){
        if(autoLogoutManager ==null)
        autoLogoutManager = new AutoLogoutManager();

        return autoLogoutManager;
    }

    public static void  clearActiveSession(){
        currentActivity=null;
        autoLogoutManager =null;
    }

    public  void registerForAutoLogout(Activity activity){

        if(Singleton.getUser()==null) return;

        currentActivity = activity;
        registerForAutoLogout();

    }
    private void registerForAutoLogout(){
        handler.removeCallbacks(runnable);
        requestExecutedAt = System.currentTimeMillis();
        handler.postDelayed(runnable, LOGOUT_INTERVAL);
        Log.e("hello", "" + System.currentTimeMillis());
    }
    public  void unregisterForAutoLogout(){

        if(Singleton.getUser()==null) return;

        currentActivity=null;

        handler.removeCallbacks(runnable);
    }

    private AutoLogoutManager(){
    }

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.e(""+requestExecutedAt,""+System.currentTimeMillis());
            handler.removeCallbacks(runnable);
            Utility.logout(currentActivity, false);
            clearActiveSession();
        }
    };

}
