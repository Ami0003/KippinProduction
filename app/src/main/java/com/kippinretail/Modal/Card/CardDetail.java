package com.kippinretail.Modal.Card;

/**
 * Created by sandeep.saini on 3/24/2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class CardDetail {
    @SerializedName("ImageName")
    @Expose
    private String ImageName;

    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("UserId")
    @Expose
    private String UserId;

    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;

    @SerializedName("FrontImagePath")
    @Expose
    private String FrontImagePath;

    @SerializedName("BackImagePath")
    @Expose
    private String BackImagePath;

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
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

    public String getFrontImagePath() {
        return FrontImagePath;
    }

    public void setFrontImagePath(String frontImagePath) {
        FrontImagePath = frontImagePath;
    }

    public String getBackImagePath() {
        return BackImagePath;
    }

    public void setBackImagePath(String backImagePath) {
        BackImagePath = backImagePath;
    }
}
