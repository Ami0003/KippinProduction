package com.kippin.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;
import com.kippin.app.Kippin;
import com.kippin.bankstatement.ActivityBankStatement;
import com.kippin.cloudimages.ActivityCloudImages;
import com.kippin.cloudimages.CloudClickListener;
import com.kippin.dashboard.ActivityDashboard;
import com.kippin.dashboard.UserStatus;
import com.kippin.kippin.R;
import com.kippin.selectdate.ModelBankStatements;
import com.kippin.selectmonthyear.ActivitySelectMonthYear;
import com.kippin.storage.SharedPref;
import com.kippin.storage.SharedPrefWriter;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.ImagePickUtility.CameraPicListener;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.utils.io.FileWriter;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModalMobileDropdownListing;
import com.kippin.webclient.model.ModelClassification;
import com.kippin.webclient.model.ModelLogin;
import com.kippin.webclient.model.TemplateData;
import com.pack.kippin.LicenseSelectClass;
import com.pack.kippin.LoginScreen;
import com.pack.kippin.PrivateKeyClass;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by dilip.singh on 1/19/2016.
 */
public class Utility {

    // cariable for run time permission
    public static final int MY_PERMISSION_ACCESS_CAMERA = 301;
    public static final int MY_PERMISSION_ACCESS_STORAGE = 302;
    public static final String IS_CREDIT_CARD = "IS_CREDIT_CARD";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final String[] okFileExtensions = new String[]{"jpg", "png", "gif", "jpeg"};
    public static boolean haveConnectedWifi = false;
    public static boolean haveConnectedMobile = false;
    public static boolean haveConnectedActive = false;
    public static ConnectivityManager connMgr;
    public static android.net.NetworkInfo wifi;
    public static android.net.NetworkInfo mobile;
    public static String crashTrackerApi = "19666c2d";
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]";
    public static ActivityBankStatement activityBankStatement;
    public static ActivitySelectMonthYear yearActivity;
    public static ActivitySelectMonthYear monthActivity;
    public static ActivityCloudImages activityCloudImages;
    public static LicenseSelectClass licenseSelectClass;
    //    public static ActivityOCR activityOCR;
    public static CloudClickListener cloudClickListener;
    public static String accNumber = null;
    public static String accName;
    public static String bankId;
    public static CameraPicListener cameraPicListener;
    public static Bitmap bitmap;
    public static ArrayList<ModalMobileDropdownListing> banks;
    public static boolean FinanceLogin = false;
    static CustomProgressDialog customProgressDialog;
    static Dialog dialog;
    static ProgressDialog progressDialog;
    static Handler handler = new Handler();
    private static boolean isDebug = true;
    DialogBox dialogBox;
    Context mContext;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void printLog(String tag, String msg) {

        if (isDebug) {
            Log.e(tag, msg + "");
        }

    }

    public static String getFolderName() {
        String folder = Environment.getExternalStorageDirectory() + File.separator + Kippin.res.getString(R.string.app_name);
        FileWriter.createFolderIfNotExists(folder);
        return folder;
    }

    public static byte[] convertBitmap2ByteArray(Bitmap bm) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static String convertBitmap2Base64(Bitmap bm) throws Exception {
        return Base64.encodeToString(convertBitmap2ByteArray(bm), Base64.DEFAULT);
    }

    public static void showMessage(String message) {
        Toast.makeText(Kippin.kippin, message, Toast.LENGTH_LONG).show();
    }

    public static void startActivity(Activity activity, Class aClass, boolean isInOut) {

        startActivity(activity, aClass, null, true);
        if (isInOut)
            activity.overridePendingTransition(R.anim.push_in_to_left,
                    R.anim.push_in_to_right);
        else
            activity.overridePendingTransition(R.anim.push_out_to_left,
                    R.anim.push_out_to_right);

    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = (int) ((dp * displayMetrics.density) + 0.5);
        return px;
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = (int) ((px / displayMetrics.density) + 0.5);
        return dp;
    }

    public static void startActivityFinish(Activity activity, Class aClass, boolean isInOut) {
        startActivity(activity, aClass, null, true);
        activity.finish();
        if (isInOut)
            activity.overridePendingTransition(R.anim.push_in_to_left,
                    R.anim.push_in_to_right);
        else
            activity.overridePendingTransition(R.anim.push_out_to_left,
                    R.anim.push_out_to_right);

    }

    public static void startActivityFinishBundle(Activity activity, Class aClass,Bundle bundle, boolean isInOut) {
        startActivity(activity, aClass, bundle, true);
        activity.finish();
        if (isInOut)
            activity.overridePendingTransition(R.anim.push_in_to_left,
                    R.anim.push_in_to_right);
        else
            activity.overridePendingTransition(R.anim.push_out_to_left,
                    R.anim.push_out_to_right);
    }

    public static void startActivity(Activity activity, Class aClass, Bundle bundle, boolean isInOut) {

        Intent intent = new Intent(activity, aClass);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        activity.startActivity(intent);
        if (isInOut)
            activity.overridePendingTransition(R.anim.push_in_to_left,
                    R.anim.push_in_to_right);
        else
            activity.overridePendingTransition(R.anim.push_out_to_left,
                    R.anim.push_out_to_right);
    }

    public static void startActivity(Context context, Class aClass, int flags) {
        try {
            Intent intent = new Intent(context, aClass);
            intent.setFlags(flags);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDialog(Context context) {
        dialog = new Dialog(context, R.style.NewDialog);
        customProgressDialog = new CustomProgressDialog(context, dialog);
    }

    public static void dismissDialog() {
        dialog.dismiss();
    }

    public static void showProgressDialog(Context context, int message_id) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false);

            }
            progressDialog.setMessage(Kippin.res.getString(message_id));
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DialogBox dialogBox;
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    public static void moveToTarget(Activity context, Class taget) {
        Intent in = new Intent();
        in.setClass(context, taget);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(in);
        context.finish();
        context.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    public static void logout(Activity activityDashboard, boolean isInOut) {
        SharedPrefWriter.clear();

        Singleton.putUser(null);
        Utility.startActivity(activityDashboard, Kippin.logout, Intent.FLAG_ACTIVITY_NO_HISTORY);
        for (int i = 0; i < SuperActivity.activities.size(); i++) {
            try {
                SuperActivity.activities.get(i).finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
//        activityDashboard.finish();
            if (isInOut)
                activityDashboard.overridePendingTransition(R.anim.push_in_to_left,
                        R.anim.push_in_to_right);
            else
                activityDashboard.overridePendingTransition(R.anim.push_out_to_left,
                        R.anim.push_out_to_right);

            Singleton.clear();
        }


    }

    public static void onBackPress(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_out_to_left,
                R.anim.push_out_to_right);
    }

    public static void login(final Activity context, final String username, final String password, final boolean isDialogNeeded, final boolean isNoInternet) {

        if (isNoInternet) {
            DialogBox dialogBox = new DialogBox(context, context.getResources().getString(R.string.no_internet_connection), true, new DialogBoxListener() {
                @Override
                public void onDialogOkPressed() {
                    context.finish();
                }
            });
        } else {
            WSTemplate wsTemplate = new WSTemplate();
            wsTemplate.aClass = ModelLogin.class;
            wsTemplate.context = context;
            wsTemplate.message_id = R.string.app_name;
            wsTemplate.methods = WSMethods.POST;
            wsTemplate.requestCode = 1001;
            wsTemplate.url = Url.URL_LOGIN;
            ArrayListPost templatePosts = new ArrayListPost();
            templatePosts.add("username", username);
            templatePosts.add("password", password);
            wsTemplate.templatePosts = templatePosts;

            wsTemplate.wsInterface = new WSInterface() {
                @Override
                public void onResult(int requestCode, TemplateData data) {
                    switch (requestCode) {
                        case 1001:
                            Log.e("MODEL DATATATATA","MODEL DAYAYSTATATATATA");
                            final ModelLogin modelLogin = data.getData(ModelLogin.class);
                            final String responseCode = modelLogin.getResponseCode();
                            final Activity activity;
                            Gson gson = new Gson();
                            String modelDataLogin = gson.toJson(modelLogin);
                            Singleton.putUser(modelLogin);
                            activity = (Activity) context;
                            Log.e("ModelLogin:",""+Singleton.getUser().isIsOnlyInvoice());
                            Log.e("modelLogin:",""+modelLogin.is_IsOnlyInvoice());


                            if ((Singleton.getUser() == null || Singleton.getUser().getId().equalsIgnoreCase("") || Singleton.getUser().getId().equalsIgnoreCase("0")) &&
                                    !modelLogin.getResponseCode().equals("3")
                                    ) {
                                new DialogBox(context, "Can't Signin right now, Please try later.", true, new DialogBoxListener() {
                                    @Override
                                    public void onDialogOkPressed() {
                                        context.finish();
                                    }
                                });
                            } else {
                                if (isDialogNeeded) {
                                    String response = modelLogin.getResponseMessage();
                                    if (responseCode.equals("1")) {
                                     //   CommonUtility.setIsPerformAutoLogout(true);
                                        response = Kippin.res.getString(R.string.login_successful);
                                        if (!Singleton.getUser().isTrial() && !Singleton.getUser().isPaid()) {
                                            response = Kippin.res.getString(R.string.pls_chk_email_to_proceed);

                                            new DialogBox(context, response, (DialogBoxListener) null);
                                            return;
                                        }
                                    }

//                                    if(!(Singleton.getUser().isTrial() && !Singleton.getUser().isPaid())  ){
//                                        new DialogBox(context, response  , new DialogBoxListener() {
//                                            @Override
//                                            public void onDialogOkPressed() {
//                                                performOperationLogin(activity,responseCode);
//                                            }
//                                        });
//                                    }else
                                    performOperationLogin(activity, responseCode, isDialogNeeded);
                                } else {

                                    if (!Singleton.getUser().isTrial() && !Singleton.getUser().isPaid()) {
                                        String response = Kippin.res.getString(R.string.pls_chk_email_to_proceed);

                                        new DialogBox(context, response, (DialogBoxListener) null);
                                        return;
                                    } else
                                        performOperationLogin(activity, responseCode, isDialogNeeded);
                                }
                            }
                            break;
                    }
                }
            };
            new WSHandler(wsTemplate).execute();
        }
    }

    private static void performOperationLogin(final Activity activity, String requestCode, final boolean isDialogNeeded) {
        switch (requestCode) {
            case "1":

//                if(Singleton.getUser().isTrial() && !Singleton.getUser().isPaid()){

                if (true) {

                    WSUtils.hitServiceGet(activity, Url.getStatusUrl(), new ArrayListPost(), TemplateData.class, new WSInterface() {
                        @Override
                        public void onResult(int requestCode, TemplateData data) {
//                                data.data = UserStatus.Unpaid.toString();
//                                data.data = "Unpaid";
                            if (data == null || data.data == null || data.data.length() == 0) {
                                return;
                            }

                            if (data.data.contains(UserStatus.Error.toString())) {

                                new DialogBox(activity, "An Error occured. Please login after sometime.", new DialogBoxListener() {
                                    @Override
                                    public void onDialogOkPressed() {
                                        activity.finish();
                                    }
                                });

                            } else if (data.data.contains(UserStatus.Unpaid.toString())) {
                                String response = Kippin.res.getString(R.string.pls_chk_email_to_proceed);

                                SharedPrefWriter.clear();

                                new DialogBox(activity, response, new DialogBoxListener() {
                                    @Override
                                    public void onDialogOkPressed() {
//                                        Utility.logout(activity,true);
//                                        activity.finish();
                                    }
                                });

                            } else {


                                if (isDialogNeeded) {
                                    taskOnSuccessfulLogin();
                                    new DialogBox(activity, activity.getResources().getString(R.string.login_successful), new DialogBoxListener() {
                                        @Override
                                        public void onDialogOkPressed() {
                                            proceedToLogin(activity);
                                        }
                                    });
                                } else {
                                    taskOnSuccessfulLogin();
                                    proceedToLogin(activity);
                                }
                            }
                        }
                    });


                } else {
                    taskOnSuccessfulLogin();
                    proceedToLogin(activity);
                }
                break;

            case "8":
                new DialogBox(activity, "You have been unlinked from your accountant", new DialogBoxListener() {
                    @Override
                    public void onDialogOkPressed() {

                        WSUtils.hitServiceGet(activity, Url.getStatusUrl(), new ArrayListPost(), TemplateData.class, new WSInterface() {
                            @Override
                            public void onResult(int requestCode, TemplateData data) {
//                                data.data = UserStatus.Unpaid.toString();
//                                data.data = "Unpaid";
                                if (data == null || data.data == null || data.data.length() == 0) {
                                    return;
                                }

                                if (data.data.contains(UserStatus.Error.toString())) {
                                    new DialogBox(activity, "An Error occured. Please login after sometime.", new DialogBoxListener() {
                                        @Override
                                        public void onDialogOkPressed() {
                                            activity.finish();
                                        }
                                    });
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("USERNAME", Singleton.getUser().getUsername());
                                    bundle.putString("PASSWORD", Singleton.getUser().getPassword());
                                    bundle.putString("EMAILADDRESS", Singleton.getUser().getEmail());
                                    bundle.putString("PAID", (Utility.isUserAllowed(data.data) ? true : false) + "");
                                    bundle.putBoolean("IsUnlink", Singleton.getUser().isUnlink());
                                    Utility.startActivity(activity, PrivateKeyClass.class, bundle, true);
                                }
                            }
                        });

                    }

//                    @Override
//                    public void onPositiveClick() {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("USERNAME", Singleton.getUser().getUsername());
//                        bundle.putString("PASSWORD" , Singleton.getUser().getPassword());
//                        bundle.putString("EMAILADDRESS" , Singleton.getUser().getEmail()   );
//                        bundle.putString("PAID", true + "");
//                        bundle.putBoolean("IsUnlink", Singleton.getUser().isUnlink()) ;
//                        Utility.startActivity(activity, PrivateKeyClass.class, bundle, true);
//                    }
//
//                    @Override
//                    public void onNegativeClick() {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("USERNAME" , Singleton.getUser().getUsername());
//                        bundle.putString("PASSWORD" , Singleton.getUser().getPassword());
//                        bundle.putString("EMAILADDRESS" , Singleton.getUser().getEmail()   );
//                        bundle.putString("PAID", false+ "");
//                        bundle.putBoolean("IsUnlink", Singleton.getUser().isUnlink()) ;
//                        Utility.startActivity(activity, PrivateKeyClass.class, bundle, true);
//                    }
                });
                break;

            case "3":
//                Utility.logout(activity,true);
//                activity.finish();
                new DialogBox(activity, "Invalid login credentials.", new DialogBoxListener() {
                    @Override
                    public void onDialogOkPressed() {

                    }
                });
                break;

            default:

                boolean isUserSaved = SharedPref.getBoolean(SharedPref.USERNAME, false);
                SharedPref.logout();

                if (isUserSaved) Utility.startActivityFinish(activity, LoginScreen.class, true);
                break;
        }
    }

    private static void proceedToLogin(Activity activity) {
        Utility.startActivityFinish(activity, ActivityDashboard.class, true);
    }

    private static void taskOnSuccessfulLogin() {
        new SharedPrefWriter(SharedPref.USERNAME, Singleton.getUser().getUsername()).execute();
        new SharedPrefWriter(SharedPref.PASSWORD, Singleton.getUser().getPassword()).execute();
    }

    public static void getStatement(final Activity activity, ArrayListPost templatePosts, WSInterface wsInterface) {
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.templatePosts = templatePosts;
        wsTemplate.wsInterface = wsInterface;
        wsTemplate.url = Url.getUrlBankStatement(Singleton.isCreditCard);
        wsTemplate.aClass = ModelBankStatements.class;
        wsTemplate.context = activity;
        wsTemplate.methods = WSMethods.POST;
        wsTemplate.isFormData = false;
        new WSHandler(wsTemplate).execute();
    }

    public static void getCreditStatement(final Activity activity, ArrayListPost templatePosts, WSInterface wsInterface) {
        Log.e("Here", "Here");
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.templatePosts = templatePosts;
        wsTemplate.wsInterface = wsInterface;
        wsTemplate.url = Url.getUrlCreditBankStatement(Singleton.isCreditCard);
        wsTemplate.aClass = ModelBankStatements.class;
        wsTemplate.context = activity;
        wsTemplate.methods = WSMethods.POST;
        wsTemplate.isFormData = false;
        new WSHandler(wsTemplate).execute();
    }

    /**
     * Method use for internet detector
     *
     * @param mContext
     * @return
     */
    public static boolean checkInternetConnection(Context mContext) {

        connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            haveConnectedActive = true;
        }

        if (wifi.isAvailable()) {
            haveConnectedWifi = true;
        }

        if (mobile.isAvailable()) {
            haveConnectedMobile = true;
        }

        if ((haveConnectedWifi == true || haveConnectedMobile == true) && haveConnectedActive == true) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Method use for track the crashes in App
     *
     * @param mContext
     */
    public static void checkCrashTracker(Context mContext) {

        BugSenseHandler.initAndStartSession(mContext, crashTrackerApi);
        BugSenseHandler.startSession(mContext);
        BugSenseHandler.closeSession(mContext);
        BugSenseHandler.flush(mContext);

    }

    public static String getMonth(String month) {
        switch (month) {
            case "1":
                month = "January";
                break;
            case "2":
                month = "February";
                break;
            case "3":
                month = "March";
                break;
            case "4":
                month = "April";
                break;
            case "5":
                month = "May";
                break;
            case "6":
                month = "June";
                break;
            case "7":
                month = "July";
                break;
            case "8":
                month = "August";
                break;
            case "9":
                month = "September";
                break;
            case "10":
                month = "October";
                break;
            case "11":
                month = "November";
                break;
            case "12":
                month = "December";
                break;

        }
        return month;
    }

    public static String getMonthInShort(String month) {
        switch (month) {
            case "1":
                month = "Jan";
                break;
            case "2":
                month = "Feb";
                break;
            case "3":
                month = "Mar";
                break;
            case "4":
                month = "Apr";
                break;
            case "5":
                month = "May";
                break;
            case "6":
                month = "Jun";
                break;
            case "7":
                month = "Jul";
                break;
            case "8":
                month = "Aug";
                break;
            case "9":
                month = "Sept";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            case "12":
                month = "Dec";
                break;

        }
        return month;
    }


//    public static String getAmount(String s){
//        try{
//            Double aDouble  = Double.parseDouble(s);
//            return Math.round(aDouble)+"";
//
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return s;
//        }
//    }


//    public static Bitmap resizeIfneeded(Bitmap bm) {
//
//        if(bm==null)
//            return null;
//
//        try {
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//
////        width : height = memory_consumption(kb)
////        width/memory_consumption : height/memory_consumption(kb) = 1(kb)  {resolution in 1kb}
////        if(image > 512kb)
////        width/memory_consumption *(512 ) : height/memory_consumption *(512 ) = 512(kb)
//
////        float memory_consumption_byte = bm.getByteCount();
////        float memory_consumption_kb = memory_consumption_byte *(1/1000f);
//
//
//            File file = new File(Environment.getExternalStorageDirectory()+File.separator+Kippin.kippin.getString(R.string.temp_folder));
//
//            if(!file.exists()){
//                file.mkdirs();
//            }
//
//            file = new File(Environment.getExternalStorageDirectory()+File.separator+Kippin.kippin.getString(R.string.temp_folder)+File.separator+"Temp.png");
//
//            if(!file.exists())
//            file.createNewFile();
//
//            boolean isSaved= bm.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file)) ;
//            int memory_consumption_kb = Integer.parseInt(String.valueOf(file.length()/1024));
////            file.delete();
////file.get
//            if(memory_consumption_kb>=512){
//
//                float width_required =  (new Float(width) / new Float(memory_consumption_kb))*1024f ;
//
//                float height_required =  (new Float(height) / new Float(memory_consumption_kb))*1024f;
//
//                bm = Bitmap.createScaledBitmap(bm, Math.round(width_required),Math.round(height_required),false);
//
//            }
//            isSaved= bm.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file)) ;
//            memory_consumption_kb = Integer.parseInt(String.valueOf(file.length()/1024));
//
//            if(memory_consumption_kb < 64)
//            {
//                bm = null;
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        catch (Error e) {
//            e.printStackTrace();
//        }
//        return bm;
//    }

    public static String getTimeParsed(String s) {
        try {
            return s.substring(0, s.indexOf("T"));
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * vaid cell phone
     *
     * @param number
     * @return
     */
    public static boolean validCellPhone(String number) {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    private static boolean saveImageTemporarily(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Kippin.kippin.getString(R.string.temp_folder));

        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(Environment.getExternalStorageDirectory() + File.separator + Kippin.kippin.getString(R.string.temp_folder) + File.separator + "Temp.png");

        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        try {
            return bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getRealPathFromURI(Activity context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};

            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            Cursor c1 = context.managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Bitmap resizeIfneeded(File original) {

        int memory_consumption_kb = Integer.parseInt(String.valueOf(original.length()));

        File a = new File("/storage/emulated/0/KIPPIN/temp.png");
        if (memory_consumption_kb <= 64) {
            return null;
        } else {
            Bitmap bm = getBitmapFromFile(original);
            return bm;
        }


//        if(memory_consumption_kb >= 64 && memory_consumption_kb <= 512  ){
//            return bm;
//        }else{
//
//            if(bm==null)
//                return null;
//
//            try {
//                int width = bm.getWidth();
//                int height = bm.getHeight();
//
////        width : height = memory_consumption(kb)
////        width/memory_consumption : height/memory_consumption(kb) = 1(kb)  {resolution in 1kb}
////        if(image > 512kb)
////        width/memory_consumption *(512 ) : height/memory_consumption *(512 ) = 512(kb)
//
////        float memory_consumption_byte = bm.getByteCount();
////        float memory_consumption_kb = memory_consumption_byte *(1/1000f);
//
//
//                File file = new File(Environment.getExternalStorageDirectory()+File.separator+Kippin.kippin.getString(R.string.temp_folder));
//
//                if(!file.exists()){
//                    file.mkdirs();
//                }
//
//                file = new File(Environment.getExternalStorageDirectory()+File.separator+Kippin.kippin.getString(R.string.temp_folder)+File.separator+"Temp.png");
//
//                if(!file.exists())
//                    file.createNewFile();
//
//                boolean isSaved= bm.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file)) ;
////                int memory_consumption_kb = Integer.parseInt(String.valueOf(file.length()/1024));
////            file.delete();
////file.get
//                if(memory_consumption_kb>=512){
//
//                    float width_required =  (new Float(width) / new Float(memory_consumption_kb))*1024f ;
//
//                    float height_required =  (new Float(height) / new Float(memory_consumption_kb))*1024f;
//
//                    bm = Bitmap.createScaledBitmap(bm, Math.round(width_required),Math.round(height_required),false);
//
//                }
//                isSaved= bm.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file)) ;
//                memory_consumption_kb = Integer.parseInt(String.valueOf(file.length()/1024));
//
//                if(memory_consumption_kb < 64)
//                {
//                    bm = null;
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            catch (Error e) {
//                e.printStackTrace();
//            }
//            return bm;
//        }

    }

    private static Bitmap getBitmapFromFile(File original) {

        int REQUIRED_SIZE = 500;

        int memory_consumption_kb = Integer.parseInt(String.valueOf(original.length()));

//        File new_destination= new File(Environment.getExternalStorageDirectory()+File.separator+Kippin.kippin.getString(R.string.temp_folder));
//
//        try {
//            copy(original, new_destination);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        boolean isNeeded = true;

        Bitmap bitmap = resize(original, REQUIRED_SIZE);

//        while (isNeeded && REQUIRED_SIZE>300){
//
//            int memory_consumption_kb = Integer.parseInt(String.valueOf(original.length() / 1024));
//            if(memory_consumption_kb>512){
//                REQUIRED_SIZE = REQUIRED_SIZE-50;
//                Bitmap bitmap = resize(new_destination,REQUIRED_SIZE);
//            }else {
//                REQUIRED_SIZE = REQUIRED_SIZE+50;
//            }
//
//        }


        return bitmap;

//        String selectedImagePath = original.getPath();
//
//        int memory_consumption_kb = Integer.parseInt(String.valueOf(original.length() / 1024));
//
//        Bitmap bm = null;
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(selectedImagePath, options);
//
//        int scale = 1;
//
//        //  scale = REQUIRED_SIZE/(options.outWidth);
//        if (true) {
//            final int REQUIRED_SIZE = 1000;
//            scale = calculateInSampleSize(options, REQUIRED_SIZE, REQUIRED_SIZE);
//        }
//
//
////        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
////                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
////            scale = scale/2;
//
//
//        options.inSampleSize = scale;
//
////        options.
//
//        options.inJustDecodeBounds = false;
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
//        options.inDither = true;
//        bm = BitmapFactory.decodeFile(selectedImagePath, options);
//        saveImageTemporarily(bm);
//        return bm;
    }

    private static Bitmap resize(File image, int REQUIRED_SIZE) {
        String selectedImagePath = image.getPath();
        int memory_consumption_kb = Integer.parseInt(String.valueOf(image.length()));
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        int scale = 1;

        //  scale = REQUIRED_SIZE/(options.outWidth);
        if (true) {
//            final int REQUIRED_SIZE = 1000;
            scale = calculateInSampleSize(options, REQUIRED_SIZE, REQUIRED_SIZE);

//            scale = Math.round(memory_consumption_kb/512f);
        }

        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        saveImageTemporarily(bm);

        return bm;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 3;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;


        }

        return inSampleSize;
    }

    public static void clearMemory() {
        try {
            System.runFinalization();
            Runtime.getRuntime().gc();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    public static boolean isUserAllowed(String status) {


        while (status.contains("\"")) {
            status = status.replace("\"", "");
        }


        if (status.equalsIgnoreCase(UserStatus.Paid.toString())
                || status.equalsIgnoreCase(UserStatus.Trial.toString())
                ) {
            return true;
        }
        return false;
    }

    public static void setVisibility(int visibility, View... views) {

        for (int i = 0; i < views.length; i++) {
            views[i].setVisibility(visibility);
        }

    }

    public static void removeText(View... views) {


        for (int i = 0; i < views.length; i++) {

            if (views[i] instanceof TextView) {
                TextView textView = (TextView) views[i];
                textView.setText("");
            } else if (views[i] instanceof EditText) {
                EditText editText = (EditText) views[i];
                editText.setText("");
            }


        }


    }

    public static boolean imageValid(File file) {
        String extensions = ".jpg.png.jpeg";
        String fileName = file.getName();
        String extension = fileName.substring(fileName.length() - 5, fileName.length());
        if (extensions.contains(extension)) {
            int kb = Utility.getFileInKb(file);

            if (kb < 512) {


            }

        }
        return false;
    }

    private static int getFileInKb(File file) {
        int memory_consumption_kb = Integer.parseInt(String.valueOf(file.length()));
        return memory_consumption_kb;
    }

    public static String getZeroAddedIfLessTen(int i) {
        if (i < 10) {
            return 0 + "" + i;
        }
        return i + "";
    }

    public static String parseDateBySlashes(int day, int month, int year) {
        return getZeroAddedIfLessTen(day) + "/" + getZeroAddedIfLessTen(month) + "/" + year;
    }

    public static String parseDateByHiphenYMD(int day, int month, int year) {
        return year + "-" + getZeroAddedIfLessTen(month) + "-" + getZeroAddedIfLessTen(day);
    }

    public static boolean isImageFileValid(String name) {

        for (String extension : okFileExtensions) {
            if (name.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    public static String shortDouble(String asd) {

        if (!asd.contains(".")) {
            return asd;
        }

        String beforeDot = (asd.substring(0, asd.indexOf('.')));

        String afterDot = (asd.substring(asd.indexOf('.') + 1, asd.length()));

        if (afterDot.length() > 2) {
            return beforeDot + "." + afterDot.substring(0, 2);
        }

        return asd;
    }


    public static double parseDouble(String s) {
        try {
            return (s == null || s.length() == 0) ? 0 : s.startsWith(".") ? 0.0 : Double.parseDouble(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String parseDouble(double c) {

        String s = c + "";

        String data1 = s.substring(s.indexOf(".") + 1, s.length());

        if (/*s.endsWith(".0") ||*/ data1.length() == 1) {
            s = s + "0";
        }

        return s;
    }


    public static String parseTaxWithZero(Double aDouble) {
        if (aDouble == 0)
            return "";
        else return Utility.parseDouble(aDouble);
    }

    public static double add(double credit, double v) {
        BigDecimal bigDecimal = new BigDecimal(credit);
        BigDecimal bigDecimal1 = new BigDecimal(v);
        return roundOFF2Digits(bigDecimal.add(bigDecimal1).doubleValue());
    }

    public static double sub(double credit, double debit) {

        BigDecimal bigDecimal = new BigDecimal(credit);

        BigDecimal bigDecimal1 = new BigDecimal(debit);

        return roundOFF2Digits(bigDecimal.subtract(bigDecimal1).doubleValue());
    }


    public static double add(double... credit) {


        BigDecimal result = new BigDecimal(0);

        for (int i = 0; i < credit.length; i++) {
            result = result.add(new BigDecimal(credit[i]));
        }


        return roundOFF2Digits(result.doubleValue());
    }


    public static double roundOFF2Digits(double a) {
        return Math.round(a * 100.0) / 100.0;
    }

    public static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }

    }

    public static void goToDashboard() {

        while (true) {

            if (!(SuperActivity.activities.get(SuperActivity.activities.size() - 1).getClass().getName().equalsIgnoreCase(ActivityDashboard.class.getName()))) {
                SuperActivity.activities.get(SuperActivity.activities.size() - 1).finish();
                SuperActivity.activities.remove(SuperActivity.activities.size() - 1);
            } else break;

        }

    }

    public static ArrayList<ModelClassification> getSortedClassifications(ArrayList<ModelClassification> modelClassifications) {

        ArrayList<ModelClassification> assets = new ArrayList<>();
        ArrayList<ModelClassification> liability = new ArrayList<>();
        ArrayList<ModelClassification> equity = new ArrayList<>();
        ArrayList<ModelClassification> revenue = new ArrayList<>();
        ArrayList<ModelClassification> expense = new ArrayList<>();


        for (int i = 0; i < modelClassifications.size(); i++) {

            switch (modelClassifications.get(i).getCategoryId()) {
                case 1 + "":
                    assets.add(modelClassifications.get(i));
                    break;
                case 2 + "":
                    expense.add(modelClassifications.get(i));
                    break;
                case 3 + "":
                    revenue.add(modelClassifications.get(i));
                    break;
                case 4 + "":
                    liability.add(modelClassifications.get(i));
                    break;
                case 5 + "":
                    equity.add(modelClassifications.get(i));
                    break;
            }
        }

//        ModelClassification modalClassification = modelClassifications.get(0) ;
        modelClassifications = new ArrayList<>();


//        modelClassifications.add(modalClassification) ;

        modelClassifications.addAll(assets);
        modelClassifications.addAll(liability);
        modelClassifications.addAll(equity);
        modelClassifications.addAll(revenue);
        modelClassifications.addAll(expense);

        return modelClassifications;
    }

    public static String getClassificationHeader_H_T(int chartAccNumber) {
        if (chartAccNumber >= 4000 && chartAccNumber <= 4260) {
            return "Sales Revenue" + ":" + "Net Sales";
        } else if (chartAccNumber >= 4400 && chartAccNumber <= 4500) {
            return "Other Revenue" + ":" + "Total Other Revenue";
        } else if (chartAccNumber >= 5000 && chartAccNumber <= 5350) {
            return "Cost of Goods Sold" + ":" + "Total Cost of Goods Sold";
        } else if (chartAccNumber >= 5400 && chartAccNumber <= 5490) {
            return "Payroll Expenses" + ":" + "Total Payroll Expense";
        } else if (chartAccNumber >= 5600 && chartAccNumber <= 5999) {
            return "General & Administrative Expenses" + ":" + "Total General & Admin. Expenses";
        } else
            return "";
    }


    public static String getClassificationHeader_H_T_ByCustomChartId(int chartId) {

        switch (chartId) {

            case 1:
                return "Sales Revenue" + ":" + "Net Sales";


            case 2:
                return "Other Revenue" + ":" + "Total Other Revenue";

            case 3:
                return "Cost of Goods Sold" + ":" + "Total Cost of Goods Sold";

            case 4:
                return "Payroll Expenses" + ":" + "Total Payroll Expense";

            case 5:
                return "General & Administrative Expenses" + ":" + "Total General & Admin. Expenses";

        }

        return "";
    }


    public static int getClassification_id(int chartAccNumber) {

        if (chartAccNumber >= 4000 && chartAccNumber <= 4260) {
            return 1;
        } else if (chartAccNumber >= 4400 && chartAccNumber <= 4500) {
            return 2;
        } else if (chartAccNumber >= 5000 && chartAccNumber <= 5350) {
            return 3;
        } else if (chartAccNumber >= 5400 && chartAccNumber <= 5490) {
            return 4;
        } else if (chartAccNumber >= 5600 && chartAccNumber <= 5999) {
            return 5;
        } else
            return 0;

    }

    public static void hideKeyboard(Context context, EditText editText) {

        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static void goToHome(Activity activity) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(startMain);
    }

    public static void removeFocus(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }


    public static Bitmap fixOrientation(Bitmap mBitmap, float angle) {
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        } catch (Exception ex) {
            Log.e("fixOrientation", ex.getMessage());
        }

        return mBitmap;
    }

    public static Bitmap fixOrientationInPortrait(Bitmap mBitmap) {
        try {
            if (mBitmap.getWidth() > mBitmap.getHeight()) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);

            }
        } catch (Exception ex) {
            Log.e("fixOrientation", ex.getMessage());
        }

        return mBitmap;
    }

    public static Bitmap fixOrientationInlandcape(Bitmap mBitmap) {
        try {

            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);


        } catch (Exception ex) {
            Log.e("fixOrientation", ex.getMessage());
        }

        return mBitmap;
    }
}
