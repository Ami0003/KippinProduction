package com.kippinretail.facebook;

/**
 * Created by Rishabh on 26/04/15.
 */
public interface OnSocialLoginListener {

    /**
     * Override this method to get Login Data
     *
     * @param type
     * @param loginData
     */
    void onSocialLogin(int type, SocialLoginData loginData);
}
