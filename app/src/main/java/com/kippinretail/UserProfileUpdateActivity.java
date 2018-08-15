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
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.CommonDialog.UploadImageDialog;
import com.kippinretail.Modal.LoginData.LoginDataClass;
import com.kippinretail.Modal.UserAddress.MyGeoCoder;
import com.kippinretail.Modal.UserAddress.Results;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.kippin.utils.Utility.parseDateByHiphenYMD;

/**
 * Created by kamaljeet.singh on 4/4/2016.
 */
public class UserProfileUpdateActivity extends SuperActivity implements View.OnClickListener
        , GoogleApiClient.OnConnectionFailedListener
        , com.google.android.gms.location.LocationListener
        , GoogleApiClient.ConnectionCallbacks {
//{
EditText dobEditText,
        locationEditText, phoneNumberEditText, ed_streetAddress;
    private boolean networkProvider = false;
    private boolean gpsProvider = false;
    private LocationManager locationmanager = null;

    private int year;
    private int month;
    private int day;
    private String mDOB;
    Spinner genderSpinner;
    private EditText firstNameEditText, lastNameEditText, ed_city;
    private String mGenderValue = "";
    private String mLocation, profileImageBase64 = "";
    private String country, roleId, currentUserId;
    private ArrayList<String> listForYears;
    private String[] mStringArray;
    private String compres;
    double lattitude, longitude;
    private String mCity;
    private String mCountry;
    Button updateButton, changePasswordButton;
    private ImageView profileImage;
    private Spinner ageSpinner;
    private String mAgeValue;
    static int DATE_PICKER_ID = 2222;
    DatePickerDialog dialog = null;
    private String imgBase64;
    private String imagePath = "";
    private final long LOGOUT_INTERVAL = 1000 * 70;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
    GoogleApiClient mGoogleApiClient = null;

    private FusedLocationProviderApi fusedLocationProviderApi;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_update);
        Log.e("Profile ", "Update");
        Initlization();
        updateUI();
        addListener();

    }


    private void Initlization() {
        SelectUserTypeActivity.plus = 2;
        generateActionBar(R.string.title_user_profile, true, false, false);
        generateRightText("", null);
        dobEditText = (EditText) findViewById(R.id.dobEditText);
        ed_streetAddress = (EditText) findViewById(R.id.ed_streetAddress);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        ageSpinner = (Spinner) findViewById(R.id.ageSpinner);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        updateButton = (Button) findViewById(R.id.updateButton);
        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        ed_city = (EditText) findViewById(R.id.ed_city);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        // Get current date by calender

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
                    if (parent.getChildAt(0) != null) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    }

                } else {
                    mGenderValue = "";
                    if ((TextView) parent.getChildAt(0) != null) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#A7A7A7"));
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        String gender = CommonData.getUserData(UserProfileUpdateActivity.this).getGender();
        int genderPosition = adapter.getPosition(gender);
        genderSpinner.setSelection(genderPosition);
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
    }

    private Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            profileImage.setImageBitmap(bitmap);
            try {
                imagePath = imgBase64 = CommonUtility.encodeTobase64(bitmap);
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
    };

    private void updateUI() {
        LoginDataClass login = CommonData.getUserData(this);
        String ph = CommonData.getUserData(UserProfileUpdateActivity.this).getBusinessPhoneNumber();
        String dob = CommonData.getUserData(UserProfileUpdateActivity.this).getDob();
        int age = Integer.parseInt(CommonData.getUserData(UserProfileUpdateActivity.this).getAge());
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
        ageSpinner.setSelection(age - 17);

        imagePath = CommonData.getUserData(this).getProfileImage();
        String address = "" + CommonData.getUserData(this).getAddress();
        Log.e("Final Address", "" + address);
        if (address != null) {
            if (address.equals("null")) {

            } else {
                ed_streetAddress.setText(address);
            }
        }

        if (imagePath.length() < 200) {
            if (!imagePath.equals("")) {
                Picasso.with(UserProfileUpdateActivity.this).load(imagePath).placeholder(R.drawable.icon_placeholder).
                        resize(CommonUtility.dpToPx(UserProfileUpdateActivity.this, 130), CommonUtility.dpToPx(UserProfileUpdateActivity.this, 130))
                        .into(mTarget);
            }
        } else { // base 64 image to set on ImageView when i click it from Camera or in My Share Pref I Have Base 64

            byte[] decodedString = Base64.decode(imagePath, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            String path = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, decodedByte));
            profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(path, CommonUtility.dpToPx(this, 130),
                    CommonUtility.dpToPx(this, 130)));
        }
        currentUserId = String.valueOf(CommonData.getUserData(this).getId());
        firstNameEditText.setText(CommonData.getUserData(this).getFirstname());
        lastNameEditText.setText(CommonData.getUserData(this).getLastname());
        country = CommonData.getUserData(this).getCountry();
        roleId = String.valueOf(CommonData.getUserData(this).getRoleId());
        if (dob != null) {
            String[] ar = dob.split("-");
            dobEditText.setText(ar[1] + "-" + ar[2].substring(0, 2) + "-" + ar[0]);
        }
        phoneNumberEditText.setText(CommonData.getUserData(this).getBusinessPhoneNumber());
        ed_city.setText(CommonData.getUserData(this).getCity());
        Utils.hideKeyboard(this);
        ;

        try {
            locationmanager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            networkProvider = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            gpsProvider = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (networkProvider || gpsProvider) {
                LoadingBox.showLoadingDialog(UserProfileUpdateActivity.this, "Please wait");
                handler.postDelayed(runnable, LOGOUT_INTERVAL);
                if (!mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }

            } else {
                showGPSDisabledAlertToUser();
            }
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
    }

    private void addListener() {
        profileImage.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
        dobEditText.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {

        int i1 = 0;
        switch (v.getId()) {
            case R.id.dobEditText:
                clickDate(1972, 0, 1, R.style.Date_Picker_Spinner);

                //  if(dialog==null) {
                //  showDialog(++DATE_PICKER_ID);
//                }
//                else {
//
//                   onCreateDialog(DATE_PICKER_ID);
//               }
                /*                    if(dialog!=null){
                    if(mAgeValue!=""){
                        Calendar calendar = Calendar.getInstance();
                        int currentYear = calendar.get(Calendar.YEAR);
                        calendar.set(Calendar.MONTH, 0);
                        calendar.set(Calendar.YEAR, currentYear - Integer.parseInt(mAgeValue));
                        Date minDate = calendar.getTime();
                        dialog.getDatePicker().setMinDate(minDate.getTime());

                    }else{
                        Calendar calendar = Calendar.getInstance();
                        int currentYear = calendar.get(Calendar.YEAR);
                        calendar.set(Calendar.MONTH, 0);
                        calendar.set(Calendar.YEAR, currentYear - 18);
                        Date minDate = calendar.getTime();
                        dialog.getDatePicker().setMinDate(minDate.getTime());
                    }
                }*/
                //    showDialog(DATE_PICKER_ID);
                //  }
                break;
            case R.id.updateButton:

                if (firstNameEditText.getText().length() != 0 && lastNameEditText.getText().toString().length() != 0 && ed_city.getText().toString().length() != 0
                        && dobEditText.getText().toString().length() != 0 && phoneNumberEditText.getText().toString().length() != 0
                        && phoneNumberEditText.getText().toString().length() != 0 && !mAgeValue.equals("") && !mGenderValue.equals("")
                        ) {
                    UpdateUser();

                } else {
                    CommonDialog.With(UserProfileUpdateActivity.this).Show("Please fill the empty fields.");
                }
                break;
            case R.id.changePasswordButton:
                Intent i = new Intent(UserProfileUpdateActivity.this, ChangePasswordActivity.class);
                startActivity(i);

                break;
            case R.id.profileImage:
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("KIPPIN D CROP")
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setCropMenuCropButtonTitle("Done")
                        .setRequestedSize(400, 400)
                        .setCropMenuCropButtonIcon(R.drawable.ic_tick)
                        .start(this);
                // UploadImageDialog.showUploadImageDialog(UserProfileUpdateActivity.this,getApplicationContext());
                break;
        }
    }

    public int getAge(int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }

    public void onDateSet(DatePicker view, int selectedYear,
                          int selectedMonth, int selectedDay) {
        Toast.makeText(this, "Restet", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {

//        switch (id) {
//            case DATE_PICKER_ID:

        if (mAgeValue != "") {
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.YEAR, currentYear - Integer.parseInt(mAgeValue));
            calendar.set(Calendar.DATE, 1);
            Date minDate = calendar.getTime();
            dialog = new DatePickerDialog(this, pickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            dialog.getDatePicker().setMinDate(minDate.getTime());
            calendar.add(Calendar.DATE, 364);

            dialog.getDatePicker().setMaxDate(calendar.getTime().getTime());

        }/*else{
                    Calendar calendar = Calendar.getInstance();
                    int currentYear = calendar.get(Calendar.YEAR);
                    calendar.set(Calendar.MONTH, 0);
                    calendar.set(Calendar.YEAR, currentYear - Integer.parseInt(mAgeValue));
                    Date minDate = calendar.getTime();
                    dialog = new DatePickerDialog(this, pickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMinDate(minDate.getTime());
                    dialog.getDatePicker().setMaxDate(minDate.getTime());
                }
*/
        return dialog;
        //   }
        //  return null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
           /* dobEditText.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));*/
            mDOB = formatDate(year, (month), day);
            Log.e("mDOB", "=" + mDOB);
            dobEditText.setText(setFormatDate(year, month, day));

        }
    };


    @VisibleForTesting
    void clickDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(UserProfileUpdateActivity.this)
                .callback(startDateClick)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    public com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener startDateClick = new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = parseDateByHiphenYMD(dayOfMonth, monthOfYear + 1, year);
            // start = new Date(year, monthOfYear, dayOfMonth);
            dobEditText.setText(formatDate(year, monthOfYear + 1, dayOfMonth));
        }
    };


    private void updatePref() {
        LoginDataClass obj = CommonData.getUserData(this);
        obj.setProfileImage(imagePath);
        obj.setFirstname(firstNameEditText.getText().toString());
        obj.setLastname(lastNameEditText.getText().toString());
        obj.setAge(mAgeValue);
        obj.setGender(mGenderValue);
        String[] dformat = dobEditText.getText().toString().toString().split(("-"));
        obj.setDob(dformat[2] + "-" + dformat[0] + "-" + dformat[1]);
        obj.setLocation(locationEditText.getText().toString());
        obj.setBusinessPhoneNumber(phoneNumberEditText.getText().toString());
        obj.setCountry(mCountry);
        obj.setCity(ed_city.getText().toString());
        CommonData.saveUserData(this, obj);
    }

    private void UpdateUser() {
//        {
//            "Id":"38",
//                "ProfileImage":"aHR0cDovL2tpcHBpbnJldGFpbC53ZWIxLmFuemxlYWRzLmNvbS9odHRwOi8va2lwcGlucmV0YWls
//            LndlYjEuYW56bGVhZHMuY29tL1Byb2ZpbGVJbWFnZS8yMDE2LTA3LTIwLS0xMi0yMS0xMS0ucG5n
//            ",
//            "Firstname":"Sandeep",
//                "Lastname":"singh",
//                "Age":"21",
//                "Gender":"Male",
//                "Dob":"01-01-1995",
//                "Location":"Chandigarh,India",
//                "BusinessPhoneNumber":"750-8551258",
//                "Country":"india",
//                "RoleId":"2"
//        }
        LoadingBox.showLoadingDialog(UserProfileUpdateActivity.this, "");
        HashMap<String, String> jsonBody = new HashMap<String, String>();
        jsonBody.put("Id", currentUserId);
        jsonBody.put("Firstname", firstNameEditText.getText().toString());
        jsonBody.put("Lastname", lastNameEditText.getText().toString());
        jsonBody.put("Age", mAgeValue);
        jsonBody.put("Gender", mGenderValue);
        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        // Expect4ed MM/dd/yyyy
        String[] dformat = dobEditText.getText().toString().toString().split(("-"));
        /*jsonBody.put("Dob",dformat[2]+"-"+dformat[0]+"-"+dformat[1]);*/ // - Month- Date - Year
        //jsonBody.put("Dob",dformat[2]+"-"+dformat[0]+"-"+dformat[1]);
        Log.e("STRING:",""+dobEditText.getText().toString().toString());
        Log.e("LENGTH:",""+dformat.length);
        Log.e("dformat:",""+dformat);
        Log.e("0:",""+dformat[0]);
        Log.e("1:",""+dformat[1]);
        jsonBody.put("Dob", dformat[1] + "-" + dformat[0] + "-" + dformat[2]);
        jsonBody.put("Location", locationEditText.getText().toString());
        jsonBody.put("BusinessPhoneNumber", phoneNumberEditText.getText().toString());
        jsonBody.put("Country", mCountry);
        jsonBody.put("City", ed_city.getText().toString());
        jsonBody.put("RoleId", CommonUtility.userRoleId);
        jsonBody.put("ProfileImage", imagePath);
        RestClient.getApiServiceForPojo().UpdateUser(jsonBody, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement userRegister, Response response) {
                Log.d("URL", response.getUrl());
                Log.d("OUTPUT", userRegister.toString());
                Log.i("Tag", "Request data " + new Gson().toJson(userRegister));
                JsonObject jsonObj = userRegister.getAsJsonObject();
                String strObj = jsonObj.toString();
                try {
                    JSONObject OBJ = new JSONObject(strObj);
                    String ResponseMessage = OBJ.getString("ResponseMessage");
                    String ResponseCode = OBJ.getString("ResponseCode");
                    if (ResponseMessage.equals("Success")) {
                        MessageDialog.showDialog(UserProfileUpdateActivity.this, "Profile has been successfully Updated ", true);
                        updatePref();
                    } else {
                        MessageDialog.showDialog(UserProfileUpdateActivity.this, ResponseMessage, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("user updated exception response", " = " + e.toString());
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.d("URL", error.getUrl());
                Log.e("user updated Failure => ", " = " + error.getMessage());
                ErrorCodes.checkCode(UserProfileUpdateActivity.this, error);
                MessageDialog.showDialog(UserProfileUpdateActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }

        });
    }


    public void Show(String message, final String responseCode) {
        try {

            final Dialog dialog = new Dialog(UserProfileUpdateActivity.this,
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
                        in.setClass(UserProfileUpdateActivity.this, DashBoardMerchantActivity.class);
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


    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

        return sdf.format(date);
    }

    private static String setFormatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        return sdf.format(date);
    }
    // Network error dialog

    private void showGPSDisabledAlertToUser() {
        try {
            final Dialog dialog = new Dialog(UserProfileUpdateActivity.this,
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


    public String getCurrentLocation() {
        try {
            String latlng;
            latlng = lattitude + "," + longitude;
            String sensor = "true";
            final String[] ar = new String[1];
            RestClient.getMyApiService().getCurrentLocation(latlng, sensor, CommonUtility.getAndroidKey(), new Callback<JsonElement>() {
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
                    Log.e("Google Api Error", error.getMessage());
                    MessageDialog.showDialog(UserProfileUpdateActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
                }
            });
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return mLocation;
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
                        imagePath = imgBase64 = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
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
                        imagePath = imgBase64 = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        //     Bitmap scaledBitmap = Bitmap.createScaledBitmap(CommonUtility.VoucherBitMap,Utils.dpToPx(UserProfileUpdateActivity.this,130),Utils.dpToPx(UserProfileUpdateActivity.this,130),false);
                        //   profileImage.setImageBitmap(scaledBitmap);
                        profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath, CommonUtility.dpToPx(this, 130),
                                CommonUtility.dpToPx(this, 130)));
                    }
                } catch (Exception e) {

                }
                break;
            case CommonUtility.REQUEST_CODE_TAKE_PICTURE:
                try {
                    getContentResolver().notifyChange(UploadImageDialog.mImageUri, null);
                    imagePath = new File(UploadImageDialog.mImageUri.getPath()).getAbsolutePath();
                    Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), UploadImageDialog.mImageUri);
                    // ======================= CODE TO ROTATE ========================
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
                    imagePath = imgBase64 = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                    profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(orientedImagePath, CommonUtility.dpToPx(this, 130),
                            CommonUtility.dpToPx(this, 130)));
                    UploadImageDialog.mImageUri = null;
                } catch (Exception ex) {

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();

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
            LocationRequest mLocationRequest = createLocationRequest(10000, 10000);
            if (mGoogleApiClient.isConnected()) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserProfileUpdateActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSION_ACCESS_COARSE_LOCATION);
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserProfileUpdateActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSION_ACCESS_FINE_LOCATION);
                }
                fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {
        try {

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
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
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
                MessageDialog.showDialog(UserProfileUpdateActivity.this, "Sorry fail to find location try again. ", false);
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
                    break;
            }
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
    }
}
