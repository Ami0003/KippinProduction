
package com.kippin.webclient.model.automatic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountDisplayName {

    @SerializedName("defaultNormalAccountName")
    @Expose
    private String defaultNormalAccountName;

    public String getDefaultNormalAccountName() {
        return defaultNormalAccountName;
    }

    public void setDefaultNormalAccountName(String defaultNormalAccountName) {
        this.defaultNormalAccountName = defaultNormalAccountName;
    }

}
