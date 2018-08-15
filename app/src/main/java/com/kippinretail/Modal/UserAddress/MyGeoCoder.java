package com.kippinretail.Modal.UserAddress;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep.saini on 2/9/2016.
 */
public class MyGeoCoder
{
    @SerializedName("results")
    @Expose
    private List< Results> results;

    @SerializedName("status")
    @Expose
    private String status;

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
