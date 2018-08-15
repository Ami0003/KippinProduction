package com.kippinretail.KippinInvoice;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippin.performbook.adapter.CurrencyAdapter;
import com.kippin.utils.Url;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.model.ModelCurrency;
import com.kippin.webclient.model.ModelCurrencys;
import com.kippin.webclient.model.TemplateData;
import com.kippinretail.ActivityTermsAndConditions;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.ApplicationuUlity.PhoneWatcher;
import com.kippinretail.ApplicationuUlity.Validation;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.UploadImageDialog;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.KippinInvoice.InvoiceAdapter.GoodsAdapter;
import com.kippinretail.KippinInvoice.ModalInvoice.ModalForIndutry;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 11/11/2016.
 */

public class InvoiceRegistrationInvoice extends SuperActivity implements View.OnClickListener {

    public ArrayList<ModelCurrency> arrayListCurrencies;
    public ArrayList<ModalForIndutry> arrayListForIndustryModal;
    ImageView ivLogo;
    EditText etUsername, etCompanyname, etContactPerson, etMobileNumber, etBusinessNumber, etEmail, etPassword, etComfirmPassword, etWebsite;
    Button btn_Submit;
    ImageView ivArrowCorporationAddress, ivArrowShippingAddress;
    RelativeLayout rlAddButon;
    ArrayList<String> listForEmails = new ArrayList();
    boolean dialogType;
    RelativeLayout rlCorporationAddress, rlShippingAddress;
    TextView tvForCorporationAddress, tvForShippingAddress;
    // Adapter
    CurrencyAdapter currencyAdapter;
    // Spinner
    Spinner spinnerCurrency, spinner_TypesofGoods;
    //ModelCurrency modelCurrency;
    PhoneWatcher phoneWatcher = null;
    CheckBox checkBox;
    // Strings
    private String CorporateAptNo;
    private String CorporateHouseNo;
    private String CorporateStreet;
    private String CorporateCity;
    private String CorporateState;
    private String CorporatePostalCode;
    private String ShippingAptNo;
    private String ShippingHouseNo;
    private String ShippingStreet;
    private String ShippingCity;
    private String ShippingState;
    private String ShippingPostalCode;
    private String encodedString;
    private String emailCC = "";
    private TextView textViewCurrency, textview_term_condition;
    private String mCurrency;
    private String registrationType = "1";
    private TextView tvTypesofGoods;
    private int firstTime;
    private List<ModalForIndutry> modalForIndustry;
    private GoodsAdapter goodsAdapter;
    private String mTypesAndGoods;
    private ScrollView topLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_voice_registration);
        Initlization();
    }

    private void Initlization() {
        String s = getString(R.string.invoice_registartion);
        generateRightText1(s);
        topLayout = (ScrollView) findViewById(R.id.topLayout);
        //EditText
        etUsername = (EditText) findViewById(R.id.etUsername);
        etCompanyname = (EditText) findViewById(R.id.etCompanyname);
        etContactPerson = (EditText) findViewById(R.id.etContactPerson);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        // etTypesofGoods = (EditText) findViewById(R.id.etTypesofGoods);
        etBusinessNumber = (EditText) findViewById(R.id.etBusinessNumber);
        etWebsite = (EditText) findViewById(R.id.etWebsite);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etComfirmPassword = (EditText) findViewById(R.id.etComfirmPassword);
        // Spinner
        spinnerCurrency = (Spinner) findViewById(R.id.spinner_currency);
        spinner_TypesofGoods = (Spinner) findViewById(R.id.spinner_TypesofGoods);
        //Button
        btn_Submit = (Button) findViewById(R.id.btn_Submit);
        btn_Submit.setOnClickListener(this);

        // ImageView
        ivArrowCorporationAddress = (ImageView) findViewById(R.id.ivArrowCorporationAddress);
        ivArrowShippingAddress = (ImageView) findViewById(R.id.ivArrowShippingAddress);

        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivLogo.setOnClickListener(this);

        // CheckBox
        checkBox = (CheckBox) findViewById(R.id.checkbox);

        //TextViewsz
        tvForCorporationAddress = (TextView) findViewById(R.id.etCorprationAddress);
        tvForShippingAddress = (TextView) findViewById(R.id.etShippingAddress);

        //tvForCorporationAddress.setMovementMethod(ScrollingMovementMethod.getInstance());
        //  tvForShippingAddress.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvForCorporationAddress.setMovementMethod(new ScrollingMovementMethod());
        tvForShippingAddress.setMovementMethod(new ScrollingMovementMethod());


        topLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                tvForCorporationAddress.getParent().requestDisallowInterceptTouchEvent(false);
                tvForShippingAddress.getParent().requestDisallowInterceptTouchEvent(false);

                return false;
            }
        });

        tvForCorporationAddress.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                tvForCorporationAddress.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        tvForShippingAddress.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                tvForShippingAddress.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        textViewCurrency = (TextView) findViewById(R.id.currency);
        textViewCurrency.setOnClickListener(this);
        textview_term_condition = (TextView) findViewById(R.id.textview_term_condition);
        textview_term_condition.setOnClickListener(this);


        tvTypesofGoods = (TextView) findViewById(R.id.tvTypesofGoods);
        tvTypesofGoods.setOnClickListener(this);
        // Relativelayout
        rlAddButon = (RelativeLayout) findViewById(R.id.rlAddButon);
        rlAddButon.setOnClickListener(this);

        rlCorporationAddress = (RelativeLayout) findViewById(R.id.rlCorporationAddress);
        rlShippingAddress = (RelativeLayout) findViewById(R.id.rlShippingAddress);
        rlCorporationAddress.setOnClickListener(this);
        rlShippingAddress.setOnClickListener(this);

        phoneWatcher = new PhoneWatcher(etMobileNumber);
        etMobileNumber.addTextChangedListener(phoneWatcher);

        checkCurrencyData();

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    textViewCurrency.setTextColor(Color.parseColor("#A1A1A1"));

                } else {
                    textViewCurrency.setTextColor(Color.BLACK);
                    textViewCurrency.setText(arrayListCurrencies.get(position).getSymbolCode());
                    // mCurrency = arrayListCurrencies.get(position).getId();
                    mCurrency = arrayListCurrencies.get(position).getSymbolCode();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_TypesofGoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    tvTypesofGoods.setTextColor(Color.parseColor("#A1A1A1"));
                } else {
                    tvTypesofGoods.setTextColor(Color.BLACK);
                    tvTypesofGoods.setText(modalForIndustry.get(position).getIndustryName());
                    mTypesAndGoods = "" + modalForIndustry.get(position).getIndustryId();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Submit:
                if (etUsername.getText().toString().length() != 0 && etCompanyname.getText().toString().length() != 0 && etBusinessNumber.getText().toString().length() != 0
                        && etEmail.getText().toString().length() != 0) {
                    if (CorporateStreet != null && CorporateCity != null && CorporateState != null && CorporatePostalCode != null) {
                        if (ShippingStreet != null && ShippingCity != null && ShippingState != null && ShippingPostalCode != null) {
                            if (mCurrency != null) {
                                if (emailValidator1(etEmail.getText().toString())) {
                                    if (!etPassword.getText().toString().equals("") || etPassword.getText().toString().length() > 6) {
                                        if (!etComfirmPassword.getText().toString().equals("") || !etComfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
                                            if (checkBox.isChecked()) {
                                                callInvoiceRegistration();
                                            } else {
                                                CommonDialog.With(InvoiceRegistrationInvoice.this).Show("Please read and accept Terms & Conditions");
                                            }
                                        } else {
                                            CommonDialog.With(InvoiceRegistrationInvoice.this).Show("Password mismatch");
                                        }
                                    } else {
                                        CommonDialog.With(InvoiceRegistrationInvoice.this).Show("Please enter password with minimum length 6");
                                    }
                                } else {
                                    CommonDialog.With(InvoiceRegistrationInvoice.this).Show(getResources().getString(R.string.plz_enter_valide_email_id));
                                }
                            } else {
                                CommonDialog.With(InvoiceRegistrationInvoice.this).Show("Please select country");
                            }
                        } else {
                            CommonDialog.With(InvoiceRegistrationInvoice.this).Show("Please fill Corporate address all fields");
                        }
                    } else {
                        CommonDialog.With(InvoiceRegistrationInvoice.this).Show("Please fill Shipping address all fields");
                    }
                } else {
                    CommonDialog.With(InvoiceRegistrationInvoice.this).Show(getResources().getString(R.string.plz_fill_empty_mandatory_));
                }
                break;
            case R.id.rlCorporationAddress:
                dialogType = true;
                dialogForAddress();
                break;
            case R.id.rlShippingAddress:
                dialogType = false;
                dialogForAddress();
                break;
            case R.id.rlAddButon:
                dialogToAddEmail();
                break;
            case R.id.ivLogo:
                uploadImage();
                break;
            case R.id.currency:
                spinnerCurrency.performClick();
                break;
            case R.id.tvTypesofGoods:
                spinner_TypesofGoods.performClick();
                break;
            case R.id.textview_term_condition:
                Intent in = new Intent();
                in.setClass(InvoiceRegistrationInvoice.this, ActivityTermsAndConditions.class);
                startActivity(in);
                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }
    }

    private void uploadImage() {
        UploadImageDialog.showUploadImageDialog(InvoiceRegistrationInvoice.this, getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CommonUtility.REQUEST_CODE_GALLERY:
                try {
                    String imgPath = null;
                    if (data != null) {
                        imgPath = data.getStringExtra("imagePath");
                    }
                    if (imgPath != null) {
                        CommonUtility.VoucherBitMap = BitmapFactory.decodeFile(imgPath);
                        encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                        ivLogo.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath, CommonUtility.dpToPx(this, 130),
                                CommonUtility.dpToPx(this, 130)));
                    }
                } catch (Exception e) {

                }
                break;
            case CommonUtility.REQUEST_CODE_TAKE_PICTURE:
                try {
                    //     getContentResolver().notifyChange(UploadImageDialog.mImageUri, null);
                    //  CommonUtility.VoucherBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), UploadImageDialog.mImageUri);
                    CommonUtility.VoucherBitMap = getThumbnail(UploadImageDialog.mImageUri);
                    encodedString = CommonUtility.base64Image = CommonUtility.encodeTobase64(CommonUtility.VoucherBitMap);
                   /* ExifInterface exifInterface = new ExifInterface(UploadImageDialog.mImageUri.getPath());
                    int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                    float angle = 360;
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            angle = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            angle = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            angle = 270;
                            break;
                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            break;
                    }*/
                    float angle = 90;
                    Bitmap oriented = CommonUtility.fixOrientation(CommonUtility.VoucherBitMap, angle);
                    String orientedImagePath = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, oriented));
                    ivLogo.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(orientedImagePath, CommonUtility.dpToPx(this, 130),
                            CommonUtility.dpToPx(this, 130)));
                    UploadImageDialog.mImageUri = null;
                } catch (Exception ex) {

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        //double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 3;
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }


    private void dialogToAddEmail() {

        try {


            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_for_email);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            final EditText etEmail = (EditText) dialog.findViewById(R.id.etEmail);
            Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);

            ImageView ivForCross = (ImageView) dialog.findViewById(R.id.ivForCross);
            final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.row_list_item, listForEmails);
            ListView listView = (ListView) dialog.findViewById(R.id.lvForEmails);
            listView.setAdapter(adapter);


            ivForCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Validation.isEmailAddress(etEmail, true)) {
                        if (!listForEmails.contains(etEmail.getText().toString())) {
                            listForEmails.add(etEmail.getText().toString());
                            adapter.notifyDataSetChanged();
                            etEmail.setText("");
                        } else {
                            Toast.makeText(InvoiceRegistrationInvoice.this, "Email already exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dialogForAddress() {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_for_address);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            final EditText etAptNumber = (EditText) dialog.findViewById(R.id.etAptNumber);
            final EditText etHouseNumber = (EditText) dialog.findViewById(R.id.etHouseNumber);
            final EditText etStreet = (EditText) dialog.findViewById(R.id.etStreet);
            final EditText etCity = (EditText) dialog.findViewById(R.id.etCity);
            final EditText etState = (EditText) dialog.findViewById(R.id.etState);
            final EditText etPostalCode = (EditText) dialog.findViewById(R.id.etPostalCode);

            Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
            ImageView ivForCross = (ImageView) dialog.findViewById(R.id.ivForCross);

            if (dialogType) {
                etAptNumber.setText(CorporateAptNo);
                etHouseNumber.setText(CorporateHouseNo);
                etStreet.setText(CorporateStreet);
                etCity.setText(CorporateCity);
                etState.setText(CorporateState);
                etPostalCode.setText(CorporatePostalCode);
            } else {
                etAptNumber.setText(ShippingAptNo);
                etHouseNumber.setText(ShippingHouseNo);
                etStreet.setText(ShippingStreet);
                etCity.setText(ShippingCity);
                etState.setText(ShippingState);
                etPostalCode.setText(ShippingPostalCode);
            }

            ivForCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (dialogType) {
                        CorporateAptNo = etAptNumber.getText().toString();
                        CorporateHouseNo = etHouseNumber.getText().toString();
                        CorporateStreet = etStreet.getText().toString();
                        CorporateCity = etCity.getText().toString();
                        CorporateState = etState.getText().toString();
                        CorporatePostalCode = etPostalCode.getText().toString();

                        if (CorporateStreet.toString().length() != 0 && CorporateCity.toString().length() != 0
                                && CorporateState.toString().length() != 0 && CorporatePostalCode.toString().length() != 0) {
                            if (CorporatePostalCode.toString().length() < 9 && CorporatePostalCode.toString().length() > 5) {
                                tvForCorporationAddress.setVisibility(View.VISIBLE);
                                tvForShippingAddress.setVisibility(View.VISIBLE);
                                String add = CorporateAptNo + "-" + CorporateHouseNo;
                                if (CorporateAptNo.toString().length() == 0)
                                    add = add.substring(1);
                                tvForCorporationAddress.setText("Corporation Address:" + "\n" + add + "\n" + CorporateStreet + "," + CorporateCity
                                        + "\n" + CorporateState + "," + CorporatePostalCode);
                                if (firstTime == 0) {
                                    firstTime = 1;
                                    ShippingAptNo = etAptNumber.getText().toString();
                                    ShippingHouseNo = etHouseNumber.getText().toString();
                                    ShippingStreet = etStreet.getText().toString();
                                    ShippingCity = etCity.getText().toString();
                                    ShippingState = etState.getText().toString();
                                    ShippingPostalCode = etPostalCode.getText().toString();
                                    tvForShippingAddress.setText("Shipping Address:" + "\n" + add + "\n" + ShippingStreet + "," + ShippingCity
                                            + "\n" + ShippingState + "," + ShippingPostalCode);
                                }
                                dialog.dismiss();
                            } else {
                                CommonDialog.With(InvoiceRegistrationInvoice.this).Show(getResources().getString(R.string.postal_code_must_be));
                            }
                        } else {
                            CommonDialog.With(InvoiceRegistrationInvoice.this).Show(getResources().getString(R.string.empty_fields));
                        }
                    } else {
                        ShippingAptNo = etAptNumber.getText().toString();
                        ShippingHouseNo = etHouseNumber.getText().toString();
                        ShippingStreet = etStreet.getText().toString();
                        ShippingCity = etCity.getText().toString();
                        ShippingState = etState.getText().toString();
                        ShippingPostalCode = etPostalCode.getText().toString();

                        if (ShippingStreet.toString().length() != 0 && ShippingCity.toString().length() != 0
                                && ShippingState.toString().length() != 0 && ShippingPostalCode.toString().length() != 0) {
                            if (ShippingPostalCode.toString().length() == 8) {
                                tvForShippingAddress.setVisibility(View.VISIBLE);
                                String add = ShippingAptNo + "-" + ShippingHouseNo;
                                if (ShippingAptNo.toString().length() == 0)
                                    add = add.substring(1);
                                tvForShippingAddress.setText("Shipping Address:" + "\n" + add + "\n" + ShippingStreet + "," + ShippingCity
                                        + "\n" + ShippingState + "," + ShippingPostalCode);
                                dialog.dismiss();
                            } else {
                                CommonDialog.With(InvoiceRegistrationInvoice.this).Show(getResources().getString(R.string.postal_code_must_be));
                            }

                        } else {
                            CommonDialog.With(InvoiceRegistrationInvoice.this).Show(getResources().getString(R.string.empty_fields));
                        }
                    }
                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void checkCurrencyData() {
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.wsInterface = new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {
                arrayListCurrencies = ((ModelCurrencys) data.getData(ModelCurrencys.class)).modelCurrencies;
                //modelCurrency = new ModelCurrency();
                //  modelCurrency.setId("0");
                //  modelCurrency.setCurrencyType("Trading Currency");
                //  arrayListCurrencies.add(0, modelCurrency);
                currencyAdapter = new CurrencyAdapter(InvoiceRegistrationInvoice.this, com.kippin.kippin.R.layout.layout_spinner_textview, arrayListCurrencies);
                spinnerCurrency.setAdapter(currencyAdapter);

                checkTypesAndGoods();

            }
        };
        wsTemplate.url = Url.URL_CURRENCY_INVOICE;
        wsTemplate.aClass = ModelCurrencys.class;
        wsTemplate.methods = WSMethods.GET;
        wsTemplate.context = this;
        new WSHandler(wsTemplate).execute();

    }


    private void checkTypesAndGoods() {
        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().getIndustryList(new Callback<JsonElement>() {
                                                                     @Override
                                                                     public void success(JsonElement jsonElement, Response response) {
                                                                         LoadingBox.dismissLoadingDialog();
                                                                         Log.e("RestClient", jsonElement.toString() + " \n " + response.getUrl());
                                                                         Gson gson = new Gson();
                                                                         modalForIndustry = gson.fromJson(jsonElement.toString(), new TypeToken<List<ModalForIndutry>>() {
                                                                         }.getType());
                                                                        /* ModalForIndutry modalForIndustry1 = new ModalForIndutry();
                                                                         modalForIndustry1.setIndustryName("Types of goods/service offered");
                                                                         modalForIndustry1.setIndustryId("0");
                                                                         modalForIndustry1.setUserId(0);
                                                                         modalForIndustry.add(0, modalForIndustry1);*/
                                                                         goodsAdapter = new GoodsAdapter(InvoiceRegistrationInvoice.this, modalForIndustry, 0);
                                                                         spinner_TypesofGoods.setAdapter(goodsAdapter);

                                                                     }

                                                                     @Override
                                                                     public void failure(RetrofitError error) {
                                                                         LoadingBox.dismissLoadingDialog();
                                                                         Log.e("RestClient", error.getUrl() + "");
                                                                     }
                                                                 }

        );
    }

    private void callInvoiceRegistration() {
        for (int i = 0; i < listForEmails.size(); i++) {
            if (emailCC.equals("")) {
                emailCC = listForEmails.get(i);
            } else {
                emailCC = emailCC + "," + listForEmails.get(i);
            }
        }
        LoadingBox.showLoadingDialog(InvoiceRegistrationInvoice.this, "");
        RestClient.getApiFinanceServiceForPojo().InvoiceRegistration1(etUsername.getText().toString(), encodedString, etCompanyname.getText().toString(), etContactPerson.getText().toString(),
                etMobileNumber.getText().toString(), CorporateAptNo, CorporateHouseNo, CorporateStreet, CorporateCity, CorporateState, CorporatePostalCode,
                ShippingAptNo, ShippingHouseNo, ShippingStreet, ShippingCity, ShippingState, ShippingPostalCode,
                mTypesAndGoods, etBusinessNumber.getText().toString(), mCurrency, etEmail.getText().toString(),
                emailCC, etWebsite.getText().toString(), registrationType, etPassword.getText().toString(), getCallback(new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Tag", "Request data " + new Gson().toJson(jsonElement));
                        // {"ResponseCode":1,"ResponseMessage":"Please check your mail for email confirmation.","UserId":3}
                        try {
                            JSONObject jsonObject = new JSONObject(jsonElement.toString());
                            String ResponseMessage = jsonObject.getString("ResponseMessage");
                            String ResponseCode = jsonObject.getString("ResponseCode");
                            String UserId = jsonObject.getString("UserId");

                            if (ResponseCode.equals("1")) {
                                CommonDialog.showDialog2Button(InvoiceRegistrationInvoice.this, ResponseMessage, new OnDialogDismissListener() {
                                    @Override
                                    public void onDialogDismiss() {
                                        Utils.logoutInvoice(InvoiceRegistrationInvoice.this);
                                    }
                                });

                            } else {
                                CommonDialog.With(InvoiceRegistrationInvoice.this).Show(ResponseMessage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("Failure ", " = " + error.getMessage());
                        LoadingBox.dismissLoadingDialog();
                        ErrorCodes.checkCode(InvoiceRegistrationInvoice.this, error);
                    }
                }));

    }

}
