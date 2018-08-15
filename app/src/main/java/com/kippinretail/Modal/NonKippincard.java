
package com.kippinretail.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NonKippincard {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("FolderName")
    @Expose
    private String folderName;
    @SerializedName("FrontImage")
    @Expose
    private String frontImage;
    @SerializedName("BackImage")
    @Expose
    private String backImage;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("ActualUserId")
    @Expose
    private Integer actualUserId;
    @SerializedName("MerchantId")
    @Expose
    private Integer merchantId;
    @SerializedName("Barcode")
    @Expose
    private String barcode;
    @SerializedName("LogoImage")
    @Expose
    private String logoImage;
    @SerializedName("LogoTemplate")
    @Expose
    private Object logoTemplate;
    @SerializedName("CardId")
    @Expose
    private Integer cardId;
    @SerializedName("SenderId")
    @Expose
    private Integer senderId;
    @SerializedName("IsShared")
    @Expose
    private Boolean isShared;
    @SerializedName("TotalPoints")
    @Expose
    private Integer totalPoints;
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getActualUserId() {
        return actualUserId;
    }

    public void setActualUserId(Integer actualUserId) {
        this.actualUserId = actualUserId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public Object getLogoTemplate() {
        return logoTemplate;
    }

    public void setLogoTemplate(Object logoTemplate) {
        this.logoTemplate = logoTemplate;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Boolean getIsShared() {
        return isShared;
    }

    public void setIsShared(Boolean isShared) {
        this.isShared = isShared;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
