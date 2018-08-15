package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 2/5/2016.
 */
public class ModelClassification extends TemplateData {
    private String CategoryId;

    private String ClassificationType;

    private String ResponseCode;

    private String Id;

    private String UserId;

    private String ResponseMessage;

    public String Desc;


    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getCategoryId() {
        return  (CategoryId);
    }

    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getClassificationType() {
        return handleNull(ClassificationType);
    }

    public void setClassificationType(String ClassificationType) {
        this.ClassificationType = ClassificationType;
    }

    public String getResponseCode() {
        return handleNull(ResponseCode);
    }

    public void setResponseCode(String ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public String getId() {
        return handleNull(Id);
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getUserId() {
        return handleNull(UserId);
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getResponseMessage() {
        return handleNull(ResponseMessage);
    }

    public void setResponseMessage(String ResponseMessage) {
        this.ResponseMessage = ResponseMessage;
    }

    @Override
    public String toString() {
        return "ClassPojo [CategoryId = " + CategoryId + ", ClassificationType = " + ClassificationType + ", ResponseCode = " + ResponseCode + ", Id = " + Id + ", UserId = " + UserId + ", ResponseMessage = " + ResponseMessage + "]";
    }
}
