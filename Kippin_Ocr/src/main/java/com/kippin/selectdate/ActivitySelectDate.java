package com.kippin.selectdate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippin.WheelView.WheelView.OnWheelViewListener;
import com.kippin.bankstatement.ActivityBankStatement;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.utils.wheel.WheelDialog;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.TemplateData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;

public class ActivitySelectDate extends SuperActivity {


    public static final String BANK_ID = "BANK_ID";
    public static final String ACCOUNT_NAME = "account_name";
    public static String ACCOUNT_NUMBER = "account_number";
    public static String USER_ID = "user_id";
    //@Bind(R.id.tvSelectMonth)
    TextView tvSelectMonth;
    //  @Bind(R.id.tvSelectYear)
    TextView tvSelectYear;
    //@Bind(R.id.ivNext)
    ImageView ivNext;
    int currentYear;
    int mon;
    int i;
    private int start;
    private ArrayList<String> year = new ArrayList<>();
    private ArrayList<String> months = new ArrayList();
    private int selectedMonth = 1;
    private String selectedYear = "";

    @Override
    public void initialiseUI() {
        super.initialiseUI();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        ButterKnife.bind(this);

        tvSelectMonth = (TextView) findViewById(R.id.tvSelectMonth);
        tvSelectYear = (TextView) findViewById(R.id.tvSelectYear);
        ivNext = (ImageView) findViewById(R.id.ivNext);

        Date dt = new Date();
        i = 1900 + dt.getYear();
        mon = dt.getMonth();
        currentYear = Calendar.getInstance().get(Calendar.YEAR);

        start = currentYear - 3;
        i = start + 12;
        for (int j = start; j <= i; j++) {
            year.add("" + j);
        }


        /*start=currentYear-3;
        for(int j =0 ; j<= 12;j++){
            year.add(""+start);
            start++;
        }*/
        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");
        generateActionBar(R.string.select_duration, true, true);

        selectedYear = currentYear + "";

        selectedMonth = mon + 1;

        tvSelectYear.setText(selectedYear);
        tvSelectMonth.setText(months.get(mon));
        setUpListeners();
    }


    @Override
    public void setUpListeners() {
        tvSelectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelDialog.showWheelDialog(ActivitySelectDate.this, year, i - start, new OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d("ActivitySelectDate", "selectedIndex: " + selectedIndex + ", item: " + item);
                        selectedYear = item;
                        tvSelectYear.setText(item);
                    }
                });
            }
        });

        tvSelectMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelDialog.showWheelDialog(ActivitySelectDate.this, months, mon, new OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d("ActivitySelectDate", "selectedIndex: " + selectedIndex + ", item: " + item);
                        selectedMonth = selectedIndex + 1;
                        tvSelectMonth.setText(item);
                    }
                });

            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvSelectMonth.getText().toString().isEmpty()) {

                    new DialogBox(ActivitySelectDate.this, getResources().getString(R.string.plz_fill_empty_field), new DialogBoxListener() {
                        @Override
                        public void onDialogOkPressed() {


                        }
                    });
                } else if (selectedYear.isEmpty()) {

                    new DialogBox(ActivitySelectDate.this, getResources().getString(R.string.plz_fill_empty_field), new DialogBoxListener() {
                        @Override
                        public void onDialogOkPressed() {


                        }
                    });

                } else {
//            WSTemplate wsTemplate = new WSTemplate();

//            wsTemplate.context = ActivitySelectDate.this;
                    ArrayListPost templatePosts = new ArrayListPost();
                    templatePosts.add("AccountNumber", getIntent().getStringExtra(ACCOUNT_NUMBER));
                    templatePosts.add("AccountName", getIntent().getStringExtra(ACCOUNT_NAME));
                    templatePosts.add("Month", selectedMonth + "");
                    templatePosts.add("Year", selectedYear);
                    templatePosts.add("BankId", getIntent().getStringExtra(BANK_ID));
                    templatePosts.add("UserId", Singleton.getUser().getId());


//            wsTemplate.templatePosts = templatePosts;
//            new WSHandler(wsTemplate).execute();


                    Utility.getStatement(ActivitySelectDate.this, templatePosts, new WSInterface() {
                        @Override
                        public void onResult(int requestCode, TemplateData data) {

                            ModelBankStatements statements = (ModelBankStatements) data.getData(ModelBankStatements.class);

                    /*if(statements.modelBankStatements.size()==1 && statements.modelBankStatements.get(0).getResponseCode().equalsIgnoreCase(ResponseCode.NO_RECORDS_FOUND)){


                        new DialogBox(ActivitySelectDate.this, statements.modelBankStatements.get(0).getResponseMessage() , new DialogBoxListener() {
                            @Override
                            public void onDialogOkPressed() {
                            }
                        });

                    }else*/
                            {

                                if (statements != null && statements.modelBankStatements != null
                                        && statements.modelBankStatements.size() > 0
                                        && statements.modelBankStatements.get(0).getResponseCode().equalsIgnoreCase("1")) {

                                    Bundle bundle = ActivitySelectDate.this.getIntent().getExtras();
                                    bundle.putString("Month", selectedMonth + "");
                                    bundle.putString("Year", selectedYear + "");
                                    bundle.putString(BANK_ID, getIntent().getStringExtra(BANK_ID));
                                    bundle.putSerializable(ActivityBankStatement.LIST_MODEL_BANK_STATEMENT, statements);

                                    Utility.startActivity(ActivitySelectDate.this, ActivityBankStatement.class, bundle, true);
                                } else {
                                    try {
                                        new DialogBox(ActivitySelectDate.this, statements.modelBankStatements.get(0).getResponseMessage()/*"Data not available right now,Please try later."*/, (DialogBoxListener) null);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                        }
                    });
                }
            }
        });
    }

    // @OnClick(R.id.tvSelectYear)
   /* public void onYearclick(View view){
        WheelDialog.showWheelDialog(ActivitySelectDate.this,year,i-start,new OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d("ActivitySelectDate", "selectedIndex: " + selectedIndex + ", item: " + item);
                selectedYear = item;
                tvSelectYear.setText(item);
            }
        });

    }
*/

