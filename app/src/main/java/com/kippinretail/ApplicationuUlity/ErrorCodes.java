package com.kippinretail.ApplicationuUlity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.R;

import org.json.JSONObject;

import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;


public class ErrorCodes {


    private static String errorMessage = "";

    /**
     * @param context
     * @param error
     */
    public static void checkCode(final Activity context, RetrofitError error) {
        try {
            errorMessage = "";


            Log.v("error bundle", error.toString() + "");

            switch (error.getKind()) {
                case NETWORK:
                    errorMessage = "No internet connection. Please try again later.";

                    break;
                case CONVERSION:
                    errorMessage = "An error was procured while parsing.";
                    break;
                case HTTP: {
                    errorMessage = "Application server could not respond. Please try later.";

                    if (error.getResponse().getStatus() == ApiResponseFlags.UNAUTHORIZED.getOrdinal()) {
                        String str = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                        JSONObject json = new JSONObject(str);
                        errorMessage = json.getString("message");
                        alertPopup(context, "", errorMessage);
                        errorMessage = "";

                    } else {
                        try {
                            String str = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                            JSONObject json = new JSONObject(str);
                            errorMessage = json.getString("message");
                        } catch (Exception e) {
                            if (errorMessage.isEmpty())
                                errorMessage = "Something went wrong. Please try later";
                        }
                    }
                }
                break;

                case UNEXPECTED:
                    errorMessage = "An unexpected error occurred. Please try later.";
                    break;


            }

            if (!errorMessage.isEmpty())
                CommonDialog.With(context).Show(errorMessage);


        } catch (Exception e) {
            CommonDialog.With(context).Show("Something went wrong. Please try later");

        }
    }

    public static void AppExit(Activity context) {

        Intent i = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        context.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    public static void alertPopup(final Activity activity, String title, String message) {
        try {
            if ("".equalsIgnoreCase(title)) {
                title = "Alert";
            }
            final Dialog dialog = new Dialog(activity,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_custom_msg);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            //TextView textHead = (TextView) dialog.findViewById(R.id.textHead);
            TextView textMessage = (TextView) dialog
                    .findViewById(R.id.textForMessage);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            //textHead.setText(title);
            textMessage.setText(message);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    //AppExit(activity);
                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}