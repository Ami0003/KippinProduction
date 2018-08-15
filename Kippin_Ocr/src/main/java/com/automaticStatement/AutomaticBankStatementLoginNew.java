package com.automaticStatement;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;

public class AutomaticBankStatementLoginNew extends SuperActivity {

    ImageView ivOk;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_automatic_bank_statement_login_new);

        setUpListeners();



    }

    @Override
    public void setUpListeners() {
        ivOk = (ImageView)findViewById(R.id.ivOk) ;
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
