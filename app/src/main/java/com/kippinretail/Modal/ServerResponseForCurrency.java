package com.kippinretail.Modal;

/**
 * Created by sandeep.singh on 8/2/2016.
 */
public class ServerResponseForCurrency {

    private String KippinCharge;

    private String Symbol;

    private String StripeCharge;

    private String ResponseCode;

    private String UserId;

    private String Country;

    private String Currency;

    private String ResponseMessage;

    public String getKippinCharge() {
        return KippinCharge;
    }

    public void setKippinCharge(String kippinCharge) {
        KippinCharge = kippinCharge;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getStripeCharge() {
        return StripeCharge;
    }

    public void setStripeCharge(String stripeCharge) {
        StripeCharge = stripeCharge;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }
}
