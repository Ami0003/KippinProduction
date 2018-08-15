package com.kippinretail.Modal;

import com.kippinretail.Interface.SuperModal;

/**
 * Created by sandeep.saini on 3/14/2016.
 */
public class MerchantModal implements SuperModal {
    private String bussinessName="";
    private String name="";
    private String merchantid="";
    private boolean checked = false;
    private String message="";
    private String parent="";
    private String friendId="";
    private String parentButton=""; // Reprenst the Button After that sub Activity Opens
    private String childActivity=""; // which Activity to open
    private String points;
    private String barcode;
    private boolean IsSubscribedMerchant;
    private String loyalityCardName;
    private String loyalityBarcode;
    private boolean isRead;
    public  boolean isAuthenticated ;
    private String ProfileImage;
    private String TermsConditions;
    private Boolean IsRead;

    public Boolean getIsRead() {
        return IsRead;
    }

    public void setIsRead(Boolean isRead) {
        IsRead = isRead;
    }

    public String getTermsConditions() {
        return TermsConditions;
    }

    public void setTermsConditions(String termsConditions) {
        TermsConditions = termsConditions;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getChildActivity() {
        return childActivity;
    }

    public void setChildActivity(String childActivity) {
        this.childActivity = childActivity;
    }

    public String getLoyalityBarcode() {
        return loyalityBarcode;
    }

    public void setLoyalityBarcode(String loyalityBarcode) {
        this.loyalityBarcode = loyalityBarcode;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public boolean isSubscribedMerchant() {
        return IsSubscribedMerchant;
    }

    public void setIsSubscribedMerchant(boolean isSubscribedMerchant) {
        IsSubscribedMerchant = isSubscribedMerchant;
    }

    public MerchantModal(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }
    public MerchantModal() {
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getParentButton() {
        return parentButton;
    }

    public void setParentButton(String parentButton) {
        this.parentButton = parentButton;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getBussinessName() {
        return bussinessName;
    }

    public void setBussinessName(String bussinessName) {
        this.bussinessName = bussinessName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMerchantid() {
        return merchantid;
    }

    public void setMerchantid(String merchantid) {
        this.merchantid = merchantid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public String toString() {
        return name ;
    }
    public void toggleChecked() {
        checked = !checked ;
    }

    public void setLoyalityCardName(String loyalityCardName) {
        this.loyalityCardName = loyalityCardName;
    }

    public String getLoyalityCardName() {
        return loyalityCardName;
    }
}

