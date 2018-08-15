package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 5/25/2016.
 */
public class AccountDisplayName {
    private String defaultNormalAccountName;

    public String getDefaultNormalAccountName ()
    {
        return defaultNormalAccountName;
    }

    public void setDefaultNormalAccountName (String defaultNormalAccountName)
    {
        this.defaultNormalAccountName = defaultNormalAccountName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [defaultNormalAccountName = "+defaultNormalAccountName+"]";
    }
}
