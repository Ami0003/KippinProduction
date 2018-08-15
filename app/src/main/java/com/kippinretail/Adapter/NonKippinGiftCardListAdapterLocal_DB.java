package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ActivityUpdateNonKippinLoyaltyCard;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.NonKippinCardType;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.ResponseToGetLoyaltyCardLocalModel;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sandeep.saini on 12/21/2016.
 */

public class NonKippinGiftCardListAdapterLocal_DB extends BaseAdapter implements Filterable {
    List<ResponseToGetLoyaltyCardLocalModel> listData;
    Activity mContext;
    LayoutInflater inflater;
    int hei, wei;
    Integer selected_position = -1;
    ArrayList<ResponseToGetLoyaltyCardLocalModel> toGetLoyaltyCards;
    private boolean isLocal;
    private ShareType shareType;
    private NonKippinCardType nonKippinCardType;
    private ItemFilter mfilter = new ItemFilter();
    private String merchantId;
    private List<ResponseToGetLoyaltyCardLocalModel> originalData;

    public NonKippinGiftCardListAdapterLocal_DB(List<ResponseToGetLoyaltyCardLocalModel> listData, Activity activity, String merchantId) {
        this.originalData = listData;
        this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext, 60);
        wei = CommonUtility.dpToPx(mContext, 60);
    }

    public NonKippinGiftCardListAdapterLocal_DB(List<ResponseToGetLoyaltyCardLocalModel> listData, Activity activity, NonKippinCardType nonKippinCardType, ShareType shareType) {
        this.originalData = listData;
        this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        this.nonKippinCardType = nonKippinCardType;
        this.shareType = shareType;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext, 60);
        wei = CommonUtility.dpToPx(mContext, 60);
    }

    public NonKippinGiftCardListAdapterLocal_DB(ArrayList<ResponseToGetLoyaltyCardLocalModel> toGetLoyaltyCards, List<ResponseToGetLoyaltyCardLocalModel> listData, Activity activity, NonKippinCardType nonKippinCardType, ShareType shareType) {
        this.originalData = listData;
        this.listData = listData;
        this.toGetLoyaltyCards = toGetLoyaltyCards;
        this.mContext = activity;
        this.merchantId = merchantId;
        this.nonKippinCardType = nonKippinCardType;
        this.shareType = shareType;

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
        if (listData.size() == 0) {
        } else {
            if (listData.size() >= position) {
                ResponseToGetLoyaltyCardLocalModel _mercDetail = listData.get(position);


                if (_mercDetail != null) {
                    /*if (_mercDetail.getIsRead() != null) {
                        if (_mercDetail.getIsRead()) {
                            holder.iv_star.setVisibility(View.GONE);
                        } else {
                            holder.iv_star.setVisibility(View.VISIBLE);
                        }

                    }*/

                    holder.txtMerchant.setText(_mercDetail.getFolderName());
                    if (_mercDetail.getLogoImage().equals("http://52.27.249.143/Kippin/wallet/") || _mercDetail.getLogoImage().equals("")) {

                    } else {
                        Log.e("LogoImage:", "" + _mercDetail.getOrignalLogoImage());
                        // new MyDecoder(holder.imageviewMyPoint, wei, hei).execute(_mercDetail.getLogoImage());
                        holder.imageviewMyPoint.setImageBitmap(SuperActivity.Decodebase64Image(_mercDetail.getLogoImage()));
                        //Picasso.with(mContext).load(_mercDetail.getOrignalLogoImage()).placeholder(R.drawable.icon_placeholder).resize(wei, hei).into(holder.imageviewMyPoint);
                    }
                }
                holder.layoutOfSubItem.setTag(_mercDetail);
            }

        }

        holder.layoutOfSubItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeNonKippingGiftCardNotification(position);
            }
        });
        return view;
    }

    private void removeNonKippingGiftCardNotification(final int index) {
        ResponseToGetLoyaltyCardLocalModel _mercDetail = listData.get(index);
        Log.e("SenderId:",""+_mercDetail.getSenderId());
        Log.e("SenderId:",""+_mercDetail.getUserId());
        Log.e("SenderId:",""+_mercDetail.getId());
        RestClient.getApiServiceForPojo().RemoveIsNonPhysical(_mercDetail.getSenderId(),
                _mercDetail.getUserId(), _mercDetail.getId(), "", new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        Log.e("output ==>", jsonElement.toString());
                        Log.e("URL -->", response.getUrl());
                        Type listType = new TypeToken<List<Merchant>>() {
                        }.getType();
                        Gson gson = new Gson();
                        Boolean flag = gson.fromJson(jsonElement.toString(), Boolean.class);
                        if (flag) {
                            Log.e("Notification Remove", "Notification Remove");
                            moveToNextActivity(index);
                        }
                        else{
                            moveToNextActivity(index);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(error.getUrl(), error.getMessage());
                        LoadingBox.dismissLoadingDialog();
                        moveToNextActivity(index);
                        //MessageDialog.showDialog(mContext, CommonUtility.TIME_OUT_MESSAGE, true);
                    }
                });
    }

    private void moveToNextActivity(int position) {
        Log.e("setOnClickListener", "setOnClickListener");
        CommonUtility.NonKippinGiftCardDatra = "";
        ResponseToGetLoyaltyCardLocalModel _mercDetail = listData.get(position);
        CommonUtility.NonKippinGiftCardDatra = new Gson().toJson(_mercDetail);
        Log.e("response:", "" + CommonUtility.NonKippinGiftCardDatra);
        //CommonUtility.NonKippinGiftCardDatra
        try {
            Log.e("Id:", "" + _mercDetail.getId());
            Log.e("merchantId:", "" + _mercDetail.getMerchantId());
            Log.e("Barcode:", "" + _mercDetail.getBarcode());
            Log.e("FolderName:", "" + _mercDetail.getFolderName());
            Intent i = new Intent();
            i.setClass(mContext, ActivityUpdateNonKippinLoyaltyCard.class);
            //i.putExtra("FrontImage", _mercDetail.getFrontImage());
            //i.putExtra("BackImage", _mercDetail.getBackImage());
            //i.putExtra("LogoImage", _mercDetail.getLogoImage());
            i.putExtra("LoyaltyCardId", _mercDetail.getId());
            i.putExtra("NonKippinCardType", nonKippinCardType);
            i.putExtra("FolderName", _mercDetail.getFolderName());
            if (_mercDetail.getMerchantId() != null) {
                i.putExtra("merchantId", _mercDetail.getMerchantId());
            } else {
                i.putExtra("merchantId", "0");
            }
            i.putExtra("Barcode", _mercDetail.getBarcode());

            i.putExtra("isFromDb", true);
            i.putExtra("isFromLocal", true);
            mContext.startActivity(i);
            mContext.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        } catch (Exception e) {
            Log.e("List null ", " == " + e.toString());
        } catch (Error ex) {
            ex.printStackTrace();
        }
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
            List<ResponseToGetLoyaltyCardLocalModel> temp = originalData;

            List<ResponseToGetLoyaltyCardLocalModel> filterdata = new ArrayList<ResponseToGetLoyaltyCardLocalModel>();
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
            listData = (ArrayList<ResponseToGetLoyaltyCardLocalModel>) results.values;
            notifyDataSetChanged();
        }
    }


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
}
