package com.kippinretail;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kippinretail.Modal.ModalGridElement;

import java.util.ArrayList;

import notification.NotificationUtil;

public class VoucherActivity extends MyAbstractGridActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();
        ;
    }

    @Override
    public void updateToolbar() {
        generateActionBar(R.string.title_user_voucher_activity, true, true, false);
    }

    @Override
    public void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();

        modalGridElements.add(getElement(R.drawable.view_merchant_promotion_orange, "View Merchant Promotions"));
        modalGridElements.add(getElement(R.drawable.enable_disable_merchant_promotion_blue, "Enable/Disable Merchant Promotions"));
        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:
                        i.setClass(VoucherActivity.this, ViewMerchantVoucherActivity.class);
                        break;
                    case 1:
                        i.putExtra("parentButton", "enableMerchantVouchers");
                        i.setClass(VoucherActivity.this, EnableMerchantVoucherActivity.class);
                        break;

                }
                startActivity(i);
                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

        if (IsVoucher) {
            NotificationUtil.setNotification(0, gridView, true);
        } else {
            NotificationUtil.setNotification(0, gridView, false);
        }
        if (IsTradePoint) {

        }
        if (IsFriendRequest) {

        }
        if (IstransferGiftCard) {

        }
        if (IsNewMerchant) {

        }
        if (IsNonKippinPhysical) {

        }
        if (IsNonKippinLoyalty) {

        }

    }
}
