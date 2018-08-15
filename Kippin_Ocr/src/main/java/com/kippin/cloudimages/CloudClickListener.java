package com.kippin.cloudimages;

import android.graphics.Bitmap;

/**
 * Created by gaganpreet.singh on 2/19/2016.
 */
public interface CloudClickListener  {
    void onCloudClick(Bitmap bitmap,String cloudImageName, String cloudImagePath,String month , String year);
}
