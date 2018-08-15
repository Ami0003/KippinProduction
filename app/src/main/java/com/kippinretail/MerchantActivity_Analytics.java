package com.kippinretail;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.kippin.utils.Utility.parseDateByHiphenYMD;


abstract public class MerchantActivity_Analytics extends SuperActivity implements AdapterView.OnClickListener {
    LinearLayout layout_container_analytics_search ,layout_searchButton;
    RelativeLayout toDateRelativeLayout, fromDateRelativeLayout,layout_country;
    TextView fromDateTextView, toDateTextView;
    TextView txt_col1,txt_col2,txt_col3;
    Button searchButton,searchButtonForRevenue;

    ListView listForAnalysis;
    private int day,month,year;
    private int day_Set,month_Set,year_Set;
    private String mFromDate,mToDate;
    private String dateValue;
    static final int DATE_PICKER_ID = 1111;
   Spinner sp_selectPoints;;
    public String getmFromDate() {
        return mFromDate;
    }

    public String getmToDate() {
        return mToDate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_activity__analytics);
        Calendar calendar = Calendar.getInstance();
        year_Set = calendar.get(Calendar.YEAR);
        month_Set =  calendar.get(Calendar.MONTH);
        day_Set =  calendar.get(Calendar.DATE);
        initUI();
        addListener();
    }

    public void initUI(){
        // ELEMENTS FOR ANA;YTICS SEARCHsp_selectPoints
        sp_selectPoints = (Spinner)findViewById(R.id.sp_selectPoints);
        layout_container_analytics_search = (LinearLayout)findViewById(R.id.layout_container_analytics_search);
        layout_searchButton = (LinearLayout)findViewById(R.id.layout_searchButton);
        fromDateRelativeLayout = (RelativeLayout) findViewById(R.id.fromDateRelativeLayout);
        toDateRelativeLayout = (RelativeLayout) findViewById(R.id.toDateRelativeLayout);
        layout_country = (RelativeLayout) findViewById(R.id.layout_country);
        fromDateTextView = (TextView) findViewById(R.id.fromDateTextView);
        toDateTextView = (TextView) findViewById(R.id.toDateTextView);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButtonForRevenue = (Button) findViewById(R.id.searchButtonForRevenue);
        // ELEMENTS FOR TABLE HEADER
        txt_col1 = (TextView) findViewById(R.id.txt_col1);
        txt_col2 = (TextView) findViewById(R.id.txt_col2);
        txt_col3 = (TextView) findViewById(R.id.txt_col3);

        listForAnalysis = (ListView) findViewById(R.id.listForAnalysis);
    }
    abstract void updateUI();
    public  void addListener(){
        fromDateRelativeLayout.setOnClickListener(this);
        toDateRelativeLayout.setOnClickListener(this);
        fromDateTextView.setOnClickListener(this);
        toDateTextView.setOnClickListener(this);
        searchButton.setOnClickListener(this);
    }


    abstract void startSearch();
     @Override
     public void onClick(View v) {
         switch(v.getId()){
             case R.id.searchButton:
                 SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                 Date from = null;
                 Date to = null;
                 Log.e("mFromDate", "=" + mFromDate);
                 Log.e("mToDate", "=" + mToDate);
                 if (mFromDate != null) {
                     if (mToDate != null) {
                         try {
                             from = sdf.parse(mFromDate);
                             to = sdf.parse(mToDate);
                         } catch (ParseException e) {
                             e.printStackTrace();
                         }

                         if (!from.equals(to)) {
                             if (to.after(from)) {
                                 startSearch();
                             } else {
                                 CommonDialog.With(MerchantActivity_Analytics.this).Show("Start date cannot be greater then end date");
                             }
                         } else {
                             CommonDialog.With(MerchantActivity_Analytics.this).Show("Start date cannot be equal to end date");
                         }

                     } else {
                         CommonDialog.With(MerchantActivity_Analytics.this).Show("Please select end date");
                     }
                 } else {
                     CommonDialog.With(MerchantActivity_Analytics.this).Show("Please select start date");
                 }
                 break;
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
                 //DateTime();
                 dateValue = "To";
                 Calendar c3 = Calendar.getInstance();
                 int year3 = c3.get(Calendar.YEAR);
                 int month3 = c3.get(Calendar.MONTH);
                 int day3 = c3.get(Calendar.DAY_OF_MONTH);
                 //int month_day=month+1;
                 clickDate(year3, month3, day3, R.style.Date_Picker_Spinner);
                 //showDialog(DATE_PICKER_ID);
                 break;
             case R.id.fromDateTextView:
                 dateValue = "From";
                 Calendar c2 = Calendar.getInstance();
                 int year2 = c2.get(Calendar.YEAR);
                 int month2 = c2.get(Calendar.MONTH);
                 int day2 = c2.get(Calendar.DAY_OF_MONTH);
                 //int month_day=month+1;
                 clickDate(year2, month2, day2, R.style.Date_Picker_Spinner);
                 //showDialog(DATE_PICKER_ID);
             break;
             case R.id.toDateTextView:
                 dateValue = "To";
                 Calendar c1 = Calendar.getInstance();
                 int year1 = c1.get(Calendar.YEAR);
                 int month1 = c1.get(Calendar.MONTH);
                 int day1 = c1.get(Calendar.DAY_OF_MONTH);
                 //int month_day=month+1;
                 clickDate(year1, month1, day1, R.style.Date_Picker_Spinner);
                 //showDialog(DATE_PICKER_ID);
                 break;
         }

     }

    private DatePickerDialog.OnDateSetListener picker_Listener = new DatePickerDialog.OnDateSetListener() {

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
            //mDOB = formatDate(year, (month), day);
          //  Log.e("mDOB", "=" + mDOB);
          //  dobEditText.setText(setFormatDate(year, month, day));

        }
    };


    @VisibleForTesting
    void clickDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(MerchantActivity_Analytics.this)
                .callback(startDateClick)
                .spinnerTheme(spinnerTheme)
                //.maxDate(year_Set,month_Set,day_Set)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    public com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener startDateClick = new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = parseDateByHiphenYMD(dayOfMonth, monthOfYear + 1, year);
            Log.d("OnDateSetListener","OnDateSetListener");
            year = year;
            month = monthOfYear;
            day = dayOfMonth;
            if (dateValue.equals("From")) {
                mFromDate = formatDate(year, (month ), day);
                Log.e("mFromDate", "=" + mFromDate);
                fromDateTextView.setText(mFromDate);
                //fromDateTextView.setText(setFormatDate(year, (month), day));
            } else {
                mToDate = formatDate(year, (month), day);
                Log.e("mToDate", "=" + mToDate);
                toDateTextView.setText(mToDate);
                //toDateTextView.setText(setFormatDate(year, (month), day));
            }
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                //start changes...
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month =  calendar.get(Calendar.MONTH);
                day =  calendar.get(Calendar.DATE);

                DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month, day);
                calendar.add(Calendar.DATE,1);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                return dialog;
            //end changes...
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            Log.d("OnDateSetListener","OnDateSetListener");
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            if (dateValue.equals("From")) {
                mFromDate = formatDate(year, (month ), day);
                Log.e("mFromDate", "=" + mFromDate);
                fromDateTextView.setText(mFromDate);
                //fromDateTextView.setText(setFormatDate(year, (month), day));
            } else {
                mToDate = formatDate(year, (month), day);
                Log.e("mToDate", "=" + mToDate);
                toDateTextView.setText(mToDate);
                //toDateTextView.setText(setFormatDate(year, (month), day));
            }
        }
    };

    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();

        //SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        //SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(date);
    }
 }
