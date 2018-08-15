package com.kippinretail.Modal.ModelToComparePrize;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep.singh on 8/22/20    @SerializedName("")     @Expose6.
 */
public class ObjGiftcardDetails {
    @SerializedName("GiftcardId")     @Expose
    private String GiftcardId;
    @SerializedName("TemplatePath")     @Expose
    private String TemplatePath;
    @SerializedName("GiftcardCount")     @Expose
    private String GiftcardCount;
    @SerializedName("ResponseCode")     @Expose
    private String ResponseCode;
    @SerializedName("UserId")     @Expose
    private String UserId;
    @SerializedName("ResponseMessage")     @Expose
    private String ResponseMessage;
    @SerializedName("TemplateId")     @Expose
    private String TemplateId;

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
