package com.kippin.utils.ImagePickUtility;

import android.graphics.Bitmap;

import com.kippin.webclient.model.TemplateData;

/**
 * Created by gaganpreet.singh on 3/11/2016.
 */
public interface OnResultListener {
    void openOCR(TemplateData data, Bitmap bm,boolean isSucess) ;


}
