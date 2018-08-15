package com.kippinretail;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.kippinretail.Modal.UserAddress.MyGeoCoder;
import com.kippinretail.Modal.UserAddress.Results;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/*import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;*/

/**
 * Created by kamaljeet.singh on 3/5/2016.
 */
public class UserRegistrationActivity extends SuperActivity implements View.OnClickListener
        , GoogleApiClient.OnConnectionFailedListener
        , com.google.android.gms.location.LocationListener
        , GoogleApiClient.ConnectionCallbacks {

    EditText firstNameEditText, lastNameEditText, userNameEditText, emailEditText, passwordEditText, confirmpasswordEditText, dobEditText,
            locationEditText, phoneNumberEditText, ed_city;
    Spinner ageSpinner, genderSpinner;
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
    private ArrayList<String> listForYears;
    private String[] mStringArray;
    private int year;
    private int month;
    private int day;
    private String mDOB;
    static int DATE_PICKER_ID = 1111;
    private String mAgeValue = "";
    private String mGenderValue = "";
    private String mLocation;
    private String mUserRoleId = "2";
    private CircleImageView profileImage;
    private String encodedString = null;
    private String country = null;
    DatePickerDialog dialog = null;
    ;
    Dialog gpsDialog;
    private final long LOGOUT_INTERVAL = 1000 * 50;
    //private GoogleApiClient mGoogleApiClient = null;
    //  private FusedLocationProviderApi fusedLocationProviderApi = null;


    // CODE FOR gOOGLE API CLIENT

    GoogleApiClient mGoogleApiClient = null;

    private FusedLocationProviderApi fusedLocationProviderApi;
    private LocationRequest mLocationRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        Log.e("USER Regriartion", "USER Regriartion");
        Initlization();
        updateUI();
        addListener();
        hideKeyboard();


        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
    }


    private void updateUI() {


    }

    private void addListener() {
        profileImage.setOnClickListener(this);
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (!ageSpinner.getSelectedItem().toString().equals("Age")) {
                    mAgeValue = ageSpinner.getSelectedItem().toString();
                    if ((TextView) parent.getChildAt(0) != null) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    }

                } else {
                    if ((TextView) parent.getChildAt(0) != null) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#A7A7A7"));
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        locationEditText.setKeyListener(null);
    }

    // function to hide keyboard on login screen
    private void hideKeyboard() {

        // Check if no view has focus:
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void Initlization() {
        SelectUserTypeActivity.plus = 2;
        generateActionBar(R.string.title_user_register, true, false, false);
        generateRightText("", null);
        // EditText ID's

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.oldPasswordEditText);
        confirmpasswordEditText = (EditText) findViewById(R.id.confirmpasswordEditText);
        dobEditText = (EditText) findViewById(R.id.dobEditText);
        ed_city = (EditText) findViewById(R.id.ed_city);
        dobEditText.setFocusable(false);
        dobEditText.setClickable(true);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        locationEditText.setEnabled(false);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        ageSpinner = (Spinner) findViewById(R.id.ageSpinner);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
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
        profileImage = (CircleImageView) findViewById(R.id.profileImage);


        String terms_conditions = getString(R.string.terms_conditions);
        SpannableString ss = new SpannableString(terms_conditions);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                CommonUtility.startNewActivity(UserRegistrationActivity.this, ActivityTermsAndConditions.class);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 26, 45, 0);

        termsConditionsTextView.setText(ss);
        termsConditionsTextView.setMovementMethod(LinkMovementMethod.getInstance());
        termsConditionsTextView.setLinkTextColor(Color.parseColor("#3195DB"));

        // Get current date by calender
        dobEditText.setOnClickListener(this);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Gender
        String[] Industry = getResources().getStringArray(R.array.Gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, Industry);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!genderSpinner.getSelectedItem().toString().equals("Gender")) {
                    mGenderValue = genderSpinner.getSelectedItem().toString();
                    if ((TextView) parent.getChildAt(0) != null) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    }

                } else {
                    if ((TextView) parent.getChildAt(0) != null) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#A7A7A7"));
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // Age
        listForYears = new ArrayList<String>();

        for (int i = 17; i <= 98; i++) {
            if (i == 17) {
                listForYears.add("" + "Age");
            } else {
                listForYears.add("" + i);
            }
        }
        mStringArray = new String[listForYears.size()];
        mStringArray = listForYears.toArray(mStringArray);
        ArrayAdapter<String> adapterAGE = new ArrayAdapter<String>(this, R.layout.spinner_item, mStringArray);
        ageSpinner.setAdapter(adapterAGE);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.registerButton:
                ///EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmpasswordEditText, dobEditText,
                // locationEditText, phoneNumberEditText;
                if (firstNameEditText.getText().toString().length() != 0 && lastNameEditText.getText().toString().length() != 0 &&
                        userNameEditText.getText().toString().length() != 0 && emailEditText.getText().toString().length() != 0
                        && passwordEditText.getText().toString().length() != 0
                        && confirmpasswordEditText.getText().toString().length() != 0 && dobEditText.getText().toString().length() != 0
                        && phoneNumberEditText.getText().toString().length() != 0 && !mAgeValue.equals("") && !mGenderValue.equals("")) {
                    if (passwordEditText.getText().toString().length() < 6) {
                        CommonDialog.With(UserRegistrationActivity.this).Show("Password should be of minimum 6 characters.");
                    } else {
                        if (phoneNumberEditText.getText().toString().length() != 10) {
                            MessageDialog.showDialog(UserRegistrationActivity.this, "enter 10 digit mobile no", false);
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
                                            userRegistration();
                                        } else {
                                            userRegistration();
                                        }

                                    } else {
                                        CommonDialog.With(UserRegistrationActivity.this).Show("Please enter valid email id..");


                                    }
                                } else {
                                    CommonDialog.With(UserRegistrationActivity.this).Show("Please read and accept Terms & Conditions.");
                                }

                            } else {
                                CommonDialog.With(UserRegistrationActivity.this).Show("Password mismatch here.Please confirm same password.");
                            }
                        }
                    }
                } else {
                    int i = 10;
                    CommonDialog.With(UserRegistrationActivity.this).Show("Please fill the empty fields.");
                }


                break;
            case R.id.alreadyRegisterButton:
                InvokeLogin();
                break;


            case R.id.dobEditText:
                //      if(dialog==null)
                showDialog(++DATE_PICKER_ID);


