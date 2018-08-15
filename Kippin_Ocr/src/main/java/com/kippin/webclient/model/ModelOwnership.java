package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 2/4/2016.
 */
public class ModelOwnership {
    private String OwnershipType;

    private String ResponseCode;

    private String Id;

    private String UserId;

    private String ResponseMessage;

    public String getOwnershipType ()
    {
        return OwnershipType;
    }

    public void setOwnershipType (String OwnershipType)
    {
        this.OwnershipType = OwnershipType;
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

    @Override
    public String toString()
    {
        return "ClassPojo [OwnershipType = "+OwnershipType+", ResponseCode = "+ResponseCode+", Id = "+Id+", UserId = "+UserId+", ResponseMessage = "+ResponseMessage+"]";
    }
}
