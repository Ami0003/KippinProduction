package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.BarcodeUtil;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.GiftCardList.GiftCard;
import com.kippinretail.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 4/14/2016.
 */
public class AdapterGiftCardsCharity extends BaseAdapter {

    Context context;
    ArrayList<GiftCard> modalGiftCards;
    LayoutInflater layoutInflater;
    BarcodeUtil barcodeUtil;
    Picasso picasso;
    int height, width;

    public AdapterGiftCardsCharity(Activity context, ArrayList<GiftCard> modalGiftCards) {
        this.context = context;
        this.modalGiftCards = modalGiftCards;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        picasso = Picasso.with(context);
        barcodeUtil = new BarcodeUtil(context);
        width = CommonUtility.dpToPx(context, CommonUtility.getWidthOfScreen(context) - 30);//mcontext.getResources().getDisplayMetrics().widthPixels);
        height = CommonUtility.dpToPx(context, 200);
    }

    public AdapterGiftCardsCharity(Activity context, ArrayList<GiftCard> modalGiftCards, View root) {
        this.context = context;
        this.modalGiftCards = modalGiftCards;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        picasso = Picasso.with(context);
        barcodeUtil = new BarcodeUtil(context, root);
        width = CommonUtility.dpToPx(context, CommonUtility.getWidthOfScreen(context) - 30);//mcontext.getResources().getDisplayMetrics().widthPixels);
        height = CommonUtility.dpToPx(context, 200);
    }

    @Override
    public int getCount() {
        return modalGiftCards.size();
    }

    @Override
    public Object getItem(int position) {
        return modalGiftCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class ViewHolder {

        ImageView ivTemplate, ivBarcode, iv_star;
        TextView txtTitle, txtFriendname;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        GiftCard giftCard = modalGiftCards.get(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.subitem_giftcard_charity_list, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.ivBarcode = (ImageView) convertView.findViewById(R.id.ivBarcode);
            viewHolder.ivTemplate = (ImageView) convertView.findViewById(R.id.ivTemplate);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            viewHolder.txtFriendname = (TextView) convertView.findViewById(R.id.txtFriendname);
            viewHolder.iv_star = (ImageView) convertView.findViewById(R.id.iv_star);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Double giftCardBalance = Double.parseDouble(giftCard.getPrice());
        int amt = (int) giftCardBalance.doubleValue();
        //String.valueOf(amt)
        viewHolder.txtTitle.setText("Gift card balance: $" + giftCardBalance);
        viewHolder.txtFriendname.setText("Friend Name: " + giftCard.getFriendName());
        picasso.load(giftCard.getGiftcardImage()).resize(width, height)
                .into(viewHolder.ivTemplate);
        if (giftCard.getBarcode() != null) {
           /* Drawable drawable = barcodeUtil.generateBarcode(giftCard.getBarcode());
            viewHolder.ivBarcode.setBackgroundDrawable(drawable);*/
            new Barcodegenerator(viewHolder.ivBarcode).execute(giftCard.getBarcode());
        }

        if (giftCard.getIsRead()) {
            viewHolder.iv_star.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_star.setVisibility(View.GONE);
        }
        return convertView;
    }

    class Barcodegenerator extends AsyncTask<String, Void, Drawable> {
        ImageView barcodeImage;

        Barcodegenerator(ImageView barcodeImage) {
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
