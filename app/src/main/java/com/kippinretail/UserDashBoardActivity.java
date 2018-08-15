package com.kippinretail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.Adapter.MySideBaseAdapter;
import com.kippinretail.Adapter.Notification_Adapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.DbNew;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.CommonDialog.PromtListener;
import com.kippinretail.KippinInvoice.Fragments_invoice.FragmentInvoiceDashBoard;
import com.kippinretail.KippinInvoice.Fragments_invoice.FragmentUpdatePassword;
import com.kippinretail.Modal.ServerResponseForNotification.GetAllNotification.ResponseToGettAllNotification;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.callbacks.DialogListener;
import com.kippinretail.callbacks.OnNOtificationDelete;
import com.kippinretail.config.Utils;
import com.kippinretail.fragment.FragmentAboutUs;
import com.kippinretail.fragment.FragmentContactUs;
import com.kippinretail.fragment.FragmentGiftCard;
import com.kippinretail.fragment.FragmentLoyalty;
import com.kippinretail.fragment.FragmentUserDashBoard;
import com.kippinretail.fragment.FragmentVoucher;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.sharedpreferences.Prefs;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class UserDashBoardActivity extends SuperActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static UserDashBoardActivity userDashBoardActivity;
    public static String Title = null;
    static boolean firstTime;
    public Fragment loaded = null;
    TextView tvTopbarTitle;
    RelativeLayout layout_notification;
    boolean isMenuClicked;
    String[] TextMerchant = {
            "Points",
            "Vouchers",
            "Gift Cards",
            "Price Comparison",
            "My Merchant",
            "Logout"
    };
    TextView txt_userDashBoard;
    RelativeLayout layout_loyalty, layout_voucher, layout_giftcards, layout_contactUs, layout_aboutUs, layout_userDashBoard, layout_UpdatePassword;
    TextView tvNewMerchantnotification;
    LinearLayout layoutForSlider;
    boolean loadDashboard = true;
    Fragment lastFragment;
    MySideBaseAdapter mySideBaseAdapter;
    String InvoiceUserType = "false";
    ImageView iv_userDashBoard;
    private GridView gridView;
    private DrawerLayout drawer;
    private ImageView iv_menu, iv_deleteAll, iv_star, iv_updateProfile, iv_home, ivLogout, iv_aboutUs;
    private boolean isHomeSet = true;
    private View firstview;
    private View secondView;
    private View thirdView;
    private View forthView;
    private View fifthView;
    private View lastView;
    private Integer roleId;
    private Dialog dialog;
    private ListView list_notification;
    private boolean isLogoutClicked = false;
    private Notification_Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        userDashBoardActivity = this;
        Initlzation();
        updateUI();
        addListener();
        Utils.hideKeyboard(this);
        Log.e("InvoiceUserType:", "" + InvoiceUserType);
        if (InvoiceUserType == null) {
            InvoiceUserType = "false";
        }
        if (!InvoiceUserType.equals("true")) {
            roleId = CommonData.getUserData(UserDashBoardActivity.this).getRoleId();
            getAllNotification();
        }
        requestAbsentPermissions(CommonUtility.requiredPermission);
    }

    protected boolean requestAbsentPermissions(String... listPermissionsNeeded) {
        ArrayList<String> requiredPermissions = new ArrayList<>();
        try {

            for (int i = 0; i < listPermissionsNeeded.length; i++) {
                int isGranted = ContextCompat.checkSelfPermission(this, listPermissionsNeeded[i]);
                if (isGranted != PackageManager.PERMISSION_GRANTED) {
                    requiredPermissions.add(listPermissionsNeeded[i]);
                }
            }
            if (requiredPermissions.size() > 0) {
                ActivityCompat.requestPermissions(this, requiredPermissions.toArray(new String[requiredPermissions.size()]), CommonUtility.REQUEST_ID_MULTIPLE_PERMISSIONS);
            }

        } catch (Exception ex) {

        }
        return requiredPermissions.size() == 0;
    }

    public void loadDashboard() {
        loadDashboard = true;
        loaded = null;
        tvTopbarTitle.setText("USER DASHBOARD");
        iv_updateProfile.setVisibility(View.VISIBLE);
        ivLogout.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
    }

    private void Initlzation() {
        // try {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            InvoiceUserType = extras.getString("InvoiceUserType");
            // and get whatever type user account id is
        }
        iv_userDashBoard = (ImageView) findViewById(R.id.iv_userDashBoard);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        iv_deleteAll = (ImageView) findViewById(R.id.iv_deleteAll);
        iv_star = (ImageView) findViewById(R.id.iv_star);
        iv_home = (ImageView) findViewById(R.id.iv_home_retail);
        ivLogout = (ImageView) findViewById(R.id.ivLogout);
        iv_updateProfile = (ImageView) findViewById(R.id.iv_updateProfile);
        gridView = (GridView) findViewById(R.id.gridView);
        tvTopbarTitle = (TextView) findViewById(R.id.tvTopbarTitle);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        layout_loyalty = (RelativeLayout) findViewById(R.id.layout_loyalty);
        layout_voucher = (RelativeLayout) findViewById(R.id.layout_voucher);
        layout_giftcards = (RelativeLayout) findViewById(R.id.layout_giftcards);
        layout_contactUs = (RelativeLayout) findViewById(R.id.layout_contactUs);
        layout_aboutUs = (RelativeLayout) findViewById(R.id.layout_aboutUs);
        layout_UpdatePassword = (RelativeLayout) findViewById(R.id.layout_UpdatePassword);
        layout_userDashBoard = (RelativeLayout) findViewById(R.id.layout_userDashBoard);
        layout_notification = (RelativeLayout) findViewById(R.id.layout_notification);
        list_notification = (ListView) findViewById(R.id.list_notification);
        iv_aboutUs = (ImageView) findViewById(R.id.iv_aboutUs);


        tvNewMerchantnotification = (TextView) findViewById(R.id.tvNewMerchantnotification);

        firstview = findViewById(R.id.firstview);
        secondView = findViewById(R.id.secondView);
        thirdView = findViewById(R.id.thirdView);
        forthView = findViewById(R.id.forthView);
        fifthView = findViewById(R.id.fifthView);
        lastView = findViewById(R.id.lastView);
        ImageView iv_contactUs = (ImageView) findViewById(R.id.iv_contactUs);

        if (InvoiceUserType != null)
            if (InvoiceUserType.equals("true")) {
                layout_loyalty.setVisibility(View.GONE);
                layout_voucher.setVisibility(View.GONE);
                layout_giftcards.setVisibility(View.GONE);
                list_notification.setVisibility(View.GONE);
                layout_notification.setVisibility(View.GONE);
                tvNewMerchantnotification.setVisibility(View.GONE);
                list_notification.setVisibility(View.GONE);
                iv_updateProfile.setVisibility(View.GONE);
                ivLogout.setVisibility(View.VISIBLE);
                layout_UpdatePassword.setVisibility(View.VISIBLE);
                firstview.setVisibility(View.GONE);
                secondView.setVisibility(View.GONE);
                thirdView.setVisibility(View.GONE);
                forthView.setVisibility(View.GONE);
                fifthView.setVisibility(View.GONE);
                lastView.setVisibility(View.GONE);
                Title = "INVOICE DASHBOARD";
                tvTopbarTitle.setText(Title);
                iv_userDashBoard.setBackgroundResource(0);
                iv_userDashBoard.setImageResource(R.drawable.kippin_invoicing_logo);
                //iv_userDashBoard.setBackgroundResource(R.drawable.kippin_invoicing_logo);
                //iv_contactUs.setBackgroundColor(Color.parseColor("#187BC5"));
                iv_star.setVisibility(View.GONE);
            } else {
                iv_userDashBoard.setBackgroundResource(0);
                iv_userDashBoard.setImageResource(R.drawable.wallet_logo);
                //iv_userDashBoard.setBackgroundResource(R.drawable.dialog_logo);
                // iv_aboutUs.setBackgroundColor(Color.parseColor("#E7A508"));

            }
       /* } catch (Exception ex) {
            // Log.e("",ex.getMessage());
        }*/
    }

    private void updateUI() {

    }

    private void addListener() {
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(GravityCompat.START)) {

                    if (!InvoiceUserType.equals("true")) {
                        isMenuClicked = true;
                        getAllNotification();
                    }
                    drawer.openDrawer(GravityCompat.START);
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    layout_notification.setVisibility(View.GONE);
                }
            }
        });
        layout_userDashBoard.setOnClickListener(this);
        layout_loyalty.setOnClickListener(this);
        layout_voucher.setOnClickListener(this);
        layout_giftcards.setOnClickListener(this);
        layout_contactUs.setOnClickListener(this);
        layout_aboutUs.setOnClickListener(this);
        layout_UpdatePassword.setOnClickListener(this);
        iv_deleteAll.setOnClickListener(this);
        iv_updateProfile.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        ivLogout.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtility.isUserDashBoard = true;

        if (loaded == null) {
            loadDashboard = false;
            if (InvoiceUserType.equals("false")) {
                iv_home.setVisibility(View.GONE);
                iv_updateProfile.setVisibility(View.VISIBLE);
                displayFragMent(FragmentUserDashBoard.newInstance());
            } else {
                displayFragMent(FragmentInvoiceDashBoard.newInstance());
            }
        } else {
            if (CommonUtility.UserType.equals("1")) {
                if (Title == null) {
                    Title = "USER DASHBOARD";
                }
                tvTopbarTitle.setText(Title);
                if (CommonUtility.HomeButtonInsideView) {
                    iv_updateProfile.setVisibility(View.VISIBLE);
                    ivLogout.setVisibility(View.GONE);
                    iv_home.setVisibility(View.GONE);
                    CommonUtility.HomeButtonInsideView = false;
                } else {
                    if (firstTime) {
                        iv_updateProfile.setVisibility(View.GONE);
                        ivLogout.setVisibility(View.GONE);
                        iv_home.setVisibility(View.VISIBLE);
                    } else {
                        iv_updateProfile.setVisibility(View.VISIBLE);
                        ivLogout.setVisibility(View.GONE);
                        iv_home.setVisibility(View.GONE);
                    }
                }
            } else if (CommonUtility.UserType.equals("2")) {
                if (Title != null) {
                    Title = "INVOICE DASHBOARD";
                }
                tvTopbarTitle.setText(Title);
                iv_updateProfile.setVisibility(View.GONE);
                if (CommonUtility.HomeButtonInsideView) {
                    ivLogout.setVisibility(View.VISIBLE);
                    iv_home.setVisibility(View.GONE);
                    CommonUtility.HomeButtonInsideView = false;
                } else {
                    if (firstTime) {
                        ivLogout.setVisibility(View.GONE);
                        iv_home.setVisibility(View.VISIBLE);
                    } else {
                        ivLogout.setVisibility(View.VISIBLE);
                        iv_home.setVisibility(View.GONE);
                    }
                }
            } else {
                tvTopbarTitle.setText(Title);
            }

            displayFragMent(loaded);
        }
    }

    public void buttons(String title) {
        loaded = null;
        tvTopbarTitle.setText(title);
        iv_updateProfile.setVisibility(View.GONE);
        ivLogout.setVisibility(View.VISIBLE);
        iv_home.setVisibility(View.GONE);
    }

    private void displayFragMent(Fragment fragment) {
        this.lastFragment = fragment;
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.container_dashboard, fragment, fragment.getClass().getCanonicalName());
            transaction.commit();
            loaded = fragment;
        } catch (Exception ex) {
            Log.e("159  ", ex.getMessage() + "  UserDashBoardActivity");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();

            com.kippin.utils.Utility.goToHome((Activity) this);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_userDashBoard:
                firstTime = false;
                loaded = null;
               /*if(lastFragment==null || (lastFragment!=null && lastFragment instanceof  FragmentUserDashBoard)){*/
                Intent intent = new Intent(UserDashBoardActivity.this, RegistrationActivity.class);
                startActivity(intent);
                // CommonUtility.moveToTarget(this,RegistrationActivity.class);
                finish();
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

                break;
            case R.id.layout_loyalty:
                firstTime = true;
                CommonUtility.dashboard = false;
                CommonUtility.aboutsUsClicked = false;
                CommonUtility.contactusClicked = false;
                CommonUtility.giftcardClicked = false;
                CommonUtility.layaltycardClicked = true;
                CommonUtility.voucherclicked = false;
                displayFragMent(FragmentLoyalty.newInstance());
                Title = "MY LOYALTY CARDS";
                tvTopbarTitle.setText(Title);
                iv_updateProfile.setVisibility(View.GONE);
                iv_home.setVisibility(View.VISIBLE);
                ivLogout.setVisibility(View.GONE);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.layout_voucher:
                firstTime = true;
                CommonUtility.dashboard = false;
                CommonUtility.aboutsUsClicked = false;
                CommonUtility.contactusClicked = false;
                CommonUtility.giftcardClicked = false;
                CommonUtility.layaltycardClicked = false;
                CommonUtility.voucherclicked = true;
                displayFragMent(FragmentVoucher.newInstance());
                Title = "MERCHANT LIST";
                tvTopbarTitle.setText(Title);
                iv_updateProfile.setVisibility(View.GONE);
                iv_home.setVisibility(View.VISIBLE);
                ivLogout.setVisibility(View.GONE);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.layout_giftcards:
                firstTime = true;
                CommonUtility.dashboard = false;
                CommonUtility.aboutsUsClicked = false;
                CommonUtility.contactusClicked = false;
                CommonUtility.giftcardClicked = true;
                CommonUtility.layaltycardClicked = false;
                CommonUtility.voucherclicked = false;
                displayFragMent(FragmentGiftCard.newInstance());
                Title = "MY GIFT CARDS";
                tvTopbarTitle.setText(Title);
                iv_updateProfile.setVisibility(View.GONE);
                iv_home.setVisibility(View.VISIBLE);
                ivLogout.setVisibility(View.GONE);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.layout_contactUs:
                firstTime = true;
                CommonUtility.dashboard = false;
                CommonUtility.aboutsUsClicked = false;
                CommonUtility.contactusClicked = true;
                CommonUtility.giftcardClicked = false;
                CommonUtility.layaltycardClicked = false;
                CommonUtility.voucherclicked = false;
                displayFragMent(FragmentContactUs.newInstance());
                Title = "CONTACT US";
                tvTopbarTitle.setText(Title);
                iv_updateProfile.setVisibility(View.GONE);
                iv_home.setVisibility(View.VISIBLE);
                ivLogout.setVisibility(View.GONE);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.layout_aboutUs:
                firstTime = true;
                CommonUtility.dashboard = false;
                CommonUtility.aboutsUsClicked = true;
                CommonUtility.contactusClicked = false;
                CommonUtility.giftcardClicked = false;
                CommonUtility.layaltycardClicked = false;
                CommonUtility.voucherclicked = false;
                displayFragMent(FragmentAboutUs.newInstance());
                Title = "ABOUT US";
                tvTopbarTitle.setText(Title);
                iv_updateProfile.setVisibility(View.GONE);
                iv_home.setVisibility(View.VISIBLE);
                ivLogout.setVisibility(View.GONE);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.layout_UpdatePassword:
                firstTime = true;
                displayFragMent(FragmentUpdatePassword.newInstance());
                Title = "UPDATE PASSWORD";
                tvTopbarTitle.setText(Title);
                iv_updateProfile.setVisibility(View.GONE);
                ivLogout.setVisibility(View.GONE);
                iv_home.setVisibility(View.VISIBLE);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.iv_deleteAll:
                deleteAllNotification();
                break;
            case R.id.iv_updateProfile:
                CommonUtility.startNewActivity(this, UserProfileUpdateActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.iv_home_retail:
                firstTime = false;
                iv_home.setVisibility(View.GONE);
                drawer.closeDrawer(GravityCompat.START);
                if (CommonUtility.UserType.equals("1")) {
                    iv_updateProfile.setVisibility(View.VISIBLE);
                    ivLogout.setVisibility(View.GONE);
                    Title = "USER DASHBOARD";
                    tvTopbarTitle.setText(Title);
                    loaded = null;
                    displayFragMent(FragmentUserDashBoard.newInstance());
                } else {
                    iv_updateProfile.setVisibility(View.GONE);
                    ivLogout.setVisibility(View.VISIBLE);
                    Title = "INVOICE DASHBOARD";
                    tvTopbarTitle.setText(Title);
                    loaded = null;

                    displayFragMent(FragmentInvoiceDashBoard.newInstance());
                }

                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.ivLogout:
                MessageDialog.showDialog(UserDashBoardActivity.this, R.string.logout_message, new PromtListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSubmit() {
                        Prefs.with(UserDashBoardActivity.this).removeAllInvoice();
                        DbNew.getInstance(UserDashBoardActivity.this).deleteAllEntries();
                        CommonData.resetInvoiceData();
                        Intent in = new Intent();
                        in.setClass(UserDashBoardActivity.this, RegistrationActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                    }
                });
                break;
        }

    }


    private void getAllNotification() {
        String userId = String.valueOf(CommonData.getUserData(this).getId());
        if (isMenuClicked) {
            LoadingBox.showLoadingDialog(UserDashBoardActivity.this, "Loading ...");

        }

        RestClient.getApiServiceForPojo().getAllNotification(userId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());
                if (isMenuClicked) {
                    LoadingBox.dismissLoadingDialog();
                    isMenuClicked = false;
                }

                Gson gson = new Gson();
                final List<ResponseToGettAllNotification> responseToGettAllNotifications = gson.fromJson(jsonElement.toString(), new TypeToken<List<ResponseToGettAllNotification>>() {
                }.getType());
                boolean flag = Utility.isResponseValid(responseToGettAllNotifications);
                if (flag) {
                    layout_notification.setVisibility(View.VISIBLE);
                    iv_star.setVisibility(View.VISIBLE);
                    Log.e("Starting size:", "" + responseToGettAllNotifications.size());
                    /*mySideBaseAdapter = new MySideBaseAdapter(responseToGettAllNotifications, UserDashBoardActivity.this, new OnNOtificationDelete() {
                        @Override
                        public void afterDelete() {
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            if (drawer.isDrawerOpen(GravityCompat.START)) {
                                drawer.closeDrawer(GravityCompat.START);
                            } else {


                            }
                        }
                    });*/
                    adapter = new Notification_Adapter(responseToGettAllNotifications, UserDashBoardActivity.this, new OnNOtificationDelete() {
                        @Override
                        public void afterDelete() {
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            if (drawer.isDrawerOpen(GravityCompat.START)) {
                                drawer.closeDrawer(GravityCompat.START);
                            } else {


                            }
                        }
                    });
                    list_notification.setAdapter(adapter);
                } else {
                    // show star for promotion
                    responseToGettAllNotifications.clear();
                    list_notification.setAdapter(null);
                    layout_notification.setVisibility(View.GONE);
                    iv_star.setVisibility(View.GONE);


                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("RestClient", error.getUrl());
                if (isMenuClicked) {
                    LoadingBox.dismissLoadingDialog();
                    isMenuClicked = false;
                }
                MessageDialog.showDialog(UserDashBoardActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);

            }
        });
    }

    private void deleteAllNotification() {
        final String userId = String.valueOf(CommonData.getUserData(this).getId());
        MessageDialog.showVerificationDialog(this, "Are you sure you want to delete Notifications?", new DialogListener() {
            @Override
            public void handleYesButton() {
                LoadingBox.showLoadingDialog(UserDashBoardActivity.this, "Loading ...");
                RestClient.getApiServiceForPojo().deleteAllNotification(userId, "", new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());

                        LoadingBox.dismissLoadingDialog();
                        Gson gson = new Gson();
                        Boolean result = (Boolean) gson.fromJson(jsonElement.toString(), Boolean.class);
                        if (result.booleanValue()) {
                            MessageDialog.showDialog(UserDashBoardActivity.this, "All Notofications Deleted", false);
                            layout_notification.setVisibility(View.GONE);
                            list_notification.setAdapter(null);
                            iv_star.setVisibility(View.GONE);
                            if (drawer.isDrawerOpen(GravityCompat.START)) {
                                drawer.closeDrawer(GravityCompat.START);
                            }
                        } else {
                            MessageDialog.showDialog(UserDashBoardActivity.this, "Fail to Delete Notification");
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("RestClient", error.getUrl());
                        LoadingBox.dismissLoadingDialog();
                        MessageDialog.showDialog(UserDashBoardActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtility.dashboard = false;
        CommonUtility.aboutsUsClicked = false;
        CommonUtility.contactusClicked = false;
        CommonUtility.giftcardClicked = false;
        CommonUtility.layaltycardClicked = false;
        CommonUtility.voucherclicked = false;
    }
}
