package com.kippin.ocr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kippin.activities.ActivityPreview;
import com.kippin.app.Kippin;
import com.kippin.bankstatement.ActivityBankStatement;
import com.kippin.classification.ClassificationCallback;
import com.kippin.classification.DialogClassification;
import com.kippin.cloudimages.ActivityCloudImages;
import com.kippin.cloudimages.CloudClickListener;
import com.kippin.kippin.R;
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
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;



public class ActivityOCR1 extends SuperActivity implements OnResultListener, CloudClickListener {

//    public static final String DATE = "DATE";
//    public static final String STATUS = "STATUS";
//    public static final String DESCRIPTION = "DESCRIPTION";
//    public static String AMOUNT="AMOUNT";
//    public static String STATEMENT_ID="STATEMENT_ID";
//    public static String ITEM_POSITION="ITEM_POSITION";
//    private int item_position = -1;

   // @Bind(R.id.tvDate)
    TextView tvDate;


  //  @Bind(R.id.tvStatus)
    TextView tvStatus;

   // @Bind(R.id.tvDescription)
    TextView tvDescription;

 //   @Bind(R.id.tvAmount)
    TextView tvAmount;

  //  @Bind(R.id.tvCredit)
    TextView tvCredit;

//    @Bind(R.id.tvDebit)
    TextView tvDebit;

  //  @Bind(R.id.etVendor)
    EditText etVendor;


 //  @Bind(R.id.etPurpose)
    EditText etPurpose;

  //  @Bind(R.id.etClassification)
    TextView etClassification;

 //   @Bind(R.id.etCategory)

    TextView tvCategory;


  //  @Bind(R.id.textView)
    TextView tvTopTitle;


 //   @Bind(R.id.etClassificationDetail)
    TextView tvClassificationDetail;

 //   @Bind(R.id.etTotal)
    EditText etTotal;

  ///  @Bind(R.id.etComment)
    EditText etComment;

  //  @Bind(R.id.ivReconcile)
    ImageView ivReconcile;

   // @Bind(R.id.ivViewUploadedBills)
    ImageView ivViewUploadedBills;

    //@Bind(R.id.ivAddUploadedBills)
    ImageView ivAddUploadedBills;

  //  @Bind(R.id.etGST)
    EditText etGst;

    //@Bind(R.id.etHst)
    EditText etHst;

   // @Bind(R.id.etPST)
    EditText etPst;

    //@Bind(R.id.etQST)
    EditText etQst;

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
        Utility.removeFocus(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_ocr);

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


