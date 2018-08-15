package com.kippinretail.Modal.Invoice;

/**
 * Created by kamaljeet.singh on 11/22/2016.
 */

public class ModalDataForAddress {
    private String CorporateAptNo;
    private String CorporateHouseNo;
    private String CorporateStreet;
    private String CorporateCity;
    private String CorporateState;
    private String CorporatePostalCode;
    private String ShippingAptNo;
    private String ShippingHouseNo;
    private String ShippingStreet;
    private String ShippingCity;
    private String ShippingState;
    private String ShippingPostalCode;
    private int firstTime = 0;
    private boolean dialogType;

    public String getCorporateAptNo() {
        if(CorporateAptNo == null)
         return "";
        return CorporateAptNo;
    }

    public void setCorporateAptNo(String corporateAptNo) {
        CorporateAptNo = corporateAptNo;
    }

    public String getCorporateHouseNo() {
        if(CorporateHouseNo == null)
            return "";
        return CorporateHouseNo;
    }

    public void setCorporateHouseNo(String corporateHouseNo) {
        CorporateHouseNo = corporateHouseNo;
    }

    public String getCorporateStreet() {
        if(CorporateStreet == null)
            return "";
        return CorporateStreet;
    }

    public void setCorporateStreet(String corporateStreet) {
        CorporateStreet = corporateStreet;
    }

    public String getCorporateCity() {
        if(CorporateCity == null)
            return "";
        return CorporateCity;
    }

    public void setCorporateCity(String corporateCity) {
        CorporateCity = corporateCity;
    }

    public String getCorporateState() {
        if(CorporateState == null)
            return "";
        return CorporateState;
    }

    public void setCorporateState(String corporateState) {
        CorporateState = corporateState;
    }

    public String getCorporatePostalCode() {
        if(CorporatePostalCode == null)
            return "";
        return CorporatePostalCode;
    }

    public void setCorporatePostalCode(String corporatePostalCode) {
        CorporatePostalCode = corporatePostalCode;
    }

    public String getShippingAptNo() {
        if(ShippingAptNo == null)
            return "";
        return ShippingAptNo;
    }

    public void setShippingAptNo(String shippingAptNo) {
        ShippingAptNo = shippingAptNo;
    }

    public String getShippingHouseNo() {
        if(ShippingHouseNo == null)
            return "";
        return ShippingHouseNo;
    }

    public void setShippingHouseNo(String shippingHouseNo) {
        ShippingHouseNo = shippingHouseNo;
    }

    public String getShippingStreet() {
        if(ShippingStreet == null)
            return "";
        return ShippingStreet;
    }

    public void setShippingStreet(String shippingStreet) {
        ShippingStreet = shippingStreet;
    }

    public String getShippingCity() {
        if(ShippingCity == null)
        return "";
        return ShippingCity;
    }

    public void setShippingCity(String shippingCity) {
        ShippingCity = shippingCity;
    }

    public String getShippingState() {
        if(ShippingState == null)
            return "";
        return ShippingState;
    }

    public void setShippingState(String shippingState) {
        ShippingState = shippingState;
    }

    public String getShippingPostalCode() {
        if(ShippingPostalCode == null)
            return "";
        return ShippingPostalCode;
    }

    public void setShippingPostalCode(String shippingPostalCode) {
        ShippingPostalCode = shippingPostalCode;
    }

    public int getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(int firstTime) {
        this.firstTime = firstTime;
    }

    public boolean isDialogType() {
        return dialogType;
    }

    public void setDialogType(boolean dialogType) {
        this.dialogType = dialogType;
    }
}
