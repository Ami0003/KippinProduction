package com.kippin.webclient.model;

import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 2/4/2016.
 */
public class ModelIndustry  {

    private String ResponseCode;

    private String IndustryId;

    private String UserId;

    private String IndustryName;

    private String ResponseMessage;

    private ArrayList<ModalSubIndustry> ObjSubIndustryList;


    public ArrayList<ModalSubIndustry> getObjSubIndustryList() {
        return ObjSubIndustryList;
    }

    public void setObjSubIndustryList(ArrayList<ModalSubIndustry> objSubIndustryList) {
        ObjSubIndustryList = objSubIndustryList;
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
        return IndustryId;
    }

    public void setId (String Id)
    {
        this.IndustryId = Id;
    }

    public String getUserId ()
    {
        return UserId;
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

    public String getIndustryType ()
    {
        return IndustryName;
    }

    public void setIndustryType (String IndustryType)
    {
        this.IndustryName = IndustryType;
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
        return "ClassPojo [ResponseCode = "+ResponseCode+", Id = "+IndustryId+", UserId = "+UserId+", IndustryType = "+IndustryName+", ResponseMessage = "+ResponseMessage+"]";
    }

}
