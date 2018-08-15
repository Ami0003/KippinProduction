package com.kippinretail;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.CommonDialog.UploadImageDialog;
import com.kippinretail.Modal.LoginData.LoginDataClass;
import com.kippinretail.Modal.ModalResponse;
import com.kippinretail.Modal.UserAddress.MyGeoCoder;
import com.kippinretail.Modal.UserAddress.Results;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.retrofit.RestClientAdavanced;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Charity_EditProfile extends SuperActivity implements View.OnClickListener {

    ToggleButton registerCharityToggle, foundationToggle, nonProfitCorporationToggle;
    EditText ed_firstName, ed_lastName, confirmpasswordEditText, notForProfitEditText, purposeOfCharityEditText,
            descriptionEditText, passwordEditText, ed_countryCode, ed_areaCode, ed_phoneNumber,
            businessRegNumberEditText, ed_city, websiteEditText;


    private Button registerButton, changePasswordButton;
    private Button alreadyRegisterButton;
    private String mUserRoleId = "3";
    private String CharityTypeIdsCommaSeparated = "";
    private String address;
    private String compres;
    private boolean networkProvider = false;
    private boolean gpsProvider = false;
    private LocationManager locationmanager = null;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
    double lattitude, longitude;
    private String mCity;
    private String mCountry;
    private String mLocation;
    private String counryCodeAndBusinessPhoneNumber;
    private ArrayList<String> typeId = new ArrayList<String>();
    LoginDataClass login;
    private Button btn_update;
    ImageView profileImage;
    private String encodedString = "";
    private StringBuilder userPhoneNumber;
    private String imagePath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_edit_profile);
        Initlization();

        updateUI();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(this);
    }

    private void updateUI() {

        login = CommonData.getUserData(this);


        ed_firstName.setText(login.getFirstname());
        ed_lastName.setText(login.getLastname());

        notForProfitEditText.setText(login.getBusinessName());
        purposeOfCharityEditText.setText(login.getCharityPurpose());
        descriptionEditText.setText(login.getBusinessdescription());

        ed_countryCode.setText(login.getCountry());
        String countryCode_bussinessNumber = login.getBusinessNumber();
        String[] ar = countryCode_bussinessNumber.split("-");
        if (ar.length >= 3) {
            ed_countryCode.setText(ar[0]);
            ed_countryCode.setText(ar[0]);
            ed_areaCode.setText(ar[1]);
            ed_areaCode.setText(ar[1]);
            ed_phoneNumber.setText(ar[2]);
            ed_phoneNumber.setText(ar[2]);

        }
        ed_city.setText(login.getCity());
        websiteEditText.setText(login.getWebsite());
        businessRegNumberEditText.setText(login.getBusinessPhoneNumber());

        /*try {
            encodedString = CommonUtility.encodeTobase64(BitmapFactory.decodeStream(new URL(imagePath).openConnection().getInputStream()));
        }catch(Exception ex){
            Log.e("120","MerchantUpdatedActivity");
        }*/
//        BitmapFactory.Options options;
//        options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
//        encodedString = CommonUtility.encodeTobase64(BitmapFactory.decodeFile(imagePath,options));

       /* if(imagePath.length()<100){
            if (imagePath != null && !imagePath.equals("")) {
                Picasso.with(Charity_EditProfile.this).load(imagePath).placeholder(R.drawable.icon_placeholder).
                        resize(CommonUtility.dpToPx(Charity_EditProfile.this, 108), CommonUtility.dpToPx(Charity_EditProfile.this, 135))
                        .into(profileImage);
            }
        }else{
            byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImage.setImageBitmap(decodedByte);
       }*/
        imagePath = CommonData.getUserData(this).getProfileImage();
        if (imagePath.length() < 200) {
            if (!imagePath.equals("")) {
                Picasso.with(Charity_EditProfile.this).load(imagePath).placeholder(R.drawable.icon_placeholder).
                        resize(CommonUtility.dpToPx(Charity_EditProfile.this, 108), CommonUtility.dpToPx(Charity_EditProfile.this, 135))
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                profileImage.setImageBitmap(bitmap);
                                try {
                                    imagePath = encodedString = CommonUtility.encodeTobase64(bitmap);
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
        } else {
            byte[] decodedString = Base64.decode(imagePath, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            String path = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, decodedByte));
            profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(path, CommonUtility.dpToPx(this, 108),
                    CommonUtility.dpToPx(this, 135)));
        }


//        }else{
//            profileImage.setImageBitmap(encodedString);
//        }


    }

    private void Initlization() {
        generateActionBar(R.string.title_charity_register11, true, true, false);
        generateRightText("", null);

        profileImage = (ImageView) findViewById(R.id.profileImage);
        //ToggleButton
        registerCharityToggle = (ToggleButton) findViewById(R.id.registerCharityToggle);
        foundationToggle = (ToggleButton) findViewById(R.id.foundationToggle);
        nonProfitCorporationToggle = (ToggleButton) findViewById(R.id.nonProfitCorporationToggle);

        // EditText
        ed_firstName = (EditText) findViewById(R.id.ed_firstName);
        ed_lastName = (EditText) findViewById(R.id.ed_lastName);
        ed_areaCode = (EditText) findViewById(R.id.ed_areaCode);
        ed_phoneNumber = (EditText) findViewById(R.id.ed_phoneNumber);
        passwordEditText = (EditText) findViewById(R.id.oldPasswordEditText);
        confirmpasswordEditText = (EditText) findViewById(R.id.confirmpasswordEditText);
        notForProfitEditText = (EditText) findViewById(R.id.notForProfitEditText);
        purposeOfCharityEditText = (EditText) findViewById(R.id.purposeOfCharityEditText);

        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);

        ed_countryCode = (EditText) findViewById(R.id.ed_countryCode);
        businessRegNumberEditText = (EditText) findViewById(R.id.businessRegNumberEditText);
        ed_city = (EditText) findViewById(R.id.ed_city);
        websiteEditText = (EditText) findViewById(R.id.websiteEditText);
        // Button
        registerButton = (Button) findViewById(R.id.registerButton);
        alreadyRegisterButton = (Button) findViewById(R.id.alreadyRegisterButton);


        // CheckBox


        btn_update = (Button) findViewById(R.id.btn_update);
        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        btn_update.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
        // TextView's ID


        String terms_conditions = getString(R.string.terms_conditions);
