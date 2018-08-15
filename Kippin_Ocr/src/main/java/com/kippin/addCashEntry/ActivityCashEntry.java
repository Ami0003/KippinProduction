package com.kippin.addCashEntry;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kippin.app.Kippin;
import com.kippin.classification.AdapterCategory;
import com.kippin.classification.ClassificationCallback;
import com.kippin.classification.DialogClassification;
import com.kippin.cloudimages.CloudClickListener;
import com.kippin.keyboard.KeyboardListener;
import com.kippin.keyboard.MyKeyboard;
import com.kippin.kippin.R;
import com.kippin.selectmonthyear.ActivitySelectMonthYear;
import com.kippin.superviews.SuperActivity;
import com.kippin.topbar.FragmentTopbar;
import com.kippin.utils.ImagePickUtility.CameraPicListener;
import com.kippin.utils.ImagePickUtility.CapturePicView;
import com.kippin.utils.ImagePickUtility.OnCloudClickListener;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModelAddCashEntry;
import com.kippin.webclient.model.ModelCategoryList;
import com.kippin.webclient.model.ModelCategoryLists;
import com.kippin.webclient.model.ModelClassification;
import com.kippin.webclient.model.TemplateData;
import com.kippin.webclient.model.category.OnCategoryLoaded;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.kippin.utils.Utility.parseDateByHiphenYMD;

public class ActivityCashEntry extends SuperActivity implements CloudClickListener {

    public static int cashentry = 0;
    static boolean isDataLoaded = false;
    private final int GST = 1;
    private final int PST = 2;
    private final int HST = 3;
    private final int QST = 4;
    //   @Bind(R.id.spCategory)
    Spinner spCategory;
    //  @Bind(R.id.tvDate)
    TextView tvDate;
    //@Bind(R.id.etVendor)
    TextView etVendor;
    //@Bind(R.id.etDescription)
    EditText etDescription;
    //@Bind(R.id.etPurpose)
    EditText etPurpose;
    // @Bind(R.id.etBillTotal)
    EditText etBillTotal;
    // @Bind(R.id.etGST)
    EditText etGst;
    // @Bind(R.id.etHst)
    EditText etHst;
    //@Bind(R.id.etPST)
    EditText etPst;
    //   @Bind(R.id.etQST)
    EditText etQst;

    EditText tvDebit;
    EditText tvCredit;

    //
//    @Bind(R.id.temp)
//    TextView temp;
    //  @Bind(R.id.etComment)
    TextView etComment;
    // @Bind(R.id.btnUploadBill)
    ImageView btnUploadBill;
    // @Bind(R.id.tvSubmit)
    ImageView tvSubmit;
    //   @Bind(R.id.tvMandatory)
    TextView tvMandatory;
    //  @Bind(R.id.llFooter)
    LinearLayout llFooter;
    //  @Bind(R.id.tvClassification)
    TextView etClassification;
    DialogClassification dialogClassification = null;
    boolean isCloudImage = false;
    Bitmap selectedImage = null;
    CapturePicView capturePicView;
    // @Bind(R.id.etCategory)
    TextView tvCategory;
    public static String MONTHSTRING = "";
    public static String YEARSTRING = "";

//    @Bind(R.id.etClassification)
//    TextView etClassification;


    //    @Bind(R.id.etClassificationDetail)
//    TextView tvClassificationDetail;
    //   @Bind(R.id.tvCredit)
    EditText etCredit;
    // @Bind(R.id.tvDebit)
    EditText etDebit;
    //    String selectedClassificationId = "95";
    String selectedClassificationId = "-1";
    //    String selectedCatId = "-1";
    String selectedCatId = "1";
    AdapterCategory adapterCategory = null;

    boolean isDebitSelected = true;

