package com.kippinretail;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.ApplicationuUlity.TouchImageView;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.CommonDialog.UploadImageDialog;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.squareup.picasso.Picasso;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedString;

import static com.kippin.utils.Utility.parseDateByHiphenYMD;

/**
 * Created by kamaljeet.singh on 3/29/2016.
 */
public class UploadVoucherActivity extends SuperActivity implements View.OnClickListener {
    TouchImageView voucher_image;
    EditText voucherEditText;
    private RelativeLayout fromDateRelativeLayout;
    private RelativeLayout toDateRelativeLayout;
    Button uploadButton;
    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111;
    private String dateValue;
    private String mFromDate;
    private String mToDate;
    private TextView fromDateTextView;
    private TextView toDateTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_voucher);
        Initlization();
        DateTime();
    }

    private void DateTime() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    private void Initlization() {
        try {
            generateActionBar(R.string.title_user_voucher_detail, true, true, false);
            voucher_image = (TouchImageView) findViewById(R.id.voucher_image);
            //EditText
            voucherEditText = (EditText) findViewById(R.id.voucherEditText);
            //Relativelayout
            fromDateRelativeLayout = (RelativeLayout) findViewById(R.id.fromDateRelativeLayout);
            toDateRelativeLayout = (RelativeLayout) findViewById(R.id.toDateRelativeLayout);
            fromDateTextView = (TextView) findViewById(R.id.fromDateTextView);
            toDateTextView = (TextView) findViewById(R.id.toDateTextView);
            uploadButton = (Button) findViewById(R.id.uploadButton);
            uploadButton.setOnClickListener(this);

            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                voucherEditText.setEnabled(true);
                toDateRelativeLayout.setOnClickListener(this);
                fromDateRelativeLayout.setOnClickListener(this);
                String path  = CommonUtility.getRealPathFromURI(this,CommonUtility.getImageUri(this,CommonUtility.VoucherBitMap));
                voucher_image.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(path,CommonUtility.dpToPx(this,220),CommonUtility.dpToPx(this,200)));
                CommonUtility.VoucherBitMap = null;

            } else {
                String VOUCHER_NAME = extras.getString("VOUCHER_NAME");
                String VOUCHER_IMAGE = extras.getString("VOUCHER_IMAGE");
                String START_DATE = extras.getString("START_DATE");
                //  String sDate[] = START_DATE.split("T");
                String END_DATE = extras.getString("END_DATE");
                //  String eDate[] = END_DATE.split("T");
                voucherEditText.setEnabled(false);
                toDateRelativeLayout.setOnClickListener(null);
                fromDateRelativeLayout.setOnClickListener(null);
                uploadButton.setVisibility(View.GONE);
                if (VOUCHER_NAME != null) {
                    String nVoucher[] = VOUCHER_NAME.split("\\.");
                    voucherEditText.setText(nVoucher[0]);
                }
                fromDateTextView.setText("Start date: " + START_DATE);
                toDateTextView.setText("End date: " + END_DATE);
                Picasso picasso = new Picasso.Builder(this).build();
                picasso.with(UploadVoucherActivity.this)
                        .load(VOUCHER_IMAGE)
                        .resize(CommonUtility.dpToPx(this,220),CommonUtility.dpToPx(this,200))
                        .into(voucher_image);
            }
        }catch(Exception ex){

        }

    }
    @VisibleForTesting
    void clickDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(UploadVoucherActivity.this)
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
            year = year;
            month = monthOfYear;
            day = dayOfMonth;


            if (dateValue.equals("From")) {
                mFromDate = formatDate(year, month, day);
                fromDateTextView.setText(setFormatDate(year, month, day));
            } else {
                mToDate = formatDate(year, month, day);
                toDateTextView.setText(setFormatDate(year, month , day));
            }



        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fromDateRelativeLayout:
                dateValue = "From";
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                //int month_day=month+1;
                clickDate(year, month, day, R.style.Date_Picker_Spinner);
                //showDialog(DATE_PICKER_ID);
                break;
            case R.id.toDateRelativeLayout:
                dateValue = "To";
                Calendar c1 = Calendar.getInstance();
                int year1 = c1.get(Calendar.YEAR);
                int month1 = c1.get(Calendar.MONTH);
                int day1 = c1.get(Calendar.DAY_OF_MONTH);
                //int month_day=month+1;
                clickDate(year1, month1, day1, R.style.Date_Picker_Spinner);
               // showDialog(DATE_PICKER_ID);
                break;
            case R.id.uploadButton:
                if (voucherEditText.getText().toString().length() != 0 && mFromDate != null && mToDate != null) {
                    if(Utils.comaperDates(mFromDate,mToDate)){
                        uploadVoucher();
                    }
                    else{
                        MessageDialog.showDialog(UploadVoucherActivity.this , "Start date cannot be equal to end date",false);
                    }

                } else {
                    CommonDialog.With(UploadVoucherActivity.this).Show("Please fill the empty fields.");
                }
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(this);
    }

    private void uploadVoucher() {
        String merchantId = "" + CommonData.getUserData(UploadVoucherActivity.this).getId();
        String voucherName = voucherEditText.getText().toString();
        Log.e("merchantId", "==" + merchantId);
        Log.e("voucherName", "==" + voucherName);
        Log.e("mFromDate", "==" + mFromDate);
        Log.e("mToDate", "==" + mToDate);
        Log.e("base64Image", "==" + CommonUtility.base64Image.length());
        LoadingBox.showLoadingDialog(UploadVoucherActivity.this, "");

        String json = "{\"VoucherImage\":\"" + CommonUtility.base64Image + "\",\"VoucherName\":\"" + voucherName + "\",\"StartDate\":\"" + mFromDate + "\",\"EndDate\":\"" + mToDate + "\"}";
        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // FooResponse response = foo.postRawJson(in);
        RestClient.getApiServiceForPojo().QueryToUploadVouchers(merchantId, in, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement uploadVoucher, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.i("Upload Voucher Tag", "Request data " + new Gson().toJson(uploadVoucher));
                //{"ResponseCode":1,"UserId":0,"ResponseMessage":"Voucher added successfully."}
                JsonObject jsonObj = uploadVoucher.getAsJsonObject();
                String strObj = jsonObj.toString();
                JSONObject obj = new JSONObject();
                try {
                    JSONObject OBJ = new JSONObject(strObj);
                    String ResponseMessage = OBJ.getString("ResponseMessage");
                    String ResponseCode = OBJ.getString("ResponseCode");
                    if(ResponseCode.equals("1")) {
                        Show(ResponseMessage);
                    }
                    else{
                        CommonDialog.With(UploadVoucherActivity.this).Show(ResponseMessage);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Voucher Tag", " = " + e.toString());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Upload Vouchers Get Failure => ", " = " + error.getMessage());
                MessageDialog.showDialog(UploadVoucherActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);

            }

        });
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

    public void Show(String message) {
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
            textMessage.setTextColor(Color.parseColor("#000000"));
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent();
                    in.setClass(UploadVoucherActivity.this, DashBoardMerchantActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month, day);
                Calendar calendar = Calendar.getInstance();
                Date minDate = calendar.getTime();
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return dialog;
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            if (dateValue.equals("From")) {
                mFromDate = formatDate(year, month, day);
                fromDateTextView.setText(setFormatDate(year, month, day));
            } else {
                mToDate = formatDate(year, month, day);
                toDateTextView.setText(setFormatDate(year, month , day));
            }


        }
    };

    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(
                "MMM dd, yyyy");
        return sdf.format(date);
    }

    private static String setFormatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(date);
    }

    public class TypedJsonString extends TypedString {
        public TypedJsonString(String body) {
            super(body);
        }

        @Override
        public String mimeType() {
            return "application/json";
        }
    }
}
