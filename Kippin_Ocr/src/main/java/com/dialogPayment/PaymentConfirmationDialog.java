package com.dialogPayment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.kippin.app.Kippin;
import com.kippin.kippin.R;
import com.kippin.utils.Utility;
import com.pack.kippin.PaymentDetailsClass;

/**
 * Created by gaganpreet.singh on 4/18/2016.
 */
public class PaymentConfirmationDialog extends AlertDialog {

    Builder builder = null;
    private Activity activity;

    public PaymentConfirmationDialog(Activity context) {
        super(context);
        this.activity=context;
        builder = new Builder(context) ;
        builder.setMessage(R.string.payment_dialog_message);
        builder.setTitle(R.string.kippin) ;
        builder.setNegativeButton(R.string.trial, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToLogin();
            }
        });
        builder.setPositiveButton(R.string.payment, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Bundle bundle = new Bundle();
                bundle.putBoolean("REDIRECT_TO_LOGIN",true) ;
                Utility.startActivity(activity, PaymentDetailsClass.class,bundle,true);
            }
        });
    }

    @Override
    public void show() {
        builder.show();
    }

    public void goToLogin(){
        Utility.logout(activity,false);
    }

}
