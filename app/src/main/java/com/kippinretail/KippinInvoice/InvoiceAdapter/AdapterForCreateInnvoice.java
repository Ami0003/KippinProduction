package com.kippinretail.KippinInvoice.InvoiceAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.KippinInvoice.CreateInvoiceActivity;
import com.kippinretail.KippinInvoice.InnvoiceCustomerCreate;
import com.kippinretail.Modal.Invoice.CustomerList;
import com.kippinretail.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Codewix Dev on 6/21/2017.
 */

public class AdapterForCreateInnvoice extends BaseAdapter {


    private final List<CustomerList> mCustomerLists;
    private final LayoutInflater mLayoutInflater;
    private final String roleId;
    AdapterForCreateInnvoice.Holder holder;
    private InnvoiceCustomerCreate mContext;
    private ArrayList<CustomerList> arraylist;

    public AdapterForCreateInnvoice(InnvoiceCustomerCreate invoiceCustomerList, List<CustomerList> customerLists, String roleId) {
        mContext = invoiceCustomerList;
        mCustomerLists = customerLists;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arraylist = new ArrayList<CustomerList>();
        this.arraylist.addAll(customerLists);
        this.roleId = roleId;
    }

    @Override
    public int getCount() {
        return mCustomerLists.size();
    }

    @Override
    public CustomerList getItem(int position) {
        return mCustomerLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(
                    R.layout.row_list_item, null);
            holder = new AdapterForCreateInnvoice.Holder();

            holder.label = (TextView) convertView.findViewById(R.id.label);

            convertView.setTag(holder);


        } else {
            holder = (AdapterForCreateInnvoice.Holder) convertView.getTag();
        }

        holder.label.setText(mCustomerLists.get(position).getCompanyName());

        // Listen for ListView Item Click
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

             //   if (roleId == null) {
                    Intent in = new Intent();
                    in.setClass(mContext, CreateInvoiceActivity.class);
                    in.putExtra("CustomerId", "" + mCustomerLists.get(position).getCustomerUserMappingId());
                    CommonUtility.IsFinance = ""+mCustomerLists.get(position).getIsFinanceUser();
                    mContext.startActivity(in);
                    mContext.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
//                } else {
//                    Intent in = new Intent();
//                    in.setClass(mContext, EditCustomerActivity.class);
//                    in.putExtra("CustomerData", new Gson().toJson(mCustomerLists.get(position)));
//                    in.putExtra("roleId",roleId);
//                    CommonUtility.IsFinance = mCustomerLists.get(position).getIsFinance();
//                    mContext.startActivityForResult(in, 1);
//                    mContext.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
//                }

            }
        });

        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase();
        mCustomerLists.clear();
        if (charText.length() == 0) {
            mCustomerLists.addAll(arraylist);
        } else {
            if(arraylist.size()!=0) {
                for (CustomerList wp : arraylist) {
                    if (wp.getCompanyName().toLowerCase().contains(charText)) {
                        mCustomerLists.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    class Holder {

        TextView label;
        int pos;

    }
}
