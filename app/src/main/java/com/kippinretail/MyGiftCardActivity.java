package com.kippinretail;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.kippinretail.config.Utils;

public class MyGiftCardActivity extends Activity implements View.OnClickListener {

    private RelativeLayout backArrowLayout;
    ListView listMerchant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gift_card);
        initilization();
    }

    private void initilization()
    {
        listMerchant = (ListView)findViewById(R.id.listMerchant);
        backArrowLayout = (RelativeLayout)findViewById(R.id.backArrowLayout);
        backArrowLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.backArrowLayout:
                Utils.backPressed(this);
                break;

        }
    }

}
