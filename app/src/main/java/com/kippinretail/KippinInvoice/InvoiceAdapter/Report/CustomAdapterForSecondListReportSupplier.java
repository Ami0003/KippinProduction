package com.kippinretail.KippinInvoice.InvoiceAdapter.Report;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.KippinInvoice.InvoiceReportActivity;
import com.kippinretail.KippinInvoice.InvoiceStripePaymentActivity;
import com.kippinretail.KippinInvoice.ModalInvoice.ReportList;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.interfaces.ButtonYesNoListner;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.kippinretail.KippinInvoice.CommonNumberFormatter.getRound;
import static com.kippinretail.R.id.tvInvAmt;
import static com.kippinretail.R.id.tvTypeOfInv;
import static com.kippinretail.retrofit.RestClientAdavanced.getCallback;

/**
 * Created by kamaljeet.singh on 12/14/2016.
 */

public class CustomAdapterForSecondListReportSupplier extends BaseAdapter {
    private final Activity activity;
    private final List<ReportList> reportLists;
    private String amountPaid = "";
    private String amountWithJVID;
    private boolean amountToBePaid;
    private String Invoice;
    private float BalanceDue;
    private String DepositePayment;


    public CustomAdapterForSecondListReportSupplier(Activity activity, List<ReportList> reportLists) {
        this.activity = activity;
        this.reportLists = reportLists;
    }

