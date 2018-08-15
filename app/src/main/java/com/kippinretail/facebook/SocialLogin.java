package com.kippinretail.facebook;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import org.json.JSONException;
import org.json.JSONObject;


public class SocialLogin {

    public static final int GOOGLE_LOGIN = 7;
    public static final int FACEBOOK_LOGIN = 6;

    private Activity activity;
    private SocialLoginData loginData;
    private CallbackManager callbackManager;
    private RelativeLayout btnLoginViaFacebook;

    public SocialLogin(Activity activity) {
        this.activity = activity;
    }

    public void loginViaFacebook(int viewId) {
        String appLinkUrl, previewImageUrl;

        appLinkUrl = "";
        previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(activity, content);
        }
//        Log.e("loginViaFacebook", "loginViaFacebook");
//        callbackManager = CallbackManager.Factory.create();
//
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//
//
//                        Log.e("ACCESS TOKEN", "" + loginResult.getAccessToken().getToken());
//                        Profile profile = Profile.getCurrentProfile();
//                        Log.e("First Name",profile.getFirstName());
//                        Log.e("First Name",profile.getLastName());
//                        Log.e("First Name",profile.getProfilePictureUri(100,100).toString());
//                        Log.e("First Name",profile.getId());
//                        Log.e("First Name",profile.getMiddleName());
//
//                        loginData = new SocialLoginData();
//                        loginData.setAccess_token(loginResult.getAccessToken().getToken());
//
//                        requestFacebookUserData(loginResult.getAccessToken());
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                        Log.e("CANCEL", "CANCEL");
//
//                        loginData = new SocialLoginData();
//                        // requestFacebookUserData(AccessToken.getCurrentAccessToken());
//                        CommonDialog.With(activity).Show("No internet connection. Please try again later.");
//
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                        Log.v("ERROR", "ERROR");
//
//                        CommonDialog.With(activity).Show("No internet connection. Please try again later.");
//                    }
//                });
//
//        btnLoginViaFacebook = (RelativeLayout) activity.findViewById(viewId);
//        btnLoginViaFacebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                LoginManager.getInstance().logInWithReadPermissions(activity,
//                        Arrays.asList("email"));
//            }
//        });

      //  return callbackManager;
    }


    public void Logout() {
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            Log.v("error in logout fb", "The SDK has not been initialized, make sure to call FacebookSdk.sdkInitialize() first.");
        }
    }


    public void requestFacebookUserData(AccessToken token) {

        GraphRequest request = GraphRequest.newMeRequest(
                token, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.v("GRAPH RESPONSE", response.toString());
                        Log.v("JSON OBJECT", object.toString());

                        try {

                            String key;

                            key = "id";
                            loginData.setSocialUserID(getValue(key, object));

                            key = "birthday";
                            loginData.setBirthday_date(getValue(key, object));

                            key = "gender";
                            loginData.setSex(getValue(key, object));

                            key = "name";
                            String name = getValue(key, object);

                            key = "email";
                            String em = getValue(key, object);
                            if (em.isEmpty()) {
                                em = loginData.getSocialUserID() + "@facebook.com";
                            }
                            loginData.setEmail(em);

                            key = "first_name";
                            loginData.setFirst_name(getValue(key, object));

                            key = "last_name";
                            loginData.setLast_name(getValue(key, object));

                            key = "about";
                            loginData.setAbout_me(getValue(key, object));

                            returnFacebookUserData();
                        } catch (JSONException jEx) {

                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender, birthday, about, first_name, last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private String getValue(String key, JSONObject jObj) throws JSONException {
        if (jObj.has(key))
            return jObj.getString(key);
        return "";
    }

    private void returnFacebookUserData() {
        loginData.setPic_big("https://graph.facebook.com/" + loginData.getSocialUserID() + "/picture?width=640&height=640");
        loginData.setUserType("f");
        if (activity instanceof OnSocialLoginListener)
            ((OnSocialLoginListener) activity).onSocialLogin(FACEBOOK_LOGIN, loginData);
    }
}
