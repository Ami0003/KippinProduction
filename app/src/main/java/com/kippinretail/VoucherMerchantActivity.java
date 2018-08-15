package com.kippinretail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.AdapterForVouchers;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.InternalStorageContentProvider;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.CommonDialog.UploadImageDialog;
import com.kippinretail.Modal.VoucherMerchant.VoucherData;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;

//import eu.janmuller.android.simplecropimage.CropImage;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 3/23/2016.
 */

public class VoucherMerchantActivity extends SuperActivity implements View.OnClickListener {


    ImageView uploadNewVoucherButton;
    private List<VoucherData> voucherList;
    ListView voucher_list;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final String TAG = "VoucherMerchant";
    private File mFileTemp;
    private String imagePath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_merchant);
        Initlization();
        updateUI();
    }


    private void Initlization() {
        generateActionBar(R.string.title_user_voucher_detail, true, true, false);
        uploadNewVoucherButton = (ImageView) findViewById(R.id.uploadNewVoucherButton);
        uploadNewVoucherButton.setOnClickListener(this);
        voucher_list = (ListView) findViewById(R.id.voucher_list);

        // Image File local
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(),
                    TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
    }

    private void updateUI() {
        getVoucherList();
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getVoucherList() {
        String merchantId = "" + CommonData.getUserData(VoucherMerchantActivity.this).getId();
        LoadingBox.showLoadingDialog(VoucherMerchantActivity.this, "");
        RestClient.getApiServiceForPojo().QueryToVouchers(merchantId,merchantId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement loginData, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.i("Tag", "Request data " + new Gson().toJson(loginData));
                Log.e("URL",response.getUrl());
                Gson gson = new Gson();
                String jsonOutput = loginData.toString();
                Type listType = new TypeToken<List<VoucherData>>() {
                }.getType();

                voucherList = (List<VoucherData>) gson.fromJson(jsonOutput, listType);
                if (voucherList.get(0).getResponseCode() != 5) {
                    AdapterForVouchers adap = new AdapterForVouchers(VoucherMerchantActivity.this, voucherList);
                    voucher_list.setAdapter(adap);
                } else {
                    CommonDialog.With(VoucherMerchantActivity.this).Show(voucherList.get(0).getResponseMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Vouchers Get Failure => ", " = " + error.getMessage());
                Log.e("URL",error.getUrl());
                ErrorCodes.checkCode(VoucherMerchantActivity.this, error);
                MessageDialog.showDialog(VoucherMerchantActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);

            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadNewVoucherButton:

                CommonUtility.VoucherBitMap = null;
                UploadImageDialog.showUploadImageDialog(VoucherMerchantActivity.this,getApplicationContext());
                break;

        }
    }

    private void backPressed() {
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
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
                        CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        callUploadVoucherIntent();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error while creating temp file" + e);
                }
                break;
            case CommonUtility.REQUEST_CODE_TAKE_PICTURE:
                try{

                        getContentResolver().notifyChange(UploadImageDialog.mImageUri, null);
                        imagePath = new File(UploadImageDialog.mImageUri.getPath()).getAbsolutePath();

                        Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), UploadImageDialog.mImageUri);
                    // ======================= CODE TO ROTATE ========================
                        ExifInterface exifInterface = new ExifInterface(Uri.parse(imagePath).getPath());
                        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL);
                        float angle = 0;
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                angle = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                angle = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                angle = 270;
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default:
                                break;
                        }


                        Bitmap oriented = CommonUtility.fixOrientation(bm, angle);
                            CommonUtility.VoucherBitMap = oriented;
                            CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);

                            callUploadVoucherIntent();
                    UploadImageDialog.mImageUri = null;

                }catch(Exception ex){
                    Log.e(TAG, "Error while creating temp file" + ex.getMessage());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void callUploadVoucherIntent() {
        Intent in = new Intent();
        in.setClass(VoucherMerchantActivity.this, UploadVoucherActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      switch(requestCode){
          case CommonUtility.MY_PERMISSION_ACCESS_CAMERA:
          case CommonUtility.MY_PERMISSION_ACCESS_STORAGE:
                UploadImageDialog.onRequestPermissionsResult(requestCode,permissions,grantResults);
      }
    }
}
