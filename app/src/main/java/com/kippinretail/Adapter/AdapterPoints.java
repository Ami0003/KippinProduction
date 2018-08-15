package com.kippinretail.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.Modal.PointsData.PointsDataa;
import com.kippinretail.PointsMerchantActivity;
import com.kippinretail.R;

import java.text.DateFormatSymbols;
import java.util.List;

/**
 * Created by kamaljeet.singh on 3/25/2016.
 */
public class AdapterPoints extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<PointsDataa> mPointList;
    Holder holder;

    public AdapterPoints(PointsMerchantActivity pointsMerchantActivity, List<PointsDataa> pointsList) {
        mContext = pointsMerchantActivity;
        mPointList = pointsList;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mPointList.size();
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
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(
                    R.layout.points_row_item, null);
            holder = new Holder();

            holder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
            holder.pointsTextView = (TextView) convertView.findViewById(R.id.pointsTextView);
            holder.arrowImageView = (ImageView) convertView.findViewById(R.id.arrow);
            holder.pointsLinearLayout = (LinearLayout) convertView.findViewById(R.id.pointsLinearLayout);
            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.pos = position;
        holder.dateTextView.setTag(holder);
        holder.pointsTextView.setTag(holder);
        holder.arrowImageView.setTag(holder);
        holder.pointsLinearLayout.setTag(holder);
        Log.e("Points val", "==" + mPointList.get(position).getMonth());
        Log.e("Response Code", "==" + mPointList.get(position).getResponseCode());
        if (mPointList.get(position).getResponseCode() == 2 || mPointList.get(position).getResponseCode() == 5) {
            holder.dateTextView.setVisibility(View.GONE);
            holder.pointsTextView.setVisibility(View.GONE);
            holder.arrowImageView.setVisibility(View.GONE);
        } else {
            if (!mPointList.get(position).getMonth().toString().equals("0")) {
                String month = getMonth(mPointList.get(position).getMonth()).substring(0, 3);
                holder.dateTextView.setText(month + " " + mPointList.get(position).getYear());
            }

            holder.pointsTextView.setText(mPointList.get(position).getPoint() + " Points");
        }


        return convertView;
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    class Holder {
        public ImageView arrowImageView;
        TextView dateTextView, pointsTextView;
        LinearLayout pointsLinearLayout;
        int pos;

    }
}

