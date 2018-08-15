package com.kippinretail.KippinInvoice.InvoiceAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.KippinInvoice.ItemDetailActivity;
import com.kippinretail.KippinInvoice.ItemDetailRejected;
import com.kippinretail.KippinInvoice.ModalInvoice.ModalForIndutry;
import com.kippinretail.KippinInvoice.ModalInvoice.Revenue;
import com.kippinretail.R;
import com.library_for_stickylist.StickyListHeadersAdapter;

import java.util.List;


/**
 * Created by kamaljeet.singh on 11/24/2016.
 */

public class GoodsAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private final int flag;
    private final LayoutInflater mLayoutInflater;
    Holder holder;
    private List<Revenue> revenueList;
    private List<ModalForIndutry> mModalForIndustry;
    private Activity mContext;
    boolean reject = false;

    public GoodsAdapter(Activity activity, List<ModalForIndutry> mModalForIndustry, int flag) {
        mContext = activity;
        this.mModalForIndustry = mModalForIndustry;
        this.flag = flag;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public GoodsAdapter(ItemDetailActivity activity, List<Revenue> revenueList, int flag) {
        mContext = activity;
        this.revenueList = revenueList;
        this.flag = flag;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public GoodsAdapter(ItemDetailRejected activity, List<Revenue> revenueList, int flag, boolean reject) {
        mContext = activity;
        this.revenueList = revenueList;
        this.reject = reject;
        this.flag = flag;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public GoodsAdapter(Activity activity, List<Revenue> revenueList, int flag, int i) {
        mContext = activity;
        this.revenueList = revenueList;
        this.flag = flag;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if (flag == 0) {
            return mModalForIndustry.size();
        } else {
            return revenueList.size();
        }

    }

    @Override
    public Object getItem(int position) {
        if (flag == 0) {
            return mModalForIndustry.size();
        } else {
            return revenueList.size();
        }
    }

    @Override
    public long getItemId(int position) {
        if (flag == 0) {
            return mModalForIndustry.indexOf(getItem(position));
        } else {
            return revenueList.indexOf(getItem(position));

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(
                    R.layout.row_list_item, null);
            holder = new Holder();

            holder.label = (TextView) convertView.findViewById(R.id.label);

            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }
        if (flag == 0) {
            holder.label.setText(mModalForIndustry.get(position).getIndustryName());
        } else {
            Log.e("reject:", "" + reject);
            holder.label.setText(revenueList.get(position).getClassificationType());
           /* if (reject) {
                Log.e("IDDDD:", "" + revenueList.get(position).getId());
                if (revenueList.get(position).getId() != null)
                    if (revenueList.get(position).getId() == ItemDetailRejected.servicetypeValue) {
                        holder.label.setText(revenueList.get(position).getClassificationType());
                    }
            } else {
                holder.label.setText(revenueList.get(position).getClassificationType());
            }*/

            /*else{

            }*/

        }

        return convertView;
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_header, parent, false);
            holder = new HeaderViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }


        holder.header.setText(revenueList.get(position).getCategoryId() == 1 ? "ASSET" : "EXPENSE");


        //set header text based on first letter
        //String headerText = "" + friends.get(position).subSequence(0, 1).charAt(0);
        // holder.header.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return revenueList.get(position).getCategoryId();
    }

    class Holder {

        TextView label;

    }

    private class HeaderViewHolder {
        private TextView header;

        public HeaderViewHolder(View v) {
            header = (TextView) v.findViewById(R.id.header_text);
        }
    }
}
