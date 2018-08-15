package com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kippinretail.Modal.ModalResponse;

/**
 * Created by sandeep.singh on 7/16/2016.
 */
public class ResponseForGiftCardUsageAnalytics extends ModalResponse {


    @SerializedName("Symbol")
    @Expose
    private String Symbol;

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String Symbol) {
        Symbol = Symbol;
    }

    @SerializedName("Amount")
    @Expose
    private String Amount;

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    @SerializedName("GiftcardId")
    @Expose
    private String GiftcardId;
    @SerializedName("TemplatePath")
    @Expose
    private String TemplatePath;

    @SerializedName("Location")
    @Expose
    private String Location;

    @SerializedName("GiftcardCount")
    @Expose
    private String GiftcardCount;


    @SerializedName("UserId")
    @Expose
    private String UserId;


    @SerializedName("TemplateId")
    @Expose
    private String TemplateId;
    @SerializedName("Country")
    @Expose
    private String Country;
    @SerializedName("GiftcardPrice")
    @Expose
    private String GiftcardPrice;


    public String getGiftcardId() {
        return GiftcardId;
    }

    public void setGiftcardId(String giftcardId) {
        GiftcardId = giftcardId;
    }

    public String getTemplatePath() {
        return TemplatePath;
    }

    public void setTemplatePath(String templatePath) {
        TemplatePath = templatePath;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getGiftcardCount() {
        return GiftcardCount;
    }

    public void setGiftcardCount(String giftcardCount) {
        GiftcardCount = giftcardCount;
    }


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }


    public String getTemplateId() {
        return TemplateId;
    }

    public void setTemplateId(String templateId) {
        TemplateId = templateId;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getGiftcardPrice() {
        return GiftcardPrice;
    }

    public void setGiftcardPrice(String giftcardPrice) {
        GiftcardPrice = giftcardPrice;
    }
}
