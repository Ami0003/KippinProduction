package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.BarcodeUtil;
import com.kippinretail.Modal.GiftCardModal;
import com.kippinretail.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sandeep.singh on 12/13/2016.
 */
public class PurchasedGigtcardListAdapler extends BaseAdapter{
    private LayoutInflater inflater = null;
    List<GiftCardModal> giftCardModals;
    private Activity mcontext;
    private String merchantId,customerId;
    String randomNo;
    int height,width;
    View root;
    int hei,wei;
    BarcodeUtil barcodeUtil;

    public PurchasedGigtcardListAdapler(List<GiftCardModal> giftCardModals, Activity mcontext, View view)
    {
        this.giftCardModals = giftCardModals;
        this.mcontext = mcontext;
        barcodeUtil = new BarcodeUtil(mcontext,view) ;
        inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        width = dpToPx(CommonUtility.getWidthOfScreen(mcontext)-30);//mcontext.getResources().getDisplayMetrics().widthPixels);
        height = dpToPx(200);
        this.root = view;
    }



    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mcontext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    @Override
    public int getCount() {
        return giftCardModals.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        MyHolder  viewHolder ;

        if(convertView == null){
            convertView= ((View)inflater.inflate(R.layout.subitem_purchased_card,parent,false)) ;

            viewHolder = new MyHolder();
            viewHolder .ivPunchCardBarcode = (ImageView) convertView.findViewById(R.id.ivPunchCardBarcode); // Gigt card image
            viewHolder .ivPunchCardImage = (ImageView) convertView.findViewById(R.id.ivPunchCardImage);    // barcode image
            viewHolder .tvBarcode = (TextView) convertView.findViewById(R.id.tvPunches);                   // barcode
            viewHolder .tvOffer = (TextView) convertView.findViewById(R.id.tvOffer);    // Bal
            convertView.setTag(viewHolder);
        }else  {
            viewHolder = (MyHolder) convertView.getTag();
        }
        final GiftCardModal modalPunchCard = giftCardModals.get(position);
        //(int) Double.parseDouble(giftCardModals.get(position).getGetftCardPrice())
        viewHolder.tvOffer.setText("Gift card balance: $" + giftCardModals.get(position).getGetftCardPrice());


        if(modalPunchCard.getGiftCardBarCodeNo()!=null)
            viewHolder.tvBarcode.setText("BARCODE: " + modalPunchCard.getGiftCardBarCodeNo());
        else viewHolder.tvBarcode.setText("");


            Picasso.with(mcontext).load(modalPunchCard.getGiftCardImage()).into(viewHolder.ivPunchCardImage);

        if(modalPunchCard.getGiftCardBarCodeNo()!=null) {
            new Barcodegenerator(viewHolder.ivPunchCardBarcode).execute(modalPunchCard.getGiftCardBarCodeNo());
            /*Drawable drawable = barcodeUtil.generateBarcode(modalPunchCard.getGiftCardBarCodeNo());
            viewHolder.ivPunchCardBarcode.setImageDrawable(drawable);*/
        }
        else viewHolder.ivPunchCardBarcode.setVisibility(View.GONE);
        return convertView;
    }


    class MyHolder {
        TextView  tvBarcode, tvOffer  ;
        ImageView ivPunchCardImage , ivPunchCardBarcode;

    }



    public enum  Selection{
        SINGLESELECTION , NONE
    }

    class Barcodegenerator extends AsyncTask<String,Void,Drawable>{
        ImageView barcodeImage;
        Barcodegenerator(ImageView barcodeImage){
            this.barcodeImage = barcodeImage;
        }
        @Override
        protected Drawable doInBackground(String... params) {
            String imagePath = params[0];
            Drawable drawable = barcodeUtil.generateBarcode(imagePath);
            return drawable;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            barcodeImage.setImageDrawable(drawable);
        }
    }


}
