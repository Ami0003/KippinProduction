package com.kippinretail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.callbacks.NotificationREveiver;
import com.facebook.FacebookSdk;

import java.util.ArrayList;

import notification.NotificationHandler;
import notification.NotificationUtil;


public class UserTradePointActivity extends MyAbstractGridActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        updateUI();
        FacebookSdk.sdkInitialize(this);
    }

    @Override
    public void updateToolbar() {
        generateActionBar(R.string.title_user_tradepoint_activity, true, true, false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        NotificationHandler.getInstance().getNotificationForCards(this, new NotificationREveiver() {
            @Override
            public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {
               /* if (IsVoucher) {

                }
                if (IsTradePoint) {
                    NotificationUtil.setNotification(2,gridView,true);
                }else{
                     NotificationUtil.setNotification(2,gridView,false);
                }
                if (IsFriendRequest) {
                    NotificationUtil.setNotification(1,gridView,true);
                }else{
                     NotificationUtil.setNotification(1,gridView,false);
                }
                if (IstransferGiftCard) {

                }
                if (IsNewMerchant) {

                }
                if (IsNonKippinPhysical) {


                }
                if (IsNonKippinLoyalty) {
                    NotificationUtil.setNotification(0,gridView,true);
                }else{
                      NotificationUtil.setNotification(0,gridView,false);
                }*/
            }
        });
    }

    @Override
    public void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.trade_with_friends, "Trade with Friends"));
        modalGridElements.add(getElement(R.drawable.friends_orange, "Friends"));
        modalGridElements.add(getElement(R.drawable.incoming_trade_request, "Incoming Trade Request"));
        modalGridElements.add(getElement(R.drawable.outgoing_request, "Outgoing Trades"));
        modalGridElements.add(getElement(R.drawable.gift_to_charitable_organizations_blue, "Gift to Charitable Organizations"));
        modalGridElements.add(getElement(R.drawable.kippin_white_background_icon, "Gift to Charitable Organizations"));
        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:
                        //ActivityUserPunchCard
                        i.putExtra("shareType", ShareType.TRADEPOINT);
                        i.setClass(UserTradePointActivity.this, FriendListActivity.class);
                        break;
                    case 1:
                        //  i.setClass(UserTradePointActivity.this , EnableMerchantVoucherActivity.class);

                        i.setClass(UserTradePointActivity.this, FriendsActivity.class);
                        break;
                    case 2:
                        i.setClass(UserTradePointActivity.this, IncomingAndOutgoingTradeRequestActivity.class);
                        i.putExtra("parentButton", "incomingTrade");
                        break;
                    case 3:
                        i.setClass(UserTradePointActivity.this, IncomingAndOutgoingTradeRequestActivity.class);
                        i.putExtra("parentButton", "outGoingTrade");
                        break;
                    case 4:
                        i.putExtra("shareType", ShareType.DONATEPOINT);
                        i.setClass(UserTradePointActivity.this, ActivityDonateToCharity.class);
                        break;
                    case 5:
                        return;
                    //break;
                }
                startActivity(i);
                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Invited", "Invited");
    }


    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {
        if (IsVoucher) {

        }
        if (IsTradePoint) {
            NotificationUtil.setNotification(2, gridView, true);
        } else {
            NotificationUtil.setNotification(2, gridView, false);
        }
        if (IsFriendRequest) {
            NotificationUtil.setNotification(1, gridView, true);
        } else {
            NotificationUtil.setNotification(1, gridView, false);
        }
        if (IstransferGiftCard) {

        }
        if (IsNewMerchant) {

        }
        if (IsNonKippinPhysical) {


        }
        /*if (IsNonKippinLoyalty) {
            NotificationUtil.setNotification(0,gridView,true);
        }else{
            NotificationUtil.setNotification(0,gridView,false);
        }*/
    }
}
