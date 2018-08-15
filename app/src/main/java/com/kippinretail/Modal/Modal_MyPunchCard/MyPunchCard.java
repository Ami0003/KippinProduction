package com.kippinretail.Modal.Modal_MyPunchCard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kippinretail.Modal.ModalResponse;

/**
 * Created by sandeep.saini on 5/13/2016.
 */
public class MyPunchCard  extends ModalResponse {
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



    @SerializedName("UserId")
    @Expose
    private String UserId;

    @SerializedName("Country")
    @Expose
    private String Country;



    @SerializedName("TemplateId")
    @Expose
    private String TemplateId;

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



    public String getTemplateId() {
        return TemplateId;
    }

    public void setTemplateId(String templateId) {
        TemplateId = templateId;
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
