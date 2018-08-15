package com.kippinretail.Modal.VoucherMerchant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamaljeet.singh on 3/29/2016.
 */
public class VoucherData {

    @SerializedName("Id")
    @Expose
    private Integer Id;
    @SerializedName("MerchantId")
    @Expose
    private String MerchantId;
    @SerializedName("VoucherImage")
    @Expose
    private String VoucherImage;
    @SerializedName("DateCreated")
    @Expose
    private String DateCreated;
    @SerializedName("DateModified")
    @Expose
    private String DateModified;
    @SerializedName("StartDate")
    @Expose
    private String StartDate;
    @SerializedName("EndDate")
    @Expose
    private String EndDate;
    @SerializedName("IsDeleted")
    @Expose
    private String IsDeleted;
    @SerializedName("VoucherName")
    @Expose
    private String VoucherName;
    @SerializedName("ResponseCode")
    @Expose
    private Integer ResponseCode;
    @SerializedName("UserId")
    @Expose
    private Integer UserId;
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
     * The MerchantId
     */
    public String getMerchantId() {
        return MerchantId;
    }

    /**
     *
     * @param MerchantId
     * The MerchantId
     */
    public void setMerchantId(String MerchantId) {
        this.MerchantId = MerchantId;
    }

    /**
     *
     * @return
     * The VoucherImage
     */
    public String getVoucherImage() {
        return VoucherImage;
    }

    /**
     *
     * @param VoucherImage
     * The VoucherImage
     */
    public void setVoucherImage(String VoucherImage) {
        this.VoucherImage = VoucherImage;
    }

    /**
     *
     * @return
     * The DateCreated
     */
    public String getDateCreated() {
        return DateCreated;
    }

    /**
     *
     * @param DateCreated
     * The DateCreated
     */
    public void setDateCreated(String DateCreated) {
        this.DateCreated = DateCreated;
    }

    /**
     *
     * @return
     * The DateModified
     */
    public String getDateModified() {
        return DateModified;
    }

    /**
     *
     * @param DateModified
     * The DateModified
     */
    public void setDateModified(String DateModified) {
        this.DateModified = DateModified;
    }

    /**
     *
     * @return
     * The StartDate
     */
    public String getStartDate() {
        return StartDate;
    }

    /**
     *
     * @param StartDate
     * The StartDate
     */
    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    /**
     *
     * @return
     * The EndDate
     */
    public String getEndDate() {
        return EndDate;
    }

    /**
     *
     * @param EndDate
     * The EndDate
     */
    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    /**
     *
     * @return
     * The IsDeleted
     */
    public String getIsDeleted() {
        return IsDeleted;
    }

    /**
     *
     * @param IsDeleted
     * The IsDeleted
     */
    public void setIsDeleted(String IsDeleted) {
        this.IsDeleted = IsDeleted;
    }

    /**
     *
     * @return
     * The VoucherName
     */
    public String getVoucherName() {
        return VoucherName;
    }

    /**
     *
     * @param VoucherName
     * The VoucherName
     */
    public void setVoucherName(String VoucherName) {
        this.VoucherName = VoucherName;
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
     * The UserId
     */
    public Integer getUserId() {
        return UserId;
    }

    /**
     *
     * @param UserId
     * The UserId
     */
    public void setUserId(Integer UserId) {
        this.UserId = UserId;
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