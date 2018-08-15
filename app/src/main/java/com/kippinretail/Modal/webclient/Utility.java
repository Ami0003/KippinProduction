package com.kippinretail.Modal.webclient;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.kippinretail.ActivityCharity_GiftCardUsingMerchant;
import com.kippinretail.ActivityDashboardCharity;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.imageDownloader.DownloadImageListener;
import com.kippinretail.ApplicationuUlity.imageDownloader.ImageDownloader;
import com.kippinretail.DashBoardMerchantActivity;
import com.kippinretail.LoginActivity;
import com.kippinretail.Modal.ModalResponse;
import com.kippinretail.R;
import com.kippinretail.UserDashBoardActivity;
import com.kippinretail.app.Retail;
import com.kippinretail.config.Utils;
import com.kippinretail.sharedpreferences.Prefs;
import com.pack.kippin.RegisterScreen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

//import com.facebook.internal.DialogBox;

/**
 * Created by gaganpreet.singh on 4/8/2016.
 */
public class Utility {

    public static final String ROLE_ID_MERCHANT="1";
    public static final String ROLE_ID_USER="2";
    public static final String ROLE_ID_CHARITY="3";

    /**
     * Method use for internet detector
     * @param mContext
     * @return
     */
    public static boolean checkInternetConnection(Context mContext) {

        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        boolean haveConnectedActive=false;
        if(activeNetworkInfo!=null)
        {
            haveConnectedActive=true;
        }

        boolean haveConnectedWifi=false;
        if (wifi.isAvailable()) {
            haveConnectedWifi = true;
        }

        boolean haveConnectedMobile=false;
        if (mobile.isAvailable())
        {
            haveConnectedMobile = true;
        }

        if((haveConnectedWifi==true || haveConnectedMobile==true) && haveConnectedActive==true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    static CustomProgressDialog customProgressDialog;
    static Dialog dialog;
    public static void showDialog(Context context){
        dialog = new Dialog(context, R.style.NewDialog);
        customProgressDialog = new CustomProgressDialog(context, dialog);
    }


    public static void dismissDialog(){
        dialog.dismiss();
    }




    static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context, int message_id) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false);

            }
            progressDialog.setMessage(context.getResources().getString(message_id));
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        DialogBox dialogBox;
    }

    public static void dismissProgressDialog(){
        if(progressDialog!=null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog=null;
    }

    public static void printLog(String data, String result) {
        Log.e(data, result);
    }

    public static Bitmap resize(File image){
        int REQUIRED_SIZE = 1000;

        String selectedImagePath = image.getPath();
        int memory_consumption_kb = Integer.parseInt(String.valueOf(image.length() / 1024));
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

    private  static boolean saveImageTemporarily(Bitmap bitmap){
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+ Retail.res.getString(R.string.temp_folder));

        if(!file.exists()){
            file.mkdirs();
        }

        file = new File(Environment.getExternalStorageDirectory()+File.separator+Retail.res.getString(R.string.temp_folder)+File.separator+"Temp.png");

        if(!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        try {
            return bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file)) ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

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


    public static TypedInput getTypedInput(String data) {
        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return in;
    }


    public static byte[] convertBitmap2ByteArray(Bitmap bm) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static String convertBitmap2Base64(Bitmap bm) throws Exception{
        return Base64.encodeToString(convertBitmap2ByteArray(bm), Base64.DEFAULT);
    }


    public static void logout(Activity activity) {
      /*  Prefs.with(activity).removeAll();

        CommonData.resetData();*/

        Intent in = new Intent();
        in.setClass(activity, RegisterScreen.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(in);
        activity.finish();
        activity.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

       File file = new File(CommonData.referPath);
        if(file.exists())
        file.delete();
    }

    public static boolean isReferImageSaved(){

        File file = new File(CommonData.referPath);
        return  file.exists();
    }

    public static void downloadReferImage(Context  context ,DownloadImageListener downloadImageListener){
        new ImageDownloader(context , CommonData.getUserData(context).getRefferCodepath()   , downloadImageListener ).execute() ;
    }

    public static void enterActivity(Activity activity, Class aClass) {
        enterActivity(activity, aClass, null);
    }


    public static void enterActivity(Activity activity, Class aClass,Bundle bundle) {
        try {
            Intent in = new Intent();
            Log.d("enterActivity", aClass.getClass() + "");
            in.setClass(activity, aClass);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            if (bundle != null) {
                in.putExtras(bundle);
            }
            activity.startActivity(in);
            activity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void exitActivity(Activity activity) {
        Utils.backPressed(activity);
    }



    public static void email(Context context, String emailTo, String emailCC,
                      String subject, String emailText, List<String> filePaths) {
        // need to "send multiple" to get more than one attachment
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[]{emailTo});
        emailIntent.putExtra(Intent.EXTRA_CC,
                new String[] { emailCC });

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailText));
        // has to be an ArrayList
        ArrayList<Uri> uris = new ArrayList<Uri>();
        // convert from paths to Android friendly Parcelable Uri's
        for (String file : filePaths) {
            File fileIn = new File(file);
            Uri u = Uri.fromFile(fileIn);
            uris.add(u);
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public static void writeImage(Bitmap b , String path ) {
        File android = new File(path);
        FileOutputStream data1 = null;
        try {
            data1 = new FileOutputStream(android);
            b.compress(Bitmap.CompressFormat.PNG, 100, data1) ;
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add(android.getAbsolutePath()) ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }
    /*<a href="http://www.google.com"+?jhgasfg="+new Random().nextInt()+">*/

    public static String getBody(){
        String body = "Kindly click on the link below to download the app and enter this reference code to become my friend in app." ;
        String data = "<html><body><h3><p>"+body+"</p></h3></body></html>";
        return  data;
    }

    public static void redirectToDashboard(Activity activity, String roleId) {
        Class dashboard=LoginActivity.class;
        Log.d("redirectToDashboard",roleId);
        switch (roleId){
            case ROLE_ID_CHARITY:
                dashboard =ActivityDashboardCharity.class ;
                Log.d("ROLE_ID_CHARITY:",roleId);
                enterActivity(activity, dashboard);
                break;

            case ROLE_ID_USER:
                dashboard = UserDashBoardActivity.class;
                Bundle bundle = new Bundle() ;
                bundle.putString("InvoiceUserType", "false");
                Log.d("ROLE_ID_USER:",roleId);
                enterActivity(activity, dashboard, bundle);
                break;


            case ROLE_ID_MERCHANT:
                dashboard = DashBoardMerchantActivity.class;
                Log.d("ROLE_ID_MERCHANT:",roleId);
                enterActivity(activity, dashboard);
                break;
        }

        activity.finish();
    }

    public static boolean isResponseValid(Object  modal_list) {
        ArrayList<ModalResponse> modalResponses  = (ArrayList<ModalResponse>) modal_list;
        if(modalResponses.size()==1 && !modalResponses.get(0).getResponseCode().equalsIgnoreCase(Retail.res.getString(R.string.success)))
        return false;
        return true;
    }

    public static int dpToPx( int dp) {
        DisplayMetrics displayMetrics = Retail.res.getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    public static int pxToDp(int px) {
        DisplayMetrics displayMetrics = Retail.res.getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static String getRandom(int i) {

        StringBuffer stringBuffer = new StringBuffer();

        Random random = new Random();

        for(int j = 0 ; j<i ; j++){
            stringBuffer.append(random.nextInt(9)+"");
        }

        return  stringBuffer.toString();
    }
}
