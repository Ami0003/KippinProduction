package com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kippinretail.Modal.ModalResponse;

/**
 * Created by sandeep.singh on 7/@SerializedName("EndDate")     @Expose6/20@SerializedName("EndDate")     @Expose6.
 */
public class ResponseForGiftAnalytics extends ModalResponse {
    @SerializedName("GiftcardPrice")
    @Expose
    private String GiftcardPrice;


    public String getGiftcardPrice() {
        return GiftcardPrice;
    }

    public void setGiftcardPrice(String giftcardPrice) {
        GiftcardPrice = giftcardPrice;
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
}
