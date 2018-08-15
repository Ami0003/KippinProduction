package com.kippinretail.Modal;

/**
 * Created by sandeep.singh on 8/2/2016.
 */
public class ResponseToGetLoyaltyCardLocalModel extends ModalResponse {
    private String ActualUserId;

    private String Barcode;

    private String MerchantId;

    private String FrontImage;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    private String FolderName;

    private String Id;

    private String UserId;

    private String Country;
    private Boolean IsRead;

    public Boolean getIsRead() {
        return IsRead;
    }

    public void setIsRead(Boolean isRead) {
        IsRead = isRead;
    }

    private String BackImage;
    private String Orignal_BackImage;
    private String Orignal_FrontImage;
    private String Orignal_LogoImage;

    public String getOrignalFrontImage() {
        return Orignal_LogoImage;
    }

    public void setOrignalFrontImage(String OrignalFrontImage) {
        Orignal_LogoImage = OrignalFrontImage;
    }

    public String getOrignalLogoImage() {
        return Orignal_FrontImage;
    }

    public void setOrignalLogoImage(String OrignalLogoImage) {
        Orignal_FrontImage = OrignalLogoImage;
    }


    public String getOrignalBackImage() {
        return Orignal_BackImage;
    }

    public void setOrignalBackImage(String OrignalBackImage) {
        Orignal_BackImage = OrignalBackImage;
    }

    private String SenderId;
    private Boolean IsShared;
    private String CardId;

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public Boolean getIsShared() {
        return IsShared;
    }

    public void setIsShared(Boolean isShared) {
        IsShared = isShared;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String cardId) {
        CardId = cardId;
    }

    private String LogoImage;

    public String getActualUserId() {
        return ActualUserId;
    }

    public void setActualUserId(String actualUserId) {
        ActualUserId = actualUserId;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getFrontImage() {
        return FrontImage;
    }

    public void setFrontImage(String frontImage) {
        FrontImage = frontImage;
    }


    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getBackImage() {
        return BackImage;
    }

    public void setBackImage(String backImage) {
        BackImage = backImage;
    }


    public String getLogoImage() {
        return LogoImage;
    }

    public void setLogoImage(String logoImage) {
        LogoImage = logoImage;
    }
}

