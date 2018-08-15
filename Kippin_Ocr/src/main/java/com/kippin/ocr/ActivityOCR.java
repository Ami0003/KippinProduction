package com.kippin.ocr;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customdatepicker.Month_YearPicker;
import com.kippin.activities.ActivityPreview;
import com.kippin.app.Kippin;
import com.kippin.classification.ClassificationCallback;
import com.kippin.classification.DialogClassification;
import com.kippin.cloudimages.ActivityCloudImages;
import com.kippin.cloudimages.CloudClickListener;
import com.kippin.kippin.R;
import com.kippin.selectdate.ActivitySelectDate;
import com.kippin.selectdate.ModelBankStatement;
import com.kippin.selectmonthyear.ActivitySelectMonthYear;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.ImagePickUtility.CameraPicListener;
import com.kippin.utils.ImagePickUtility.CapturePicView;
import com.kippin.utils.ImagePickUtility.OnCloudClickListener;
import com.kippin.utils.ImagePickUtility.OnResultListener;
import com.kippin.utils.ImagePickUtility.TemplateStatementImageUploader;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModelCategoryList;
import com.kippin.webclient.model.ModelClassification;
import com.kippin.webclient.model.ModelImagePost;
import com.kippin.webclient.model.TemplateData;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;


public class ActivityOCR extends SuperActivity implements OnResultListener, CloudClickListener {
    public static final String SUCCESS_MESSAGE = "SUCCESS_MESSAGE";

//    public static final String DATE = "DATE";
//    public static final String STATUS = "STATUS";
//    public static final String DESCRIPTION = "DESCRIPTION";
//    public static String AMOUNT="AMOUNT";
//    public static String STATEMENT_ID="STATEMENT_ID";
//    public static String ITEM_POSITION="ITEM_POSITION";
//    private int item_position = -1;

    // @Bind(R.id.tvDate)
    TextView tvDate;
    public static String MONTHSTRING = "";
    public static String YEARSTRING = "";

    Month_YearPicker month_yearPicker;
    String monthString = "", yearString = "";
    String cameragallery = "";
    //  @Bind(R.id.tvStatus)
    TextView tvStatus;

    //    @Bind(R.id.tvDescription)
    TextView tvDescription;

    //  @Bind(R.id.tvAmount)
    TextView tvAmount;

    //@Bind(R.id.tvCredit)
    TextView tvCredit;

    // @Bind(R.id.tvDebit)
    TextView tvDebit;

    //@Bind(R.id.etVendor)
    EditText etVendor;


    //  @Bind(R.id.etPurpose)
    EditText etPurpose;

    //  @Bind(R.id.etClassification)
    TextView etClassification;

    //  @Bind(R.id.etCategory)

    TextView tvCategory;


    // @Bind(R.id.textView)
    TextView tvTopTitle;


    //@Bind(R.id.etClassificationDetail)
    TextView tvClassificationDetail;

    //  @Bind(R.id.etTotal)
    EditText etTotal;

    // @Bind(R.id.etComment)
    EditText etComment;

    // @Bind(R.id.ivReconcile)
    ImageView ivReconcile;

    // @Bind(R.id.ivViewUploadedBills)
    ImageView ivViewUploadedBills;

    //@Bind(R.id.ivAddUploadedBills)
    ImageView ivAddUploadedBills;

    //  @Bind(R.id.etGST)
    EditText etGst;

    // @Bind(R.id.etHst)
    EditText etHst;

    // @Bind(R.id.etPST)
    EditText etPst;

    //   @Bind(R.id.etQST)
    EditText etQst;


    // @Bind(R.id.layTaxes)
    LinearLayout layTaxes;
    CheckBox checkbox;

    TextWatcher gstWatcher, hstWatcher, pstWatcher, qstWatcher = null;

    DialogClassification dialogClassification = null;

    String selectedClassificationId = "";
    private int mYear;
    private int mMonth;
    private int mDay;
    public static final int DATE_DIALOG_ID = 0;


