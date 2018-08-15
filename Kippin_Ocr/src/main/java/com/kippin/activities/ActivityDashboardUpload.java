package com.kippin.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.customdatepicker.MonthYearPicker;
import com.kippin.app.Kippin;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.ImagePickUtility.CameraPicListener;
import com.kippin.utils.ImagePickUtility.CapturePicView;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModelImagePost;
import com.kippin.webclient.model.TemplateData;

import java.io.IOException;

/**
 * Created by ucreateuser on 6/7/2017.
 */

public class ActivityDashboardUpload extends SuperActivity implements View.OnClickListener {
    CapturePicView capturePicView = null;
    TextView tvUpload, tvSelectMonth;
    MonthYearPicker monthYearPicker;
    Handler handler = new Handler();
    String monthname = "";
    EditText edInsertAccount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_upload);
        capturePicView = new CapturePicView(this);
        tvUpload = (TextView) findViewById(R.id.tvUpload);
        tvSelectMonth = (TextView) findViewById(R.id.tvSelectMonth);
        edInsertAccount = (EditText) findViewById(R.id.edInsertAccount);
        edInsertAccount.addTextChangedListener(new Decimal_Filter(edInsertAccount, ActivityDashboardUpload.this));
        tvUpload.setOnClickListener(this);
        tvSelectMonth.setOnClickListener(this);
        monthYearPicker = new MonthYearPicker(this);
        monthYearPicker.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("which:", "" + which);

                tvSelectMonth.setText(monthYearPicker.getSelectedMonthName() + "-" + monthYearPicker.getSelectedYear());
                Log.e("Reuslt:", "" + monthYearPicker.getSelectedMonthName() + " >> " + monthYearPicker.getSelectedYear());
                monthname = monthName("" + monthYearPicker.getSelectedMonthName());
            }
        }, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("which:", "which");


            }
        });

    }

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

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvSelectMonth) {
            monthYearPicker.show();
        } else if (id == R.id.tvUpload) {
            if (monthname.equals("")) {
                Toast.makeText(ActivityDashboardUpload.this, "Please Select Month and Year", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    capturePicView.launchCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

    }


    @Override
    public void setUpListeners() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {


            case CapturePicView.REQUEST_CODE_CAMERA:
            case CapturePicView.REQUEST_CODE_GALLERY:

                capturePicView.onActivityResult(requestCode, resultCode, data, new CameraPicListener() {
                    @Override
                    public void onImageSelected(Bitmap bm) {
                        if (bm == null) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogBox(ActivityDashboardUpload.this, Kippin.kippin.getResources().getString(R.string.image_size_too_low), (DialogBoxListener) null);
                                }
                            });
                        } else {
                            try {

                                String image = Utility.convertBitmap2Base64(bm);
                                Log.e("image:", "" + image);
                                final ArrayListPost templatePosts = new ArrayListPost();
                                templatePosts.add("Base64Image", image);
                                if (edInsertAccount.getText().length() != 0) {
                                    templatePosts.add("BillAmount", edInsertAccount.getText().toString());
                                }
                                templatePosts.add("ImageName", "test.jpg");
                                templatePosts.add("UserId", Singleton.getUser().getId());
                                templatePosts.add("Year", "" + monthYearPicker.getSelectedYear());
                                templatePosts.add("Month", monthname);
                                Log.e("monthname:", "" + monthname);
                                Log.e("Year:", "" + monthYearPicker.getSelectedYear());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        WSUtils.hitService(ActivityDashboardUpload.this, Url.URL_UPLOAD, false, templatePosts, WSMethods.POST, ModelImagePost.class, new WSInterface() {
                                            @Override
                                            public void onResult(int requestCode, TemplateData data) {
                                                new DialogBox(ActivityDashboardUpload.this, ((ModelImagePost) data.getData(ModelImagePost.class)).getResponseMessage(), (DialogBoxListener) null, 0);
                                                Log.e("Response", "" + ((ModelImagePost) data.getData(ModelImagePost.class)).getResponseMessage());
                                                String s = ((ModelImagePost) data.getData(ModelImagePost.class)).getResponseMessage();
                                                Singleton.bm = null;
                                              /*  if (s.equals("Image Successfully Saved.")) {
                                                    finish();
                                                }*/

                                            }
                                        });
                                    }
                                });
                            } catch (Exception e)

                            {
                                e.printStackTrace();
                            }
                        }

                    }
                });


                break;
        }

    }
}
