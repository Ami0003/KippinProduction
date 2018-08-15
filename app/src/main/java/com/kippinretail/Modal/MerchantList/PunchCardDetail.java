package com.kippinretail.Modal.MerchantList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sandeep.singh on 7/27/20 @SerializedName("")     @Expose6.
 */
public class PunchCardDetail implements Serializable {
    @SerializedName("Description")
    @Expose
    private String Description;

    @SerializedName("FreePunches")
    @Expose
    private String FreePunches;
    @SerializedName("TotalPunches")
    @Expose
    private String TotalPunches;
    @SerializedName("DateCreated")
    @Expose
    private String DateCreated;
    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;
    @SerializedName("UserId")
    @Expose
    private String UserId;
    @SerializedName("PunchCardName")
    @Expose
    private String PunchCardName;
    @SerializedName("Country")
    @Expose
    private String Country;
    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;
    @SerializedName("TermsAndCondtions")
    @Expose
    private String TermsAndCondtions;
    @SerializedName("TemplateId")
    @Expose
    private String TemplateId;
    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("PunchBarcode")
    @Expose
    private String PunchBarcode;
    @SerializedName("DateModified")
    @Expose
    private String DateModified;
    @SerializedName("MerchantId")
    @Expose
    private String MerchantId;
    @SerializedName("Punches")
    @Expose
    private String Punches;
    @SerializedName("IsDeleted")
    @Expose
    private String IsDeleted;
    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("PunchcardImage")
    @Expose
    private String PunchcardImage;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getFreePunches() {
        return FreePunches;
    }

    public void setFreePunches(String freePunches) {
        FreePunches = freePunches;
    }

    public String getTotalPunches() {
        return TotalPunches;
    }

    public void setTotalPunches(String totalPunches) {
        TotalPunches = totalPunches;
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

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPunchCardName() {
        return PunchCardName;
    }

    public void setPunchCardName(String punchCardName) {
        PunchCardName = punchCardName;
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

    public String getTermsAndCondtions() {
        return TermsAndCondtions;
    }

    public void setTermsAndCondtions(String termsAndCondtions) {
        TermsAndCondtions = termsAndCondtions;
    }

    public String getTemplateId() {
        return TemplateId;
    }

    public void setTemplateId(String templateId) {
        TemplateId = templateId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPunchBarcode() {
        return PunchBarcode;
    }

    public void setPunchBarcode(String punchBarcode) {
        PunchBarcode = punchBarcode;
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

    public String getPunches() {
        return Punches;
    }

    public void setPunches(String punches) {
        Punches = punches;
    }

    public String getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        IsDeleted = isDeleted;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPunchcardImage() {
        return PunchcardImage;
    }

    public void setPunchcardImage(String punchcardImage) {
        PunchcardImage = punchcardImage;
    }
}
