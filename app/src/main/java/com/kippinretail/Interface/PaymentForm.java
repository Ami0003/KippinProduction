package com.kippinretail.Interface;

/**
 * Created by sandeep.saini on 3/17/2016.
 */
public interface PaymentForm {
    public String getCardNumber();
    public String getCvv();
    public Integer getExpMonth();
    public Integer getExpYear();
    public String getNameOnCard();
    public String getCurrency();
}
