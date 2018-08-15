package com.kippinretail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityShareNonKippinLoyaltyCard extends AbstractListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateToolBar();
        updateUI();
        addListener();;
    }

    @Override
    public void updateToolBar() {

    }

    @Override
    public void updateUI() {
        generateActionBar(R.string.title_user_MyLoayaltyCardListActivity, true, true, false);
    }

    @Override
    public void addListener() {

    }
}
