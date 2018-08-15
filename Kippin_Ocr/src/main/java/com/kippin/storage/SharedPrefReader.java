package com.kippin.storage;

import android.os.AsyncTask;

/**
 * Created by gaganpreet.singh on 1/21/2016.
 */
public class SharedPrefReader extends AsyncTask<Void,Void,Object>{

    private SharedPrefReaderHandler handler;
    private String key = "";
    private Object  defValue;
    private  boolean result = false;
    public <T> SharedPrefReader(String key, Class<T> defValue, SharedPrefReaderHandler handler){
        this(key, defValue);
        this.handler = handler;
    }


    public <T> SharedPrefReader(String key, Class<T> defValue){
        this.defValue=defValue;
        this.key = key;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(handler!=null)
            handler.onPreExecute();
    }

    @Override
    protected Object doInBackground(Void... params) {

        if (defValue instanceof Boolean){
            return SharedPref.getBoolean(key, (Boolean) defValue);
        }else  if (defValue instanceof String){
            return  SharedPref.getDecryptedString(key);
        }else if(defValue instanceof Long){
            return SharedPref.getLong(key, (Long) defValue);
        }else if(defValue instanceof Integer){
            return  SharedPref.getInt(key, (Integer) defValue);
        }else if(defValue instanceof Float){
            return   SharedPref.getFloat(key, (Float) defValue);
        }

        return null;
    }


    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);

        if(handler!=null)
        handler.onDone(result);
    }


}
