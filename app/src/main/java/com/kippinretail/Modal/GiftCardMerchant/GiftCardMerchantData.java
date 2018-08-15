package com.kippinretail.Modal.GiftCardMerchant;

/**
 * Created by kamaljeet.singh on 3/26/2016.
 */
 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class GiftCardMerchantData {

    @SerializedName("Id")
    @Expose
    private Integer Id;
    @SerializedName("TemplateName")
    @Expose
    private String TemplateName;
    @SerializedName("TemplatePath")
    @Expose
    private String TemplatePath;
    @SerializedName("DateCreated")
    @Expose
    private Object DateCreated;
    @SerializedName("IsDeleted")
    @Expose
    private Object IsDeleted;
    @SerializedName("UserId")
    @Expose
    private Object UserId;
    @SerializedName("ResponseCode")
    @Expose
    private Integer ResponseCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;

    /**
     *
     * @return
     * The Id
     */
    public Integer getId() {
        return Id;
    }

    /**
     *
     * @param Id
     * The Id
     */
    public void setId(Integer Id) {
        this.Id = Id;
    }

    /**
     *
     * @return
     * The TemplateName
     */
    public String getTemplateName() {
        return TemplateName;
    }

    /**
     *
     * @param TemplateName
     * The TemplateName
     */
    public void setTemplateName(String TemplateName) {
        this.TemplateName = TemplateName;
    }

    /**
     *
     * @return
     * The TemplatePath
     */
    public String getTemplatePath() {
        return TemplatePath;
    }

    /**
     *
     * @param TemplatePath
     * The TemplatePath
     */
    public void setTemplatePath(String TemplatePath) {
        this.TemplatePath = TemplatePath;
    }

    /**
     *
     * @return
     * The DateCreated
     */
    public Object getDateCreated() {
        return DateCreated;
    }

    /**
     *
     * @param DateCreated
     * The DateCreated
     */
    public void setDateCreated(Object DateCreated) {
        this.DateCreated = DateCreated;
    }

    /**
     *
     * @return
     * The IsDeleted
     */
    public Object getIsDeleted() {
        return IsDeleted;
    }

    /**
     *
     * @param IsDeleted
     * The IsDeleted
     */
    public void setIsDeleted(Object IsDeleted) {
        this.IsDeleted = IsDeleted;
    }

    /**
     *
     * @return
     * The UserId
     */
    public Object getUserId() {
        return UserId;
    }

    /**
     *
     * @param UserId
     * The UserId
     */
    public void setUserId(Object UserId) {
        this.UserId = UserId;
    }

    /**
     *
     * @return
     * The ResponseCode
     */
    public Integer getResponseCode() {
        return ResponseCode;
    }

    /**
     *
     * @param ResponseCode
     * The ResponseCode
     */
    public void setResponseCode(Integer ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    /**
     *
     * @return
     * The ResponseMessage
     */
    public String getResponseMessage() {
        return ResponseMessage;
    }

    /**
     *
     * @param ResponseMessage
     * The ResponseMessage
     */
    public void setResponseMessage(String ResponseMessage) {
        this.ResponseMessage = ResponseMessage;
    }

}
