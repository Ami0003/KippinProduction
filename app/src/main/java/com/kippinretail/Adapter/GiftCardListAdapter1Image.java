package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.kippinretail.ApplicationuUlity.AppStatus;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.GiftCardMerchantList.MerchantDetail;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeep.singh on 7/22/2016.
 */
public class GiftCardListAdapter1Image extends BaseAdapter /*implements Filterable */ {
    List<MerchantDetail> listData;
    Activity mContext;
    LayoutInflater inflater;
    int hei, wei;
    // private ItemFilter mfilter = new ItemFilter();
    private String merchantId;
    private List<MerchantDetail> originalData;

    public GiftCardListAdapter1Image(List<MerchantDetail> listData, Activity activity, String merchantId) {
        this.originalData = this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext, 60);
        wei = CommonUtility.dpToPx(mContext, 60);
    }

    public GiftCardListAdapter1Image(List<MerchantDetail> listData, Activity activity) {
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
    public View getView(int position, View view, ViewGroup parent) {

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
        MerchantDetail _mercDetail = listData.get(position);
        if (AppStatus.getInstance(mContext).isOnline(mContext)) {
            if (_mercDetail.getProfileImage() != null && !_mercDetail.getProfileImage().equals("")) {
                Picasso.with(mContext).load(_mercDetail.getProfileImage()).placeholder(R.drawable.icon_placeholder).resize(wei, hei).into(holder.imageviewMyPoint);
            } else {
                if (_mercDetail.getProfileImage().isEmpty()) {

                } else {
                    Log.e("--path-----------", "" + _mercDetail.getProfileImage());
                    Picasso.with(mContext).load(_mercDetail.getProfileImage()).placeholder(R.drawable.icon_placeholder).resize(wei, hei).into(holder.imageviewMyPoint);

                }
            }
        } else {
            new MyDecoder(holder.imageviewMyPoint, wei, hei).execute(_mercDetail.getProfileImage());
        }
        holder.txtMerchant.setText(_mercDetail.getBusinessName());


        return view;
    }

    /*@Override
    public Filter getFilter() {
        return mfilter;
    }*/

    class MyDecoder extends AsyncTask<String, Void, Bitmap> {

        ImageView img;
        int w, h;

        public MyDecoder(ImageView img, int w, int h) {
            this.img = img;
            this.w = w;
            ;
            this.h = h;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap b = null;
            Log.e("BITMAP URL --", "== " + params[0]);
            if (params[0] != null && !params[0].equals("")) {
                b = Bitmap.createScaledBitmap(SuperActivity.Decodebase64Image(params[0]), this.w, this.h, false);
            }

            return b;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                this.img.setImageBitmap(bitmap);
            } else {
                Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_circle_card);
                this.img.setImageBitmap(image);
                //this.img.setImageDrawable(mContext.getDrawable(R.drawable.icon_circle_card));
            }

        }
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
            List<MerchantDetail> temp = originalData;

            List<MerchantDetail> filterdata = new ArrayList<MerchantDetail>();
            FilterResults results = new FilterResults();

            for (int cnt = 0; cnt < temp.size(); cnt++) {
                Log.e("===" + temp.get(cnt).getUsername(), constraint.toString());
                if (temp.get(cnt).getUsername().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            listData = (ArrayList<MerchantDetail>) results.values;
            notifyDataSetChanged();
        }
    }


}
