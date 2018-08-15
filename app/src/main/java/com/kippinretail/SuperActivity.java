package com.kippinretail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Retail_AutoLogoutManager;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.dialogbox.DialogBoxListener;
import com.kippinretail.app.Retail;
import com.kippinretail.callbacks.CommonCallback;
import com.kippinretail.callbacks.TopbarContentCallacks;
import com.kippinretail.config.Config;
import com.kippinretail.config.Utils;
import com.kippinretail.fragment.FragmentTopbar;
import com.kippinretail.retrofit.ApiService;
import com.kippinretail.retrofit.RestClientAdavanced;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kippin.utils.Singleton;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

import static com.kippinretail.ApplicationuUlity.CommonData.getInvoiceUserData;


//abstract public class SuperActivity extends AppCompatActivity implements  TopbarContentCallacks,CommonCallback {

public class SuperActivity extends AppCompatActivity implements TopbarContentCallacks, CommonCallback, OnHomeKeyPressed {


    public static boolean repeat = false;
    private static ArrayList<Activity> activities = new ArrayList<>();
    public String Supplier = "1";
    public String Customer = "2";
    public String Invoice = "1";
    public String Performa = "2";
    public String ReportSent = "1";
    public String ReportReceived = "2";
    public String SectionTypeSent = "Sent";
    public String SectionTypeReceived = "Received";
    protected Activity activity;
    protected Gson gson = new Gson();
    protected Handler handler = new Handler();
    FragmentTopbar fragmentTopbar;
    private String userId;
    private RelativeLayout rlBack, rlLogout, rlHome;
    private RelativeLayout rlRight;
    private TextView tvTitle;
    private String customerId;
    private boolean inVoiceOnly;

