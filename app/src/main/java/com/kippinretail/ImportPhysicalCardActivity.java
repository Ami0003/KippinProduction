package com.kippinretail;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.kippinretail.config.Utils;

public class ImportPhysicalCardActivity extends Activity implements View.OnClickListener {
    private RelativeLayout backArrowLayout;
    private ListView listFolder;
    private Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_physical_card);
        initilization();
    }

    private void initilization() {
        backArrowLayout = (RelativeLayout) findViewById(R.id.backArrowLayout);
        listFolder = (ListView) findViewById(R.id.listFolder);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        backArrowLayout.setOnClickListener(this);
        btnUpload.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArrowLayout:
                Utils.backPressed(this);
                break;
            case R.id.btnUpload:
                showDialog();
                break;

        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this,
                R.style.Theme_AppCompat_Translucent);
        dialog.setContentView(R.layout.dialog_uplaod_image);
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
//        TextView textForMessage = (TextView)dialog.findViewById(R.id.textForMessage);
//        textForMessage.setText("=============");
//               Button btnOk =(Button) dialog.findViewById(R.id.btnOk);
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//            }
//        });

        dialog.show();


    }
}
