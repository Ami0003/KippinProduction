package com.kippinretail.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippin.utils.Utility;
import com.kippinretail.ActivityMyMerchant;
;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ComparePriceActivity;

import com.kippinretail.Modal.ModalGridElement;

import com.kippinretail.R;

import com.kippinretail.UserGiftCardActivity;
import com.kippinretail.UserPointActivity;
import com.kippinretail.VoucherActivity;
import com.kippinretail.callbacks.NotificationREveiver;


import java.util.ArrayList;

import notification.NotificationHandler;
import notification.NotificationUtil;

public class FragmentUserDashBoard extends Fragment //implements View.OnClickListener
{
    private GridView gridView1;
    ArrayList<ModalGridElement> modalGridElements;
    AdapterView.OnItemClickListener onItemClickListener;
    boolean isRefreshScreen = false;
    private String state = "active";

    public static FragmentUserDashBoard newInstance() {
        FragmentUserDashBoard fragment = new FragmentUserDashBoard();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View localView = inflater.inflate(R.layout.fragment_fragment_user_dash_board, container, false);
        gridView1 = (GridView) localView.findViewById(R.id.gridView);
        updateUI();
        addListener();
        setRetainInstance(true);
        return localView;
    }

    private void addListener() {
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:
                        isRefreshScreen = true;
                        i.setClass(getActivity(), UserPointActivity.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

                        break;
                    case 1:

                        i.setClass(getActivity(), VoucherActivity.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

                        break;
                    case 2:
                        isRefreshScreen = true;
                        i.setClass(getActivity(), UserGiftCardActivity.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

                        break;
                    case 3:

                        i.setClass(getActivity(), ComparePriceActivity.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

                        break;
                    case 4:

                        i.setClass(getActivity(), ActivityMyMerchant.class);
                        if (CommonData.getUserData(getActivity()).isEmployee()) {
                            i.putExtra("parentButton", "MyMerchant");
                            startActivity(i);
                            getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        }

                        break;
                    case 5:
                        CommonUtility.isUserDashBoard = false;
                        CommonUtility.logout(getActivity());
                        break;
                }
            }
        });
    }

