package com.kippin.app;

import android.app.Application;
import android.content.res.Resources;

import com.bumptech.glide.request.target.ViewTarget;
import com.kippin.kippin.R;
import com.kippin.utils.AutoLogoutManager;
import com.kippin.utils.Utility;

/**
 * Created by gaganpreet.singh on 1/21/2016.
 */
public class Kippin {

    public static Application kippin;
    public static Resources res;
    public static Class logout;
    public static Class invoiceDashBoardClass;
    public static Class invoiceRegistrationClass;
    public static Class RegistrationSuiteClass;
    public static Class _FinanceDashBoard;

    public static void onCreate(Application kippin , Class logout1 ,Class invoiceDashBoard, Class invoiceRegistration, Class RegistrationSuite, Class FinanceDashBoard) {
        Utility.checkCrashTracker(kippin);
        Kippin.kippin = kippin ;
        res = kippin.getResources();
        invoiceDashBoardClass = invoiceDashBoard;
        invoiceRegistrationClass = invoiceRegistration;
        RegistrationSuiteClass = RegistrationSuite;
        _FinanceDashBoard = FinanceDashBoard;
        ViewTarget.setTagId(R.id.glide_tag);
        logout = logout1 ;
        AutoLogoutManager.getActiveSession();
    }

}
