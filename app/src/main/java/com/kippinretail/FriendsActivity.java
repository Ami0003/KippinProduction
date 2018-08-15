package com.kippinretail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.RequestType;
import com.kippinretail.ApplicationuUlity.imageDownloader.DownloadImageListener;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.callbacks.NotificationREveiver;
import com.kippinretail.config.Utils;
import com.kippinretail.facebook.InviteFriends;

import java.util.ArrayList;

import notification.NotificationHandler;
import notification.NotificationUtil;

public class FriendsActivity extends MyAbstractGridActivity {

    private String cc = "dummysmartbuzz@gmail.com";
    private String email_Subject = "Retail App Invitation";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();

    }

    @Override
   public void updateToolbar() {
        generateActionBar(R.string.title_user_friends_activity, true, true, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
   public void updateUI() {

        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        //modalGridElements.add(getElement(R.drawable.invite_facebook_friends , "Invite Facebook Friends to KIPPIN")) ;
        modalGridElements.add(getElement(R.drawable.kippin_white_background_icon, "Invite Facebook Friends to KIPPIN"));
        modalGridElements.add(getElement(R.drawable.invite_friends_via_email_orange , "Invite Friends To KIPPIN (via email address)")) ;
        modalGridElements.add(getElement(R.drawable.invite_kippin_friends_red , "Invite KIPPIN Friends")) ;
        modalGridElements.add(getElement(R.drawable.incoming_friend_request_green , "Incoming Friend Request")) ;

try {
    setData(modalGridElements, new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i = new Intent();
            switch (position) {
                case 0:
                   // FacebookSdk.sdkInitialize(getApplicationContext());
                    //inviteFacebookFriend();
                    break;
                case 1:

                    try {
                        if (false) {
                            email();
                        } else
                            Utility.downloadReferImage(FriendsActivity.this, new DownloadImageListener() {
                                @Override
                                public void onImageLoaded(Bitmap bitmap) {
                                    Utility.writeImage(bitmap, CommonData.referPath);
                                    email();
                                }

                                @Override
                                public void onError() {
                                }
                            });
                    }catch(Exception ex){
                        Log.e("",ex.getMessage());
                    }
                    break;
                case 2:
                    i.setClass(FriendsActivity.this, Activity_MyPoint_InviteKippinFriends_FriendList.class);
                    i.putExtra(getString(R.string.class_), getClass().getCanonicalName());
                    startActivity(i);
                    overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                    break;
                case 3:
                    //TransferGiftCards_FriendListActivity.class
                    i.putExtra("RequestType", RequestType.FRIENDREQUEST);
                    i.setClass(FriendsActivity.this, IncomingFriendRequest.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                    break;

            }

        }
    });

}catch (Exception ex){
    Log.e("102 "+ex.getMessage() ,"Friends Activity ....");
}
    }

    private void inviteFacebookFriend() {
        InviteFriends friends = new InviteFriends(this);
        friends.sendInvitation();

    }

    private void email() {
        final ArrayList arrayList = new ArrayList();
     //   CommonData.referPath = CommonData.getUserData(this).getRefferCodepath();
        arrayList.add(CommonData.referPath);
        Utils.hideKeyboard(this);
        new CountDownTimer(500,300){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Utility.email(FriendsActivity.this, "", "", email_Subject, Utility.getBody(), arrayList);
            }
        }.start();

    }

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {



        if (IsFriendRequest) {
            NotificationUtil.setNotification(3, gridView,true);
        }else{
          //  NotificationUtil.setNotification(3, gridView,false);
        }

        if (IsNonKippinLoyalty) {
            NotificationUtil.setNotification(0,gridView,true);
        }else{
          //  NotificationUtil.setNotification(0,gridView,false);
        }
    }

    @Override
    protected void onRestart() {



        super.onRestart();
        NotificationHandler.getInstance().getNotificationForCards(this, new NotificationREveiver() {
            @Override
            public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {
                // COMING FROM PARENT IF NOTIFIVATION HANDLE THEN REMOCE IN ELSE PART


                if (IsFriendRequest) {
                    NotificationUtil.setNotification(3, gridView,true);
                }else{
                    NotificationUtil.setNotification(3, gridView,false);
                }



                /*if (IsNonKippinLoyalty) {
                    NotificationUtil.setNotification(0,gridView,true);
                }else{
                    NotificationUtil.setNotification(0,gridView,false);
                }*/
            }
        });
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult","onActivityResult");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                CommonUtility.hideKeyboard(FriendsActivity.this);
            }
        }, 300);
    }*/
}
