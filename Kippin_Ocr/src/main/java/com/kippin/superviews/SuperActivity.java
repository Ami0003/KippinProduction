package com.kippin.superviews;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kippin.app.Kippin;
import com.kippin.dashboard.ActivityDashboard;
import com.kippin.kippin.R;
import com.kippin.storage.SharedPrefWriter;
import com.kippin.superviews.listeners.CommonCallbacks;
import com.kippin.topbar.FragmentTopbar;
import com.kippin.topbar.callbacks.TopbarContentCallacks;
import com.kippin.utils.AutoLogoutManager;
import com.kippin.utils.BroadcastService;
import com.kippin.utils.Singleton;
import com.kippin.utils.Utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public abstract class SuperActivity extends FragmentActivity implements CommonCallbacks, TopbarContentCallacks {


    public static ArrayList<Activity> activities = new ArrayList<>();
    FragmentTopbar fragmentTopbar;
    private long startTime = 14 * 60 * 1000;// 15 MINS IDLE TIME
    private final long interval = 1 * 1000;
    CountDownTimer countDownTimer;
    BroadcastService broadcastService;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {

//        Utility.clearMemory();

        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

//        Utility.checkCrashTracker(SuperActivity.this);
//        if(Build.VERSION.SDK_INT >=21)
//            getWindow().setStatusBarColor(Color.WHITE);


        try {
            activities.add(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        context = this;



        // countDownTimer = new MyCountDownTimer(startTime, interval);
    }

    /**
     * Method checks if the app is in background or not
     *
     * @param context
     * @return
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    /**
     * Checks if the application is being sent in the background (i.e behind
     * another application's Activity).
     *
     * @param context the context
     * @return <code>true</code> if another application will be above this one.
     */
    public static boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }
    @Override
    protected void onStop() {
        super.onStop();

    }


    public class MyCountDownTimer extends CountDownTimer {

        /**
         * @param startTime The number of millis in the future from the call
         *                  to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                  is called.
         * @param interval  The interval along the way to receive
         *                  {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            Log.e("onFinish", "onFinish");
            /*SharedPrefWriter.clear();
            Singleton.putUser(null);
          *//*  Utility.startActivity(AutoLogoutManager.currentActivity, Kippin.logout, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            for (int i = 0; i < SuperActivity.activities.size(); i++) {
                try {
                    SuperActivity.activities.get(i).finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Singleton.clear();
            }*//*
            Singleton.clear();
            AutoLogoutManager.clearActiveSession();
            Intent intent = new Intent(SuperActivity.this, Kippin.logout);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
*/
            SharedPrefWriter.clear();
            Singleton.putUser(null);
            Singleton.clear();
            AutoLogoutManager.clearActiveSession();
            Intent intent = new Intent(SuperActivity.this, Kippin.logout);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

        }
    }

    public String getText(TextView textView) {
        return textView.getText().toString();
    }

    public String getText(EditText textView) {
        return textView.getText().toString();
    }

    protected <T extends Serializable> T getData(Class<T> tClass, String key) {
        return (T) getIntent().getSerializableExtra(key);
    }

    protected String returnName(Class aClass) {
        return aClass.getName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        Utility.clearMemory();

        try {
            activities.remove(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // countDownTimer.cancel();
    }

    @Override
    public void initialiseUI() {
        ButterKnife.bind(this);

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();


    }

    @Override
    protected void onResume() {
        super.onResume();
       // Log.e("RESUME", "RESUME");
       // Log.e("RESUME TEST:", "" + isMyServiceRunning());

        if (isMyServiceRunning(BroadcastService.class)) {
            stopService(new Intent(this, BroadcastService.class));
        }

        //AutoLogoutManager.getActiveSession().unregisterForAutoLogout();
        // countDownTimer.cancel();

        if (Singleton.getUser() == null) {
            Utility.logout(this, false);
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) SuperActivity.this.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(Integer.MAX_VALUE);
            if (list != null) {
                for (ActivityManager.RunningServiceInfo service : list) {
                    if (BroadcastService.class.getName().equals(service.service.getClassName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause", "onPause");
        Log.e("Bkground:", "" + isApplicationSentToBackground(context));
        if (isApplicationSentToBackground(context)) {
            Log.e("Background", "Background");
           // broadcastService = new BroadcastService(context);

            startService(new Intent(this, BroadcastService.class));
        }

        //Reset the timer on user interaction...
        //countDownTimer.cancel();
        // countDownTimer.start();
        //  AutoLogoutManager.getActiveSession().registerForAutoLogout(this);

    }

    public void setUpListeners(View.OnClickListener listener, View... view) {
        for (int i = 0; i < view.length; i++) {
            if (view[i] != null) view[i].setOnClickListener(listener);
        }
    }

    public void setFragmentTopbarClickListener(int id, View.OnClickListener onClickListener) {
        fragmentTopbar.addClickListener(id, onClickListener);
    }

    public void generateActionBarLogoutInvoice() {
        fragmentTopbar.show();
        fragmentTopbar.hideBack();
        fragmentTopbar.showLogout();


    }

    public void generateActionBar(int title, boolean isBackPresent, boolean isLogoutPresent) {

        if (getString(title).length() == 0 && !isBackPresent && !isLogoutPresent) {
            fragmentTopbar.hide();
        } else {
            fragmentTopbar.show();
            if (fragmentTopbar != null) {
                fragmentTopbar.setTvTitle(getString(title));
            }
            if (!isBackPresent) {
                fragmentTopbar.hideBack();
            }
            if (!isLogoutPresent) {
                fragmentTopbar.hideHome();
            } else {
                fragmentTopbar.showHome();
            }
        }

    }

    @Override
    public void onInitialise(FragmentTopbar fragmentTopbar) {
        this.fragmentTopbar = fragmentTopbar;

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Utility.onBackPress(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED) return;

    }


}

