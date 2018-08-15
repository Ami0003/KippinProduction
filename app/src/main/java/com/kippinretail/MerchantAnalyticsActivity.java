package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.kippinretail.Modal.ModalGridElement;

import java.util.ArrayList;

public class MerchantAnalyticsActivity extends MyAbstractGridActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();

    }

    @Override
    public void updateToolbar() {
        generateActionBar(R.string.title_merchant_MerchantAnalyticsActivity, true, true, false);
    }

    @Override
    public  void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.loyalty_analytics_blue , "Point Analytics")) ;
        modalGridElements.add(getElement(R.drawable.points_analytics , "Gift Card Analytics")) ;
        modalGridElements.add(getElement(R.drawable.gift_card_revenue_red , "Gift Card Revenue")) ;
        modalGridElements.add(getElement(R.drawable.cpmparsion_of_gift_card_activity , "Comparison of Gift Card Activity")) ;


        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch(position){
                    case 0:
                        i.setClass(MerchantAnalyticsActivity.this , MerchantActivity_PointAnalytics.class);
                        break;
                    case 1:
                        i.setClass(MerchantAnalyticsActivity.this , MerchantActivity_GigtCardAnalytics.class);
                        break;
                    case 2:
                        i.setClass(MerchantAnalyticsActivity.this , MerchantActivity_GiftCardUsageAnalytics.class);
                        break;
                    case 3:
                        i.setClass(MerchantAnalyticsActivity.this , MerchantActivity_ComparisonGiftCard.class);
                        break;
                   /* case 4:
                        i.setClass(MerchantAnalyticsActivity.this, MerchantActivity_WeekdayActivity.class);
                        break;
                    case 5:
                        i.setClass(MerchantAnalyticsActivity.this,MerchantActivity_ComparisonGiftCard.class);
                        break;*/
                }
                startActivity(i);
                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
            }
        });
    }


    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

    }
}