//    //@OnClick(R.id.tvSelectMonth)
//    public void onMonthclick(View view){
//
//        WheelDialog.showWheelDialog(ActivitySelectDate.this, months,mon, new OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, String item) {
//                Log.d("ActivitySelectDate", "selectedIndex: " + selectedIndex + ", item: " + item);
//                selectedMonth = selectedIndex + 1;
//                tvSelectMonth.setText(item);
//            }
//        });
//
//
//    }


    //@OnClick(R.id.ivNext)
    public void onNextClick(View v) {
        if (tvSelectMonth.getText().toString().isEmpty()) {

            new DialogBox(ActivitySelectDate.this, getResources().getString(R.string.plz_fill_empty_field), new DialogBoxListener() {
                @Override
                public void onDialogOkPressed() {


                }
            });
        } else if (selectedYear.isEmpty()) {

            new DialogBox(ActivitySelectDate.this, getResources().getString(R.string.plz_fill_empty_field), new DialogBoxListener() {
                @Override
                public void onDialogOkPressed() {


                }
            });

        } else {
//            WSTemplate wsTemplate = new WSTemplate();

//            wsTemplate.context = ActivitySelectDate.this;
            ArrayListPost templatePosts = new ArrayListPost();
            templatePosts.add("AccountNumber", getIntent().getStringExtra(ACCOUNT_NUMBER));
            templatePosts.add("AccountName", getIntent().getStringExtra(ACCOUNT_NAME));
            templatePosts.add("Month", selectedMonth + "");
            templatePosts.add("Year", selectedYear);
            templatePosts.add("BankId", getIntent().getStringExtra(BANK_ID));
            templatePosts.add("UserId", Singleton.getUser().getId());


//            wsTemplate.templatePosts = templatePosts;
//            new WSHandler(wsTemplate).execute();
//http://52.27.249.143/Kippin/Finance/KippinFinanceApi/GeneralLedger/UserAccount/BankStatementDetails

            Utility.getStatement(ActivitySelectDate.this, templatePosts, new WSInterface() {
                @Override
                public void onResult(int requestCode, TemplateData data) {

                    ModelBankStatements statements = (ModelBankStatements) data.getData(ModelBankStatements.class);

                    /*if(statements.modelBankStatements.size()==1 && statements.modelBankStatements.get(0).getResponseCode().equalsIgnoreCase(ResponseCode.NO_RECORDS_FOUND)){


                        new DialogBox(ActivitySelectDate.this, statements.modelBankStatements.get(0).getResponseMessage() , new DialogBoxListener() {
                            @Override
                            public void onDialogOkPressed() {
                            }
                        });

                    }else*/
                    {

                        if (statements != null && statements.modelBankStatements != null
                                && statements.modelBankStatements.size() > 0
                                && statements.modelBankStatements.get(0).getResponseCode().equalsIgnoreCase("1")) {

                            Bundle bundle = ActivitySelectDate.this.getIntent().getExtras();
                            bundle.putString("Month", selectedMonth + "");
                            bundle.putString("Year", selectedYear + "");
                            bundle.putString(BANK_ID, getIntent().getStringExtra(BANK_ID));
                            bundle.putSerializable(ActivityBankStatement.LIST_MODEL_BANK_STATEMENT, statements);

                            Utility.startActivity(ActivitySelectDate.this, ActivityBankStatement.class, bundle, true);
                        } else {
                            try {
                                new DialogBox(ActivitySelectDate.this, statements.modelBankStatements.get(0).getResponseMessage()/*"Data not available right now,Please try later."*/, (DialogBoxListener) null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
