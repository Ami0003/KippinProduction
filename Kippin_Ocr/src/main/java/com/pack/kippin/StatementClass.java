package com.pack.kippin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModelRegistration;
import com.kippin.webclient.model.TemplateData;


import butterknife.ButterKnife;

/**
 * Created by dilip.singh on 1/27/2016.
 */
public class StatementClass extends SuperActivity {

    //@Bind(R.id.imageview_arrow)
  //  ImageView imageViewArrow;
  //  @Bind(R.id.textview_date)
    EditText textViewDate;
  //  @Bind(R.id.textview_category)
    EditText textViewCategory;
  //  @Bind(R.id.textview_vendor)
    EditText textViewVendors;
   // @Bind(R.id.textview_desc)
    EditText textViewDesc;
    //@Bind(R.id.textview_purpose)
    EditText textViewPurpose;
 //   @Bind(R.id.textview_ammount)
    EditText textViewAmmount;
  //  @Bind(R.id.textview_comments)
    EditText textViewComments;

    @Override
    public void initialiseUI() {
        super.initialiseUI();
     //  imageViewArrow = (ImageView)findViewById(R.id.imageview_arrow);
        textViewDate =(EditText)findViewById(R.id.textview_date);
        textViewCategory =(EditText)findViewById(R.id.textview_category);
        textViewVendors =(EditText)findViewById(R.id.textview_vendor);
        textViewDesc =(EditText)findViewById(R.id.textview_desc);

        textViewPurpose =(EditText)findViewById(R.id.textview_purpose);
        textViewAmmount =(EditText)findViewById(R.id.textview_ammount);
        textViewComments =(EditText)findViewById(R.id.textview_comments);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_statement_details1);
        initialiseUI();
        setUpListeners();;
        ButterKnife.bind(this);
        generateActionBar(R.string.statement_details, true, true);
        callStatementApi();

    }

    /**
     * Method use for call statement api
     */
    public void callStatementApi(){
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.aClass = ModelRegistration.class;
        wsTemplate.context = StatementClass.this;
        wsTemplate .message_id = R.string.app_name;
        wsTemplate.methods = WSMethods.POST;
        wsTemplate.requestCode = 1001;

        wsTemplate.url = Url.getStatementApi();

        ArrayListPost templatePosts = new ArrayListPost();
        templatePosts.add("AccountNumber","4242424242424242");
        templatePosts.add("Month","nov");
        templatePosts.add("Year","2015");
        wsTemplate.templatePosts = templatePosts;


        wsTemplate.wsInterface = new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {


            }
        };
        new WSHandler(wsTemplate).execute();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        StatementClass.this.overridePendingTransition(R.anim.push_out_to_left,
                R.anim.push_out_to_right);

    }

    @Override
    public void setUpListeners() {

    }
}