    public static final String MODEL_BANK_STATEMENT = "MODEL_BANK_STATEMENT";
    private ModelBankStatement modelBankStatement;

    public static CapturePicView capturePicView = null;

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Resume MONTH:: ", "" + CapturePicView.monthString);
        Log.e("Resume YEar: ", "" + CapturePicView.yearString);
        Utility.removeFocus(this);
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        capturePicView = new CapturePicView(this);
        capturePicView.addOnCloudClickListener(onCloudListener);


        month_yearPicker = new Month_YearPicker(this);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        tvCredit = (TextView) findViewById(R.id.tvCredit);
        tvDebit = (TextView) findViewById(R.id.tvDebit);
        etVendor = (EditText) findViewById(R.id.etVendor);
        etPurpose = (EditText) findViewById(R.id.etPurpose);

        etClassification = (TextView) findViewById(R.id.etClassification);
        tvCategory = (TextView) findViewById(R.id.etCategory);
        tvTopTitle = (TextView) findViewById(R.id.textView);
        tvClassificationDetail = (TextView) findViewById(R.id.etClassificationDetail);


        etTotal = (EditText) findViewById(R.id.etTotal);
        etComment = (EditText) findViewById(R.id.etComment);
        ivReconcile = (ImageView) findViewById(R.id.ivReconcile);
        ivViewUploadedBills = (ImageView) findViewById(R.id.ivViewUploadedBills);

        ivViewUploadedBills = (ImageView) findViewById(R.id.ivViewUploadedBills);
        etGst = (EditText) findViewById(R.id.etGST);
        etPst = (EditText) findViewById(R.id.etPST);
        etHst = (EditText) findViewById(R.id.etHst);
        ivAddUploadedBills = (ImageView) findViewById(R.id.ivAddUploadedBills);
        etQst = (EditText) findViewById(R.id.etQST);
        layTaxes = (LinearLayout) findViewById(R.id.layTaxes);

        gstWatcher = new TaxWatcher(etGst, GST);
        hstWatcher = new TaxWatcher(etHst, HST);
        pstWatcher = new TaxWatcher(etPst, PST);
        qstWatcher = new TaxWatcher(etQst, QST);
        Log.e("ActivityOCR:", "" + modelBankStatement.isIsCanadian());
        if (modelBankStatement.isIsCanadian()) {
            layTaxes.setVisibility(View.GONE);
        } else {
            layTaxes.setVisibility(View.VISIBLE);
        }

        month_yearPicker.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("11", "010");
                // tvSelectMonth.setText(month_yearPicker.getSelectedMonthName() + "-" + month_yearPicker.getSelectedYear());
                Log.e("Reuslt:", "" + month_yearPicker.getSelectedMonthName() + " >> " + month_yearPicker.getSelectedYear());
                monthString = monthName("" + month_yearPicker.getSelectedMonthName());
                yearString = "" + month_yearPicker.getSelectedYear();
                if (cameragallery.equals("camera")) {
                    try {
                        capturePicView.launchCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (cameragallery.equals("gallery")) {
                    capturePicView.launchGallery();
                } else {

                }

            }
        }, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("00", "00");
                monthString = "";
                yearString = "";
                if (cameragallery.equals("camera")) {
                    try {
                        capturePicView.launchCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (cameragallery.equals("gallery")) {
                    capturePicView.launchGallery();
                } else {

                }


            }
        });

    }

    public String monthName(String monthname) {
        if (monthname.equals("January")) {
            monthname = "01";
            return monthname;
        } else if (monthname.equals("February")) {
            monthname = "02";
            return monthname;
        } else if (monthname.equals("March")) {
            monthname = "03";
            return monthname;
        } else if (monthname.equals("April")) {
            monthname = "04";
            return monthname;
        } else if (monthname.equals("May")) {
            monthname = "05";
            return monthname;
        } else if (monthname.equals("June")) {
            monthname = "06";
            return monthname;
        } else if (monthname.equals("July")) {
            monthname = "07";
            return monthname;
        } else if (monthname.equals("August")) {
            monthname = "08";
            return monthname;
        } else if (monthname.equals("September")) {
            monthname = "09";
            return monthname;
        } else if (monthname.equals("October")) {
            monthname = "10";
            return monthname;
        } else if (monthname.equals("November")) {
            monthname = "11";
            return monthname;
        } else if (monthname.equals("December")) {
            monthname = "12";
            return monthname;
        }
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_ocr);
        Log.e("edit_transaction", "edit_transaction");

        if (getIntent().getBooleanExtra(SUCCESS_MESSAGE, false)) {
            new DialogBox(this, "Success", new DialogBoxListener() {
                @Override
                public void onDialogOkPressed() {
                    taskOnCreate();
                }
            });
        } else {
            taskOnCreate();
        }


    }

    TextWatcher commentWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            etComment.setHint(R.string.ocr_comment);
