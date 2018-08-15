package com.kippinretail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.CommonDialog.UploadImageDialog;
import com.kippinretail.Modal.LoginData.LoginDataClass;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 4/5/2016.
 */
public class MerchantUpdatedActivity extends SuperActivity implements View.OnClickListener {
    EditText businessNameEditText, descriptionEditText,
            phoneNumberEditText, websiteEditText, countryCodeEditText,businessNumberEditText;
    EditText ed_firstName,ed_lastName , ed_countryCode, ed_areaCode,ed_phoneNumber,ed_city;
    private Button updateButton;
    private Button changePasswordButton;
    private String currentUserId= null;
    private String encodedString = "";
    ImageView profileImage;
    private StringBuilder userPhoneNumber;
    private String imagePath="";
  final  Bitmap bitmap_profileImage = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_update);
        Initlization();
        updateUI();
        addListener();
    }

    private void Initlization() {
        generateActionBar(R.string.title_merchant_profile, true, false, false);
        generateRightText("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        currentUserId = String.valueOf(CommonData.getUserData(this).getId());
        businessNameEditText = (EditText) findViewById(R.id.businessNameEditText);
        ed_city = (EditText) findViewById(R.id.ed_city);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        businessNumberEditText = (EditText) findViewById(R.id.businessNumberEditText);
        websiteEditText = (EditText) findViewById(R.id.websiteEditText);
        countryCodeEditText = (EditText) findViewById(R.id.countryCodeEditText);
        ed_firstName = (EditText) findViewById(R.id.ed_firstName);
        ed_lastName = (EditText) findViewById(R.id.ed_lastName);
        ed_countryCode = (EditText) findViewById(R.id.ed_countryCode);
        ed_areaCode = (EditText) findViewById(R.id.ed_areaCode);
        ed_phoneNumber = (EditText) findViewById(R.id.ed_phoneNumber);
        updateButton = (Button) findViewById(R.id.updateButton);
        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        profileImage = (ImageView)findViewById(R.id.profileImage);

    }
    private void updateUI(){
        LoginDataClass login = CommonData.getUserData(this);
        ed_firstName.setText(CommonData.getUserData(this).getFirstname());
        ed_lastName.setText(CommonData.getUserData(this).getLastname());
        businessNameEditText.setText(CommonData.getUserData(this).getBusinessName());
        descriptionEditText.setText(CommonData.getUserData(this).getBusinessdescription());
        websiteEditText.setText(CommonData.getUserData(this).getWebsite());

        String countryCode_bussinessPhoneNumber =CommonData.getUserData(this).getBusinessNumber();
        String [] ar = countryCode_bussinessPhoneNumber.split("-");
        if(ar.length>=3){
            ed_countryCode.setText(ar[0]);
            ed_countryCode.setText(ar[0]);
            ed_areaCode.setText(ar[1]);
            ed_areaCode.setText(ar[1]);
            ed_phoneNumber.setText(ar[2]);
            ed_phoneNumber.setText(ar[2]);

        }
        businessNumberEditText.setText(CommonData.getUserData(this).getBusinessPhoneNumber());
         imagePath = CommonData.getUserData(this).getProfileImage();
    if(imagePath.length()<200 ) {
        if(!imagePath.equals("")){
            Picasso.with(MerchantUpdatedActivity.this).load(imagePath).placeholder(R.drawable.icon_placeholder).
                    resize(CommonUtility.dpToPx(MerchantUpdatedActivity.this, 108), CommonUtility.dpToPx(MerchantUpdatedActivity.this, 135))
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            profileImage.setImageBitmap(bitmap);
                            try {
                                imagePath = CommonUtility.encodeTobase64(bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }

    }else{
        encodedString  = imagePath;
        byte[] decodedString = Base64.decode(imagePath, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        String path = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, decodedByte));
        profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(path,CommonUtility.dpToPx(this,108),
                CommonUtility.dpToPx(this,135)));
    }

        ed_city.setText(CommonData.getUserData(this).getCity());
    }
    private void addListener(){
        updateButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Log.e("onClick(View v)", "onClick(View v)");
        switch (v.getId())
        {
            case R.id.updateButton:
                if (ed_firstName.getText().toString().length()!=0 && ed_lastName.getText().toString().length()!=0 &&
                        businessNameEditText.getText().toString().length() != 0 && descriptionEditText.getText().toString().length() != 0
                        && ed_countryCode.getText().toString().length()!=0 && ed_areaCode.getText().toString().length()!=0
                        && ed_phoneNumber.getText().toString().length()!=0 && ed_city.getText().toString().length()!=0 &&
                        businessNumberEditText.getText().toString().length()!=0/*&& websiteEditText.getText().toString().length() != 0*/
                         ) {
                    UpdateMerchant();

                } else {
                    CommonDialog.With(MerchantUpdatedActivity.this).Show("Please fill the empty fields.");
                }
                break;
            case R.id.changePasswordButton:
                Intent i = new Intent();
                i.setClass(this , ChangePasswordActivity.class);
                startActivity(i);
                //overridePendingTransition(R.anim.animation_from_right,R.anim.animation_from_left);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(this);
    }

    private void updatePref() {
        LoginDataClass obj = CommonData.getUserData(this);
        obj.setProfileImage(imagePath);
        obj.setFirstname(ed_firstName.getText().toString());
        obj.setLastname(ed_lastName.getText().toString());
        obj.setBusinessName(getText(businessNameEditText));
        obj.setBusinessNumber(userPhoneNumber.toString());
        obj.setBusinessdescription(getText(descriptionEditText));
        obj.setBusinessPhoneNumber(businessNumberEditText.getText().toString()); // In bussines phone number i am getting bussiness number
        obj.setWebsite(getText(websiteEditText));
        CommonData.saveUserData(this , obj);
    }
    private void UpdateMerchant() {
        LoadingBox.showLoadingDialog(MerchantUpdatedActivity.this, "Updating...");
        userPhoneNumber = new StringBuilder();
        userPhoneNumber.append(ed_countryCode.getText().toString());
        userPhoneNumber.append("-");
        userPhoneNumber.append(ed_areaCode.getText().toString());
        userPhoneNumber.append("-");
        userPhoneNumber.append(ed_phoneNumber.getText().toString());
            HashMap<String , String> jsonBody = new HashMap<String,String>();
            jsonBody.put("ProfileImage",imagePath);
            jsonBody.put("Firstname",ed_firstName.getText().toString());
            jsonBody.put("Lastname",ed_lastName.getText().toString());
            jsonBody.put("Id",currentUserId);
            jsonBody.put("BusinessName",getText(businessNameEditText) );
            jsonBody.put("Businessdescription", getText(descriptionEditText));
            jsonBody.put("BusinessNumber", userPhoneNumber.toString());
            jsonBody.put("BusinessPhoneNumber",businessNumberEditText.getText().toString());
            jsonBody.put("Website", getText(websiteEditText));
            jsonBody.put("City", ed_city.getText().toString());
            jsonBody.put("RoleId",CommonUtility.merchantRoleId );
        RestClient.getApiServiceForPojo().UpdateMerchantProfile(jsonBody, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        Log.e("Output  ", jsonElement.toString());
                        Log.e("URL  ", response.getUrl());
                        JsonObject jsonObj = jsonElement.getAsJsonObject();
                        String strObj = jsonObj.toString();
                        try {

                            Gson gson = new Gson() ;
                            LoginDataClass loginDataClass  =gson.fromJson(jsonElement  ,LoginDataClass.class) ;
                            CommonData.saveUserData(MerchantUpdatedActivity.this , loginDataClass);

                            JSONObject OBJ = new JSONObject(strObj);
                            String ResponseMessage = OBJ.getString("ResponseMessage");
                           if(ResponseMessage.equals("Success")){
                               MessageDialog.showDialog(MerchantUpdatedActivity.this,"Profile has been successfully updated",true);
                                updatePref();
                           }else{
                               MessageDialog.showDialog(MerchantUpdatedActivity.this,"Fail to Update Profile Try Again Later..",true);
                           }
                        }catch(Exception ex){

                        }
                        LoadingBox.dismissLoadingDialog();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("URL  ", error.getUrl());
                        LoadingBox.dismissLoadingDialog();

                        MessageDialog.showFailureDialog(MerchantUpdatedActivity.this);
                    }
                });
    }



    private boolean validateUpdateProfileForm(){

        return false;
    }

    private void resetForm(){
        businessNameEditText.setText("");
        descriptionEditText.setText("");
        countryCodeEditText.setText("");
        phoneNumberEditText.setText("");
        businessNumberEditText.setText("");
        websiteEditText.setText("");

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
                       imagePath =  encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        profileImage.setImageURI(CommonUtility.getImageUri(this, CommonUtility.VoucherBitMap));
                        profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath, CommonUtility.dpToPx(this, 108),
                                CommonUtility.dpToPx(this, 135)));
                    }
                } catch (Exception e) {

                }
                break;
            case CommonUtility.REQUEST_CODE_TAKE_PICTURE:
                try{
                        getContentResolver().notifyChange(UploadImageDialog.mImageUri, null);
                    imagePath = new File(UploadImageDialog.mImageUri.getPath()).getAbsolutePath();
                        Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), UploadImageDialog.mImageUri);;

                    ExifInterface exifInterface = new ExifInterface(UploadImageDialog.mImageUri.getPath());
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
                    String orientedImagePath = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, oriented));
                    imagePath = encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                    profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(orientedImagePath,CommonUtility.dpToPx(this,108),
                            CommonUtility.dpToPx(this,135)));
                    UploadImageDialog.mImageUri = null;

                    UploadImageDialog.mImageUri = null;

                }catch(Exception ex){

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadLogo(View v){
        UploadImageDialog.showUploadImageDialog(this,getApplicationContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            switch (requestCode) {
                case CommonUtility.MY_PERMISSION_ACCESS_CAMERA:
                case CommonUtility.MY_PERMISSION_ACCESS_STORAGE:
                    UploadImageDialog.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    break;
                case CommonUtility.MY_PERMISSION_ACCESS_LOCATION:
                    break;
            }
        }catch (Exception ex){
            Log.e("",ex.getMessage());
        }
    }
}
