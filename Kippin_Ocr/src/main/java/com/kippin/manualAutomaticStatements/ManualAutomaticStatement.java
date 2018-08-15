package com.kippin.manualAutomaticStatements;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.automaticStatement.ActivityAutomaticStatement;
import com.automaticStatement.AutomaticBankStatementLoginNew;
import com.kippin.kippin.R;
import com.kippin.storage.SharedPref;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.TemplateData;

public class ManualAutomaticStatement extends SuperActivity {


    ImageView ivManualStatement, ivAutomaticStatement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manual_automatic_statement);

        generateActionBar(R.string.select_type, true, true);

        initialiseUI();
        setUpListeners();
    }

    //@OnClick(R.id.ivManualStatement)
    public void onManualClick(View v) {
//        Utility.startActivity(ManualAutomaticStatement.this, ActivityConnection1.class, true);
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url.URL_ACTIVITY_CONNECTION + "/" + SharedPref.getString(SharedPref.USERNAME, "") + "/" + SharedPref.getString(SharedPref.PASSWORD, "")));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

//
//
//   // @OnClick(R.id.ivAutomaticStatement)
//    public void onAutomaticClick(View v)
//    {
//        WSUtils.hitServiceGet(ManualAutomaticStatement.this, Url.getCheckYodleeAccApi(), new ArrayListPost(), TemplateData.class, new WSInterface() {
//            @Override
//            public void onResult(int requestCode, TemplateData data) {
//                if(data.data.toLowerCase().equalsIgnoreCase("true")){
//                    Utility.startActivity(ManualAutomaticStatement.this,ActivityAutomaticStatement.class,true);
//                }else if(data.data.toLowerCase().eq   ualsIgnoreCase("false")){
//                    Utility.startActivity(ManualAutomaticStatement.this,ActivityYodleeLogin.class,true);
//                }
//            }
//        }) ;
//
//    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        ivManualStatement = (ImageView) findViewById(R.id.ivManualStatement);
        ivAutomaticStatement = (ImageView) findViewById(R.id.ivAutomaticStatement);
    }

    @Override
    public void setUpListeners() {
        ivManualStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    // http://52.39.97.124:8080/Account/Web?uname=gagan&password=qwerty
                    // Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url.URL_ACTIVITY_CONNECTION + "/" + SharedPref.getString(SharedPref.USERNAME, "") + "/" + SharedPref.getString(SharedPref.PASSWORD, "")));
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url.URL_ACTIVITY_MANUAL_CONNECTION1 + "uname=" + SharedPref.getString(SharedPref.USERNAME, "") + "&password=" + SharedPref.getString(SharedPref.PASSWORD, "")));
                    startActivity(myIntent);
                    //  Utility.startActivity(ManualAutomaticStatement.this, ActivityConnection.class, true);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ManualAutomaticStatement.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        ivAutomaticStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WSUtils.hitServiceGet(ManualAutomaticStatement.this, Url.getCheckYodleeAccApi(), new ArrayListPost(), TemplateData.class, new WSInterface() {
                    @Override
                    public void onResult(int requestCode, TemplateData data) {
                        if (data.data.toLowerCase().equalsIgnoreCase("true")) {
                            Log.e("ivAutomaticStatement", "ivAutomaticStatement");
                            Utility.startActivity(ManualAutomaticStatement.this, ActivityAutomaticStatement.class, true);

                        } else if (data.data.toLowerCase().equalsIgnoreCase("false")) {
                            Utility.startActivity(ManualAutomaticStatement.this, AutomaticBankStatementLoginNew.class, true);
                        }
                    }
                });
            }
        });
    }
}
