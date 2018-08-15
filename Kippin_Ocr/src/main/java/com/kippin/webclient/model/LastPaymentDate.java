
package com.kippin.webclient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastPaymentDate {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("localFormat")
    @Expose
    private String localFormat;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocalFormat() {
        return localFormat;
    }

    public void setLocalFormat(String localFormat) {
        this.localFormat = localFormat;
    }

}
