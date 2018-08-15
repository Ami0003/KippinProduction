package com.kippinretail.Modal.GiftCardList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kippinretail.Modal.ModalResponse;

/**
 * Created by sandeep.saini on 3/16/2016.
 */
public class GiftCard extends ModalResponse
{
    @SerializedName("Barcode")
    @Expose
    private String Barcode;

    @SerializedName("ExpiredDate")
    @Expose
    private String ExpiredDate;

    @SerializedName("GiftCardPoints")
    @Expose
    private String GiftCardPoints;

    @SerializedName("DateCreated")
    @Expose
    private String DateCreated;

//    @SerializedName("ResponseCode")
//    @Expose
//    private String ResponseCode;

    @SerializedName("IsExpired")
    @Expose
    private String IsExpired;

    @SerializedName("UserId")
    @Expose
    private String UserId;

    @SerializedName("Country")
    @Expose
    private String Country;

//    @SerializedName("ResponseMessage")
//    @Expose
//    private String ResponseMessage;

    @SerializedName("TemplateId")
    @Expose
    private String TemplateId;

    @SerializedName("DateModified")
    @Expose
    private String DateModified;

    @SerializedName("MerchantId")
    @Expose
    private String MerchantId;

    @SerializedName("Price")
    @Expose
    private String Price;

    @SerializedName("GiftcardImage")
    @Expose
    private String GiftcardImage;

    @SerializedName("Id")
    @Expose
    private String Id;

    @SerializedName("IsPaid")
    @Expose
    private String IsPaid;

    @SerializedName("FriendName")
    @Expose
    private String FriendName;

    @SerializedName("IsRead")
    @Expose
    private Boolean IsRead;


    public Boolean getIsRead() {
        return IsRead;
    }

    public void setIsRead(Boolean isRead) {
        IsRead = isRead;
    }

    public String getFriendName() {
        return FriendName;
    }

    public void setFriendName(String friendName) {
        FriendName = friendName;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getExpiredDate() {
        return ExpiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        ExpiredDate = expiredDate;
    }

    public String getGiftCardPoints() {
        return GiftCardPoints;
    }

    public void setGiftCardPoints(String giftCardPoints) {
        GiftCardPoints = giftCardPoints;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getIsExpired() {
        return IsExpired;
    }

    public void setIsExpired(String isExpired) {
        IsExpired = isExpired;
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

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public String getTemplateId() {
        return TemplateId;
    }

    public void setTemplateId(String templateId) {
        TemplateId = templateId;
    }

    public String getDateModified() {
        return DateModified;
    }

    public void setDateModified(String dateModified) {
        DateModified = dateModified;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getGiftcardImage() {
        return GiftcardImage;
    }

    public void setGiftcardImage(String giftcardImage) {
        GiftcardImage = giftcardImage;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getIsPaid() {
        return IsPaid;
    }

    public void setIsPaid(String isPaid) {
        IsPaid = isPaid;
    }
}
