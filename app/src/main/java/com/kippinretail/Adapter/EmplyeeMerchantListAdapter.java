package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.MerchantAsEmployeeList.MerchantAsEmployeeDetail;
import com.kippinretail.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeep.singh on 7/26/2016.
 */
public class EmplyeeMerchantListAdapter extends BaseAdapter implements Filterable {

    List<MerchantAsEmployeeDetail> listData;
    Activity mContext ;
    LayoutInflater inflater;
    int hei,wei;
    private ItemFilter mfilter = new ItemFilter();
    private String merchantId;
    private List<MerchantAsEmployeeDetail> originalData;

    public EmplyeeMerchantListAdapter( Activity activity,List<MerchantAsEmployeeDetail> listData ){
        this.originalData = this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext, 60);
        wei = CommonUtility.dpToPx(mContext,60);
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

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = null;
        if(view == null)
        {   holder = new ViewHolder();
            view =  inflater.inflate(R.layout.subitem_merchant_voucher_list_image,parent,false);
            holder.imageviewMyPoint = (ImageView)view.findViewById(R.id.imageviewMyPoint);
            holder.layoutOfSubItem = (RelativeLayout)view.findViewById(R.id.layoutOfSubItem);
            holder.txtMerchant = (TextView)view.findViewById(R.id.txtMerchant);
            holder.iv_star = (ImageView)view.findViewById(R.id.iv_star);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
            holder.txtMerchant.setText("");
        }
        MerchantAsEmployeeDetail _mercDetail = listData.get(position);
        if(_mercDetail.getProfileImage()!=null && !_mercDetail.getProfileImage().equals("")){
            Picasso.with(mContext).load(_mercDetail.getProfileImage()).placeholder(R.drawable.icon_placeholder).resize(wei, hei).into(holder.imageviewMyPoint);
        }
        else{
            Picasso.with(mContext).load(_mercDetail.getProfileImage()).placeholder(R.drawable.icon_placeholder).resize(wei, hei).into(holder.imageviewMyPoint);
        }
        holder.txtMerchant.setText(_mercDetail.getBusinessname());

        return view;
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    private class ViewHolder
    {
        ImageView imageviewMyPoint;
        RelativeLayout layoutOfSubItem;
        TextView txtMerchant;
        ImageView iv_star;

    }
    class ItemFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MerchantAsEmployeeDetail> temp = originalData;

            List<MerchantAsEmployeeDetail> filterdata = new ArrayList<MerchantAsEmployeeDetail>();
            FilterResults results = new FilterResults();

            for(int cnt = 0;cnt<temp.size() ; cnt++)
            {
                Log.e("===" + temp.get(cnt).getBusinessname(), constraint.toString());
                if(temp.get(cnt).getBusinessname().toLowerCase().contains(constraint.toString().toLowerCase())){
                    filterdata.add(temp.get(cnt));
                }
            }
            Log.e("filterdata",filterdata.size()+"");
            results.values = filterdata;
            results.count = filterdata.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listData = (ArrayList <MerchantAsEmployeeDetail>)results.values;
            notifyDataSetChanged();
        }
    }

}
