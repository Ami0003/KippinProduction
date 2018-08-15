package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sandeep.singh on 6/17/2016.
 */
public class GridViewAdpter extends BaseAdapter {
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private int  width,height;
    ArrayList<Integer> refForImages = null;
    public GridViewAdpter(Activity dashBoardMerchantActivity,ArrayList<Integer> refForImages) {
        mContext = dashBoardMerchantActivity;
        this.refForImages = refForImages;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        width = mContext.getResources().getDisplayMetrics().widthPixels/2;
        height = 192;
    }
    @Override
    public int getCount() {
        return refForImages.size();
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
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView =mLayoutInflater.inflate(
                    R.layout.grid_row_item1, null);
            holder = new Holder();
            holder.iv_gridItem = (ImageView) convertView.findViewById(R.id.iv_gridItem);
            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }
        Picasso.with(mContext).load(refForImages.get(position)).resize(width, CommonUtility.dpToPx(mContext, height)).placeholder(R.drawable.user).into(holder.iv_gridItem);
        return convertView;
    }

    class Holder {

        ImageView iv_gridItem;
    }
}
