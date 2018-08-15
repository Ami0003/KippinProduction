package com.kippin.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.customdatepicker.MonthYearPicker;
import com.kippin.app.Kippin;
import com.kippin.bankstatement.ActivityBankStatement;
import com.kippin.kippin.R;
import com.kippin.selectdate.ModelBankStatement;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.ImagePickUtility.CameraPicListener;
import com.kippin.utils.ImagePickUtility.CapturePicView;
import com.kippin.utils.ImagePickUtility.OnResultListener;
import com.kippin.utils.ImagePickUtility.TemplateStatementImageUploader;
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

/**
 * Created by ucreateuser on 6/7/2017.
 */

public class ActivityPreview extends SuperActivity implements OnResultListener {
    Button cancel, upload;
    ImageView imageView;
    public static CapturePicView capturePicView = null;
    public static Bitmap bitmap;
    public static ModelBankStatement modelBankStatement;
    Dialog lDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_preview);
        cancel = (Button) findViewById(R.id.btn_cancel);
        upload = (Button) findViewById(R.id.btn_upload);
        imageView = (ImageView) findViewById(R.id.iv_preview);
        imageView.setImageBitmap(bitmap);

        capturePicView = new CapturePicView(this);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TemplateStatementImageUploader templateStatementImageUploader = new TemplateStatementImageUploader();
                templateStatementImageUploader.month = CapturePicView.monthString;
                templateStatementImageUploader.year = CapturePicView.yearString;
                templateStatementImageUploader.onResultListener = ActivityPreview.this;
                capturePicView.uploadImageForStatement(ActivityPreview.this, bitmap, modelBankStatement, templateStatementImageUploader);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }


    @Override
    public void setUpListeners() {

    }

    @Override
    public void openOCR(TemplateData data, Bitmap bm, boolean isSucess) {
        Log.e("After Upload", "Upload");

        Log.e("After Upload", "Upload");
        Log.e("After isSucess:", "" + isSucess);
        if (isSucess) {
             Dialog_Box("Success");
        } else {
             Dialog_Box("Image Failed to upload");
        }


    }

    public void Dialog_Box(final String mMessage) {
        try {

            lDialog = new Dialog(this);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialogbox_with_msg);
            TextView textViewMessage = (TextView) lDialog.findViewById(R.id.text_msg);
//            textViewMessage.setTextSize(Utility.dpToPx(context, R.dimen.dp_20));
            TextView textViewOk = (TextView) lDialog.findViewById(R.id.ok_btn);
            textViewMessage.setText(mMessage);

            textViewOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    finish();


                }
            });

            lDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }
}
