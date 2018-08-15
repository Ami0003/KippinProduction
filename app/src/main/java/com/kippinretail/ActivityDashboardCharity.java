package com.kippinretail;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.DbNew;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.CommonDialog.PromtListener;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.callbacks.NotificationREveiver;
import com.kippinretail.sharedpreferences.Prefs;

import java.util.ArrayList;

import notification.NotificationHandler;

public class ActivityDashboardCharity extends SuperActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ImageView ivPoints, ivGiftcard, ivLogout;
    ArrayList<ModalGridElement> modalGridElements;
    AdapterView.OnItemClickListener onItemClickListener;
    private GridView gridView1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_charity);
        initUI();
        updateToolbar();
        updateUI();
        setUpUI();
        setUpListeners();
        checkLocationPermission();
    }

    public void initUI() {
        gridView1 = (GridView) findViewById(R.id.gridView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtility.isCharityDashBoard = true;
        NotificationHandler.getInstance().getNotificationForCards(this, new NotificationREveiver() {
            @Override
            public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

            }
        });
    }

    void updateToolbar() {
        SelectUserTypeActivity.plus = 2;
        generateActionBar(R.string.charity_dashboard, true, false, true);
        generateRightText("", null);
        moveLeftTowardsRight();
    }


    void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.icon_loyalty, "Loyalty"));
        modalGridElements.add(getElement(R.drawable.gift_cards_orange, "Gift Cards"));
        modalGridElements.add(getElement(R.drawable.money_donated, "Money Donated"));
        modalGridElements.add(getElement(R.drawable.log_out_green, "Logout"));

        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:
                        i.setClass(ActivityDashboardCharity.this, ActivityCharity_Points.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 1:

                        i.setClass(ActivityDashboardCharity.this, ActivityMerchantListCharity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 2:
                        i.setClass(ActivityDashboardCharity.this, GiftCardsMerchantActivity.class);
                        CommonUtility.isCharityDashBoard = false;
                        logout(view);
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

                        i.setClass(ActivityDashboardCharity.this, CharityLoyalitySection.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 1:

                        i.setClass(ActivityDashboardCharity.this, ActivityMerchantListCharity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 2:
                        i.setClass(ActivityDashboardCharity.this, MoneyDonatedActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

                        break;
                    case 3:
                        logout(view);
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
        MyAbstractGridAdapter2 adapter = new MyAbstractGridAdapter2(this, modalGridElements);
        gridView1.setAdapter(adapter);

    }

    protected ModalGridElement getElement(int id, String title) {
        ModalGridElement modalGridElement = new ModalGridElement();
        modalGridElement.res = id;
        modalGridElement.string = title;
        return modalGridElement;
    }

    //    public void setUpUI() {
//
//    }
//
//    public void setUpListeners() {
//        setClickListeners(this,  ivPoints , ivGiftcard,ivLogout);
//    }
//
//    public void initialiseUI() {
//        ivPoints = findViewById(ImageView.class, R.id.ivPoints);
//        ivGiftcard = findViewById(ImageView.class, R.id.ivGiftcard);
//        ivLogout = findViewById(ImageView.class, R.id.ivLogout);
//    }
//
    public void logout(View d) {
        MessageDialog.showDialog(ActivityDashboardCharity.this, R.string.logout_message, new PromtListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onSubmit() {

                Prefs.with(ActivityDashboardCharity.this).removeAll();
                CommonData.resetData();
                DbNew.getInstance(ActivityDashboardCharity.this).DeleteAllNonKippinGift();
                Intent in = new Intent();
                in.setClass(ActivityDashboardCharity.this, RegistrationActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityDashboardCharity.this.startActivity(in);
                ActivityDashboardCharity.this.finish();
                ActivityDashboardCharity.this.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
            }
        });
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        //.setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ActivityDashboardCharity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()){
//            case R.id.ivPoints:
//                Utility.enterActivity(ActivityDashboardCharity.this,ActivityCharity_Points.class);
//                break;
//            case R.id.ivGiftcard:
//                Utility.enterActivity(this, ActivityMerchantListCharity.class);
//                break;
//            case R.id.ivLogout:
//                logout(v);
//                break;
//        }
//
//    }
//    @Override
//    public void onBackPressed() {
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        // locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
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
            // width = mContext.getResources().getDisplayMetrics().widthPixels / 2;
            //    height = 185;

            float myX = mContext.getResources().getDisplayMetrics().widthPixels - com.kippin.utils.Utility.dpToPx(mContext, 85);
            float x = (myX / 2f);
            float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - com.kippin.utils.Utility.dpToPx(mContext, 150);
            float y = (myHeightPX / 3f - com.kippin.utils.Utility.dpToPx(mContext, 40));
            width = Math.round(x);
            height = Math.round(y);

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

            //int res = getBackgroundDrawableUsingPosition(position);
            //holder.ivGridBg.setImageResource(res);
            //  holder.ivGridBg.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.ivGridBg.setImageResource(modalGridElement.res);
            holder.ivGridIcon.setVisibility(View.GONE);
            //holder.tvName.setText(modalGridElement.string);
            holder.tvName.setVisibility(View.GONE);
            convertView.setLayoutParams(new AbsListView.LayoutParams(width, height));
//        convertView.setLayoutParams(new AbsListView.LayoutParams(  CommonUtility.dpToPx(mContext , 140.75d )  ,CommonUtility.dpToPx(mContext , 152 )  ));


            return convertView;
        }

        private int getBackgroundDrawableUsingPosition(int position) {

            switch (position) {
                case 0:
                    return R.drawable.blue;
                case 1:
                    return R.drawable.gift_bg;
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
