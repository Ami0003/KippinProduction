package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 2/4/2016.
 */
public class ModelCurrency {
    private String ResponseCode;

    private String Id;

    private String UserId;

    private String ResponseMessage;

    private String CurrencyType;

    private String Country;
    private String Symbol;
    private String SymbolCode;

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getSymbolCode() {
        return SymbolCode;
    }

    public void setSymbolCode(String symbolCode) {
        SymbolCode = symbolCode;
    }

    public String getResponseCode ()
    {
        return ResponseCode;
    }

    public void setResponseCode (String ResponseCode)
    {
        this.ResponseCode = ResponseCode;
    }

    public String getId ()
    {
        return Id;
    }

    public void setId (String Id)
    {
        this.Id = Id;
    }

    public String getUserId ()
    {
        return UserId;
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

    public String getResponseMessage ()
    {
        return ResponseMessage;
    }

    public void setResponseMessage (String ResponseMessage)
    {
        this.ResponseMessage = ResponseMessage;
    }

    public String getCurrencyType ()
    {
        return CurrencyType;
    }

    public void setCurrencyType (String CurrencyType)
    {
        this.CurrencyType = CurrencyType;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ResponseCode = "+ResponseCode+", Id = "+Id+", UserId = "+UserId+", ResponseMessage = "+ResponseMessage+", CurrencyType = "+CurrencyType+"]";
    }
}
