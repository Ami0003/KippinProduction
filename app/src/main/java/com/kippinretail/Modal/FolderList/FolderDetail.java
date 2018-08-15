package com.kippinretail.Modal.FolderList;

/**
 * Created by sandeep.saini on 3/23/2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class FolderDetail {

    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("FolderName")
    @Expose
    private String FolderName;

    @SerializedName("UserId")
    @Expose
    private String UserId;

    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
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
}
