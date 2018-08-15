package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.AppStatus;
import com.kippinretail.BarcodeUtil;
import com.kippinretail.Modal.GiftCardModal;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sandeep.saini on 3/21/2016.
 */
public class GiftCardListAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    List<GiftCardModal> giftCardModals;
    private Activity mcontext;
    private String merchantId,customerId;
    String randomNo;
    int height,width;
    View root;
    int hei,wei;
    BarcodeUtil barcodeUtil;

    public GiftCardListAdapter(List<GiftCardModal> giftCardModals, Activity mcontext, View view)
    {
        this.giftCardModals = giftCardModals;
        this.mcontext = mcontext;
        barcodeUtil = new BarcodeUtil(mcontext,view) ;
        inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        width =(mcontext.getResources().getDisplayMetrics().widthPixels);
        height = dpToPx(150);
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
            convertView= ((View)inflater.inflate(R.layout.subitem_punch_card,parent,false)) ;

            viewHolder = new MyHolder();
            viewHolder .ivPunchCardBarcode = (ImageView) convertView.findViewById(R.id.ivPunchCardBarcode);
            viewHolder .ivPunchCardImage = (ImageView) convertView.findViewById(R.id.ivPunchCardImage);
            viewHolder .tvBarcode = (TextView) convertView.findViewById(R.id.tvPunches);
            viewHolder .tvPunchesBalace = (TextView) convertView.findViewById(R.id.tvDescription);
            viewHolder .tvOffer = (TextView) convertView.findViewById(R.id.tvOffer);
            viewHolder .tvBuisnessName= (TextView) convertView.findViewById(R.id.tvBuisnessName);
            viewHolder .cbSelection= (CheckBox) convertView.findViewById(R.id.cbSelection);
            convertView.setTag(viewHolder);
        }else  {
            viewHolder = (MyHolder) convertView.getTag();
        }
        final GiftCardModal modalPunchCard = giftCardModals.get(position);
        //(int)Double.parseDouble(giftCardModals.get(position).getGetftCardPrice())
        viewHolder.tvOffer.setText("Gift card balance: $" +giftCardModals.get(position).getGetftCardPrice());
        viewHolder.tvPunchesBalace.setVisibility(View.GONE);//setText("Punches Balance:"+modalPunchCard.getPunches());

        if(modalPunchCard.getGiftCardBarCodeNo()!=null)
            viewHolder.tvBarcode.setText("BARCODE: " + modalPunchCard.getGiftCardBarCodeNo());
        else viewHolder.tvBarcode.setVisibility(View.GONE);
        viewHolder.tvBuisnessName.setVisibility(View.GONE);
        if(AppStatus.getInstance(mcontext).isOnline(mcontext)) {
            Picasso.with(mcontext).load(modalPunchCard.getGiftCardImage()).into(viewHolder.ivPunchCardImage);
        }else{
            new MyDecoder(viewHolder.ivPunchCardImage,wei,hei).execute(modalPunchCard.getGiftCardImage());
        }

        if(modalPunchCard.getGiftCardBarCodeNo()!=null) {
            Drawable drawable = barcodeUtil.generateBarcode(modalPunchCard.getGiftCardBarCodeNo());
            viewHolder.ivPunchCardBarcode.setImageDrawable(drawable);
        }
        else viewHolder.ivPunchCardBarcode.setVisibility(View.GONE);
        viewHolder.cbSelection.setVisibility(View.GONE);
        viewHolder.ivPunchCardImage.setTag(modalPunchCard);
        return convertView;
    }


    class MyHolder {
        TextView  tvBarcode, tvPunchesBalace , tvOffer ,tvBuisnessName ;
        ImageView ivPunchCardImage , ivPunchCardBarcode;
        CheckBox cbSelection;
    }



    public enum  Selection{
        SINGLESELECTION , NONE
    }

    class MyDecoder extends AsyncTask<String , Void , Bitmap> {

        ImageView img;
        int w,h;

        public MyDecoder(ImageView img,int w,int h) {
            this.img = img;
            this.w = w;;
            this.h = h;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap b = null;
            Log.e("BITMAP URL --","== " + params[0]);
            if(params[0]!=null && !params[0].equals("")) {
                b = Bitmap.createScaledBitmap(SuperActivity.Decodebase64Image(params[0]), width, height, false);
            }

            return b;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                this.img.setImageBitmap(bitmap);
            }else{
                //Bitmap image = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.icon_circle_card);
                //this.img.setImageBitmap(image);
                //this.img.setImageDrawable(mContext.getDrawable(R.drawable.icon_circle_card));
            }

        }
    }

}
