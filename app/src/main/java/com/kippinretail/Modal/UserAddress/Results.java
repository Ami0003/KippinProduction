package com.kippinretail.Modal.UserAddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep.saini on 2/9/2016.
 */
public class Results {

    @SerializedName("place_id")
    @Expose
    private String place_id;

    @SerializedName("address_components")
    @Expose
    private List<Address_components> address_components;

    @SerializedName("formatted_address")
    @Expose
    private String formatted_address;

    @SerializedName("types")
    @Expose
    private List<String> types;

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public List<Address_components> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(List<Address_components> address_components) {
        this.address_components = address_components;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
