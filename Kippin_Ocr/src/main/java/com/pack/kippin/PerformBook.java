package com.pack.kippin;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kippin.adapters.AdapterIncomeStatement_PerformBook;
import com.kippin.interfaces.IntefaceEntryItemListener;
import com.kippin.kippin.R;
import com.kippin.performbook.adapter.CurrencyAdapter;
import com.kippin.performbook.adapter.DaysAdapter;
import com.kippin.performbook.adapter.IndustryAdapter;
import com.kippin.performbook.adapter.MonthAdapter;
import com.kippin.performbook.adapter.OwnerShipAdapter;
import com.kippin.performbook.adapter.SpinnerAdapter;
import com.kippin.performbook.adapter.SubIndustryAdapter;
import com.kippin.performbook.adapter.YearAdapter;
import com.kippin.selectdate.ModelProvinceArrayData;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.utils.pinnedlistview.EntryItem;
import com.kippin.utils.pinnedlistview.Item;
import com.kippin.utils.pinnedlistview.SectionItem;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModalSubIndustry;
import com.kippin.webclient.model.ModelCurrency;
import com.kippin.webclient.model.ModelCurrencys;
import com.kippin.webclient.model.ModelIndustry;
import com.kippin.webclient.model.ModelIndustrys;
import com.kippin.webclient.model.ModelLogin;
import com.kippin.webclient.model.ModelOwnership;
import com.kippin.webclient.model.ModelOwnerships;
import com.kippin.webclient.model.ModelRegistration;
import com.kippin.webclient.model.ProvinceModel;
import com.kippin.webclient.model.TemplateData;

import java.util.ArrayList;

/**
 * Created by dilip.singh on 1/22/2016.
 */
