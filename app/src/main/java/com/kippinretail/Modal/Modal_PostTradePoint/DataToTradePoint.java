package com.kippinretail.Modal.Modal_PostTradePoint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep.saini on 5/10/2016.
 */
public class DataToTradePoint {
    @SerializedName("ObjList")
    @Expose
    List<ObjList> ObjList;

    public List<ObjList> getObjListList() {
        return ObjList;
    }

    public void setObjListList(List<ObjList> objListList) {
        this.ObjList = objListList;
    }
}
