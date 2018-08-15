package com.kippinretail;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.ApplicationuUlity.RequestType;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.Modal.UserAddress.MyGeoCoder;
import com.kippinretail.Modal.UserAddress.Results;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.sharedpreferences.Prefs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 3/11/2016.
 */
public class DashBoardMerchantActivity extends MyAbstractGridActivity {
    private GridView gridView;
    String[] TextMerchant = {
            "Add Employee",
            "Points/Loyality",
            "Gift Cards",
            "Vouchers",
            "Analytics",
            "Logout"
    };
    private boolean isLogoutClicked = false;
    ImageView logoutButton;
    private Dialog dialog;
    private String compres;
    private boolean networkProvider = false;
    private boolean gpsProvider = false;
    private LocationManager locationmanager = null;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
    double lattitude, longitude;
    private String mCountry;
    private String address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initlzation();
        updateToolbar();;
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtility.isMerchantdashBoard = true;

   
    }

    @Override
   public void updateToolbar() {
        generateActionBar(R.string.title_merchant_dashboard, true, false, true);
        generateRightText("", null);
        moveLeftTowardsRight();
    }

    @Override
    public void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.add_employee, "Add Employee")) ;
        modalGridElements.add(getElement(R.drawable.points_orange, "Points/Loyalty")) ;
        modalGridElements.add(getElement(R.drawable.gift_cards , "Gift Cards")) ;
        modalGridElements.add(getElement(R.drawable.promotions_green , "Promotions")) ;
        modalGridElements.add(getElement(R.drawable.analytics_blue , "Analytics")) ;
        modalGridElements.add(getElement(R.drawable.logout_orange, "Logout")) ;

        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:

                        i.setClass(DashBoardMerchantActivity.this, AddEmployeeMerchantActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 1:

                        i.setClass(DashBoardMerchantActivity.this, PointsMerchantActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 2:
                        i.setClass(DashBoardMerchantActivity.this, GiftCardsMerchantActivity.class);
                        i.putExtra("parentButton", "GiftCard");
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 3:
                        i.setClass(DashBoardMerchantActivity.this, VoucherMerchantActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 4:
                        i.setClass(DashBoardMerchantActivity.this, MerchantAnalyticsActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 5:
                        CommonUtility.isMerchantdashBoard = false;
                        CommonUtility.logout(DashBoardMerchantActivity.this);
                        break;
                }

            }
        });


    }


    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

    }



}