public class PerformBook extends SuperActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public ArrayList<ModelOwnership> arrayListOwnerships;
    public ArrayList<ModelCurrency> arrayListCurrencies;
    public ArrayList<String> arrayListDays = new ArrayList<>();
    public ArrayList<String> arrayMonthno = new ArrayList<>();
    public ArrayList<String> arrayMonthname = new ArrayList<>();
    public ArrayList<String> arrayyear = new ArrayList<>();
    public ArrayList<ModelIndustry> arrayListIndustries;
    public ArrayList<ModalSubIndustry> arrayListSubIndustries;
    //    @Bind(R.id.payment_proceed)
    Button imageViewProceedPayment;
    // @Bind(R.id.firstname)
    EditText editTextFirstName;
    //   @Bind(R.id.lastname)
    EditText editTextLastname;
    //   @Bind(R.id.mobile_number)
    EditText editTextMobileNumber;
    //   @Bind(R.id.city)
    EditText editTextCity;
    // @Bind(R.id.postal_code)
    EditText editTextPostalCode;
    // @Bind(R.id.corporation_address)
    EditText editTextCorporation;
    // @Bind(R.id.gst_hst)
    EditText editTextGstHst;
    //  @Bind(R.id.bussiness_number)
    EditText editTextBusinessNumber;
    // @Bind(R.id.industry)
    TextView textViewIndustry;
    // @Bind(R.id.subIndustry)
    TextView textViewSubIndustry;
    //  @Bind(R.id.imageview_indutsry)
    ImageView imageViewIndustry;
    //    @Bind(R.id.imageview_subIndutsry)
    ImageView imageViewSubIndustry;
    TextView currency;
    TextView startdate;
    TextView month;
    TextView year;
    //   @Bind(R.id.company_name)
    EditText editTextCompanyName;
    //  @Bind(R.id.ownership)
    TextView textViewOwnership;
    // @Bind(R.id.image_ownership)
    ImageView imageViewOwnership;
    //  @Bind(R.id.currency)
    TextView textViewCurrency;
    // @Bind(R.id.image_currency)
    ImageView imageViewCurrency;
    DialogBox dialogBox;
    // @Bind(R.id.spinner_industry)
    Spinner spinnerIndustry;
    // @Bind(R.id.spinner_ownership)
    Spinner spinnerOwnership;
    // @Bind(R.id.spinner_currency)
    Spinner spinnerCurrency;
    //  @Bind(R.id.province_text)
    TextView textViewProvince;
    ImageView image_province;
    //@Bind(R.id.image_province)
    ImageView imageViewProvinceArrow;
    ImageView image_startdate;
    ImageView image_month;
    ImageView image_year;
    //    @Bind(R.id.layProvince)
    LinearLayout layProvince;
    //   @Bind(R.id.spinner_province)
    Spinner spinnerProvince;
    //    @Bind(R.id.spinner_subIndustry)
    Spinner spinnerSubIndustry;
    ArrayList<ProvinceModel> arrayListSpinnerProvince;
    ProvinceModel provinceModel;
    SpinnerAdapter spinnerAdapter;
    ArrayList<String> arrayListSpinnerIndustry;
    ArrayList<String> arrayListSpinnerOwnership;
    ArrayList<String> arrayListSpinnerCurrency;
    IndustryAdapter industryAdapter;
    SubIndustryAdapter subIndustryAdapter;
    OwnerShipAdapter ownershipAdapter;
    CurrencyAdapter currencyAdapter;
    DaysAdapter daysAdapter;
    MonthAdapter monthAdapter;
    YearAdapter yearAdapter;
    String mProvince, mIndustry, mOwnership, mCurrency;
    String mUsername, mPassword, mEmailAddress;
    Bundle nBundle;
    ModelOwnership modelOwnership;
    ModelCurrency modelCurrency;
    ModelIndustry modelIndustry;
    ModalSubIndustry modelSubIndustry;


    String[] arrayString;
    Spinner spinner_startdate;
    Spinner spinner_month;
    Spinner spinner_year;


    //@Bind(R.id.lvIndustrySubIndustry)
    ListView lvIndustrySubIndustry;
    TextView ownership;
    //@Bind(R.id.rvIndustrySubIndustry)
    RelativeLayout rvIndustrySubIndustry;
    LinearLayout layIndustry, layOwnership, layCurrency, laySubIndustry, layStartdate, layMonth, layYear;
    Handler handler
            = new Handler();
    PhoneWatcher phoneWatcher = null;
    View.OnKeyListener key = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (!((EditText) v).toString().isEmpty())
                v.focusSearch(View.FOCUS_RIGHT).requestFocus();

            return false;
        }
    };
    String[] yeararray;
    String[] montharray;
    String[] daysarray;
    String taxstartyear = "0";
    String taxstartmonthid = "0";
    String taxstartday = "0";
    private boolean isPaid = false;

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        imageViewProvinceArrow = (ImageView) findViewById(R.id.image_province);
        image_startdate = (ImageView) findViewById(R.id.image_startdate);
        image_month = (ImageView) findViewById(R.id.image_month);
        image_year = (ImageView) findViewById(R.id.image_year);

        spinnerSubIndustry = (Spinner) findViewById(R.id.spinner_subIndustry);
        currency = (TextView) findViewById(R.id.currency);
        startdate = (TextView) findViewById(R.id.startdate);
        month = (TextView) findViewById(R.id.month);
        year = (TextView) findViewById(R.id.year);

        editTextFirstName = (EditText) findViewById(R.id.firstname);
        layProvince = (LinearLayout) findViewById(R.id.layProvince);
        editTextLastname = (EditText) findViewById(R.id.lastname);
        editTextMobileNumber = (EditText) findViewById(R.id.mobile_number);
        editTextCity = (EditText) findViewById(R.id.city);
        editTextPostalCode = (EditText) findViewById(R.id.postal_code);
        editTextCorporation = (EditText) findViewById(R.id.corporation_address);
        imageViewIndustry = (ImageView) findViewById(R.id.imageview_indutsry);
        imageViewProceedPayment = (Button) findViewById(R.id.payment_proceed);
        editTextGstHst = (EditText) findViewById(R.id.gst_hst);
        editTextBusinessNumber = (EditText) findViewById(R.id.bussiness_number);
        textViewIndustry = (TextView) findViewById(R.id.industry);
        textViewSubIndustry = (TextView) findViewById(R.id.subIndustry);
        editTextCompanyName = (EditText) findViewById(R.id.company_name);
        textViewOwnership = (TextView) findViewById(R.id.ownership);
        imageViewOwnership = (ImageView) findViewById(R.id.image_ownership);
        textViewCurrency = (TextView) findViewById(R.id.currency);
        imageViewCurrency = (ImageView) findViewById(R.id.image_currency);
        spinnerOwnership = (Spinner) findViewById(R.id.spinner_ownership);
        spinnerIndustry = (Spinner) findViewById(R.id.spinner_industry);
        spinnerCurrency = (Spinner) findViewById(R.id.spinner_currency);
        textViewProvince = (TextView) findViewById(R.id.province_text);
        spinnerProvince = (Spinner) findViewById(R.id.spinner_province);

        spinner_startdate = (Spinner) findViewById(R.id.spinner_startdate);
        spinner_month = (Spinner) findViewById(R.id.spinner_month);
        spinner_year = (Spinner) findViewById(R.id.spinner_year);


        lvIndustrySubIndustry = (ListView) findViewById(R.id.lvIndustrySubIndustry);
        rvIndustrySubIndustry = (RelativeLayout) findViewById(R.id.rvIndustrySubIndustry);
        image_province = (ImageView) findViewById(R.id.image_province);
        imageViewSubIndustry = (ImageView) findViewById(R.id.imageview_subIndutsry);
        ownership = (TextView) findViewById(R.id.ownership);
        layIndustry = (LinearLayout) findViewById(R.id.layIndustry);
        layOwnership = (LinearLayout) findViewById(R.id.layOwnership);
        layCurrency = (LinearLayout) findViewById(R.id.layCurrency);
        laySubIndustry = (LinearLayout) findViewById(R.id.laySubIndustry);
        layStartdate = (LinearLayout) findViewById(R.id.layStartdate);
        layMonth = (LinearLayout) findViewById(R.id.layMonth);
        layYear = (LinearLayout) findViewById(R.id.layYear);


        yeararray = getResources().getStringArray(R.array.years_array);
        daysarray = getResources().getStringArray(R.array.day_array);
        montharray = getResources().getStringArray(R.array.month_array);
        editTextGstHst.setHint("Tax Number");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.checkCrashTracker(PerformBook.this);
        setContentView(R.layout.layout_perform_book);
        initialiseUI();
        setUpListeners();
        // requestForFocus();
        //    ButterKnife.bind(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            mUsername = bundle.getString("USERNAME");
            mPassword = bundle.getString("PASSWORD");
            mEmailAddress = bundle.getString("EMAILADDRESS");
            isPaid = "true".equalsIgnoreCase(bundle.getString("PAID"));
        }

        //Province Spinner
        arrayListSpinnerProvince = new ArrayList<ProvinceModel>();
        callProvinceApi(arrayListSpinnerProvince);


        generateActionBar(R.string.plz_fill_all_further_detail, true, false);


        checkIndustryData();


        checkOwnershipData();
        checkCurrencyData();
        spinnerDays();
        spinnerMonth();
        spinnerYear();

        phoneWatcher = new PhoneWatcher(editTextMobileNumber);

        editTextMobileNumber.addTextChangedListener(phoneWatcher);

        editTextMobileNumber.setOnEditorActionListener(new MyEditorInfoListener(spinnerProvince, editTextMobileNumber));
        editTextBusinessNumber.setOnEditorActionListener(new MyEditorInfoListener(spinnerIndustry, editTextBusinessNumber));
        editTextCompanyName.setOnEditorActionListener(new MyEditorInfoListener(spinnerOwnership, editTextCompanyName));
        editTextPostalCode.setOnEditorActionListener(new MyEditorInfoListenerEditText(editTextCorporation));


        rvIndustrySubIndustry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvIndustrySubIndustry.setVisibility(View.GONE);
            }
        });
    }

    private void populateIndustrySubIndustryList() {

        ArrayList<Item> items = new ArrayList<Item>();

        for (int i = 0; i < arrayListIndustries.size(); i++) {

            items.add(new SectionItem(arrayListIndustries.get(i).getIndustryType(), R.color.holo_red_light));

            ArrayList<ModalSubIndustry> modalSubIndustries = arrayListIndustries.get(i).getObjSubIndustryList();

            if (modalSubIndustries == null) modalSubIndustries = new ArrayList<>();

            for (int j = 0; j < modalSubIndustries.size(); j++) {
                items.add(new EntryItem(modalSubIndustries.get(j).getSubIndustryName(), "", i, j));
            }

        }

        AdapterIncomeStatement_PerformBook adapterIncomeStatement = new AdapterIncomeStatement_PerformBook(PerformBook.this, lvIndustrySubIndustry, items, new IntefaceEntryItemListener() {
            @Override
            public void onClick(int itemPosition, int subitemPosition) {
                mIndustry = arrayListIndustries.get(itemPosition).getId();
                modelSubIndustry = arrayListIndustries.get(itemPosition).getObjSubIndustryList().get(subitemPosition);

                textViewIndustry.setText(modelSubIndustry.getSubIndustryName());

                textViewIndustry.setTextColor(Color.BLACK);

                rvIndustrySubIndustry.setVisibility(View.GONE);
            }
        });

        lvIndustrySubIndustry.setAdapter(adapterIncomeStatement);


    }

    private void checkCurrencyData() {
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.wsInterface = new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {

                arrayListCurrencies = ((ModelCurrencys) data.getData(ModelCurrencys.class)).modelCurrencies;


                modelCurrency = new ModelCurrency();

                modelCurrency.setId("0");
                modelCurrency.setCurrencyType(" Currency ");
                arrayListCurrencies.add(0, modelCurrency);


                currencyAdapter = new CurrencyAdapter(PerformBook.this, R.layout.layout_spinner_textview, arrayListCurrencies);
                spinnerCurrency.setAdapter(currencyAdapter);
                spinnerCurrency.setOnItemSelectedListener(PerformBook.this);


            }
        };
        wsTemplate.url = Url.URL_CURRENCY;
        wsTemplate.aClass = ModelCurrencys.class;
        wsTemplate.methods = WSMethods.GET;
        wsTemplate.context = this;

        new WSHandler(wsTemplate).execute();

    }

    private void checkOwnershipData() {
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.wsInterface = new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {

                arrayListOwnerships = ((ModelOwnerships) data.getData(ModelOwnerships.class)).modelOwnerships;


                modelOwnership = new ModelOwnership();
                modelOwnership.setId("0");
                modelOwnership.setOwnershipType(" Ownership ");
                arrayListOwnerships.add(0, modelOwnership);


                ownershipAdapter = new OwnerShipAdapter
                        (PerformBook.this, android.R.layout.simple_spinner_item, arrayListOwnerships);
                spinnerOwnership.setAdapter(ownershipAdapter);
                spinnerOwnership.setOnItemSelectedListener(PerformBook.this);


            }
        };
        wsTemplate.url = Url.URL_OWNERSHIP;
        wsTemplate.aClass = ModelOwnerships.class;
        wsTemplate.methods = WSMethods.GET;
        wsTemplate.context = this;

        new WSHandler(wsTemplate).execute();

    }

    private void checkIndustryData() {

        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.wsInterface = new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {

                arrayListIndustries = ((ModelIndustrys) data.getData(ModelIndustrys.class)).modelIndustries;

                industryAdapter = new IndustryAdapter
                        (PerformBook.this, android.R.layout.simple_spinner_item, arrayListIndustries);
//                spinnerIndustry.setAdapter(industryAdapter);
//                spinnerIndustry.setOnItemSelectedListener(PerformBook.this);


                populateIndustrySubIndustryList();

            }
        };
        wsTemplate.url = Url.URL_INDUSTRY;
        wsTemplate.aClass = ModelIndustrys.class;
        wsTemplate.methods = WSMethods.GET;
        wsTemplate.context = this;

        new WSHandler(wsTemplate).execute();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        int id1 = spinner.getId();
        if (id1 == R.id.spinner_province) {
            if (position == 0) {
                textViewProvince.setTextColor(getResources().getColor(R.color.select_date_et));
            } else
                textViewProvince.setTextColor(Color.BLACK);
            textViewProvince.setText(arrayListSpinnerProvince.get(position).getProvinceName());
            mProvince = arrayListSpinnerProvince.get(position).getProvinceId();
            Utility.printLog("id is ", mProvince);
        } else if (id1 == R.id.spinner_startdate) {
            startdate.setText("");
            if (position == 0) {
                startdate.setTextColor(getResources().getColor(R.color.select_date_et));
                startdate.setTextColor(getResources().getColor(R.color.select_date_et));
            } else {
                startdate.setTextColor(Color.BLACK);
                startdate.setTextColor(Color.BLACK);
            }
            startdate.setText(arrayListDays.get(position));
            if (position == 0) {
            } else {
                taxstartday = arrayListDays.get(position);
            }
        } else if (id1 == R.id.spinner_month) {
            month.setText("");
            if (position == 0) {
                month.setTextColor(getResources().getColor(R.color.select_date_et));
                month.setTextColor(getResources().getColor(R.color.select_date_et));
            } else {
                month.setTextColor(Color.BLACK);
                month.setTextColor(Color.BLACK);
            }
            month.setText(arrayMonthname.get(position));
            if (position == 0) {
            } else {
                taxstartmonthid = arrayMonthno.get(position);
            }
        } else if (id1 == R.id.spinner_year) {
            Log.e("position:", "" + position);
            year.setText("");
            if (position == 0) {
                year.setTextColor(getResources().getColor(R.color.select_date_et));
                year.setTextColor(getResources().getColor(R.color.select_date_et));
            } else {
                year.setTextColor(Color.BLACK);
                year.setTextColor(Color.BLACK);
            }
            if (position == 0) {
            } else {
                taxstartyear = arrayyear.get(position);
            }
            year.setText(arrayyear.get(position));

        } else if (id1 == R.id.spinner_industry) {
            modelSubIndustry = null;
            textViewSubIndustry.setText("");

            if (position == 0) {
                textViewIndustry.setTextColor(getResources().getColor(R.color.select_date_et));
                textViewSubIndustry.setTextColor(getResources().getColor(R.color.select_date_et));
            } else {
                textViewIndustry.setTextColor(Color.BLACK);
                textViewSubIndustry.setTextColor(Color.BLACK);
            }

            textViewIndustry.setText(arrayListIndustries.get(position).getIndustryType());
            mIndustry = arrayListIndustries.get(position).getId();
        } else if (id1 == R.id.spinner_ownership) {
            if (position == 0) {
                textViewOwnership.setTextColor(getResources().getColor(R.color.select_date_et));
            } else
                textViewOwnership.setTextColor(Color.BLACK);
            textViewOwnership.setText(arrayListOwnerships.get(position).getOwnershipType());
            mOwnership = arrayListOwnerships.get(position).getId();
        } else if (id1 == R.id.spinner_currency) {
            if (position == 0) {
                textViewCurrency.setTextColor(getResources().getColor(R.color.select_date_et));
            } else
                textViewCurrency.setTextColor(Color.BLACK);
            textViewCurrency.setText(arrayListCurrencies.get(position).getCurrencyType());
            mCurrency = arrayListCurrencies.get(position).getId();
        }
        /*switch (spinner.getId()) {
            case R.id.spinner_province:
                if (position == 0) {
                    textViewProvince.setTextColor(getResources().getColor(R.color.select_date_et));
                } else
                    textViewProvince.setTextColor(Color.BLACK);
                textViewProvince.setText(arrayListSpinnerProvince.get(position).getProvinceName());
                mProvince = arrayListSpinnerProvince.get(position).getProvinceId();
                Utility.printLog("id is ", mProvince);

                break;
            case R.id.spinner_industry:
                modelSubIndustry = null;
                textViewSubIndustry.setText("");

                if (position == 0) {
                    textViewIndustry.setTextColor(getResources().getColor(R.color.select_date_et));
                    textViewSubIndustry.setTextColor(getResources().getColor(R.color.select_date_et));
                } else {
                    textViewIndustry.setTextColor(Color.BLACK);
                    textViewSubIndustry.setTextColor(Color.BLACK);
                }

                textViewIndustry.setText(arrayListIndustries.get(position).getIndustryType());
                mIndustry = arrayListIndustries.get(position).getId();

//                if (position != 0)
//                    setSubIndustryAdapter(position);

                break;

//            case R.id.spinner_subIndustry:
//                modelSubIndustry = arrayListSubIndustries.get(position);
//                textViewSubIndustry.setTextColor(Color.BLACK);
//                textViewSubIndustry.setText(modelSubIndustry.getSubIndustryName());
//                break;

            case R.id.spinner_ownership:
                if (position == 0) {
                    textViewOwnership.setTextColor(getResources().getColor(R.color.select_date_et));
                } else
                    textViewOwnership.setTextColor(Color.BLACK);
                textViewOwnership.setText(arrayListOwnerships.get(position).getOwnershipType());
                mOwnership = arrayListOwnerships.get(position).getId();
                break;
            case R.id.spinner_currency:
                if (position == 0) {
                    textViewCurrency.setTextColor(getResources().getColor(R.color.select_date_et));
                } else
                    textViewCurrency.setTextColor(Color.BLACK);
                textViewCurrency.setText(arrayListCurrencies.get(position).getCurrencyType());
                mCurrency = arrayListCurrencies.get(position).getId();
                break;

        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    // @OnClick(R.id.payment_proceed)
    public void onProceedPaymentClick(View v) {
        /*if (editTextFirstName.getText().toString().isEmpty() ||
                editTextLastname.getText().toString().isEmpty() ||
                editTextMobileNumber.getText().toString().isEmpty())

        {
            dialogBox = new DialogBox(getResources().getString(R.string.plz_fill_empty_mandatory), PerformBook.this);

        } else if (!Utility.validCellPhone(editTextMobileNumber.getText().toString()) && editTextMobileNumber.getText().toString().length() != 1) {
            dialogBox = new DialogBox(getResources().getString(R.string.plz_enter_valide_phone), PerformBook.this);
        } else if (editTextCity.getText().toString().isEmpty() ||
                editTextPostalCode.getText().toString().isEmpty()
                ) {

            dialogBox = new DialogBox(getResources().getString(R.string.plz_fill_empty_mandatory), PerformBook.this);
        } else if (editTextPostalCode.getText().toString().length() != 6) {
            dialogBox = new DialogBox(getResources().getString(R.string.postal_code_must_be), PerformBook.this);
        } else if (editTextCorporation.getText().toString().isEmpty() ||
                editTextGstHst.getText().toString().isEmpty() ||
                editTextBusinessNumber.getText().toString().isEmpty() ||
                editTextCompanyName.getText().toString().isEmpty()) {
            dialogBox = new DialogBox(getResources().getString(R.string.plz_fill_empty_mandatory), PerformBook.this);
        } else if (mProvince.equalsIgnoreCase("0")) {
            dialogBox = new DialogBox(getResources().getString(R.string.plz_select_province), PerformBook.this);
        } else if (mOwnership.equalsIgnoreCase("0")) {

            dialogBox = new DialogBox(getResources().getString(R.string.plz_select_ownership), PerformBook.this);
        } else if (mIndustry ==null  ) {
            dialogBox = new DialogBox(getResources().getString(R.string.plz_select_industry), PerformBook.this);
        } else if (modelSubIndustry == null) {
            dialogBox = new DialogBox(getResources().getString(R.string.plz_select_sub_industry), PerformBook.this);
        } else if (mCurrency.equalsIgnoreCase("0")) {
            dialogBox = new DialogBox(getResources().getString(R.string.plz_select_currency), PerformBook.this);
        } else {
            WSTemplate wsTemplate = new WSTemplate();
            wsTemplate.aClass = ModelRegistration.class;
            wsTemplate.context = PerformBook.this;
            wsTemplate.message_id = R.string.app_name;
            wsTemplate.methods = WSMethods.POST;
            wsTemplate.requestCode = 1001;
//            wsTemplate.url = Url.BASE_URL_COMPANY;

            wsTemplate.url = Url.URL_USER_AS_AN_ACC_REGISTRATION;


            ArrayListPost templatePosts = new ArrayListPost();
            templatePosts.add("FirstName", editTextFirstName.getText().toString());
            templatePosts.add("LastName", editTextLastname.getText().toString());
            templatePosts.add("Email", mEmailAddress);
            templatePosts.add("Username", mUsername);
            templatePosts.add("Password", mPassword);
            templatePosts.add("MobileNumber", editTextMobileNumber.getText().toString()*//*.replace("-","")*//*);
            templatePosts.add("CountryId", "1");
            templatePosts.add("province", mProvince);
            templatePosts.add("City", editTextCity.getText().toString());
            templatePosts.add("PostalCode", editTextPostalCode.getText().toString());
            templatePosts.add("CompanyName", editTextCompanyName.getText().toString());
            templatePosts.add("CorporationAddress", editTextCorporation.getText().toString());
            templatePosts.add("GSTNumber", editTextGstHst.getText().toString());
            templatePosts.add("BusinessNumber", editTextBusinessNumber.getText().toString());
            templatePosts.add("OwnershipId", mOwnership);
            templatePosts.add("IndustryId", mIndustry);
            templatePosts.add("SubIndustryId", modelSubIndustry.getSubIndustryId());
            templatePosts.add("CurrencyId", mCurrency);
            wsTemplate.templatePosts = templatePosts;
            wsTemplate.wsInterface = new WSInterface() {
                @Override
                public void onResult(int requestCode, TemplateData data) {
                    switch (requestCode) {
                        case 1001:
                            ModelRegistration modelRegistration = data.getData(ModelRegistration.class);

                            if (modelRegistration.getResponseCode().equalsIgnoreCase("1")) {
                                if (true) {

                                    Singleton.putUser(new ModelLogin());
                                    Singleton.getUser().setId(modelRegistration.getUserId());

                                    arrayString = new String[]{modelRegistration.getUserId(), mCurrency};

//                                        dialogBox = new DialogBox(PerformBook.this,getResources().getString(R.string.successfully_detailed_added_by_performbook),
//                                        arrayString,"");

//                                    new DialogBox(PerformBook.this, getResources().getString(R.string.successfully_detailed_added_by_performbook), new DialogBoxListener() {
//                                        @Override
//                                        public void onDialogOkPressed() {
//                                            new PaymentConfirmationDialog(PerformBook.this).show();
//                                        }
//                                    });

                                    new DialogBox(PerformBook.this, getResources().getString(R.string.pls_chk_email_to_proceed), new DialogBoxListener() {
                                        @Override
                                        public void onDialogOkPressed() {
                                            Utility.logout(PerformBook.this, true);
                                        }
                                    });
                                } else {
                                    new DialogBox(PerformBook.this, modelRegistration.getResponseMessage(), new DialogBoxListener() {
                                        @Override
                                        public void onDialogOkPressed() {
                                            Utility.logout(PerformBook.this, true);
                                        }
                                    });
                                }

                            } else {

                                dialogBox = new DialogBox(modelRegistration.getResponseMessage(), PerformBook.this);

                            }
                            break;

                    }

                }
            };
            new WSHandler(wsTemplate).execute();


        }*/

    }

