package com.kippinretail.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.ActivityDashboardCharity;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.Charity_EditProfile;
import com.kippinretail.DashBoardMerchantActivity;
import com.kippinretail.KippinInvoice.CreateInvoiceCustomer;
import com.kippinretail.LoginActivity;
import com.kippinretail.MerchantUpdatedActivity;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.OnHomeKeyPressed;
import com.kippinretail.R;
import com.kippinretail.UserDashBoardActivity;
import com.kippinretail.UserProfileUpdateActivity;
import com.kippinretail.callbacks.CommonCallback;
import com.kippinretail.callbacks.TopbarContentCallacks;
import com.kippinretail.config.Config;
import com.kippinretail.config.Utils;
import com.kippinretail.sharedpreferences.Prefs;


/**
 * Created by gaganpreet.singh on 1/27/2016.
 */
public class FragmentTopbar extends Fragment implements CommonCallback, View.OnClickListener {

    public static boolean isLogOutSet = false;
    View view;
    Activity currentActivity;
    boolean isUpdateButtonSet = false;
    String[] array = {};
    private TextView tvTitle;
    private RelativeLayout rlBack, rlLogout, rlHome;
    private Dialog dialog;
    private ImageView ivBack, ivAddCustmer;
    private TopbarContentCallacks callacks;
    private boolean isBackenable = true;
    private TextView tvRight;
    private RelativeLayout rlRight;
    private String roleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        view = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.fragment_topbar, container, false);
        initialiseUI();
        setUpListeners();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callacks = (TopbarContentCallacks) getActivity();
        if (callacks != null) {
            callacks.onInitialise(this);
        }
        currentActivity = getActivity();
    }

    public void setTvTitle(String title) {
        tvTitle.setText(title.toUpperCase());
    }

    public void updateBackImageWithProfie() {
        ivBack.setImageResource(R.drawable.updateprofile);
        isUpdateButtonSet = true;

    }


    public void hideLogout() {
        onHideFunction(rlLogout);
    }

    public void generateRightLogoutVisible(String name, View.OnClickListener onClickListener) {
        rlLogout.setVisibility(View.GONE);
        rlHome.setVisibility(View.VISIBLE);
        rlRight.setVisibility(View.GONE);
        rlBack.setVisibility(View.GONE);
        tvTitle.setText(name);

        if (onClickListener != null)
            rlHome.setOnClickListener(onClickListener);

    }

    public void hideBack() {
        onHideFunction(ivBack);
        isBackenable = false;
    }

    private void onHideFunction(View v) {
        v.setVisibility(View.GONE);
        v.setEnabled(false);
        v.setOnClickListener(null);
    }

