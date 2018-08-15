package com.kippin.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.encrypter.Encryption;
import com.kippin.app.Kippin;
import com.kippin.utils.Singleton;
import com.kippin.webclient.model.ModelLogin;


/**
 * Created by gaganpreet.singh on 1/21/2016.
 */
public final class SharedPrefWallet {


    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    private static final String key = "kippin_smartbuzzInc";
    private static final String STORAGE_FILE_NAME = "kippinWallet";

    public static String USER= "USER";

    public static final String REMEMBER_ME = "isRememberMe";
    /**
     *
     * @param key is shared pref key
     * @param value is data to write
     *
     *              This method will encrypt key and data before writing .
     */
    public static boolean putEncrypted(String key , String value ){
        String key_ =getEncrypted(key);
        String value_ =getEncrypted(value);
        return put(key_, value_);
    }

    /**
     *
     * @param key is shared pref key
     * @param value is data to write
     *
     *              This method will write data on particular key .
     */
    public static boolean put(String key , String value ){
        return getEditor().putString(key, value).commit();
    }

    /**
     *
     * @param key is shared pref key
     * @param value is data to write
     *
     *              This method will write data on particular key .
     */
    public static boolean put(String key , boolean value ){
        return getEditor().putBoolean(key, value).commit();
    }

    /**
     *
     * @param key is shared pref key
     * @param value is data to write
     *
     *              This method will write data on particular key .
     */
    public static boolean put(String key , int value ){
        return getEditor().putInt(key, value).commit();
    }

    /**
     *
     * @param key is shared pref key
     * @param value is data to write
     *
     *              This method will write data on particular key .
     */
    public static boolean put(String key , long value ){
        return getEditor().putLong(key, value).commit();
    }

    /**
     *
     * @param key is shared pref key
     * @param value is data to write
     *
     *              This method will write data on particular key .
     */
    public static boolean put(String key , float value ){
        return getEditor().putFloat(key, value).commit();
    }
    /**
     *
     * @param key is shared pref key
     * @param defvalue is data to write
     *
     *              This method will write data on particular key .
     */
    public  static String getString(String key , String defvalue ){
        return getPref().getString(key, defvalue);
    }
    /**
     *
     * @param key is shared pref key
     *
     *              This method will read data on particular key then decrypt it .
     */
    public  static String getDecryptedString(String key ){
        String key_ = getEncrypted(key);
        String data = getPref().getString(key,null);

        return (data !=null ? data : getDecrypted(data));
    }


    public static int getInt(String key, int defValue) {
        return getPref().getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return getPref().getLong(key, defValue);
    }

    public static  float getFloat(String key, float defValue) {
        return getPref().getFloat(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getPref().getBoolean(key, defValue);
    }

    public static boolean contains(String key) {
        return getPref().contains(key);
    }

    /**
     *
     * @return instance of sharedPref
     */
    private static SharedPreferences getPref(){
        try {

            return Kippin.kippin.getSharedPreferences(STORAGE_FILE_NAME, Context.MODE_PRIVATE);
        }catch(Exception ex){
            Log.e("141", ex.getMessage());
        }
        return  null;
    }

    /**
     *
     * @return instance of Editor
     */
    private static SharedPreferences.Editor getEditor(){
        return getPref().edit();
    }

    /**
     *
     * @param data input
     * @return Encrypted data
     */
    private static String getEncrypted(String data){
        Encryption encryption =new Encryption();
        return encryption.encrypt(data, key);
    }

    /**
     *
     * @param data input
     * @return Encrypted data
     */
    private static String getDecrypted(String data){
        Encryption encryption =new Encryption();
        return encryption.decrypt(data, key);
    }



    public static boolean isLoggedIn(){

        ModelLogin modelLogin = Singleton.getUser();

        boolean result =false;


        if(modelLogin!=null
                &&  modelLogin.getId()!=null
                &&  modelLogin.getId().length()>0
                &&  modelLogin.getUsername()!=null
                &&  modelLogin.getPassword()!= null
                && modelLogin.getUsername().length()>0
                && modelLogin.getPassword().length()>0
                ){
            result =  true;
        }

        return result ;

    }

   /* public static boolean logout() {
        return getEditor().clear().commit();
    }*/

}
