package com.pack.kippin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.Utility;



/**
 * Created by dilip.singh on 1/19/2016.
 */
public class RegisterScreen extends SuperActivity {

   // @Bind(R.id.checkbox)
    CheckBox checkBox;
   // @Bind(R.id.textview_term_condition)
    TextView textViewTermCondition;
  //  @Bind(R.id.register_btn)
    ImageView imageViewRegisterBtn;
   // @Bind(R.id.editText_username)
    EditText editTextUsername;

    //@Bind(R.id.editText_email_address)
    EditText editTextEmailaddress;

  //  @Bind(R.id.editText_password)
    EditText editTextPassword;

   // @Bind(R.id.editText_confirmpassword)
    EditText editTextConfirmPassword;

    DialogBox dialogBox;

  //  @Bind(R.id.already_already_registered)
    ImageView getImageViewRegisterBtn;
    Bundle nBundle;

  //  @Bind(R.id.rgPaymentOption)
    RadioGroup rgPayment;
    ImageView ivBack;
    @Override
    public void initialiseUI() {
        super.initialiseUI();
        checkBox =(CheckBox)findViewById(R.id.checkbox);
        textViewTermCondition =(TextView)findViewById(R.id.textview_term_condition);
        imageViewRegisterBtn =(ImageView)findViewById(R.id.register_btn);
        editTextUsername =(EditText)findViewById(R.id.editText_username);

        editTextEmailaddress =(EditText)findViewById(R.id.editText_email_address);
        editTextPassword =(EditText)findViewById(R.id.editText_password);
        editTextConfirmPassword =(EditText)findViewById(R.id.editText_confirmpassword);
        getImageViewRegisterBtn =(ImageView)findViewById(R.id.already_already_registered);
        rgPayment =(RadioGroup)findViewById(R.id.rgPaymentOption);
        ivBack = (ImageView)findViewById(R.id.ivBack1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.checkCrashTracker(RegisterScreen.this);
        setContentView(R.layout.registration_screen);
        initialiseUI();
        setUpListeners();
        //  ButterKnife.bind(this);


       /* String text = "<font color=#000000>I have read &amp; agree to the " +
                "</font> <font color=#6495ed>Terms &amp; Conditions</font>";
        checkBox.setText(Html.fromHtml(text));*/




    }

   /*// @OnClick(R.id.register_btn)
    public void onRegisterClick(View v){
        if (editTextUsername.getText().toString().isEmpty())
        {
            dialogBox  = new DialogBox(getResources().getString(R.string.plz_enter_username),RegisterScreen.this);
        }
        else if(editTextEmailaddress.getText().toString().isEmpty()){
            dialogBox  = new DialogBox(getResources().getString(R.string.Plz_enter_email_address),RegisterScreen.this);
        }
        else if(!Utility.isValidEmail(editTextEmailaddress.getText().toString())){
            dialogBox  = new DialogBox(getResources().getString(R.string.plz_enter_valide_email_address),RegisterScreen.this);
        }

        else if(editTextPassword.getText().toString().isEmpty()){
            dialogBox  = new DialogBox(getResources().getString(R.string.plz_enter_pasword),RegisterScreen.this);
        }
        else if(editTextPassword.getText().toString().length()<6){

            dialogBox = new DialogBox(getResources().getString(R.string.plz_enter_minimum_and_maximum_password_lenth),RegisterScreen.this);

        }
        else if(editTextConfirmPassword.getText().toString().isEmpty()){
            dialogBox  = new DialogBox(getResources().getString(R.string.plz_enter_confirm_password),RegisterScreen.this);
        }
        else if(!editTextPassword.getText().toString().equalsIgnoreCase(editTextConfirmPassword.getText().toString())){

            dialogBox = new DialogBox(getResources().getString(R.string.password_confirm_not_same),RegisterScreen.this);

        }
        else if(!checkBox.isChecked()){

                dialogBox = new DialogBox(getResources().getString(R.string.terms_and_condition),RegisterScreen.this);

        }
        else {

            Intent intent = new Intent(RegisterScreen.this, PrivateKeyClass.class);
            nBundle = new Bundle();
            nBundle.putString("USERNAME", editTextUsername.getText().toString());
            nBundle.putString("EMAILADDRESS",editTextEmailaddress.getText().toString());
            nBundle.putString("PASSWORD",editTextPassword.getText().toString());
            nBundle.putString("PAID",(rgPayment.getCheckedRadioButtonId()==R.id.rbPaymentPaid)+"");

            intent.putExtras(nBundle);
            startActivity(intent);
            overridePendingTransition(R.anim.push_in_to_left,
                    R.anim.push_in_to_right);

        }

    }*/

    /*//@OnClick(R.id.already_already_registered)
    public void onAllreadyRegistered(View v){

        Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_out_to_left,
                R.anim.push_out_to_right);
        finish();


    }*/

   /* //@OnClick(R.id.textview_term_condition)
    public void onTermCondition(View v){
        Intent intent = new Intent(RegisterScreen.this, TermsAndCondition.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_in_to_left,
                R.anim.push_in_to_right);

    }*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    public void setUpListeners() {
        imageViewRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUsername.getText().toString().isEmpty())
                {
                    dialogBox  = new DialogBox(getResources().getString(R.string.plz_fill_empty_field),RegisterScreen.this);
                }
                else if(editTextEmailaddress.getText().toString().isEmpty()){
                    dialogBox  = new DialogBox(getResources().getString(R.string.plz_fill_empty_field),RegisterScreen.this);
                }
                else if(!Utility.isValidEmail(editTextEmailaddress.getText().toString())){
                    dialogBox  = new DialogBox(getResources().getString(R.string.plz_enter_valide_email_address),RegisterScreen.this);
                }

                else if(editTextPassword.getText().toString().isEmpty()){
                    dialogBox  = new DialogBox(getResources().getString(R.string.plz_fill_empty_field),RegisterScreen.this);
                }
                else if(editTextPassword.getText().toString().length()<6){

                    dialogBox = new DialogBox(getResources().getString(R.string.plz_enter_minimum_and_maximum_password_lenth),RegisterScreen.this);

                }
                else if(editTextConfirmPassword.getText().toString().isEmpty()){
                    dialogBox  = new DialogBox(getResources().getString(R.string.plz_fill_empty_field),RegisterScreen.this);
                }
                else if(!editTextPassword.getText().toString().equalsIgnoreCase(editTextConfirmPassword.getText().toString())){

                    dialogBox = new DialogBox(getResources().getString(R.string.password_confirm_not_same),RegisterScreen.this);

                }
                else if(!checkBox.isChecked()){

                    dialogBox = new DialogBox(getResources().getString(R.string.terms_and_condition),RegisterScreen.this);

                }
                else {

                    Intent intent = new Intent(RegisterScreen.this, PrivateKeyClass.class);
                    nBundle = new Bundle();
                    nBundle.putString("USERNAME", editTextUsername.getText().toString());
                    nBundle.putString("EMAILADDRESS",editTextEmailaddress.getText().toString());
                    nBundle.putString("PASSWORD",editTextPassword.getText().toString());
                    nBundle.putString("PAID",(rgPayment.getCheckedRadioButtonId()==R.id.rbPaymentPaid)+"");

                    intent.putExtras(nBundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_in_to_left,
                            R.anim.push_in_to_right);

                }
            }
        });

        getImageViewRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_out_to_left,
                        R.anim.push_out_to_right);
                finish();

            }
        });

        textViewTermCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterScreen.this, TermsAndCondition.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_in_to_left,
                        R.anim.push_in_to_right);

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
            }
        });
    }
}
