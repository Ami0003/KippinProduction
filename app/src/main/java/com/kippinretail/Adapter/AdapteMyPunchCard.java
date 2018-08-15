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
import com.kippinretail.Modal.Modal_MyPunchCard.MyPunchCard;
import com.kippinretail.R;
import com.kippinretail.config.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeep.saini on 5/13/2016.
 */
public class AdapteMyPunchCard extends BaseAdapter {
    private Activity activity;
    List<MyPunchCard> modalPunchCards;
    BarcodeUtil barcodeUtil;
    String buisnessName;
    Selection selection;
    OnSelectionChanged onSelectionChanged;
    private int width, height;
    CheckBox old_view = null;
    int old_position = -1;
    private String merchantName=null;



    public AdapteMyPunchCard(String merchantName,Activity activit, ArrayList<MyPunchCard> modalPunchCards, String buisnessName, View rootView) {
        this.modalPunchCards = modalPunchCards;
        this.activity = activit;
        this.buisnessName = buisnessName;
        barcodeUtil = new BarcodeUtil(activit, rootView);
        width = (activit.getResources().getDisplayMetrics().widthPixels) / 2;
        height = Utils.dpToPx(activit,160);
        this.merchantName = merchantName;
    }


    public AdapteMyPunchCard(Activity activit, ArrayList<MyPunchCard> modalPunchCards, Selection selection, OnSelectionChanged onSelectionChanged) {
        this.modalPunchCards = modalPunchCards;
        this.activity = activit;
        this.selection = selection;
        this.onSelectionChanged = onSelectionChanged;
        width = (activit.getResources().getDisplayMetrics().widthPixels);
        height = Utils.dpToPx(activit, 160);

        barcodeUtil = new BarcodeUtil(activit, new View(activit));
    }

    @Override
    public int getCount() {
        return modalPunchCards.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder viewHolder;

        ImageView [] punches = new ImageView[7];;
        if (convertView == null) {
            convertView = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.subitem_mypunch_card, parent, false);
            viewHolder = new MyHolder();
            viewHolder.ivPunchCard = (ImageView) convertView.findViewById(R.id.ivPunchCard);
            viewHolder.ivBarCode = (ImageView) convertView.findViewById(R.id.ivBarCode);
            viewHolder.tvBarcode = (TextView) convertView.findViewById(R.id.tvBarcode);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            viewHolder.txtName = (TextView)convertView.findViewById(R.id.txtName);
            viewHolder.ivPunch1 = (ImageView)convertView.findViewById(R.id.ivPunch1);
            viewHolder.ivPunch2 = (ImageView)convertView.findViewById(R.id.ivPunch2);
            viewHolder.ivPunch3 = (ImageView)convertView.findViewById(R.id.ivPunch3);
            viewHolder.ivPunch4 = (ImageView)convertView.findViewById(R.id.ivPunch4);
            viewHolder.ivPunch5 = (ImageView)convertView.findViewById(R.id.ivPunch5);
            viewHolder.ivPunch6 = (ImageView)convertView.findViewById(R.id.ivPunch6);
            viewHolder.ivPunch7 = (ImageView)convertView.findViewById(R.id.ivPunch7);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyHolder) convertView.getTag();
        }

        punches[0]=viewHolder.ivPunch1;
        punches[1]=viewHolder.ivPunch2;
        punches[2]=viewHolder.ivPunch3;
        punches[3]=viewHolder.ivPunch4;
        punches[4]=viewHolder.ivPunch5;
        punches[5]=viewHolder.ivPunch6;
        punches[6]=viewHolder.ivPunch7;

//        if(modalPunchCards.get(position).getPunchcardImage()!=null){
            Picasso.with(activity).load(modalPunchCards.get(position).getPunchcardImage()).placeholder(R.drawable.user_icon).resize(width, height).into(viewHolder.ivPunchCard);
//        }else{
//            Picasso.with(activity).load("").placeholder(R.drawable.user_icon).resize(width, height).into(viewHolder.ivPunchCard);
//        }

        for(int i =0;i<punches.length;i++){
            punches[i].setVisibility(View.GONE);
            punches[i].setImageResource(R.drawable.white_circle);
        }
        viewHolder.txtName.setText(merchantName);
        if(modalPunchCards.get(position).getTotalPunches()!=null){
            int totalPunches = Integer.parseInt(modalPunchCards.get(position).getTotalPunches());
            int punchesLeft = Integer.parseInt(modalPunchCards.get(position).getPunches());
            disablePunchCard(totalPunches,punchesLeft,punches);
        }
        else{
            viewHolder.ivPunch1.setVisibility(View.GONE);
            viewHolder.ivPunch2.setVisibility(View.GONE);
            viewHolder.ivPunch3.setVisibility(View.GONE);
            viewHolder.ivPunch4.setVisibility(View.GONE);
            viewHolder.ivPunch5.setVisibility(View.GONE);
            viewHolder.ivPunch6.setVisibility(View.GONE);
            viewHolder.ivPunch7.setVisibility(View.GONE);
//            for(int i=0;i<7;i++){
//                punches[i].setVisibility(View.GONE);
//            }
        }
        viewHolder.ivBarCode.setImageDrawable(barcodeUtil.generateBarcode(modalPunchCards.get(position).getPunchBarcode()));

        viewHolder.tvBarcode.setText("BARCODE :" + modalPunchCards.get(position).getPunchBarcode());
        viewHolder.tvDescription.setText(modalPunchCards.get(position).getDescription());
//        if()
        return convertView;


    }


    private void disablePunchCard(int totalPunch,int leftPunchCard,ImageView []ar){
        switch (totalPunch){
            case 1:
                disable(1,leftPunchCard,ar);
                break;
            case 2:
                disable(2,leftPunchCard,ar);
                break;
            case 3:
                disable(3,leftPunchCard,ar);
                break;
            case 4:
                disable(4,leftPunchCard,ar);
                break;
            case 5:
                disable(5,leftPunchCard,ar);
                break;
            case 6:
                disable(6,leftPunchCard,ar);
                break;
            case 7:
                disable(7,leftPunchCard,ar);
                break;
        }
    }
    private void disable(int toatalPunch ,int leftPunchCard,ImageView []ar){
        for(int i=0;i<toatalPunch;i++){
            ar[i].setVisibility(View.VISIBLE);
            if(i<(toatalPunch-leftPunchCard)){
               ar[i].setImageResource(R.drawable.green_circle);
            }
        }

    }
    class MyHolder {
        ImageView ivPunchCard , ivBarCode;
        TextView txtName;
        ImageView ivPunch1,ivPunch2,ivPunch3,ivPunch4,ivPunch5,ivPunch6,ivPunch7,ivPunch8,ivPunch9,ivPunch10;
        TextView  tvBarcode, tvDescription ;
    }
    public enum  Selection{
        SINGLESELECTION , NONE
    }
}
