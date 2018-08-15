package com.kippinretail;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.ApplicationuUlity.Validation;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.CommonDialog.UploadImageDialog;
import com.kippinretail.Modal.Industry;
import com.kippinretail.Modal.UserAddress.MyGeoCoder;
import com.kippinretail.Modal.UserAddress.Results;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 3/5/2016.
 */
public class MerchantRegistrationActivity extends SuperActivity implements View.OnClickListener
        , GoogleApiClient.OnConnectionFailedListener
        , com.google.android.gms.location.LocationListener
        , GoogleApiClient.ConnectionCallbacks {
    //{
    EditText businessNumberEditText, userNameEditText, emailEditText, passwordEditText, ed_phone_number, confirmpasswordEditText, businessNameEditText, industryEditText, descriptionEditText,
            locationEditText, phoneNumberEditText, websiteEditText, countryCodeEditText, areaCodeEditText, firstNameEditText, lastNameEditText;
    CheckBox checkBoxPolicy;
    TextView termsConditionsTextView;
    ImageView registerButton;
    ImageView alreadyRegisterButton;
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
    private Spinner industrySpinner;
    List<Industry> industries;
    private String mLocation;
    private String mMerchantRoleId = "1";
    private List<String> listForIndutry;
    private String mIndustry;
    private String loyaltyBarcode;
    private ImageView profileImage;
    private String encodedString = null;
    private EditText ed_city;
    private final long LOGOUT_INTERVAL = 1000 * 70;
    RelativeLayout lalayout_ivBack;
    View terms;


    GoogleApiClient mGoogleApiClient = null;
    LocationRequest mLocationRequest = null;
    private FusedLocationProviderApi fusedLocationProviderApi;
    private Dialog gpsDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_registration);
        Initlization();
        addListener();
        updateUI();
        Utils.hideKeyboard(this);
    }

    private void updateUI() {

        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
        getIndustryValues();
    }

    private void addListener() {
        profileImage.setOnClickListener(this);
        locationEditText.setKeyListener(null);
        lalayout_ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.slideDown(MerchantRegistrationActivity.this, terms);
            }
        });
        locationEditText.setKeyListener(null);
    }

    private void Initlization() {
        // EditText ID's

        terms = findViewById(R.id.terms);
        lalayout_ivBack = (RelativeLayout) terms.findViewById(R.id.lalayout_ivBack);
        SelectUserTypeActivity.plus = 2;
        generateActionBar(R.string.title_merchant_register, true, false, false);
        generateRightText("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        profileImage = (ImageView) findViewById(R.id.profileImage);
        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        businessNumberEditText = (EditText) findViewById(R.id.businessNumberEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.oldPasswordEditText);
        confirmpasswordEditText = (EditText) findViewById(R.id.confirmpasswordEditText);
        businessNameEditText = (EditText) findViewById(R.id.businessNameEditText);
        industrySpinner = (Spinner) findViewById(R.id.industrySpinner);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        ed_city = (EditText) findViewById(R.id.ed_city);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        ed_phone_number = (EditText) findViewById(R.id.ed_phone_number);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        areaCodeEditText = (EditText) findViewById(R.id.areaCodeEditText);
        websiteEditText = (EditText) findViewById(R.id.websiteEditText);
        countryCodeEditText = (EditText) findViewById(R.id.countryCodeEditText);
        // CheckBox
        checkBoxPolicy = (CheckBox) findViewById(R.id.checkBoxPolicy);
        // TextView's ID
        termsConditionsTextView = (TextView) findViewById(R.id.termsConditionsTextView);
        termsConditionsTextView.setOnClickListener(this);
        // Buttons Id's
        registerButton = (ImageView) findViewById(R.id.registerButton);
        alreadyRegisterButton = (ImageView) findViewById(R.id.alreadyRegisterButton);
        registerButton.setOnClickListener(this);
        alreadyRegisterButton.setOnClickListener(this);
        // ImageView


        String terms_conditions = getString(R.string.terms_conditions);
        SpannableString ss = new SpannableString(terms_conditions);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {


            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                //  ds.setTypeface(Typeface.create(ds.getTypeface(),Typeface.BOLD));
            }
        };
        ss.setSpan(clickableSpan, 26, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsConditionsTextView.setText(ss);
        termsConditionsTextView.setMovementMethod(LinkMovementMethod.getInstance());
        termsConditionsTextView.setLinkTextColor(Color.parseColor("#3195DB"));


        String[] Industry = getResources().getStringArray(R.array.Industry);

        industrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!listForIndutry.get(position).equals("Industry")) {
                    int index = industrySpinner.getSelectedItemPosition();
                    mIndustry = String.valueOf(industries.get(index).getId());
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#A7A7A7"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void setUpUI() {
        super.setUpUI();

    }

    private void getIndustryValues() {
        LoadingBox.showLoadingDialog(MerchantRegistrationActivity.this, "");
        handler.postDelayed(runnable, LOGOUT_INTERVAL);
        RestClient.getApiServiceForPojo().Industry(new Callback<List<Industry>>() {
            @Override
            public void success(List<Industry> industry, Response response) {

                industries = industry;
                LoadingBox.dismissLoadingDialog();
                Log.e("Tag", "Request data " + new Gson().toJson(industry));
                listForIndutry = new ArrayList<>();
                for (int i = 0; i < industry.size(); i++) {
                    listForIndutry.add(String.valueOf(industry.get(i).getIndustryType()));
                }
                listForIndutry.add(0, "Industry");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MerchantRegistrationActivity.this, R.layout.spinner_item, listForIndutry);
                industrySpinner.setAdapter(adapter);
                //  [ { "Id": 1,"IndustryType": "Transportation"}

            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("Industry Failure => ", " = " + error.getMessage());
                CommonDialog.With(MerchantRegistrationActivity.this).Show(error.getMessage());

                MessageDialog.showFailureDialog(MerchantRegistrationActivity.this);

            }

        });
    }


    @Override
    public void onClick(View v) {

        int ii = 0;

        switch (v.getId()) {
            case R.id.registerButton:

                if (firstNameEditText.getText().toString().length() != 0 && lastNameEditText.getText().toString().length() != 0
                        && userNameEditText.getText().toString().length() != 0 && emailEditText.getText().toString().length() != 0
                        && passwordEditText.getText().toString().length() != 0 && businessNumberEditText.getText().toString().length() != 0
                        && confirmpasswordEditText.getText().toString().length() != 0 && descriptionEditText.getText().toString().length() != 0
                        && phoneNumberEditText.getText().toString().length() != 0 && businessNameEditText.getText().toString().length() != 0
                        /*&& websiteEditText.getText().toString().length() != 0*/ && mIndustry != ""
                        && countryCodeEditText.getText().toString().length() != 0 && areaCodeEditText.getText().toString().length() != 0) {
                    if (passwordEditText.getText().toString().length() < 6) {
                        CommonDialog.With(MerchantRegistrationActivity.this).Show("Password should be of minimum 6 characters.");
                    } else {
                        if (Validation.checkPassWordAndConfirmPassword(passwordEditText.getText().toString(), confirmpasswordEditText.getText().toString())) {

                            if (checkBoxPolicy.isChecked()) {
                                if (emailValidator(emailEditText.getText().toString().trim())) {
                                    loyaltyBarcode = phoneNumberEditText.getText().toString();
                                    if (encodedString == null) {
                                        Bitmap ProfileImageAsBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.dialog_logo);
                                        try {
                                            encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(ProfileImageAsBitMap);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        CallMerchantRegister();
                                    } else {
                                        CallMerchantRegister();
                                    }

                                } else {
                                    CommonDialog.With(MerchantRegistrationActivity.this).Show("Please enter valid email id..");
                                }

                            } else {
                                CommonDialog.With(MerchantRegistrationActivity.this).Show("Please read and accept Terms & Conditions.");
                            }

                        } else {
                            CommonDialog.With(MerchantRegistrationActivity.this).Show("Password mismatch here.Please confirm same password.");
                        }
                    }
                } else {
                    CommonDialog.With(MerchantRegistrationActivity.this).Show("Please fill the empty fields.");
                }

                break;
            case R.id.alreadyRegisterButton:
                InvokeLogin();
                break;
            case R.id.termsConditionsTextView:
                CommonUtility.startNewActivity(this, ActivityTermsAndConditions.class);
                break;
            case R.id.profileImage:

                break;

        }

    }

    private void uploadImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("KIPPIN CROP")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Done")
                .setRequestedSize(400, 400)
                .setCropMenuCropButtonIcon(R.drawable.ic_tick)
                .start(this);
        // UploadImageDialog.showUploadImageDialog(MerchantRegistrationActivity.this, getApplicationContext());
    }

    private boolean isValidEmaillId(String trim) {
        return false;
    }


    // BusinessPhoneNumber used to store (Bussibess Reg Number)
    // BusinessNumber used to store (country code ,area code  , phone number )
    private void CallMerchantRegister() {

        int camm = 0;
        StringBuilder userPhoneNumber = new StringBuilder();
        userPhoneNumber.append(countryCodeEditText.getText().toString());
        userPhoneNumber.append("-");
        userPhoneNumber.append(areaCodeEditText.getText().toString());
        userPhoneNumber.append("-");
        userPhoneNumber.append(phoneNumberEditText.getText().toString());
        LoadingBox.showLoadingDialog(MerchantRegistrationActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().MerchantRegistration(encodedString, firstNameEditText.getText().toString(), lastNameEditText.getText().toString(),
                userNameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(),
                businessNameEditText.getText().toString(), mIndustry, descriptionEditText.getText().toString()
                , ed_city.getText().toString(), mLocation, businessNumberEditText.getText().toString()
                , userPhoneNumber.toString(), websiteEditText.getText().toString(), mCountry, mMerchantRoleId,
                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement userRegister, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.i("Tag", "Request data " + new Gson().toJson(userRegister));
                        Log.e("URL", response.getUrl());
                        JsonObject jsonObj = userRegister.getAsJsonObject();
                        String strObj = jsonObj.toString();
                        try {
                            JSONObject OBJ = new JSONObject(strObj);
                            String ResponseMessage = OBJ.getString("ResponseMessage");
                            String ResponseCode = OBJ.getString("ResponseCode");
                            Show(ResponseMessage, ResponseCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Forgot password", " = " + e.toString());
                        }
                    }

                    //
                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Login Failure => ", " = " + error.getMessage());
                        Log.e("URL", error.getMessage());
                        MessageDialog.showFailureDialog(MerchantRegistrationActivity.this);


                    }

                });
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

    // Show dialog
    public void Show(String message, final String responseCode) {
        try {

            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_custom_msg);

            TextView textMessage = (TextView) dialog
                    .findViewById(R.id.text_msg);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText(message);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (responseCode.equals("1")) {
                        Utils.logoutUser(MerchantRegistrationActivity.this);
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

    private void InvokeLogin() {
        finish();
        Intent in = new Intent();
        in.setClass(MerchantRegistrationActivity.this, LoginActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    /* // Getting Current Location
     public String getLocationParam() {

         handler.postDelayed(runnable, LOGOUT_INTERVAL);
         final String[] ar = new String[1];
         final double[] lat = new double[2];
         locationmanager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
         try {
             networkProvider = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
             gpsProvider = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
             if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                 ActivityCompat.requestPermissions(MerchantRegistrationActivity.this,
                         new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                         MY_PERMISSION_ACCESS_COARSE_LOCATION);
             }
             if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                 ActivityCompat.requestPermissions(MerchantRegistrationActivity.this,
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
 */
    private void showGPSDisabledAlertToUser() {
        try {
            gpsDialog = new Dialog(MerchantRegistrationActivity.this,
                    R.style.Theme_AppCompat_Translucent);
            gpsDialog.setContentView(R.layout.dialog_network);
            WindowManager.LayoutParams layoutParams = gpsDialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            gpsDialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            gpsDialog.setCancelable(false);
            gpsDialog.setCanceledOnTouchOutside(false);

            Button btnNO = (Button) gpsDialog.findViewById(R.id.btnNO);
            Button btnYES = (Button) gpsDialog.findViewById(R.id.btnYES);

            btnYES.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callGPSSettingIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(callGPSSettingIntent, CommonUtility.REQUEST_LOCATION);
                }

            });
            btnNO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gpsDialog.dismiss();
                    Toast.makeText(MerchantRegistrationActivity.this, "Location Service is required to register", Toast.LENGTH_LONG).show();
                    LoadingBox.dismissLoadingDialog();
                    //   finish();
                    //  MerchantRegistrationActivity.this.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                }
            });
            gpsDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getCurrentLocation() {
        String latlng;
        latlng = lattitude + "," + longitude;
        String sensor = "true";
        final String[] ar = new String[1];
        RestClient.getMyApiService().getCurrentLocation(latlng, sensor, CommonUtility.getAndroidKey(), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

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
                        if (result.getAddress_components().get(x).getTypes().get(0).equals("administrative_area_level_1")) {
                            Log.e("CITY", "===" + result.getAddress_components().get(x).getLong_name());
                            mCity = result.getAddress_components().get(x).getLong_name();
                            mLocation = mCity;
                            locationEditText.setText(mLocation);
                            // break;
                        }
                        if (result.getAddress_components().get(x).getTypes().get(0).equals("country")) {
                            Log.e("country", "===" + result.getAddress_components().get(x).getLong_name());
                            mCountry = result.getAddress_components().get(x).getLong_name();
                            mLocation = mCity + "," + mCountry;
                            locationEditText.setText(mLocation);
                            // break;
                        }
                    }
                    handler.removeCallbacks(runnable);
                    LoadingBox.dismissLoadingDialog();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showFailureDialog(MerchantRegistrationActivity.this);
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
                    String imgPath = result.getUri().getPath();

                    if (imgPath != null) {
                        CommonUtility.VoucherBitMap = BitmapFactory.decodeFile(imgPath);
                        encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath, CommonUtility.dpToPx(this, 140),
                                CommonUtility.dpToPx(this, 140)));
                    }
                } catch (Exception e) {

                }
                // ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
               /* Toast.makeText(
                        this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG)
                        .show();*/
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
        if (resultCode != RESULT_OK) {
            switch (requestCode) {
                case CommonUtility.REQUEST_LOCATION:
                    try {
                        locationmanager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        networkProvider = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        gpsProvider = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        if (networkProvider || gpsProvider) {
                            gpsDialog.dismiss();
                            LoadingBox.showLoadingDialog(MerchantRegistrationActivity.this, "Please wait");
                            handler.postDelayed(runnable, LOGOUT_INTERVAL);
                            if (!mGoogleApiClient.isConnected()) {
                                mGoogleApiClient.connect();
                            }

                        } else {
                            gpsDialog.dismiss();
                            Toast.makeText(MerchantRegistrationActivity.this, "Location Service is required to register", Toast.LENGTH_LONG).show();
                            //    finish();
                            //   MerchantRegistrationActivity.this.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                        }
                    } catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }
                    break;
            }
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
                        encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath, CommonUtility.dpToPx(this, 108),
                                CommonUtility.dpToPx(this, 135)));
                    }
                } catch (Exception e) {

                }
                break;
            case CommonUtility.REQUEST_CODE_TAKE_PICTURE:
                try {
                    getContentResolver().notifyChange(UploadImageDialog.mImageUri, null);
                    CommonUtility.VoucherBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), UploadImageDialog.mImageUri);
                    encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                    ExifInterface exifInterface = new ExifInterface(UploadImageDialog.mImageUri.getPath());
                    int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                    float angle = 360;
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
                    Bitmap oriented = CommonUtility.fixOrientation(CommonUtility.VoucherBitMap, angle);
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
        uploadImage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            locationmanager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            networkProvider = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            gpsProvider = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (networkProvider || gpsProvider) {

                if (!mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }

            } else {
                showGPSDisabledAlertToUser();
            }
        } catch (Exception ex) {

        }

    }


    public static LocationRequest createLocationRequest(int interval, int fastestInterval) {
        LocationRequest mLocationRequest = null;
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //GET HIGH ACCURATE LOCATION IN APP
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(fastestInterval);
        return mLocationRequest;
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLocationRequest = createLocationRequest(10000, 10000);

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MerchantRegistrationActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        CommonUtility.MY_PERMISSION_ACCESS_LOCATION);
                //  LoadingBox.dismissLoadingDialog();
                //handler.removeCallbacks(runnable);
            } else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MerchantRegistrationActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        CommonUtility.MY_PERMISSION_ACCESS_LOCATION);
            } else {
                LoadingBox.showLoadingDialog(MerchantRegistrationActivity.this, "Please wait");
                handler.postDelayed(runnable, LOGOUT_INTERVAL);
                fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        } catch (Exception ex) {

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            if (location.getLongitude() != 0.0 && location.getLongitude() != 0.0) {
                lattitude = location.getLatitude();
                longitude = location.getLongitude();
                if (mGoogleApiClient != null) {
                    mGoogleApiClient.disconnect();
                }
                compres = getCurrentLocation();
                if (compres != null) {
                    Log.e("Final Addess", compres);

                }
            }

        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("Handler finish", "Handler finish");
                handler.removeCallbacks(runnable);
                if (LoadingBox.isDialogShowing()) {
                    LoadingBox.dismissLoadingDialog();
                }
                MessageDialog.showDialog(MerchantRegistrationActivity.this, "Sorry fail to find location try again. ", true);
            } catch (Exception ex) {
                Log.e("====", ex.getMessage());
            }
        }
    };

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
                    if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        LoadingBox.showLoadingDialog(MerchantRegistrationActivity.this, "Please wait");
                        handler.postDelayed(runnable, LOGOUT_INTERVAL);
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                        fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    } else {
                        Toast.makeText(MerchantRegistrationActivity.this, "This is a required for the app to function", Toast.LENGTH_LONG).show();

                    }
                    break;
            }
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
    }
}
