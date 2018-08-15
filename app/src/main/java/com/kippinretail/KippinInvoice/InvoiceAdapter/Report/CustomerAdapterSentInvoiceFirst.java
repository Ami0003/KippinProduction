package com.kippinretail.KippinInvoice.InvoiceAdapter.Report;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippinretail.KippinInvoice.ModalInvoice.ReceivedInvoiceList;
import com.kippinretail.R;

import java.util.List;

/**
 * Created by Codewix Dev on 6/16/2017.
 */

public class CustomerAdapterSentInvoiceFirst extends BaseAdapter {
    private final Activity activity;
    private final List<ReceivedInvoiceList> receivedLists;
    CustomerAdapterSentInvoiceFirst.ViewHolder viewHolder;
    private String invoiceNumber;

    public CustomerAdapterSentInvoiceFirst(Activity activity, List<ReceivedInvoiceList> receivedLists) {
        this.activity = activity;
        this.receivedLists = receivedLists;
    }

    @Override
    public int getCount() {
        return receivedLists.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (true) {
            viewHolder = new CustomerAdapterSentInvoiceFirst.ViewHolder();
            convertView = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_sent_first, parent, false);

            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.tvDescription);

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (CustomerAdapterSentInvoiceFirst.ViewHolder) convertView.getTag();
        }
          viewHolder.tvDate.setText(receivedLists.get(position).getInvoiceDate());
        int invoiceNumber1 = Integer.parseInt(receivedLists.get(position).getInvoiceNumber());
        invoiceNumber = String.format("%05d", invoiceNumber1);

        viewHolder.tvDesc.setText(invoiceNumber);
        return convertView;
    }

    class ViewHolder {
        TextView tvDate;
        TextView tvDesc;
    }
}
