package com.pack.kippin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippin.kippin.R;
import com.kippin.payment.PaymentForm;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.CustomProgressDialog;
import com.kippin.utils.Singleton;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModelRegistration;
import com.kippin.webclient.model.TemplateData;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.ArrayList;
import java.util.Calendar;


import butterknife.ButterKnife;


/**
 * Created by dilip.singh on 1/22/2016.
 */
public class PaymentDetailsClass extends SuperActivity implements PaymentForm{
    private int mYear;
    private int mMonth;
    private int mDay;
    public static final int DATE_DIALOG_ID = 0;
  //  @Bind(R.id.expirationdate_text)
    TextView textViewExpirationDate;

    //@Bind(R.id.pay_btn)
    ImageView imageViewPayButton;

  //  @Bind(R.id.editText_cardnumber)
    EditText editTextCardNumber;
   // @Bind(R.id.editText_Security)
    EditText editTextSecurity;

 //   @Bind(R.id.edittext_namecard)
    EditText editTextNameofCard;
    DialogBox dialogBox;
    Dialog dialog;
    CustomProgressDialog customProgressDialog;
    /*
     * Change this to your publishable key.
     * You can get your key here: https://manage.stripe.com/account/apikeys
     * Now using indian publish key for testing
     */
    public static final String PUBLISHABLE_KEY = "pk_test_oMsVzjtEjIK36u2UFzwh8KkM";
    private String /*mLicense,*/mUserId/*,mCurrency*/;

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        textViewExpirationDate =(TextView)findViewById(R.id.expirationdate_text);
        imageViewPayButton =(ImageView)findViewById(R.id.pay_btn);
        editTextCardNumber =(EditText)findViewById(R.id.editText_cardnumber);
        editTextSecurity =(EditText)findViewById(R.id.editText_Security);
        editTextNameofCard =(EditText)findViewById(R.id.edittext_namecard);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.checkCrashTracker(PaymentDetailsClass.this);
        setContentView(R.layout.layout_payment_details);
        initialiseUI();
        setUpListeners();
        ButterKnife.bind(this);
        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        dialog = new Dialog(PaymentDetailsClass.this, R.style.NewDialog);

//        Bundle bundle = getIntent().getExtras();

