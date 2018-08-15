package com.kippinretail.KippinInvoice.ModalInvoice;

/**
 * Created by kamaljeet.singh on 11/24/2016.
 */

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjSubIndustryList {

   @SerializedName("SubIndustryId")
   @Expose
   private Integer subIndustryId;
   @SerializedName("SubIndustryName")
   @Expose
   private String subIndustryName;

   /**
    *
    * @return
    * The subIndustryId
    */
   public Integer getSubIndustryId() {
       return subIndustryId;
   }

   /**
    *
    * @param subIndustryId
    * The SubIndustryId
    */
   public void setSubIndustryId(Integer subIndustryId) {
       this.subIndustryId = subIndustryId;
   }

   public ObjSubIndustryList withSubIndustryId(Integer subIndustryId) {
       this.subIndustryId = subIndustryId;
       return this;
   }

   /**
    *
    * @return
    * The subIndustryName
    */
   public String getSubIndustryName() {
       return subIndustryName;
   }

   /**
    *
    * @param subIndustryName
    * The SubIndustryName
    */
   public void setSubIndustryName(String subIndustryName) {
       this.subIndustryName = subIndustryName;
   }

   public ObjSubIndustryList withSubIndustryName(String subIndustryName) {
       this.subIndustryName = subIndustryName;
       return this;
   }

}
