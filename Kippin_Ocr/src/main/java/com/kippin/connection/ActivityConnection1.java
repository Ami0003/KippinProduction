package com.kippin.connection;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippin.connection.banks.FragmentBanks;
import com.kippin.connection.connectionOption.FragmentConnectionOptions;
import com.kippin.connection.connector.OnConnectionOptionSelected;
import com.kippin.kippin.R;
import com.kippin.storage.SharedPref;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;




public class ActivityConnection1 extends SuperActivity implements OnConnectionOptionSelected,View.OnClickListener{


//    private FragmentConnectionOptions fragmentConnectionOptions;
//    private FragmentBanks fragmentBanks;
//    @Bind(R.id.ivBack)
//    public ImageView ivBack;
//    @Bind(R.id.tvUserMenu)
//    public TextView tvUserMenu;
//
//
//
//    @Bind(R.id.ivOptionMenu)
//    public ImageView ivOptionMenu;
//
//
//    @Bind(R.id.rlMenuOptions)
//    public RelativeLayout rlMenuOptions;
//
//
//    @Bind(R.id.llMenuUserOptions)
//    public LinearLayout llMenuUserOptions;
//
//    boolean isMenuOpened = false;
//    boolean isUserMenuOpened = false;
//
//    @Bind(R.id.tvStatementReconcilation)
//    TextView tvStatementReconcilation;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_connection);
        generateActionBar(R.string.bank_connection, true, true);
        initialiseUI();
    }


    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE=1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if(requestCode==FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    private void loginUser() {
        WebView view = (WebView)findViewById(R.id.webView);
//        String postData = "Username="+ SharedPref.getString(SharedPref.USERNAME,"")+"&Password="+SharedPref.getString(SharedPref.PASSWORD,"");
//        view.getSettings().setJavaScriptEnabled(true);
//        view.setWebChromeClient(new WebChromeClient());
//        view.setWebViewClient(new WebViewClient());
//        view.postUrl(Url.URL_ACTIVITY_CONNECTION, EncodingUtils.getBytes(postData, "BASE64"));

//        String postData = "Username="+ SharedPref.getString(SharedPref.USERNAME,"")+"&Password="+SharedPref.getString(SharedPref.PASSWORD,"");

        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setAllowContentAccess(true);
        view.getSettings().setAllowFileAccess(true);

        view.getSettings().setBuiltInZoomControls(true);
        view.getSettings().setPluginState(WebSettings.PluginState.ON);
        view.getSettings().setAllowFileAccess(true);
        view.getSettings().setSupportZoom(true);

        //view.getSettings().setAllowUniversalAccessFromFileURLs(true);
        view.getSettings().setAppCacheEnabled(true);

//        view.setWebChromeClient(new WebChromeClient());
//        view.setWebViewClient(new WebViewClient());



        view.setWebChromeClient(new MyWebChromeClient());
        view.setWebViewClient(new myWebClient());

        view.loadUrl(Url.URL_ACTIVITY_CONNECTION + "/" + SharedPref.getString(SharedPref.USERNAME, "") + "/" + SharedPref.getString(SharedPref.PASSWORD, ""));
        Log.e("url", Url.URL_ACTIVITY_CONNECTION + "/" + SharedPref.getString(SharedPref.USERNAME, "") + "/" + SharedPref.getString(SharedPref.PASSWORD, ""));
    }

    class  MyWebChromeClient extends WebChromeClient {
        //The undocumented magic method override
        //Eclipse will swear at you if you try to put @Override here
        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            ActivityConnection1.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);

        }

        // For Android 3.0+
        public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            ActivityConnection1.this.startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE);
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            ActivityConnection1.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), ActivityConnection1.FILECHOOSER_RESULTCODE );

        }

    };



    //flipscreen not loading again
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }




    class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);


        }
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        loginUser();
    }

    @Override
    public void setUpListeners() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDownloadSelected() {

    }

    @Override
    public void onUploadSelected() {

    }

///////////////////////////////////////////////////////////////////////////////////


//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_connection_new);
//        initialiseUI();
//
//        switchScreen(fragmentConnectionOptions);
//        setUpListeners();
//
//        tvUserMenu.setText(" " + Singleton.getUser().getUsername() + " ");
//
////        ivBack.setVisibility(View.GONE);
//
//
//        generateActionBar(R.string.bank_connection, true, true);
//
//
//        if (!Singleton.getUser().getAccountantId().equalsIgnoreCase("")) {
//            tvStatementReconcilation.setVisibility(View.GONE);
//        }
//    }
//
//
//    public void switchMenuState(){
//
//        if(isMenuOpened)
//            closeMenu();
//        else
//            openMenu();
//
//
//    }
//
//    private void switchUserMenu() {
//        if(isUserMenuOpened)
//            closeUserMenu();
//        else
//            openUserMenu();
//    }
//
//    private void closeUserMenu() {
//        if(!isUserMenuOpened) return;
//
//        llMenuUserOptions.setVisibility(View.GONE);
//        isUserMenuOpened= false;
//    }
//
//    private void openUserMenu() {
//        if(isUserMenuOpened) return;
//
//        closeMenu();
//
//        llMenuUserOptions.setVisibility(View.VISIBLE);
//        isUserMenuOpened= true;
//    }
//
//    private void openMenu() {
//        if(isMenuOpened) return;
//
//        closeUserMenu();
//
//        rlMenuOptions.setVisibility(View.VISIBLE);
//        isMenuOpened = true;
//    }
//
//    private void closeMenu() {
//        if(!isMenuOpened) return;
//
//        rlMenuOptions.setVisibility(View.GONE);
//        isMenuOpened = false;
//    }
//
//    @Override
//    public void initialiseUI() {
//        super.initialiseUI();
//        fragmentBanks = new FragmentBanks();
//        fragmentConnectionOptions = new FragmentConnectionOptions();
//
//    }
//
//    @Override
//    public void setUpListeners() {
////        ivBack.setOnClickListener(this);
//        ivOptionMenu.setOnClickListener(this);
//        tvUserMenu.setOnClickListener(this);
//    }
//
//    @Override
//    public void onDownloadSelected() {
////        new DialogBox(this,"Coming Soon...",(DialogBoxListener)null);
//        switchScreen(fragmentBanks);
//        closeUnwantedMenus();
//    }
//
//    private void closeUnwantedMenus() {
//        closeMenu();
//    }
//
//    @Override
//    public void onUploadSelected() {
////        new DialogBox(this,"Coming Soon...",(DialogBoxListener)null);
//        switchScreen(fragmentBanks);
//        closeUnwantedMenus();
//    }
//
//    public void comingSoon(View c){
//        new DialogBox(this,"Coming Soon...",(DialogBoxListener)null);
//    }
//
//    public void switchScreen(Fragment fragment){
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        try {
//            ft.replace(R.id.container, fragment);
//        }catch (Exception e){
//            ft.add(R.id.container, fragment);
//        }
//        ft.commit();
//
//    }
//
//    public void pop(){
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        Fragment fragment = fm.findFragmentById(R.id.container);
//        ft.remove(fragment);
//        ft.commit();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.tvUserMenu:
//                switchUserMenu();
//                break;
//
//            case R.id.ivOptionMenu:
//                switchMenuState();
//                break;
//
//        }
//    }


}
