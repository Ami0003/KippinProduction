package com.kippinretail.Modal;

import java.io.UnsupportedEncodingException;

import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * Created by gaganpreet.singh on 4/13/2016.
 */
public class ModalResponse {
//    {"ResponseCode":1,"UserId":0,"ResponseMessage":"Loyality card import successfully."}

    protected String ResponseCode,ResponseMessage;

    public TypedInput getTypedInput(String data){
        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  in;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }
}
