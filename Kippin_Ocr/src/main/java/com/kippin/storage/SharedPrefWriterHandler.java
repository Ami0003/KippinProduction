package com.kippin.storage;

/**
 * Created by gaganpreet.singh on 1/21/2016.
 */
public interface SharedPrefWriterHandler {
    void onPreExecute();
    void doInBackground();
    void onDone(boolean result);
}
