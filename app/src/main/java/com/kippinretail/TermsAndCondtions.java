package com.kippinretail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.kippinretail.R;

/**
 * Created by kamaljeet.singh on 3/5/2016.
 */
public class TermsAndCondtions extends Activity implements View.OnClickListener {
    EditText refrelCodedEditText;
    Button submitButton, skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_conditions_activity);
        Initilization();
    }

    private void Initilization() {
        // EditText Id's
        refrelCodedEditText = (EditText) findViewById(R.id.refrelCodedEditText);
        //Buttons Id's
        submitButton = (Button) findViewById(R.id.submitButton);
        skipButton = (Button) findViewById(R.id.skipButton);
        submitButton.setOnClickListener(this);
        skipButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitButton:
                break;
            case R.id.skipButton:

                Intent in = new Intent();
                in.setClass(TermsAndCondtions.this, LoginActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }
}
