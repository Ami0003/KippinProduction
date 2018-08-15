package com.kippinretail.ImagePickUtility;

import android.graphics.Bitmap;

/**
 * Created by gaganpreet.singh on 1/22/2016.
 */
public interface CameraPicListener  {

    void onImageSelected(Bitmap optionSelected);

    void onError();

    //void onImageSelected(String  image);

}
