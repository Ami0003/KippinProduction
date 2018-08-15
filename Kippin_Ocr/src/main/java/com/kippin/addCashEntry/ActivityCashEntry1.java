package com.kippin.addCashEntry;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.kippin.utils.ImagePickUtility.OnResultListener;
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
import com.kippin.webclient.model.TemplateData;
import com.kippin.webclient.model.category.OnCategoryLoaded;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ActivityCashEntry1 extends SuperActivity implements  CloudClickListener {


  //  @Bind(R.id.spCategory)
    Spinner spCategory;

    //@Bind(R.id.tvDate)
    TextView tvDate;

  //  @Bind(R.id.tvClassification)
    TextView tvClassification;

   // @Bind(R.id.etVendor)
    TextView etVendor;

   // @Bind(R.id.etDescription)
    TextView etDescription;

 //   @Bind(R.id.etPurpose)
    EditText etPurpose;

    //@Bind(R.id.etBillTotal)
    EditText etBillTotal;

   // @Bind(R.id.etGST)
    EditText etGst;

  //  @Bind(R.id.etHst)
    EditText etHst;

  //  @Bind(R.id.etPST)
    EditText etPst;

 //   @Bind(R.id.etQST)
    EditText etQst;

 //   @Bind(R.id.etComment)
    TextView etComment;

   // @Bind(R.id.btnUploadBill)
    ImageView btnUploadBill;

   // @Bind(R.id.tvSubmit)
    ImageView tvSubmit;

   // @Bind(R.id.tvMandatory)
    TextView tvMandatory;

    //@Bind(R.id.llFooter)
    LinearLayout llFooter;


//    @Bind(R.id.temp)
//    TextView temp;


    private ArrayList<ModelCategoryList> categories;

    private MyKeyboard myKeyboard;
    private int year, month, day;

    boolean isCloudImage = false;

    private String cloudImagePath;
    String cloudImageName = "";
    Bitmap selectedImage = null;

    static boolean isDataLoaded=false;

    CapturePicView capturePicView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDataLoaded = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_entry);
        Log.e("here","here");
        generateActionBar(R.string.add_cash_entry, true, true);

        initialiseUI();
        setUpListeners();
        setUpUI();


    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();

        capturePicView = new CapturePicView(this);
        capturePicView.addOnCloudClickListener(onCloudListener);
        spCategory = (Spinner)findViewById(R.id.spCategory);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvClassification = (TextView) findViewById(R.id.tvClassification);
        etVendor = (TextView) findViewById(R.id.etVendor);
        etDescription = (EditText) findViewById(R.id.etDescription);

        etPurpose = (EditText) findViewById(R.id.etPurpose);
        etBillTotal = (EditText) findViewById(R.id.etBillTotal);

        llFooter = (LinearLayout) findViewById(R.id.llFooter);
        tvSubmit = (ImageView) findViewById(R.id.tvSubmit);
        btnUploadBill = (ImageView) findViewById(R.id.btnUploadBill);
        etComment = (TextView) findViewById(R.id.etComment);
        etQst = (EditText) findViewById(R.id.etQST);

        etPst = (EditText) findViewById(R.id.etPST);
        etHst = (EditText) findViewById(R.id.etHst);
        etGst = (EditText) findViewById(R.id.etGST);
        tvMandatory = (TextView)findViewById(R.id.tvMandatory);


        myKeyboard = new MyKeyboard(this, findViewById(R.id.root), new KeyboardListener() {
            @Override
            public void isKeyboardShowing(boolean isShowing) {

                int visibility = View.GONE;

                if (isShowing) {
                    visibility = View.GONE;
                } else {
                    visibility = View.VISIBLE;
                }


                llFooter.setVisibility(visibility);
            }
        });
        myKeyboard.addGlobalCallbackListener();



        gstWatcher = new TaxWatcher(etGst,GST) ;
        hstWatcher = new TaxWatcher(etHst,HST) ;
        pstWatcher = new TaxWatcher(etPst,PST) ;
        qstWatcher = new TaxWatcher(etQst,QST) ;


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
                            new DialogBox(ActivityCashEntry1.this, "Invalid Image", (DialogBoxListener) null);
                            return;
                        }else{
                            ActivityCashEntry1.this.selectedImage = bm;
                            ActivityCashEntry1.this.cloudImageName = "";
                            ActivityCashEntry1.this.isCloudImage = false;
                        }
                    }
                });


                break;
        }
    }


    private OnCloudClickListener onCloudListener = new OnCloudClickListener() {
        @Override

        public void onClick() {
            Utility.startActivity(ActivityCashEntry1.this, ActivitySelectMonthYear.class, true);
            Utility.cloudClickListener = ActivityCashEntry1.this;
        }
    };


    public void setDate() {
        tvDate.setText(Utility.parseDateBySlashes(day, month + 1, year));
    }

    //@OnClick(R.id.tvDate)
    public void onDateClick(View c) {
        new android.app.DatePickerDialog(ActivityCashEntry1.this, new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                day = dayOfMonth;
                ActivityCashEntry1.this.month = monthOfYear;
                ActivityCashEntry1.this.year = year;
                setDate();
            }

        }, year, month, day).show();

    }

   // @OnClick(R.id.btnUploadBill)
    public void onUploadBillClick(View c) {
        capturePicView.show(false);
    }


    private void setUpUI() {

        Date dt = new Date();
        year = 1900 + dt.getYear();
        month = dt.getMonth();
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        setDate();

        tvClassification.setText(Kippin.res.getString(R.string.cash));

        ModelCategoryLists modelCategoryLists = ModelCategoryLists.getInstance();

        modelCategoryLists.loadData(this, new OnCategoryLoaded() {
            @Override
            public void onCategoryLoaded(ArrayList<ModelCategoryList> modelCategoryLists) {
                categories = modelCategoryLists;

                adapterCategory = new AdapterCategory(ActivityCashEntry1.this, modelCategoryLists);

                spCategory.setAdapter(adapterCategory);

                spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
        });

    }

    AdapterCategory adapterCategory = null;

    //@OnClick(R.id.tvSubmit)
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

            templatePosts.add("BillPath", s);
            templatePosts.add("BankId", Utility.bankId);
            templatePosts.add("BillTotal", Utility.parseDouble(getText(etBillTotal)) + "");
            templatePosts.add("CategoryId", Integer.parseInt(categories.get(spCategory.getSelectedItemPosition()).getId()) + "");
            templatePosts.add("Classification", Kippin.res.getString(R.string.cash));
            templatePosts.add("CloudImageName", cloudImageName);
            templatePosts.add("ClassificationId", "95");
            templatePosts.add("Comment", getText(etComment));
            templatePosts.add("UserId", Integer.parseInt(Singleton.getUser().getId()) + "");
            templatePosts.add("Description", getText(etDescription));
            templatePosts.add("CashBillDate", getText(tvDate));
            templatePosts.add("GSTtax", Utility.parseDouble(getText(etGst)) + "");
            templatePosts.add("HSTtax", Utility.parseDouble(getText(etHst)) + "");
            templatePosts.add("QSTtax", Utility.parseDouble(getText(etQst)) + "");
            templatePosts.add("PSTtax", Utility.parseDouble(getText(etPst)) + "");
            templatePosts.add("Purpose", getText(etPurpose));
            templatePosts.add("Vendor", getText(etVendor));
            templatePosts.add("ImageName", System.currentTimeMillis() + ".png");
            templatePosts.add("IsCloud", isCloudImage + "");
            templatePosts.add("AccountName", "Cash");
            templatePosts.add("AccountNumber", Utility.accNumber);



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


            WSUtils.hitService(ActivityCashEntry1.this, Url.URL_ADD_CASH_ENTRY, false, templatePosts, WSMethods.POST, TemplateData.class, new WSInterface() {
                @Override
                public void onResult(int requestCode, TemplateData data) {
                    new DialogBox(ActivityCashEntry1.this, data.getResponseMsg(), new DialogBoxListener() {
                        @Override
                        public void onDialogOkPressed() {
                            Singleton.reloadStatements = true;
                            ActivityCashEntry1.this.finish();
                        }
                    });

                }
            });

        }

    }


    private boolean isValidated() {
        boolean isValidated = true;

        String msg = "";

        if (
//                TextUtils.isEmpty(getText(etVendor))
//                ||
        TextUtils.isEmpty(getText(etDescription))
                || TextUtils.isEmpty(getText(etPurpose))
                || spCategory.getSelectedItemPosition()==0
                || Utility.parseDouble(getText(etBillTotal)) == 0
                ) {
            isValidated = false;
            msg = "Please fill mandatory fields";
        }

        double billTotal = Utility.parseDouble(getText(etBillTotal));
        double taxes = Utility.parseDouble(getText(etGst))
                + Utility.parseDouble(getText(etQst))
                +Utility.parseDouble(getText(etHst))
                +Utility.parseDouble(getText(etPst)) ;

        if(billTotal<taxes){
            isValidated = false;
            msg = "Total Tax must be less than Total";
        }

        if (!isValidated) {
            new DialogBox(ActivityCashEntry1.this, msg, (DialogBoxListener) null);
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
        addTaxWachers();
        etBillTotal.setOnFocusChangeListener(billWatcher);
    }


    BillWatcher billWatcher = new BillWatcher();


    double total;

    class BillWatcher implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if(!hasFocus){
                total = Utility.parseDouble(getText(etBillTotal)) ;
                performCalculationOnTotal();
            }

        }
    }

    public boolean billTotalExceeds(){
        double billTotal = total;
        if( getTotalTax() > billTotal) return true;
        return false;
    }

    private void performCalculationOnTotal() {
        if(billTotalExceeds()){
//            clearTaxes();
            total = Utility.parseDouble(getText(etBillTotal))+getTotalTax();
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

    private void removeTaxWachers(){
        etGst.removeTextChangedListener(gstWatcher);
        etHst.removeTextChangedListener(hstWatcher);
        etQst.removeTextChangedListener(qstWatcher);
        etPst.removeTextChangedListener(pstWatcher);
    }

    private void addTaxWachers(){
        etGst.addTextChangedListener(gstWatcher);
        etHst.addTextChangedListener(hstWatcher);
        etQst.addTextChangedListener(qstWatcher);
        etPst.addTextChangedListener(pstWatcher);
    }


    TextWatcher gstWatcher,hstWatcher , pstWatcher,qstWatcher=  null;

    @Override
    public void onCloudClick(Bitmap bitmap, String cloudImageName, String cloudImagePath ,  String month, String year) {
        this.selectedImage = bitmap;
        this.cloudImageName = cloudImageName;
        this.cloudImagePath = cloudImagePath;
        this.isCloudImage = true;
    }



    private final int GST = 1;
    private final int PST = 2;
    private final int HST = 3;
    private final int QST = 4;


    HashMap<Integer,Double> taxes = new HashMap<Integer, Double>() ;


//    TAX WATCHER CODE BELOW
//    IT CHECKS IF TAX IS LESS TOTAL
    class  TaxWatcher implements TextWatcher{

        EditText taxChild;
        String pastValue;
        int taxIdentifier = -1;

        public TaxWatcher(EditText taxChild , int taxIdentifier){
                    this.taxChild= taxChild;
            this.taxIdentifier = taxIdentifier;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String s_ = s.toString();


            if(s_.equalsIgnoreCase(" "))setText("0");


            if(s_.equalsIgnoreCase(".")){
                setText("0.");
                taxChild.setSelection(2);
            }

            s=Utility.parseDouble(s+"")+"";

            taxes .put(taxIdentifier , Utility.parseDouble(s+"")) ;

            if (billTotalExceeds()){
                new DialogBox(ActivityCashEntry1.this,getString(R.string.error_tax_must_be_less_or_equal_total) , (DialogBoxListener)null);
                taxChild.removeTextChangedListener(this);
                taxes.put(taxIdentifier, Utility.parseDouble(pastValue+"") );
                taxChild.setText(pastValue);
                taxChild.addTextChangedListener(this);
            }else{
                etBillTotal.setText(( (BigDecimal.valueOf(total) .subtract(BigDecimal.valueOf(getTotalTax()))).doubleValue()+""));
                this.pastValue = s+"";
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

    private double getTotalTax() {
        return getTax(GST)+getTax(PST)+getTax(HST)+getTax(QST);
    }

    private double getTax(int identifier){
        return (taxes.containsKey(identifier)?taxes.get(identifier):0);
    }
    public  void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            switch (requestCode) {
                case Utility.MY_PERMISSION_ACCESS_CAMERA:
                case Utility.MY_PERMISSION_ACCESS_STORAGE:
                    capturePicView.onRequestPermissionsResult(requestCode,permissions,grantResults,ActivityCashEntry1.this);
                    break;
            }
        }catch(Exception ex){
            Log.e("", ex.getMessage());
        }
    }
}
