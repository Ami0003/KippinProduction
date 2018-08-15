package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ResponseForGiftAnalytics;
import com.kippinretail.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sandeep.singh on 7/19/2016.
 */
public class Analytic_GiftAdapter extends BaseAdapter {
    List<ResponseForGiftAnalytics> listData;
    Activity mContext ;
    LayoutInflater inflater;
    int hei;
    int wid;
    public Analytic_GiftAdapter(List<ResponseForGiftAnalytics> listData , Activity activity){
        this.listData = listData;
        this.mContext = activity;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wid = CommonUtility.dpToPx(mContext, 250);
        hei = CommonUtility.dpToPx(mContext,100);
    }
    @Override
    public int getCount() {
        if(listData!=null){
            return listData.size()+1;
        }
        else{
            return  0;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = (View)inflater.inflate(R.layout.subitem_giftcard_analytic, parent, false);
            holder.txt_giftCardCount =(TextView) convertView.findViewById(R.id.txt_giftCardCount);
            holder.txt_country =(TextView) convertView.findViewById(R.id.txt_country);
            holder.txt_count =(TextView) convertView.findViewById(R.id.txt_count);
            holder.iv_giftCardTemplate =(ImageView) convertView.findViewById(R.id.iv_giftCardTemplate);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
            holder.txt_giftCardCount.setText("");
        }
        if(position==0){

        }else{
            ResponseForGiftAnalytics response = listData.get(position-1);
            if(response.getTemplatePath()!=null && !response.getTemplatePath().equals("")){
                Picasso.with(mContext).load(response.getTemplatePath()).resize(wid,hei).into(holder.iv_giftCardTemplate);
            }
            else{
                Picasso.with(mContext).load(response.getTemplatePath()).placeholder(R.drawable.gift_bg).error(R.drawable.gift_bg).resize(wid, hei).into(holder.iv_giftCardTemplate);
            }
            holder.txt_giftCardCount.setText(response.getGiftcardPrice());
            holder.txt_count.setText(response.getGiftcardCount());
            holder.txt_country.setText(response.getLocation());
        }

        return convertView;
    }
    class ViewHolder{
        ImageView iv_giftCardTemplate;
        TextView txt_giftCardCount,txt_country,txt_count;
    }
}
