package com.kippinretail.retrofit;

import android.app.Activity;
import android.util.Log;

import com.google.gson.JsonElement;
import com.kippinretail.loadingindicator.LoadingBox;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by gaganpreet.singh on 4/13/2016.
 */
public class RestClientAdavanced extends RestClient {

    public static ApiService getApiServiceForPojo(Activity activity){
        LoadingBox.showLoadingDialog(activity, "Loading ...");
        return RestClient.getApiServiceForPojo();
    }

    public static Callback<JsonElement>  getCallback(final Callback<JsonElement> callback){
        return new Callback<JsonElement>()
        {
            @Override
            public void success(JsonElement o, Response response) {
                Log.e("RestClientAdavanced",response.getUrl()+" : ");
                Log.e("RestClientAdavanced"," : "+o+"  ");
            LoadingBox.dismissLoadingDialog();
                callback.success(o,response);
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                callback.failure(error);
                Log.e("RestClientAdavanced", error.getUrl() + " : " );
            }
        };
    }
}