        generateActionBar(R.string.payment_detail, true, false);

/*        if (bundle != null) {

            mUserId = bundle.getString("USERID");
            mLicense=bundle.getString("LICENSE");


            if (mLicense.equalsIgnoreCase("30"))
                mLicense = "1";
            else if(mLicense.equalsIgnoreCase("50"))
                mLicense="2";
            else if(mLicense.equalsIgnoreCase("100"))
                mLicense="3";

            mCurrency = bundle.getString("CURRENCY").toLowerCase();

        }else*/
        {
            mUserId = Singleton.getUser().getId();
        }



    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }

        return null;

    }
    //@OnClick(R.id.expirationdate_text)
    public void onExpirationClick(View v){

        showDialog(DATE_DIALOG_ID);
    }

   // @OnClick(R.id.pay_btn)
   /* public void onPayButtonClick(View v){
        if (editTextCardNumber.getText().toString().isEmpty()||textViewExpirationDate.getText().toString().isEmpty()||
                editTextSecurity.getText().toString().isEmpty()||editTextNameofCard.getText().toString().isEmpty())
            dialogBox = new DialogBox(getResources().getString(R.string.plz_fill_empty_mandatory),PaymentDetailsClass.this);
        else
        saveCreditCard(this);

    }*/
    /**
     * Updates the date we display in the TextView
     */
    private void updateDisplay() {
        textViewExpirationDate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("/")
                        .append(mYear).append(" "));
    }

    /**
     * The callback received when the user "sets" the date in the dialog
     */
    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        PaymentDetailsClass.this.overridePendingTransition(R.anim.push_out_to_left,
                R.anim.push_out_to_right);
    }

    @Override
    public String getCardNumber() {
        return this.editTextCardNumber.getText().toString();
    }

    @Override
    public Integer getExpMonth() {
        return mMonth+1;
    }

    @Override
    public Integer getExpYear() {
        return mYear;
    }

    @Override
    public String getCvc() {
        return this.editTextSecurity.getText().toString();
    }

    @Override
    public String getCurrency() {
        return "usd";
    }

    /**
     * Save Credit card value and send to Stripe payment gateway
     * @param form
     */
    public void saveCreditCard(PaymentForm form) {

        Card card = new Card(
                form.getCardNumber(),
                form.getExpMonth(),
                form.getExpYear(),
                form.getCvc());
        card.setCurrency(form.getCurrency());

        boolean validation = card.validateCard();
        if (validation) {
            customProgressDialog = new CustomProgressDialog(PaymentDetailsClass.this,dialog);
            new Stripe().createToken(
                    card,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {

                        @Override
                        public void onSuccess(final Token token) {
                        dialog.dismiss();

                            sendPaymentDataApi(token.getId());

                        }


                        @Override
                        public void onError(Exception error) {
                            dialogBox = new DialogBox(error.getLocalizedMessage(),PaymentDetailsClass.this);

                            dialog.dismiss();
                        }



                    });
        } else if (!card.validateNumber()) {
            dialogBox = new DialogBox(getResources().getString(R.string.card_number_invalide),PaymentDetailsClass.this);
        } else if (!card.validateExpiryDate()) {
            dialogBox = new DialogBox(getResources().getString(R.string.expiration_invalide),PaymentDetailsClass.this);
        } else if (!card.validateCVC()) {
            dialogBox = new DialogBox(getResources().getString(R.string.cvc_code_invalide),PaymentDetailsClass.this);
        } else {
            dialogBox = new DialogBox(getResources().getString(R.string.card_detail_invalide),PaymentDetailsClass.this);
        }
    }

    /**
     * call api for send payment data
     * @param tokenId
     */
    public void sendPaymentDataApi(String tokenId){
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.aClass = ModelRegistration.class;
        wsTemplate.context = PaymentDetailsClass.this;
        wsTemplate .message_id = R.string.app_name;
        wsTemplate.methods = WSMethods.POST;
        wsTemplate.requestCode = 1001;
        wsTemplate.url = Url.getPaymentDataUrl(mUserId,tokenId/*,mLicense*/);

        wsTemplate.wsInterface = new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {

                switch (requestCode){
                    case 1001:

                      final  ModelRegistration modelRegistration = data.getData(ModelRegistration.class);

                        if (modelRegistration.getResponseCode().equalsIgnoreCase("1")){

//                            dialogBox = new DialogBox(getResources().getString(R.string.successfully_registerd),PaymentDetailsClass.this);

                            ArrayListPost templatePosts = new ArrayListPost();

                            templatePosts.add("UserId",Singleton.getUser().getId());
                            templatePosts.add("CardNumber",editTextCardNumber.getText().toString());
                            templatePosts.add("CVV",editTextSecurity.getText().toString());
                            templatePosts.add("ExpiryMonth",(mMonth + 1)+"");
                            templatePosts.add("ExpiryYear",(mYear)+"");
                            templatePosts.add("FirstName",editTextNameofCard.getText().toString());
                            templatePosts.add("LastName","");



                            WSUtils.hitService(PaymentDetailsClass.this, Url.URL_ADD_CARD_DETAILS, false, templatePosts, WSMethods.POST, TemplateData.class, new WSInterface() {
                                @Override
                                public void onResult(int requestCode, TemplateData data) {

                                    new DialogBox(PaymentDetailsClass.this, modelRegistration.getResponseMessage(), new DialogBoxListener() {
                                        @Override
                                        public void onDialogOkPressed() {

                                            if(getIntent().getExtras().getBoolean("REDIRECT_TO_LOGIN")){
                                                Utility.logout(PaymentDetailsClass.this,false);
                                            }else PaymentDetailsClass.this.finish();
                                        }
                                    });
                                }
                            });


                        }
//                        else {
//
//                            dialogBox = new DialogBox(modelRegistration.getResponseMessage(),PaymentDetailsClass.this);
//                        }



                        break;


                }
            }
        };
        new WSHandler(wsTemplate).execute();

    }

    @Override
    public void setUpListeners() {
        imageViewPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCardNumber.getText().toString().isEmpty()||textViewExpirationDate.getText().toString().isEmpty()||
                        editTextSecurity.getText().toString().isEmpty()||editTextNameofCard.getText().toString().isEmpty())
                    dialogBox = new DialogBox(getResources().getString(R.string.plz_fill_empty_mandatory_),PaymentDetailsClass.this);
                else
                    saveCreditCard(PaymentDetailsClass.this);
            }
        });

        textViewExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }
}
