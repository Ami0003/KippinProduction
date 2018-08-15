package com.pack.kippin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.kippin.kippin.R;
import com.lyft.android.scissors.CropView;

import java.io.ByteArrayOutputStream;

//import com.lyft.android.scissors.CropView;
public class CropActivity extends AppCompatActivity implements View.OnClickListener{
    
    RelativeLayout layout_ivsave,lalayout_ivBack;
    private CropView crop;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        initUI();
        addListener();
        updateUI();
    }

    private void updateUI() {
        try {
            byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            crop.setImageBitmap(bmp);
        }catch(Exception ex){
            Log.e(ex.getMessage(), "");
        }
    }

    private void initUI() {
        lalayout_ivBack = (RelativeLayout)findViewById(R.id.lalayout_ivBack);
        layout_ivsave = (RelativeLayout)findViewById(R.id.layout_ivsave);
        crop = (CropView) findViewById(R.id.crop_view);
    }
    private void addListener() {
        lalayout_ivBack.setOnClickListener(this);
        layout_ivsave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.lalayout_ivBack){
            finish();
        }else if (v.getId() == R.id.layout_ivsave) {

            cropAndSaveBitmap();
        }

    }

    private void cropAndSaveBitmap() {
        Bitmap cropedBitmap = crop.crop();
        // ADD BIT MAP TO INTENT
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        cropedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Intent i = new Intent();
        i.putExtra("cropbitmap",byteArray);
        setResult(RESULT_OK,i);
        finish();
    }
}
