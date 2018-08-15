package com.kippin.logout;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.kippin.kippin.R;
import com.kippin.storage.SharedPrefWriter;
import com.kippin.utils.Utility;
import com.pack.kippin.LoginScreen;


public class ActivityLogoutDialog implements View.OnClickListener{


    private ImageView ivCancel;
    private ImageView ivOk;
    private  Activity activity;
    private Dialog builder;

    public ActivityLogoutDialog(Activity context) {

        this.activity=context;
        init();
    }

    public void init() {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_logout, null, false);
        view. findViewById(R.id.ivOk).setOnClickListener(this);
        view. findViewById(R.id.ivCancel).setOnClickListener(this);
        builder = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        builder.setContentView(view);

    }

    public void show(){
        builder.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.ivOk){
            builder. dismiss();
            SharedPrefWriter.clear();
            Utility.startActivity(activity, LoginScreen.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.finish();
        }else if(id==R.id.ivCancel){
            builder.  dismiss();
        }
        /*switch (v.getId()){
            case R.id.ivOk:
                builder. dismiss();
                SharedPrefWriter.clear();
                Utility.startActivity(activity, LoginScreen.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.finish();
                break;
            case R.id.ivCancel:
                builder.  dismiss();
                break;
        }*/
    }
}