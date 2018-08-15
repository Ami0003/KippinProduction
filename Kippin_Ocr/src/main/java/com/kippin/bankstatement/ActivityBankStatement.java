package com.kippin.bankstatement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.customdatepicker.Month_YearPicker;
import com.kippin.activities.ActivityPreview;
import com.kippin.app.Kippin;
import com.kippin.cloudimages.CloudClickListener;
import com.kippin.kippin.R;
import com.kippin.ocr.ActivityOCR;
import com.kippin.selectdate.ActivitySelectDate;
import com.kippin.selectdate.ModelBankStatement;
import com.kippin.selectdate.ModelBankStatements;
import com.kippin.selectmonthyear.ActivitySelectMonthYear;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.ImagePickUtility.CameraPicListener;
import com.kippin.utils.ImagePickUtility.CapturePicView;
import com.kippin.utils.ImagePickUtility.OnCloudClickListener;
import com.kippin.utils.ImagePickUtility.OnResultListener;
import com.kippin.utils.ImagePickUtility.TemplateStatementImageUploader;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.utils.dialogbox.OnClickListener;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.codes.ResponseCode;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModelImagePost;
import com.kippin.webclient.model.TemplateData;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityBankStatement extends SuperActivity implements BankStatementListener, OnResultListener, CloudClickListener {
    public static final String LIST_MODEL_BANK_STATEMENT = "LIST_MODEL_BANK_STATEMENT";
    public static String BLANK_CLASSIFICATION_ID = "1";
    public static CapturePicView capturePicView = null;
    Month_YearPicker month_yearPicker;
    String monthString = "", yearString = "";
    String cameragallery = "";
    //    String[] statuses  = new String[]{"All","Pending","Approved","Reject","Locked"};
//    public static String[] statuses  = new String[]{"All","Pending","Submitted","Reject","Locked"};
    public static String[] statuses_ids = new String[]{"0", "1", "2", "3", "4"};
    public static int postion = -1;
    ListView dates, desc;
    // @Bind(R.id.tvStatus)
    TextView tvStatus;
    // @Bind(R.id.tvDate)
    TextView tvDate;
    // @Bind(R.id.spStatus)
    Spinner spStatus;
    //    String status_selected =statuses[0] ;
    String status_selected_id = statuses_ids[0];
    Handler handler = new Handler();
    private ArrayList<ModelBankStatement> modelBankStatements;
    public static ArrayList<ModelBankStatement> modelBankStatements_ = new ArrayList<>();
    private OnCloudClickListener onCloudListener = new OnCloudClickListener() {
        @Override

        public void onClick() {
            Utility.startActivity(ActivityBankStatement.this, ActivitySelectMonthYear.class, true);
//            Intent intent = new Intent(context, ActivitySelectMonthYear.class);
//            context.startActivityForResult(intent,REQUESTCODE_CLOUD);

//            Utility.activityBankStatement = ActivityBankStatement.this;
            Utility.cloudClickListener = ActivityBankStatement.this;
        }
    };

    public String monthName(String monthname) {
        if (monthname.equals("January")) {
            monthname = "01";
            return monthname;
        } else if (monthname.equals("February")) {
            monthname = "02";
            return monthname;
        } else if (monthname.equals("March")) {
            monthname = "03";
            return monthname;
        } else if (monthname.equals("April")) {
            monthname = "04";
            return monthname;
        } else if (monthname.equals("May")) {
            monthname = "05";
            return monthname;
        } else if (monthname.equals("June")) {
            monthname = "06";
            return monthname;
        } else if (monthname.equals("July")) {
            monthname = "07";
            return monthname;
        } else if (monthname.equals("August")) {
            monthname = "08";
            return monthname;
        } else if (monthname.equals("September")) {
            monthname = "09";
            return monthname;
        } else if (monthname.equals("October")) {
            monthname = "10";
            return monthname;
        } else if (monthname.equals("November")) {
            monthname = "11";
            return monthname;
        } else if (monthname.equals("December")) {
            monthname = "12";
            return monthname;
        }
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.checkCrashTracker(ActivityBankStatement.this);
        setContentView(R.layout.activity_bank_statement);
        month_yearPicker = new Month_YearPicker(this);
        initialiseUI();

        dates = (ListView) findViewById(R.id.listView1);
        desc = (ListView) findViewById(R.id.hListView1);

//        addHeaders();
        String month = getIntent().getStringExtra("Month");
        String year = getIntent().getStringExtra("Year");


        modelBankStatements = ((ModelBankStatements) getIntent().getSerializableExtra(LIST_MODEL_BANK_STATEMENT)).modelBankStatements;

        tvDate.setText(Utility.getMonthInShort(month) + "-" + year);


        capturePicView = new CapturePicView(this);

        ScrollSync scrollSync = new ScrollSync();
        scrollSync.addList(dates);
        scrollSync.addList(desc);
        setClickListeners();

        month_yearPicker.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("11", "010");
                // tvSelectMonth.setText(month_yearPicker.getSelectedMonthName() + "-" + month_yearPicker.getSelectedYear());
                Log.e("Reuslt:", "" + month_yearPicker.getSelectedMonthName() + " >> " + month_yearPicker.getSelectedYear());
                monthString = monthName("" + month_yearPicker.getSelectedMonthName());
                yearString = "" + month_yearPicker.getSelectedYear();
                if (cameragallery.equals("camera")) {
                    try {
                        capturePicView.launchCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (cameragallery.equals("gallery")) {
                    capturePicView.launchGallery();
                } else {

                }

            }
        }, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("00", "00");
                monthString = "";
                yearString = "";
                if (cameragallery.equals("camera")) {
                    try {
                        capturePicView.launchCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (cameragallery.equals("gallery")) {
                    capturePicView.launchGallery();
                } else {

                }




            }
        });


    }

    public void initialiseUI() {
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvDate = (TextView) findViewById(R.id.tvDate);
        spStatus = (Spinner) findViewById(R.id.spStatus);
    }

    private void setClickListeners() {

        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spStatus.performClick();
            }
        });

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                String s = statuses_ids[position] ;

