package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kippinretail.ActivityUpdateNonKippinLoyaltyCard;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.Modal.ResponseToGetLoyaltyCardModel;
import com.kippinretail.R;
import com.kippinretail.callbacks.CheckBoxClickHandler;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sandeep.singh on 7/28/2016.
 */
public class NonKippinGiftCardListAdapter extends BaseAdapter /*implements Filterable*/ {
    List<ResponseToGetLoyaltyCardModel> listData;
    Activity mContext;
    LayoutInflater inflater;
    int hei, wei;
    //private ItemFilter mfilter = new ItemFilter();
    private String merchantId;
    private List<ResponseToGetLoyaltyCardModel> originalData;
    Integer selected_position = -1;
    boolean old_check = false;
    CheckBoxClickHandler onSelectionChanged;
    boolean isMultichoice = true;
    CheckBox old_cb = null;
    int old_position = -1;
    ShareType shateType;

    public NonKippinGiftCardListAdapter(List<ResponseToGetLoyaltyCardModel> listData, Activity activity, String merchantId) {
        this.originalData = this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext, 60);
        wei = CommonUtility.dpToPx(mContext, 60);
    }

    public NonKippinGiftCardListAdapter(ShareType shareType, List<ResponseToGetLoyaltyCardModel> listData, Activity activity, boolean isMultiChoice, CheckBoxClickHandler onSelectionChanged) {
        this.originalData = this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext, 60);
        wei = CommonUtility.dpToPx(mContext, 60);
        this.isMultichoice = isMultiChoice;
        this.onSelectionChanged = onSelectionChanged;
        this.shateType = shareType;
    }

    public NonKippinGiftCardListAdapter(List<ResponseToGetLoyaltyCardModel> listData, Activity activity) {
        this.originalData = this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext, 60);
        wei = CommonUtility.dpToPx(mContext, 60);


    }

    @Override
    public int getCount() {
        if (listData != null) {
            return listData.size();
        } else {
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
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.subitem_share, parent, false);//subitem_merchant_voucher_list
            holder.imageviewMyPoint = (CircleImageView) view.findViewById(R.id.imageviewMyPoint);
            holder.layoutOfSubItem = (RelativeLayout) view.findViewById(R.id.layoutOfSubItem);
            holder.txtMerchant = (TextView) view.findViewById(R.id.txtMerchant);
            holder.checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            holder.txtMerchant.setText("");
        }


//        boolean isChecked  =listData.get(position).isChecked();
//
//        holder.checkBox1.setChecked(isChecked);
//
//        if(isChecked){
//            old_cb = holder.checkBox1;
//            old_position = position;
//        }
//
//        holder.checkBox1.setOnClickListener(new View.OnClickListener() {
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
//                    NonKippinGiftCardListAdapter.this.old_cb = cb;
//                    NonKippinGiftCardListAdapter.this.old_position = position;
//                }else onSelectionChanged.onSelectionChanged(position ,  isChecked);
//            }
//        });

//        holder.checkBox1.setChecked(position == selected_position);
//        holder.checkBox1.setTag(position);
//        holder.checkBox1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selected_position = (Integer) view.getTag();
//
//            }
//        });

        ResponseToGetLoyaltyCardModel _mercDetail = listData.get(position);
        if (_mercDetail.getLogoImage() != null && !_mercDetail.getLogoImage().equals("")) {
            Picasso.with(mContext).load(_mercDetail.getLogoImage()).placeholder(R.drawable.user).resize(wei, hei).into(holder.imageviewMyPoint);
        } else {
            Picasso.with(mContext).load(_mercDetail.getLogoImage()).placeholder(R.drawable.user).resize(wei, hei).into(holder.imageviewMyPoint);
        }
        holder.txtMerchant.setText(_mercDetail.getFolderName());
        holder.checkBox1.setTag(_mercDetail);
        holder.checkBox1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ResponseToGetLoyaltyCardModel loyaltyCardModel = (ResponseToGetLoyaltyCardModel) v.getTag();
                if (((CheckBox) v).isChecked()) {
                    onSelectionChanged.onChechkBoxClick(Integer.parseInt(loyaltyCardModel.getId()), loyaltyCardModel.getFolderName(), true, null);
                } else {
                    onSelectionChanged.onChechkBoxClick(Integer.parseInt(loyaltyCardModel.getId()), loyaltyCardModel.getFolderName(), false, null);
                }
            }
        });
        holder.layoutOfSubItem.setTag(_mercDetail);
        holder.layoutOfSubItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseToGetLoyaltyCardModel temp = (ResponseToGetLoyaltyCardModel) v.getTag();
                CommonUtility.NonKippinGiftCardDatra = "";
                CommonUtility.NonKippinGiftCardDatra = new Gson().toJson(temp);
                Intent i = new Intent();
                i.setClass(mContext, ActivityUpdateNonKippinLoyaltyCard.class);
                i.putExtra("LoyaltyCardId", temp.getId());
                i.putExtra("shareType", shateType);
                i.putExtra("FolderName", temp.getFolderName());
                i.putExtra("FrontImage", temp.getFrontImage());
                i.putExtra("BackImage", temp.getBackImage());
                i.putExtra("LogoImage", temp.getLogoImage());
                i.putExtra("Barcode", temp.getBarcode());
                i.putExtra("merchantId", temp.getMerchantId());
                i.putExtra("edit", true);
                mContext.startActivity(i);
                mContext.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
            }
        });
        return view;
    }

    /*@Override
    public Filter getFilter() {
        return mfilter;
    }*/

    private class ViewHolder {
        ImageView imageviewMyPoint;
        RelativeLayout layoutOfSubItem;
        TextView txtMerchant;
        CheckBox checkBox1;
    }
    /*class ItemFilter extends Filter
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
        }*/
    //  }


}