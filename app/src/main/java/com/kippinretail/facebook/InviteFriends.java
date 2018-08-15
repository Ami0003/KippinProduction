package com.kippinretail.facebook;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.facebook.applinks.AppLinkData;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.kippinretail.ApplicationuUlity.CommonData;

import bolts.AppLinks;

/**
 * Created by sandeep.saini on 3/11/2016.
 */
public class InviteFriends
{
    //private String appLinkUrl="https://fb.me/599291310218951";//https://fb.me/958257997630358
    //private String appLinkUrl="https://fb.me/1791946294382499";
       private String appLinkUrl="https://fb.me/816815501774609";
    Activity activity;



    public InviteFriends(Activity activity) {
        this.activity = activity;
    }

    public void sendInvitation()
    {
            try {
                if (AppInviteDialog.canShow()) {
                    System.out.println("Show check is Pass");
                    AppInviteContent content = new AppInviteContent.Builder()
                            .setApplinkUrl(appLinkUrl)
                            .setPreviewImageUrl(CommonData.getUserData(activity).getRefferCodepath())
                            .build();

                    AppInviteDialog.show(activity, content);
                    System.out.println("Dialog Visible");
                } else {
                    System.out.println("Check is Fail");
                }
            }catch(Exception ex){

            }

    }
    public void createAppLink()
    {
        System.out.println("Inside Create App link");
        Uri targetUrl =
                AppLinks.getTargetUrlFromInboundIntent(activity, activity.getIntent());
        if (targetUrl != null) {
            System.out.println( "App Link Target URL: " + targetUrl.toString());
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
            Toast.makeText(activity, targetUrl.toString(), Toast.LENGTH_LONG).show();
        }else {
            System.out.println("Inside Create App link else");
            AppLinkData.fetchDeferredAppLinkData(
                    activity,
                    new AppLinkData.CompletionHandler() {
                        @Override
                        public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {

                        }
                    });
        }
    }
}
