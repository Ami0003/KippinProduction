package com.kippin.retrofit;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kippin.utils.Url;


import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedInput;


/**
 * Define all server calls here
 */
public interface ApiService {


    @GET("/GetAllAccountTypeList/{UserId}")
    @Headers("Content-Type:application/json; charset=utf-8")
    public void getAccountList(@Path("UserId")int userid, Callback<JsonObject> response);

    @PUT("/KippinFinanceApi/Account/User/UpdatePassword")
    public void UpdatePassword(@Body TypedInput body, Callback<JsonElement> response);


}