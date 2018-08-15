package com.kippinretail.CommonDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.ImagePickUtility.CameraListener;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.LoginActivity;
import com.kippinretail.Modal.dialogbox.DialogBoxListener;
import com.kippinretail.R;
import com.kippinretail.callbacks.DialogListener;
import com.kippinretail.sharedpreferences.Prefs;

import org.w3c.dom.Text;

/**
 * Created by sandeep.saini on 3/17/2016.
 */

public class MessageDialog
{
    public static void  showFailureDialog(final Activity mcontext)
    {
        try {
            final Dialog dialog = new Dialog(mcontext);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(com.kippin.kippin.R.layout.dialogbox_with_msg);;
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;

            TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
            textForMessage.setText(CommonUtility.failureMsg);
            TextView btnOk = (TextView) dialog.findViewById(R.id.ok_btn);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }catch(Exception ex){
            Log.e("Exception in Dialog", ex.getMessage()+"");
        }

    }
    public static void  showDialog(final Activity mcontext,String message, final boolean isDestroy,final boolean isLogout,final Class target)
    {

        final Dialog dialog = new Dialog(mcontext);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(com.kippin.kippin.R.layout.dialogbox_with_msg);;
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;
        TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
        textForMessage.setText(message);
        TextView btnOk = (TextView) dialog.findViewById(R.id.ok_btn);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDestroy){
                    mcontext.finish();
                }
                else if(isLogout){
                    Intent in = new Intent();
                    in.setClass(mcontext, target);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mcontext.startActivity(in);
                    mcontext.finish();
                }
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    public static void  showDialog(final Activity mcontext,String message, final boolean isDestroy,final boolean isLogout)
    {

        final Dialog dialog = new Dialog(mcontext);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(com.kippin.kippin.R.layout.dialogbox_with_msg);;
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;
        TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
        textForMessage.setText(message);
        TextView btnOk = (TextView) dialog.findViewById(R.id.ok_btn);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDestroy){
                    mcontext.finish();
                }
                else if(isLogout){
                    CommonData.clearPref(mcontext);
                    Intent in = new Intent();
                    in.setClass(mcontext, LoginActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mcontext.startActivity(in);
                    mcontext.finish();
                }
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public static void  showDialog(final Activity mcontext,String message, final boolean isDestroy)
    {
        try {
            final Dialog dialog = new Dialog(mcontext);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(com.kippin.kippin.R.layout.dialogbox_with_msg);;
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;

            TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
            textForMessage.setText(message);
            TextView btnOk = (TextView) dialog.findViewById(R.id.ok_btn);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isDestroy) {
                        mcontext.finish();
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();
        }catch(Exception ex){
            Log.e("Exception in Dialog", ex.getMessage()+"");
        }

    }
    public static void  showDialog(final Activity mcontext,String message, final Class targetclass,final ShareType type )
    {

        final Dialog dialog = new Dialog(mcontext);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(com.kippin.kippin.R.layout.dialogbox_with_msg);;
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;


        TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
        textForMessage.setText(message);
        TextView btnOk = (TextView) dialog.findViewById(R.id.ok_btn);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonUtility.moveToTarget(mcontext, targetclass);
                Intent in = new Intent();
                in.putExtra("shareType",type);
                in.putExtra(mcontext.getString(R.string.user) , CommonUtility.friendId ) ;
                in.setClass(mcontext, targetclass);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mcontext.startActivity(in);
                mcontext.finish();
                mcontext.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
            }
        });

        dialog.show();

    }
    public static void  showDialog(final Activity mcontext,String message, final Class targetclass)
    {

        final Dialog dialog = new Dialog(mcontext);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(com.kippin.kippin.R.layout.dialogbox_with_msg);;
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;

        TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
        textForMessage.setText(message);
        TextView btnOk = (TextView) dialog.findViewById(R.id.ok_btn);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonUtility.moveToTarget(mcontext,targetclass);
            }
        });

        dialog.show();

    }
    public static void  showDialog(final Activity mcontext,String message)
    {
        final Dialog dialog = new Dialog(mcontext);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(com.kippin.kippin.R.layout.dialogbox_with_msg);;
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;
        TextView textForMessage = (TextView)dialog.findViewById(R.id.text_msg);
        textForMessage.setText(message);
        TextView btnOk =(TextView) dialog.findViewById(R.id.ok_btn);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    mcontext.finish();

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public static void showDialog(final Context mcontext, String message, final DialogBoxListener listener) {
        final Dialog dialog = new Dialog(mcontext);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(com.kippin.kippin.R.layout.dialogbox_with_msg);;
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;

        TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
        textForMessage.setText(message);
        TextView btnOk = (TextView) dialog.findViewById(R.id.ok_btn);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mcontext.finish();
                dialog.dismiss();

                if (listener != null)
                    listener.onDialogOkPressed();

            }
        });

        dialog.show();

    }


    /**
     * Method use for call camera and Gallery
     *
     * @param context
     * @param listener
     */
    public static void showDialog(final Activity context, final CameraListener listener) {
        try {

            final Activity mActivity = new Activity();
            final Dialog lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_photo_add);
            lDialog.show();

            Button buttonGallery = (Button) lDialog.findViewById(R.id.gallery_library);
            buttonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onGalleryClick();
                    lDialog.dismiss();
                }
            });

            Button buttonCamera = (Button) lDialog.findViewById(R.id.camera);
            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCameraClick();
                    lDialog.dismiss();
                }
            });

            Button buttonCancel = (Button) lDialog.findViewById(R.id.Cancel);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

// Dialog for Logou
    public static void showDialog(final Activity activity,int message, final PromtListener promtListener) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(com.kippin.kippin.R.layout.dialog_box_yes_no);;
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
        textForMessage.setText(message);
        TextView btnYES = (TextView) dialog.findViewById(R.id.yes_btn);
        btnYES.setText("YES");
        TextView btnNO = (TextView) dialog.findViewById(R.id.no_btn);
            btnNO.setText("CANCEL");
        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promtListener.onCancel();
                dialog.dismiss();
            }
        });

        btnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                promtListener.onSubmit();
            }
        });
        dialog.show();


    }

    public static void showDialog2Button(Context activity, String message, final OnDialogDismissListener onDialogDismiss) {
        try {

            final Dialog dialog = new Dialog(activity,
                    R.style.Theme_AppCompat_Translucent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(com.kippin.kippin.R.layout.dialogbox_with_msg);;
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            TextView textForMessage = (TextView) dialog
                    .findViewById(R.id.text_msg);
            textForMessage.setMovementMethod(new ScrollingMovementMethod());
            textForMessage.setText(message);
            TextView btnOk = (TextView) dialog.findViewById(R.id.ok_btn);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    if (onDialogDismiss != null)
                        onDialogDismiss.onDialogDismiss();

                }

            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showVerificationDialog(Activity context , String Message , final DialogListener litner){
       final Dialog   dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(com.kippin.kippin.R.layout.dialog_box_yes_no);;
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;


        TextView textForMessage = (TextView) dialog.findViewById(R.id.text_msg);
        textForMessage.setText(Message);
        TextView btnYES = (TextView) dialog.findViewById(R.id.yes_btn);
        btnYES.setText("YES");
        TextView btnNO = (TextView) dialog.findViewById(R.id.no_btn);
        btnNO.setText("CANCEL");
        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
               litner.handleYesButton();
            }
        });
        dialog.show();

    }

}
