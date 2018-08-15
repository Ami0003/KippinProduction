package com.kippinretail;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.kippinretail.ApplicationuUlity.BarcodeUtil;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.config.Utils;

public class CardDetail extends SuperActivity {
    ImageView iv_delete,iv_barcode;
    TextView txt_merchantName,txt_barcode;
    private BarcodeUtil barcodeUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        initialiseUI();
        setUpListeners();;
        setUpUI();

    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        barcodeUtil = new BarcodeUtil(this);
        txt_merchantName = (TextView)findViewById(R.id.txt_merchantName);
        txt_barcode = (TextView)findViewById(R.id.txt_barcode);
        iv_delete = (ImageView)findViewById(R.id.iv_delete);
        iv_barcode = (ImageView)findViewById(R.id.iv_barcode);
    }

    @Override
    public void setUpUI() {
        super.setUpUI();
        generateActionBar(R.string.title_purchased_giftcards, true, true, false);;
        Intent i = getIntent();
        if(i!=null){
            txt_merchantName.setText(i.getStringExtra("merchantName"));
            txt_barcode.setText(i.getStringExtra("barcode"));
            Drawable drawable = barcodeUtil.generateBarcode(i.getStringExtra("barcode"));
          iv_barcode.setBackgroundDrawable(drawable);
        }
    }

    @Override
    public void setUpListeners() {
        super.setUpListeners();
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
         //       overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
//
            }
        });
    }
}
