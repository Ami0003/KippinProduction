package com.kippinretail.Modal.MerchantList;

/**
 * Created by gaganpreet.singh on 4/8/2016.
 */

public class ObjList
{
    private String MerchantId;

    private String FriendId;

    private String UserId;

    private String Points;

    public String getMerchantId ()
    {
        return MerchantId;
    }

    public void setMerchantId (String MerchantId)
    {
        this.MerchantId = MerchantId;
    }

    public String getFriendId ()
    {
        return FriendId;
    }

    public void setFriendId (String FriendId)
    {
        this.FriendId = FriendId;
    }

    public String getUserId ()
    {
        return UserId;
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

    public String getPoints ()
    {
        return Points;
    }

    public void setPoints (String Points)
    {
        this.Points = Points;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [MerchantId = "+MerchantId+", FriendId = "+FriendId+", UserId = "+UserId+", Points = "+Points+"]";
    }
}

