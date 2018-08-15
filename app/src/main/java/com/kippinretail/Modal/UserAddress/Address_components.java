package com.kippinretail.Modal.UserAddress;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep.saini on 2/9/2016.
 */
public class Address_components
{

    @SerializedName("long_name")
    @Expose
    private String long_name;

    @SerializedName("types")
    @Expose
    private List<String> types;

    @SerializedName("short_name")
    @Expose
    private String short_name;

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }
}