    private void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.icon_loyalty, "Loyalty"));
        modalGridElements.add(getElement(R.drawable.icon_vouchers, "Promotions"));
        modalGridElements.add(getElement(R.drawable.icon_giftcard, "Gift cards"));
        modalGridElements.add(getElement(R.drawable.icon_price, "Price Comparison"));
        if (CommonData.getUserData(getActivity()).isEmployee()) {
            modalGridElements.add(getElement(R.drawable.my_merchant_blue, "My Merchant"));
        } else {
            modalGridElements.add(getElement(R.drawable.blank_logo, "My Merchant"));
        }
        modalGridElements.add(getElement(R.drawable.icon_logout, "Logout"));
        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:
                        resetClick();
                        isRefreshScreen = true;
                        i.setClass(getActivity(), UserPointActivity.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 1:
                        resetClick();
                        i.setClass(getActivity(), VoucherActivity.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 2:
                        resetClick();
                        isRefreshScreen = true;
                        i.setClass(getActivity(), UserGiftCardActivity.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 3:
                        resetClick();
                        i.setClass(getActivity(), ComparePriceActivity.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 4:
                        resetClick();
                        if (CommonData.getUserData(getActivity()).isEmployee()) {
                            i.setClass(getActivity(), ActivityMyMerchant.class);
                            i.putExtra("parentButton", "MyMerchant");
                            startActivity(i);
                            getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        }
                        break;
                    case 5:
                        CommonUtility.logout(getActivity());
                        break;
                }

            }
        });

    }

    private void resetClick() {
        CommonUtility.giftcardClicked=false;
        CommonUtility.layaltycardClicked = false;
        CommonUtility.aboutsUsClicked=false;
        CommonUtility.contactusClicked = false;
        CommonUtility.voucherclicked = false;

    }

    @Override
    public void onStop() {
        super.onStop();
        resetClick();
    }

    protected void setData(ArrayList<ModalGridElement> modalGridElements, AdapterView.OnItemClickListener onItemClickListener) {
        this.modalGridElements = modalGridElements;
        this.onItemClickListener = onItemClickListener;
        loadGrid();

    }

    private void loadGrid() {
        MyAbstractGridAdapter1 adapter = new MyAbstractGridAdapter1(getActivity(), modalGridElements);
        gridView1.setAdapter(adapter);

    }

    protected ModalGridElement getElement(int id, String title) {
        ModalGridElement modalGridElement = new ModalGridElement();
        modalGridElement.res = id;
        modalGridElement.string = title;
        return modalGridElement;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(isRefreshScreen){

            Log.e("Refresh Screen", "Refresh Screen");
            isRefreshScreen = false;
            NotificationHandler.getInstance().getNotificationForCards(getActivity(), new NotificationREveiver() {
                @Override
                public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {
                    if (IsVoucher) {
                        NotificationUtil.setNotification(1, gridView1,true);
                    }else{
                        NotificationUtil.setNotification(1, gridView1,false);
                    }
                    /*if (IsTradePoint) {
                        NotificationUtil.setNotification(0, gridView1,true);
                    }else{
                        NotificationUtil.setNotification(0, gridView1,false);
                    }
                    if (IsFriendRequest) {
                        NotificationUtil.setNotification(0, gridView1,true);
                        NotificationUtil.setNotification(2, gridView1,true);
                    }else{
                        NotificationUtil.setNotification(0, gridView1,false);
                        NotificationUtil.setNotification(2, gridView1,false);
                    }
                    if (IstransferGiftCard|IsNonKippinPhysical) {
                        NotificationUtil.setNotification(2, gridView1,true);
                    }else{
                        NotificationUtil.setNotification(2, gridView1,false);
                    }*/



                    /*if (IsTradePoint) {
                        NotificationUtil.setNotification(0, gridView1,true);
                    }else{
                        NotificationUtil.setNotification(0, gridView1,false);
                    }*/
                    if (IsFriendRequest | IsTradePoint | IsNonKippinLoyalty| IstransferGiftCard|IsNonKippinPhysical) {

                        if(IsFriendRequest|IsTradePoint |IsNonKippinLoyalty){
                            NotificationUtil.setNotification(0, gridView1,true);

                        }else {
                            NotificationUtil.setNotification(0, gridView1,false);
                        }

                        if(IsFriendRequest | IstransferGiftCard | IsNonKippinPhysical){
                            NotificationUtil.setNotification(2, gridView1,true);
                        }else{
                            NotificationUtil.setNotification(2, gridView1,false);
                        }


                    }else{
                        NotificationUtil.setNotification(0, gridView1,false);
                        NotificationUtil.setNotification(2, gridView1,false);
                    }



                    /*if (IstransferGiftCard|IsNonKippinPhysical) {
                        NotificationUtil.setNotification(2, gridView1,true);
                    }else{
                        NotificationUtil.setNotification(2, gridView1,false);
                    }*/




                    /*if (IsNonKippinPhysical) {
                        NotificationUtil.setNotification(2, gridView1,true);
                    }else
                    {
                        NotificationUtil.setNotification(2, gridView1,false);
                    }*/
                    /*if (IsNonKippinLoyalty) {
                        NotificationUtil.setNotification(0, gridView1,true);
                    }else{
                        NotificationUtil.setNotification(0, gridView1,false);
                    }*/

                }
            });
        }
        else{
            Log.e("Intialize Screen", "Intialize Screen");
            NotificationHandler.getInstance().getNotificationForCards(getActivity(), new NotificationREveiver() {
                @Override
                public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {
                    if (IsVoucher) {
                        NotificationUtil.setNotification(1, gridView1, true);
                    } else {
                            NotificationUtil.setNotification(1, gridView1,false);
                    }
                    if (IsTradePoint | IsNonKippinLoyalty) {
                        NotificationUtil.setNotification(0, gridView1, true);
                    } else {
                        //     NotificationUtil.setNotification(0, gridView1,false);
                    }
                    if (IsFriendRequest) {
                        NotificationUtil.setNotification(0, gridView1, true);
                        NotificationUtil.setNotification(2, gridView1, true);
                    } else {
                        //     NotificationUtil.setNotification(0, gridView1,false);
                        //     NotificationUtil.setNotification(2, gridView1,false);
                    }
                    if (IstransferGiftCard | IsNonKippinPhysical) {
                        NotificationUtil.setNotification(2, gridView1, true);
                    } else {
                        // NotificationUtil.setNotification(2, gridView1,false);
                    }
                    if (IsNewMerchant) {

                    }
                    /*if (IsNonKippinPhysical) {
                        NotificationUtil.setNotification(2, gridView1, true);
                    } else {
                        //         NotificationUtil.setNotification(2, gridView1,false);
                    }*/
                    /*if (IsNonKippinLoyalty) {
                        NotificationUtil.setNotification(0, gridView1, true);
                    } else {
                        //     NotificationUtil.setNotification(0, gridView1,false);
                    }*/

                }
            });
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }



    public class MyAbstractGridAdapter1 extends BaseAdapter {

        private Activity mContext;
        private LayoutInflater mLayoutInflater;
        private int width, height;
        private ArrayList<ModalGridElement> refForImages = null;


        public MyAbstractGridAdapter1(Activity dashBoardMerchantActivity,ArrayList<ModalGridElement> refForImages) {
            mContext = dashBoardMerchantActivity;
            this.refForImages = refForImages;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        public MyAbstractGridAdapter1(Activity dashBoardMerchantActivity,ArrayList<ModalGridElement> refForImages,GridView grid) {
            mContext = dashBoardMerchantActivity;
            this.refForImages = refForImages;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                float myX = mContext.getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(mContext, 85);
                float x=  (myX/ 2f);
                float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(mContext , 150);
                float y = (myHeightPX/ 3f - Utility.dpToPx(mContext , 40))  ;
                width = Math.round(x);
                height = Math.round(y);
            }else{

                float myX = mContext.getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(mContext, 35);
                float x=  (myX/ 4f);
                float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(mContext , 40);
                float y = (myHeightPX/ 2f - Utility.dpToPx(mContext , 5))  ;
                width = Math.round(x);
                height = Math.round(y);
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
            Holder holder;

            ModalGridElement modalGridElement = refForImages.get(position);

            if (convertView == null) {
                holder = new Holder();
                convertView = mLayoutInflater.inflate(R.layout.item_abstract_grid, null);
                holder = new Holder();
                holder.ivGridBg = (ImageView) convertView.findViewById(R.id.grid_bg);

                holder.ivGridIcon = (ImageView) convertView.findViewById(R.id.grid_icon);

                holder.tvName = (TextView) convertView.findViewById(R.id.grid_text);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

           /* int res = getBackgroundDrawableUsingPosition(position);
            holder.ivGridBg.setImageResource(res);
            holder.ivGridBg.setScaleType(ImageView.ScaleType.FIT_XY);*/

            if(!modalGridElement.string.equals((""))){
                //holder.ivGridIcon.setImageResource(modalGridElement.res);
                holder.ivGridBg.setVisibility(View.VISIBLE);
                holder.ivGridBg.setImageResource(modalGridElement.res);
                holder.tvName.setVisibility(View.GONE);
                holder.ivGridIcon.setVisibility(View.GONE);

            }else{
                holder.ivGridBg.setVisibility(View.GONE);
                holder.ivGridIcon.setVisibility(View.GONE);
            }
            //holder.tvName.setText(modalGridElement.string);
            convertView.setLayoutParams(new AbsListView.LayoutParams(width , height));
            return convertView;
        }

        private int getBackgroundDrawableUsingPosition(int position) {

            switch (position) {
                case 0:
                    return R.drawable.blue;

                case 1:
                    return R.drawable.points_bg;

                case 2:
                    return R.drawable.gift_bg;

                case 3:
                    return R.drawable.voucher_bg;

                case 4:
                    if (CommonData.getUserData(mContext).isEmployee()) {
                        return R.drawable.analysis_bg;
                    } else {

                        return R.drawable.icon_kippin_user;
                    }

                case 5:
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




