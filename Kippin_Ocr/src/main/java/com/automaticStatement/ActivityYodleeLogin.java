package com.automaticStatement;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.TemplateData;



public class ActivityYodleeLogin extends SuperActivity implements View.OnClickListener{

    public static final String ADD_ACCOUNT ="ADD_ACCOUNT" ;
    public static final String OPERATION ="OPERATION" ;
  //  @Bind(R.id.etAccUsername)
    EditText etAccUsername;

 //   @Bind(R.id.etPassword)
    EditText etPassword;

 //   @Bind(R.id.tvAdd)
    TextView tvAdd;


    private enum Operation{ LOGIN , ADD_ACC}

    private Operation operation = Operation.LOGIN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yodlee_login);
        initialiseUI();

        if(getIntent().getStringExtra(OPERATION)!=null){
            operation = Operation.ADD_ACC;
            generateActionBar(R.string.add_conn , true , true);
        }else {
            tvAdd.setText("Login");
            generateActionBar(R.string.login, true, true);
        }

        setUpListeners();
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        etAccUsername = (EditText) findViewById(R.id.etAccUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvAdd = (TextView) findViewById(R.id.tvAdd);

    }

    @Override
    public void setUpListeners() {
        tvAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tvAdd){
            if(getIntent().getStringExtra(OPERATION)!=null)
                addAccount();
            else login();
        }
        /*switch (v.getId()){

            case R.id.tvAdd:
                if(getIntent().getStringExtra(OPERATION)!=null)
                   addAccount();
                else login();
                break;

        }*/
    }

    private void addAccount() {

//        UserId,AddAccountUserName,AddAccountPassword

        ArrayListPost ArrayListPost = new ArrayListPost();
        ArrayListPost.add("UserId" , Singleton.getUser().getId()) ;
        ArrayListPost.add("AddAccountUserName" , getText(etAccUsername)) ;
        ArrayListPost.add("AddAccountPassword" , getText(etPassword) ) ;
        ArrayListPost.add("BankId" , getIntent().getStringExtra("BankId")) ;

        WSUtils.hitService(this, Url.getYodleeAddAccApi( ), ArrayListPost, WSMethods.POST,
                TemplateData.class, new WSInterface() {
                    @Override
                    public void onResult(int requestCode, TemplateData data) {

                        if (data.data.toLowerCase().equalsIgnoreCase("REFRESH_COMPLETED") || data.data.toLowerCase().equalsIgnoreCase("\"REFRESH_COMPLETED\"") ) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            new DialogBox(ActivityYodleeLogin.this , data.data, (DialogBoxListener)null) ;
                        }

                    }
                }) ;
    }


    private void login(){
        ArrayListPost ArrayListPost = new ArrayListPost();
        ArrayListPost.add("UserId" , Singleton.getUser().getId()) ;
        ArrayListPost.add("UserName" , getText(etAccUsername)) ;
        ArrayListPost.add("Password" , getText(etPassword) ) ;

        WSUtils.hitService(this, Url.getYodleeLoginApi(getText(etAccUsername), getText(etPassword)), ArrayListPost, WSMethods.POST,
                TemplateData.class, new WSInterface() {
                    @Override
                    public void onResult(int requestCode, TemplateData data) {

                        if (data.data.toLowerCase().equalsIgnoreCase("\"success\"")) {
                            finish();
                            Utility.startActivity(ActivityYodleeLogin.this, ActivityAutomaticStatement.class, true);
                        } else {
                            new DialogBox(ActivityYodleeLogin.this , data.data, (DialogBoxListener)null) ;
                        }

                    }
                }) ;
    }

}
