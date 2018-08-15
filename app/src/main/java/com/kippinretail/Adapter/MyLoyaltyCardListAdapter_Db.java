package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.ActivityMyPunchCards;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;

import java.util.ArrayList;
import java.util.List;

public class MyLoyaltyCardListAdapter_Db extends BaseAdapter /*implements Filterable*/ {
    List<Merchant> listData;
    Activity mContext;
    LayoutInflater inflater;
    int hei, wei;
    private ArrayList<String> profileImageList;
    private ItemFilter mfilter = new ItemFilter();
    private String merchantId;
    private List<Merchant> originalData;

    public MyLoyaltyCardListAdapter_Db(List<Merchant> listData, Activity activity, String merchantId) {
        this.originalData = this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext, 60);
        wei = CommonUtility.dpToPx(mContext, 60);
    }

    public MyLoyaltyCardListAdapter_Db(List<Merchant> listData, Activity activity) {
        this.originalData = this.listData = listData;
        this.mContext = activity;
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
            view = inflater.inflate(R.layout.subitem_merchant_voucher_list_image, parent, false);
            holder.imageviewMyPoint = (ImageView) view.findViewById(R.id.imageviewMyPoint);
            holder.layoutOfSubItem = (RelativeLayout) view.findViewById(R.id.layoutOfSubItem);
            holder.txtMerchant = (TextView) view.findViewById(R.id.txtMerchant);
            holder.iv_star = (ImageView) view.findViewById(R.id.iv_star);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            holder.txtMerchant.setText("");
        }
        Merchant _mercDetail = listData.get(position);
        holder.txtMerchant.setText(_mercDetail.getBusinessName());
   MyDecoder d =     new MyDecoder(holder.imageviewMyPoint,wei,hei);
        d.execute(_mercDetail.getProfileImage());
     //   Bitmap b = Bitmap.createScaledBitmap(SuperActivity.Decodebase64Image(_mercDetail.getProfileImage()),wei, hei,false);
    //    holder.imageviewMyPoint.setImageBitmap(b);
        holder.layoutOfSubItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Merchant _mercDetail = listData.get(position);
                    //CommonUtility.DataDetail = _mercDetail;
                    CommonUtility.DataDetail = _mercDetail;
                    Intent in = new Intent();
                    in.setClass(mContext, ActivityMyPunchCards.class);
                    mContext.startActivity(in);
                    mContext.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                } catch (Exception e) {
                    Log.e("List null ", " == " + e.toString());
                }
            }
        });


        return view;
    }

    /*@Override
    public Filter getFilter() {
        return mfilter;
    }
*/
    private class ViewHolder {
        ImageView imageviewMyPoint;
        RelativeLayout layoutOfSubItem;
        TextView txtMerchant;
        ImageView iv_star;

    }


    class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Merchant> temp = originalData;

            List<Merchant> filterdata = new ArrayList<Merchant>();
            FilterResults results = new FilterResults();

            for (int cnt = 0; cnt < temp.size(); cnt++) {
                Log.e("===" + temp.get(cnt).getBusinessName(), constraint.toString());
                if (temp.get(cnt).getBusinessName().contains(constraint.toString())) {
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
            listData = (ArrayList<Merchant>) results.values;
            notifyDataSetChanged();
        }
    }


    class MyDecoder extends  AsyncTask<String , Void , Bitmap>{

        ImageView img;
        int w,h;

        public MyDecoder(ImageView img,int w,int h) {
            this.img = img;
            this.w = w;;
            this.h = h;
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap b = Bitmap.createScaledBitmap(SuperActivity.Decodebase64Image(params[0]),this.w, this.h,false);
            return b;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                this.img.setImageBitmap(bitmap);
            }else{
                this.img.setImageDrawable(mContext.getDrawable(R.drawable.icon_circle_card));
            }

        }
    }
}

