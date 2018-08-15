package com.kippin.webclient.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gaganpreet.singh on 1/22/2016.
 */
public class TemplateData {

    public String data ;
    public boolean aBoolean;

    private String resp = null;

    public <T> T getData(Class<T> t){

        return (T)this;
    }

    protected  String handleNull(String data) {

        return (data==null) ? "":data ;

    }

    public String getResponseMsg(){
//        {"ResponseCode":1,"ResponseMessage":"Success","UserId":0}

        if(resp !=null)
            return resp;

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);

            resp = jsonObject.getString("ResponseMessage");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resp;
    }

}
