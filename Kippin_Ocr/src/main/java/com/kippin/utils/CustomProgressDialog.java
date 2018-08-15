package com.kippin.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.Window;
import android.widget.ImageView;

import com.kippin.kippin.R;

/**
 * Created by dilip.singh on 1/20/2016.
 */
public class CustomProgressDialog {

    Dialog progressDialog;
    Context mContext;

    /**
     * Cunstructor
     * @param context
     * @param mProgressDialog
     */
    public CustomProgressDialog(Context context, Dialog mProgressDialog){

        mContext=context;
        progressDialog = mProgressDialog;


            progressDialog.setContentView(R.layout.progress_dialog);

            ImageView imageViewProgress = (ImageView) progressDialog
                    .findViewById(R.id.imageview_progress);

            BitmapDrawable frame1 = (BitmapDrawable) mContext
                    .getResources().getDrawable(R.drawable.spinner_0);
            BitmapDrawable frame2 = (BitmapDrawable) mContext
                    .getResources().getDrawable(R.drawable.spinner_1);
            BitmapDrawable frame3 = (BitmapDrawable) mContext
                    .getResources().getDrawable(R.drawable.spinner_2);
            BitmapDrawable frame4 = (BitmapDrawable) mContext
                    .getResources().getDrawable(R.drawable.spinner_3);
            BitmapDrawable frame5 = (BitmapDrawable) mContext
                    .getResources().getDrawable(R.drawable.spinner_4);
            BitmapDrawable frame6 = (BitmapDrawable) mContext
                    .getResources().getDrawable(R.drawable.spinner_5);
            BitmapDrawable frame7 = (BitmapDrawable) mContext
                    .getResources().getDrawable(R.drawable.spinner_6);
            BitmapDrawable frame8 = (BitmapDrawable) mContext
                    .getResources().getDrawable(R.drawable.spinner_7);
            BitmapDrawable frame9 = (BitmapDrawable) mContext
                    .getResources().getDrawable(R.drawable.spinner_8);
            BitmapDrawable frame10 = (BitmapDrawable) mContext
                    .getResources().getDrawable(R.drawable.spinner_9);
            BitmapDrawable frame11 = (BitmapDrawable) mContext
                    .getResources().getDrawable(R.drawable.spinner_10);

            BitmapDrawable frame12 = (BitmapDrawable) mContext
                    .getResources().getDrawable(R.drawable.spinner_11);

            AnimationDrawable Anim = new AnimationDrawable();
            Anim.addFrame(frame1, 100);
            Anim.addFrame(frame2, 100);
            Anim.addFrame(frame3, 100);
            Anim.addFrame(frame4, 100);
            Anim.addFrame(frame5, 100);
            Anim.addFrame(frame6, 100);
            Anim.addFrame(frame7, 100);
            Anim.addFrame(frame8, 100);

            Anim.addFrame(frame9, 100);
            Anim.addFrame(frame10, 100);
            Anim.addFrame(frame11, 100);

            Anim.addFrame(frame12, 100);

            Anim.setOneShot(false);
            imageViewProgress.setBackgroundDrawable(Anim);
            Anim.start();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);

                try{
                    progressDialog.show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }







    }
}
