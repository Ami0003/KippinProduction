package com.kippinretail.Modal.MerchantList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit.mime.TypedInput;

/**
 * Created by gaganpreet.singh on 4/8/2016.
 */
public class ModalBodyMerchant implements TypedInput {
    private ArrayList<ObjList> ObjList;

    public ArrayList<ObjList> getObjList ()
    {
        return ObjList;
    }

    public void setObjList (ArrayList<ObjList> ObjList)
    {
        this.ObjList = ObjList;
    }

    @Override
    public String mimeType() {
        return "";
    }

    @Override
    public long length() {
        return 0;
    }

    @Override
    public InputStream in() throws IOException {
        return null;
    }
}