//        SpannableString ss = new SpannableString(terms_conditions);
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(View textView) {
//                Intent in = new Intent();
//                in.setClass(Charity_EditProfile.this, ReferralActivity.class);
//                startActivity(in);
//                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
//            }
//        };
//        ss.setSpan(clickableSpan, 26, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        termsConditionsTextView.setText(ss);


//        registerCharityToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
//
//                if (isChecked) {
//                    typeId.add("1");
//
//                } else {
//                    typeId.remove("1");
//                }
//            }
//        });
//
//        foundationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
//                if (isChecked) {
//                    typeId.add("2");
//
//                } else {
//                    typeId.remove("2");
//                }
//            }
//        });

//        nonProfitCorporationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
//                if (isChecked) {
//                    typeId.add("3");
//
//                } else {
//                    typeId.remove("3");
//                }
//
//            }
//        });

        locationmanager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        networkProvider = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        gpsProvider = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (networkProvider || gpsProvider) {
            getLocationParam();
        } else {
            showGPSDisabledAlertToUser();
        }
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImageDialog.showUploadImageDialog(Charity_EditProfile.this, getApplicationContext());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.changePasswordButton:
                Intent intent = new Intent();
                intent.setClass(this, ChangePasswordActivity.class);
                startActivity(intent);

                break;

            case R.id.alreadyRegisterButton:
                InvokeLogin();
                break;
            case R.id.btn_update:
                CharityTypeIdsCommaSeparated = "";

                Log.e("typeId", "==" + typeId);

                for (int i = 0; i < typeId.size(); i++) {

                    if (CharityTypeIdsCommaSeparated.equals("")) {
                        CharityTypeIdsCommaSeparated = typeId.get(i);
                    } else {
                        CharityTypeIdsCommaSeparated = CharityTypeIdsCommaSeparated + "," + typeId.get(i);
                    }
                }
                Log.e("CharityTypeIdsCommaSeparated", "==" + CharityTypeIdsCommaSeparated);
                if (ed_firstName.getText().toString().length() != 0 && ed_lastName.getText().toString().length() != 0 && ed_countryCode.getText().toString().length() != 0 &&
                        ed_areaCode.getText().toString().length() != 0 && ed_phoneNumber.getText().toString().length() != 0


                        && notForProfitEditText.getText().toString().length() != 0 && purposeOfCharityEditText.getText().toString().length() != 0
                        && descriptionEditText.getText().toString().length() != 0
                        && ed_countryCode.getText().toString().length() != 0
                        && businessRegNumberEditText.getText().toString().length() != 0 && businessRegNumberEditText.getText().toString().length() != 0
                    /*&& websiteEditText.getText().toString().length() != 0*/) {
                    updateProfile();

                } else {


                    MessageDialog.showDialog(Charity_EditProfile.this, "Please Fill All the Field", false);


                }


                break;

        }
    }


    private void updatePref() {
        LoginDataClass obj = CommonData.getUserData(this);
        obj.setProfileImage(imagePath);
        obj.setFirstname(ed_firstName.getText().toString());
        obj.setLastname(ed_lastName.getText().toString());
        obj.setBusinessName(getText(notForProfitEditText));
        obj.setBusinessNumber(userPhoneNumber.toString());
        obj.setBusinessdescription(getText(descriptionEditText));
        obj.setBusinessPhoneNumber(businessRegNumberEditText.getText().toString());
        obj.setCity(getText(ed_city));
        obj.setWebsite(getText(websiteEditText));
        CommonData.saveUserData(this, obj);
    }

    private void updateProfile() {
//        Id"                    :(jsonDict["Id"] as? Int)!,
//        ""          :notforProfitTxt.text!,
//                "CharityPurpose"        :charityPurposeTxt.text!,
//                "BusinessPhoneNumber"   :registeredNumberTxt.text!,
//                "Businessdescription"   :businessDescpTxt.text!,
//                "BusinessNumber"        :countryCodeTxt.text! + "-" + businessPhoneNmuberTxt.text!,
//                "Mobile"                :phnNumberTxt.text!,
//                "Website"               :websiteTxt.text!,
//                "RoleId"                :"3"

        ;

        HashMap<String, String> hmap = new HashMap<String, String>();
        userPhoneNumber = new StringBuilder();
        userPhoneNumber = new StringBuilder();
        userPhoneNumber.append(ed_countryCode.getText().toString());
        userPhoneNumber.append("-");
        userPhoneNumber.append(ed_countryCode.getText().toString());
        userPhoneNumber.append("-");
        userPhoneNumber.append(ed_phoneNumber.getText().toString());

        hmap.put("ProfileImage", imagePath);//encodedString
        hmap.put("Firstname", ed_firstName.getText().toString());
        hmap.put("Lastname", ed_lastName.getText().toString());
        hmap.put("Id", login.getId() + "");
        hmap.put("BusinessName", getText(notForProfitEditText));
        hmap.put("CharityPurpose", getText(purposeOfCharityEditText));
        hmap.put("Businessdescription", getText(descriptionEditText));
        hmap.put("BusinessPhoneNumber", getText(businessRegNumberEditText));
        hmap.put("BusinessNumber", userPhoneNumber.toString());
        hmap.put("City", getText(ed_city));
        hmap.put("Website", getText(websiteEditText));
        hmap.put("RoleId", (login.getRoleId()) + "");


        RestClientAdavanced.getApiServiceForPojo(this).UpdateProfileCharity(hmap, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e(jsonElement.toString(), response.getUrl());
                ModalResponse modalResponse = new Gson().fromJson(jsonElement, ModalResponse.class);
                if (modalResponse.getResponseCode().equals("1")) {
                    MessageDialog.showDialog(Charity_EditProfile.this, "Profile has been successfully updated", true);
                    updatePref();
                } else {
                    MessageDialog.showDialog(Charity_EditProfile.this, "Fail to Update Profile Try Again Later..", true);
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(error.getUrl(), error.getMessage());
                MessageDialog.showDialog(Charity_EditProfile.this, "We are experiencing technical difficulties but value your business… please try again later");
            }
        });

    }
