package com.kippinretail;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.config.Utils;

public class GiftCardGiftedToMeActivity extends SuperActivity implements View.OnClickListener{

    private RelativeLayout backArrowLayout;
    private String customerID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card_gifted_to_me);
        initilization();
    }
    private void initilization()
    {
        backArrowLayout = (RelativeLayout)findViewById(R.id.backArrowLayout);
        customerID = String.valueOf(CommonData.getUserData(GiftCardGiftedToMeActivity.this).getId());
        backArrowLayout.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backArrowLayout:
                Utils.backPressed(GiftCardGiftedToMeActivity.this);
                break;

        }
    }
}
