package com.pack.kippin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dialogPayment.PaymentConfirmationDialog;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.KeyboardDontShowBottomView;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModelLogin;
import com.kippin.webclient.model.ModelRegistration;
import com.kippin.webclient.model.TemplateData;


/**
 * Created by dilip.singh on 1/21/2016.
 */
public class PrivateKeyClass extends SuperActivity {

    //@Bind(R.id.do_not_key)
    FrameLayout imageViewDonotKey;

    String mUsername,mPassword,mEmailAddress;

  //  @Bind(R.id.editText_private_key)
    EditText editTextPrivateKey;

  //  @Bind(R.id.submit_btn)
    FrameLayout imageViewSabmit;

    DialogBox dialogBox;
    private String[] sArray;

   // @Bind(R.id.parentLayout)
    RelativeLayout parentLayout;

  //  @Bind(R.id.childLayout)
    LinearLayout childLayout;
    KeyboardDontShowBottomView keyboardDontShowBottomView;



    private boolean mIsPaid,mIsUnlink;

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        imageViewDonotKey =(FrameLayout)findViewById(R.id.do_not_key);

        editTextPrivateKey =(EditText)findViewById(R.id.editText_private_key);
        imageViewSabmit =(FrameLayout)findViewById(R.id.submit_btn);
        parentLayout =(RelativeLayout)findViewById(R.id.parentLayout);
        childLayout =(LinearLayout)findViewById(R.id.childLayout);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.checkCrashTracker(PrivateKeyClass.this);
        setContentView(R.layout.layout_register_private_key);
        initialiseUI();
        setUpListeners();
      //  ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUsername = bundle.getString("USERNAME");
            mPassword= bundle.getString("PASSWORD");
            mEmailAddress = bundle.getString("EMAILADDRESS");
            // now payment is on server
            mIsPaid = Boolean.parseBoolean(bundle.getString("PAID"));
            mIsUnlink=bundle.getBoolean("IsUnlink") ;
            sArray = new String[]{mUsername,mPassword,mEmailAddress, mIsPaid+""};
            imageViewDonotKey.setVisibility(mIsUnlink? View.GONE:View.VISIBLE);
        }
        generateActionBar(R.string.registration_top, true, false);
      //  keyboardDontShowBottomView = new KeyboardDontShowBottomView(PrivateKeyClass.this,parentLayout,childLayout);
    }
    //@OnClick(R.id.submit_btn)
    public void onSubmitClick(View v){

        if (editTextPrivateKey.getText().toString().isEmpty()){

            dialogBox = new DialogBox(getResources().getString(R.string.plz_enter_pvt_key),PrivateKeyClass.this);

        }
        else {
            Utility.printLog("private key string",editTextPrivateKey.getText().toString());
            WSTemplate wsTemplate = new WSTemplate();
            wsTemplate.aClass = ModelRegistration.class;
            wsTemplate.context = PrivateKeyClass.this;
            wsTemplate .message_id = R.string.app_name;
            wsTemplate.methods = WSMethods.PUT;
            wsTemplate.requestCode = 1001;
//            wsTemplate.url = Url.BASE_URL+"/Company/User/PrivateKey/"+editTextPrivateKey.getText().toString();

            wsTemplate.url= Url.getUrlPrivateKey(editTextPrivateKey.getText().toString());

            ArrayListPost templatePosts = new ArrayListPost();
            templatePosts.add("Username",mUsername);
            templatePosts.add("Password",mPassword);
            templatePosts.add("Privatekey",editTextPrivateKey);
            templatePosts.add("Email",mEmailAddress);
            templatePosts.add("SectorId","1");
            templatePosts.add("IsUnlink",""+(mIsUnlink));
            templatePosts.add("IsPaid",mIsPaid+"");
            templatePosts.add("IsTrial",(!mIsPaid)+"");

//            templatePosts.add("IsUnlink",""+(mIsUnlink?1:0));
//            int isPaid = mIsPaid?1:0;
//            int mIsTrial = mIsPaid?0:1;
//            templatePosts.add("IsPaid",isPaid+"");
//            templatePosts.add("IsTrial",mIsTrial+"");

            wsTemplate.templatePosts = templatePosts;

            wsTemplate.wsInterface = new WSInterface() {
                @Override
                public void onResult(int requestCode, TemplateData data) {

                    switch (requestCode){
                        case 1001:
                            ModelRegistration modelRegistration = data.getData(ModelRegistration.class);

                            if (modelRegistration.getResponseCode().equalsIgnoreCase("1")){
//                                if(mIsPaid){
                                if(true){
                                    Singleton.putUser(new ModelLogin());
                                    Singleton.getUser().setId(modelRegistration.getUserId());
//                                    String[] arrayString = new String[]{modelRegistration.getUserId(),""};
//                                    dialogBox = new DialogBox(PrivateKeyClass.this,getResources().getString(R.string.successfully_detailed_added_by_performbook),
//                                            arrayString,"");
                                    new DialogBox(PrivateKeyClass.this, modelRegistration.getResponseMessage(), new DialogBoxListener() {
                                        @Override
                                        public void onDialogOkPressed() {
                                            new DialogBox(PrivateKeyClass.this, "Please check your email to proceed.", new DialogBoxListener() {
                                                @Override
                                                public void onDialogOkPressed() {
                                                    Utility.moveToTarget(PrivateKeyClass.this, LoginScreen.class);
                                                }
                                            });
                                            /*if(mIsPaid && mIsUnlink){
                                                Utility.logout(PrivateKeyClass.this,false);
                                            }else
                                            new PaymentConfirmationDialog(PrivateKeyClass.this).show();*/
                                        }
                                    });
                                }


                            } // if close  modelRegistration.getResponseCode().equalsIgnoreCase("1")
                            else {
                                if (modelRegistration.getResponseMessage().equalsIgnoreCase("No user exist with this email id")){

                                    dialogBox = new DialogBox(getResources().getString(R.string.invalide_private_key), PrivateKeyClass.this);
                                }
                                else {
                                    dialogBox = new DialogBox(modelRegistration.getResponseMessage(), PrivateKeyClass.this);
                                }
                            }
                            break;


                    }

                }
            };

            new WSHandler(wsTemplate).execute();

        }
    }
    //@OnClick(R.id.do_not_key)
    public void onDonotHaveKey(View v){

        dialogBox = new DialogBox(getResources().getString(R.string.do_u_have_perform),sArray,PrivateKeyClass.this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        PrivateKeyClass.this.overridePendingTransition(R.anim.push_out_to_left,
                R.anim.push_out_to_right);
    }


    @Override
    public void setUpListeners() {
        imageViewSabmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextPrivateKey.getText().toString().isEmpty()){

                    dialogBox = new DialogBox(getResources().getString(R.string.plz_enter_pvt_key),PrivateKeyClass.this);

                }
                else {
                    Utility.printLog("private key string",editTextPrivateKey.getText().toString());
                    WSTemplate wsTemplate = new WSTemplate();
                    wsTemplate.aClass = ModelRegistration.class;
                    wsTemplate.context = PrivateKeyClass.this;
                    wsTemplate .message_id = R.string.app_name;
                    wsTemplate.methods = WSMethods.PUT;
                    wsTemplate.requestCode = 1001;
//            wsTemplate.url = Url.BASE_URL+"/Company/User/PrivateKey/"+editTextPrivateKey.getText().toString();

                    wsTemplate.url= Url.getUrlPrivateKey(editTextPrivateKey.getText().toString());

                    ArrayListPost templatePosts = new ArrayListPost();
                    templatePosts.add("Username",mUsername);
                    templatePosts.add("Password",mPassword);
                    templatePosts.add("Privatekey",editTextPrivateKey);
                    templatePosts.add("Email",mEmailAddress);
                    templatePosts.add("SectorId","1");
                    templatePosts.add("IsUnlink",""+(mIsUnlink));
                    templatePosts.add("IsPaid",mIsPaid+"");
                    templatePosts.add("IsTrial",(!mIsPaid)+"");

//            templatePosts.add("IsUnlink",""+(mIsUnlink?1:0));
//            int isPaid = mIsPaid?1:0;
//            int mIsTrial = mIsPaid?0:1;
//            templatePosts.add("IsPaid",isPaid+"");
//            templatePosts.add("IsTrial",mIsTrial+"");

                    wsTemplate.templatePosts = templatePosts;

                    wsTemplate.wsInterface = new WSInterface() {
                        @Override
                        public void onResult(int requestCode, TemplateData data) {

                            switch (requestCode){
                                case 1001:
                                    ModelRegistration modelRegistration = data.getData(ModelRegistration.class);

                                    if (modelRegistration.getResponseCode().equalsIgnoreCase("1")){
//                                if(mIsPaid){
                                        if(true){
                                            Singleton.putUser(new ModelLogin());
                                            Singleton.getUser().setId(modelRegistration.getUserId());
//                                    String[] arrayString = new String[]{modelRegistration.getUserId(),""};
//                                    dialogBox = new DialogBox(PrivateKeyClass.this,getResources().getString(R.string.successfully_detailed_added_by_performbook),
//                                            arrayString,"");
                                            new DialogBox(PrivateKeyClass.this, "Successful Registration", new DialogBoxListener() {
                                                @Override
                                                public void onDialogOkPressed() {

                                                    new DialogBox(PrivateKeyClass.this, "Please check your email to proceed.", new DialogBoxListener() {
                                                        @Override
                                                        public void onDialogOkPressed() {
                                                            Utility.moveToTarget(PrivateKeyClass.this,LoginScreen.class);
                                                        }
                                                    });
                                                    /*if(mIsPaid && mIsUnlink){
                                                        Utility.logout(PrivateKeyClass.this,false);
                                                    }else
                                                        new PaymentConfirmationDialog(PrivateKeyClass.this).show();*/
                                                }
                                            });
                                        }

                                    }// if close  modelRegistration.getResponseCode().equalsIgnoreCase("1")
                                    else {
                                        if (modelRegistration.getResponseMessage().equalsIgnoreCase("No user exist with this email id")){

                                            dialogBox = new DialogBox(getResources().getString(R.string.invalide_private_key), PrivateKeyClass.this);
                                        }
                                        else {
                                            dialogBox = new DialogBox(modelRegistration.getResponseMessage(), PrivateKeyClass.this);
                                        }
                                    }
                                    break;


                            }

                        }
                    };

                    new WSHandler(wsTemplate).execute();

                }
            }
        });


        imageViewDonotKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox = new DialogBox(getResources().getString(R.string.do_u_have_perform),sArray,PrivateKeyClass.this);
            }
        });
    }
}
