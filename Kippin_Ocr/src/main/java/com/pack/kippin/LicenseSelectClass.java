package com.pack.kippin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.Utility;


import butterknife.ButterKnife;


/**
 * Created by dilip.singh on 1/22/2016.
 */
public class LicenseSelectClass extends SuperActivity implements View.OnClickListener {

   // @Bind(R.id.monthly_linear)
    LinearLayout linearLayoutMonthly;

//    @Bind(R.id.quartly_linear)
    LinearLayout linearLayoutQuartely;

   // @Bind(R.id.yearly_linear)
    LinearLayout linearLayoutYearly;

    //@Bind(R.id.next_btn)
    ImageView imageViewNextButton;
    Bundle nBundle;
    private String mLicense="",mUserId,mCurrency;
    DialogBox dialogBox;


    @Override
    public void initialiseUI() {
        super.initialiseUI();
        linearLayoutMonthly =(LinearLayout)findViewById(R.id.monthly_linear);
        linearLayoutQuartely =(LinearLayout)findViewById(R.id.quartly_linear);
        linearLayoutYearly =(LinearLayout)findViewById(R.id.yearly_linear);
        imageViewNextButton =(ImageView)findViewById(R.id.next_btn);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.checkCrashTracker(LicenseSelectClass.this);
        setContentView(R.layout.layout_license_type);
        initialiseUI();
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUserId = bundle.getString("USERID");
            mCurrency = bundle.getString("CURRENCY");
        }else{
            mUserId = Singleton.getUser().getId();
            mCurrency = Singleton.getUser().getCurrencyId();
        }



        generateActionBar(R.string.select_licens_type, true, false);

    }
    //@OnClick({R.id.monthly_linear,R.id.quartly_linear,R.id.yearly_linear})
    public void onLincenseClick(View view){
        if (view.getId()==R.id.monthly_linear){

            linearLayoutMonthly.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            linearLayoutQuartely.setBackgroundColor(Color.TRANSPARENT);
            linearLayoutYearly.setBackgroundColor(Color.TRANSPARENT);
            mLicense="30";

        }
        else if(view.getId()==R.id.quartly_linear){

            linearLayoutQuartely.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            linearLayoutMonthly.setBackgroundColor(Color.TRANSPARENT);
            linearLayoutYearly.setBackgroundColor(Color.TRANSPARENT);
            mLicense = "50";

        }
        else if(view.getId()==R.id.yearly_linear){

            linearLayoutYearly.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            linearLayoutQuartely.setBackgroundColor(Color.TRANSPARENT);
            linearLayoutMonthly.setBackgroundColor(Color.TRANSPARENT);
            mLicense = "100";

        }
        else {
                dialogBox = new DialogBox(getResources().getString(R.string.plz_select_license),LicenseSelectClass.this);
        }

        generateActionBar(R.string.select_licens_type, true, false);

    }

   // @OnClick(R.id.next_btn)
    public void onNextClick(View v){
        if (mLicense.isEmpty()){
            dialogBox = new DialogBox(getResources().getString(R.string.plz_select_license),LicenseSelectClass.this);
        }
        else {
            Intent intent = new Intent(LicenseSelectClass.this,PaymentDetailsClass.class);
            nBundle = new Bundle();
            nBundle.putString("LICENSE",mLicense);
            nBundle.putString("USERID",mUserId);
            nBundle.putString("CURRENCY", mCurrency);
            intent.putExtras(nBundle);
            startActivity(intent);
            overridePendingTransition(R.anim.push_in_to_left,
                    R.anim.push_in_to_right);


            Utility.licenseSelectClass = this;

        }


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        LicenseSelectClass.this.overridePendingTransition(R.anim.push_out_to_left,
                R.anim.push_out_to_right);
    }


    @Override
    public void setUpListeners() {
        imageViewNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLicense.isEmpty()){
                    dialogBox = new DialogBox(getResources().getString(R.string.plz_select_license),LicenseSelectClass.this);
                }
                else {
                    Intent intent = new Intent(LicenseSelectClass.this,PaymentDetailsClass.class);
                    nBundle = new Bundle();
                    nBundle.putString("LICENSE",mLicense);
                    nBundle.putString("USERID",mUserId);
                    nBundle.putString("CURRENCY", mCurrency);
                    intent.putExtras(nBundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_in_to_left,
                            R.anim.push_in_to_right);


                    Utility.licenseSelectClass = LicenseSelectClass.this;

                }
            }
        });
        linearLayoutMonthly.setOnClickListener(this);
        linearLayoutQuartely.setOnClickListener(this);
        linearLayoutYearly.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.monthly_linear){

            linearLayoutMonthly.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            linearLayoutQuartely.setBackgroundColor(Color.TRANSPARENT);
            linearLayoutYearly.setBackgroundColor(Color.TRANSPARENT);
            mLicense="30";

        }
        else if(view.getId()==R.id.quartly_linear){

            linearLayoutQuartely.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            linearLayoutMonthly.setBackgroundColor(Color.TRANSPARENT);
            linearLayoutYearly.setBackgroundColor(Color.TRANSPARENT);
            mLicense = "50";

        }
        else if(view.getId()==R.id.yearly_linear){

            linearLayoutYearly.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            linearLayoutQuartely.setBackgroundColor(Color.TRANSPARENT);
            linearLayoutMonthly.setBackgroundColor(Color.TRANSPARENT);
            mLicense = "100";

        }
        else {
            dialogBox = new DialogBox(getResources().getString(R.string.plz_select_license),LicenseSelectClass.this);
        }

        generateActionBar(R.string.select_licens_type, true, false);
    }
}
