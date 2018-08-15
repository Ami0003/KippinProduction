package com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep.singh on 7/16/2016.
 */
public class ServerResponseForLOcationAnalysis
{
    @SerializedName("Amount")
    @Expose
    private String Amount;
    @SerializedName("Country")
    @Expose
    private String Country;

    public String getAmount()  {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }
}
