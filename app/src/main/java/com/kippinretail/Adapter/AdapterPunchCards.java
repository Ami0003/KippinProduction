package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.BarcodeUtil;
import com.kippinretail.Interface.OnSelectionChanged;
import com.kippinretail.Modal.punchcard.ModalPunchCard;
import com.kippinretail.R;
import com.kippinretail.config.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaganpreet.singh on 4/29/2016.
 */
public class AdapterPunchCards extends BaseAdapter {
    private Activity activity;
    List<ModalPunchCard> modalPunchCards ;
    BarcodeUtil barcodeUtil;
    String buisnessName;
    Selection selection;
    OnSelectionChanged onSelectionChanged;
    private int width,height;
    CheckBox old_view  = null;
    int old_position  = -1;

    public AdapterPunchCards(Activity activit , ArrayList<ModalPunchCard>  modalPunchCards, String buisnessName, View rootView){
        this.modalPunchCards  = modalPunchCards;
        this.activity = activit;
        this.buisnessName =buisnessName;
        barcodeUtil = new BarcodeUtil(activit, rootView);
        width =(activit.getResources().getDisplayMetrics().widthPixels)/2;
        height = Utils.dpToPx(activit, 200);
    }



    public AdapterPunchCards(Activity activit , ArrayList<ModalPunchCard>  modalPunchCards, Selection selection , OnSelectionChanged onSelectionChanged){
        this.modalPunchCards  = modalPunchCards;
        this.activity = activit;
        this.selection = selection;
        this.onSelectionChanged = onSelectionChanged;
        width =(activit.getResources().getDisplayMetrics().widthPixels)/2;
        height = Utils.dpToPx(activit,200);
    }



    @Override
    public int getCount() {
        return modalPunchCards.size();
    }

    @Override
    public Object getItem(int position) {
        return modalPunchCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        MyHolder  viewHolder ;

        if(convertView == null){
            convertView= ((LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.subitem_punch_card,parent,false) ;

            viewHolder = new MyHolder();
           // viewHolder .ivPunchCardBarcode = (ImageView) convertView.findViewById(R.id.ivPunchCardBarcode);
            viewHolder .ivPunchCardImage = (ImageView) convertView.findViewById(R.id.ivPunchCardImage);
            viewHolder .tvPunches = (TextView) convertView.findViewById(R.id.tvPunches);
            viewHolder .tvDescription = (TextView) convertView.findViewById(R.id.tvDescription );
        //    viewHolder .tvOffer = (TextView) convertView.findViewById(R.id.tvOffer);
      //      viewHolder .tvBuisnessName= (TextView) convertView.findViewById(R.id.tvBuisnessName);
            viewHolder .cbSelection= (CheckBox) convertView.findViewById(R.id.cbSelection);
            convertView.setTag(viewHolder);
        }else  {
            viewHolder = (MyHolder) convertView.getTag();
        }


        final ModalPunchCard modalPunchCard = modalPunchCards.get(position);

//        viewHolder.tvOffer.setText(modalPunchCard.getDescription());
        if(modalPunchCard.getPunchcardImage()!=null){
            Picasso.with(activity).load(modalPunchCard.getPunchcardImage()).resize(width, height).into(viewHolder.ivPunchCardImage);
        }
        if(modalPunchCard.getPunches()!=null)
            viewHolder.tvPunches.setText("Punches:" + modalPunchCard.getPunches());
        else viewHolder.tvPunches.setVisibility(View.GONE);

        viewHolder.tvDescription.setText(modalPunchCard.getDescription());


//
//        if(buisnessName!=null)
//        viewHolder.tvBuisnessName.setText(buisnessName);
//        else viewHolder.tvBuisnessName.setVisibility(View.GONE);
//
//
//
//
//        if(modalPunchCard.getPunchBarcode()!=null) {
//            Drawable drawable = barcodeUtil.generateBarcode(modalPunchCard.getPunchBarcode());
//            viewHolder.ivPunchCardBarcode.setImageDrawable(drawable);
//        }
//        else viewHolder.ivPunchCardBarcode.setVisibility(View.GONE);


        if(selection!=null ){

            if(old_position==position){
                old_view = viewHolder.cbSelection ;
                viewHolder.cbSelection.setChecked(true);
            }


            viewHolder.cbSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean isChecked = ((CheckBox) v).isChecked();

                    if (old_view != null)
                        old_view.setChecked(false);

                    if (isChecked) {
                        old_position = position;
                        old_view = (CheckBox) v;
                    }else {
                        old_view=null;
                    }

                    onSelectionChanged.onSelectionChanged(position, modalPunchCard, isChecked);
                }
            });

        }else viewHolder.cbSelection.setVisibility(View.GONE);

        return convertView;
    }


    class MyHolder {
        TextView  tvPunches, tvDescription , tvOffer ,tvBuisnessName ;
        ImageView ivPunchCardImage , ivPunchCardBarcode;
        CheckBox cbSelection;
    }



    public enum  Selection{
            SINGLESELECTION , NONE
    }
}

