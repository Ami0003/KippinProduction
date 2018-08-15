package com.kippinretail.KippinInvoice.ModalInvoice;

/**
 * Created by kamaljeet.singh on 11/24/2016.
 */

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Revenue {

   @SerializedName("Id")
   @Expose
   private Integer id;
   @SerializedName("CategoryId")
   @Expose
   private int categoryId;
   @SerializedName("ClassificationType")
   @Expose
   private String classificationType;
   @SerializedName("ChartAccountNumber")
   @Expose
   private String chartAccountNumber;
   @SerializedName("CreatedDate")
   @Expose
   private String createdDate;
   @SerializedName("ModifiedDate")
   @Expose
   private String modifiedDate;
   @SerializedName("IsDeleted")
   @Expose
   private Boolean isDeleted;
   @SerializedName("IsSole")
   @Expose
   private Boolean isSole;
   @SerializedName("IsIncorporated")
   @Expose
   private Boolean isIncorporated;
   @SerializedName("IsPartnerShip")
   @Expose
   private Boolean isPartnerShip;
   @SerializedName("Desc")
   @Expose
   private String desc;
   @SerializedName("UserId")
   @Expose
   private String userId;
   @SerializedName("IndustryId")
   @Expose
   private String industryId;
   @SerializedName("SubIndustryId")
   @Expose
   private String subIndustryId;
   @SerializedName("Type")
   @Expose
   private String type;
   @SerializedName("Name")
   @Expose
   private String name;
   @SerializedName("RangeofAct")
   @Expose
   private String rangeofAct;
   @SerializedName("CategoryValue")
   @Expose
   private String categoryValue;
   @SerializedName("ResponseCode")
   @Expose
   private String responseCode;
   @SerializedName("ResponseMessage")
   @Expose
   private String responseMessage;
    private boolean isSectionItem;

    public boolean isSectionItem() {
        return isSectionItem;
    }

    public void setSectionItem(boolean sectionItem) {
        isSectionItem = sectionItem;
    }

   /**
    *
    * @return
    * The id
    */
   public Integer getId() {
       return id;
   }

   /**
    *
    * @param id
    * The Id
    */
   public void setId(Integer id) {
       this.id = id;
   }

   public Revenue withId(Integer id) {
       this.id = id;
       return this;
   }

   /**
    *
    * @return
    * The categoryId
    */
   public int getCategoryId() {
       return categoryId;
   }

   /**
    *
    * @param categoryId
    * The CategoryId
    */
   public void setCategoryId(int categoryId) {
       this.categoryId = categoryId;
   }

   public Revenue withCategoryId(Integer categoryId) {
       this.categoryId = categoryId;
       return this;
   }

   /**
    *
    * @return
    * The classificationType
    */
   public String getClassificationType() {
       return classificationType;
   }

   /**
    *
    * @param classificationType
    * The ClassificationType
    */
   public void setClassificationType(String classificationType) {
       this.classificationType = classificationType;
   }

   public Revenue withClassificationType(String classificationType) {
       this.classificationType = classificationType;
       return this;
   }

   /**
    *
    * @return
    * The chartAccountNumber
    */
   public String getChartAccountNumber() {
       return chartAccountNumber;
   }

   /**
    *
    * @param chartAccountNumber
    * The ChartAccountNumber
    */
   public void setChartAccountNumber(String chartAccountNumber) {
       this.chartAccountNumber = chartAccountNumber;
   }

   public Revenue withChartAccountNumber(String chartAccountNumber) {
       this.chartAccountNumber = chartAccountNumber;
       return this;
   }

   /**
    *
    * @return
    * The createdDate
    */
   public String getCreatedDate() {
       return createdDate;
   }

   /**
    *
    * @param createdDate
    * The CreatedDate
    */
   public void setCreatedDate(String createdDate) {
       this.createdDate = createdDate;
   }

   public Revenue withCreatedDate(String createdDate) {
       this.createdDate = createdDate;
       return this;
   }

   /**
    *
    * @return
    * The modifiedDate
    */
   public String getModifiedDate() {
       return modifiedDate;
   }

   /**
    *
    * @param modifiedDate
    * The ModifiedDate
    */
   public void setModifiedDate(String modifiedDate) {
       this.modifiedDate = modifiedDate;
   }

   public Revenue withModifiedDate(String modifiedDate) {
       this.modifiedDate = modifiedDate;
       return this;
   }

   /**
    *
    * @return
    * The isDeleted
    */
   public Boolean getIsDeleted() {
       return isDeleted;
   }

   /**
    *
    * @param isDeleted
    * The IsDeleted
    */
   public void setIsDeleted(Boolean isDeleted) {
       this.isDeleted = isDeleted;
   }

   public Revenue withIsDeleted(Boolean isDeleted) {
       this.isDeleted = isDeleted;
       return this;
   }

   /**
    *
    * @return
    * The isSole
    */
   public Boolean getIsSole() {
       return isSole;
   }

   /**
    *
    * @param isSole
    * The IsSole
    */
   public void setIsSole(Boolean isSole) {
       this.isSole = isSole;
   }

   public Revenue withIsSole(Boolean isSole) {
       this.isSole = isSole;
       return this;
   }

   /**
    *
    * @return
    * The isIncorporated
    */
   public Boolean getIsIncorporated() {
       return isIncorporated;
   }

   /**
    *
    * @param isIncorporated
    * The IsIncorporated
    */
   public void setIsIncorporated(Boolean isIncorporated) {
       this.isIncorporated = isIncorporated;
   }

   public Revenue withIsIncorporated(Boolean isIncorporated) {
       this.isIncorporated = isIncorporated;
       return this;
   }

   /**
    *
    * @return
    * The isPartnerShip
    */
   public Boolean getIsPartnerShip() {
       return isPartnerShip;
   }

   /**
    *
    * @param isPartnerShip
    * The IsPartnerShip
    */
   public void setIsPartnerShip(Boolean isPartnerShip) {
       this.isPartnerShip = isPartnerShip;
   }

   public Revenue withIsPartnerShip(Boolean isPartnerShip) {
       this.isPartnerShip = isPartnerShip;
       return this;
   }

   /**
    *
    * @return
    * The desc
    */
   public String getDesc() {
       return desc;
   }

   /**
    *
    * @param desc
    * The Desc
    */
   public void setDesc(String desc) {
       this.desc = desc;
   }

   public Revenue withDesc(String desc) {
       this.desc = desc;
       return this;
   }

   /**
    *
    * @return
    * The userId
    */
   public Object getUserId() {
       return userId;
   }

   /**
    *
    * @param userId
    * The UserId
    */
   public void setUserId(String userId) {
       this.userId = userId;
   }

   public Revenue withUserId(String userId) {
       this.userId = userId;
       return this;
   }

   /**
    *
    * @return
    * The industryId
    */
   public Object getIndustryId() {
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

   public Revenue withIndustryId(String industryId) {
       this.industryId = industryId;
       return this;
   }

   /**
    *
    * @return
    * The subIndustryId
    */
   public Object getSubIndustryId() {
       return subIndustryId;
   }

   /**
    *
    * @param subIndustryId
    * The SubIndustryId
    */
   public void setSubIndustryId(String subIndustryId) {
       this.subIndustryId = subIndustryId;
   }

   public Revenue withSubIndustryId(String subIndustryId) {
       this.subIndustryId = subIndustryId;
       return this;
   }

   /**
    *
    * @return
    * The type
    */
   public String getType() {
       return type;
   }

   /**
    *
    * @param type
    * The Type
    */
   public void setType(String type) {
       this.type = type;
   }

   public Revenue withType(String type) {
       this.type = type;
       return this;
   }

   /**
    *
    * @return
    * The name
    */
   public String getName() {
       return name;
   }

   /**
    *
    * @param name
    * The Name
    */
   public void setName(String name) {
       this.name = name;
   }

   public Revenue withName(String name) {
       this.name = name;
       return this;
   }

   /**
    *
    * @return
    * The rangeofAct
    */
   public Object getRangeofAct() {
       return rangeofAct;
   }

   /**
    *
    * @param rangeofAct
    * The RangeofAct
    */
   public void setRangeofAct(String rangeofAct) {
       this.rangeofAct = rangeofAct;
   }

   public Revenue withRangeofAct(String rangeofAct) {
       this.rangeofAct = rangeofAct;
       return this;
   }

   /**
    *
    * @return
    * The categoryValue
    */
   public Object getCategoryValue() {
       return categoryValue;
   }

   /**
    *
    * @param categoryValue
    * The CategoryValue
    */
   public void setCategoryValue(String categoryValue) {
       this.categoryValue = categoryValue;
   }

   public Revenue withCategoryValue(String categoryValue) {
       this.categoryValue = categoryValue;
       return this;
   }

   /**
    *
    * @return
    * The responseCode
    */
   public String getResponseCode() {
       return responseCode;
   }

   /**
    *
    * @param responseCode
    * The ResponseCode
    */
   public void setResponseCode(String responseCode) {
       this.responseCode = responseCode;
   }

   public Revenue withResponseCode(String responseCode) {
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

   public Revenue withResponseMessage(String responseMessage) {
       this.responseMessage = responseMessage;
       return this;
   }

}