    @VisibleForTesting
    void clickDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(ActivityCashEntry.this)
                .callback(startDateClick)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    public DatePickerDialog.OnDateSetListener startDateClick = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = parseDateByHiphenYMD(dayOfMonth, monthOfYear + 1, year);
            day = dayOfMonth;
            ActivityCashEntry.this.month = monthOfYear;
            ActivityCashEntry.this.year = year;
            setDate();
        }
    };
    ClassificationCallback classificationCallback = new ClassificationCallback() {
        @Override
        public void onClassificatonSelected(ModelClassification modelClassification, int position) {
            etClassification.setText(modelClassification.getClassificationType());

            selectedClassificationId = modelClassification.getId();

            tvCategory.setOnClickListener(null);
            findViewById(R.id.imageView2).setVisibility(View.INVISIBLE);
            spCategory.setVisibility(View.GONE);

            tvCategory.setText(modelClassification.getClassificationType());

//            tvClassificationDetail.setText(modelClassification.getDesc());

            performCategoryOperation(modelClassification.getCategoryId());

        }
    };
    BillWatcher billWatcher = new BillWatcher();
    double total;
    TextWatcher gstWatcher, hstWatcher, pstWatcher, qstWatcher = null;
    HashMap<Integer, Double> taxes = new HashMap<Integer, Double>();
    private ArrayList<ModelCategoryList> categories;
    private MyKeyboard myKeyboard;
    private int year, month, day;
    private String cloudImageName = "";
    private String cloudImagePath;

    /* //@OnClick(R.id.tvSubmit)
     public void onSubmitClick(View view) {
         if (isValidated()) {
             ModelAddCashEntry modelAddCashEntry = new ModelAddCashEntry();

             ArrayListPost templatePosts = new ArrayListPost();

             String s = "";

             try {
                 s = Utility.convertBitmap2Base64(selectedImage);
             } catch (Exception e) {
                 e.printStackTrace();
             }


             double total = Utility.parseDouble(getText(etCredit)) - Utility.parseDouble(getText(etDebit)) -
                     Utility.parseDouble(getText(etGst)) - Utility.parseDouble(getText(etPst))
                     - Utility.parseDouble(getText(etPst)) - Utility.parseDouble(getText(etQst));

             if (total < 0) total = -total;

             templatePosts.add("BillPath", s);
             templatePosts.add("BankId", "6");
 //            templatePosts.add("BillTotal", Utility.parseDouble(getText(etBillTotal)) + "");
             templatePosts.add("BillTotal", Utility.parseDouble(total) + "");
 //            templatePosts.add("CategoryId", Integer.parseInt(categories.get(spCategory.getSelectedItemPosition()).getId()) + "");
             templatePosts.add("CategoryId", selectedCatId + "");
             templatePosts.add("Classification", Kippin.res.getString(R.string.cash));
             templatePosts.add("CloudImageName", cloudImageName);

             templatePosts.add("ClassificationId", selectedClassificationId);


             templatePosts.add("Comment", getText(etComment));
             templatePosts.add("UserId", Integer.parseInt(Singleton.getUser().getId()) + "");
             templatePosts.add("Description", getText(etDescription));
             templatePosts.add("CashBillDate", getText(tvDate));
             templatePosts.add("GSTtax", Utility.parseDouble(Utility.parseDouble(getText(etGst))));
             templatePosts.add("HSTtax", Utility.parseDouble(Utility.parseDouble(getText(etHst))));
             templatePosts.add("QSTtax", Utility.parseDouble(Utility.parseDouble(getText(etQst))));
             templatePosts.add("PSTtax", Utility.parseDouble(Utility.parseDouble(getText(etPst))));
             templatePosts.add("Purpose", getText(etPurpose));
             templatePosts.add("Vendor", getText(etVendor));
             templatePosts.add("ImageName", System.currentTimeMillis() + ".png");
             templatePosts.add("IsCloud", isCloudImage + "");

             templatePosts.add("ImagePath", cloudImagePath);

             templatePosts.add("AccountName", "Cash");
             templatePosts.add("AccountNumber", Utility.accNumber);
             templatePosts.add("Credit", Utility.parseDouble(Utility.parseDouble(getText(etCredit))));
             templatePosts.add("Debit", Utility.parseDouble(Utility.parseDouble(getText(etDebit))));


 //            "UserId":"\(userId!)",
 //                    "BillPath":"\(BASE64!)" as String,
 //            "CategoryId":"\(CategoryId)",
 //                    "Vendor":TextFieldVendor.text!,
 //                    "Description":TextViewDescription.text!,
 //                    "Purpose":TextFieldPurpose.text!,
 //                    "Total":0.0,
 //                    "Comment":TextViewComments.text!,
 //                    "CashBillDate":TextFieldDate.text!,
 //                    "Credit":0.0,
 //                    "Debit":0.0,
 //                    "GSTtax":TextFieldBillTaxG.text!,
 //                    "QSTtax":TextFieldBillTaxQ.text!,
 //                    "HSTtax":TextFieldBillTaxH.text!,
 //                    "PSTtax":TextFieldBillTaxP.text!,
 //                    "BillTotal":TextFieldBillTotal.text!,
 //                    "IsCloud":isCloud!,
 //                    "CloudImageName":IMAGENAME! as! String,
 //                    "ImageName":"test.jpg",
 //                    "BankId":6


             WSUtils.hitService(ActivityCashEntry.this, Url.URL_ADD_CASH_ENTRY, false, templatePosts, WSMethods.POST, TemplateData.class, new WSInterface() {
                 @Override
                 public void onResult(int requestCode, TemplateData data) {

                     new DialogBox(ActivityCashEntry.this, data.getResponseMsg(), new DialogBoxListener() {
                         @Override
                         public void onDialogOkPressed() {
                             Singleton.reloadStatements = true;
                             ActivityCashEntry.this.finish();
                         }
                     });

                 }
             });

         }

     }*/
    private OnCloudClickListener onCloudListener = new OnCloudClickListener() {
        @Override

        public void onClick() {
            Utility.startActivity(ActivityCashEntry.this, ActivitySelectMonthYear.class, true);
            Utility.cloudClickListener = ActivityCashEntry.this;
        }
    };

    private TextWatcher mDebitListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            etCredit.removeTextChangedListener(mCrediListener);
            etCredit.setText("");
            etCredit.addTextChangedListener(mCrediListener);
        }
    };

    private TextWatcher mCrediListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            etDebit.setText("0.00");
        }

        @Override
        public void afterTextChanged(Editable s) {
            etDebit.removeTextChangedListener(mDebitListener);
            etDebit.setText("");
            etDebit.addTextChangedListener(mDebitListener);

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDataLoaded = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_entry);
        Log.e("ActivityCashEntry", "ActivityCashEntry");
        generateActionBar(R.string.add_cash_entry, true, true);

        initialiseUI();
        setUpListeners();
        setUpUI();


    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvCategory = (TextView) findViewById(R.id.etCategory);
        etCredit = (EditText) findViewById(R.id.tvCredit);
        etDebit = (EditText) findViewById(R.id.tvDebit);

        etClassification = (TextView) findViewById(R.id.tvClassification);
        etDebit = (EditText) findViewById(R.id.tvDebit);
        llFooter = (LinearLayout) findViewById(R.id.llFooter);
        tvSubmit = (ImageView) findViewById(R.id.tvSubmit);
        btnUploadBill = (ImageView) findViewById(R.id.btnUploadBill);
        etComment = (TextView) findViewById(R.id.etComment);
        etQst = (EditText) findViewById(R.id.etQST);

        etPst = (EditText) findViewById(R.id.etPST);
        etHst = (EditText) findViewById(R.id.etHst);
        etGst = (EditText) findViewById(R.id.etGST);
        etBillTotal = (EditText) findViewById(R.id.etBillTotal);

        etPurpose = (EditText) findViewById(R.id.etPurpose);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etVendor = (TextView) findViewById(R.id.etVendor);
        spCategory = (Spinner) findViewById(R.id.spCategory);
        capturePicView = new CapturePicView(this);
        capturePicView.addOnCloudClickListener(onCloudListener);
        myKeyboard = new MyKeyboard(this, findViewById(R.id.root), new KeyboardListener() {
            @Override
            public void isKeyboardShowing(boolean isShowing) {

                int visibility = View.VISIBLE;

                if (isShowing) {
                    visibility = View.GONE;
                }

//                myKeyboard.setVisibility(visibility,llFooter);

                llFooter.setVisibility(visibility);
            }
        });
        myKeyboard.addGlobalCallbackListener();


        gstWatcher = new TaxWatcher(etGst, GST);
        hstWatcher = new TaxWatcher(etHst, HST);
        pstWatcher = new TaxWatcher(etPst, PST);
        qstWatcher = new TaxWatcher(etQst, QST);

        dialogClassification = new DialogClassification(this, null);
        dialogClassification.requestClassifications(null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case CapturePicView.REQUEST_CODE_CAMERA:
            case CapturePicView.REQUEST_CODE_GALLERY:
            case CapturePicView.PIC_CROP:
                capturePicView.onActivityResult(requestCode, resultCode, data, new CameraPicListener() {
                    @Override
                    public void onImageSelected(final Bitmap bm) {
                        if (bm == null) {
                            new DialogBox(ActivityCashEntry.this, "Invalid Image", (DialogBoxListener) null);
                            return;
                        } else {
                            Log.e("Month:", "" + CapturePicView.monthString);
                            Log.e("yearString:", "" + CapturePicView.yearString);
                            ActivityCashEntry.this.selectedImage = bm;
                            ActivityCashEntry.this.cloudImageName = "";
                            ActivityCashEntry.this.isCloudImage = false;
                        }
                    }
                });


                break;
        }
    }

    public void setDate() {
        CapturePicView.monthString = "" + (month + 1);
        CapturePicView.yearString = "" + year;

        MONTHSTRING = "" + (month + 1);
        YEARSTRING = "" + year;
        tvDate.setText(Utility.parseDateBySlashes(day, month + 1, year));
    }

    // @OnClick(R.id.tvDate)
    public void onDateClick(View c) {
        Calendar c1 = Calendar.getInstance();
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);

        //int month_day=month+1;
        clickDate(year1, month1, day1, R.style.Date_Picker_Spinner);

       /* new android.app.DatePickerDialog(ActivityCashEntry.this, new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                day = dayOfMonth;
                ActivityCashEntry.this.month = monthOfYear;
                ActivityCashEntry.this.year = year;
                setDate();
            }

        }, year, month, day).show();*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        cashentry = 0;
    }

    // @OnClick(R.id.btnUploadBill)
    public void onUploadBillClick(View c) {
        cashentry = 1;
        capturePicView.show(false);
    }

    private void setUpUI() {

        Date dt = new Date();
        year = 1900 + dt.getYear();
        month = dt.getMonth();
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        setDate();

        etClassification.setText(Kippin.res.getString(R.string.please_select));

        ModelCategoryLists modelCategoryLists = ModelCategoryLists.getInstance();

        modelCategoryLists.loadData(this, new OnCategoryLoaded() {
            @Override
            public void onCategoryLoaded(final ArrayList<ModelCategoryList> modelCategoryLists) {
                categories = modelCategoryLists;

                adapterCategory = new AdapterCategory(ActivityCashEntry.this, modelCategoryLists);

                spCategory.setAdapter(adapterCategory);

                spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String s = ((TextView) view.findViewById(R.id.tvClassification)).getText().toString();

                        Log.e("selected", "hfksd " + s);

                        tvCategory.setText(s);

//                        if (position > 0) {
//                            temp.setVisibility(View.GONE);
//                            adapterCategory.disableDefaultItem();
//                            ((TextView) parent.getChildAt(0)).setVisibility(View.GONE);
//                            spCategory.setOnItemSelectedListener(null);
//                            spCategory.setSelection(position);
//                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                spCategory.setSelection(1);
                spCategory.setEnabled(false);

            }
        });

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String s = ((TextView) view.findViewById(R.id.tvClassification)).toString();

                Log.e("selected", "hfksd " + s);

                tvCategory.setText(s);

//                        if (position > 0) {
//                            temp.setVisibility(View.GONE);
//                            adapterCategory.disableDefaultItem();
//                            ((TextView) parent.getChildAt(0)).setVisibility(View.GONE);
//                            spCategory.setOnItemSelectedListener(null);
//                            spCategory.setSelection(position);
//                        }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private boolean isValidated() {
        boolean isValidated = true;

        String msg = "";

        msg = getResources().getString(R.string.plz_fill_empty_mandatory_);


        /*if (Utility.parseDouble(getText(etCredit)) == 0 && Utility.parseDouble(getText(etDebit)) == 0) {
            isValidated = false;
            msg = "Either credit or debit is mandatory";
        } */
    /*    if (getText(etPurpose).length() == 0) {
            isValidated = false;
        } else */
        if (getText(etDescription).length() == 0) {
            isValidated = false;
        } else if (selectedClassificationId.equalsIgnoreCase("-1")) {
            isValidated = false;
            msg = "Please Select Classification";
        }
        /*else if (etDebit.getText().toString().equalsIgnoreCase(etCredit.getText().toString())) {
            isValidated = false;
            msg = "Please enter only one out of credit or debit.";
        }*/

        else if (etDebit.getText().toString().equals("0.00") && etCredit.getText().toString().equals("0.00")) {
            isValidated = false;
            msg = "Please enter either a withdrawal or deposit amount.";
        } else if (etDebit.getText().toString().length() != 0) {
            if (etCredit.getText().length() != 0) {
                isValidated = false;
                msg = "Please enter either a withdrawal or deposit amount.";
            }
        } else if (etCredit.getText().toString().length() != 0) {
            if (etDebit.getText().length() != 0) {

                isValidated = false;
                msg = "Please enter either a withdrawal or deposit amount.";
            }
        }

        /*else if (Utility.parseDouble(getText(etCredit)) > 0 && Utility.parseDouble(getText(etDebit)) > 0) {
            isValidated = false;
            msg = "Either Credit or Debit is allowed";
        }*/
        else if (
//                TextUtils.isEmpty(getText(etVendor))
//                ||
                TextUtils.isEmpty(getText(etDescription))
                        || TextUtils.isEmpty(getText(etPurpose))
               /* || spCategory.getSelectedItemPosition()==0*/
//                || Utility.parseDouble(getText(etBillTotal)) == 0
                ) {
            isValidated = false;
//            msg = "Please fill mandatory fields";
        } else {

//        double billTotal = Utility.parseDouble(getText(etBillTotal));
//        double taxes = Utility.parseDouble(getText(etGst))
//                + Utility.parseDouble(getText(etQst))
//                +Utility.parseDouble(getText(etHst))
//                +Utility.parseDouble(getText(etPst)) ;
//
//        if(billTotal<taxes){
//            isValidated = false;
//            msg = "Total Tax must be less than Total";
//        }

            double total = Utility.parseDouble(getText(etCredit)) - Utility.parseDouble(getText(etDebit));

            if (total < 0) {
                total = -total;
            }


            double taxes = Utility.parseDouble(getText(etGst))
                    + Utility.parseDouble(getText(etQst))
                    + Utility.parseDouble(getText(etHst))
                    + Utility.parseDouble(getText(etPst));

            if (total < taxes) {
                isValidated = false;
                msg = "Total Tax must be less than Total";
            }
        }

        if (!isValidated) {
            new DialogBox(ActivityCashEntry.this, msg, (DialogBoxListener) null);
            return false;
        }

        return true;
    }

    @Override
    public void onInitialise(FragmentTopbar fragmentTopbar) {
        super.onInitialise(fragmentTopbar);
    }

    @Override
    public void setUpListeners() {

//        tvCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//             spCategory.performClick();
//            }
//        });
        btnUploadBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePicView.show(false);
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c1 = Calendar.getInstance();
                int year1 = c1.get(Calendar.YEAR);
                int month1 = c1.get(Calendar.MONTH);
                int day1 = c1.get(Calendar.DAY_OF_MONTH);

                //int month_day=month+1;
                clickDate(year1, month1, day1, R.style.Date_Picker_Spinner);
                /*new android.app.DatePickerDialog(ActivityCashEntry.this, new android.app.DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        day = dayOfMonth;
                        ActivityCashEntry.this.month = monthOfYear;
                        ActivityCashEntry.this.year = year;
                        setDate();
                    }

                }, year, month, day).show();*/
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("et--------Debit", "" + etDebit.getText().toString());
                Log.e("et--------Credit", "" + etCredit.getText().toString());
                if (isValidated()) {

                    if (etDebit.getText().toString().equals("0.00") && etCredit.getText().toString().equals("0.00")) {

                        String msg = "Please enter either a withdrawal or deposit amount.";
                        new DialogBox(ActivityCashEntry.this, msg, (DialogBoxListener) null);
                        return;
                    }
                    if (etDebit.getText().length() == 0 && etCredit.getText().length() == 0) {

                        String msg = "Please enter either a withdrawal or deposit amount.";
                        new DialogBox(ActivityCashEntry.this, msg, (DialogBoxListener) null);
                        return;
                    }

                    ModelAddCashEntry modelAddCashEntry = new ModelAddCashEntry();

                    ArrayListPost templatePosts = new ArrayListPost();

                    String s = "";

                    try {
                        s = Utility.convertBitmap2Base64(selectedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    double total = Utility.parseDouble(getText(etCredit)) - Utility.parseDouble(getText(etDebit)) -
                            Utility.parseDouble(getText(etGst)) - Utility.parseDouble(getText(etPst))
                            - Utility.parseDouble(getText(etPst)) - Utility.parseDouble(getText(etQst));

                    if (total < 0) total = -total;

                   /* templatePosts.add("BillPath", s);
                    templatePosts.add("BankId", "6");
//            templatePosts.add("BillTotal", Utility.parseDouble(getText(etBillTotal)) + "");
                    templatePosts.add("BillTotal", Utility.parseDouble(total) + "");
//            templatePosts.add("CategoryId", Integer.parseInt(categories.get(spCategory.getSelectedItemPosition()).getId()) + "");
                    templatePosts.add("CategoryId", selectedCatId + "");
                    templatePosts.add("Classification", Kippin.res.getString(R.string.cash));
                    templatePosts.add("CloudImageName", cloudImageName);

                    templatePosts.add("ClassificationId", selectedClassificationId);


                    templatePosts.add("Comment", getText(etComment));
                    templatePosts.add("UserId", Integer.parseInt(Singleton.getUser().getId()) + "");
                    templatePosts.add("Description", getText(etDescription));*/
                   /* templatePosts.add("CashBillDate", getText(tvDate));
                    templatePosts.add("GSTtax", Utility.parseDouble(Utility.parseDouble(getText(etGst))));
                    templatePosts.add("HSTtax", Utility.parseDouble(Utility.parseDouble(getText(etHst))));
                    templatePosts.add("QSTtax", Utility.parseDouble(Utility.parseDouble(getText(etQst))));
                    templatePosts.add("PSTtax", Utility.parseDouble(Utility.parseDouble(getText(etPst))));
                    templatePosts.add("Purpose", getText(etPurpose));
                    templatePosts.add("Vendor", getText(etVendor));
                    templatePosts.add("ImageName", System.currentTimeMillis() + ".png");
                    templatePosts.add("IsCloud", isCloudImage + "");

                    templatePosts.add("ImagePath", cloudImagePath);

                    templatePosts.add("AccountName", "Cash");
                    templatePosts.add("AccountNumber", Utility.accNumber);
                    templatePosts.add("Credit", Utility.parseDouble(Utility.parseDouble(getText(etCredit))));
                    templatePosts.add("Debit", Utility.parseDouble(Utility.parseDouble(getText(etDebit))));*/
                    Log.e("MONTH:", "" + MONTHSTRING);
                    Log.e("YEARSTRING:", "" + YEARSTRING);
                    templatePosts.add("BillPath", s);
                    templatePosts.add("BankId", Utility.bankId);

                    templatePosts.add("Month", MONTHSTRING);
                    templatePosts.add("Year", YEARSTRING);
                    // ActivityCashEntry.this.month ;
                    //ActivityCashEntry.this.year
                    Log.e("Month:", "" + CapturePicView.monthString);
                    Log.e("year:", "" + CapturePicView.yearString);
                    // Log.e("yearString:",""+CapturePicView.yearString);
                    templatePosts.add("CategoryId", selectedCatId + "");
                    //  templatePosts.add("CategoryId", Integer.parseInt(categories.get(spCategory.getSelectedItemPosition()).getId()) + "");
                    templatePosts.add("Classification", Kippin.res.getString(R.string.cash));
                    templatePosts.add("CloudImageName", cloudImageName);
                    templatePosts.add("ClassificationId", selectedClassificationId);
                    templatePosts.add("Comment", getText(etComment));
                    templatePosts.add("UserId", Integer.parseInt(Singleton.getUser().getId()) + "");
                    templatePosts.add("Description", getText(etDescription));
                    templatePosts.add("CashBillDate", getText(tvDate));
                    templatePosts.add("GSTtax", Utility.parseDouble(Utility.parseDouble(getText(etGst))));
                    templatePosts.add("HSTtax", Utility.parseDouble(Utility.parseDouble(getText(etHst))));
                    templatePosts.add("QSTtax", Utility.parseDouble(Utility.parseDouble(getText(etQst))));
                    templatePosts.add("PSTtax", Utility.parseDouble(Utility.parseDouble(getText(etPst))));
                    templatePosts.add("Purpose", getText(etPurpose));
                    templatePosts.add("Vendor", getText(etVendor));
                    templatePosts.add("ImageName", System.currentTimeMillis() + ".png");
                    templatePosts.add("IsCloud", isCloudImage + "");
                    templatePosts.add("AccountName", "Cash");
                    templatePosts.add("AccountNumber", Utility.accNumber);
                    if (etBillTotal.getText().length() == 0 || etBillTotal.getText().toString().equals("0.0")) {
                        if (etCredit.getText().length() > 0 && !etCredit.getText().toString().equals("0.0") && etCredit.getText().length() != 0) {
                            etBillTotal.setText(etCredit.getText().toString());
                        } else if (etDebit.getText().length() > 0 && !etDebit.getText().toString().equals("0.0") && etDebit.getText().length() != 0) {
                            etBillTotal.setText(etDebit.getText().toString());
                        }
                        templatePosts.add("BillTotal", Utility.parseDouble(getText(etBillTotal)) + "");
                    } else {
                        templatePosts.add("BillTotal", Utility.parseDouble(getText(etBillTotal)) + "");
                    }
                    templatePosts.add("Credit", Utility.parseDouble(Utility.parseDouble(getText(etCredit))));
                    templatePosts.add("Debit", Utility.parseDouble(Utility.parseDouble(getText(etDebit))));

                    Log.e("Debit", "" + Utility.parseDouble(Utility.parseDouble(getText(etDebit))));
                    Log.e("Credit", "" + Utility.parseDouble(Utility.parseDouble(getText(etCredit))));
                    Log.e("AccountNumber", "" + Utility.accNumber);
                    Log.e("AccountName", "Cash");
                    Log.e("IsCloud", "" + isCloudImage + "");
                    Log.e("ImageName", "" + System.currentTimeMillis() + ".png");
                    Log.e("Vendor", "" + getText(etVendor));
                    Log.e("Purpose", "" + getText(etPurpose));
                    Log.e("PSTtax", "" + Utility.parseDouble(Utility.parseDouble(getText(etPst))));
                    Log.e("QSTtax", "" + Utility.parseDouble(Utility.parseDouble(getText(etQst))));
                    Log.e("HSTtax", "" + Utility.parseDouble(Utility.parseDouble(getText(etHst))));
                    Log.e("GSTtax", "" + Utility.parseDouble(Utility.parseDouble(getText(etGst))));
                    Log.e("CashBillDate", "" + getText(tvDate));
                    Log.e("Description", "" + getText(etDescription));
                    Log.e("UserId", "" + Integer.parseInt(Singleton.getUser().getId()) + "");
                    Log.e("Comment", "" + getText(etComment));
                    Log.e("ClassificationId", "" + selectedClassificationId);
                    Log.e("CloudImageName", "" + cloudImageName);
                    Log.e("CategoryId", "" + selectedCatId + "");

                    Log.e("Bill-------Total", "" + Utility.parseDouble(getText(etBillTotal)) + "");
                    Log.e("BankId", "" + Utility.bankId);
                    Log.e("Classification", "" + Kippin.res.getString(R.string.cash));
                    WSUtils.hitService(ActivityCashEntry.this, Url.URL_ADD_CASH_ENTRY, false, templatePosts, WSMethods.POST, TemplateData.class, new WSInterface() {
                        @Override
                        public void onResult(int requestCode, TemplateData data) {

                            new DialogBox(ActivityCashEntry.this, data.getResponseMsg(), new DialogBoxListener() {
                                @Override
                                public void onDialogOkPressed() {
                                    Singleton.reloadStatements = true;
                                    ActivityCashEntry.this.finish();
                                }
                            });

                           /* new DialogBox(ActivityCashEntry.this, "Cash entry has been added successfully", new DialogBoxListener() {
                                @Override
                                public void onDialogOkPressed() {
                                    Singleton.reloadStatements = true;
                                    ActivityCashEntry.this.finish();
                                }
                            });*/

                        }
                    });

                }
            }
        });
        etClassification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClassification.show(classificationCallback);
            }
        });

