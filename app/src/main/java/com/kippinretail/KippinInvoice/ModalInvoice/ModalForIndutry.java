package com.kippinretail.KippinInvoice.ModalInvoice;

/**
 * Created by kamaljeet.singh on 11/24/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ModalForIndutry {

   @SerializedName("IndustryId")
   @Expose
   private String industryId;
   @SerializedName("IndustryName")
   @Expose
   private String industryName;
   @SerializedName("ObjSubIndustryList")
   @Expose
   private List<ObjSubIndustryList> objSubIndustryList = new ArrayList<ObjSubIndustryList>();
   @SerializedName("ResponseCode")
   @Expose
   private Integer responseCode;
   @SerializedName("ResponseMessage")
   @Expose
   private String responseMessage;
   @SerializedName("UserId")
   @Expose
   private Integer userId;

   /**
    *
    * @return
    * The industryId
    */
   public String getIndustryId() {
       return industryId;
   }

   /**
    *
    * @param industryId
    * The IndustryId
    */
   public void setIndustryId(String industryId) {
       this.industryId = industryId;
   }

   public ModalForIndutry withIndustryId(String industryId) {
       this.industryId = industryId;
       return this;
   }

   /**
    *
    * @return
    * The industryName
    */
   public String getIndustryName() {
       return industryName;
   }

   /**
    *
    * @param industryName
    * The IndustryName
    */
   public void setIndustryName(String industryName) {
       this.industryName = industryName;
   }

   public ModalForIndutry withIndustryName(String industryName) {
       this.industryName = industryName;
       return this;
   }

   /**
    *
    * @return
    * The objSubIndustryList
    */
   public List<ObjSubIndustryList> getObjSubIndustryList() {
       return objSubIndustryList;
   }

   /**
    *
    * @param objSubIndustryList
    * The ObjSubIndustryList
    */
   public void setObjSubIndustryList(List<ObjSubIndustryList> objSubIndustryList) {
       this.objSubIndustryList = objSubIndustryList;
   }

   public ModalForIndutry withObjSubIndustryList(List<ObjSubIndustryList> objSubIndustryList) {
       this.objSubIndustryList = objSubIndustryList;
       return this;
   }

   /**
    *
    * @return
    * The responseCode
    */
   public Integer getResponseCode() {
       return responseCode;
   }

   /**
    *
    * @param responseCode
    * The ResponseCode
    */
   public void setResponseCode(Integer responseCode) {
       this.responseCode = responseCode;
   }

   public ModalForIndutry withResponseCode(Integer responseCode) {
       this.responseCode = responseCode;
       return this;
   }

   /**
    *
    * @return
    * The responseMessage
    */
   public String getResponseMessage() {
       return responseMessage;
   }

   /**
    *
    * @param responseMessage
    * The ResponseMessage
    */
   public void setResponseMessage(String responseMessage) {
       this.responseMessage = responseMessage;
   }

   public ModalForIndutry withResponseMessage(String responseMessage) {
       this.responseMessage = responseMessage;
       return this;
   }

   /**
    *
    * @return
    * The userId
    */
   public Integer getUserId() {
       return userId;
   }

   /**
    *
    * @param userId
    * The UserId
    */
   public void setUserId(Integer userId) {
       this.userId = userId;
   }

   public ModalForIndutry withUserId(Integer userId) {
       this.userId = userId;
       return this;
   }

}
