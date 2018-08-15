package com.kippin.bankexpense;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippin.app.Kippin;
import com.kippin.classification.ClassificationCallback;
import com.kippin.classification.DialogClassification;
import com.kippin.keyboard.MyKeyboard;
import com.kippin.kippin.R;
import com.kippin.selectdate.ModelBankStatement;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.codes.ResponseCode;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.model.ModelCategoryList;
import com.kippin.webclient.model.ModelClassification;
import com.kippin.webclient.model.ModelExpenses;
import com.kippin.webclient.model.ModelResponse;
import com.kippin.webclient.model.TemplateData;

import java.io.File;



/**
 * Created by dilip.singh on 1/27/2016.
 */
public class StatementClass extends SuperActivity {

    public static final String BANK_STATEMENT_INDEX = "BANK_STATEMENT_INDEX";


    //@Bind(R.id.ivArrow)
    ImageView ivArrow;

   // @Bind(R.id.tvClassification)
    TextView tvClassification;

 //   @Bind(R.id.textview_date)
    EditText tvDate;
  //  @Bind(R.id.textview_category)
    EditText tvCategory;
  //  @Bind(R.id.textview_vendor)
    EditText tvVendors;
 //   @Bind(R.id.textview_desc)
    EditText tvDesc;
   // @Bind(R.id.textview_purpose)
    EditText tvPurpose;
   // @Bind(R.id.textview_ammount)
    EditText tvAmmount;
 //   @Bind(R.id.textview_comments)
    EditText tvComments;

  //  @Bind(R.id.next_btn)
    ImageView btnNext;

    DialogClassification dialogClassification;

    String selectedClasificationId = "";

    private MyKeyboard myKeyboard = null;

    boolean isShowing=false;

    public static final String MODEL_BANK_STATEMENT ="MODEL_BANK_STATEMENT" ;


    private ModelBankStatement  modelBankStatement;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Utility.checkCrashTracker(StatementClass.this);
        setContentView(R.layout.layout_statement_details1);

        Singleton.reloadStatements=true;

        int i = getIntent().getIntExtra(BANK_STATEMENT_INDEX, -1);
        if(i==-1) return;

         modelBankStatement = (ModelBankStatement) getIntent().getExtras().getSerializable(MODEL_BANK_STATEMENT);

        dialogClassification = new DialogClassification(this,modelBankStatement.getId());

        WSTemplate wsTemplate = new WSTemplate();

