package com.kippinretail.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.VoucherMerchant.VoucherData;
import com.kippinretail.R;
import com.kippinretail.UploadVoucherActivity;
import com.kippinretail.VoucherMerchantActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kamaljeet.singh on 3/29/2016.
 */
public class AdapterForVouchers extends BaseAdapter {
    private final VoucherMerchantActivity mContext;
    private final List<VoucherData> mVoucherList;
    private final LayoutInflater mLayoutInflater;
    Holder holder;
    int width ,height;
    public AdapterForVouchers(VoucherMerchantActivity voucherMerchantActivity, List<VoucherData> voucherList) {
        mContext = voucherMerchantActivity;
        mVoucherList = voucherList;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        height = width = CommonUtility.dpToPx(mContext,60);
    }

    @Override
    public int getCount() {
        return mVoucherList.size();
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
                    R.layout.voucher_row_item, null);
            holder = new Holder();

            holder.voucherNameTextView = (TextView) convertView.findViewById(R.id.voucherNameTextView);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
            holder.imageView_arrow = (ImageView) convertView.findViewById(R.id.imageView_arrow);
            holder.voucher_image = (ImageView) convertView.findViewById(R.id.voucher_image);
            holder.voucher_relative = (RelativeLayout) convertView.findViewById(R.id.voucher_relative);
            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.pos = position;
        holder.voucherNameTextView.setTag(holder);
        holder.dateTextView.setTag(holder);
        holder.voucher_image.setTag(holder);
        holder.imageView_arrow.setTag(holder);
        holder.voucher_relative.setTag(holder);
        if(mVoucherList.get(position).getVoucherName() !=null) {
            String nVoucher[] = mVoucherList.get(position).getVoucherName().split("\\.");
            holder.voucherNameTextView.setText(nVoucher[0]);
        }

        if (mVoucherList.get(position).getStartDate() != null && mVoucherList.get(position).getEndDate() != null) {
            String sDate[] = mVoucherList.get(position).getStartDate().split("T");
            String eDate[] = mVoucherList.get(position).getEndDate().split("T");
            //holder.dateTextView.setText(sDate[0] + " To " + eDate[0]);
            String startDate =sDate[0].substring(0, 10);
            String startDay = startDate.substring(8, 10);
            String startMonth = startDate.substring(5,7);
            String startYear = startDate.substring(0,4);
            StringBuilder _startDate = new StringBuilder();
            _startDate.append(startMonth);_startDate.append("-");
            _startDate.append(startDay);_startDate.append("-");
            _startDate.append(startYear);

            String endDate = eDate[0].substring(0, 10);
            String endDay = endDate.substring(8, 10);
            String endMonth = endDate.substring(5,7);
            String endYear = endDate.substring(0, 4);
            StringBuilder _endDate = new StringBuilder();
            _endDate.append(endMonth);_endDate.append("-");
            _endDate.append(endDay);_endDate.append("-");
            _endDate.append(endYear);
            holder.dateTextView.setText(Html.fromHtml("<font color='#3E92D7'>" +_startDate.toString() + "</font>" + "  To  " + "<font color='#3E92D7'>" + _endDate.toString() + "</font>"));
        }
        Picasso.with(mContext)
                .load(mVoucherList.get(position).getVoucherImage())
                 .resize(width , height)
                .into(holder.voucher_image);

        holder.voucher_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder = (Holder) v.getTag();
                int pos = holder.pos;
                Intent intent = new Intent();
                intent.setClass(mContext, UploadVoucherActivity.class);
                intent.putExtra("VOUCHER_NAME", mVoucherList.get(pos).getVoucherName());
                intent.putExtra("VOUCHER_IMAGE", mVoucherList.get(pos).getVoucherImage());
                String sDate[] = mVoucherList.get(pos).getStartDate().split("T");
                String startDate =sDate[0].substring(0, 10);
                String startDay = startDate.substring(8, 10);
                String startMonth = startDate.substring(5,7);
                String startYear = startDate.substring(0,4);
                StringBuilder _startDate = new StringBuilder();
                _startDate.append(startMonth);_startDate.append("-");
                _startDate.append(startDay);_startDate.append("-");
                _startDate.append(startYear);
                intent.putExtra("START_DATE",_startDate.toString());
                String eDate[] = mVoucherList.get(pos).getEndDate().split("T");

                String endDate = eDate[0].substring(0, 10);
                String endDay = endDate.substring(8, 10);
                String endMonth = endDate.substring(5,7);
                String endYear = endDate.substring(0, 4);
                StringBuilder _endDate = new StringBuilder();
                _endDate.append(endMonth);_endDate.append("-");
                _endDate.append(endDay);_endDate.append("-");
                _endDate.append(endYear);
                intent.putExtra("END_DATE", _endDate.toString());
                mContext.startActivity(intent);
                mContext.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
            }
        });

        return convertView;
    }

    class Holder {
        public ImageView voucher_image, imageView_arrow;
        TextView voucherNameTextView, dateTextView;
        RelativeLayout voucher_relative;
        int pos;

    }
}
