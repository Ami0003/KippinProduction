package com.kippinretail;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.Analytic_GiftAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ResponseForGiftAnalytics;
import com.kippinretail.Modal.BankStatementDescriptionAdapter;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.squareup.picasso.Picasso;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.kippin.utils.Utility.parseDateByHiphenYMD;

public class MerchantActivity_GigtCardAnalytics extends SuperActivity implements View.OnClickListener{
//    TextView txt_col1 ,txt_col2 , txt_col3;
    Button searchButton;
    RelativeLayout toDateRelativeLayout, fromDateRelativeLayout ;
    TextView fromDateTextView, toDateTextView;
    private int day,month,year;
    private String mFromDate,mToDate;
    private String dateValue;
    static final int DATE_PICKER_ID = 1111;


    ListView  listFirst, listSecond;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_statement_1);
        initUI();
        updateUI();
        addListener();
    }


    private void initUI(){
//        txt_col1 = (TextView) findViewById(R.id.txt_col1);
//        txt_col2 = (TextView) findViewById(R.id.txt_col2);
//        txt_col3 = (TextView) findViewById(R.id.txt_col3);
        searchButton = (Button) findViewById(R.id.searchButton);
        fromDateRelativeLayout = (RelativeLayout) findViewById(R.id.fromDateRelativeLayout);
        toDateRelativeLayout = (RelativeLayout) findViewById(R.id.toDateRelativeLayout);
        fromDateTextView = (TextView) findViewById(R.id.fromDateTextView);
        toDateTextView = (TextView) findViewById(R.id.toDateTextView);
        listFirst = (ListView) findViewById(R.id.listView1);
        listSecond = (ListView) findViewById(R.id.hListView1);


    }
   private void updateUI() {
        generateActionBar(R.string.tile_merchant_GigtCardAnalytics, true, true, false);


    }


    private void addListener() {
        fromDateRelativeLayout.setOnClickListener(this);
        toDateRelativeLayout.setOnClickListener(this);
        searchButton.setOnClickListener(this);
    }

    void startSearch() {

        String merchantId = "" + CommonData.getUserData(this).getId();
        LoadingBox.showLoadingDialog(activity, "Loading ...");
        RestClient.getApiServiceForPojo().getGiftCardAnalysis(mFromDate, mToDate, merchantId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                android.util.Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());
                Gson gson = new Gson();
                List<ResponseForGiftAnalytics> responseForGiftAnalyticses = gson.fromJson(jsonElement.toString(), new TypeToken<List<ResponseForGiftAnalytics>>() {
                }.getType());
                boolean flag = Utility.isResponseValid(responseForGiftAnalyticses)  ;


                if(flag){
//                    listForAnalysis.setAdapter(new Analytic_GiftAdapter(responseForGiftAnalyticses,MerchantActivity_GigtCardAnalytics.this));
//                  setting both adapter

                    listFirst.setAdapter(new CustomAdapter(responseForGiftAnalyticses));

                    listSecond.setAdapter(new BankStatementDescriptionAdapter(MerchantActivity_GigtCardAnalytics.this  , responseForGiftAnalyticses));

                }else{
                    MessageDialog.showDialog(MerchantActivity_GigtCardAnalytics.this,responseForGiftAnalyticses.get(0).getResponseMessage(),false);
                }


            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                android.util.Log.e("RestClient", error.getUrl());

                MessageDialog.showFailureDialog(MerchantActivity_GigtCardAnalytics.this);
            }
        });
    }

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
                                CommonDialog.With(MerchantActivity_GigtCardAnalytics.this).Show("Start date cannot be greater then end date");
                            }
                        } else {
                            CommonDialog.With(MerchantActivity_GigtCardAnalytics.this).Show("Start date cannot be equal to end date");
                        }

                    } else {
                        CommonDialog.With(MerchantActivity_GigtCardAnalytics.this).Show("Please select end date");
                    }
                } else {
                    CommonDialog.With(MerchantActivity_GigtCardAnalytics.this).Show("Please select start date");
                }
                break;
            case R.id.fromDateRelativeLayout:
                dateValue = "From";
                Calendar c1 = Calendar.getInstance();
                int year1 = c1.get(Calendar.YEAR);
                int month1 = c1.get(Calendar.MONTH);
                int day1 = c1.get(Calendar.DAY_OF_MONTH);
                //int month_day=month+1;
                clickDate(year1, month1, day1, R.style.Date_Picker_Spinner);
               // showDialog(DATE_PICKER_ID);
                break;
            case R.id.toDateRelativeLayout:
                //DateTime();
                dateValue = "To";
                Calendar c2 = Calendar.getInstance();
                int year2= c2.get(Calendar.YEAR);
                int month2 = c2.get(Calendar.MONTH);
                int day2 = c2.get(Calendar.DAY_OF_MONTH);
                //int month_day=month+1;
                clickDate(year2, month2, day2, R.style.Date_Picker_Spinner);
               // showDialog(DATE_PICKER_ID);
                break;
        }

    }
    @VisibleForTesting
    void clickDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(MerchantActivity_GigtCardAnalytics.this)
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

//    Internal Adapter




    class CustomAdapter extends BaseAdapter {

        private List<ResponseForGiftAnalytics> modelBankStatements_;

        public CustomAdapter(  List<ResponseForGiftAnalytics> modelBankStatements_) {
            this.modelBankStatements_=modelBankStatements_;
        }

        @Override
        public int getCount() {
            try {
                return modelBankStatements_.size();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return modelBankStatements_.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        ViewHolder viewHolder;


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ResponseForGiftAnalytics statement = modelBankStatements_.get(position);

            if(true){
                convertView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_bank_statement_date_1, parent, false);
                viewHolder = initialiseElements(viewHolder,convertView,parent);

            }else{
                convertView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_bank_statement_date_1, parent, false);
                viewHolder = (ViewHolder) convertView.getTag();
                convertView.setTag(viewHolder);
            }


            if(viewHolder==null || viewHolder.tvGiftCardTemplate ==null){
                convertView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_bank_statement_date_1, parent, false);
                viewHolder = (ViewHolder) convertView.getTag();
                convertView.setTag(viewHolder);
            }

//            if(position%2==0){
//                convertView.setBackgroundColor((getResources().getColor(R.color.bank_statement_even_items)));
//            }else{
//                convertView.setBackgroundColor(Color.WHITE);
//            }
            if(statement.getTemplatePath()!=null && !statement.equals("")){
                Picasso.with(MerchantActivity_GigtCardAnalytics.this).load(statement.getTemplatePath()).into(viewHolder.tvGiftCardTemplate);
            }

            Double amnt = Double.parseDouble(statement.getGiftcardPrice());
            int valueOfGiftCard = (int)amnt.doubleValue();
            viewHolder.tvValueOfGiftCard.setText(String.valueOf(valueOfGiftCard));
            return convertView;
        }

        public ViewHolder initialiseElements(ViewHolder viewHolder,View convertView,ViewGroup parent){
            viewHolder = new ViewHolder();

            viewHolder .tvGiftCardTemplate = (ImageView)convertView.findViewById(R.id.tvGiftCardTemplate);
            viewHolder .tvValueOfGiftCard = (TextView)convertView.findViewById(R.id.tvValueOfGiftCard);

            convertView.setTag(viewHolder);
            return viewHolder;
        }


        class ViewHolder{
            ImageView tvGiftCardTemplate;
            TextView  tvValueOfGiftCard;

        }

    }

//    Internal Adapter ended





}