    private void reconcile() {
        if (validated()) {
            WSTemplate wsTemplate = new WSTemplate();
            wsTemplate.aClass = ModelImagePost.class;
            wsTemplate.url = Url.URL_OCR_EXPENSE;
            wsTemplate.methods = WSMethods.POST;
            wsTemplate.context = ActivityOCR1.this;


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

            wsTemplate.templatePosts = templatePosts;
            wsTemplate.wsInterface = new WSInterface() {
                @Override
                public void onResult(int requestCode, TemplateData data) {
                    String s = data.getData(ModelImagePost.class).getResponseMessage();

                    if (s.equalsIgnoreCase("Success")) {
                        Singleton.reloadStatements = true;
                        s = Kippin.kippin.getString(R.string.success_ocr);
                    }

                    new DialogBox(ActivityOCR1.this, s, new DialogBoxListener() {
                        @Override
                        public void onDialogOkPressed() {
                            ActivityOCR1.this.finish();
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
        double taxes = Utility.parseDouble(getText(etGst))
                + Utility.parseDouble(getText(etQst))
                + Utility.parseDouble(getText(etHst))
                + Utility.parseDouble(getText(etPst));

        if (billTotal < taxes) {
            isError = true;
            msg = "Total Tax must be less than Total";
        }

        if(isError){
            new DialogBox(ActivityOCR1.this , msg ,(DialogBoxListener)null) ;
        }

        return !isError;
    }


    String selectedCatId = "-1";

    public void performCategoryOperation(final String cid) {
        WSUtils.hitServiceGet(ActivityOCR1.this, Url.URL_CATEGORY_BY_ID + File.separator + cid, new ArrayListPost(), ModelCategoryList.class, new WSInterface() {
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
            entryCash = true;
            etClassification.setText(modelBankStatement.getClassificationType());
            tvClassificationDetail.setText(modelBankStatement.getClassificationDescription());
            tvCategory.setText((modelBankStatement.getCategory()));
            tvTopTitle.setText(("Cash Details").toUpperCase());
        }

        setText(tvDate, Utility.getTimeParsed(modelBankStatement.getDate()));
        setText(tvStatus, modelBankStatement.getStatus());

        credit = modelBankStatement.getCredit();
        debit = modelBankStatement.getDebit();

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

        setText(etGst, getTaxDefault(taxes.get(GST)) + "");
        setText(etPst, getTaxDefault(taxes.get(PST)) + "");
        setText(etHst, getTaxDefault(taxes.get(HST)) + "");
        setText(etQst, getTaxDefault(taxes.get(QST)) + "");



        if(credit==0 && debit==0){
            total = modelBankStatement.getBillTotal() + getTotalTax();



            setText(etTotal, modelBankStatement.getBillTotal() + "");
            etClassification.setFocusable(false);
            etClassification.setFocusableInTouchMode(false);

        }
        else {
            if(credit!=0 && debit!=0){
                total = credit-debit;
                if(total<0)
                    total = -total;

                tax = getTotalTax();

                modelBankStatement.setBillTotal(total);

                setText(etTotal, (modelBankStatement.getBillTotal()- tax) + "");

            }else {
                total = credit-debit;

                if(total<0)
                total = -total ;

                tax  = getTotalTax();

                modelBankStatement.setBillTotal(total);

                setText(etTotal, (modelBankStatement.getBillTotal() - tax) + "");
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
        setText(tvAmount, modelBankStatement.getTotal() + "");

        setText(tvCredit, credit + "");
        setText(tvDebit, debit + "");
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

    private String getTaxDefault(Double aDouble) {

        if (aDouble == 0)
            return "";
        else return aDouble + "";
    }


    private void setText(EditText et, String s) {
        et.setText(s);
    }

    private void setText(TextView et, String s) {
        et.setText(s);
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        capturePicView = new CapturePicView(this);
        capturePicView.addOnCloudClickListener(onCloudListener);


        gstWatcher = new TaxWatcher(etGst, GST);
        hstWatcher = new TaxWatcher(etHst, HST);
        pstWatcher = new TaxWatcher(etPst, PST);
        qstWatcher = new TaxWatcher(etQst, QST);

        tvDate =(TextView)findViewById(R.id.tvDate);
        tvStatus =(TextView)findViewById(R.id.tvStatus);
        tvDescription =(TextView)findViewById(R.id.tvDescription);
        tvAmount =(TextView)findViewById(R.id.tvAmount);
        tvCredit =(TextView)findViewById(R.id.tvCredit);
        tvDebit =(TextView)findViewById(R.id.tvDebit);
        etVendor =(EditText)findViewById(R.id.etVendor);
        etPurpose =(EditText)findViewById(R.id.etPurpose);

        etClassification =(TextView)findViewById(R.id.etClassification);
        tvCategory =(TextView)findViewById(R.id.etCategory);
        tvTopTitle =(TextView)findViewById(R.id.textView);
        tvClassificationDetail =(TextView)findViewById(R.id.etClassificationDetail);


        etTotal =(EditText)findViewById(R.id.etTotal);
        etComment =(EditText)findViewById(R.id.etComment);
        ivReconcile =(ImageView)findViewById(R.id.ivReconcile);
        ivViewUploadedBills =(ImageView)findViewById(R.id.ivViewUploadedBills);

        ivViewUploadedBills =(ImageView)findViewById(R.id.ivViewUploadedBills);
        etGst =(EditText)findViewById(R.id.etGST);
        etPst =(EditText)findViewById(R.id.etPST);
        etHst =(EditText)findViewById(R.id.etHst);
        ivAddUploadedBills =(ImageView)findViewById(R.id.ivAddUploadedBills);
        etQst =(EditText)findViewById(R.id.etQST);


    }

    private OnCloudClickListener onCloudListener = new OnCloudClickListener() {
        @Override

        public void onClick() {
            Utility.startActivity(ActivityOCR1.this, ActivitySelectMonthYear.class, true);
//            Intent intent = new Intent(context, ActivitySelectMonthYear.class);
//            context.startActivityForResult(intent,REQUESTCODE_CLOUD);

            Utility.cloudClickListener = ActivityOCR1.this;

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
                Bundle bundle = new Bundle();
                bundle.putString(ActivityCloudImages.CLOUD_IMG, Url.getCloudImage(modelBankStatement.getId()));
                bundle.putBoolean(ActivityCloudImages.CLICK_NEEDED, false);
                Utility.startActivity(ActivityOCR1.this, ActivityCloudImages.class, bundle, true);
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
    public void openOCR(TemplateData data, Bitmap bm,boolean isSuccess) {
        ModelImagePost modelImagePost = data.getData(ModelImagePost.class);

        new DialogBox(ActivityOCR1.this, modelImagePost.getResponseMessage(), new DialogBoxListener() {
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
                            templateStatementImageUploader.onResultListener = ActivityOCR1.this;

                            capturePicView.uploadImageForStatement(ActivityOCR1.this, bm, modelBankStatement, templateStatementImageUploader);

                        }


                    });
                    break;
                case CapturePicView.REQUEST_CODE_GALLERY:

                    capturePicView.onActivityResult(requestCode, resultCode, data, new CameraPicListener() {
                        @Override
                        public void onImageSelected(final Bitmap bm) {
//                            capturePicView.uploadImageForStatement(bm);
                            ActivityPreview.bitmap=bm;
                            ActivityPreview.modelBankStatement=modelBankStatement;
                            startActivity(new Intent(ActivityOCR1.this,ActivityPreview.class));
                            /*TemplateStatementImageUploader templateStatementImageUploader = new TemplateStatementImageUploader();
                            templateStatementImageUploader.onResultListener = ActivityOCR1.this;

                            capturePicView.uploadImageForStatement(ActivityOCR1.this, bm, modelBankStatement, templateStatementImageUploader);*/

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
        templateStatementImageUploader.onResultListener = ActivityOCR1.this;
        templateStatementImageUploader.isCloudImage = true;
        templateStatementImageUploader.CloudImageName = imageName;
        templateStatementImageUploader.CloudImagePath = imagePath;
        templateStatementImageUploader.year = year;
        templateStatementImageUploader.month = month;

        capturePicView.uploadImageForStatement(ActivityOCR1.this, bitmap, modelBankStatement, templateStatementImageUploader);
    }

    private final int GST = 1;
    private final int PST = 2;
    private final int HST = 3;
    private final int QST = 4;


    HashMap<Integer, Double> taxes = new HashMap<Integer, Double>();

    private double getTotalTax() {
        return getTax(GST) + getTax(PST) + getTax(HST) + getTax(QST);
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
            total = Utility.parseDouble(getText(etTotal)) + getTotalTax();
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

            String s_ = s+"";
            if(s_.equalsIgnoreCase(" "))setText("0");



            if(s_.equalsIgnoreCase(".")){
                setText("0.");
                taxChild.setSelection(2);
            }
            else if( s_.length()>0){

                s_ = Utility.shortDouble(s_);

                setText(s_);

                taxChild.setSelection(s_.length());
            }


            if (s_.length() == 0) s_ = "0";
            s_=Utility.parseDouble(s_+"")+"";

            taxes.put(taxIdentifier, Utility.parseDouble(s_.toString()));
            if (billTotalExceeds()) {
                new DialogBox(ActivityOCR1.this, getString(R.string.error_tax_must_be_less_or_equal_total), (DialogBoxListener) null);
                taxChild.removeTextChangedListener(this);
                taxes.put(taxIdentifier, Utility.parseDouble(pastValue + ""));
                taxChild.setText(pastValue);
                taxChild.addTextChangedListener(this);
            } else {
                etTotal.setText(((BigDecimal.valueOf(total).subtract(BigDecimal.valueOf(getTotalTax()))).doubleValue() + ""));
                this.pastValue = s_ + "";
            }
        }


        private  void  setText(String text){
            taxChild.removeTextChangedListener(this);
                taxChild.setText(text);
            taxChild.addTextChangedListener(this);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public  void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            switch (requestCode) {
                case Utility.MY_PERMISSION_ACCESS_CAMERA:
                case Utility.MY_PERMISSION_ACCESS_STORAGE:
                    capturePicView.onRequestPermissionsResult(requestCode,permissions,grantResults,ActivityOCR1.this);
                    break;
            }
        }catch(Exception ex){
            Log.e("", ex.getMessage());
        }
    }
}
