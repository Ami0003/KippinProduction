package com.kippin.reconcile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kippin.activities.ActivitySelectDateRange;
import com.kippin.connectedbankacc.ConnectedBankAccount;
import com.kippin.connectedbankacc.adapter.BankAdapter;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.Utility;


public class ActivityReconcile extends SuperActivity {

    //@Bind(R.id.ivKippinAcc)
    ImageView ivKippinAcc;

    //  @Bind(R.id.ivKippinWallet)
    ImageView ivKippinWallet;
    ImageView finance;

    BankAdapter bankAdapter;

    ImageView ivKippinIncomeStatement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.checkCrashTracker(ActivityReconcile.this);
        setContentView(R.layout.activity_reconcile_finance);
        generateActionBar(R.string.select_statement, true, true);

        initialiseUI();
        setUpListeners();
    }

    @Override
    public void setUpListeners() {
        ivKippinAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.isCreditCard = false;
                Utility.startActivity(ActivityReconcile.this, ConnectedBankAccount.class, true);

            }
        });

        ivKippinWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Utility.IS_CREDIT_CARD, true);
                Singleton.isCreditCard = true;
                Utility.startActivity(ActivityReconcile.this, ConnectedBankAccount.class, bundle, true);
            }
        });

        ivKippinIncomeStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.startActivity(ActivityReconcile.this, ActivitySelectDateRange.class, true);
            }
        });
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        bankAdapter = new BankAdapter(this);
        ivKippinAcc = (ImageView) findViewById(R.id.ivKippinAcc);
        ivKippinWallet = (ImageView) findViewById(R.id.ivKippinWallet);
        ivKippinIncomeStatement = (ImageView) findViewById(R.id.ivKippinIncomeStatement);
        finance = (ImageView) findViewById(R.id.finance);
        finance.setImageResource(R.drawable.kippin_accounting_logo);
    }

    //OnClick(R.id.ivKippinWallet)
    public void onWalletClick(View x) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(Utility.IS_CREDIT_CARD, true);
        Singleton.isCreditCard = true;
        Utility.startActivity(ActivityReconcile.this, ConnectedBankAccount.class, bundle, true);


    }

    //@OnClick(R.id.ivKippinAcc)
    public void onAccountClick(View x) {
        Singleton.isCreditCard = false;
        Utility.startActivity(ActivityReconcile.this, ConnectedBankAccount.class, true);
    }

    //@OnClick(R.id.ivKippinIncomeStatement)
    public void onKippinIncomeStatementClick(View x) {
        Utility.startActivity(ActivityReconcile.this, ActivitySelectDateRange.class, true);
    }


}
