package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CheckBoxType;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.ResponseToGetLoyaltyCardModel;
import com.kippinretail.R;
import com.kippinretail.callbacks.CheckBoxClickHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sandeep.singh on 8/10/2016.
 */
public class ShareUnshareNonKippinLoyaltyCardAdapter extends BaseAdapter implements Filterable {
    List<ResponseToGetLoyaltyCardModel> listData;
    Activity mContext ;
    LayoutInflater inflater;
    int hei,wei;
    private ItemFilter mfilter = new ItemFilter();
    private String merchantId;
    private List<ResponseToGetLoyaltyCardModel> originalData;
    Integer selected_position = -1;
    boolean old_check = false;
    CheckBoxClickHandler onSelectionChanged;
    boolean  isMultichoice  = true;
    CheckBox old_cb= null;
    int old_position = -1;
    HashMap<Integer , String > shareDate = null;
    HashMap<Integer , String > unshareDate = null;
    public ShareUnshareNonKippinLoyaltyCardAdapter(List<ResponseToGetLoyaltyCardModel> listData , Activity activity,String merchantId){
        this.originalData = this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext, 60);
        wei = CommonUtility.dpToPx(mContext,60);

    }

    public ShareUnshareNonKippinLoyaltyCardAdapter(List<ResponseToGetLoyaltyCardModel> listData , Activity activity,boolean isMultiChoice ,CheckBoxClickHandler onSelectionChanged){
        this.originalData = this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext, 60);
        wei = CommonUtility.dpToPx(mContext,60);
        this.isMultichoice = isMultiChoice;
        this.onSelectionChanged = onSelectionChanged;
    }

    public ShareUnshareNonKippinLoyaltyCardAdapter(List<ResponseToGetLoyaltyCardModel> listData , Activity activity){
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
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder = null;
        if(view == null)
        {   holder = new ViewHolder();
            view =  inflater.inflate(R.layout.subitem_share_unshare,parent,false);//subitem_merchant_voucher_list
            holder.imageviewMyPoint = (CircleImageView)view.findViewById(R.id.imageviewMyPoint);
            holder.layoutOfSubItem = (RelativeLayout)view.findViewById(R.id.layoutOfSubItem);
            holder.txtMerchant = (TextView)view.findViewById(R.id.txtMerchant);
            holder.checkbox_share = (CheckBox)view.findViewById(R.id.checkbox_share);
            holder.checkbox_unshare = (CheckBox)view.findViewById(R.id.checkbox_unshare);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
            holder.txtMerchant.setText("");
        }

        if(listData.get(position).getIsShared()){
           holder.checkbox_share.setEnabled(false);
            holder.checkbox_unshare.setEnabled(true);
        }else{
            holder.checkbox_share.setEnabled(true);
            holder.checkbox_unshare.setEnabled(false);
           // holder.checkbox_unshare.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.unshare_ico));
//            holder.checkbox_unshare.setAlpha(f);

        }


        holder.checkbox_share.setTag(listData.get(position));
        holder.checkbox_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseToGetLoyaltyCardModel temp= (ResponseToGetLoyaltyCardModel)v.getTag();
                if(((CheckBox)v).isChecked()){
                    onSelectionChanged.onChechkBoxClick(Integer.parseInt(temp.getId()),temp.getFolderName(),true, CheckBoxType.SHARE);
                }else{
                    onSelectionChanged.onChechkBoxClick(Integer.parseInt(temp.getId()),temp.getFolderName(),false, CheckBoxType.SHARE);
                }
            }
        });

        holder.checkbox_unshare.setTag(listData.get(position));
        holder.checkbox_unshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseToGetLoyaltyCardModel temp= (ResponseToGetLoyaltyCardModel)v.getTag();
                if(((CheckBox)v).isChecked()){
                    onSelectionChanged.onChechkBoxClick(Integer.parseInt(temp.getId()),temp.getFolderName(),true, CheckBoxType.UNSHARE);
                }else{
                    onSelectionChanged.onChechkBoxClick(Integer.parseInt(temp.getId()),temp.getFolderName(),false, CheckBoxType.UNSHARE);
                }

            }
        });


        // CODE FOR CHECKBOX SINGLE SELECTION

//        boolean isChecked  =listData.get(position).isChecked();
//
//        holder.checkbox_share.setChecked(isChecked);
//
//        if(isChecked){
//            old_cb = holder.checkbox_share;
//            old_position = position;
//        }

//        holder.checkbox_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (old_cb != null && position != old_position && !isMultichoice) {
//                    old_cb.setChecked(false);
//                }
//                CheckBox cb = (CheckBox) v;
//                boolean isChecked = cb.isChecked();
//                listData.get(position).setIsChecked(isChecked);
//                if (!isMultichoice) {
//                    if (position != old_position && old_cb != null)
//                        listData.get(old_position).setIsChecked(false);
//                    onSelectionChanged.onSelectionChanged(position,  isChecked);
//                    ShareUnshareNonKippinLoyaltyCardAdapter.this.old_cb = cb;
//                    ShareUnshareNonKippinLoyaltyCardAdapter.this.old_position = position;
//                }else onSelectionChanged.onSelectionChanged(position ,  isChecked);
//            }
//        });

//        holder.checkbox_share.setChecked(position == selected_position);
//        holder.checkbox_share.setTag(position);
//        holder.checkbox_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selected_position = (Integer) view.getTag();
//
//            }
//        });

        ResponseToGetLoyaltyCardModel _mercDetail = listData.get(position);
        if(_mercDetail.getLogoImage()!=null && !_mercDetail.getLogoImage().equals("")){
            Picasso.with(mContext).load(_mercDetail.getLogoImage().replace("//","/")).placeholder(R.drawable.user).resize(wei, hei).into(holder.imageviewMyPoint);
        }
        else{
            Picasso.with(mContext).load(_mercDetail.getLogoImage().replace("//","/")).placeholder(R.drawable.user).resize(wei, hei).into(holder.imageviewMyPoint);
        }
        holder.txtMerchant.setText(_mercDetail.getFolderName());

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
        CheckBox checkbox_share,checkbox_unshare;
    }
    class ItemFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ResponseToGetLoyaltyCardModel> temp = originalData;

            List<ResponseToGetLoyaltyCardModel> filterdata = new ArrayList<ResponseToGetLoyaltyCardModel>();
            FilterResults results = new FilterResults();

            for(int cnt = 0;cnt<temp.size() ; cnt++)
            {
                Log.e("===" + temp.get(cnt).getFolderName(), constraint.toString());
                if(temp.get(cnt).getFolderName().toLowerCase().contains(constraint.toString().toLowerCase())){
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
            listData = (ArrayList <ResponseToGetLoyaltyCardModel>)results.values;
            notifyDataSetChanged();
        }
    }


}