//    private void setSubIndustryAdapter(int postition) {
//        arrayListSubIndustries = arrayListIndustries.get(postition).getObjSubIndustryList();
//        subIndustryAdapter = new SubIndustryAdapter(PerformBook.this, android.R.layout.simple_spinner_item, arrayListSubIndustries);
//        spinnerSubIndustry.setAdapter(subIndustryAdapter);
//        spinnerSubIndustry.setOnItemSelectedListener(PerformBook.this);
//    }

    //@OnClick({R.id.imageview_indutsry, R.id.industry, R.id.image_ownership, R.id.ownership
    // , R.id.image_currency, R.id.currency, R.id.image_province, R.id.province_text, R.id.imageview_subIndutsry, R.id.subIndustry})
    public void onArrowClick(View view) {
        int id = view.getId();
        if (id == R.id.image_province || id == R.id.province_text) {
            spinnerProvince.performClick();
        } else if (id == R.id.industry || id == R.id.imageview_indutsry) {
            rvIndustrySubIndustry.setVisibility(View.VISIBLE);
        } else if (id == R.id.subIndustry || id == R.id.imageview_subIndutsry) {

        } else if (id == R.id.ownership || id == R.id.image_ownership) {
            spinnerOwnership.performClick();
        } /*else if (id == R.id.currency || id == R.id.image_currency) {
            spinnerCurrency.performClick();
        } */ else if (id == R.id.startdate || id == R.id.image_startdate) {
            spinner_startdate.performClick();
        } else if (id == R.id.month || id == R.id.image_month) {
            spinner_month.performClick();
        } else if (id == R.id.year || id == R.id.image_year) {
            spinner_year.performClick();
        }

        /*switch (view.getId()) {

            case R.id.image_province:
            case R.id.province_text:
                spinnerProvince.performClick();
                break;

            case R.id.imageview_indutsry:
            case R.id.industry:
//                spinnerIndustry.performClick();
                rvIndustrySubIndustry.setVisibility(View.VISIBLE);
                break;

//            case R.id.subIndustry:
//            case R.id.imageview_subIndutsry:
////                spinnerSubIndustry.performClick();
//                break;

            case R.id.ownership:
            case R.id.image_ownership:
                spinnerOwnership.performClick();
                break;

            case R.id.currency:
            case R.id.image_currency:
                spinnerCurrency.performClick();
                break;
        }*/

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (rvIndustrySubIndustry.getVisibility() == View.VISIBLE) {

            rvIndustrySubIndustry.setVisibility(View.GONE);

        } else {
            finish();
            PerformBook.this.overridePendingTransition(R.anim.push_out_to_left,
                    R.anim.push_out_to_right);
        }

    }

    /**
     * Method use for call Province api
     *
     * @param arrayList
     */
    private void callProvinceApi(final ArrayList<ProvinceModel> arrayList) {
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.aClass = ModelProvinceArrayData.class;
        wsTemplate.context = PerformBook.this;
        wsTemplate.message_id = R.string.app_name;
        wsTemplate.methods = WSMethods.GET;
        wsTemplate.requestCode = 1001;
        wsTemplate.url = Url.URL_PROVINCE;

        wsTemplate.wsInterface = new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {

                switch (requestCode) {
                    case 1001:
                        ModelProvinceArrayData modelProvinceArrayData = data.getData(ModelProvinceArrayData.class);
                        provinceModel = new ProvinceModel();
                        provinceModel.setProvinceId("0");
                        provinceModel.setProvinceName(" Province/State ");
                        arrayList.add(provinceModel);
                        for (int i = 0; i < modelProvinceArrayData.modelProvinces.size(); i++) {
                            provinceModel = new ProvinceModel();
                            provinceModel.setProvinceId(modelProvinceArrayData.modelProvinces.get(i).getId());
                            provinceModel.setProvinceName(modelProvinceArrayData.modelProvinces.get(i).getProvinceName());
                            arrayList.add(provinceModel);
                        }


                        if (arrayList.size() != 0) {
                            spinnerAdapter = new SpinnerAdapter(PerformBook.this, R.layout.layout_spinner_textview, arrayList);
                            spinnerProvince.setAdapter(spinnerAdapter);
                            spinnerProvince.setOnItemSelectedListener(PerformBook.this);
                        }

                        break;
                }

            }
        };
        new WSHandler(wsTemplate).execute();

    }

    @Override
    public void setUpListeners() {

        findViewById(R.id.payment_proceed).setOnClickListener(this);
        findViewById(R.id.imageview_indutsry).setOnClickListener(this);
        findViewById(R.id.industry).setOnClickListener(this);
        findViewById(R.id.image_ownership).setOnClickListener(this);
        findViewById(R.id.ownership).setOnClickListener(this);
        findViewById(R.id.image_currency).setOnClickListener(this);
        findViewById(R.id.currency).setOnClickListener(this);
        findViewById(R.id.image_province).setOnClickListener(this);
        findViewById(R.id.province_text).setOnClickListener(this);
        findViewById(R.id.imageview_subIndutsry).setOnClickListener(this);
        findViewById(R.id.subIndustry).setOnClickListener(this);
        ((Spinner) findViewById(R.id.spinner_industry)).setOnItemSelectedListener(this);
        ((Spinner) findViewById(R.id.spinner_subIndustry)).setOnItemSelectedListener(this);
        ((Spinner) findViewById(R.id.spinner_ownership)).setOnItemSelectedListener(this);

        ((Spinner) findViewById(R.id.spinner_currency)).setOnItemSelectedListener(this);
        ((Spinner) findViewById(R.id.spinner_province)).setOnItemSelectedListener(this);


        // Submit Payment Info
        imageViewProceedPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        {
                            if (editTextFirstName.getText().toString().isEmpty() ||
                                    editTextLastname.getText().toString().isEmpty() ||
                                    editTextMobileNumber.getText().toString().isEmpty())

                            {
                                dialogBox = new DialogBox(getResources().getString(R.string.plz_fill_empty_mandatory_), PerformBook.this);

                            } else if (!Utility.validCellPhone(editTextMobileNumber.getText().toString()) && editTextMobileNumber.getText().toString().length() != 1) {
                                dialogBox = new DialogBox(getResources().getString(R.string.plz_enter_valide_phone), PerformBook.this);
                            } else if (editTextCity.getText().toString().isEmpty() ||
                                    editTextPostalCode.getText().toString().isEmpty()
                                    ) {

                                dialogBox = new DialogBox(getResources().getString(R.string.plz_fill_empty_mandatory_), PerformBook.this);
                            } else if (editTextPostalCode.getText().toString().length() == 1 || editTextPostalCode.getText().toString().length() == 2 || editTextPostalCode.getText().toString().length() == 3 || editTextPostalCode.getText().toString().length() == 4 || editTextPostalCode.getText().toString().length() == 5) {
                                dialogBox = new DialogBox(getResources().getString(R.string.postal_code_must_be), PerformBook.this);
                            } else if (editTextCorporation.getText().toString().isEmpty() ||
                                    editTextGstHst.getText().toString().isEmpty() ||
                                    editTextBusinessNumber.getText().toString().isEmpty() ||
                                    editTextCompanyName.getText().toString().isEmpty()) {
                                dialogBox = new DialogBox(getResources().getString(R.string.plz_fill_empty_mandatory_), PerformBook.this);
                            } else if (mProvince.equalsIgnoreCase("0")) {
                                dialogBox = new DialogBox(getResources().getString(R.string.plz_select_province), PerformBook.this);
                            } else if (mOwnership.equalsIgnoreCase("0")) {

                                dialogBox = new DialogBox(getResources().getString(R.string.plz_select_ownership), PerformBook.this);
                            } else if (mIndustry == null) {
                                dialogBox = new DialogBox(getResources().getString(R.string.plz_select_industry), PerformBook.this);
                            } else if (modelSubIndustry == null) {
                                dialogBox = new DialogBox(getResources().getString(R.string.plz_select_sub_industry), PerformBook.this);
                            } /*else if (mCurrency.equalsIgnoreCase("0")) {
                                dialogBox = new DialogBox(getResources().getString(R.string.plz_select_currency), PerformBook.this);
                            }*/ else if (taxstartyear.equalsIgnoreCase("0")) {
                                dialogBox = new DialogBox(getResources().getString(R.string.plz_select_year), PerformBook.this);
                            } else if (taxstartmonthid.equalsIgnoreCase("0") || taxstartmonthid.equalsIgnoreCase("00")) {
                                dialogBox = new DialogBox(getResources().getString(R.string.plz_select_month), PerformBook.this);
                            } else if (taxstartday.equalsIgnoreCase("0")) {
                                dialogBox = new DialogBox(getResources().getString(R.string.plz_select_date), PerformBook.this);
                            } else {
                                WSTemplate wsTemplate = new WSTemplate();
                                wsTemplate.aClass = ModelRegistration.class;
                                wsTemplate.context = PerformBook.this;
                                wsTemplate.message_id = R.string.app_name;
                                wsTemplate.methods = WSMethods.POST;
                                wsTemplate.requestCode = 1001;
//            wsTemplate.url = Url.BASE_URL_COMPANY;

                                wsTemplate.url = Url.URL_USER_AS_AN_ACC_REGISTRATION;


                                ArrayListPost templatePosts = new ArrayListPost();
                                templatePosts.add("FirstName", editTextFirstName.getText().toString());
                                templatePosts.add("LastName", editTextLastname.getText().toString());
                                templatePosts.add("Email", mEmailAddress);
                                templatePosts.add("Username", mUsername);
                                templatePosts.add("Password", mPassword);
                                templatePosts.add("MobileNumber", editTextMobileNumber.getText().toString()/*.replace("-","")*/);
                                templatePosts.add("CountryId", "1");
                                templatePosts.add("province", mProvince);
                                templatePosts.add("City", editTextCity.getText().toString());
                                templatePosts.add("PostalCode", editTextPostalCode.getText().toString());
                                templatePosts.add("CompanyName", editTextCompanyName.getText().toString());
                                templatePosts.add("CorporationAddress", editTextCorporation.getText().toString());
                                templatePosts.add("GSTNumber", editTextGstHst.getText().toString());
                                templatePosts.add("BusinessNumber", editTextBusinessNumber.getText().toString());
                                templatePosts.add("OwnershipId", mOwnership);
                                templatePosts.add("IndustryId", mIndustry);
                                templatePosts.add("SubIndustryId", modelSubIndustry.getSubIndustryId());
                                // templatePosts.add("CurrencyId", mCurrency);
                                templatePosts.add("TaxStartYear", "" + taxstartyear);
                                templatePosts.add("TaxStartMonthId", "" + taxstartmonthid);
                                templatePosts.add("TaxationStartDay", "" + taxstartday);
                                wsTemplate.templatePosts = templatePosts;
                                wsTemplate.wsInterface = new WSInterface() {
                                    @Override
                                    public void onResult(int requestCode, TemplateData data) {
                                        switch (requestCode) {
                                            case 1001:
                                                ModelRegistration modelRegistration = data.getData(ModelRegistration.class);

                                                if (modelRegistration.getResponseCode().equalsIgnoreCase("1")) {
                                                    if (true) {

                                                        Singleton.putUser(new ModelLogin());
                                                        Singleton.getUser().setId(modelRegistration.getUserId());

                                                        //  arrayString = new String[]{modelRegistration.getUserId(), mCurrency};

//                                        dialogBox = new DialogBox(PerformBook.this,getResources().getString(R.string.successfully_detailed_added_by_performbook),
//                                        arrayString,"");

//                                    new DialogBox(PerformBook.this, getResources().getString(R.string.successfully_detailed_added_by_performbook), new DialogBoxListener() {
//                                        @Override
//                                        public void onDialogOkPressed() {
//                                            new PaymentConfirmationDialog(PerformBook.this).show();
//                                        }
//                                    });

                                                        new DialogBox(PerformBook.this, getResources().getString(R.string.pls_chk_email_to_proceed), new DialogBoxListener() {
                                                            @Override
                                                            public void onDialogOkPressed() {
                                                                Utility.logout(PerformBook.this, true);
                                                                Utility.startActivity(PerformBook.this, LoginScreen.class, true);
                                                            }
                                                        });
                                                    } else {
                                                        new DialogBox(PerformBook.this, modelRegistration.getResponseMessage(), new DialogBoxListener() {
                                                            @Override
                                                            public void onDialogOkPressed() {
                                                                Utility.logout(PerformBook.this, true);
                                                                Utility.startActivity(PerformBook.this, LoginScreen.class, true);
                                                            }
                                                        });
                                                    }

                                                } else {

                                                    dialogBox = new DialogBox(modelRegistration.getResponseMessage(), PerformBook.this);

                                                }
                                                break;

                                        }

                                    }
                                };
                                new WSHandler(wsTemplate).execute();


                            }
                        }

                    }
                }.start();

            }
        });

        imageViewIndustry.setOnClickListener(this);
        textViewSubIndustry.setOnClickListener(this);
        imageViewSubIndustry.setOnClickListener(this);
        textViewProvince.setOnClickListener(this);
        image_province.setOnClickListener(this);
        //imageViewCurrency.setOnClickListener(this);
        currency.setOnClickListener(this);
        startdate.setOnClickListener(this);
        month.setOnClickListener(this);
        year.setOnClickListener(this);
        image_month.setOnClickListener(this);
        image_year.setOnClickListener(this);
        image_startdate.setOnClickListener(this);
        ownership.setOnClickListener(this);
        imageViewOwnership.setOnClickListener(this);
        textViewIndustry.setOnClickListener(this);
        imageViewIndustry.setOnClickListener(this);
        layProvince.setOnClickListener(this);
        layIndustry.setOnClickListener(this);
        layOwnership.setOnClickListener(this);
        //layCurrency.setOnClickListener(this);
        laySubIndustry.setOnClickListener(this);
        layStartdate.setOnClickListener(this);
        layMonth.setOnClickListener(this);
        layYear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.image_province || id == R.id.province_text || id == R.id.layProvince) {
            spinnerProvince.performClick();
        } else if (id == R.id.industry || id == R.id.imageview_indutsry || id == R.id.layIndustry) {
            rvIndustrySubIndustry.setVisibility(View.VISIBLE);
        } else if (id == R.id.subIndustry || id == R.id.imageview_subIndutsry || id == R.id.laySubIndustry) {

        } else if (id == R.id.ownership || id == R.id.image_ownership || id == R.id.layOwnership) {
            spinnerOwnership.performClick();
        } /*else if (id == R.id.currency || id == R.id.image_currency || id == R.id.currency) {
            spinnerCurrency.performClick();
        }*/ else if (id == R.id.startdate || id == R.id.image_startdate || id == R.id.layStartdate) {
            spinner_startdate.performClick();
        } else if (id == R.id.month || id == R.id.image_month || id == R.id.layMonth) {

            spinner_month.performClick();
        } else if (id == R.id.year || id == R.id.image_year || id == R.id.layYear) {

            spinner_year.performClick();
        }
    }

    public void spinnerDays() {
        Log.e("length:", "" + daysarray.length);
        arrayListDays.clear();
        arrayListDays.add("Taxation Start Day");
        for (int i = 0; i < daysarray.length; i++) {
            arrayListDays.add(daysarray[i]);
        }
        daysAdapter = new DaysAdapter(PerformBook.this, R.layout.layout_spinner_textview, arrayListDays);
        spinner_startdate.setAdapter(daysAdapter);
        spinner_startdate.setOnItemSelectedListener(PerformBook.this);
    }

    public void spinnerMonth() {
        arrayMonthno.clear();
        arrayMonthname.clear();
        arrayMonthname.add("Taxation Start Month");
        for (int i = 0; i < montharray.length; i++) {
            arrayMonthname.add(montharray[i]);
        }
        arrayMonthno.add("00");
        arrayMonthno.add("01");
        arrayMonthno.add("02");
        arrayMonthno.add("03");
        arrayMonthno.add("04");
        arrayMonthno.add("05");
        arrayMonthno.add("06");
        arrayMonthno.add("07");
        arrayMonthno.add("08");
        arrayMonthno.add("09");
        arrayMonthno.add("10");
        arrayMonthno.add("11");
        arrayMonthno.add("12");
        monthAdapter = new MonthAdapter(PerformBook.this, R.layout.layout_spinner_textview, arrayMonthname);
        spinner_month.setAdapter(monthAdapter);
        spinner_month.setOnItemSelectedListener(PerformBook.this);

    }

    public void spinnerYear() {
        arrayyear.clear();
        arrayyear.add("Taxation Start Year");
        for (int i = 0; i < yeararray.length; i++) {
            arrayyear.add(yeararray[i]);
        }
        yearAdapter = new YearAdapter(PerformBook.this, R.layout.layout_spinner_textview, arrayyear);
        spinner_year.setAdapter(yearAdapter);
        spinner_year.setOnItemSelectedListener(PerformBook.this);

    }


    class MyEditorInfoListener implements TextView.OnEditorActionListener {
        Spinner spinner;
        EditText editText;

        public MyEditorInfoListener(Spinner spinner, EditText editText) {
            this.spinner = spinner;
            this.editText = editText;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            Utility.hideKeyboard(PerformBook.this, editText);

            new CountDownTimer(500, 200) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    spinner.performClick();
                }
            }.start();


