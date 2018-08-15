package com.kippinretail;


import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;

import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.callbacks.NotificationREveiver;

import java.util.ArrayList;

import notification.NotificationHandler;
import notification.NotificationUtil;

public class UserGiftCardActivity extends MyAbstractGridActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationHandler.getInstance().getNotificationForVoucher(this, new NotificationREveiver() {
            @Override
            public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

            }
        });
    }

    @Override
    public  void updateToolbar() {
        generateActionBar(R.string.title_user_giftCard_activity, true, true, false);
    }

    @Override
    public void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.my_gift_cards , "My Gift Cards")) ;
        modalGridElements.add(getElement(R.drawable.gift_cards_from_kippin_friends , "Gift Cards From Kippin Friends")) ;
        modalGridElements.add(getElement(R.drawable.purchase_new_gift_cards , "Purchase New Gift cards")) ;
        modalGridElements.add(getElement(R.drawable.transfer_gift_cards , "Transfer Gift cards")) ;
        modalGridElements.add(getElement(R.drawable.friends_blue , "Friends")) ;
        modalGridElements.add(getElement(R.drawable.donate_to_charity , "Donate To Charity")) ;

        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch(position){
                    case 0:
                       // MerchantsOfPurchasedGiftCardsActivity
                        i.setClass(UserGiftCardActivity.this , GiftCardListActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right,R.anim.animation_to_left);
                        break;
                    case 1:
                       // MerchantsOfPurchasedGiftCardsActivity
                        i.setClass(UserGiftCardActivity.this , KippinFriendGiftCardList.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right,R.anim.animation_to_left);
                        break;
                    case 2:
                        i.setClass(UserGiftCardActivity.this, SelectCountryActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 3:
                      //  TransferGiftCards_FriendListActivity
                        i.putExtra("shareType",ShareType.SHAREGIFTCARD);
                        i.setClass(UserGiftCardActivity.this , FriendListActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right,R.anim.animation_to_left);
                        break;
                    case 4:
                        i.setClass(UserGiftCardActivity.this, FriendsActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right,R.anim.animation_to_left);
                        break;
                    case 5:
                        //EnableMerchantVoucherActivity
                        i.putExtra("shareType", ShareType.DONATEGIFTCARD);
                        i.setClass(UserGiftCardActivity.this,ActivityDonateToCharity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right,R.anim.animation_to_left);
                        break;
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {
        if (IsVoucher) {

        }
        if (IsTradePoint) {

        }
        if (IsFriendRequest) {
            NotificationUtil.setNotification(4,gridView,true);
        }else {
            NotificationUtil.setNotification(4,gridView,false);
        }
        if (IstransferGiftCard) {
            NotificationUtil.setNotification(1,gridView,true);
        }else{
            NotificationUtil.setNotification(1,gridView,false);
        }
        if (IsNewMerchant) {

        }
        if (IsNonKippinPhysical) // See Non Kippin Physical
        {

            NotificationUtil.setNotification(0,gridView,true);
        }else{
            NotificationUtil.setNotification(0,gridView,false);
        }
        if (IsNonKippinLoyalty)  // see incoming Non Kippin Loyalty card
        {
            NotificationUtil.setNotification(5,gridView,true);
        }else{
            NotificationUtil.setNotification(5,gridView,false);
        }
    }





}