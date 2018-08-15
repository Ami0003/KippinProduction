package com.kippinretail.Modal.ModelToComparePrize;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep.singh on 8/22/2016.
 */
public class GiftCardDetailWithCountry
{

    private String GiftcardId;

    private String TemplatePath;

    private String GiftcardCount;

    private String ResponseCode;

    private String UserId;

    private String ResponseMessage;
    private String Country;
    private String TemplateId;

    public String getGiftcardId() {
        return GiftcardId;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
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

    public String getGiftcardCount() {
        return GiftcardCount;
    }

    public void setGiftcardCount(String giftcardCount) {
        GiftcardCount = giftcardCount;
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
}
