package com.pack.kippin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asynctask.OkHttpHandlerAsyncTask;
import com.kippin.app.Kippin;
import com.kippin.kippin.R;
import com.kippin.storage.SharedPrefWriter;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.model.ModelLogin;

import org.apache.poi.ss.usermodel.DataValidationConstraint;

import java.util.concurrent.locks.ReentrantLock;

import javax.xml.validation.Validator;



/**
 * Created by dilip.singh on 1/19/2016.
 */
public class LoginScreen extends Activity{


    //@Bind(R.id.linear_email)
    LinearLayout emailLinear;

  //  @Bind(R.id.linear_passowrd)
    LinearLayout passwordLinear;

   // @Bind(R.id.editText_email)
    EditText editTextEmail;

   // @Bind(R.id.editText_password)
    EditText editTextPassword;

    //@Bind(R.id.msg_image)
    ImageView imageViewMessage,new_here_register;

    //@Bind(R.id.lock_image)
    ImageView imageViewLock;

  //  @Bind(R.id.login_btn)
    ImageView loginButton;
 //   @Bind(R.id.forgot_password)
    TextView textViewForgetPsrd;
    DialogBox dialogBox;
    SharedPrefWriter  sharedPrefWriter;
    ImageView ivBack;
    RelativeLayout lay_back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        initialiseUI();
       setUpListeners();

     //  generateActionBar(R.string.statement_details1, true, false);
    }


    public void initialiseUI() {
        emailLinear =(LinearLayout)findViewById(R.id.linear_email);
        passwordLinear =(LinearLayout)findViewById(R.id.linear_passowrd);
        editTextEmail =(EditText)findViewById(R.id.editText_email);
        editTextPassword =(EditText)findViewById(R.id.editText_password);
        imageViewMessage =(ImageView)findViewById(R.id.msg_image);
        imageViewLock =(ImageView)findViewById(R.id.lock_image);
        new_here_register = (ImageView)findViewById(R.id.new_here_register);
        loginButton =(ImageView)findViewById(R.id.login_btn);
        textViewForgetPsrd =(TextView)findViewById(R.id.forgot_password);
        ivBack = (ImageView)findViewById(R.id.ivBack1);
        lay_back = (RelativeLayout)findViewById(R.id.lay_back);
       // ButterKnife.bind(this);
    }

  //  @OnClick(R.id.new_here_register)
    /**
     * Method use for Call RegisterScreen class
     */
    public void callRegister(View v){

        Singleton.putUser(new ModelLogin());
       Utility.startActivity(LoginScreen.this, AccountWithWallet.class, true);

    }

   // @OnClick(R.id.login_btn)
    /**
     * Method use for login call
     */
    public void loginCall(View v){
        if (editTextEmail.getText().toString().isEmpty()||editTextPassword.getText().toString().isEmpty() ){
            dialogBox = new DialogBox(getResources().getString(R.string.plz_fill_empty_field),LoginScreen.this);
        }
       else {
            Utility.login(LoginScreen.this, editTextEmail.getText().toString(), editTextPassword.getText().toString(),true,false);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //@OnClick(R.id.forgot_password)
    public void forgetCallDialog(){

        dialogBox=new DialogBox(LoginScreen.this);

    }



    public void setUpListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextEmail.getText().toString().isEmpty()||editTextPassword.getText().toString().isEmpty() ){
                    dialogBox = new DialogBox(getResources().getString(R.string.plz_fill_empty_field),LoginScreen.this);
                }
                else {

                    Utility.login(LoginScreen.this, editTextEmail.getText().toString(), editTextPassword.getText().toString(),true,false);


                   /* editTextEmail.setText("");
                    editTextPassword.setText("");*/
                }
            }
        });
        textViewForgetPsrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox=new DialogBox(LoginScreen.this);
            }
        });
        new_here_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Singleton.putUser(new ModelLogin());
                Utility.startActivity(LoginScreen.this, RegisterScreen.class, true);
            }
        });
        lay_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
