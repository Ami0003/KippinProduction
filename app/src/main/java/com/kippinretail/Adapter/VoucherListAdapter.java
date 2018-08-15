package com.kippinretail.Adapter;

import android.content.Context;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.Modal.VoucherList.VoucherDetail;
import com.kippinretail.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sandeep.saini on 3/18/2016.
 */
public class VoucherListAdapter extends BaseAdapter
{
    private Context mcontext;
    private LayoutInflater inflate;
    private List<VoucherDetail> voucher;
    private int width,height;
    public VoucherListAdapter(Context mcontext, List<VoucherDetail> voucher)
    {   this.voucher = voucher;
        this.mcontext = mcontext;
        inflate = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        width =dpToPx(70);
        height = dpToPx(70);
    }
    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mcontext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    @Override
    public int getCount() {
        if(voucher!=null)
        {
            return  voucher.size();
        }
        else {
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

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if(view == null)
        {
            holder = new ViewHolder();
            view = inflate.inflate(R.layout.subitem_voucher_list,parent,false);
            holder.voucherimage = (ImageView)view.findViewById(R.id.voucherimage);
            holder.txtVoucherName = (TextView)view.findViewById(R.id.txtVoucherName);
            holder.txtVoucherValidation = (TextView)view.findViewById(R.id.txtVoucherValidation);
            holder.txt_status = (TextView)view.findViewById(R.id.txt_status);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();

        }
        if(voucher.get(position).getVoucherImage()!=null){
            Picasso.with(mcontext).load(voucher.get(position).getVoucherImage()).resize(width,height).into(holder.voucherimage);
        }
        if(voucher.get(position).getVoucherName()!=null){
            int extPos = voucher.get(position).getVoucherName().lastIndexOf(".");
            if(extPos == -1) {
                holder.txtVoucherName.setText(voucher.get(position).getVoucherName());
            }
            else {
                holder.txtVoucherName.setText(voucher.get(position).getVoucherName().substring(0, extPos));
            }

        }
        if(voucher.get(position).getStartDate()!=null && voucher.get(position).getEndDate()!=null){
            String startDate =voucher.get(position).getStartDate().substring(0,10);
            String startDay = startDate.substring(8, 10);
            String startMonth = startDate.substring(5,7);
            String startYear = startDate.substring(0,4);
            StringBuilder _startDate = new StringBuilder();
            _startDate.append(startMonth);_startDate.append("-");
            _startDate.append(startDay);_startDate.append("-");
            _startDate.append(startYear);

            String endDate = voucher.get(position).getEndDate().substring(0, 10);
            String endDay = endDate.substring(8, 10);
            String endMonth = endDate.substring(5,7);
            String endYear = endDate.substring(0, 4);
            StringBuilder _endDate = new StringBuilder();
            _endDate.append(endMonth);_endDate.append("-");
            _endDate.append(endDay);_endDate.append("-");
            _endDate.append(endYear);
            holder.txtVoucherValidation.setText(Html.fromHtml("<font color='#3E92D7'>"+_startDate.toString()+"</font>"+" To "+"<font color='#3E92D7'>"+_endDate.toString()+"</font>"));
          }
        if(voucher.get(position).isRead()==false){
            holder.txt_status.setText("*");
            holder.txt_status.setVisibility(View.VISIBLE);
        }else{
            holder.txt_status.setText("");
            holder.txt_status.setVisibility(View.INVISIBLE);
        }

        return view;
    }
    class ViewHolder
    {
        ImageView voucherimage;
        TextView txtVoucherName,txtVoucherValidation,txt_status;


    }
}