//                        if(dialog!=null){
//                            if(mAgeValue!=""){
//                                Calendar calendar = Calendar.getInstance();
//                                int currentYear = calendar.get(Calendar.YEAR);
//                                calendar.set(Calendar.MONTH, 0);
//                                calendar.set(Calendar.YEAR, currentYear - Integer.parseInt(mAgeValue));
//                                Date minDate = calendar.getTime();
//                                dialog.getDatePicker().setMinDate(minDate.getTime());
//
//                            }else{
//                                Calendar calendar = Calendar.getInstance();
//                                int currentYear = calendar.get(Calendar.YEAR);
//                                calendar.set(Calendar.MONTH, 0);
//                                calendar.set(Calendar.YEAR, currentYear - 18);
//                                Date minDate = calendar.getTime();
//                                dialog.getDatePicker().setMinDate(minDate.getTime());
//                            }
//                        }
//                        showDialog(DATE_PICKER_ID);
//                }
//


                break;

            case R.id.termsConditionsTextView:
                CommonUtility.startNewActivity(this, ActivityTermsAndConditions.class);
                break;
            case R.id.profileImage:
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
        // UploadImageDialog.showUploadImageDialog(UserRegistrationActivity.this, getApplicationContext());
    }

    private boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }


    private void userRegistration() {


        LoadingBox.showLoadingDialog(UserRegistrationActivity.this, "");
        HashMap<String, String> jsonBody = new HashMap<String, String>();
        jsonBody.put("ProfileImage", encodedString);
        jsonBody.put("Firstname", firstNameEditText.getText().toString());
        jsonBody.put("Lastname", lastNameEditText.getText().toString());
        jsonBody.put("Username", userNameEditText.getText().toString());
        jsonBody.put("Email", emailEditText.getText().toString());
        jsonBody.put("Password", passwordEditText.getText().toString());
        jsonBody.put("Age", mAgeValue);
        jsonBody.put("Gender", mGenderValue);
        jsonBody.put("Dob", mDOB);
        jsonBody.put("City", ed_city.getText().toString());
        jsonBody.put("Location", mLocation);
        jsonBody.put("BusinessPhoneNumber", phoneNumberEditText.getText().toString());
        jsonBody.put("Country", mCountry);
        jsonBody.put("RoleId", CommonUtility.userRoleId);
        RestClient.getApiServiceForPojo().UserRegistration(jsonBody,
                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement userRegister, Response response) {
                        Log.e(encodedString, "==");
                        Log.e("Tag", "Request data " + new Gson().toJson(userRegister));
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
                        LoadingBox.dismissLoadingDialog();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Log.e("Login Failure => ", " = " + error.getMessage());
                        Log.e("URL", error.getUrl());
                        MessageDialog.showDialog(UserRegistrationActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
                        LoadingBox.dismissLoadingDialog();
                    }

                });
    }


    // Network error dialog

    private void showGPSDisabledAlertToUser() {
        // final Dialog
        try {

            gpsDialog = new Dialog(UserRegistrationActivity.this,
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
                    Toast.makeText(UserRegistrationActivity.this, "Location Service is required to register", Toast.LENGTH_LONG).show();
                    //finish();
                    //   UserRegistrationActivity.this.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                }
            });
            gpsDialog.show();
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

    // Show dialog
    public void Show(String message, final String responseCode) {
        try {

            final Dialog dialog = new Dialog(UserRegistrationActivity.this,
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
                        in.setClass(UserRegistrationActivity.this, LoginActivity.class);
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


    @Override
    protected Dialog onCreateDialog(int id) {
        DatePickerDialog dialog = null;
//        switch (id) {
//            case DATE_PICKER_ID:

        if (mAgeValue.equals("")) {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            cal.set(Calendar.MONTH, 0);
            month = cal.get(Calendar.MONTH);
            cal.set(Calendar.DATE, 1);
            day = cal.get(Calendar.DATE);
            year = year - 18;
            cal.set(year, month, day);
            // dialog = new DatePickerDialog(this, pickerListener, year, month, day);
            dialog = new DatePickerDialog(this, pickerListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dialog.getDatePicker().setMaxDate(cal.getTime().getTime());

        } else {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            cal.set(Calendar.MONTH, 0);
            month = cal.get(Calendar.MONTH);
            cal.set(Calendar.DATE, 1);
            day = cal.get(Calendar.DATE);
            year = year - Integer.parseInt(mAgeValue);
            cal.set(year, month, day);
            //dialog = new DatePickerDialog(this, pickerListener, year, month, day);
            dialog = new DatePickerDialog(this, pickerListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dialog.getDatePicker().setMinDate(cal.getTime().getTime());
            cal.add(Calendar.DATE, 364);
            dialog.getDatePicker().setMaxDate(cal.getTime().getTime());


        }
        return dialog;
//        }
//        return  null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {


        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            mDOB = formatDate(year, (month), day);
            //          Calendar calendar = Calendar.getInstance();
//            int currentYear = calendar.get(Calendar.YEAR);
//            Log.e("mDOB", "=" + mDOB);
            dobEditText.setText(setFormatDate(year, month, day));

        }
    };

    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        return sdf.format(date);
    }

    private static String setFormatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        return sdf.format(date);
    }

    private void InvokeLogin() {
        finish();
        Intent in = new Intent();
        in.setClass(UserRegistrationActivity.this, LoginActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    // Getting Location
     /* public String getLocationParam() {
        LoadingBox.showLoadingDialog(UserRegistrationActivity.this, "Please wait");
          handler.postDelayed(runnable, LOGOUT_INTERVAL);
          Log.e("Handler Set","Handler Set");
          final String[] ar = new String[1];
          final double[] lat = new double[2];
          locationmanager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);


          try {
              networkProvider = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
              gpsProvider = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
              if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                  ActivityCompat.requestPermissions(UserRegistrationActivity.this,
                          new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                          MY_PERMISSION_ACCESS_COARSE_LOCATION);
              }
              if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                  ActivityCompat.requestPermissions(UserRegistrationActivity.this,
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

                       *//*   Toast.makeText(UserRegistrationActivity.this,"Location",Toast.LENGTH_LONG).show();*//*
                          lattitude = location.getLatitude();
                          longitude = location.getLongitude();
                          compres = getCurrentLocation();
                          if (compres != null)
                          {
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
                address = null;
                //https://maps.googleapis.com/maps/api/geocode/json?latlng=43.45%2C-79.6833&sensor=true&key=AIzaSyBSiOFCcTEJZKOPyb6g3TFuRj4zE3iCx4s
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
                            country = mCountry = result.getAddress_components().get(x).getLong_name();
                            mLocation = mCity + "," + mCountry;
                            locationEditText.setText(mCity + "," + mCountry);
                        }
                    }

                    handler.removeCallbacks(runnable);
                    LoadingBox.dismissLoadingDialog();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Google Api Error", error.getMessage());
                MessageDialog.showDialog(UserRegistrationActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
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
              /*  Toast.makeText(
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
                            LoadingBox.showLoadingDialog(UserRegistrationActivity.this, "Please wait");
                            handler.postDelayed(runnable, LOGOUT_INTERVAL);
                            if (!mGoogleApiClient.isConnected()) {
                                mGoogleApiClient.connect();
                            }

                        } else {
                            gpsDialog.dismiss();
                            Toast.makeText(UserRegistrationActivity.this, "Location Service is required to register", Toast.LENGTH_LONG).show();
                            //   finish();
                            //  UserRegistrationActivity.this.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
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
                        profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath, CommonUtility.dpToPx(this, 130),
                                CommonUtility.dpToPx(this, 130)));
                    }
                } catch (Exception e) {

                }
                break;
            case CommonUtility.REQUEST_CODE_TAKE_PICTURE:
                try {
                    getContentResolver().notifyChange(UploadImageDialog.mImageUri, null);
                    CommonUtility.VoucherBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), UploadImageDialog.mImageUri);
                    ;
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
                    profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(orientedImagePath, CommonUtility.dpToPx(this, 130),
                            CommonUtility.dpToPx(this, 130)));
                    UploadImageDialog.mImageUri = null;

                } catch (Exception ex) {

                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        String imgString = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

        byte[] byteFormat = stream.toByteArray();
        imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    @Override
    protected void onPause() {
        super.onPause();
        CommonUtility.removeFocus(this);
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
            Log.e("====", ex.getMessage());
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
            LoadingBox.showLoadingDialog(UserRegistrationActivity.this, "Please wait");
            handler.postDelayed(runnable, LOGOUT_INTERVAL);
            mLocationRequest = createLocationRequest(10000, 10000);
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UserRegistrationActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        CommonUtility.MY_PERMISSION_ACCESS_LOCATION);
                LoadingBox.dismissLoadingDialog();
                handler.removeCallbacks(runnable);
            } else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UserRegistrationActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        CommonUtility.MY_PERMISSION_ACCESS_LOCATION);
            } else {
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
                MessageDialog.showDialog(UserRegistrationActivity.this, "Sorry fail to find location try again. ", true);
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
                        LoadingBox.showLoadingDialog(UserRegistrationActivity.this, "Please wait");
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
                        Toast.makeText(UserRegistrationActivity.this, "This is a required for the app to function", Toast.LENGTH_LONG).show();


                    }
                    break;
            }
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
    }
}
