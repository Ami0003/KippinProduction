package com.kippin.activities;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippin.app.Kippin;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.DatePickerSelector;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.XMLParser;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModalIncomeStatement;
import com.kippin.webclient.model.TemplateData;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.kippin.utils.Utility.parseDateByHiphenYMD;

public class ActivitySelectDateRange extends SuperActivity implements DatePickerSelector.onDatePickerSelector {

    // @Bind(R.id.tvSelectStartDate)
    TextView tvSelectStartDate;
    private int yearPicker = -1, monthOfYearPicker = -1, dayOfMonthPicker = -1;
    public Calendar newCalendar = Calendar.getInstance();
    //   @Bind(R.id.tvSelectEndDate)
    TextView tvSelectEndDate;
    ImageView ivNext;
    private final int requestCodeStart = 1001, requestCodeEnd = 1002;
    DatePickerSelector datePickerSelectorStart, datePickerSelectorEnd;

    Date start, end;

    SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
    Calendar mCalendarInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_range);
        initialiseUI();
        setUpListeners();
        yearPicker = newCalendar.get(Calendar.YEAR);
        monthOfYearPicker = newCalendar.get(Calendar.MONTH);
        dayOfMonthPicker = newCalendar.get(Calendar.DAY_OF_MONTH);
        generateActionBar(R.string.select_period, true, true);
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        datePickerSelectorStart = new DatePickerSelector(this, this);
        datePickerSelectorEnd = new DatePickerSelector(this, this);
        tvSelectStartDate = (TextView) findViewById(R.id.tvSelectStartDate);
        tvSelectEndDate = (TextView) findViewById(R.id.tvSelectEndDate);
        ivNext = (ImageView) findViewById(R.id.ivNext);
    }

  /*  //@OnClick(R.id.tvSelectStartDate)
    public void onStartDateClick(View v){
        datePickerSelectorStart.show(requestCodeStart);
    }


   // @OnClick(R.id.tvSelectEndDate)
    public void onEndDateClick(View v){
        datePickerSelectorEnd.show(requestCodeEnd);
    }
*/

    @Override
    public void setUpListeners() {
        tvSelectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showStartDate(yearPicker, monthOfYearPicker, dayOfMonthPicker, R.style.Date_Picker_Spinner);

                //datePickerSelectorStart.show(requestCodeStart);
            }
        });

        tvSelectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDate(yearPicker, monthOfYearPicker, dayOfMonthPicker, R.style.Date_Picker_Spinner);
                // datePickerSelectorEnd.show(requestCodeEnd);
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidated()) {

                    WSUtils.hitServiceGetXML(ActivitySelectDateRange.this, Url.getSelectPeriodUrl(tvSelectStartDate.getText().toString(), tvSelectEndDate.getText().toString()), new ArrayListPost(), ModalIncomeStatement.class, new WSInterface() {
                        @Override
                        public void onResult(int requestCode, TemplateData data) {

                            ModalIncomeStatement modalIncomeStatement = (ModalIncomeStatement) data;

                            if (modalIncomeStatement == null || (modalIncomeStatement.getObjExpenseList().length == 0 || modalIncomeStatement.getObjRevenueList().length == 0)) {

                                new DialogBox(ActivitySelectDateRange.this, Kippin.res.getString(R.string.no_record_found), (DialogBoxListener) null);

                                return;
                            }

                            Bundle bundle = new Bundle();
                            bundle.putSerializable(returnName(ModalIncomeStatement.class), (ModalIncomeStatement) data);
                            Utility.startActivity(ActivitySelectDateRange.this, ActivityIncomePieChart.class, bundle, true);
                        }
                    });
                }

            }
        });
    }

    //   @OnClick(R.id.ivNext)
    public void onNextClick(View v) {

        if (isValidated()) {
            WSUtils.hitServiceGet(this, Url.getSelectPeriodUrl(tvSelectStartDate.getText().toString(), tvSelectEndDate.getText().toString()), new ArrayListPost(), ModalIncomeStatement.class, new WSInterface() {
                @Override
                public void onResult(int requestCode, TemplateData data) {

                    ModalIncomeStatement modalIncomeStatement = (ModalIncomeStatement) data;

                    if (modalIncomeStatement == null || (modalIncomeStatement.getObjExpenseList() == null && modalIncomeStatement.getObjRevenueList() == null)) {

                        new DialogBox(ActivitySelectDateRange.this, Kippin.res.getString(R.string.no_record_found), (DialogBoxListener) null);

                        return;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(returnName(ModalIncomeStatement.class), (ModalIncomeStatement) data);
                    Utility.startActivity(ActivitySelectDateRange.this, ActivityIncomePieChart.class, bundle, true);
                }
            });
        }


    }

    private boolean isValidated() {

        int error = -1;


        boolean isValidated = true;

        if (TextUtils.isEmpty(tvSelectStartDate.getText().toString())) {
            isValidated = false;
            error = R.string.start_date_empty;
        } else if (TextUtils.isEmpty(tvSelectEndDate.getText().toString())) {
            isValidated = false;
            error = R.string.end_date_empty;
        } else if (end.before(start)) {
            isValidated = false;
            error = R.string.end_date_before_start;
        }

        if (!isValidated)
            new DialogBox(this, getString(error), (DialogBoxListener) null);

        return isValidated;
    }


    @Override
    public void onDateSelected(int textId, int year, int monthOfYear, int dayOfMonth) {
        String date = parseDateByHiphenYMD(dayOfMonth, monthOfYear + 1, year);
        switch (textId) {
            case requestCodeStart:
                start = new Date(year, monthOfYear, dayOfMonth);
                tvSelectStartDate.setText(formatDate(date));

                //End date should be automatically increament by 1 year. {@15th June, 2017}
                date = Utility.parseDateByHiphenYMD(dayOfMonth, monthOfYear + 1, ++year);
                end = new Date(year, monthOfYear, dayOfMonth);
                tvSelectEndDate.setText(formatDate(date));
                break;

            case requestCodeEnd:
                end = new Date(year, monthOfYear, dayOfMonth);
                tvSelectEndDate.setText(formatDate(date));
                break;

        }
    }

    /**
     * Implements a method to RETURN the name of MONTH from
     * specific date format.
     *
     * @param strDate : Example => "2009-11-30T18:30:00Z"
     * @return strDate  : MMM [NOV]
     */
    public String formatDate(String strDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        outputFormat = new SimpleDateFormat("MM-dd-yyyy");

        try {
            Date date = inputFormat.parse(strDate);
            strDate = outputFormat.format(date);
        } catch (ParseException exp) {
            exp.printStackTrace();
        }
        return strDate;
    }
    @VisibleForTesting
    void showStartDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(ActivitySelectDateRange.this)
                .callback(startDateClick)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }
    @VisibleForTesting
    void showEndDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(ActivitySelectDateRange.this)
                .callback(endDateClick)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    public DatePickerDialog.OnDateSetListener startDateClick = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = parseDateByHiphenYMD(dayOfMonth, monthOfYear + 1, year);
            start = new Date(year, monthOfYear, dayOfMonth);
            tvSelectStartDate.setText(formatDate(date));

            //End date should be automatically increament by 1 year. {@15th June, 2017}
            date = Utility.parseDateByHiphenYMD(dayOfMonth, monthOfYear + 1, ++year);
            end = new Date(year, monthOfYear, dayOfMonth);
            tvSelectEndDate.setText(formatDate(date));
        }
    };
    public DatePickerDialog.OnDateSetListener endDateClick = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = parseDateByHiphenYMD(dayOfMonth, monthOfYear + 1, year);
            end = new Date(year, monthOfYear, dayOfMonth);
            tvSelectEndDate.setText(formatDate(date));
        }
    };
    /*class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                XMLParser parser = new XMLParser();
                String xml = parser.getXmlFromUrl("http://52.27.249.143/Kippin/Finance/KippinFinanceApi/GeneralLedger/IncomeReport/61/05-15-2015/05-15-2016"); // getting XML
                Document doc = parser.getDomElement(xml); // getting DOM element

                return doc.toString();
            } catch (Exception e) {
                this.exception = e;

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            Log.e("onPostExecute", "Feed "+ feed);
            ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
        }
    }*/
}
