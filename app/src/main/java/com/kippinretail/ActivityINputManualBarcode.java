package com.kippinretail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.config.Config;

public class ActivityINputManualBarcode extends SuperActivity implements AdapterView.OnClickListener {

    private RelativeLayout lalayout_ivBack;
    TextView txt_save,tvTopbarTitle;
    EditText ed_cardNo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_input_manual_barcode);
        initialiseUI();
        setUpUI();
        setUpListeners();
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        lalayout_ivBack = (RelativeLayout)findViewById(R.id.lalayout_ivBack);
        txt_save = (TextView) findViewById(R.id.txt_save);
        tvTopbarTitle = (TextView) findViewById(R.id.tvTopbarTitle);
        ed_cardNo = (EditText)findViewById(R.id.ed_cardNo);
    }

    @Override
    public void setUpUI() {
        super.setUpUI();
        tvTopbarTitle.setText("Manual Input");
    }

    @Override
    public void setUpListeners() {
        super.setUpListeners();
        lalayout_ivBack.setOnClickListener(this);
        txt_save.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.lalayout_ivBack:
                Config.scanner=false;
                Config.CONTENT="";
                Intent i = new Intent();
                i.putExtra("cardNo","");
               // setResult(RESULT_CANCELED, i);
                finish();
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                break;
            case R.id.txt_save:
                Config.scanner=true;
                Config.CONTENT=ed_cardNo.getText().toString();
                Intent i1 = new Intent();
                i1.putExtra("cardNo",ed_cardNo.getText().toString());
               // setResult(RESULT_OK, i1);
                finish();
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                break;
        }
    }
}