    public static String Base64Image(Bitmap profileBitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            profileBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] byteArray = stream.toByteArray();
            String imgString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            return imgString;
        } catch (Exception ex) {
            ex.fillInStackTrace();
        } catch (Error ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String Base64Image(Bitmap profileBitmap, int targetWidth, int targetHeight, Activity context) {
        try {
            Bitmap targetBitmap = CommonUtility.decodeSampledBitmapFromResource(profileBitmap, targetWidth, targetHeight);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            targetBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String imgString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            return imgString;
        } catch (Exception ex) {
            ex.fillInStackTrace();
        } catch (Error ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static Bitmap Decodebase64Image(String imgString) {

        byte[] byteArray = Base64.decode(imgString, Base64.DEFAULT);
        Bitmap mBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return mBitmap;
    }

    public static boolean emailValidator1(final String mailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        if (
                getClass().getCanonicalName().equalsIgnoreCase(DashBoardMerchantActivity.class.getCanonicalName())
                        || getClass().getCanonicalName().equalsIgnoreCase(ActivityDashboardCharity.class.getCanonicalName())
                        || getClass().getCanonicalName().equalsIgnoreCase(UserDashBoardActivity.class.getCanonicalName())
                ) {
            activities = new ArrayList<>();
        }
        activities.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
    }

    public void BackWithHOME(String name, View.OnClickListener onClickListener) {
        fragmentTopbar.BackTitleHome(name, onClickListener);
    }

    public void callNext() {
        fragmentTopbar.callCreateCustomerActivity();
    }

    public String getValue(String key) {
        return getIntent().getStringExtra(key);
    }

    public void generateActionBar(int title, boolean isBackPresent, boolean isLogoutPresent, boolean showBackAsUpdate) {
        if (fragmentTopbar != null) {
            fragmentTopbar.setTvTitle(getString(title));
        }
        if (Config.SCREEN_Reg == 1) {
            fragmentTopbar.RemoveRightAddButton();
        }
        fragmentTopbar.setOnHomeKeyPressedListeners((OnHomeKeyPressed) this);
        if (!isBackPresent) {

            fragmentTopbar.hideBack();
        } else {
        }

        if (isLogoutPresent || activities.size() > 1) {

            fragmentTopbar.hideLogout();
        } else {
            //   FragmentTopbar.isLogOutSet = false;
        }

        if (activities.size() > 1) {
            fragmentTopbar.showHome();
        }
        if (showBackAsUpdate) {
            fragmentTopbar.updateBackImageWithProfie();
        } else {
            fragmentTopbar.updateBackImageWithBackButton();
        }
        Log.e("Value:", "" + SelectUserTypeActivity.plus);
        if (SelectUserTypeActivity.plus == 1) {
            fragmentTopbar.Remove_RightAddButton();
            SelectUserTypeActivity.plus = 0;
        }
        if (SelectUserTypeActivity.plus == 2) {
            fragmentTopbar.Remove_RightAddButton();
            SelectUserTypeActivity.plus = 0;
        }
    }

    public void setLogoText(String logo) {
        fragmentTopbar.setTvTitle(logo);
    }

    public <T extends View> T findViewById(Class<T> t, int id) {
        return (T) findViewById(id);
    }

    public void setClickListeners(View.OnClickListener onClickListener, View... views) {

        for (int i = 0; i < views.length; i++) {
            views[i].setOnClickListener(onClickListener);
        }

    }

    protected String getText(TextView textView) {
        return textView.getText().toString();
    }

    protected String getText(EditText textView) {
        return textView.getText().toString();
    }
   /* public void generateRightText1(String name){
        rlLogout.setVisibility(View.GONE);
        rlHome.setVisibility(View.GONE);
        rlRight.setVisibility(View.GONE);
        tvTitle.setText(name);

    }*/

    @Override
    public void onInitialise(FragmentTopbar fragmentTopbar) {
        this.fragmentTopbar = fragmentTopbar;
    }

    protected void startDialog(String message) {
        startDialog(message, null);
    }

    public TypedInput getTypedInput(String data) {
        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return in;
    }

    protected void startDialog(int message) {
        startDialog(Retail.res.getString(message), null);
    }

    protected void startDialog(final String message, final DialogBoxListener dialogBoxListener) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                MessageDialog.showDialog(SuperActivity.this, message, dialogBoxListener);
            }
        });
    }

    public void generateTitleBackWithLogoutString(String name) {
        fragmentTopbar.generateRightWithLogoutVisible(name);
    }

    @Override
    public void setUpListeners() {
    }

    @Override
    public void initialiseUI() {
    }

    @Override
    public void setUpUI() {

    }

    public void generateTitleWithLogoutString(String name, View.OnClickListener onClickListener) {
        fragmentTopbar.generateRightLogoutVisible(name, onClickListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (CommonUtility.isPerformAutoLogout()) {
                Log.e("Time Start Now", "Time Start Now");
                Retail_AutoLogoutManager.getActiveSession(this).registerForAutoLogout(this);
            }

            CommonUtility.removeFocus(this);
        } catch (Exception ex) {
            Toast.makeText(this, "OnPause " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            Utils.hideKeyboard(this);
            if (Retail_AutoLogoutManager.isRegister()) {
                Log.e("Time Stop", "Time Stop");
                Retail_AutoLogoutManager.getActiveSession(this).unregisterForAutoLogout();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "OnResume " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

        } catch (Exception ex) {
            Toast.makeText(this, "OnStop " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public ApiService getApiServiceForPojo() {
        return RestClientAdavanced.getApiServiceForPojo();
    }

    public Callback<JsonElement> getCallback(Callback<JsonElement> jsonElementCallback) {
        return RestClientAdavanced.getCallback(jsonElementCallback);
    }

    public void generateRightText(String name, View.OnClickListener onClickListener) {
        fragmentTopbar.generateRightText(name, onClickListener);
    }

    public void moveLeftTowardsRight() {
        fragmentTopbar.MoveLeftTowardsRight();
    }

    @Override
    public void onHomeKey() {
        try {
            int size = activities.size();
            for (int i = 1; i < size; i++) {
                Activity activity = activities.get(i);
                activities.remove(i);
                activity.finish();

            }
            repeat = false;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (!repeat) {
                repeat = true;
                onHomeKey();
            }

        }
    }

    public String getUserId() {
        if (CommonUtility.UserType.equals("0")) {
            userId = "" + Singleton.getUser().getInvoiceId();
        } else {
            userId = "" + getInvoiceUserData(activity).getId();
        }
        return userId;
    }

    public String getCustomerId() {
        if (CommonUtility.UserType.equals("0")) {
            customerId = Singleton.getUser().getCustomerId();
        } else {
            customerId = getInvoiceUserData(activity).getCustomerId();

        }
        return customerId;
    }

    public boolean getInvoiceOnly() {
        if (CommonUtility.UserType.equals("0")) {
            if (Singleton.getUser().isIsOnlyInvoice() != null) {
                inVoiceOnly = Singleton.getUser().isIsOnlyInvoice();
            } else {
                inVoiceOnly = false;
            }

        } else {
            inVoiceOnly = CommonData.getInvoiceUserData(activity).getIsOnlyInvoice();
        }
        return inVoiceOnly;
    }

    public void generateRightText1(String name) {
        fragmentTopbar.generateRightText1(name);
    }

    public String getGenerateRightText1() {
        return fragmentTopbar.getGenerateRightText1();
    }

    public void generateRightAddButton(String name, String roleId) {
        fragmentTopbar.generateRightAddButton(name, roleId);
    }

    public void generateRightAddButton(String name) {
        fragmentTopbar.generateRightAddButton(name);
    }


}

