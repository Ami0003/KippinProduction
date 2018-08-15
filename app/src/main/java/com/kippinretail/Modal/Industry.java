package com.kippinretail.Modal;

/**
 * Created by kamaljeet.singh on 3/11/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Industry {

    @SerializedName("Id")
    @Expose
    private Integer Id;
    @SerializedName("IndustryType")
    @Expose
    private String IndustryType;

    /**
     * @return The Id
     */
    public Integer getId() {
        return Id;
    }

    /**
     * @param Id The Id
     */
    public void setId(Integer Id) {
        this.Id = Id;
    }

    /**
     * @return The IndustryType
     */
    public String getIndustryType() {
        return IndustryType;
    }

    /**
     * @param IndustryType The IndustryType
     */
    public void setIndustryType(String IndustryType) {
        this.IndustryType = IndustryType;
    }

}
