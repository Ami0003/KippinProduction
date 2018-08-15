package com.kippinretail.ApplicationuUlity;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.kippinretail.Modal.InvoiceLogin.InvoiceLoginData;
import com.kippinretail.Modal.LoginData.LoginDataClass;
import com.kippinretail.sharedpreferences.Prefs;
import com.kippinretail.sharedpreferences.SharedPreferencesName;

import java.io.File;


/**
 * Created by Sony on 27-08-2015.
 */
public class CommonData {
    private static LoginDataClass loginUserData = null;
    private static InvoiceLoginData invoiceLoginData = null;
    public static String referPath= Environment.getExternalStorageDirectory() + File.separator + "refer_retail_image.png";


    /**
     * @param context
     * @param deviceToken
     */
    public static void SaveDeviceToken(Context context, String deviceToken) {
        Prefs.with(context).save(SharedPreferencesName.DEVICE_TOKEN, deviceToken);
    }

    /**
     * @param context
     * @param _userData
     */
    public static void saveUserData(Context context, LoginDataClass _userData) {
        loginUserData = _userData;
        Prefs.with(context).save(SharedPreferencesName.USERDATA, _userData);
    }

    public static void updateUserData(){

    }
    /**
     * @param context
     * @return
     */
    public static LoginDataClass getUserData(Context context) {
        if (loginUserData == null)
            loginUserData = Prefs.with(context).getObject(SharedPreferencesName.USERDATA, LoginDataClass.class);
        return loginUserData;
    }

    public static void resetData() {
        loginUserData = null;

    }

    public static void resetInvoiceData() {
        invoiceLoginData = null;
    }

    public static  void clearPref(Activity activity){
        loginUserData = null;
        Prefs.with(activity).removeAll();
    }

    public static InvoiceLoginData getInvoiceUserData(Context context) {
        if (invoiceLoginData == null)
            invoiceLoginData = Prefs.with(context).getObject(SharedPreferencesName.INVOCIE_USERDATA, InvoiceLoginData.class);
        return invoiceLoginData;
    }

    public static void saveInvoiceLoginData(Context context, InvoiceLoginData data) {
        invoiceLoginData = data;
        Prefs.with(context).saveInvoice(SharedPreferencesName.INVOCIE_USERDATA, data);
    }

}
