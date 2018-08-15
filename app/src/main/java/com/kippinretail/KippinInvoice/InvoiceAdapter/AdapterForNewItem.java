package com.kippinretail.KippinInvoice.InvoiceAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.KippinInvoice.CreateInvoiceActivity;
import com.kippinretail.KippinInvoice.InvoiceInterface.InterfaceUpdate;
import com.kippinretail.KippinInvoice.ItemDetailActivity;
import com.kippinretail.KippinInvoice.ModalInvoice.ItemModal;
import com.kippinretail.R;

import java.util.List;

/**
 * Created by kamaljeet.singh on 11/23/2016.
 */

public class AdapterForNewItem extends BaseAdapter {


    private final LayoutInflater mLayoutInflater;
    private final InterfaceUpdate interfaceUpdate;
    Holder holder;
    private List<ItemModal> mlistForNewItem;
    private CreateInvoiceActivity mContext;


    private TextView tvClicked = null;
    private TextView tvClicked1 = null;


    public AdapterForNewItem(CreateInvoiceActivity createInvoiceActivity, List<ItemModal> listForNewItem, InterfaceUpdate interfaceUpdate) {
        mContext = createInvoiceActivity;
        mlistForNewItem = listForNewItem;
        this.interfaceUpdate = interfaceUpdate;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mlistForNewItem.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(
                    R.layout.sub_item_create_invoice, null);
            holder = new Holder();

            holder.label = (TextView) convertView.findViewById(R.id.label);
            holder.label1 = (TextView) convertView.findViewById(R.id.label1);
            holder.ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
            holder.ivEdit = (ImageView) convertView.findViewById(R.id.ivEdit);

            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.label.setText(mlistForNewItem.get(position).getItem());
        if (mlistForNewItem.get(position).getServiceProductType() != null && !mlistForNewItem.get(position).getServiceProductType().equals("")) {
            holder.label1.setText(mlistForNewItem.get(position).getServiceProductType());
        } else {
            holder.label1.setText(mlistForNewItem.get(position).getDescription());
        }

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(mContext, ItemDetailActivity.class);
                in.putExtra("DataDetails", new Gson().toJson(mlistForNewItem.get(position)));
                in.putExtra("position", "" + position);
                tvClicked = (TextView) holder.label;
                tvClicked1 = (TextView) holder.label1;
                if(CommonUtility.IsFinance!=null){
                    if(CommonUtility.IsFinance.equals("true")){

                    }
                }
                else{
                    CommonUtility.IsFinance="true";
                }
               // CommonUtility.IsFinance = "true";
                mContext.startActivityForResult(in, 1);

                mContext.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistForNewItem.size() > 1) {
                    mlistForNewItem.remove(position);
                    int x = 0;
                    for (x = 0; x < mlistForNewItem.size(); x++) {
                        mlistForNewItem.get(x).setId("" + (x + 1));
                        mlistForNewItem.get(x).setItem(String.format("%02d", (x + 1)));
                    }
                    notifyDataSetChanged();

                    interfaceUpdate.updateValues(x);
                } else {
                    CommonDialog.With(mContext).Show("You must have atleast 1 ITEM in Invoice.");
                }
            }
        });
        return convertView;
    }

    public void updateValues(int position, ItemModal itemModal) {
        mlistForNewItem.set(position, itemModal);
        notifyDataSetChanged();
    }


    class Holder {

        TextView label, label1;
        ImageView ivDelete, ivEdit;
        int pos;

    }
}
