package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 2/18/2016.
 */
public class ModelCloudImage extends TemplateData {
    private String ImageName;

    private String ResponseCode;

    private String UserId;

    private String ResponseMessage;

    private String ImagePath;

    public boolean IsAssociated;
    private String ImageDisplayName;


    public String getImageDisplayName() {
        return ImageDisplayName;
    }

    public void setImageDisplayName(String ImageDisplayName) {
        this.ImageDisplayName = ImageDisplayName;
    }

    private Integer StatementId;

    public Integer getStatementId() {
        return StatementId;
    }

    public void setStatementId(Integer StatementId) {
        this.StatementId = StatementId;
    }

    public boolean isAssociated() {
        return IsAssociated;
    }

    public void setIsAssociated(boolean isAssociated) {
        IsAssociated = isAssociated;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String ImageName) {
        this.ImageName = ImageName;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String ResponseMessage) {
        this.ResponseMessage = ResponseMessage;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String ImagePath) {
        this.ImagePath = ImagePath;
    }

    @Override
    public String toString() {
        return "ClassPojo [ImageName = " + ImageName + ", ResponseCode = " + ResponseCode + ", UserId = " + UserId + ", ResponseMessage = " + ResponseMessage + ", ImagePath = " + ImagePath + "]";
    }
}
