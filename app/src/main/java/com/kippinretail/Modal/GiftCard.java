
package com.kippinretail.Modal;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiftCard implements Serializable
{

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("FrontImage")
    @Expose
    private String frontImage;
    @SerializedName("BackImage")
    @Expose
    private String backImage;
    @SerializedName("FolderName")
    @Expose
    private String folderName;
    @SerializedName("ActualUserId")
    @Expose
    private int actualUserId;
    @SerializedName("MerchantId")
    @Expose
    private Object merchantId;
    @SerializedName("Barcode")
    @Expose
    private String barcode;
    @SerializedName("LogoImage")
    @Expose
    private String logoImage;
    @SerializedName("LogoTemplate")
    @Expose
    private Object logoTemplate;
    @SerializedName("IsRead")
    @Expose
    private boolean isRead;
    @SerializedName("SenderId")
    @Expose
    private int senderId;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("ResponseCode")
    @Expose
    private int responseCode;
    @SerializedName("UserId")
    @Expose
    private int userId;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    private final static long serialVersionUID = -3296568391937580468L;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GiftCard withId(int id) {
        this.id = id;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public GiftCard withCountry(String country) {
        this.country = country;
        return this;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public GiftCard withFrontImage(String frontImage) {
        this.frontImage = frontImage;
        return this;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public GiftCard withBackImage(String backImage) {
        this.backImage = backImage;
        return this;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public GiftCard withFolderName(String folderName) {
        this.folderName = folderName;
        return this;
    }

    public int getActualUserId() {
        return actualUserId;
    }

    public void setActualUserId(int actualUserId) {
        this.actualUserId = actualUserId;
    }

    public GiftCard withActualUserId(int actualUserId) {
        this.actualUserId = actualUserId;
        return this;
    }

    public Object getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Object merchantId) {
        this.merchantId = merchantId;
    }

    public GiftCard withMerchantId(Object merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public GiftCard withBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public GiftCard withLogoImage(String logoImage) {
        this.logoImage = logoImage;
        return this;
    }

    public Object getLogoTemplate() {
        return logoTemplate;
    }

    public void setLogoTemplate(Object logoTemplate) {
        this.logoTemplate = logoTemplate;
    }

    public GiftCard withLogoTemplate(Object logoTemplate) {
        this.logoTemplate = logoTemplate;
        return this;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public GiftCard withIsRead(boolean isRead) {
        this.isRead = isRead;
        return this;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public GiftCard withSenderId(int senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public GiftCard withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public GiftCard withResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public GiftCard withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public GiftCard withResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
        return this;
    }

}
