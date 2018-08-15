package com.kippinretail.scanning;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kippinretail.ActivityINputManualBarcode;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.R;
import com.kippinretail.config.Config;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    Button btn_manual, txt_close;
   // private Camera mCamera;
    boolean isFlashOn = false;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scanner);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        btn_manual = (Button) findViewById(R.id.btn_manual);
        txt_close = (Button) findViewById(R.id.txt_close);
        scannerView = new ZXingScannerView(this);
        contentFrame.addView(scannerView);
        btn_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScannerActivity.this, ActivityINputManualBarcode.class);
                startActivity(intent);
                finish();
            }
        });
        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.scanner = false;
                finish();
            }
        });

    }
   /* public void flashOn(View v){
        try{
            // mCamera = Camera.open();

            Camera.Parameters p = mCamera.getParameters();
            if(!isFlashOn){

                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(p);
                mCamera.startPreview();
                isFlashOn = true;
            }else{
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(p);
                isFlashOn = false;
            }
        }catch(Exception ex){
            android.util.Log.e("Message",ex.getMessage());
        }
    }*/
    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        //Call back data to main activity
        Log.e("FORMAT:", "" + rawResult.getBarcodeFormat().toString());
        Log.e("CONTENT:", "" + rawResult.getText());
        Config.FORMAT = rawResult.getBarcodeFormat().toString();
        Config.CONTENT = rawResult.getText();
        Config.scanner = true;
        Intent intent = new Intent();
        intent.putExtra(Config.FORMAT, rawResult.getBarcodeFormat().toString());
        intent.putExtra(Config.CONTENT, rawResult.getText());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
