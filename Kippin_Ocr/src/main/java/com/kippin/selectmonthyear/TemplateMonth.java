package com.kippin.selectmonthyear;

import com.kippin.webclient.model.TemplateData;

/**
 * Created by gaganpreet.singh on 2/16/2016.
 */
public class TemplateMonth extends TemplateData {
    private String ResponseCode;

    private String FolderName;

    private String UserId;

    private boolean IsAssociated = false;

    private String ResponseMessage;

    public boolean isAssociated() {
        return IsAssociated;
    }

    public void setIsAssociated(boolean isAssociated) {
        IsAssociated = isAssociated;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String FolderName) {
        this.FolderName = FolderName;
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

    @Override
    public String toString() {
        return "ClassPojo [ResponseCode = " + ResponseCode + ", FolderName = " + FolderName + ", UserId = " + UserId + ", ResponseMessage = " + ResponseMessage + "]";
    }
}
