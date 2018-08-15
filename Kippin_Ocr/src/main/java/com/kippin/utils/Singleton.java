package com.kippin.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.kippin.storage.SharedPref;
import com.kippin.storage.SharedPrefReader;
import com.kippin.storage.SharedPrefWriter;
import com.kippin.webclient.model.ModelLogin;
import com.squareup.picasso.Picasso;

/**
 * Created by dilip.singh on 1/25/2016.
 */
public class Singleton {

    private static  ModelLogin user=null;
    public static boolean reloadStatements=false;
    public static Bitmap bm;
    private static Picasso picasso;
    public static boolean isCreditCard = false;

    public static Picasso getPicasso(Context context) {
        if (picasso == null) {
            picasso = new Picasso.Builder(context.getApplicationContext())
                    .build();
        }
        return picasso;
    }
    private Singleton() {
        throw new AssertionError("No instances.");
    }

    public static ModelLogin getUser(){

        if(Singleton.user==null || Singleton.user.getId()==null || Singleton.user.getId().length()==0 ||Singleton.user.getId().equalsIgnoreCase("0"))
        {
            Gson gson = new Gson();
            Singleton.user =  gson.fromJson(SharedPref.getString(SharedPref.USER,"") , ModelLogin.class) ;
        }

        return  Singleton.user;
    }

    public static void clear() {
        picasso = null;
//        user = null;
        bm=null;
        reloadStatements=false;
    }

    public static void putUser(ModelLogin modelLogin) {
        Singleton.user = modelLogin;
        SharedPref.put(SharedPref.USER,new Gson().toJson(modelLogin)) ;
//        new SharedPrefWriter(SharedPref.USER  , new Gson().toJson(modelLogin)) ;
    }

//    public ModelLogin getUser(){
//
//
//
//    }
}
