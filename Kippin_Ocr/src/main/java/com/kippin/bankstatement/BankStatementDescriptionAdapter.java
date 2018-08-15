package com.kippin.bankstatement;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippin.app.Kippin;
import com.kippin.kippin.R;
import com.kippin.selectdate.ModelBankStatement;
import com.kippin.utils.ImagePickUtility.CapturePicView;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;

import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 1/27/2016.
 */
public class BankStatementDescriptionAdapter extends BaseAdapter {

    private Activity context;
    private String status_id;
    private BankStatementListener bankStatementListener;
    private final int REQUESTCODE_CLOUD = 1001;
    private ArrayList<ModelBankStatement> modelBankStatements_;

    public BankStatementDescriptionAdapter(Activity activityBankStatement, String status_id, ArrayList<ModelBankStatement> modelBankStatements_, BankStatementListener bankStatementListener) {
        this.context = activityBankStatement;
        this.status_id = status_id;
        this.bankStatementListener = bankStatementListener;
        this.modelBankStatements_ = modelBankStatements_;
    }

    @Override
    public int getCount() {
        return modelBankStatements_.size();
    }

    /**
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return modelBankStatements_.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    ViewHolder viewHolder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ModelBankStatement statement = modelBankStatements_.get(position);

        if (!status_id.equalsIgnoreCase(ActivityBankStatement.statuses_ids[0]) && !statement.getStatusId().equalsIgnoreCase(status_id)) {
            return new View(context);
        }

//        if(convertView==null || (ViewHolder) convertView.getTag()==null){

        if (true) {
            viewHolder = new ViewHolder();
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_bank_statement_detail, parent, false);

//            viewHolder .tvDesc = (TextView)convertView.findViewById(R.id.tvDescription);
            viewHolder.tvCredits = (TextView) convertView.findViewById(R.id.tvCredits);
            viewHolder.tvDebit = (TextView) convertView.findViewById(R.id.tvDebits);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            viewHolder.iv_star = (ImageView) convertView.findViewById(R.id.iv_star);
            viewHolder.ivEdit = (ImageView) convertView.findViewById(R.id.ivEdit);
//            viewHolder .tvAmount= (TextView)convertView.findViewById(R.id.tvAmount);
            viewHolder.tvClassification = (TextView) convertView.findViewById(R.id.tvClassification);
            viewHolder.ivView = (ImageView) convertView.findViewById(R.id.ivView);
            viewHolder.ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor((context.getResources().getColor(R.color.bank_statement_even_items)));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }


        viewHolder.ivView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proceedIfNotAllowed(statement.getStatusId(), BillOption.edit)) {

                    bankStatementListener.onViewClick(position);
                }
            }
        });


//        viewHolder.tvDesc.setText(statement.getDescription());
        viewHolder.tvCredits.setText(Utility.parseDouble(statement.getCredit()));
        viewHolder.tvDebit.setText(Utility.parseDouble(statement.getDebit()));

        viewHolder.tvStatus.setText(statement.getStatus());
        Log.e("VALUE:", "" + statement.isIsNewBillAttached());
        if (statement.isIsNewBillAttached()) {
            viewHolder.iv_star.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_star.setVisibility(View.GONE);
        }

//        viewHolder.tvAmount.setText(statement.getTotal()+"");

        if (statement.getClassificationId().equalsIgnoreCase(ActivityBankStatement.BLANK_CLASSIFICATION_ID)) {
            viewHolder.tvClassification.setText(Kippin.res.getText(R.string.blank_classification_msg));
        } else
            viewHolder.tvClassification.setText(statement.getClassificationType() + "");


        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proceedIfNotAllowed(statement.getStatusId(), BillOption.upload)) {
                    String input = Utility.getTimeParsed(statement.getDate());
                    String[] out = input.split("-");
                    // System.out.println("Month = " + out[0]);
                    Log.e("DATE: ", "" + out[0]);
                    Log.e("DATE: ", "" + out[1]);
                    CapturePicView.monthString = out[1];
                    CapturePicView.yearString = out[0];
                    bankStatementListener.onEditClick(position);
                }
            }
        });


        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proceedIfNotAllowed(statement.getStatusId(), BillOption.delete)) {
                    bankStatementListener.onDeleteClick(position);
                }
            }
        });

        return convertView;
    }


    class ViewHolder {
        TextView /*tvDesc,*/tvCredits, tvStatus, tvDebit, tvAmount, tvClassification;
        ImageView ivView, ivEdit, ivDelete, iv_star;
    }


    enum BillOption {
        upload, edit, delete
    }


    private boolean proceedIfNotAllowed(String status_id, BillOption billOption) {

        if (!status_id.equalsIgnoreCase(ActivityBankStatement.statuses_ids[4])
//                &&
//                !status_id.equalsIgnoreCase(ActivityBankStatement.statuses_ids[2])
                ) {
            return true;
        }
        if (billOption == BillOption.edit) {
            //new DialogBox(context, "You can't edit statement until it is in process.",(DialogBoxListener)null);
            new DialogBox(context, "Item locked by accountant… please contact accountant", (DialogBoxListener) null);
        } else if (billOption == BillOption.upload) {
            //new DialogBox(context, "You can't upload any bill until it is in process.",(DialogBoxListener)null);
            new DialogBox(context, "Item locked by accountant… please contact accountant", (DialogBoxListener) null);
        } else if (billOption == BillOption.delete) {
            // new DialogBox(context, "You can't Delete statement until it is in process.",(DialogBoxListener)null);
            new DialogBox(context, "Item locked by accountant… please contact accountant", (DialogBoxListener) null);
        }
//        if(status_id.equalsIgnoreCase(ActivityBankStatement.statuses_ids[4])  ){

//        }

        return false;
    }

}
