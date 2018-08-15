package com.kippinretail;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.UploadImageDialog;
import com.kippinretail.R;
import com.kippinretail.config.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadPhysicalCardActivity extends SuperActivity implements View.OnClickListener{

    private boolean btnFrontImageClicked = false;
    private boolean btnbackImageClicked = false;
    private Bitmap bFront, bBack;
    CircleImageView iv_cardIcon;
    private Button btnFrontImage, btnbackImage;
    private String profileImageBase64;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_physical_card);
        initUI();
        updateUI();
        addListener();


    }
    private void initUI(){
        generateActionBar(R.string.tile_upload_physical_card, true, true, false);
        iv_cardIcon = (CircleImageView) findViewById(R.id.iv_cardIcon);
        btnFrontImage = (Button) findViewById(R.id.btnFrontImage);
        btnbackImage = (Button) findViewById(R.id.btnbackImage);
    }
    private void updateUI(){

    }
    private void addListener(){
        iv_cardIcon.setOnClickListener(this);
        btnFrontImage.setOnClickListener(this);
        btnbackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_cardIcon:
                UploadImageDialog.showUploadImageDialog(UploadPhysicalCardActivity.this);
                break;
            case R.id.btnFrontImage:
                btnFrontImageClicked = true;
                btnbackImageClicked = false;
                UploadImageDialog.showUploadImageDialog(UploadPhysicalCardActivity.this);
                break;
            case R.id.btnbackImage:
                btnFrontImageClicked = false;
                btnbackImageClicked = true;
                UploadImageDialog.showUploadImageDialog(UploadPhysicalCardActivity.this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CommonUtility.REQUEST_CODE_GALLERY:
                try {
                    String imgPath = null;
                    if(data!=null){
                        imgPath  = data.getStringExtra("imagePath");
                    }
                    if(imgPath!=null){
                        CommonUtility.VoucherBitMap = BitmapFactory.decodeFile(imgPath);
                        profileImageBase64 = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(CommonUtility.VoucherBitMap, Utils.dpToPx(UploadPhysicalCardActivity.this, 150),Utils.dpToPx(UploadPhysicalCardActivity.this,150),false);
                        iv_cardIcon.setImageBitmap(scaledBitmap);
                        cropImage(Uri.parse(imgPath));
                        if(btnFrontImageClicked){
                            changeBackgroundColor(btnFrontImage, getResources().getColor(R.color.frontImageButtonClick));
                        }else if(btnbackImageClicked){
                            changeBackgroundColor(btnbackImage, getResources().getColor(R.color.frontImageButtonClick));
                        }
                    }
                } catch (Exception e) {

                }
                break;
            case CommonUtility.REQUEST_CODE_TAKE_PICTURE:
                try{
                    if(data!=null){
                        CommonUtility.VoucherBitMap = (Bitmap) data.getExtras().get("data");
                        CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(CommonUtility.VoucherBitMap,Utils.dpToPx(UploadPhysicalCardActivity.this,150),Utils.dpToPx(UploadPhysicalCardActivity.this,150),false);
                        iv_cardIcon.setImageBitmap(scaledBitmap);
                        if(btnFrontImageClicked){
                            changeBackgroundColor(btnFrontImage, getResources().getColor(R.color.frontImageButtonClick));
                        }else if(btnbackImageClicked){
                            changeBackgroundColor(btnbackImage, getResources().getColor(R.color.frontImageButtonClick));
                        }
                    }
                }catch(Exception ex){
                    Bundle extras = data.getExtras();
                    Bitmap thePic = extras.getParcelable("data");
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(thePic, Utils.dpToPx(UploadPhysicalCardActivity.this, 150), Utils.dpToPx(UploadPhysicalCardActivity.this, 150), false);
                    iv_cardIcon.setImageBitmap(scaledBitmap);
                }
                break;

            case CommonUtility.PIC_CROP:
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void changeBackgroundColor(final View view, final int color) {

                view.setBackgroundColor(color);

    }

    private void cropImage(Uri picUri){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, CommonUtility.PIC_CROP);
        }catch(ActivityNotFoundException ex){

            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }





}
