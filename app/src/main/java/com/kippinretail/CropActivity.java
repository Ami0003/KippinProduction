package com.kippinretail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.gesture.RotationGestureDetector;
import com.lyft.android.scissors.CropView;

//import com.lyft.android.scissors.CropView;
public class CropActivity extends SuperActivity implements View.OnClickListener,RotationGestureDetector.OnRotationGestureListener{
    RelativeLayout layout_ivsave,lalayout_ivBack;
    private CropView crop;;
    private RotationGestureDetector mRotationDetector;
    private Bitmap scaledBitmap;
    float prevAngle=0.0f;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        initUI();
        addListener();
        updateUI();
    }

    private void updateUI() {
        try {
          /*  byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);*/
            if(CommonUtility.bitmapToCrop!=null){
               scaledBitmap = Bitmap.createScaledBitmap(CommonUtility.bitmapToCrop , CommonUtility.getWidthOfScreen(this)-20,
                        CommonUtility.getHeightOfScreen(this),true);
                CommonUtility.bitmapToCrop = null;
                crop.setImageBitmap(scaledBitmap);
            }

        }catch(Exception ex){
            Log.e(ex.getMessage(), "");
        }
    }

    private void initUI() {
        mRotationDetector = new RotationGestureDetector(this);
        lalayout_ivBack = (RelativeLayout)findViewById(R.id.lalayout_ivBack);
        layout_ivsave = (RelativeLayout)findViewById(R.id.layout_ivsave);
        crop = (CropView) findViewById(R.id.crop_view);
        mRotationDetector = new RotationGestureDetector(this,crop);
     //   mRotationDetector = new RotationGestureDetector(this);
    }
    private void addListener() {
        lalayout_ivBack.setOnClickListener(this);
        layout_ivsave.setOnClickListener(this);
        crop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRotationDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.lalayout_ivBack:
                finish();
                break;
            case R.id.layout_ivsave:
                cropAndSaveBitmap();
                break;
        }
    }

    private void cropAndSaveBitmap() {
        try {
            Bitmap temp = crop.crop();
            // ADD BIT MAP TO INTENT
            CommonUtility.cropedBitmap = temp;
            Intent i = new Intent();
            setResult(RESULT_OK, i);
            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
            cropedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Intent i = new Intent();
            i.putExtra("cropbitmap", byteArray);
            setResult(RESULT_OK, i);*/
            finish();
        }catch(Exception ex){

        }
    }






    @Override
    public void onRotation(RotationGestureDetector rotationDetector,float lastUpdatedPrevAngle) {
      //  Log.e(scaledBitmap.getWidth() + "   //////   " +scaledBitmap.getHeight(), rotationDetector.getAngle() + "");
        Matrix matrix=new Matrix();
        Log.e("Rotation",lastUpdatedPrevAngle+"  "+rotationDetector.getAngle());
        matrix.postRotate(lastUpdatedPrevAngle+rotationDetector.getAngle());
        Bitmap rotatedBitmap= Bitmap.createBitmap(scaledBitmap, 0, 40, scaledBitmap.getWidth(), scaledBitmap.getHeight()-41, matrix, true);
        CommonUtility.bitmapToCrop = rotatedBitmap;
        crop.setImageBitmap(rotatedBitmap);

    }
}

