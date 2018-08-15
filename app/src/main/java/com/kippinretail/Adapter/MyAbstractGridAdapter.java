package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.R;
import com.kippin.utils.Utility;

import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 7/15/2016.
 */
public class MyAbstractGridAdapter extends BaseAdapter{

    private Activity mContext;
    private LayoutInflater mLayoutInflater;
  private int  width,height;
    private ArrayList<ModalGridElement> refForImages = null;


    public MyAbstractGridAdapter(Activity dashBoardMerchantActivity,ArrayList<ModalGridElement> refForImages) {
        mContext = dashBoardMerchantActivity;
        this.refForImages = refForImages;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            float myX = mContext.getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(mContext, 85);
            float x=  (myX/ 2f);
            float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(mContext , 150);
            float y = (myHeightPX/ 3f - Utility.dpToPx(mContext , 40))  ;
            width = Math.round(x);
            height = Math.round(y);
        }else{

            float myX = mContext.getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(mContext, 35);
            float x=  (myX/ 4f);
            float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(mContext , 40);
            float y = (myHeightPX/ 2f - Utility.dpToPx(mContext , 5))  ;
            width = Math.round(x);
            height = Math.round(y);
        }

    }
    public MyAbstractGridAdapter(Activity dashBoardMerchantActivity,ArrayList<ModalGridElement> refForImages,GridView grid) {
        System.gc();
        mContext = dashBoardMerchantActivity;
        this.refForImages = refForImages;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            float myX = mContext.getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(mContext, 85);
            float x=  (myX/ 2f);
            float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(mContext , 150);
            float y = (myHeightPX/ 3f - Utility.dpToPx(mContext , 40))  ;
            width = Math.round(x);
            height = Math.round(y);
        }else{

            float myX = mContext.getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(mContext, 40);
            float x=  (myX/ 4f);
            float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(mContext , 40);
            float y = (myHeightPX/ 2f - Utility.dpToPx(mContext , 5))  ;
            width = Math.round(x);
            height = Math.round(y);
        }

    }
    @Override
    public int getCount() {
        return refForImages.size();
    }

    @Override
    public Object getItem(int position) {
        return refForImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        ModalGridElement modalGridElement = refForImages.get(position)  ;
        if (convertView == null) {
            holder = new Holder();
            convertView =mLayoutInflater.inflate(   R.layout.item_abstract_grid, null);
            holder = new Holder();
            holder.ivGridBg = (ImageView) convertView.findViewById(R.id.grid_bg);
            holder.ivGridIcon= (ImageView) convertView.findViewById(R.id.grid_icon);
            holder.tvName= (TextView) convertView.findViewById(R.id.grid_text);
            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }
        //int res = getBackgroundDrawableUsingPosition(position);
        //holder.ivGridBg.setImageResource(res);
       // holder.ivGridBg.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.ivGridBg.setImageResource(modalGridElement.res);
        //holder.tvName.setText(modalGridElement.string);
        holder.tvName.setVisibility(View.GONE);
        holder.ivGridIcon.setVisibility(View.GONE);
        convertView.setLayoutParams(new AbsListView.LayoutParams(width , height));
        return convertView;
    }

    private int getBackgroundDrawableUsingPosition(int position) {

        switch (position){
            case 0:
                return R.drawable.blue ;

            case 1:
                return R.drawable.points_bg ;

            case 2:
                return R.drawable.gift_bg ;

            case 3:
                return R.drawable.voucher_bg ;

            case 4:
                return R.drawable.analysis_bg;

            case 5:
                return R.drawable.compare_bg ;
        }

        return R.drawable.analysis_bg;
    }

    class Holder {

        ImageView ivGridBg, ivGridIcon ;
        TextView tvName ;
    }



}
