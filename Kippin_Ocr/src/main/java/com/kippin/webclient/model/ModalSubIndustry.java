package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 4/23/2016.
 */
public class ModalSubIndustry
{
    private String SubIndustryName;

    private String SubIndustryId;

    public String getSubIndustryName ()
    {
        return SubIndustryName;
    }

    public void setSubIndustryName (String SubIndustryName)
    {
        this.SubIndustryName = SubIndustryName;
    }

    public String getSubIndustryId ()
    {
        return SubIndustryId;
    }

    public void setSubIndustryId (String SubIndustryId)
    {
        this.SubIndustryId = SubIndustryId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [SubIndustryName = "+SubIndustryName+", SubIndustryId = "+SubIndustryId+"]";
    }
}

