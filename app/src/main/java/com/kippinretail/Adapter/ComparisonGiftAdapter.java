package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.Modal.ModelToComparePrize.GiftCardDetailWithCountry;
import com.kippinretail.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sandeep.singh on 8/22/2016.
 */
public class ComparisonGiftAdapter extends BaseAdapter {

    List<GiftCardDetailWithCountry> listData;
    Activity mContext ;
    LayoutInflater inflater;


    public ComparisonGiftAdapter(List<GiftCardDetailWithCountry> listData , Activity activity){
        this.listData = listData;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = activity;

    }

    @Override
    public int getCount() {
        if(listData!=null){
            return listData.size();
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

    View lastView;
    int lastPosition = -1;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = (View)inflater.inflate(R.layout.subitem_compare,parent,false);
            holder.txt_giftCardCount =(TextView) convertView.findViewById(R.id.txt_giftCardCount);
            holder.txt_country =(TextView) convertView.findViewById(R.id.txt_country);
            holder.iv_gift =(ImageView) convertView.findViewById(R.id.iv_gift);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
            holder.txt_giftCardCount.setText("");
            holder.txt_country.setText("");

        }
        GiftCardDetailWithCountry detail = listData.get(position);
        holder.txt_country.setText(detail.getCountry());
        holder.txt_giftCardCount.setText("Gift Card Count : "+detail.getGiftcardCount());
        if(detail.getTemplatePath()!=null && !detail.getTemplatePath().equals("")){
            Picasso.with(mContext).load(detail.getTemplatePath()).into(holder.iv_gift);
        }else{
            Picasso.with(mContext).load(detail.getTemplatePath()).into(holder.iv_gift);
        }


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


        if(lastPosition ==position){
            convertView.setBackgroundColor(Color.LTGRAY);
        }else if(lastPosition!=position && lastPosition>=0){
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView iv_gift;
        TextView txt_giftCardCount,txt_country;
    }
}
