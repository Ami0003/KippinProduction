package com.pack.kippin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.Utility;



/**
 * Created by dilip.singh on 1/21/2016.
 */
public class AccountWithWallet extends SuperActivity {


   // @Bind(R.id.account_btn)
    ImageView imageViewAccount;
    //@Bind(R.id.wallet_btn)
    ImageView imageViewWallet;
    DialogBox dialogBox;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.checkCrashTracker(AccountWithWallet.this);
        setContentView(R.layout.layout_account_wallet);
        initialiseUI();
        setUpListeners();
       // ButterKnife.bind(this);

        generateActionBar(R.string.kipp_in_suit, true, false);

    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        imageViewAccount =(ImageView)findViewById(R.id.account_btn);
        imageViewWallet =(ImageView)findViewById(R.id.wallet_btn);
    }

    //@OnClick({R.id.account_btn,R.id.wallet_btn})
    public void clickButtons(View view){
        if (view.getId()==R.id.account_btn){

            Utility.startActivityFinish(AccountWithWallet.this, RegisterScreen.class, true);
        }
        else {
            dialogBox = new DialogBox(getResources().getString(R.string.work_in_progress),AccountWithWallet.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Utility.startActivityFinish(AccountWithWallet.this, LoginScreen.class, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setUpListeners() {

    }
}
