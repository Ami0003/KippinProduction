package com.kippinretail.Interface;

import com.kippinretail.Modal.donateToCharity.ModalDonateToCharity;

/**
 * Created by gaganpreet.singh on 4/1/2016.
 */
public interface OnDonateToCharity {
    public void onDonate(String userId);
    public void onDonate(ModalDonateToCharity merModalDonateToCharity);
}
