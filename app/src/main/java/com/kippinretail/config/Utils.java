package com.kippinretail.config;

import android.app.Activity;
import android.content.Intent;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.KippinInvoice.Invoice_LoginScreen;
import com.kippinretail.LoginActivity;
import com.kippinretail.R;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * Created by sandeep.saini on 3/18/2016.
 */
public class Utils
{
    public static void backPressed(Activity mcontext)
    {

        mcontext.finish();
        mcontext.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

    }
    public static void hideKeyboard(Activity context)
    {
        context.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    public static boolean comaperDates(String fromDate , String toDate){
        boolean flag = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "MMM dd, yyyy");
            Date _fromDate = sdf.parse(fromDate);
            Date _toDate = sdf.parse(toDate);
            flag =   _fromDate.before(_toDate);
        }catch(Exception ex)
        {

        }
        return flag;
    }

    public static TypedInput getTypedInput(Object convertToJson){
        Gson gson = new Gson();
        String JSON = gson.toJson(convertToJson);
        Log.d("getTypedInput",JSON);
        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", JSON.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return in;
    }
    public static int dpToPx(Activity context,int dps) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);

        return pixels;
    }
    public static void logoutUser(Activity context){
        Intent in = new Intent();
        in.setClass(context, LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(in);
        context.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
        context.finish();
    }

    public static void logoutInvoice(Activity context){
        Intent in = new Intent();
        in.setClass(context, Invoice_LoginScreen.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(in);
        context.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
        context.finish();
    }
}
