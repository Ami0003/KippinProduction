
package com.kippinretail.Modal.kippinggallery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KippinCloudgallery {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("TemplateName")
    @Expose
    private String templateName;
    @SerializedName("TemplatePath")
    @Expose
    private String templatePath;
    @SerializedName("DateCreated")
    @Expose
    private Object dateCreated;
    @SerializedName("IsDeleted")
    @Expose
    private Object isDeleted;
    @SerializedName("UserId")
    @Expose
    private Object userId;
    @SerializedName("IsPunchCard")
    @Expose
    private Object isPunchCard;
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public Object getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Object dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Object getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Object isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getIsPunchCard() {
        return isPunchCard;
    }

    public void setIsPunchCard(Object isPunchCard) {
        this.isPunchCard = isPunchCard;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