//    private void onHideFunction(View v, View Layout) {
//        v.setVisibility(View.GONE);
//        v.setEnabled(false);
//        Layout.setVisibility(View.GONE);
//        Layout.setEnabled(false);
//        v.setOnClickListener(null);
//    }


    @Override
    public void initialiseUI() {
        tvTitle = (TextView) view.findViewById(R.id.tvTopbarTitle);
        ivBack = (ImageView) view.findViewById(R.id.ivBack);
        rlBack = (RelativeLayout) view.findViewById(R.id.lalayout_ivBack);
        rlLogout = (RelativeLayout) view.findViewById(R.id.layout_ivLogout);
        rlHome = (RelativeLayout) view.findViewById(R.id.layout_ivHome);
        rlRight = (RelativeLayout) view.findViewById(R.id.layout_rightText);

        tvRight = (TextView) view.findViewById(R.id.tvRightText);
        ivAddCustmer = (ImageView) view.findViewById(R.id.ivAddCustmer);
        ivAddCustmer.setOnClickListener(this);
        if (Config.SCREEN_OPEN == 1) {
            ivAddCustmer.setVisibility(View.VISIBLE);
        } else if (Config.SCREEN_OPEN == 2) {
            ivAddCustmer.setVisibility(View.VISIBLE);
        } else {
            ivAddCustmer.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUpUI() {
    }


    @Override
    public void setUpListeners() {
        rlLogout.setOnClickListener(this);
        rlBack.setOnClickListener(this);
        rlHome.setOnClickListener(this);
    }

    public void BackTitleHome(String name, View.OnClickListener onClickListener) {
        rlLogout.setVisibility(View.GONE);
        rlHome.setVisibility(View.VISIBLE);
        rlRight.setVisibility(View.GONE);
        rlBack.setVisibility(View.VISIBLE);
        tvTitle.setText(name);
        if (onClickListener != null)
            rlHome.setOnClickListener(onClickListener);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lalayout_ivBack:
                if (isUpdateButtonSet) {
                    Intent i = null;
                    switch (CommonData.getUserData(getActivity()).getRoleId() + "") {

                        case Utility.ROLE_ID_CHARITY:
                            i = new Intent(currentActivity, Charity_EditProfile.class);
                            startActivity(i);

                            currentActivity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                            break;

                        case Utility.ROLE_ID_MERCHANT:
                            i = new Intent(currentActivity, MerchantUpdatedActivity.class);
                            startActivity(i);
                            currentActivity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                            break;

                        case Utility.ROLE_ID_USER:
                            i = new Intent(currentActivity, UserProfileUpdateActivity.class);
                            startActivity(i);
                            currentActivity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                            break;

                    }

                } else if (isBackenable) {
                    CommonUtility.HomeButtonInsideView = true;
                    Utils.backPressed(getActivity());
                } else {

                }

                break;

            case R.id.layout_ivLogout:
                CallLogout();
                break;

            case R.id.ivAddCustmer:

                callCreateCustomerActivity();
                break;

        }
    }


    public void generateRightWithLogoutVisible(String name) {
        rlLogout.setVisibility(View.VISIBLE);
        rlHome.setVisibility(View.GONE);
        rlRight.setVisibility(View.GONE);
        rlBack.setVisibility(View.VISIBLE);
        tvTitle.setText(name);
    }

    public void callCreateCustomerActivity() {
        Intent intent = new Intent();
        if (Config.SCREEN_OPEN == 1) {
            intent.setClass(getActivity(), CreateInvoiceCustomer.class);
            intent.putExtra("roleId", "5");
            intent.putExtra("Supplier", roleId);
            intent.putExtra("plus", "invoice_visible");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        } else if (Config.SCREEN_OPEN == 2) {
            intent.setClass(getActivity(), CreateInvoiceCustomer.class);
            intent.putExtra("roleId", "5");
            intent.putExtra("Supplier", roleId);
            intent.putExtra("plus", "supplier_visible");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        } else {
            intent.setClass(getActivity(), CreateInvoiceCustomer.class);
            intent.putExtra("roleId", "5");
            intent.putExtra("Supplier", roleId);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }


    }


    private void CallLogout() {

        dialog = new Dialog(currentActivity,
                R.style.Theme_AppCompat_Translucent);
        dialog.setContentView(R.layout.dialog_logout_);
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView textForMessage = (TextView) dialog.findViewById(R.id.textMessage);
        textForMessage.setText("Are you sure you want to logout");
        Button btnYES = (Button) dialog.findViewById(R.id.btnYES);
        Button btnNO = (Button) dialog.findViewById(R.id.btnNO);
        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonData.resetData();
                Log.d("Logout On Click", currentActivity.getClass().toString());
                Intent in = new Intent();
                in.setClass(currentActivity, LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                currentActivity.finish();
                currentActivity.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                Prefs.with(currentActivity).removeAll();
            }
        });
        dialog.show();
    }

    public void updateBackImageWithBackButton() {
        ivBack.setImageResource(R.drawable.arrow_l);
    }


    public void setOnHomeKeyPressedListeners(final OnHomeKeyPressed onHomeKeyPressed) {
        rlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtility.isUserDashBoard) {
                    /*((UserDashBoardActivity)getActivity()).buttons("USER DASHBOARD");*/
                    CommonUtility.HomeButtonInsideView = true;
                    UserDashBoardActivity.userDashBoardActivity.loadDashboard();
                    onHomeKeyPressed.onHomeKey();
                } else if (CommonUtility.isMerchantdashBoard) {
                    CommonUtility.HomeButtonInsideView = true;
                    CommonUtility.moveToTarget(getActivity(), DashBoardMerchantActivity.class);
                } else if (CommonUtility.isCharityDashBoard) {
                    CommonUtility.HomeButtonInsideView = true;
                    CommonUtility.moveToTarget(getActivity(), ActivityDashboardCharity.class);
                }

            }
        });
    }

    public void showHome() {
        rlHome.setVisibility(View.VISIBLE);
        if (CommonUtility.UserType.equals("1")) {
            UserDashBoardActivity.Title = "USER DASHBOARD";
        } else {
            UserDashBoardActivity.Title = "INVOICE DASHBOARD";
        }
    }

    public void generateRightText(String name, View.OnClickListener onClickListener) {
        rlLogout.setVisibility(View.GONE);
        rlHome.setVisibility(View.GONE);
        rlRight.setVisibility(View.VISIBLE);
        tvRight.setText(name);
        if (onClickListener != null)
            rlRight.setOnClickListener(onClickListener);
    }

    public void MoveLeftTowardsRight() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlBack.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlBack.setLayoutParams(params);
    }

    public void generateRightText1(String name) {
        rlLogout.setVisibility(View.GONE);
        rlHome.setVisibility(View.GONE);
        rlRight.setVisibility(View.GONE);
        tvTitle.setText(name);

    }

    public String getGenerateRightText1() {
        return tvTitle.getText().toString();
    }

    public void generateRightAddButton(String name, String roleId) {
        rlLogout.setVisibility(View.GONE);
        rlHome.setVisibility(View.GONE);
        rlRight.setVisibility(View.GONE);
        if (Config.SCREEN_OPEN == 1 || Config.SCREEN_OPEN == 2) {
            if (name.equals("CREATE CUSTOMER") || name.equals("CREATE SUPPLIER")) {
                ivAddCustmer.setVisibility(View.GONE);
            } else {
                ivAddCustmer.setVisibility(View.VISIBLE);
            }
        } else {
            ivAddCustmer.setVisibility(View.GONE);
        }
        if (Config.SCREEN_Reg == 1) {
            Config.SCREEN_Reg = 0;
            ivAddCustmer.setVisibility(View.GONE);
            rlLogout.setVisibility(View.GONE);
            rlHome.setVisibility(View.GONE);
            rlRight.setVisibility(View.GONE);
        }
        Log.e("name:", "" + name);
        this.roleId = roleId;
        tvTitle.setText(name);
    }

    public void RemoveRightAddButton() {
        if (Config.SCREEN_Reg == 1) {
            Config.SCREEN_Reg = 0;
            ivAddCustmer.setVisibility(View.GONE);
            rlLogout.setVisibility(View.GONE);
            rlHome.setVisibility(View.GONE);
            rlRight.setVisibility(View.GONE);
        }
    }

    public void Remove_RightAddButton() {
        ivAddCustmer.setVisibility(View.GONE);
        rlLogout.setVisibility(View.GONE);
        rlHome.setVisibility(View.GONE);
        rlRight.setVisibility(View.GONE);
    }

    public void generateRightAddButton(String name) {
        ivAddCustmer.setVisibility(View.GONE);
    }
}
