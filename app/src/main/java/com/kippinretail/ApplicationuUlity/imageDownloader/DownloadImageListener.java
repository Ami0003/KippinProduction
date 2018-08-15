package com.kippinretail.ApplicationuUlity.imageDownloader;

import android.graphics.Bitmap;

/**
 * Created by gaganpreet.singh on 4/12/2016.
 */
public interface DownloadImageListener {
    void onImageLoaded(Bitmap bitmap);
    void onError();
}
