package com.kippin.connection;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.custompro.afilechooser.utils.FileUtils;
import com.kippin.app.Kippin;
import com.kippin.connection.banks.FragmentBanks;
import com.kippin.connection.connectionOption.FragmentConnectionOptions;
import com.kippin.connection.connector.OnConnectionOptionSelected;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.utils.excel.ExcelOperation;
import com.kippin.utils.excel.ExcelUtils;
import com.kippin.utils.excel.OnExcelResultListener;

import java.io.File;


public class ActivityConnection extends SuperActivity implements OnConnectionOptionSelected,View.OnClickListener{

    private FragmentConnectionOptions fragmentConnectionOptions;
    private FragmentBanks fragmentBanks;
 //   @Bind(R.id.ivBack)
    public ImageView ivBack;
 //   @Bind(R.id.tvUserMenu)
    public TextView tvUserMenu;

    //@Bind(R.id.ivOptionMenu)
    public ImageView ivOptionMenu;

    

   // @Bind(R.id.rlMenuOptions)
    public RelativeLayout rlMenuOptions;


   // @Bind(R.id.llMenuUserOptions)
    public LinearLayout llMenuUserOptions;

    boolean isMenuOpened = false;
    boolean isUserMenuOpened = false;

  //  @Bind(R.id.tvStatementReconcilation)
    TextView tvStatementReconcilation;

    private Banks banks;
    private final int OPEN_CHOOSER = 1002;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_new);
        initialiseUI();

        switchScreen(fragmentConnectionOptions);
        setUpListeners();

        tvUserMenu.setText(" " + Singleton.getUser().getUsername() + " ");
        generateActionBar(R.string.bank_connection, true, true);
        if (!Singleton.getUser().getAccountantId().equalsIgnoreCase("")) {
            tvStatementReconcilation.setVisibility(View.GONE);
        }


    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        super.initialiseUI();
        fragmentBanks = new FragmentBanks();
        fragmentConnectionOptions = new FragmentConnectionOptions();
        ivBack =(ImageView)findViewById(R.id.ivBack);
        tvUserMenu =(TextView)findViewById(R.id.tvUserMenu);
        ivOptionMenu =(ImageView)findViewById(R.id.ivOptionMenu);
        rlMenuOptions =(RelativeLayout)findViewById(R.id.rlMenuOptions);

        llMenuUserOptions =(LinearLayout)findViewById(R.id.llMenuUserOptions);
        tvStatementReconcilation =(TextView)findViewById(R.id.tvStatementReconcilation);
    }
    public void switchMenuState(){

        if(isMenuOpened)
            closeMenu();
        else
            openMenu();

    }

    private void switchUserMenu() {
        if(isUserMenuOpened)
            closeUserMenu();
        else
            openUserMenu();
    }

    private void closeUserMenu() {
        if(!isUserMenuOpened) return;

        llMenuUserOptions.setVisibility(View.GONE);
        isUserMenuOpened= false;
    }

    private void openUserMenu() {
        if(isUserMenuOpened) return;

        closeMenu();

        llMenuUserOptions.setVisibility(View.VISIBLE);
        isUserMenuOpened= true;
    }

    private void openMenu() {
        if(isMenuOpened) return;

        closeUserMenu();

        rlMenuOptions.setVisibility(View.VISIBLE);
        isMenuOpened = true;
    }

    private void closeMenu() {
        if(!isMenuOpened) return;

        rlMenuOptions.setVisibility(View.GONE);
        isMenuOpened = false;
    }


    @Override
    public void setUpListeners() {
//        ivBack.setOnClickListener(this);




//        ivOptionMenu.setOnClickListener(this);
//        tvUserMenu.setOnClickListener(this);
    }

    @Override
    public void onDownloadSelected() {
        new DialogBox(this,"Coming Soon...",(DialogBoxListener)null);
//        switchScreen(fragmentBanks);
//        closeUnwantedMenus();
    }

    private void closeUnwantedMenus() {
        closeMenu();
    }

    @Override
    public void onUploadSelected() {
//        new DialogBox(this,"Coming Soon...",(DialogBoxListener)null);
        switchScreen(fragmentBanks);
        closeUnwantedMenus();
    }

   public void comingSoon(View c){
       new DialogBox(this,"Coming Soon...",(DialogBoxListener)null);
   }

    public void switchScreen(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        try {
            ft.replace(R.id.container, fragment);
        }catch (Exception e){
            ft.add(R.id.container, fragment);
        }
        ft.commit();

    }

    public void pop(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentById(R.id.container);
        ft.remove(fragment);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.tvUserMenu){
            switchUserMenu();
        }else if(id==R.id.ivOptionMenu){
            switchMenuState();
        }
        /*switch (v.getId()){
            case R.id.tvUserMenu:
                switchUserMenu();
                break;

            case R.id.ivOptionMenu:
                switchMenuState();
                break;

        }*/
    }


    public void onRbcClick(View c)
    {
        banks = Banks.RBC;
        openFileChooser();
    }

    private void openFileChooser(){
        // Create the ACTION_GET_CONTENT Intent
        Intent getContentIntent = FileUtils.createGetContentIntent();

        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
        startActivityForResult(intent, OPEN_CHOOSER);
    }



    public void onBmoClick(View c)
    {
        banks = Banks.BMO;
        openFileChooser();
    }


    public void onCanadaTrustClick(View c)
    {
        banks = Banks.CANADA_TRUST;
        openFileChooser();
    }



    public void onCibcClick(View c)
    {
        banks = Banks.CIBC;
        openFileChooser();
    }


    public void onScotiaClick(View c)
    {
        banks = Banks.SCOTIA;
        openFileChooser();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case OPEN_CHOOSER:
                if (resultCode == Activity.RESULT_OK) {

                    final Uri uri = data.getData();

                    // Get the File path from the Uri
                    String path = FileUtils.getPath(this, uri);

                    // Alternatively, use FileUtils.getFile(Context, Uri)
                    if (path != null && FileUtils.isLocal(path)) {
                        File file = new File(path);

                        if(!file.getAbsolutePath().endsWith(".csv")){

                            new DialogBox(this, Kippin.res.getString(R.string.only_csv_supported),(DialogBoxListener)null);

                            return;
                        }

                        new ExcelUtils(this, file.getPath(), new OnExcelResultListener() {
                            @Override
                            public void onResult() {

                            }
                        }, ExcelOperation.READ_EXCEL).execute();

                    }
                }
                break;
        }
    }


}
