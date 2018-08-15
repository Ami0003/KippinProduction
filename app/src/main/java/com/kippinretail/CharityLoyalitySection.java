package com.kippinretail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import com.kippinretail.Modal.ModalGridElement;
import com.kippin.utils.Utility;

import java.util.ArrayList;

/**
 * Created by agnihotri on 11/01/18.
 */

public class CharityLoyalitySection  extends SuperActivity  {

    ImageView ivPoints,ivGiftcard,ivLogout;
    private GridView gridView1;
    ArrayList<ModalGridElement> modalGridElements;
    AdapterView.OnItemClickListener onItemClickListener;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_charity);
        initUI();
        updateToolbar();
        updateUI();
        setUpUI();
        setUpListeners();

    }
    public void initUI(){
        gridView1 = (GridView) findViewById(R.id.gridView);
    }
    void updateToolbar() {
        generateActionBar(R.string.charity_dashboard , true,true,false);
       // generateActionBar(R.string.charity_dashboard, true, false, true);
        //generateRightText("",null);
       // moveLeftTowardsRight();
    }

    void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.incoming_loyalty_donations, "Incoming Loyalty Donations")) ;
        modalGridElements.add(getElement(R.drawable.my_donated_loyalty_cards , "My Donated Loyalty Cards")) ;


        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:
                        i.setClass(CharityLoyalitySection.this, ActivityCharity_Points.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 1:

                        i.setClass(CharityLoyalitySection.this, CharityDonatedLoyaltySection.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;

                }

            }
        });
    }



    @Override
    public void setUpListeners() {
        super.setUpListeners();
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:

                        i.setClass(CharityLoyalitySection.this, ActivityCharity_Points.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 1:

                        i.setClass(CharityLoyalitySection.this, CharityDonatedLoyaltySection.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;


                }
            }
        });
    }

    protected void setData(ArrayList<ModalGridElement> modalGridElements, AdapterView.OnItemClickListener onItemClickListener) {
        this.modalGridElements = modalGridElements;
        this.onItemClickListener = onItemClickListener;
        loadGrid();

    }

    private void loadGrid() {
        CharityLoyalitySection.MyAbstractGridAdapter2 adapter = new CharityLoyalitySection.MyAbstractGridAdapter2(this, modalGridElements);
        gridView1.setAdapter(adapter);

    }

    protected ModalGridElement getElement(int id, String title) {
        ModalGridElement modalGridElement = new ModalGridElement();
        modalGridElement.res = id;
        modalGridElement.string = title;
        return modalGridElement;
    }

    public class MyAbstractGridAdapter2 extends BaseAdapter {

        private Activity mContext;
        private LayoutInflater mLayoutInflater;
        private int width, height;
        private ArrayList<ModalGridElement> refForImages = null;


        public MyAbstractGridAdapter2(Activity dashBoardMerchantActivity, ArrayList<ModalGridElement> refForImages) {
            mContext = dashBoardMerchantActivity;
            this.refForImages = refForImages;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          /*  width = mContext.getResources().getDisplayMetrics().widthPixels / 2;
            height = 185;*/

            if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                float myX = mContext.getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(mContext, 85);
                float x=  (myX/ 2f);
                float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(mContext , 150);
                float y = (myHeightPX/ 3f - Utility.dpToPx(mContext , 40))  ;
                width = Math.round(x);
                height = Math.round(y);
                Log.e("if x axis width "+myX , "y axis height "+myHeightPX);
                Log.e("if width "+width , "height "+height);
            }else{

                float myX = mContext.getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(mContext, 40);
                float x=  (myX/ 4f);
                float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(mContext , 40);
                float y = (myHeightPX/ 2f - Utility.dpToPx(mContext , 5))  ;
                width = Math.round(x);
                height = Math.round(y);
                Log.e("else x axis width "+myX , "y axis height "+myHeightPX);
                Log.e("else width "+width , "height "+height);
            }



        }

        @Override
        public int getCount() {
            return refForImages.size();
        }

        @Override
        public Object getItem(int position) {
            return refForImages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CharityLoyalitySection.MyAbstractGridAdapter2.Holder holder;

            ModalGridElement modalGridElement = refForImages.get(position);

            if (convertView == null) {
                holder = new CharityLoyalitySection.MyAbstractGridAdapter2.Holder();
                convertView = mLayoutInflater.inflate(R.layout.item_abstract_grid, null);
                holder = new CharityLoyalitySection.MyAbstractGridAdapter2.Holder();
                holder.ivGridBg = (ImageView) convertView.findViewById(R.id.grid_bg);
                holder.ivGridIcon = (ImageView) convertView.findViewById(R.id.grid_icon);
                holder.tvName = (TextView) convertView.findViewById(R.id.grid_text);
                convertView.setTag(holder);


            } else {
                holder = (CharityLoyalitySection.MyAbstractGridAdapter2.Holder) convertView.getTag();
            }

            holder.ivGridBg.setImageResource(modalGridElement.res);
            holder.ivGridIcon.setVisibility(View.GONE);
            //holder.tvName.setText(modalGridElement.string);
            holder.tvName.setVisibility(View.GONE);
            convertView.setLayoutParams(new AbsListView.LayoutParams(width , height));

            return convertView;
        }

        private int getBackgroundDrawableUsingPosition(int position) {

            switch (position) {
                case 0:
                    return R.drawable.gift_bg;
                case 1:
                    return R.drawable.blue;
                case 2:
                    return R.drawable.voucher_bg;
                case 3:
                    return R.drawable.compare_bg;
            }
            return R.drawable.analysis_bg;
        }

        class Holder {

            ImageView ivGridBg, ivGridIcon;
            TextView tvName;
        }

    }
}
