package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 5/25/2016.
 */
public class CurrentBalance {
    private String amount;

    private String currencyCode;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getCurrencyCode ()
    {
        return currencyCode;
    }

    public void setCurrencyCode (String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", currencyCode = "+currencyCode+"]";
    }
}
