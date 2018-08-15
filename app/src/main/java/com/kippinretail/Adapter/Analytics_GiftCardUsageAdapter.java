package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ResponseForGiftCardUsageAnalytics;
import com.kippinretail.R;

import java.util.List;

/**
 * Created by sandeep.singh on 7/19/2016.
 */
public class Analytics_GiftCardUsageAdapter extends BaseAdapter {
    List<ResponseForGiftCardUsageAnalytics> listData;
    Activity mContext;
    LayoutInflater inflater;
    int hei;
    int wid;

    public Analytics_GiftCardUsageAdapter(List<ResponseForGiftCardUsageAnalytics> listData, Activity activity) {
        this.listData = listData;
        this.mContext = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wid = CommonUtility.dpToPx(mContext, 45);
        hei = CommonUtility.dpToPx(mContext, 35);
    }

    @Override
    public int getCount() {
        if (listData != null) {
            return listData.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    View lastView;
    int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = (View) inflater.inflate(R.layout.subitem_giftcard_usage, parent, false);
            holder.txt_col1 = (TextView) convertView.findViewById(R.id.txt_col1);
            holder.txt_col2 = (TextView) convertView.findViewById(R.id.txt_col2);
            convertView.setTag(holder);
        } else {
           /* holder = (ViewHolder)convertView.getTag();
            holder.txt_giftCardCount.setText("");*/
            holder.txt_col1.setText("");
            holder.txt_col2.setText("");
        }
        /*ResponseForGiftCardUsageAnalytics response = listData.get(position);
        if(response.getTemplatePath()!=null && !response.getTemplatePath().equals("")){
            Picasso.with(mContext).load(response.getTemplatePath()).resize(wid,hei).into(holder.iv_giftCardTemplate);
        }
        else{
            Picasso.with(mContext).load(response.getTemplatePath()).placeholder(R.drawable.gift_bg).error(R.drawable.gift_bg).resize(wid, hei).into(holder.iv_giftCardTemplate);
        }*/

        holder.txt_col1.setText(listData.get(position).getCountry());
        holder.txt_col2.setText(listData.get(position).getSymbol() + " " + listData.get(position).getAmount());


        final View finalView = convertView;
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (lastView != null && lastPosition != position) {
                    lastView.setBackgroundColor(Color.WHITE);
                }

                if (lastPosition != position) {
                    lastPosition = position;
                    lastView = finalView;
                    finalView.setBackgroundColor(Color.LTGRAY);
                }

                return false;
            }
        });


        if (lastPosition == position) {
            convertView.setBackgroundColor(Color.LTGRAY);
        } else if (lastPosition != position && lastPosition >= 0) {
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    class ViewHolder {

        TextView txt_col1, txt_col2;
    }
}
