package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.kippinretail.ActivityUpdateNonKippinLoyaltyCard;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.NonKippinCardType;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.Modal.GiftCard;
import com.kippinretail.Modal.ResponseToGetLoyaltyCardLocalModel;
import com.kippinretail.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Amit Agnihotri on 8/1/2016.
 */
public class MerchantGiftListAdapter extends BaseAdapter implements Filterable {
    List<GiftCard> listData;
    Activity mContext;
    LayoutInflater inflater;
    int hei, wei;
    private ItemFilter mfilter = new ItemFilter();
    private String merchantId;
    private List<GiftCard> originalData;
    Integer selected_position = -1;

    public MerchantGiftListAdapter(List<GiftCard> listData, Activity activity, String merchantId) {
        this.originalData = this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext, 60);
        wei = CommonUtility.dpToPx(mContext, 60);
        this.nonKippinCardType = nonKippinCardType;
    }


    // constructor for cache
    private boolean isLocal;
    private ShareType shareType;
    private NonKippinCardType nonKippinCardType;

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
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.subitem_merchant_voucher_list, parent, false);//subitem_share
            holder.imageviewMyPoint = (CircleImageView) view.findViewById(R.id.imageviewMyPoint);
            holder.layoutOfSubItem = (RelativeLayout) view.findViewById(R.id.layoutOfSubItem);
            holder.txtMerchant = (TextView) view.findViewById(R.id.txtMerchant);
            holder.iv_star = (ImageView) view.findViewById(R.id.iv_star);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            holder.txtMerchant.setText("");
        }


        GiftCard _mercDetail = listData.get(position);
        if (_mercDetail.getLogoImage() != null && !_mercDetail.getLogoImage().equals("")) {
            Picasso.with(mContext).load(_mercDetail.getLogoImage().replace("//", "/")).placeholder(R.drawable.icon_circle_card).resize(wei, hei).into(holder.imageviewMyPoint);
        } else {
            Picasso.with(mContext).load(_mercDetail.getLogoImage()).placeholder(R.drawable.icon_circle_card).resize(wei, hei).into(holder.imageviewMyPoint);
        }
        holder.txtMerchant.setText(_mercDetail.getFolderName());
        holder.iv_star.setVisibility(View.GONE);


        holder.layoutOfSubItem.setTag(_mercDetail);
        holder.layoutOfSubItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftCard temp = (GiftCard) v.getTag();
                CommonUtility.NonKippinGiftCardDatra = new Gson().toJson(temp);

               /* ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCard = new Gson().fromJson(CommonUtility.NonKippinGiftCardDatra, ResponseToGetLoyaltyCardLocalModel.class);
                Log.e("ID:", "" + responseToGetLoyaltyCard.getId());
*/
                Intent i = new Intent();
                i.setClass(mContext, ActivityUpdateNonKippinLoyaltyCard.class);
                Log.e("ID:", "" + temp.getId());

                i.putExtra("LoyaltyCardId", temp.getId());
                i.putExtra("NonKippinCardType", nonKippinCardType);
                i.putExtra("FolderName", temp.getFolderName());
                i.putExtra("FrontImage", temp.getFrontImage());
                i.putExtra("BackImage", temp.getBackImage());
                i.putExtra("LogoImage", temp.getLogoImage());
                i.putExtra("Barcode", temp.getBarcode());
                i.putExtra("merchantId", "0");
                i.putExtra("isFromDb", false);
                i.putExtra("isLocal", false);
                i.putExtra("edit", true);
                mContext.startActivity(i);
                mContext.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
            }
        });
        return view;
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    private class ViewHolder {
        ImageView imageviewMyPoint;
        RelativeLayout layoutOfSubItem;
        TextView txtMerchant;
        ImageView iv_star;

    }

    class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<GiftCard> temp = originalData;

            List<GiftCard> filterdata = new ArrayList<GiftCard>();
            FilterResults results = new FilterResults();

            for (int cnt = 0; cnt < temp.size(); cnt++) {
                Log.e("===" + temp.get(cnt).getFolderName().toLowerCase(), constraint.toString());
                if (temp.get(cnt).getFolderName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    filterdata.add(temp.get(cnt));
                }
            }
            Log.e("filterdata", filterdata.size() + "");
            results.values = filterdata;
            results.count = filterdata.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listData = (ArrayList<GiftCard>) results.values;
            notifyDataSetChanged();
        }
    }


}