//            if (actionId == EditorInfo.IME_ACTION_NEXT) {
//                spinner.performClick();
//            }

            return true;
        }
    }

    class MyEditorInfoListenerEditText implements TextView.OnEditorActionListener {

        EditText editText;

        public MyEditorInfoListenerEditText(EditText spinner) {
            this.editText = spinner;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            editText.performClick();
            editText.requestFocus();
            return true;
        }
    }

    class PhoneWatcher implements TextWatcher {

        final char concater = '-';

        EditText editText;

        public PhoneWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String data = s.toString();

            int length = data.length();

            if (length == 4) {
                String pre = data.substring(0, 3);

                int cursor = 0;

                if (!data.endsWith(concater + "")) {
                    char post = data.charAt(3);
                    setText(pre + concater + post);
                } else {
                    setText(pre);
                }
                editText.setSelection(editText.getText().toString().length());
            } else if (length > 4 && data.charAt(3) != concater) {
                data = data.replace(concater + "", "");

                String pre = data.substring(0, 3);

                String post = data.substring(3, data.length());
                int cursor = 0;
                setText(pre + concater + post);
                editText.setSelection(editText.getText().toString().length());
            }

        }

        private void setText(String text) {
            editText.removeTextChangedListener(this);
            editText.setText(text);

            editText.addTextChangedListener(this);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