//                if(s.toLowerCase().equalsIgnoreCase("Approved")){

//                if(s.toLowerCase().equalsIgnoreCase(statuses[2])){
//                    status_selected = "Accept";
//                }else{
                status_selected_id = statuses_ids[position];
//                }
//                tvStatus.setText((String) spStatus.getSelectedItem());

                if (modelBankStatements.size() == 1 && modelBankStatements.get(0).getResponseCode().equalsIgnoreCase(ResponseCode.NO_RECORDS_FOUND)) {


                    new DialogBox(ActivityBankStatement.this, modelBankStatements.get(0).getResponseMessage(), new DialogBoxListener() {
                        @Override
                        public void onDialogOkPressed() {
                        }
                    });

                } else
                    loadList(status_selected_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        loadList(status_selected_id);
        generateActionBar(R.string.bank_statement_header, true, true);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Singleton.reloadStatements) {
            Singleton.reloadStatements = false;
            sync();
        }

    }

    public void sync() {
        WSTemplate wsTemplate = new WSTemplate();

        ArrayListPost templatePosts = new ArrayListPost();
        templatePosts.add("AccountNumber", getIntent().getStringExtra(ActivitySelectDate.ACCOUNT_NUMBER));
        templatePosts.add("AccountName", getIntent().getStringExtra(ActivitySelectDate.ACCOUNT_NAME));
        templatePosts.add("Month", getIntent().getStringExtra("Month"));
        templatePosts.add("Year", getIntent().getStringExtra("Year"));
        templatePosts.add("BankId", getIntent().getStringExtra(ActivitySelectDate.BANK_ID));
        templatePosts.add("UserId", Singleton.getUser().getId());


        wsTemplate.templatePosts = templatePosts;

        wsTemplate.wsInterface = new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {
//                Singleton.statements =(ModelBankStatements)data.getData(ModelBankStatements.class);

                modelBankStatements = ((ModelBankStatements) data.getData(ModelBankStatements.class)).modelBankStatements;

                status_selected_id = statuses_ids[0];

                if (modelBankStatements != null) {
                    loadList(status_selected_id);
                } else {
                    new DialogBox(ActivityBankStatement.this, "Data not available right now,Please try later.", (DialogBoxListener) null);
                }

            }
        };

        wsTemplate.url = Url.getUrlBankStatement(Singleton.isCreditCard);
        wsTemplate.aClass = ModelBankStatements.class;
        wsTemplate.context = ActivityBankStatement.this;
        wsTemplate.methods = WSMethods.POST;
        wsTemplate.isFormData = false;
        new WSHandler(wsTemplate).execute();

    }

    private void loadList(String status_id) {


        modelBankStatements_ = new ArrayList<>();

        for (int i = 0; i < modelBankStatements.size(); i++) {
//            if( status_id.equalsIgnoreCase("All") ||  modelBankStatements.get(i).getStatus().equalsIgnoreCase(status_id)  ){
//                modelBankStatements_.add(modelBankStatements.get(i));
//            }

            if (status_id.equalsIgnoreCase(statuses_ids[0]) || modelBankStatements.get(i).getStatusId().equalsIgnoreCase(status_id)) {
                modelBankStatements_.add(modelBankStatements.get(i));
            }

        }
        dates.setAdapter(new CustomAdapter(status_id, modelBankStatements_));
        dates.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        desc.setAdapter(new BankStatementDescriptionAdapter(ActivityBankStatement.this, status_id, modelBankStatements_, this));
        ActivityBankStatement.capturePicView.addOnCloudClickListener(onCloudListener);


    }

    @Override
    public void setUpListeners() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        {
            switch (requestCode) {
                case 1:
                    loadList(status_selected_id);
                    break;
                case CapturePicView.REQUEST_CODE_CAMERA:
                    capturePicView.onActivityResult(requestCode, resultCode, data, new CameraPicListener() {
                        @Override
                        public void onImageSelected(final Bitmap bm) {
                            if (bm == null) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogBox(ActivityBankStatement.this, Kippin.kippin.getResources().getString(R.string.image_size_too_low), (DialogBoxListener) null);
                                    }
                                });
                            } else {
                                //    capturePicView.uploadImageForStatement(bm);
                                ActivityPreview.bitmap=bm;
                                modelBankStatements_.get(postion).setBankId(getIntent().getStringExtra(ActivitySelectDate.BANK_ID));
                                TemplateStatementImageUploader templateStatementImageUploader = new TemplateStatementImageUploader();
                                templateStatementImageUploader.month = CapturePicView.monthString;
                                templateStatementImageUploader.year = CapturePicView.yearString;
                                templateStatementImageUploader.onResultListener = ActivityBankStatement.this;
                                ActivityPreview.modelBankStatement=modelBankStatements_.get(postion);
                                capturePicView.uploadImageForStatement(ActivityBankStatement.this, bm, modelBankStatements_.get(postion), templateStatementImageUploader);

                            }
                        }


                    });
                    break;
                case CapturePicView.REQUEST_CODE_GALLERY:
             //   case CapturePicView.PIC_CROP:

                    capturePicView.onActivityResult(requestCode, resultCode, data, new CameraPicListener() {
                        @Override
                        public void onImageSelected(final Bitmap bm) {
                            if (bm == null) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogBox(ActivityBankStatement.this, Kippin.kippin.getResources().getString(R.string.image_size_too_low), (DialogBoxListener) null);
                                    }
                                });
                            } else {
                                //    capturePicView.uploadImageForStatement(bm);
                                ActivityPreview.bitmap=bm;
                                ActivityPreview.modelBankStatement=modelBankStatements_.get(postion);
                                startActivity(new Intent(ActivityBankStatement.this,ActivityPreview.class));
/*                                modelBankStatements_.get(postion).setBankId(getIntent().getStringExtra(ActivitySelectDate.BANK_ID));
                                TemplateStatementImageUploader templateStatementImageUploader = new TemplateStatementImageUploader();
                                templateStatementImageUploader.month = CapturePicView.monthString;
                                templateStatementImageUploader.year = CapturePicView.yearString;
                                templateStatementImageUploader.onResultListener = ActivityBankStatement.this;
                                ActivityPreview.modelBankStatement=modelBankStatements_.get(postion);
                                capturePicView.uploadImageForStatement(ActivityBankStatement.this, bm, modelBankStatements_.get(postion), templateStatementImageUploader);*/

                            }
                        }


                    });


                    break;
            }
        }


    }

    public void openOCR(TemplateData data, Bitmap bm, boolean isToshowSucessMessage) {

        String message = "Success";

        message = Kippin.kippin.getString(R.string.success_edit_statement);

        ModelImagePost modelImagePost = data.getData(ModelImagePost.class);

//        Singleton.modelImagePost = modelImagePost;

        Bundle bundle = new Bundle();
        bundle.putString(ActivitySelectDate.BANK_ID, getIntent().getStringExtra(ActivitySelectDate.BANK_ID));

        ModelBankStatement statement = modelBankStatements_.get(postion);
//        bundle.putString(ActivityOCR.DATE,statement.getCashBillDate());
//        bundle.putString(ActivityOCR.STATUS,statement.getStatus());
//        bundle.putString(ActivityOCR.DESCRIPTION,statement.getDescription());
//        bundle.putString(ActivityOCR.AMOUNT,statement.getTotal()+"");
//        bundle.putString(ActivityOCR.STATEMENT_ID,statement.getId());
//        bundle.putInt(ActivityOCR.ITEM_POSITION, postion);
        bundle.putSerializable(ActivityOCR.MODEL_BANK_STATEMENT, statement);

        bundle.putBoolean(ActivityOCR.SUCCESS_MESSAGE, isToshowSucessMessage);

        Singleton.bm = Bitmap.createScaledBitmap(bm, Utility.dpToPx(ActivityBankStatement.this, 90), Utility.dpToPx(ActivityBankStatement.this, 90), false);
        ;

        Utility.startActivity(ActivityBankStatement.this, ActivityOCR.class, bundle, true);


    }

    public void onCloudClick(Bitmap bitmap, String cloudImageName, String cloudImagePath, String month, String year) {
        Utility.activityBankStatement = null;
//        final Activity activity ,final Bitmap bm,final ModelBankStatement modelBankStatement,final OnResultListener onResultListener

        modelBankStatements_.get(postion).setBankId(getIntent().getStringExtra(ActivitySelectDate.BANK_ID));

        TemplateStatementImageUploader templateStatementImageUploader = new TemplateStatementImageUploader();
        templateStatementImageUploader.CloudImageName = cloudImageName;
        templateStatementImageUploader.CloudImagePath = cloudImagePath;
        templateStatementImageUploader.isCloudImage = true;
        templateStatementImageUploader.year = year;
        templateStatementImageUploader.month = month;
        templateStatementImageUploader.onResultListener = ActivityBankStatement.this;

        capturePicView.uploadImageForStatement(ActivityBankStatement.this, bitmap, modelBankStatements_.get(postion), templateStatementImageUploader);
    }

    @Override
    public void onViewClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ActivityOCR.MODEL_BANK_STATEMENT, modelBankStatements_.get(position));
        bundle.putString(ActivitySelectDate.BANK_ID, getIntent().getStringExtra(ActivitySelectDate.BANK_ID));
        Utility.startActivity(ActivityBankStatement.this, ActivityOCR.class, bundle, true);
    }

    @Override
    public void onEditClick(int position) {
        ActivityBankStatement.capturePicView.show(false);
        ActivityBankStatement.postion = position;
    }

    @Override
    public void onDeleteClick(final int position) {

        new DialogBox(ActivityBankStatement.this, "Are you sure you want to delete?", new OnClickListener() {
            @Override
            public void onPositiveClick() {

                WSUtils.hitService(ActivityBankStatement.this, Url.getDeleteStatementUrl(modelBankStatements_.get(position).getId()), new ArrayListPost(), WSMethods.POST, TemplateData.class, new WSInterface() {
                    @Override
                    public void onResult(int requestCode, TemplateData data) {
                        boolean b = Boolean.getBoolean(data.data);

                        if (true) {
                            ModelBankStatement modelBankStatement = modelBankStatements_.get(position);
                            modelBankStatements.remove(modelBankStatement);
                            modelBankStatements_.remove(modelBankStatement);
                            loadList(status_selected_id);
                            new DialogBox(ActivityBankStatement.this, "Deleted Successfully", (DialogBoxListener) null);
                        }

                    }
                });
            }

            @Override
            public void onNegativeClick() {

            }
        }, "CANCEL");


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            switch (requestCode) {
                case Utility.MY_PERMISSION_ACCESS_CAMERA:
                    if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        cameragallery = "camera";
                        month_yearPicker.show();
                       // capturePicView.launchCamera();

                    } else {
                        Toast.makeText(ActivityBankStatement.this, "This is a required for the app to function", Toast.LENGTH_LONG).show();

                    }
                    break;
                case Utility.MY_PERMISSION_ACCESS_STORAGE:
                    if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        cameragallery = "gallery";
                        month_yearPicker.show();
                       // capturePicView.launchGallery();

                    } else {
                        Toast.makeText(ActivityBankStatement.this, "This is a required for the app to function", Toast.LENGTH_LONG).show();

                    }
                    break;
            }
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
    }

    class CustomAdapter extends BaseAdapter {

        ViewHolder viewHolder;
        private String status;
        private ArrayList<ModelBankStatement> modelBankStatements_;

        public CustomAdapter(String s, ArrayList<ModelBankStatement> modelBankStatements_) {
            this.modelBankStatements_ = modelBankStatements_;
            this.status = s;
        }

        @Override
        public int getCount() {
            try {
                return modelBankStatements_.size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return modelBankStatements_.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ModelBankStatement statement = modelBankStatements_.get(position);


//            if( !status.equalsIgnoreCase("All") &&  !statement.getStatus().equalsIgnoreCase(status)  ){
//                return  new View(ActivityBankStatement.this.getApplicationContext());
//            }


            if (true) {
                convertView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_bank_statement_date_ocr, parent, false);
                viewHolder = initialiseElements(viewHolder, convertView, parent);

            } else {
                convertView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_bank_statement_date, parent, false);
                viewHolder = (ViewHolder) convertView.getTag();
                convertView.setTag(viewHolder);
            }


            if (viewHolder == null || viewHolder.tvDate == null) {
                convertView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_bank_statement_date, parent, false);
                viewHolder = (ViewHolder) convertView.getTag();
                convertView.setTag(viewHolder);
            }

            if (position % 2 == 0) {
                convertView.setBackgroundColor((getResources().getColor(R.color.bank_statement_even_items)));
            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }

            viewHolder.tvDate.setText(Utility.getTimeParsed(statement.getDate()));


            viewHolder.tvDesc.setText(statement.getDescription());
            return convertView;
        }

        public ViewHolder initialiseElements(ViewHolder viewHolder, View convertView, ViewGroup parent) {
            viewHolder = new ViewHolder();

            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.tvDescription);

            convertView.setTag(viewHolder);
            return viewHolder;
        }


        class ViewHolder {
            TextView tvDate, tvDesc;

        }

    }
}