// Network error dialog

    private void showGPSDisabledAlertToUser() {
        try {
            final Dialog dialog = new Dialog(Charity_EditProfile.this,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_network);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            Button btnNO = (Button) dialog.findViewById(R.id.btnNO);
            Button btnYES = (Button) dialog.findViewById(R.id.btnYES);

            btnYES.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callGPSSettingIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(callGPSSettingIntent);
                }

            });
            btnNO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void CallCharityRegisteration() {

        counryCodeAndBusinessPhoneNumber = ed_countryCode.getText().toString() + "-" + businessRegNumberEditText.getText().toString();

        LoadingBox.showLoadingDialog(Charity_EditProfile.this, "");
        //    EditText ed_firstName,ed_lastName, passwordEditText, confirmpasswordEditText, notForProfitEditText, purposeOfCharityEditText,
        //     notForProfitRegisteredNumberEditText, descriptionEditText, fullAddressEditText, ed_countryCode,
        //               businessRegNumberEditText, ed_city, websiteEditText;

        /*RestClient.getApiServiceForPojo().CharityRegistration(ed_firstName.getText().toString(), ed_lastNamenotForProfitRegisteredNumberEditText.getText().toString(),
                passwordEditText.getText().toString(), notForProfitEditText.getText().toString(), purposeOfCharityEditText.getText().toString(),
                CharityTypeIdsCommaSeparated,notForProfitRegisteredNumberEditText.getText().toString(),descriptionEditText.getText().toString(),mLocation,
                counryCodeAndBusinessPhoneNumber, ed_city.getText().toStcheckBoxPolicyring(),websiteEditText.getText().toString(),
                mCountry,mUserRoleId,
                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement userRegister, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.i("Tag", "Request data " + new Gson().toJson(userRegister));
                        JsonObject jsonObj = userRegister.getAsJsonObject();
                        String strObj = jsonObj.toString();
                        // Request data
                        // {"Id":26,"Email":"kamaljeet.singh@smartbuzz.net","RoleId":2,"Gender":"Male","Dob":"1990-12-15T00:00:00",
                        // "Mobile":"1234567890","Password":"123456","ReferralCode":"kama4GGt",
                        // "RefferCodepath":"http://kippin_retailpreproduction.web1.anzleads.com/ReferCode/kama4GGt.png",
                        // "MerchantPoint":0.0,"ResponseCode":1,"UserId":0,"ResponseMessage":"Please check your mail for email confirmation."}
                        try {
                            JSONObject OBJ = new JSONObject(strObj);
                            String ResponseMessage = OBJ.getString("ResponseMessage");
                            String ResponseCode = OBJ.getString("ResponseCode");
                            Show(ResponseMessage,ResponseCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Forgot password", " = " + e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Charity Failure => ", " = " + error.getMessage());

                        ErrorCodes.checkCode(Charity_EditProfile.this, error);

                    }

                });*/
    }

    // Show dialog
    public void Show(String message, final String responseCode) {
        try {

            final Dialog dialog = new Dialog(Charity_EditProfile.this,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_custom_msg);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            TextView textMessage = (TextView) dialog
                    .findViewById(R.id.textForMessage);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText(message);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (responseCode.equals("1")) {
                        finish();
                        Intent in = new Intent();
                        in.setClass(Charity_EditProfile.this, ReferralActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                    } else {
                        dialog.dismiss();
                    }
                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Email validation
    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void InvokeLogin() {
        finish();
        Intent in = new Intent();
        in.setClass(Charity_EditProfile.this, LoginActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    private void backPressed() {
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    // Getting Location
    public String getLocationParam() {
        LoadingBox.showLoadingDialog(Charity_EditProfile.this, "");
        final String[] ar = new String[1];
        final double[] lat = new double[2];
        locationmanager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            networkProvider = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            gpsProvider = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Charity_EditProfile.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Charity_EditProfile.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_ACCESS_FINE_LOCATION);
            }
            if (networkProvider) {
                Log.e("Network Provider ", "Network Provider ");
                locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        lattitude = location.getLatitude();
                        longitude = location.getLongitude();
                        compres = getCurrentLocation();
                        if (compres != null) {
                            Log.e("Final Addess", compres);

                        }

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
            if (gpsProvider) {
                Log.e("GPS Provider ", "GPS Provider ");
                locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        lattitude = location.getLatitude();
                        longitude = location.getLongitude();
                        compres = getCurrentLocation();
                        if (compres != null) {
                            Log.e("Final Addess", compres);

                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                });
            }
        } catch (Exception ex) {
            Log.e("Prob Location Param", ex.getMessage() + "");
        }
        return compres;
    }

    public String getCurrentLocation() {
        String latlng;
        latlng = lattitude + "," + longitude;
        String sensor = "true";
        final String[] ar = new String[1];
        RestClient.getMyApiService().getCurrentLocation(latlng, sensor, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                String jsonoutput = jsonElement.toString();
                Log.e("Output is == ", jsonoutput);
                Type listtype = new TypeToken<MyGeoCoder>() {
                }.getType();
                Gson gson = new Gson();
                MyGeoCoder geoCoder = gson.fromJson(jsonoutput, listtype);
                List<Results> results = geoCoder.getResults();
                Log.e("Total Result :: ", results.size() + "");
                if (results.size() > 0) {
                    Results result = results.get(0);
                    // address = result.getAddress_components();
                    for (int x = 0; x < result.getAddress_components().size(); x++) {
                        Log.e("DUTYY", "===" + result.getAddress_components().get(x).getTypes().get(0));
                        if (result.getAddress_components().get(x).getTypes().get(0).equals("locality")) {
                            Log.e("CITY", "===" + result.getAddress_components().get(x).getLong_name());
                            mCity = result.getAddress_components().get(x).getLong_name();
                            mLocation = mCity;

                            // break;
                        }
                        if (result.getAddress_components().get(x).getTypes().get(0).equals("country")) {
                            Log.e("country", "===" + result.getAddress_components().get(x).getLong_name());
                            mCountry = result.getAddress_components().get(x).getLong_name();
                            mLocation = mCity + "," + mCountry;

                            // break;
                        }
                    }

                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Google Api Error", error.getMessage());
                MessageDialog.showDialog(Charity_EditProfile.this, "We are experiencing technical difficulties but value your business… please try again later");
            }
        });
        return address;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                try {
                    String imgPath = null;

                    imgPath = result.getUri().getPath();

                    if (imgPath != null) {
                        CommonUtility.VoucherBitMap = BitmapFactory.decodeFile(imgPath);
                        imagePath = encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        //     Bitmap scaledBitmap = Bitmap.createScaledBitmap(CommonUtility.VoucherBitMap,Utils.dpToPx(UserProfileUpdateActivity.this,130),Utils.dpToPx(UserProfileUpdateActivity.this,130),false);
                        //   profileImage.setImageBitmap(scaledBitmap);
                        profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath, CommonUtility.dpToPx(this, 130),
                                CommonUtility.dpToPx(this, 130)));
                    }
                } catch (Exception e) {

                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CommonUtility.REQUEST_CODE_GALLERY:
                try {
                    String imgPath = null;
                    if (data != null) {
                        imgPath = data.getStringExtra("imagePath");
                    }
                    if (imgPath != null) {
                        CommonUtility.VoucherBitMap = BitmapFactory.decodeFile(imgPath);
                        imagePath = encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath, CommonUtility.dpToPx(this, 108),
                                CommonUtility.dpToPx(this, 135)));
                    }
                } catch (Exception e) {

                }
                break;
            case CommonUtility.REQUEST_CODE_TAKE_PICTURE:
                try {
                    getContentResolver().notifyChange(UploadImageDialog.mImageUri, null);
                    imagePath = new File(UploadImageDialog.mImageUri.getPath()).getAbsolutePath();
                    Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), UploadImageDialog.mImageUri);
                    ExifInterface exifInterface = new ExifInterface(imagePath);
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
                    imagePath = encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                    String orientedImagePath = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, oriented));
                    profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(orientedImagePath, CommonUtility.dpToPx(this, 108),
                            CommonUtility.dpToPx(this, 135)));
                    UploadImageDialog.mImageUri = null;
                } catch (Exception ex) {

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadLogo(View v) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("KIPPIN CROP")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Done")
                .setRequestedSize(400, 400)
                .setCropMenuCropButtonIcon(R.drawable.ic_tick)
                .start(this);
        UploadImageDialog.showUploadImageDialog(this, getApplicationContext());
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
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
    }

}
