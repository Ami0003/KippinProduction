package com.kippinretail;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 3/31/2016.
 */
public class CharityRegistratonActivity extends SuperActivity implements View.OnClickListener
        , GoogleApiClient.OnConnectionFailedListener
        , com.google.android.gms.location.LocationListener
        , GoogleApiClient.ConnectionCallbacks {


    ToggleButton registerCharityToggle, foundationToggle, nonProfitCorporationToggle;
    EditText userNameEditText, emailEditText, passwordEditText, confirmpasswordEditText, notForProfitEditText, purposeOfCharityEditText,
            descriptionEditText, ed_location, ed_country_code,
            businessRegNumberEditText, websiteEditText, ed_city, ed_firstName, ed_lastName, ed_area_code, ed_phone_number;
    private TextView termsConditionsTextView;
    private CheckBox checkBoxPolicy;
    private ImageView registerButton;
    private ImageView alreadyRegisterButton;
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
    private CircleImageView iv_profileImage;
    private String encodedString;
    private final long LOGOUT_INTERVAL = 1000 * 70;


    GoogleApiClient mGoogleApiClient = null;

    private FusedLocationProviderApi fusedLocationProviderApi;
    private Dialog gpsDialog;
    private LocationRequest mLocationRequest = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity);
        Initlization();
        updateUI();
        addListener();

    }

    private void updateUI() {

        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(this);
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
            Log.e("141 - CharityRegistratonActivity", ex.getMessage());
        }
    }

    private void addListener() {
        ed_location.setKeyListener(null);
        iv_profileImage.setOnClickListener(this);
    }


    private void Initlization() {
        SelectUserTypeActivity.plus = 2;
        generateActionBar(R.string.title_charity_register, true, false, false);
        generateRightText("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //ToggleButton
        iv_profileImage = (CircleImageView) findViewById(R.id.iv_profileImage);
        registerCharityToggle = (ToggleButton) findViewById(R.id.registerCharityToggle);
        foundationToggle = (ToggleButton) findViewById(R.id.foundationToggle);
        nonProfitCorporationToggle = (ToggleButton) findViewById(R.id.nonProfitCorporationToggle);

        // EditText
        ed_firstName = (EditText) findViewById(R.id.ed_firstName);
        ed_lastName = (EditText) findViewById(R.id.ed_lastName);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.oldPasswordEditText);
        confirmpasswordEditText = (EditText) findViewById(R.id.confirmpasswordEditText);
        notForProfitEditText = (EditText) findViewById(R.id.notForProfitEditText);
        purposeOfCharityEditText = (EditText) findViewById(R.id.purposeOfCharityEditText);

        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        ed_city = (EditText) findViewById(R.id.ed_city);
        ed_location = (EditText) findViewById(R.id.ed_location);
        ed_area_code = (EditText) findViewById(R.id.ed_area_code);
        ed_phone_number = (EditText) findViewById(R.id.ed_phone_number);
        ed_country_code = (EditText) findViewById(R.id.ed_country_code);
        businessRegNumberEditText = (EditText) findViewById(R.id.businessRegNumberEditText);

        websiteEditText = (EditText) findViewById(R.id.websiteEditText);
        // Button
        registerButton = (ImageView) findViewById(R.id.registerButton);
        alreadyRegisterButton = (ImageView) findViewById(R.id.alreadyRegisterButton);
        registerButton.setOnClickListener(this);
        alreadyRegisterButton.setOnClickListener(this);
        // CheckBox
        checkBoxPolicy = (CheckBox) findViewById(R.id.checkBoxPolicy);
        // TextView's ID
        termsConditionsTextView = (TextView) findViewById(R.id.termsConditionsTextView);
        String terms_conditions = getString(R.string.terms_conditions);
        SpannableString ss = new SpannableString(terms_conditions);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent in = new Intent();
                in.setClass(CharityRegistratonActivity.this, ActivityTermsAndConditions.class);
                startActivity(in);
                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 26, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsConditionsTextView.setText(ss);
        termsConditionsTextView.setMovementMethod(LinkMovementMethod.getInstance());
        termsConditionsTextView.setLinkTextColor(Color.parseColor("#3195DB"));
        registerCharityToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {

                if (isChecked) {
                    typeId.add("1");

                } else {
                    typeId.remove("1");
                }
            }
        });

        foundationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    typeId.add("2");

                } else {
                    typeId.remove("2");
                }
            }
        });

        nonProfitCorporationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    typeId.add("3");

                } else {
                    typeId.remove("3");
                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.alreadyRegisterButton:
                InvokeLogin();
                break;
            case R.id.registerButton:
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
                if (ed_firstName.getText().toString().length() != 0 && ed_lastName.getText().toString().length() != 0 && ed_city.getText().toString().length() != 0 &&
                        userNameEditText.getText().toString().length() != 0 && emailEditText.getText().toString().length() != 0
                        && passwordEditText.getText().toString().length() != 0 && ed_phone_number.getText().length() != 0
                        && confirmpasswordEditText.getText().toString().length() != 0
                        && notForProfitEditText.getText().toString().length() != 0 && purposeOfCharityEditText.getText().toString().length() != 0
                        &&/* notForProfitRegisteredNumberEditText.getText().toString().length() != 0*/ descriptionEditText.getText().toString().length() != 0
                        && ed_country_code.getText().toString().length() != 0
                        && businessRegNumberEditText.getText().toString().length() != 0 && businessRegNumberEditText.getText().toString().length() != 0
                    /*&& websiteEditText.getText().toString().length() != 0*/) {
                    if (passwordEditText.getText().toString().length() < 6) {
                        CommonDialog.With(CharityRegistratonActivity.this).Show("Password should be of minimum 6 characters.");
                    } else {
                        if (Validation.checkPassWordAndConfirmPassword(passwordEditText.getText().toString(), confirmpasswordEditText.getText().toString())) {

                            if (checkBoxPolicy.isChecked()) {
                                if (emailValidator(emailEditText.getText().toString().trim())) {
                                    if (encodedString == null) {
                                        Bitmap ProfileImageAsBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_username);
                                        try {
                                            encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(ProfileImageAsBitMap);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        CallCharityRegisteration();
                                    } else {
                                        CallCharityRegisteration();
                                    }

                                } else {
                                    CommonDialog.With(CharityRegistratonActivity.this).Show("Please enter valid email id..");
                                }

                            } else {
                                CommonDialog.With(CharityRegistratonActivity.this).Show("Please read and accept Terms & Conditions.");
                            }

                        } else {
                            CommonDialog.With(CharityRegistratonActivity.this).Show("Password mismatch here.Please confirm same password.");
                        }
                    }
                } else {
                    CommonDialog.With(CharityRegistratonActivity.this).Show("Please fill the empty fields.");
                }

                break;

            case R.id.iv_profileImage:
                uploadImage();
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
        // UploadImageDialog.showUploadImageDialog(this, getApplicationContext());
    }
