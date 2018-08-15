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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.ActivityUpdateNonKippinLoyaltyCard;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.NonKippinCardType;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.ResponseToGetLoyaltyCard;
import com.kippinretail.Modal.ServerResponseToGetLoyaltyCard;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

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

public class NonKippinGiftCardListAdapter1_DB extends BaseAdapter /*implements Filterable*/ {
    private boolean isLocal;
    private ShareType shareType;
    private NonKippinCardType nonKippinCardType;
    List<ResponseToGetLoyaltyCard> listData;
    Activity mContext;
    LayoutInflater inflater;
    int hei, wei;
    private ItemFilter mfilter = new ItemFilter();
    private String merchantId;
    private List<ResponseToGetLoyaltyCard> originalData;
    Integer selected_position = -1;
    ArrayList<ServerResponseToGetLoyaltyCard> toGetLoyaltyCards;

    public NonKippinGiftCardListAdapter1_DB(List<ResponseToGetLoyaltyCard> listData, Activity activity, String merchantId) {
        this.originalData = listData;
        this.listData = listData;
        this.mContext = activity;
        this.merchantId = merchantId;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mContext, 60);
        wei = CommonUtility.dpToPx(mContext, 60);
    }

    public NonKippinGiftCardListAdapter1_DB(List<ResponseToGetLoyaltyCard> listData, Activity activity, NonKippinCardType nonKippinCardType, ShareType shareType) {
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
    public NonKippinGiftCardListAdapter1_DB(ArrayList<ServerResponseToGetLoyaltyCard> toGetLoyaltyCards,List<ResponseToGetLoyaltyCard> listData, Activity activity, NonKippinCardType nonKippinCardType, ShareType shareType) {
        this.originalData = listData;
        this.listData = listData;
        this.toGetLoyaltyCards=toGetLoyaltyCards;
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
            ResponseToGetLoyaltyCard _merc_Detail = listData.get(position);
            if(toGetLoyaltyCards.size()>=position){
                ServerResponseToGetLoyaltyCard _mercDetail=toGetLoyaltyCards.get(position);

                if (_mercDetail != null) {

                    holder.txtMerchant.setText(_mercDetail.getFolderName());
                    if(_mercDetail.getLogoImage().equals("http://52.27.249.143/Kippin/wallet/")|| _mercDetail.getLogoImage().equals("")){
                       // Log.e("IMAGEIMAGEIMAGE:", "IMAGEIMAGEIMAGE");
                    }
                    else{
                        Picasso.with(mContext).load(_mercDetail.getLogoImage()).placeholder(R.drawable.icon_placeholder).resize(wei, hei).into(holder.imageviewMyPoint);
                    }
                }
                holder.layoutOfSubItem.setTag(_mercDetail);
            }

        }

        //holder.imageviewMyPoint.setImageBitmap(SuperActivity.Decodebase64Image(_mercDetail.getLogoImage()));
        //new   MyDecoder(holder.imageviewMyPoint,wei,hei).execute(_mercDetail.getLogoImage());



        /*if(_mercDetail!=null){
            if(_mercDetail.getIsRead()!=null){
                if(!_mercDetail.getIsRead().booleanValue()){
                    holder.iv_star.setVisibility(View.VISIBLE);
                }

            }else{
                holder.iv_star.setVisibility(View.GONE);
            }
        }*/


        holder.layoutOfSubItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.NonKippinGiftCardDatra = "";
                //ResponseToGetLoyaltyCard temp = (ResponseToGetLoyaltyCard) v.getTag();
                ServerResponseToGetLoyaltyCard _mercDetail = toGetLoyaltyCards.get(position);
                //Log.e("------IMAGE--------:",""+_mercDetail.getLogoImage());
             //   Log.e("-here---getBackImage---------:",""+_mercDetail.getBackImage());
             //   Log.e("-------getFrontImage----------:",""+_mercDetail.getFrontImage());

                CommonUtility.NonKippinGiftCardDatra = new Gson().toJson(_mercDetail);
                // try {
                Intent i = new Intent();
                i.setClass(mContext, ActivityUpdateNonKippinLoyaltyCard.class);
                //i.putExtra("MyClass",new Gson().toJson(temp));
                /*    i.putExtra("MyClass",new Gson().toJson(temp));
                *  i.putExtra("FrontImage",temp.getFrontImage());
                    i.putExtra("BackImage", temp.getBackImage());
                    i.putExtra("LogoImage", temp.getLogoImage());
                *
                * */
                i.putExtra("FrontImage",_mercDetail.getFrontImage());
                i.putExtra("BackImage", _mercDetail.getBackImage());
                i.putExtra("LogoImage", _mercDetail.getLogoImage());
                i.putExtra("LoyaltyCardId", _mercDetail.getId());
                i.putExtra("NonKippinCardType", nonKippinCardType);
                i.putExtra("FolderName", _mercDetail.getFolderName());

                i.putExtra("Barcode", _mercDetail.getBarcode());
                i.putExtra("merchantId", _mercDetail.getMerchantId());
                i.putExtra("isFromDb", true);
                i.putExtra("isFromLocal", true);
                mContext.startActivity(i);
                mContext.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
              /*  } catch (Exception e) {
                    Log.e("List null ", " == " + e.toString());
                }
                catch(Error ex){
                    ex.printStackTrace();
                }*/
            }
        });
        return view;
    }

    private void removeNonKippingGiftCardNotification(final int index) {
        RestClient.getApiServiceForPojo().RemoveIsNonPhysical(listData.get(index).getSenderId(),
                listData.get(index).getUserId(), listData.get(index).getId(), "", new Callback<JsonElement>() {
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
                        MessageDialog.showDialog(mContext, CommonUtility.TIME_OUT_MESSAGE, true);
                    }
                });
    }

    private void moveToNextActivity(int position) {
        Intent i = new Intent();
        i.setClass(mContext, ActivityUpdateNonKippinLoyaltyCard.class);
        i.putExtra("LoyaltyCardId", listData.get(position).getId());
        i.putExtra("NonKippinCardType", nonKippinCardType);
        i.putExtra("FolderName", listData.get(position).getFolderName());
        i.putExtra("FrontImage", listData.get(position).getFrontImage());
        i.putExtra("BackImage", listData.get(position).getBackImage());
        i.putExtra("LogoImage", listData.get(position).getLogoImage());
        i.putExtra("Barcode", listData.get(position).getBarcode());
        i.putExtra("merchantId", listData.get(position).getMerchantId());
        i.putExtra("isFromDb", true);
        mContext.startActivity(i);
        mContext.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }


    /*@Override
    public Filter getFilter() {
        return mfilter;
    }*/

    private class ViewHolder {
        ImageView imageviewMyPoint;
        RelativeLayout layoutOfSubItem;
        TextView txtMerchant;
        ImageView iv_star;

    }

    class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ResponseToGetLoyaltyCard> temp = originalData;

            List<ResponseToGetLoyaltyCard> filterdata = new ArrayList<ResponseToGetLoyaltyCard>();
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
            listData = (ArrayList<ResponseToGetLoyaltyCard>) results.values;
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
            //Log.e("BITMAP URL --", "== " + params[0]);
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
