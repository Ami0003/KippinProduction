package com.kippinretail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kippinretail.Adapter.GridViewAdpter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.RequestType;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.callbacks.NotificationREveiver;

import java.util.ArrayList;

import notification.NotificationHandler;
import notification.NotificationUtil;


public class UserPointActivity extends MyAbstractGridActivity  {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        updateUI();
        updateToolbar();
        String path = CommonData.getUserData(this).getRefferCodepath();
    }


    @Override
    public  void updateToolbar() {
        generateActionBar(R.string.title_user_points, true, true, false);
    }

    @Override
    public  void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.my_loyalty_cards_b , "My Loyalty Cards")) ;
        modalGridElements.add(getElement(R.drawable.loyalty_program_sign_up , "Loyalty Program SignUp")) ;
        modalGridElements.add(getElement(R.drawable.trade_points , "Trade Points")) ;
        modalGridElements.add(getElement(R.drawable.loyalty_analytics , "Loyalty Analytics")) ;
        modalGridElements.add(getElement(R.drawable.share_non_kippin_loyalty_cards , "Share Non Kippin Loyalty Cards")) ;
        modalGridElements.add(getElement(R.drawable.incoming_non_kippin_loyalty_cards , "Incoming Non Kippin Loyalty Cards")) ;

        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:
                        //ActivityUserPunchCard
                        i.setClass(UserPointActivity.this, MyLoayaltyCardListActivity.class);
                        break;
                    case 1:
                        i.putExtra("parentButton","loyaltyProgramSignUp");
                        i.setClass(UserPointActivity.this, EnableMerchantVoucherActivity.class);
                        break;
                    case 2:
                        i.setClass(UserPointActivity.this, UserTradePointActivity.class);
                        break;
                    case 3:
                        i.setClass(UserPointActivity.this, UserAnalyticActivity.class);
                        break;
                    case 4:
                        i.putExtra("shareType", ShareType.LOYALTY);
                        i.setClass(UserPointActivity.this, FriendListActivity.class);
                        break;
                    case 5:
                        i.putExtra("RequestType", RequestType.LOYALTCARD);
                        i.setClass(UserPointActivity.this, IncomingFriendRequest.class);
                        break;
                }
                startActivity(i);
                overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        NotificationHandler.getInstance().getNotificationForCards(this, new NotificationREveiver() {
            @Override
            public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {
                /*if (IsVoucher) {

                }
                if (IsTradePoint) {
                    NotificationUtil.setNotification(2,gridView,true);
                }else{
                    //  NotificationUtil.setNotification(2,gridView,false);
                }
                if (IsFriendRequest) {
                    NotificationUtil.setNotification(2,gridView,true);
                }
                else{
                     // NotificationUtil.setNotification(2,gridView,false);
                }
                if (IstransferGiftCard) {

                }
                if (IsNewMerchant) {

                }
                if (IsNonKippinPhysical) {


                }
                if (IsNonKippinLoyalty) {
                    NotificationUtil.setNotification(5,gridView,true);

                }else{
                   //   NotificationUtil.setNotification(5,gridView,false);
                }
*/
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        NotificationHandler.getInstance().getNotificationForVoucher(this, new NotificationREveiver() {
//            @Override
//            public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {
//                if (IsVoucher) {
//
//                }
//                if (IsTradePoint) {
//                    NotificationUtil.setNotification(2,gridView);
//                }
//                if (IsFriendRequest) {
//                    NotificationUtil.setNotification(2,gridView);
//                }
//                if (IstransferGiftCard) {
//
//                }
//                if (IsNewMerchant) {
//
//                }
//                if (IsNonKippinPhysical) {
//
//
//                }
//                if (IsNonKippinLoyalty) {
//                    NotificationUtil.setNotification(0,gridView);
//                }
//
//            }
//        });

    }

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {
        if (IsVoucher) {

        }
        if (IsTradePoint || IsFriendRequest) {
            NotificationUtil.setNotification(2,gridView,true);
        }else{
             NotificationUtil.setNotification(2,gridView,false);
        }

        if (IstransferGiftCard) {

        }
        if (IsNewMerchant) {

        }
        if (IsNonKippinPhysical) {


        }
        if (IsNonKippinLoyalty) {
            NotificationUtil.setNotification(5,gridView,true);

        }else{
             NotificationUtil.setNotification(5,gridView,false);
        }



    }
}