//            if (etComment.hasFocus()
//                    && etComment.getText().toString().length() == 0
//
//                    ) {
//                etComment.setHint(R.string.ocr_comment);
//            } else if (!etComment.hasFocus()) {
//                etComment.setHint("");
//            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private void taskOnCreate() {

        Singleton.reloadStatements = true;

        Bundle bundle = getIntent().getExtras();
//        item_position=bundle.getInt(ITEM_POSITION);
        modelBankStatement = (ModelBankStatement) bundle.getSerializable(MODEL_BANK_STATEMENT);
        generateActionBar(R.string.edit_transaction, true, true);
        initialiseUI();
        updateUI();
        setUpListeners();
        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        dialogClassification = new DialogClassification(this, modelBankStatement.getId());
        dialogClassification.requestClassifications(null);

        etComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    etComment.setHint(R.string.ocr_comment);
                    etComment.addTextChangedListener(commentWatcher);
                } else {
                    etComment.setHint("");

                    etComment.removeTextChangedListener(commentWatcher);
                }

            }
        });


        if (getIntent().getStringExtra(ActivitySelectDate.BANK_ID).equalsIgnoreCase("6")) {
            layTaxes.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Resume MONTH:: ", "" + CapturePicView.monthString);
        Log.e("Resume YEar: ", "" + CapturePicView.yearString);


    }

    private void reconcile() {
        if (validated()) {
            WSTemplate wsTemplate = new WSTemplate();
            wsTemplate.aClass = ModelImagePost.class;
            wsTemplate.url = Url.URL_OCR_EXPENSE;
            wsTemplate.methods = WSMethods.POST;
            wsTemplate.context = ActivityOCR.this;


            ArrayListPost templatePosts = new ArrayListPost();
            templatePosts.add("Id", modelBankStatement.getId());


            if (entryCash) {
                templatePosts.add("ClassificationId", modelBankStatement.getClassificationId());
                templatePosts.add("CategoryId", modelBankStatement.getCategoryId());
            } else {
                templatePosts.add("ClassificationId", selectedClassificationId);
                templatePosts.add("CategoryId", selectedCatId);
            }


            templatePosts.add("Description", modelBankStatement.getDescription());
            String check = "" + checkbox.isChecked();
            Log.e("check:", "" + check);
            templatePosts.add("IsAutoClassified", check);
            templatePosts.add("Comments", gettext(etComment));
            templatePosts.add("Purpose", gettext(etPurpose));
            templatePosts.add("Vendor", gettext(etVendor));


//            Pass status Id instead of status
//            1=Pending,2=Submit,3=Reject,4=Locked

            templatePosts.add("StatusId", "2");

            templatePosts.add("Total", modelBankStatement.getTotal() + "");

            templatePosts.add("BillTotal", gettext(etTotal));

            templatePosts.add("GSTtax", Utility.parseDouble(getText(etGst)) + "");
            templatePosts.add("HSTtax", Utility.parseDouble(getText(etHst)) + "");
            templatePosts.add("QSTtax", Utility.parseDouble(getText(etQst)) + "");
            templatePosts.add("PSTtax", Utility.parseDouble(getText(etPst)) + "");

            Log.e("templatePosts:", "" + templatePosts.getJson());

            wsTemplate.templatePosts = templatePosts;
            wsTemplate.wsInterface = new WSInterface() {
                @Override
                public void onResult(int requestCode, TemplateData data) {
                    String s = data.getData(ModelImagePost.class).getResponseMessage();

                    if (s.equalsIgnoreCase("Success")) {
                        Singleton.reloadStatements = true;
                        s = Kippin.kippin.getString(R.string.success_ocr);
                    }

                    new DialogBox(ActivityOCR.this, s, new DialogBoxListener() {
                        @Override
                        public void onDialogOkPressed() {
                            ActivityOCR.this.finish();
                        }
                    });

                }
            };
            new WSHandler(wsTemplate).execute();

        }


    }

    private boolean validated() {


//
//
//        try {
//            new DialogBox(ActivityOCR.this, "", (DialogBoxListener)null);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(ActivityOCR.this, "Please fill mandatory field.", Toast.LENGTH_SHORT);
//        }
//    }


        String msg = "";

        boolean isError = false;


        if (
                TextUtils.isEmpty(etClassification.getText().toString())
                        ||
                        etClassification.getText().toString().equalsIgnoreCase("Please Select")
                        ||
                        TextUtils.isEmpty(tvCategory.getText().toString())
                        ||
                        tvCategory.getText().toString().equalsIgnoreCase("Please Select")
                        ||
                        TextUtils.isEmpty(etTotal.getText().toString())
                        ||
                        TextUtils.isEmpty(selectedClassificationId)
                ) {
            isError = true;
            msg = "Please fill mandatory field.";
        }

        double billTotal = Utility.parseDouble(getText(etTotal));
        double taxes = Utility.add(Utility.parseDouble(getText(etGst))
                , Utility.parseDouble(getText(etQst))
                , Utility.parseDouble(getText(etHst))
                , Utility.parseDouble(getText(etPst)));


        if (billTotal < taxes) {
            isError = true;
            msg = "Total Tax must be less than Total";
        }

        if (isError) {
            new DialogBox(ActivityOCR.this, msg, (DialogBoxListener) null);
        }

        return !isError;
    }


    String selectedCatId = "-1";

    public void performCategoryOperation(final String cid) {
        WSUtils.hitServiceGet(ActivityOCR.this, Url.URL_CATEGORY_BY_ID + File.separator + cid, new ArrayListPost(), ModelCategoryList.class, new WSInterface() {
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


    double credit, debit,/* total_ ,*/
            tax;
    ;

    public String gettext(TextView textView) {
        return textView.getText().toString();
    }

    public String gettext(EditText textView) {
        return textView.getText().toString();
    }

    boolean isCashClassification = false;

    boolean entryCash = false;

    private void updateUI() {

        if (modelBankStatement.getClassificationId().equalsIgnoreCase(getString(R.string.classification_cash_id))) {
//            entryCash = true;
            etClassification.setText(modelBankStatement.getClassificationType());
            tvClassificationDetail.setText(modelBankStatement.getClassificationDescription());
            tvCategory.setText((modelBankStatement.getCategory()));
//            tvTopTitle.setText(("Cash Details").toUpperCase());
        }

        setText(tvDate, Utility.getTimeParsed(modelBankStatement.getDate()));

        Log.e("DATE: ", "" + modelBankStatement.getDate());
        Log.e("Change DATE: ", "" + Utility.getTimeParsed(modelBankStatement.getDate()));
        String input = Utility.getTimeParsed(modelBankStatement.getDate());
        String[] out = input.split("-");
        // System.out.println("Month = " + out[0]);
        Log.e("DATE: ", "" + out[0]);
        Log.e("DATE: ", "" + out[1]);
        CapturePicView.monthString = out[1];
        CapturePicView.yearString = out[0];
        MONTHSTRING = out[1];
        YEARSTRING = out[0];
       /* Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);*/
        setText(tvStatus, modelBankStatement.getStatus());

        credit = modelBankStatement.getCredit();
        debit = modelBankStatement.getDebit();
        checkbox.setChecked(modelBankStatement.getIsAutoClassified());

//        if (modelBankStatement.getDebit() > 0 || modelBankStatement.getCredit() > 0) {
//            double i = BigDecimal.valueOf(debit).add(BigDecimal.valueOf(credit)).doubleValue();
//            if (i < 0) {
//                i = -i;
//            }
//            modelBankStatement.setBillTotal(i);
//        }


        taxes.put(GST, modelBankStatement.getGSTtax());
        taxes.put(PST, modelBankStatement.getPSTtax());
        taxes.put(HST, modelBankStatement.getHSTtax());
        taxes.put(QST, modelBankStatement.getQSTtax());

        setText(etGst, Utility.parseTaxWithZero(taxes.get(GST)));
        setText(etPst, Utility.parseTaxWithZero(taxes.get(PST)));
        setText(etHst, Utility.parseTaxWithZero(taxes.get(HST)));
        setText(etQst, Utility.parseTaxWithZero(taxes.get(QST)));


        if (credit == 0 && debit == 0) {
            total = Utility.add(modelBankStatement.getBillTotal(), getTotalTax());


            setText(etTotal, Utility.parseDouble(modelBankStatement.getBillTotal()));
            etClassification.setFocusable(false);
            etClassification.setFocusableInTouchMode(false);

        } else {
            if (credit != 0 && debit != 0) {


                total = Utility.sub(credit, debit);//credit-debit;
                if (total < 0)
                    total = -total;

                tax = getTotalTax();

                modelBankStatement.setBillTotal(total);

                setText(etTotal, Utility.parseDouble((Utility.sub(modelBankStatement.getBillTotal(), tax))));

            } else {

                if (modelBankStatement.getBillTotal() == 0) {
                    total = Utility.sub(credit, debit);
                    if (total < 0)
                        total = -total;

                    tax = getTotalTax();

                    modelBankStatement.setBillTotal(total);

                    setText(etTotal, Utility.parseDouble((Utility.sub(modelBankStatement.getBillTotal(), tax))));
                } else {
                    total = modelBankStatement.getBillTotal();

                    tax = getTotalTax();

                    total = total + tax;

                    setText(etTotal, modelBankStatement.getBillTotal() + "");
                }

            }


        }
//        total = modelBankStatement.getBillTotal();


//        if (isCashClassification) {
//            setText(etTotal, modelBankStatement.getBillTotal() + "");
//            etClassification.setFocusable(false);
//            etClassification.setFocusableInTouchMode(false);
//        } else
//            setText(etTotal, (total - tax) + "");


        setText(tvDescription, modelBankStatement.getDescription());
        setText(tvAmount, Utility.parseDouble(modelBankStatement.getTotal()));

        setText(tvCredit, Utility.parseDouble(credit));
        setText(tvDebit, Utility.parseDouble(debit));
        setText(etVendor, modelBankStatement.getVendor());
        setText(etPurpose, modelBankStatement.getPurpose());
        setText(tvCategory, modelBankStatement.getCategory());
        selectedCatId = modelBankStatement.getCategoryId();


        setText(etComment, modelBankStatement.getComments());

        Singleton.bm = null;

        setText(etClassification, modelBankStatement.getClassificationType());
        setText(tvClassificationDetail, modelBankStatement.getClassificationDescription());
        selectedClassificationId = modelBankStatement.getClassificationId();

        isCashClassification = modelBankStatement.getClassificationType().equalsIgnoreCase("cash");


    }


    private void setText(EditText et, String s) {
        et.setText(s);
    }

    private void setText(TextView et, String s) {
        et.setText(s);
    }


    private OnCloudClickListener onCloudListener = new OnCloudClickListener() {
        @Override

        public void onClick() {
            Utility.startActivity(ActivityOCR.this, ActivitySelectMonthYear.class, true);
//            Intent intent = new Intent(context, ActivitySelectMonthYear.class);
//            context.startActivityForResult(intent,REQUESTCODE_CLOUD);

            Utility.cloudClickListener = ActivityOCR.this;

        }
    };


    @Override
    public void setUpListeners() {
        ivReconcile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reconcile();
            }
        });

        if (!entryCash)
            etClassification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogClassification.show(classificationCallback);
                }
            });

        ivViewUploadedBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(ActivityOCR.this,WebPdfViewActivity.class);
                startActivity(intent);*/
                Bundle bundle = new Bundle();
                bundle.putString(ActivityCloudImages.CLOUD_IMG, Url.getCloudImage(modelBankStatement.getId()));
                bundle.putBoolean(ActivityCloudImages.CLICK_NEEDED, false);
                Utility.startActivity(ActivityOCR.this, ActivityCloudImages.class, bundle, true);
            }
        });


        ivAddUploadedBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePicView.show(false);
            }
        });

        addTaxWachers();
    }


    ClassificationCallback classificationCallback = new ClassificationCallback() {
        @Override
        public void onClassificatonSelected(ModelClassification modelClassification, int position) {
            etClassification.setText(modelClassification.getClassificationType());
            selectedClassificationId = modelClassification.getId();
            tvClassificationDetail.setText(modelClassification.getDesc());
            performCategoryOperation(modelClassification.getCategoryId());
        }
    };


    @Override
    public void openOCR(TemplateData data, Bitmap bm, boolean i) {
        ModelImagePost modelImagePost = data.getData(ModelImagePost.class);

        new DialogBox(ActivityOCR.this, modelImagePost.getResponseMessage(), new DialogBoxListener() {
            @Override
            public void onDialogOkPressed() {
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        {
            switch (requestCode) {
                case 1:
                    break;
                case CapturePicView.REQUEST_CODE_CAMERA:
                    capturePicView.onActivityResult(requestCode, resultCode, data, new CameraPicListener() {
                        @Override
                        public void onImageSelected(final Bitmap bm) {
//                            capturePicView.uploadImageForStatement(bm);
                            TemplateStatementImageUploader templateStatementImageUploader = new TemplateStatementImageUploader();
                            templateStatementImageUploader.onResultListener = ActivityOCR.this;
                            templateStatementImageUploader.month = MONTHSTRING;
                            templateStatementImageUploader.year = YEARSTRING;
                            capturePicView.uploadImageForStatement(ActivityOCR.this, bm, modelBankStatement, templateStatementImageUploader);

                        }


                    });


                    break;
                case CapturePicView.REQUEST_CODE_GALLERY:
                    capturePicView.onActivityResult(requestCode, resultCode, data, new CameraPicListener() {
                        @Override
                        public void onImageSelected(final Bitmap bm) {
//                            capturePicView.uploadImageForStatement(bm);
                            ActivityPreview.bitmap = bm;
                            ActivityPreview.modelBankStatement = modelBankStatement;
                            startActivity(new Intent(ActivityOCR.this, ActivityPreview.class));
                            /*TemplateStatementImageUploader templateStatementImageUploader = new TemplateStatementImageUploader();
                            templateStatementImageUploader.onResultListener = ActivityOCR.this;
                            templateStatementImageUploader.month=CapturePicView.monthString;
                            templateStatementImageUploader.year=CapturePicView.yearString;
                            capturePicView.uploadImageForStatement(ActivityOCR.this, bm, modelBankStatement, templateStatementImageUploader);
*/
                        }


                    });


                    break;
                case CapturePicView.PIC_CROP:
                    capturePicView.onActivityResult(requestCode, resultCode, data, new CameraPicListener() {
                        @Override
                        public void onImageSelected(final Bitmap bm) {
//                            capturePicView.uploadImageForStatement(bm);
                            TemplateStatementImageUploader templateStatementImageUploader = new TemplateStatementImageUploader();
                            templateStatementImageUploader.onResultListener = ActivityOCR.this;
                            templateStatementImageUploader.month = CapturePicView.monthString;
                            templateStatementImageUploader.year = CapturePicView.yearString;
                            capturePicView.uploadImageForStatement(ActivityOCR.this, bm, modelBankStatement, templateStatementImageUploader);

                        }


                    });


                    break;
            }
        }
    }


    public void onCloudClick(Bitmap bitmap, String imageName, String imagePath, String month, String year) {
        Utility.activityBankStatement = null;
//        final Activity activity ,final Bitmap bm,final ModelBankStatement modelBankStatement,final OnResultListener onResultListener

        TemplateStatementImageUploader templateStatementImageUploader = new TemplateStatementImageUploader();
        templateStatementImageUploader.onResultListener = ActivityOCR.this;
        templateStatementImageUploader.isCloudImage = true;
        templateStatementImageUploader.CloudImagePath = imagePath;
        templateStatementImageUploader.CloudImageName = imageName;
        templateStatementImageUploader.year = year;
        templateStatementImageUploader.month = month;

        capturePicView.uploadImageForStatement(ActivityOCR.this, bitmap, modelBankStatement, templateStatementImageUploader);
    }

    private final int GST = 1;
    private final int PST = 2;
    private final int HST = 3;
    private final int QST = 4;


    HashMap<Integer, Double> taxes = new HashMap<Integer, Double>();

    private double getTotalTax() {
        return Utility.add(getTax(GST), getTax(PST), getTax(HST), getTax(QST));
    }

    private double getTax(int identifier) {
        return (taxes.containsKey(identifier) ? taxes.get(identifier) : 0);
    }


    public boolean billTotalExceeds() {
        double billTotal = total;
        if (getTotalTax() > billTotal) return true;
        return false;
    }

    private void performCalculationOnTotal() {
        if (billTotalExceeds()) {
//            clearTaxes();
            total = Utility.add(Utility.parseDouble(getText(etTotal)), getTotalTax());
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


    double total;

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

            String s_ = s + "";
            if (s_.equalsIgnoreCase(" ")) setText("0");


            if (s_.equalsIgnoreCase(".")) {
                setText("0.");
                taxChild.setSelection(2);
            } else if (s_.length() > 0) {

                s_ = Utility.shortDouble(s_);

                setText(s_);

                taxChild.setSelection(s_.length());
            }


            if (s_.length() == 0) s_ = "0";
            s_ = Utility.parseDouble(s_ + "") + "";

            taxes.put(taxIdentifier, Utility.parseDouble(s_.toString()));
            if (billTotalExceeds()) {
                new DialogBox(ActivityOCR.this, getString(R.string.error_tax_must_be_less_or_equal_total), (DialogBoxListener) null);
                taxChild.removeTextChangedListener(this);
                taxes.put(taxIdentifier, Utility.parseDouble(pastValue + ""));
                taxChild.setText(pastValue);
                taxChild.addTextChangedListener(this);
            } else {
                etTotal.setText(Utility.parseDouble(((BigDecimal.valueOf(total).subtract(BigDecimal.valueOf(getTotalTax()))).doubleValue())));
                this.pastValue = s_ + "";
            }
        }


        private void setText(String text) {
            if (taxChild != null) {
                taxChild.removeTextChangedListener(this);
                taxChild.setText(text);
                taxChild.addTextChangedListener(this);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            switch (requestCode) {
                case Utility.MY_PERMISSION_ACCESS_CAMERA:
                case Utility.MY_PERMISSION_ACCESS_STORAGE:
                    capturePicView.onRequestPermissionsResult(requestCode, permissions, grantResults, ActivityOCR.this);
                    break;
            }
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
    }
}
