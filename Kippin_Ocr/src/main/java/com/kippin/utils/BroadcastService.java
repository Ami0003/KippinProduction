package com.kippin.utils;

/**
 * Created by amit on 8/29/2017.
 */

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.kippin.app.Kippin;
import com.kippin.kippin.R;
import com.kippin.storage.SharedPrefWriter;
import com.kippin.superviews.SuperActivity;

public class BroadcastService extends Service {
    private final static String TAG = "BroadcastService";

    CountDownTimer cdt = null;
    private final long LOGOUT_INTERVAL = 15 * 60000;//15 minutes in millis
    private final long interval = 1 * 1000;




    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("onCreate", "onCreate");

    }

    @Override
    public void onDestroy() {

        cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onStartCommand", "onStartCommand");
        cdt = new CountDownTimer(LOGOUT_INTERVAL, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e(TAG, "onTick");
            }

            @Override
            public void onFinish() {
                cdt.cancel();
                SharedPrefWriter.clear();
                Singleton.putUser(null);
                Singleton.clear();
                AutoLogoutManager.clearActiveSession();
                Intent intent = new Intent(BroadcastService.this, Kippin.logout);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // finish();
                //  overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                stopSelf();
            }
        };
        cdt.start();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}