package com.kippinretail.Modal;

import retrofit.mime.TypedString;

/**
 * Created by gaganpreet.singh on 4/8/2016.
 */
public class JsonBody extends TypedString{

    public JsonBody(String body) {
        super(body);
    }

    @Override
    public String mimeType() {
        return "application/json";
    }

}
