package com.kippin.dashboard;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fragment.FragmentUpdatePassword;
import com.fragment.FragmentWebView;
import com.kippin.activities.FragmentSelectDateRange;
import com.kippin.app.Kippin;
import com.kippin.connectedbankacc.FragmentConnectedBankAccount;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.ImagePickUtility.CapturePicView;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.TemplateData;
import com.pack.kippin.PaymentDetailsClass;

public class ActivityDashboard extends SuperActivity {


    //    static ArrayList<SuperFragment > superFragments = new ArrayList<>();
    public static ActivityDashboard activityDashboard;
    static boolean isCaontact, isaboutUsClicked;
    static Fragment fragment = null;
    public boolean loadDashboard = false;
    //   @Bind(R.id.frame_container)
    FrameLayout flContainer;
    // @Bind(R.id.side_option_dashboard)
    LinearLayout llDashboard;
    // @Bind(R.id.side_option_reconcile_bs)
    LinearLayout llReconcileBankStatement;
    //   @Bind(R.id.side_option_reconcile_cc)
    LinearLayout llReconcileCreditCardStatement;
    //   @Bind(R.id.side_option_generate_is)
    LinearLayout llGenerateIncomeStatement, side_option_contact_us, side_option_about_us,side_option_updatepassword;
    // @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    boolean isInitialised = false;
    boolean isStatusDialogOpened = false;
    ImageView ivMenu;
    Handler handler = new Handler();

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        loadFragment(new FragmentDashboard());
        flContainer = (FrameLayout) findViewById(R.id.frame_container);
        llDashboard = (LinearLayout) findViewById(R.id.side_option_dashboard);
        llReconcileBankStatement = (LinearLayout) findViewById(R.id.side_option_reconcile_bs);
        llReconcileCreditCardStatement = (LinearLayout) findViewById(R.id.side_option_reconcile_cc);
        llGenerateIncomeStatement = (LinearLayout) findViewById(R.id.side_option_generate_is);
        side_option_contact_us = (LinearLayout) findViewById(R.id.side_option_contact_us);
        side_option_about_us = (LinearLayout) findViewById(R.id.side_option_about_us);
        side_option_updatepassword= (LinearLayout) findViewById(R.id.side_option_updatepassword);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (activityDashboard.loadDashboard) {
            activityDashboard.loadDashboard = false;
            loadDashboard();
        }

    }

    public void loadDashboard() {
        loadFragment(new FragmentDashboard());
    }

    @Override
    public void setUpListeners() {
        llDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isCaontact || isaboutUsClicked){
//                    loadFragment(new FragmentDashboard());
//                    isCaontact = false;
//                    isaboutUsClicked = false;
//                }else {
//                    Intent intent = new Intent(ActivityDashboard.this, Kippin.logout);
//                    startActivity(intent);
//                    finish();
//                }
                if (fragment == null || (fragment != null && fragment instanceof FragmentDashboard)) {
                    Intent intent = new Intent(ActivityDashboard.this, Kippin.logout);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                    ActivityDashboard.activityDashboard = null;
                } else {
                    Intent intent = new Intent(ActivityDashboard.this, Kippin.RegistrationSuiteClass);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                }
            }
        });
        llGenerateIncomeStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FragmentSelectDateRange());
            }
        });

        llReconcileBankStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.isCreditCard = false;
                loadFragment(new FragmentConnectedBankAccount());
            }
        });

        llReconcileCreditCardStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.isCreditCard = true;
                loadFragment(new FragmentConnectedBankAccount());
            }
        });
              //http://www.kippinitsimple.com/ContactUs?platform=1
        side_option_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //http://www.kippinitsimple.com/ContactUs?platform=1
                isCaontact = true;
                isaboutUsClicked = false;
                FragmentWebView fragmentWebView = new FragmentWebView();
                fragmentWebView.load("https://kippinitsimple.com/contact/", R.string.contact_us);
                loadFragment(fragmentWebView);
            }
        });
        side_option_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //http://www.kippinitsimple.com/AboutUs?platform=1
                isCaontact = false;
                isaboutUsClicked = true;
                FragmentWebView fragmentWebView = new FragmentWebView();
                fragmentWebView.load("https://www.kippinitsimple.com/index.html#about", R.string.about_us);
                loadFragment(fragmentWebView);
            }
        });
        side_option_updatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(FragmentUpdatePassword.newInstance());

            }
        });

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        isCaontact = false;
        isaboutUsClicked = false;
    }

    //   @OnClick(R.id.ivMenu)
    public void ivMenu(View v) {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    private void loadFragment(Fragment fragment) {
        this.fragment = fragment;
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        // update selected item and title, then close the drawer
        close();
    }

    private void open() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(Gravity.LEFT);
        }

    }

    private void close() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }

    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        switch (newConfig.orientation ){
//            case Configuration.ORIENTATION_PORTRAIT:
//                setContentView(R.layout.dashboard_with_menu);
//                initialiseUI();
//            break;
//
//            case Configuration.ORIENTATION_LANDSCAPE:
//                setContentView(R.layout.dashboard_with_menu);
//                break;
//
//
//        }
//
//        initialiseUI();
//        setUpListeners();
//        isStatusDialogOpened = false;
//        isInitialised = true;
//
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Utility.printLog("time out", System.currentTimeMillis() + "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_with_menu);

        activityDashboard = this;

        initialiseUI();
        setUpListeners();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//          fragment.onConfigurationChanged(newConfig);

        if (fragment.getClass() == FragmentDashboard.class) {
            new Handler().post(new Runnable() {
                public void run() {
                    loadFragment(new FragmentDashboard());
                }
            });

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkStatus();
    }

    private void checkStatus() {

        if (!isInitialised && !isStatusDialogOpened && Singleton.getUser().getId() != null) {
            WSUtils.hitServiceGet(this, Url.getStatusUrl(), new ArrayListPost(), TemplateData.class, new WSInterface() {
                @Override
                public void onResult(int requestCode, TemplateData data) {

                    isStatusDialogOpened = true;
                    if (data == null || data.data == null || data.data.length() == 0) {
                        isStatusDialogOpened = false;
                        return;
                    }

                    if (data.data.equalsIgnoreCase(UserStatus.Error.toString())) {
                        new DialogBox(ActivityDashboard.this, "An Error occured. Please login after sometime.", new DialogBoxListener() {
                            @Override
                            public void onDialogOkPressed() {
                                isStatusDialogOpened = false;
                                Utility.logout(ActivityDashboard.this, false);
                            }
                        });

                    } else if (Utility.isUserAllowed(data.data)) {
                        initialiseUI();
                        setUpListeners();
                        isStatusDialogOpened = false;
                        isInitialised = true;
                    } else {

//                        getIntent().getExtras().getBoolean("REDIRECT_TO_LOGIN");
                        new DialogBox(ActivityDashboard.this, "Please pay", new DialogBoxListener() {
                            @Override
                            public void onDialogOkPressed() {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("REDIRECT_TO_LOGIN", false);
                                Utility.startActivity(ActivityDashboard.this, PaymentDetailsClass.class, bundle, true);
                                isStatusDialogOpened = false;
                            }
                        });
                    }
                }
            });
        }

    }

    @Override
    public void onBackPressed() {

//        if(fragment.getClass() != FragmentDashboard.class){
//            loadFragment(new FragmentDashboard());
//        }else{
//            super.onBackPressed();
//        }


        Utility.goToHome(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case CapturePicView.REQUEST_CODE_CAMERA:
            case CapturePicView.REQUEST_CODE_GALLERY:
                fragment.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }
}
