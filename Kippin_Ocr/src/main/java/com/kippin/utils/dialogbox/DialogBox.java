package com.kippin.utils.dialogbox;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kippin.app.Kippin;
import com.kippin.dashboard.ActivityDashboard;
import com.kippin.kippin.R;
import com.kippin.utils.ImagePickUtility.CameraListener;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModelBankAccount;
import com.kippin.webclient.model.ModelRegistration;
import com.kippin.webclient.model.ModelUpdateAccountName;
import com.kippin.webclient.model.TemplateData;
import com.pack.kippin.PaymentDetailsClass;
import com.pack.kippin.PerformBook;

/**
 * Created by dilip.singh on 1/21/2016.
 */
public class DialogBox {


    Activity mActivity;
    Context mContext;
    Dialog lDialog;
    Bundle nBundle;


    /**
     * Cunstructor use for call
     * forget password dialog
     *
     * @param context
     */
    public DialogBox(final Context context) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dailog_forget_passowrd);
            final EditText editTextEmail = (EditText) lDialog.findViewById(R.id.editText_emailid);
            TextView textViewCancel = (TextView) lDialog.findViewById(R.id.cancel_btn);
            TextView textViewSubmit = (TextView) lDialog.findViewById(R.id.submit_btn);
            textViewCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                }
            });

            textViewSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mActivity = (Activity) context;
                    if (editTextEmail.getText().toString().isEmpty()) {
                        DialogBox dialogBox = new DialogBox(mActivity.getResources().getString(R.string.plz_fill_email_field), context);
                    } else if (!Utility.isValidEmail(editTextEmail.getText().toString())) {
                        DialogBox dialogBox = new DialogBox(mActivity.getResources().getString(R.string.plz_enter_valide_email_address), context);
                    } else {
                        WSTemplate wsTemplate = new WSTemplate();
                        wsTemplate.aClass = ModelRegistration.class;
                        wsTemplate.context = context;
                        wsTemplate.message_id = R.string.app_name;
                        wsTemplate.methods = WSMethods.PUT;
                        wsTemplate.requestCode = 1001;

                        wsTemplate.url = Url.getForgotPassword(editTextEmail.getText().toString());

                        ArrayListPost templatePosts = new ArrayListPost();
                        templatePosts.add("Email", editTextEmail.getText().toString());
                        Utility.printLog("edit email value", editTextEmail.getText().toString());
                        wsTemplate.templatePosts = templatePosts;
                        wsTemplate.wsInterface = new WSInterface() {
                            @Override
                            public void onResult(int requestCode, TemplateData data) {
                                switch (requestCode) {
                                    case 1001:
                                        ModelRegistration modelRegistration = data.getData(ModelRegistration.class);
                                        if (modelRegistration.getResponseMessage().equalsIgnoreCase("Success") && modelRegistration.getResponseCode().equalsIgnoreCase("1")) {

                                            DialogBox dialogBox = new DialogBox(context, Kippin.res.getString(R.string.pls_go_to_email), new DialogBoxListener() {
                                                @Override
                                                public void onDialogOkPressed() {
                                                    lDialog.dismiss();
                                                }
                                            });
                                        } else if (modelRegistration.getResponseCode().equalsIgnoreCase("1")) {
                                            new DialogBox(modelRegistration.getResponseMessage(), context);
                                        } else {
                                            editTextEmail.setText("");
                                            DialogBox dialogBox = new DialogBox(modelRegistration.getResponseMessage(), context);
                                        }

                                        break;


                                }
                            }
                        };
                        new WSHandler(wsTemplate).execute();
                    }
                }
            });

            lDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    /**
     * Cunstructor use for call message dialog
     *
     * @param mMessage
     * @param context
     */
    public DialogBox(final String mMessage, final Context context) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialogbox_with_msg);
            TextView textViewMessage = (TextView) lDialog.findViewById(R.id.text_msg);
            TextView textViewOk = (TextView) lDialog.findViewById(R.id.ok_btn);
            textViewMessage.setText(mMessage);
            textViewOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    mActivity = (Activity) context;
                    Intent intent;
                    switch (mMessage) {
                        case "Login Successfully":
                            intent = new Intent(context, ActivityDashboard.class);
                            mActivity.startActivity(intent);
                            mActivity.overridePendingTransition(R.anim.push_in_to_left,
                                    R.anim.push_in_to_right);
                            mActivity.finish();
                            break;

                        case "Successfully Registered":
                            Utility.logout(mActivity, false);

                            break;
                    }
                }
            });

            lDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }

    }

    /**
     * Constructor use for yes and no dialog box
     *
     * @param mMessage
     * @param nameArray
     * @param context
     */
    public DialogBox(final String mMessage, final String[] nameArray, final Context context) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_box_yes_no);
            TextView textViewMessage = (TextView) lDialog.findViewById(R.id.text_msg);
            textViewMessage.setText(mMessage);
            TextView textViewNo = (TextView) lDialog.findViewById(R.id.no_btn);
            TextView textViewYes = (TextView) lDialog.findViewById(R.id.yes_btn);
            textViewYes.setText("YES");
            textViewNo.setText("CANCEL");


            textViewNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    if (mMessage.equalsIgnoreCase(context.getResources().getString(R.string.are_sure_uwant_logout))) {

                    } else {
                        DialogBox dialogBox = new DialogBox(context.getResources().getString(R.string.kindly_contact_private_key), context);
                    }
                }
            });

            textViewYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    mActivity = (Activity) context;
                    if (mMessage.equalsIgnoreCase(context.getResources().getString(R.string.are_sure_uwant_logout))) {
                        Utility.logout(mActivity, false);
                    } else {
                        Intent intent = new Intent(context, PerformBook.class);
                        nBundle = new Bundle();
                        nBundle.putString("USERNAME", nameArray[0]);
                        nBundle.putString("PASSWORD", nameArray[1]);
                        nBundle.putString("EMAILADDRESS", nameArray[2]);
                        nBundle.putString("PAID", nameArray[3]);
                        intent.putExtras(nBundle);
                        mActivity.startActivity(intent);
                        mActivity.overridePendingTransition(R.anim.push_in_to_left,
                                R.anim.push_in_to_right);
                    }


                }
            });

            lDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor use for yes and no dialog box
     *
     * @param mMessage
     * @param nameArray
     * @param context
     */
    public DialogBox(final String[] nameArray, final Context context, final String mMessage) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_box_yes_no);
            TextView textViewMessage = (TextView) lDialog.findViewById(R.id.text_msg);
            textViewMessage.setText(mMessage);
            TextView textViewNo = (TextView) lDialog.findViewById(R.id.no_btn);
            TextView textViewYes = (TextView) lDialog.findViewById(R.id.yes_btn);
            textViewNo.setText("Cancel");


            textViewNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    if (mMessage.equalsIgnoreCase(context.getResources().getString(R.string.are_sure_uwant_logout))) {

                    } else {
                        DialogBox dialogBox = new DialogBox(context.getResources().getString(R.string.kindly_contact_private_key), context);
                    }
                }
            });

            textViewYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    mActivity = (Activity) context;
                    if (mMessage.equalsIgnoreCase(context.getResources().getString(R.string.are_sure_uwant_logout))) {
                        Utility.logout(mActivity, false);
                    } else {
                        Intent intent = new Intent(context, PerformBook.class);
                        nBundle = new Bundle();
                        nBundle.putString("USERNAME", nameArray[0]);
                        nBundle.putString("PASSWORD", nameArray[1]);
                        nBundle.putString("EMAILADDRESS", nameArray[2]);
                        intent.putExtras(nBundle);
                        mActivity.startActivity(intent);
                        mActivity.overridePendingTransition(R.anim.push_in_to_left,
                                R.anim.push_in_to_right);
                    }

                }
            });

            lDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * Cunstructor use for call message dialog
     *
     * @param mMessage
     * @param context
     */
    public DialogBox(final Context context, final String mMessage, final DialogBoxListener listener) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
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
                    if (listener != null)
                        listener.onDialogOkPressed();

                }
            });

            lDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    /**
     * Cunstructor use for call message dialog
     *
     * @param mMessage
     * @param context
     */
    public DialogBox(final Context context, final String mMessage, final DialogBoxListener listener, int status,int nothing) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
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
                   /* if (mMessage.equals("No Record.")) {
                        mActivity = (Activity) context;
                        mActivity.finish();
                    }*/

                }
            });

            lDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * Cunstructor use for call message dialog
     *
     * @param mMessage
     * @param context
     */
    public DialogBox(final Context context, final String mMessage) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
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
                   // if (mMessage.equals("Success")) {
                        mActivity = (Activity) context;
                        mActivity.finish();
                   // }

                }
            });

            lDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }



    /**
     * Cunstructor use for call message dialog
     *
     * @param mMessage
     * @param context
     */
    public DialogBox(final Context context, final String mMessage, final DialogBoxListener listener, int status) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
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
                    if (mMessage.equals("Image Successfully Saved.")) {
                        mActivity = (Activity) context;
                        mActivity.finish();
                    }

                }
            });

            lDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    /**
     * Cunstructor use for call message dialog
     *
     * @param mMessage
     * @param context
     */
    public DialogBox(final Context context, final String mMessage, final boolean isNoInternet, final DialogBoxListener listener) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialogbox_with_msg);
            TextView textViewMessage = (TextView) lDialog.findViewById(R.id.text_msg);
            TextView textViewOk = (TextView) lDialog.findViewById(R.id.ok_btn);
            textViewMessage.setText(mMessage);

            textViewOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    if (isNoInternet) {
                        mActivity = (Activity) context;
                        mActivity.finish();
                    }
                }
            });

            lDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * Cunstructor use for call message dialog
     *
     * @param mMessage
     * @param context
     */
    public DialogBox(final Context context, final String mMessage, final String[] array, String msg) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialogbox_with_msg);
            TextView textViewMessage = (TextView) lDialog.findViewById(R.id.text_msg);
            TextView textViewOk = (TextView) lDialog.findViewById(R.id.ok_btn);
            textViewMessage.setText(mMessage);

            textViewOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    mActivity = (Activity) context;

                    Intent intent = new Intent(context, PaymentDetailsClass.class);

                    nBundle = new Bundle();
                    nBundle.putString("USERID", array[0]);
                    nBundle.putString("CURRENCY", array[1]);

                    nBundle.putBoolean("REDIRECT_TO_LOGIN", true);

                    intent.putExtras(nBundle);
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.push_in_to_left,
                            R.anim.push_in_to_right);
                }
            });

            lDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    /**
     * Method use for call camera and Gallery
     *
     * @param context
     * @param listener
     */
    public DialogBox(final Activity context, final CameraListener listener) {
        try {

            mActivity = new Activity();
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dailogphotoadd);
            lDialog.show();

            Button buttonGallery = (Button) lDialog.findViewById(R.id.gallery_library);
            buttonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onGalleryClick();
                }
            });

            Button buttonCamera = (Button) lDialog.findViewById(R.id.camera);
            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(context,
                                new String[]{Manifest.permission.CAMERA},
                                Utility.MY_PERMISSION_ACCESS_CAMERA);
                        // DIALOG CLOSE SO AFTER ACCEPTING USER AGAIN OPEN THE DIALOG
                        lDialog.dismiss();
                    }else{*/
                    listener.onCameraClick();
                    lDialog.dismiss();
                    //}

                }
            });

            Button btnCloud = (Button) lDialog.findViewById(R.id.btnCloud);
            btnCloud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCloudClick();
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


    /**
     * Method use for call camera and Gallery
     *
     * @param context
     * @param listener
     */
    public DialogBox(final Context context, final ModelBankAccount modelBankAccount, final WSInterface listener) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_box_update_username);
            lDialog.show();

            final EditText etEnterAcc = (EditText) lDialog.findViewById(R.id.etEnterAcName);

            lDialog.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                }
            });

            lDialog.findViewById(R.id.tvSubmit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String username = etEnterAcc.getText().toString();
                    if (!TextUtils.isEmpty(username)) {

                        final WSInterface wsInterface = new WSInterface() {
                            @Override
                            public void onResult(int requestCode, TemplateData data) {

                                ModelUpdateAccountName modelUpdateAccountName = data.getData(ModelUpdateAccountName.class);
                                modelUpdateAccountName.setAccountNumber(modelBankAccount.getAccountNumber());
                                modelUpdateAccountName.setAccountName(username);

                                lDialog.dismiss();
                                listener.onResult(requestCode, modelUpdateAccountName);
                            }
                        };
//                    /{userid}/{bankid}/{AccountNumber}/{AccountName}”

                        ArrayListPost templatePosts = new ArrayListPost();
                        templatePosts.add("userid", modelBankAccount.getUserId());
                        templatePosts.add("bankid", modelBankAccount.getBankId() + "");
//                        templatePosts.add("AccountNumber", modelBankAccount.getAccountNumber());
                        templatePosts.add("AccountName", modelBankAccount.getAccountName());
                        templatePosts.add("NewAccountname", username);

                        WSUtils.hitService(context, Url.URL_ACC_UPDATE, templatePosts, WSMethods.POST, ModelUpdateAccountName.class, wsInterface);

                    } else {
                        new DialogBox(context, "Please fill Account Name.", (DialogBoxListener) null);
                    }

                }
            });


        } catch (Error e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Constructor use for yes and no dialog box
     *
     * @param mMessage
     * @param context
     */

    public DialogBox(final Context context, final String mMessage, final OnClickListener onClickListener, String btnText) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_box_yes_no);
            TextView textViewMessage = (TextView) lDialog.findViewById(R.id.text_msg);
            textViewMessage.setText(mMessage);
            TextView textViewNo = (TextView) lDialog.findViewById(R.id.no_btn);
            TextView textViewYes = (TextView) lDialog.findViewById(R.id.yes_btn);
            textViewNo.setText(btnText);
            textViewYes.setText("YES");


            textViewNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    onClickListener.onNegativeClick();
                }
            });

            textViewYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    onClickListener.onPositiveClick();
                }
            });

            lDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    public DialogBox(final Context context, final String mMessage, final OnClickListener onClickListener) {
        try {
            mActivity = new Activity();
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_box_yes_no);
            TextView textViewMessage = (TextView) lDialog.findViewById(R.id.text_msg);
            textViewMessage.setText(mMessage);
            TextView textViewNo = (TextView) lDialog.findViewById(R.id.no_btn);
            textViewNo.setText("NO");
            TextView textViewYes = (TextView) lDialog.findViewById(R.id.yes_btn);
            textViewYes.setText("YES");
//            textViewNo.setText("Cancel");


            textViewNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    onClickListener.onNegativeClick();
                }
            });

            textViewYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    onClickListener.onPositiveClick();
                }
            });

            lDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    public static void SendAgreement(final Activity mActivity) {
        try {

            final Dialog lDialog = new Dialog(mActivity);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dailog_forget_passowrd);
            final EditText editTextEmail = (EditText) lDialog.findViewById(R.id.editText_emailid);
            TextView textViewCancel = (TextView) lDialog.findViewById(R.id.cancel_btn);
            TextView textViewSubmit = (TextView) lDialog.findViewById(R.id.submit_btn);
            textViewCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                }
            });

            textViewSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (editTextEmail.getText().toString().isEmpty()) {
                        DialogBox dialogBox = new DialogBox(mActivity.getResources().getString(R.string.plz_fill_email_field), mActivity);
                    } else if (!Utility.isValidEmail(editTextEmail.getText().toString())) {
                        DialogBox dialogBox = new DialogBox(mActivity.getResources().getString(R.string.plz_enter_valide_email_address), mActivity);
                    } else {
                        lDialog.dismiss();
                        WSTemplate wsTemplate = new WSTemplate();
                        wsTemplate.aClass = ModelRegistration.class;
                        wsTemplate.context = mActivity;
                        wsTemplate.message_id = R.string.app_name;
                        wsTemplate.methods = WSMethods.POST;
                        wsTemplate.requestCode = 1001;

                        wsTemplate.url = Url.getSendAgreementUrl();

                        ArrayListPost templatePosts = new ArrayListPost();

                        templatePosts.add("To", editTextEmail.getText().toString());


                        Utility.printLog("edit email value", editTextEmail.getText().toString());

                        wsTemplate.templatePosts = templatePosts;
                        wsTemplate.wsInterface = new WSInterface() {
                            @Override
                            public void onResult(int requestCode, TemplateData data) {
                                switch (requestCode) {
                                    case 1001:
                                        ModelRegistration modelRegistration = data.getData(ModelRegistration.class);
                                        if (modelRegistration.getResponseMessage().equalsIgnoreCase("Success") && modelRegistration.getResponseCode().equalsIgnoreCase("1")) {
                                            DialogBox dialogBox = new DialogBox("Terms and Conditions has been sent to your emailId", mActivity);
                                        } else if (modelRegistration.getResponseCode().equalsIgnoreCase("1")) {
                                            new DialogBox(modelRegistration.getResponseMessage(), mActivity);
                                        } else {
                                            DialogBox dialogBox = new DialogBox(modelRegistration.getResponseMessage(), mActivity);
                                        }

                                        break;


                                }
                            }
                        };
                        new WSHandler(wsTemplate).execute();
                    }
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