    @Override
    public int getCount() {
        return reportLists.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //TextView tvSupplierName,tvTypeOfInv,tvInvJVID,tvPayJVID,tvInvAmt,tvPaidAmt,tvOutStandingBalance,tvInvAge,tvFlowStatus, tvStatus,tvMehtodOfPayment;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sub_second_item_list, parent, false);

            viewHolder.tvSupplierName = (TextView) convertView.findViewById(R.id.tvSupplierName);
            viewHolder.tvInvoiceNumber = (TextView) convertView.findViewById(R.id.tvInvoiceNumber);
            viewHolder.tvTypeOfInv = (TextView) convertView.findViewById(R.id.tvTypeOfInv);
            viewHolder.tvInvJVID = (TextView) convertView.findViewById(R.id.tvInvJVID);
            viewHolder.tvPayJVID = (TextView) convertView.findViewById(R.id.tvPayJVID);
            viewHolder.tvInvAmt = (TextView) convertView.findViewById(R.id.tvInvAmt);
            viewHolder.tvPaidAmt = (TextView) convertView.findViewById(R.id.tvPaidAmt);
            viewHolder.tvOutStandingBalance = (TextView) convertView.findViewById(R.id.tvOutStandingBalance);
            viewHolder.tvInvAge = (TextView) convertView.findViewById(R.id.tvInvAge);
            viewHolder.tvFlowStatus = (TextView) convertView.findViewById(R.id.tvFlowStatus);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            viewHolder.tvMehtodOfPayment = (TextView) convertView.findViewById(R.id.tvMehtodOfPayment);
            viewHolder.btnStripePayment = (Button) convertView.findViewById(R.id.btnStripePayment);
            viewHolder.btnCancel = (Button) convertView.findViewById(R.id.btnCancel);

            // EditText
            viewHolder.tvInsertAmountToBePaid = (TextView) convertView.findViewById(R.id.tvInsertAmountToBePaid);
            viewHolder.tvInsertJVID = (TextView) convertView.findViewById(R.id.tvInsertJVID);

            viewHolder.btnManualPayment = (Button) convertView.findViewById(R.id.btnManualPayment);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvSupplierName.setText(reportLists.get(position).getInvoiceDate());
        viewHolder.tvInvoiceNumber.setText(reportLists.get(position).getSupplier());
        viewHolder.tvTypeOfInv.setText(reportLists.get(position).getInvoiceTypeText());

        viewHolder.tvInvAmt.setText(reportLists.get(position).getTotal());
        try {
            if (reportLists.get(position).getManualJVID() != null) {
                viewHolder.tvInvJVID.setText(reportLists.get(position).getManualJVID());
            } else {
                viewHolder.tvInvJVID.setText("-");
            }
        } catch (Exception e) {
            viewHolder.tvInvJVID.setText("-");
        }
        try {
            if (!reportLists.get(position).getStripeJVID().toString().equals("")) {
                viewHolder.tvPayJVID.setText(reportLists.get(position).getStripeJVID());
            } else {
                viewHolder.tvPayJVID.setText("-");
            }
        } catch (Exception e) {
            viewHolder.tvPayJVID.setText("-");
        }

        if (!reportLists.get(position).getProStatus().equalsIgnoreCase("Inprogress")) {
            viewHolder.tvStatus.setText(reportLists.get(position).getProStatus());
        } else {
            viewHolder.tvStatus.setText("In Progress");
        }
        // manual Payment check
        // 0 is false   -- 1 is true

        if (!reportLists.get(position).getIsCustomer()) {
//            if (reportLists.get(position).getSupplierManualPaidAmount().equals("true") &&
//                    reportLists.get(position).getIsPaymentbyStripe().equals("true")) {
//                viewHolder.tvMehtodOfPayment.setText("Both");
//            } else if (reportLists.get(position).getSupplierManualPaidAmount().equals("false")
//                    && reportLists.get(position).getIsPaymentbyStripe().equals("true")) {
//                viewHolder.tvMehtodOfPayment.setText("Stripe");
//            } else if (reportLists.get(position).getSupplierManualPaidAmount().equals("true")
//                    && reportLists.get(position).getIsPaymentbyStripe().equals("false")) {
//                viewHolder.tvMehtodOfPayment.setText("Manual");
//            } else {
            viewHolder.tvMehtodOfPayment.setText("-");
            //    }

        } else {
            /*if (reportLists.get(position).getIsCustomerManualPaid().equals("true")
                    && reportLists.get(position).getIsPaymentbyStripe().equals("true")) {
                viewHolder.tvMehtodOfPayment.setText("Both");
            } else if (reportLists.get(position).getIsCustomerManualPaid().equals("false")
                    && reportLists.get(position).getIsPaymentbyStripe().equals("true")) {
                viewHolder.tvMehtodOfPayment.setText("Stripe");

            } else if (reportLists.get(position).getIsCustomerManualPaid().equals("true")
                    && reportLists.get(position).getIsPaymentbyStripe().equals("false"))

            {
                viewHolder.tvMehtodOfPayment.setText("Manual");

            } else {
                viewHolder.tvMehtodOfPayment.setText("-");

            }*/

            viewHolder.tvMehtodOfPayment.setText("-");

        }


        // String Pro_FlowStatus = reportLists.get(position).getProFlowStatus();
        String Pro_Status = reportLists.get(position).getProStatus();
//        if (Pro_FlowStatus.equals("Pending Approval")) {
//            viewHolder.tvFlowStatus.setText("Sent");
//        } else if (Pro_Status.equals("Accepted")) {

        viewHolder.tvFlowStatus.setText(reportLists.get(position).getFlowStatus());


        if (!Pro_Status.equals("Paid")) {
            String dueDate = reportLists.get(position).getDueDate();
            String todayDate = getTodaydDate();
            // viewHolder.tvInvAge.setText(reportLists.get(position).getInvoiceAge());
            // getDateDiffrence(dueDate, todayDate, viewHolder);
        } else {
            String dueDate = reportLists.get(position).getDueDate();
            String paymentDate = reportLists.get(position).getPaymentDate();

        }

        //   Manual Button & edittext Fields Visibility check
        if (Pro_Status.equals("Accepted")) {
            if (Float.parseFloat(reportLists.get(position).getBalanceDue()) == 0.0) {
                GONE(viewHolder, position);

            } else if (Float.parseFloat(reportLists.get(position).getDepositPayment().toString()) > 0.0) {
                VISIBLE(viewHolder, position);
            } else {
                VISIBLE(viewHolder, position);
            }
        } else {
            GONE(viewHolder, position);
        }

        // Stripe Payment button check
        if (!reportLists.get(position).getIsCustomer()) {
            //contentCell.contentButton.hidden            = true;
            viewHolder.btnStripePayment.setVisibility(View.INVISIBLE);
        } else {
            if (Pro_Status.equals("Accepted")) {
                if (reportLists.get(position).getIsStripe().equals("true")) {
                    if (Float.parseFloat(reportLists.get(position).getBalanceDue()) == 0.0) {
                        //  contentCell.contentButton.hidden = true;
                        viewHolder.btnStripePayment.setVisibility(View.INVISIBLE);
                    } else {
                        viewHolder.btnStripePayment.setVisibility(View.VISIBLE);
                    }
                } else {
                    viewHolder.btnStripePayment.setVisibility(View.INVISIBLE);
                }
            } else {
                viewHolder.btnStripePayment.setVisibility(View.INVISIBLE);
            }
        }


        viewHolder.tvInvAge.setText("" + reportLists.get(position).getInvoiceAge());
        viewHolder.tvPaidAmt.setText(reportLists.get(position).getPaidAmountText());
       /* if (reportLists.get(position).getDepositPayment() == 0.0000) {
            viewHolder.tvPaidAmt.setText("0.00");
        } else {
            viewHolder.tvPaidAmt.setText(reportLists.get(position).getPaidAmountText());
        }*/

        viewHolder.tvOutStandingBalance.setText(getRound(Float.parseFloat(reportLists.get(position).getBalanceDue())));
        viewHolder.btnManualPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAmount(position);
               /* if (!amountPaid.equals("")) {
                    DoManualPayment(position);
                } else {
                    CommonDialog.With(activity).Show("Please enter Amount to be Paid.");
                }*/
            }
        });
        viewHolder.tvInsertAmountToBePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountToBePaid = true;
                addAmount(position, viewHolder);
            }
        });
        // Insert JVID
        viewHolder.tvInsertJVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountToBePaid = false;
                addAmount(position, viewHolder);

            }
        });
        // ------- Stripe Payment -----
        viewHolder.btnStripePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.e("amountPaid:",""+amountPaid);
               // Log.e("amountWithJVID:",""+amountWithJVID);
               // Log.e("",""+reportLists.get(position).getBalanceDue());

              //  if (amountPaid.length() != 0) {
                    Intent in = new Intent();
                    in.setClass(activity, InvoiceStripePaymentActivity.class);
                    in.putExtra("InvoiceId", reportLists.get(position).getId());
                    in.putExtra("BalanceAmount", reportLists.get(position).getBalanceDue());
                    in.putExtra("AmountToBePaid", amountPaid);
                    in.putExtra("JVID", amountWithJVID);
                    in.putExtra("SupplierId", reportLists.get(position).getUserId());
                    activity.startActivity(in);
                    activity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
               // }
            }
        });

        viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMessage("Are you sure you want cancel this Invoice",position);

            }
        });

        return convertView;
    }

    // Cancel Invoice

    private void cancelInvoice(int position) {

        if (reportLists.get(position).getType() == 1)
            Invoice = "1";
        else Invoice = "2";

        HashMap templatePosts = new HashMap();
        templatePosts.put("Id", "" + reportLists.get(position).getId());
        templatePosts.put("ButtonType", "Delete");
        templatePosts.put("SectionType", "Received");
        templatePosts.put("Type", Invoice);


        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().UpdateInvoice(((InvoiceReportActivity) activity).getTypedInput(new Gson().toJson(templatePosts)), getCallback(new Callback<JsonElement>() {

            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Tag", "Request data " + new Gson().toJson(jsonElement));

                try {
                    JSONObject jsonObject = new JSONObject(jsonElement.toString());
                    String ResponseMessage = jsonObject.getString("ResponseMessage");
                    String ResponseCode = jsonObject.getString("ResponseCode");
                    String UserId = jsonObject.getString("UserId");

                    if (ResponseCode.equals("1")) {
                        CommonDialog.showDialog2Button(activity, "Invoice cancelled successfully", new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                activity.finish();
                                activity.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                            }
                        });
                    } else {
                        CommonDialog.With(activity).Show(ResponseMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(activity, error);
            }
        }));


    }

    private void getDateDiffrence(String dueDate, String todayDate, ViewHolder viewHolder) {
        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = dates.parse(dueDate);
            Date date2 = dates.parse(todayDate);
            //long difference = Math.abs(date2.getTime() - date1.getTime());
            long difference = date1.getTime() - date2.getTime();
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            String dayDifference = Long.toString(differenceDates);
            viewHolder.tvInvAge.setText(dayDifference);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private String getTodaydDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void VISIBLE(ViewHolder viewHolder, int position) {
        viewHolder.btnManualPayment.setVisibility(View.VISIBLE);
        viewHolder.tvInsertAmountToBePaid.setVisibility(View.GONE);
        viewHolder.tvInsertJVID.setVisibility(View.GONE);
        //viewHolder.btnStripePayment.setVisibility(View.VISIBLE);
       /* if (reportLists.get(position).getIsCustomer()) {
            viewHolder.btnCancel.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnCancel.setVisibility(View.GONE);
        }*/

        if ((!reportLists.get(position).getProStatus().equals("Accepted")) ||
                (reportLists.get(position).getProStatus().equals("Accepted") &&
                        reportLists.get(position).getPaidAmountText().equals("0.00") ||
                        reportLists.get(position).getPaidAmountText().equals("0") ||
                        reportLists.get(position).getPaidAmountText().equals("0.0000") ||
                        reportLists.get(position).getPaidAmountText().equals("0.000"))) {
            viewHolder.btnCancel.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnCancel.setVisibility(View.GONE);
        }

        //    iscustomer == 1 and status == accepted

       /* if (reportLists.get(position).getIsStripe().equalsIgnoreCase("true")) {
            viewHolder.btnStripePayment.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnStripePayment.setVisibility(View.GONE);
        }*/

    }

    private void GONE(ViewHolder viewHolder, int position) {
        viewHolder.btnManualPayment.setVisibility(View.GONE);
        viewHolder.tvInsertAmountToBePaid.setVisibility(View.GONE);
        viewHolder.tvInsertJVID.setVisibility(View.GONE);
      /*  if (reportLists.get(position).getIsCustomer().equals("true")) {
            viewHolder.btnCancel.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnCancel.setVisibility(View.GONE);
        }*/
        viewHolder.btnCancel.setVisibility(View.GONE);

    }


    private void addAmount(final int position, final ViewHolder viewHolder) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.add_amount_dialog);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            final EditText etEnterAmount = (EditText) dialog.findViewById(R.id.etEnterAmount);
            if (!amountToBePaid) {
                etEnterAmount.setHint("Enter JVID");
            }
            Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
            ImageView ivForCross = (ImageView) dialog.findViewById(R.id.ivForCross);
            ivForCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etEnterAmount.getText().toString().length() != 0) {
                        if (amountToBePaid) {
                            if (Float.parseFloat(reportLists.get(position).getBalanceDue()) >= Float.parseFloat(etEnterAmount.getText().toString())) {
                                amountPaid = etEnterAmount.getText().toString();
                                viewHolder.tvInsertAmountToBePaid.setText(amountPaid);
                                notifyDataSetChanged();
                            } else {
                                CommonDialog.With(activity).Show("Amount must be less than or equal to Balance amount");
                            }
                        } else {
                            // if (Float.parseFloat(reportLists.get(position).getBalanceDue()) >= Float.parseFloat(etEnterAmount.getText().toString())) {
                            amountWithJVID = etEnterAmount.getText().toString();
                            viewHolder.tvInsertJVID.setText(amountWithJVID);
                            notifyDataSetChanged();
                           /* } else {
                                CommonDialog.With(activity).Show("Amount must be less than or equal to Balance amount");
                            }*/
                        }
                        dialog.dismiss();

                    } else {
                        //CommonDialog.With(activity).Show("Please enter amount");
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void addNewAmount(final int position) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.add_new_amount_dialog);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            final EditText etEnterAmount = (EditText) dialog.findViewById(R.id.etEnterAmount);
            final EditText etEnterJVID = (EditText) dialog.findViewById(R.id.etEnterJVID);
            /*if (!amountToBePaid) {
                etEnterAmount.setHint("Enter JVID");
            }*/
            Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
            ImageView ivForCross = (ImageView) dialog.findViewById(R.id.ivForCross);
            ivForCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (etEnterAmount.getText().toString().length() != 0 /*&& etEnterJVID.getText().toString().length() != 0*/) {
                        ////  if (amountToBePaid) {
                        if (Float.parseFloat(reportLists.get(position).getBalanceDue()) >= Float.parseFloat(etEnterAmount.getText().toString())) {
                            amountPaid = etEnterAmount.getText().toString();
                            amountWithJVID = etEnterJVID.getText().toString();
                            //  viewHolder.tvInsertAmountToBePaid.setText(amountPaid);
                            //  notifyDataSetChanged();
                            DoManualPayment(position);
                        } else {
                            CommonDialog.With(activity).Show("Amount must be less than or equal to Balance amount");
                        }
                        // } else {
                        // if (Float.parseFloat(reportLists.get(position).getBalanceDue()) >= Float.parseFloat(etEnterAmount.getText().toString())) {
//                            amountWithJVID = etEnterJVID.getText().toString();
//                            viewHolder.tvInsertJVID.setText(amountWithJVID);
//                            notifyDataSetChanged();
                           /* } else {
                                CommonDialog.With(activity).Show("Amount must be less than or equal to Balance amount");
                            }*/
                        //  }
                        dialog.dismiss();

                    } else {
                        CommonDialog.With(activity).Show("Please enter amount.");
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void DoManualPayment(int position) {

        HashMap templatePosts = new HashMap();


        templatePosts.put("InvoiceId", "" + reportLists.get(position).getId());
        templatePosts.put("IsCustomer", reportLists.get(position).getIsCustomer());

        if (reportLists.get(position).getIsCustomer()) {
            templatePosts.put("CustomerManualPaidAmount", amountPaid);
            templatePosts.put("CustomerManualPaidJVID", amountWithJVID);
        } else {
            templatePosts.put("SupplierManualPaidAmount", amountPaid);
            templatePosts.put("SupplierManualPaidJVID", amountWithJVID);
        }

        LoadingBox.showLoadingDialog(activity, "");
        RestClient.getApiFinanceServiceForPojo().ManualPayment(((SuperActivity) activity).getTypedInput(new Gson().toJson(templatePosts)), getCallback(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("Tag", "Request data " + new Gson().toJson(jsonElement));

                try {
                    JSONObject jsonObject = new JSONObject(jsonElement.toString());
                    String ResponseMessage = jsonObject.getString("ResponseMessage");
                    String ResponseCode = jsonObject.getString("ResponseCode");

                    if (ResponseCode.equals("1")) {
                        CommonDialog.showDialog2Button(activity, "Manual Payment Done Successfully", new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                CommonUtility.HomeClick(activity);
                            }
                        });
                    } else {
                        CommonDialog.With(activity).Show(ResponseMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(activity, error);
            }
        }));


    }


    class ViewHolder {
        TextView tvSupplierName, tvInvoiceNumber, tvTypeOfInv, tvInvJVID, tvPayJVID, tvInvAmt, tvPaidAmt, tvOutStandingBalance, tvInvAge, tvFlowStatus,
                tvStatus, tvMehtodOfPayment, tvInsertAmountToBePaid, tvInsertJVID;
        Button btnManualPayment, btnStripePayment, btnCancel;
    }


    private void showDialogMessage(String s,final int pos) {

        CommonDialog.DialogDeleteInvoice(activity, s, new ButtonYesNoListner() {
            @Override
            public void YesButton() {
                cancelInvoice(pos);
            }

            @Override
            public void NoButton() {

            }
        });


    }

}