// Network error dialog

    private void showGPSDisabledAlertToUser() {
        try {
            gpsDialog = new Dialog(CharityRegistratonActivity.this,
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
                    startActivity(callGPSSettingIntent);
                }

            });
            btnNO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gpsDialog.dismiss();
                    Toast.makeText(CharityRegistratonActivity.this, "Location Service is required to register", Toast.LENGTH_LONG).show();
                    // finish();
                    //  CharityRegistratonActivity.this.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                }
            });
            gpsDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // BusinessPhoneNumber used to store (Bussibess Reg Number)
    // BusinessNumber used to store (country code ,area code  , phone number )
    private void CallCharityRegisteration() {
        StringBuilder userPhoneNumber = new StringBuilder();
        userPhoneNumber.append(ed_country_code.getText().toString());
        userPhoneNumber.append("-");
        userPhoneNumber.append(ed_area_code.getText().toString());
        userPhoneNumber.append("-");
        userPhoneNumber.append(ed_phone_number.getText().toString());


        LoadingBox.showLoadingDialog(CharityRegistratonActivity.this, "");
        //    EditText userNameEditText,emailEditText, passwordEditText, confirmpasswordEditText, notForProfitEditText, purposeOfCharityEditText,
        //     notForProfitRegisteredNumberEditText, descriptionEditText, ed_location, ed_country_code,
        //               businessRegNumberEditText, primarycontactEditText, websiteEditText;

        RestClient.getApiServiceForPojo().CharityRegistration(encodedString, ed_firstName.getText().toString(), ed_lastName.getText().toString(),
                userNameEditText.getText().toString(), emailEditText.getText().toString(),
                passwordEditText.getText().toString(), notForProfitEditText.getText().toString(), purposeOfCharityEditText.getText().toString(),
                CharityTypeIdsCommaSeparated, ed_city.getText().toString(),
                mLocation/*,notForProfitRegisteredNumberEditText.getText().toString()*/, descriptionEditText.getText().toString(),
                userPhoneNumber.toString(), businessRegNumberEditText.getText().toString(), websiteEditText.getText().toString(),
                mCountry, mUserRoleId,
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
                            Show(ResponseMessage, ResponseCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Forgot password", " = " + e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Charity Failure => ", " = " + error.getMessage());

                        MessageDialog.showFailureDialog(CharityRegistratonActivity.this);

                    }

                });
    }

    // Show dialog
    public void Show(String message, final String responseCode) {
        try {

            final Dialog dialog = new Dialog(CharityRegistratonActivity.this,
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
                    .findViewById(R.id.text_msg);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText(message);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (responseCode.equals("1")) {
                        finish();
                        Intent in = new Intent();
                        in.setClass(CharityRegistratonActivity.this, LoginActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
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
        in.setClass(CharityRegistratonActivity.this, LoginActivity.class);
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

  /*  // Getting Location
    public String getLocationParam() {
        LoadingBox.showLoadingDialog(CharityRegistratonActivity.this, "Please wait");
        handler.postDelayed(runnable, LOGOUT_INTERVAL);
        final String[] ar = new String[1];
        final double[] lat = new double[2];
        locationmanager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            networkProvider = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            gpsProvider = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CharityRegistratonActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CharityRegistratonActivity.this,
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
    }*/

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
                            ed_location.setText(mLocation);
                            // break;
                        }
                        if (result.getAddress_components().get(x).getTypes().get(0).equals("country")) {
                            Log.e("country", "===" + result.getAddress_components().get(x).getLong_name());
                            mCountry = result.getAddress_components().get(x).getLong_name();
                            mLocation = mCity + "," + mCountry;
                            ed_location.setText(mLocation);
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
                MessageDialog.showFailureDialog(CharityRegistratonActivity.this);
            }
        });
        return address;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                try {
                    String imgPath = result.getUri().getPath();

                    if (imgPath != null) {
                        CommonUtility.VoucherBitMap = BitmapFactory.decodeFile(imgPath);
                        encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        iv_profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath, CommonUtility.dpToPx(this, 140),
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
                            LoadingBox.showLoadingDialog(CharityRegistratonActivity.this, "Please wait");
                            handler.postDelayed(runnable, LOGOUT_INTERVAL);
                            if (!mGoogleApiClient.isConnected()) {
                                mGoogleApiClient.connect();
                            }

                        } else {
                            gpsDialog.dismiss();
                            Toast.makeText(CharityRegistratonActivity.this, "Location Service is required to register", Toast.LENGTH_LONG).show();
                            //        finish();
                            //     CharityRegistratonActivity.this.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                        }
                    } catch (Exception ex) {

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
                        iv_profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath, CommonUtility.dpToPx(this, 140),
                                CommonUtility.dpToPx(this, 140)));
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
                    iv_profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(orientedImagePath, CommonUtility.dpToPx(this, 140),
                            CommonUtility.dpToPx(this, 140)));

                    UploadImageDialog.mImageUri = null;

                } catch (Exception ex) {

                }
                break;
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


    @Override
    public void onConnected(Bundle bundle) {
        try {
            LoadingBox.showLoadingDialog(CharityRegistratonActivity.this, "Please wait");
            handler.postDelayed(runnable, LOGOUT_INTERVAL);
            mLocationRequest = createLocationRequest(10000, 10000);
            if (mGoogleApiClient.isConnected()) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CharityRegistratonActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            CommonUtility.MY_PERMISSION_ACCESS_LOCATION);
                    LoadingBox.dismissLoadingDialog();
                    handler.removeCallbacks(runnable);
                } else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CharityRegistratonActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            CommonUtility.MY_PERMISSION_ACCESS_LOCATION);
                } else {
                    fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                }

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
                MessageDialog.showDialog(CharityRegistratonActivity.this, "Sorry fail to find location try again. ", true);
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
                        LoadingBox.showLoadingDialog(CharityRegistratonActivity.this, "Please wait");
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
                        Toast.makeText(CharityRegistratonActivity.this, "This is a required for the app to function", Toast.LENGTH_LONG).show();


                    }
                    break;
            }
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
    }
}
