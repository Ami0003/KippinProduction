package com.kippin.storage;

import android.os.AsyncTask;

/**
 * Created by gaganpreet.singh on 1/21/2016.
 */
public class SharedPrefWriter extends AsyncTask<Void,Void,Boolean>{

    private SharedPrefWriterHandler handler;
    private String key = "";
    private Object  value;
    private  boolean result = false;


    private SharedPrefWriter(){

    }

    public  SharedPrefWriter(String key, Object value, SharedPrefWriterHandler handler){
        this(key, value);
        this.handler = handler;
    }


    public  SharedPrefWriter(String key, Object value){

        this.value=value;
        this.key = key;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(handler!=null)
            handler.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        if (value instanceof Boolean){
            return SharedPref.put(key,(Boolean)value);
        }else  if (value instanceof String){
            return  SharedPref.put (key, (String) value);
        }else if(value instanceof Long){
            return SharedPref.put(key, (Long) value);
        }else if(value instanceof Integer){
            return  SharedPref.put(key, (Integer)value);
        }else if(value instanceof Float){
            return   SharedPref.put(key, (Float)value);
        }

        return false;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if(handler!=null)
        handler.onDone(aBoolean);
    }


    public static  boolean clear(){
        return SharedPref.logout();
    }
}
