package com.kippinretail.CommonDialog;

/**
 * Created by Sony on 24-08-2015.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kippinretail.ApplicationuUlity.Validation;
import com.kippinretail.Interface.EmailListInterface;
import com.kippinretail.Interface.ForgotButtonListner;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.Modal.Invoice.ModalDataForAddress;
import com.kippinretail.R;
import com.kippinretail.interfaces.ButtonYesNoListner;

import java.util.ArrayList;


public class CommonDialog {


    public static ProgressDialog progressDialog;
    private Activity activity;

    /**
     * @param act
     */
    public CommonDialog(Activity act) {
        activity = act;
    }

    /**
     * @param activity
     * @return
     */
    public static CommonDialog With(Activity activity) {
        return new CommonDialog(activity);
    }

    public static void showDialog2Button(Context activity, String message, final OnDialogDismissListener onDialogDismiss) {
        try {

            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_custom_msg);


            TextView textForMessage = (TextView) dialog
                    .findViewById(R.id.text_msg);
            textForMessage.setMovementMethod(new ScrollingMovementMethod());
            textForMessage.setText(message);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

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




    public static void DialogToCreateuser(Context activity, String message, final ButtonYesNoListner buttonYesNoListner) {
        try {

            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_create_user);


            TextView textForMessage = (TextView) dialog
                    .findViewById(R.id.textForMessage);
            textForMessage.setMovementMethod(new ScrollingMovementMethod());
            textForMessage.setText(message);
            Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
            Button btnYES = (Button) dialog.findViewById(R.id.btnYES);

            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    if (buttonYesNoListner != null)
                        buttonYesNoListner.NoButton();

                }

            });

            btnYES.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    if (buttonYesNoListner != null)
                        buttonYesNoListner.YesButton();

                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DialogDeleteInvoice(Context activity, String message, final ButtonYesNoListner buttonYesNoListner) {
        try {

            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_logout_);


            TextView textForMessage = (TextView) dialog
                    .findViewById(R.id.textMessage);
            textForMessage.setMovementMethod(new ScrollingMovementMethod());
            textForMessage.setText(message);
            Button btnNo = (Button) dialog.findViewById(R.id.btnNO);
            Button btnYES = (Button) dialog.findViewById(R.id.btnYES);

            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    if (buttonYesNoListner != null)
                        buttonYesNoListner.NoButton();

                }

            });

            btnYES.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    if (buttonYesNoListner != null)
                        buttonYesNoListner.YesButton();

                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void forgotDialog(Activity activity, final ForgotButtonListner forgotButtonListner) {

        try {

            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(com.kippin.kippin.R.layout.dailog_forget_passowrd);


            final EditText editTextEmail = (EditText) dialog.findViewById(R.id.editText_emailid);
            editTextEmail.setMaxLines(1);
            editTextEmail.setSingleLine(true);
            editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }
                }
            });

            TextView textViewCancel = (TextView) dialog.findViewById(R.id.cancel_btn);
            TextView textViewSubmit = (TextView) dialog.findViewById(R.id.submit_btn);
            textViewCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    forgotButtonListner.no();
                    editTextEmail.setText("");
                }
            });
            textViewSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forgotButtonListner.yes(editTextEmail.getText().toString(), dialog, editTextEmail);
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }

    }

    /**
     * @param message
     */
    public void ShowFinish(String message) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_custom_msg);
            TextView textMessage = (TextView) dialog.findViewById(R.id.text_msg);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText(message);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    activity.finish();
                }

            });

            dialog.show();
        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage() + "", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * @param message
     */
    public void Show(String message) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_custom_msg);
            TextView textMessage = (TextView) dialog.findViewById(R.id.text_msg);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText(message);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }

            });

            dialog.show();
        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage() + "", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    public void dialogToAddEmail(final Activity activity, ArrayList<String> listForEmails1, final EmailListInterface emailListInterface) {
        try {
            final ArrayList<String> listForEmails = new ArrayList();
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_for_email);

            final EditText etEmail = (EditText) dialog.findViewById(R.id.etEmail);
            Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
            if (!listForEmails1.isEmpty()) {
                listForEmails.addAll(listForEmails1);
            }

            ImageView ivForCross = (ImageView) dialog.findViewById(R.id.ivForCross);
            final ArrayAdapter adapter = new ArrayAdapter<String>(activity, android.R.layout.activity_list_item, listForEmails);
            ListView listView = (ListView) dialog.findViewById(R.id.lvForEmails);
            listView.setAdapter(adapter);


            ivForCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Validation.isEmailAddress(etEmail, true)) {
                        listForEmails.add(etEmail.getText().toString());
                        emailListInterface.emailList(listForEmails);
                        adapter.notifyDataSetChanged();
                        etEmail.setText("");
                    }
                }

            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dialogForAddress(final Activity activity, final ModalDataForAddress modalDataForAddress, final TextView tvForCorporationAddress,
                                 final TextView tvForShippingAddress, final String customerAddress) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_for_address);

            final EditText etAptNumber = (EditText) dialog.findViewById(R.id.etAptNumber);
            final EditText etHouseNumber = (EditText) dialog.findViewById(R.id.etHouseNumber);
            final EditText etStreet = (EditText) dialog.findViewById(R.id.etStreet);
            final EditText etCity = (EditText) dialog.findViewById(R.id.etCity);
            final EditText etState = (EditText) dialog.findViewById(R.id.etState);
            final EditText etPostalCode = (EditText) dialog.findViewById(R.id.etPostalCode);

            Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
            ImageView ivForCross = (ImageView) dialog.findViewById(R.id.ivForCross);

            if (modalDataForAddress.isDialogType()) {
                etAptNumber.setText(modalDataForAddress.getCorporateAptNo());
                etHouseNumber.setText(modalDataForAddress.getCorporateHouseNo());
                etStreet.setText(modalDataForAddress.getCorporateStreet());
                etCity.setText(modalDataForAddress.getCorporateCity());
                etState.setText(modalDataForAddress.getCorporateState());
                etPostalCode.setText(modalDataForAddress.getCorporatePostalCode());
            } else {
                etAptNumber.setText(modalDataForAddress.getShippingAptNo());
                etHouseNumber.setText(modalDataForAddress.getShippingHouseNo());
                etStreet.setText(modalDataForAddress.getShippingStreet());
                etCity.setText(modalDataForAddress.getShippingCity());
                etState.setText(modalDataForAddress.getShippingState());
                etPostalCode.setText(modalDataForAddress.getShippingPostalCode());
            }

            ivForCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (modalDataForAddress.isDialogType()) {
                        modalDataForAddress.setCorporateAptNo(etAptNumber.getText().toString());
                        modalDataForAddress.setCorporateHouseNo(etHouseNumber.getText().toString());
                        modalDataForAddress.setCorporateStreet(etStreet.getText().toString());
                        modalDataForAddress.setCorporateCity(etCity.getText().toString());
                        modalDataForAddress.setCorporateState(etState.getText().toString());
                        modalDataForAddress.setCorporatePostalCode(etPostalCode.getText().toString());

                        if (modalDataForAddress.getCorporateStreet().toString().length() != 0
                                && modalDataForAddress.getCorporateCity().toString().length() != 0
                                && modalDataForAddress.getCorporateState().toString().length() != 0 && modalDataForAddress.getCorporatePostalCode().toString().length() != 0) {
                            if (modalDataForAddress.getCorporatePostalCode().toString().length() <= 8 && modalDataForAddress.getCorporatePostalCode().toString().length() >= 6) {
                                String add = modalDataForAddress.getCorporateAptNo() + "-" + modalDataForAddress.getCorporateHouseNo();
                                if (modalDataForAddress.getCorporateAptNo().length() == 0)
                                    add = add.substring(1);
                                tvForCorporationAddress.setText(customerAddress + "\n" + add + " " + modalDataForAddress.getCorporateStreet() + "\n" + modalDataForAddress.getCorporateCity()
                                        + "," + modalDataForAddress.getCorporateState() + "\n" + modalDataForAddress.getCorporatePostalCode());
                                if (modalDataForAddress.getFirstTime() == 0) {
                                    modalDataForAddress.setFirstTime(1);
                                    modalDataForAddress.setShippingAptNo(etAptNumber.getText().toString());
                                    modalDataForAddress.setShippingHouseNo(etHouseNumber.getText().toString());
                                    modalDataForAddress.setShippingStreet(etStreet.getText().toString());
                                    modalDataForAddress.setShippingCity(etCity.getText().toString());
                                    modalDataForAddress.setShippingState(etState.getText().toString());
                                    modalDataForAddress.setShippingPostalCode(etPostalCode.getText().toString());
                                    tvForShippingAddress.setText("Shipping Address:" + "\n" + add + " " + modalDataForAddress.getShippingStreet() + "\n" + modalDataForAddress.getShippingCity()
                                            + "," + modalDataForAddress.getShippingState() + "\n" + modalDataForAddress.getShippingPostalCode());
                                }
                                dialog.dismiss();
                            } else {
                                CommonDialog.With(activity).Show(activity.getResources().getString(R.string.postal_code_must_be));
                            }
                        } else {
                            CommonDialog.With(activity).Show(activity.getResources().getString(R.string.empty_fields));
                        }
                    } else {
                        modalDataForAddress.setShippingAptNo(etAptNumber.getText().toString());
                        modalDataForAddress.setShippingHouseNo(etHouseNumber.getText().toString());
                        modalDataForAddress.setShippingStreet(etStreet.getText().toString());
                        modalDataForAddress.setShippingCity(etCity.getText().toString());
                        modalDataForAddress.setShippingState(etState.getText().toString());
                        modalDataForAddress.setShippingPostalCode(etPostalCode.getText().toString());

                        if (modalDataForAddress.getShippingStreet().length() != 0 && modalDataForAddress.getShippingCity().length() != 0
                                && modalDataForAddress.getShippingState().length() != 0 && modalDataForAddress.getShippingPostalCode().length() != 0) {
                            // if (modalDataForAddress.getCorporatePostalCode().toString().length() == 8) {
                            if (modalDataForAddress.getShippingPostalCode().toString().length() <= 8 && modalDataForAddress.getShippingPostalCode().toString().length() >= 6) {
                                String add = modalDataForAddress.getShippingAptNo() + "," + modalDataForAddress.getShippingHouseNo();
                                if (modalDataForAddress.getShippingAptNo().toString().length() == 0)
                                    add = add.substring(1);
                                tvForShippingAddress.setText("Shipping Address:" + "\n" + add + " " + modalDataForAddress.getShippingStreet() + "\n" + modalDataForAddress.getShippingCity()
                                        + "," + modalDataForAddress.getShippingState() + "\n" + modalDataForAddress.getShippingPostalCode());
                                dialog.dismiss();
                            } else {
                                CommonDialog.With(activity).Show(activity.getResources().getString(R.string.postal_code_must_be));
                            }
                        } else {
                            CommonDialog.With(activity).Show(activity.getResources().getString(R.string.empty_fields));
                        }
                    }
                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