        wsTemplate.context = this;
        wsTemplate.methods = WSMethods.GET;
        wsTemplate.templatePosts = new ArrayListPost();
        wsTemplate.aClass = ModelExpenses.class;
        wsTemplate.isFormData = false;
        wsTemplate.message_id= R.string.pls_wait;
        wsTemplate.url = Url.URL_GET_EXPENSE;
        wsTemplate.wsInterface = new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {

                dialogClassification.requestClassifications(null);


                ModelExpenses modelExpenses_ = (ModelExpenses)data.getData(ModelExpenses.class);

                initialiseUI();
                generateActionBar(R.string.statement_details, true, true);

                setUpListeners();

                ivArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogClassification.show(classificationCallback);
                    }
                });

                updateUI();

            }
        };

         new WSHandler(wsTemplate).execute();
    }

    public void initialiseUI(){
        ivArrow =(ImageView)findViewById(R.id.ivArrow);
        tvClassification =(TextView)findViewById(R.id.tvClassification);
        tvDate=(EditText)findViewById(R.id.textview_date);
        tvCategory=(EditText)findViewById(R.id.textview_category);
        tvVendors=(EditText)findViewById(R.id.textview_vendor);
        tvDesc=(EditText)findViewById(R.id.textview_desc);
        tvPurpose=(EditText)findViewById(R.id.textview_purpose);
        tvAmmount=(EditText)findViewById(R.id.textview_ammount);
        tvComments=(EditText)findViewById(R.id.textview_comments);
        btnNext=(ImageView)findViewById(R.id.next_btn);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    ClassificationCallback classificationCallback = new ClassificationCallback() {
        @Override
        public void onClassificatonSelected(ModelClassification modelClassification, int position) {
            tvClassification.setText(modelClassification.getClassificationType());
            selectedClasificationId = modelClassification.getId() ;
            performCategoryOperation(modelClassification.getCategoryId());
        }
    };


    private void updateUI() {
        int i = getIntent().getIntExtra(BANK_STATEMENT_INDEX, -1);

        if (i == -1) return;

        tvClassification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialogClassification.show(classificationCallback);
            }
        });

        selectedClasificationId = modelBankStatement.getClassificationId() ;
        selectedCatId = modelBankStatement.getCategoryId();
        tvAmmount.setText((modelBankStatement.getCredit()+""));
        tvClassification.setText(modelBankStatement.getClassificationType());
        tvCategory.setText(modelBankStatement.getCategory());
        tvComments.setText(modelBankStatement.getComments());
        tvDate.setText(Utility.getTimeParsed(modelBankStatement.getDate()));
        tvDesc.setText(modelBankStatement.getDescription());
        tvPurpose.setText(modelBankStatement.getPurpose());
        tvVendors.setText(modelBankStatement.getVendor());


    }

    String selectedCatId="-1";

    public void  performCategoryOperation(final String cid){


        WSUtils.hitServiceGet(StatementClass.this, Url.URL_CATEGORY_BY_ID+ File.separator+cid, new ArrayListPost(), ModelCategoryList.class, new WSInterface() {
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

        @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        StatementClass.this.overridePendingTransition(R.anim.push_out_to_left,
                R.anim.push_out_to_right);

    }

    @Override
    public void setUpListeners() {


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedClasificationId.equalsIgnoreCase("94") ||  tvClassification.getText().toString().equalsIgnoreCase("Please Select")){
                    new DialogBox(StatementClass.this,Kippin.kippin.getString(R.string.star_fieds_is_mandatory),(DialogBoxListener)null);

                    return;
                }

                WSTemplate wsTemplate = new WSTemplate();

                ArrayListPost templatePosts = new ArrayListPost();
                templatePosts.add("Id", modelBankStatement.getId());
                templatePosts.add("Vendor", tvVendors.getText().toString());
                templatePosts.add("Description", tvDesc.getText().toString());
                templatePosts.add("Purpose", tvPurpose.getText().toString());
                templatePosts.add("Total", modelBankStatement.getTotal()+"");
                templatePosts.add("Comments", tvComments.getText().toString());
                templatePosts.add("CategoryId", selectedCatId);
                templatePosts.add("ClassificationId", selectedClasificationId);


                wsTemplate.templatePosts = templatePosts;

                wsTemplate.wsInterface = new WSInterface() {
                    @Override
                    public void onResult(int requestCode, TemplateData data) {

                        final ModelResponse modelResponse = (ModelResponse) data.getData(ModelResponse.class);

                        String respose = modelResponse.ResponseMessage;

                        if(respose .equalsIgnoreCase(ResponseCode.SUCCESS)){
                            respose = Kippin.kippin.getString(R.string.success_edit_statement);
                        }

                        new DialogBox(StatementClass.this, respose , new DialogBoxListener() {
                            @Override
                            public void onDialogOkPressed() {
                                switch (modelResponse.ResponseMessage) {

                                    case ResponseCode.SUCCESS:

                                        StatementClass.this.finish();
                                        Singleton.reloadStatements = true;

                                        break;

                                    default:
                                        StatementClass.this.setResult(Activity.RESULT_CANCELED);
                                        break;

                                }

                            }
                        });
                    }
                };

                wsTemplate.url = Url.URL_BANK_EXPENSE;
                wsTemplate.aClass = ModelResponse.class;
                wsTemplate.context = StatementClass.this;
                wsTemplate.methods = WSMethods.PUT;
                wsTemplate.isFormData = true;
                new WSHandler(wsTemplate).execute();
            }
        });
    }
}