//        addTaxWachers();
//        etBillTotal.setOnFocusChangeListener(billWatcher);

        etCredit.addTextChangedListener(mCrediListener);
        etDebit.addTextChangedListener(mDebitListener);
    }

    public void performCategoryOperation(final String cid) {
        Log.e("URL:", "" + Url.URL_CATEGORY_BY_ID + File.separator + cid);
        WSUtils.hitServiceGet(ActivityCashEntry.this, Url.URL_CATEGORY_BY_ID + File.separator + cid, new ArrayListPost(), ModelCategoryList.class, new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {
                ModelCategoryList modelCategoryList = data.getData(ModelCategoryList.class);
                try {
                    if (cid != null) {
                        tvCategory.setText(modelCategoryList.getCategoryType());
                    }

                    selectedCatId = modelCategoryList.getId();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public boolean billTotalExceeds() {
        double billTotal = total;
        if (getTotalTax() > billTotal) return true;
        return false;
    }

    private void performCalculationOnTotal() {
        if (billTotalExceeds()) {
//            clearTaxes();
            total = Utility.parseDouble(getText(etBillTotal)) + getTotalTax();
//            new DialogBox(this,getString(R.string.error_tax_must_be_less_or_equal_total) , (DialogBoxListener)null);
        }
    }

    private void clearTaxes() {
        removeTaxWachers();
        etHst.setText("");
        etGst.setText("");
        etPst.setText("");
        etQst.setText("");

        taxes = new HashMap<>();

        addTaxWachers();
    }

    private void removeTaxWachers() {
        etGst.removeTextChangedListener(gstWatcher);
        etHst.removeTextChangedListener(hstWatcher);
        etQst.removeTextChangedListener(qstWatcher);
        etPst.removeTextChangedListener(pstWatcher);
    }

    private void addTaxWachers() {
        etGst.addTextChangedListener(gstWatcher);
        etHst.addTextChangedListener(hstWatcher);
        etQst.addTextChangedListener(qstWatcher);
        etPst.addTextChangedListener(pstWatcher);
    }

    @Override
    public void onCloudClick(Bitmap bitmap, String cloudImageName, String cloudImagePath, String month, String year) {

        if (bitmap != null) {
            new DialogBox(ActivityCashEntry.this, "Success", (DialogBoxListener) null);
            this.selectedImage = bitmap;
            this.cloudImageName = cloudImageName;
            this.cloudImagePath = cloudImagePath;
            this.isCloudImage = true;
        } else {
            new DialogBox(ActivityCashEntry.this, "Failure", (DialogBoxListener) null);
        }


    }

    private double getTotalTax() {
        return getTax(GST) + getTax(PST) + getTax(HST) + getTax(QST);
    }

    private double getTax(int identifier) {
        return (taxes.containsKey(identifier) ? taxes.get(identifier) : 0);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            switch (requestCode) {
                case Utility.MY_PERMISSION_ACCESS_CAMERA:
                case Utility.MY_PERMISSION_ACCESS_STORAGE:
                    capturePicView.onRequestPermissionsResult(requestCode, permissions, grantResults, ActivityCashEntry.this);
                    break;
            }
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
    }

    class BillWatcher implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {
                total = Utility.parseDouble(getText(etBillTotal));
                performCalculationOnTotal();
            }

        }
    }

    //    TAX WATCHER CODE BELOW
//    IT CHECKS IF TAX IS LESS TOTAL
    class TaxWatcher implements TextWatcher {

        EditText taxChild;
        String pastValue;
        int taxIdentifier = -1;

        public TaxWatcher(EditText taxChild, int taxIdentifier) {
            this.taxChild = taxChild;
            this.taxIdentifier = taxIdentifier;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String s_ = s.toString();
            if (s_.equalsIgnoreCase(" ")) setText("0");
            if (s_.equalsIgnoreCase(".")) {
                setText("0.");
                taxChild.setSelection(2);
            }
            s = Utility.parseDouble(s + "") + "";
            taxes.put(taxIdentifier, Utility.parseDouble(s + ""));

            if (billTotalExceeds()) {
                new DialogBox(ActivityCashEntry.this, getString(R.string.error_tax_must_be_less_or_equal_total), (DialogBoxListener) null);
                taxChild.removeTextChangedListener(this);
                taxes.put(taxIdentifier, Utility.parseDouble(pastValue + ""));
                taxChild.setText(pastValue);
                taxChild.addTextChangedListener(this);
            } else {
                etBillTotal.setText(((BigDecimal.valueOf(total).subtract(BigDecimal.valueOf(getTotalTax()))).doubleValue() + ""));
                this.pastValue = s + "";
            }

        }

        private void setText(String text) {
            taxChild.removeTextChangedListener(this);
            taxChild.setText(text);
            taxChild.addTextChangedListener(this);
        }


        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
