package com.kippinretail.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippinretail.Modal.PointsData.PointsDataa;
import com.kippinretail.PointsMerchantActivity;
import com.kippinretail.R;

import java.text.DateFormatSymbols;
import java.util.List;

/**
 * Created by kamaljeet.singh on 3/25/2016.
 */
public class AdapterPointsMerchant extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<PointsDataa> mPointList;
    Holder holder;

    public AdapterPointsMerchant(PointsMerchantActivity pointsMerchantActivity, List<PointsDataa> pointsList) {
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
                    R.layout.points_row_item_modified, null);
            holder = new Holder();

            holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            holder.tvEmpNo = (TextView) convertView.findViewById(R.id.tvEmpNum);
            holder.tvInvoiceNumber= (TextView) convertView.findViewById(R.id.tvInvoiceNum);
            holder.tvPoints= (TextView) convertView.findViewById(R.id.tvPoints);
            holder.tvActivity= (TextView) convertView.findViewById(R.id.tvActivity);

            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }


        PointsDataa pointsDataa = mPointList.get(position);

        holder.tvPoints.setText(pointsDataa.getPoint());
        holder.tvInvoiceNumber.setText(pointsDataa.getInvoiceNumber());
        holder.tvDate.setText(pointsDataa.getDate());
        holder.tvEmpNo.setText(pointsDataa.getEmployeeNumber());
        holder.tvActivity.setText(pointsDataa.getPointType());

        return convertView;
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    class Holder {
        TextView tvDate,tvEmpNo,tvPoints, tvInvoiceNumber,tvActivity;
    }
}

