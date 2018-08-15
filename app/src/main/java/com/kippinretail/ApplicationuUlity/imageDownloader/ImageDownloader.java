package com.kippinretail.ApplicationuUlity.imageDownloader;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by gaganpreet.singh on 4/12/2016.
 */

public class ImageDownloader extends AsyncTask<String, String, Bitmap> {

    private ProgressDialog pDialog ;
    private Context context;
    private Bitmap bitmap;
    private String url = null;
    private DownloadImageListener downloadImageListener ;

    public ImageDownloader(Context context , String url , DownloadImageListener downloadImageListener){
        this.context = context;
        this.url = url;
        this.downloadImageListener = downloadImageListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        //pDialog.setMessage("Downloading Image ....");
        pDialog.show();

    }
    protected Bitmap doInBackground(String... args) {
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap image) {


            pDialog.dismiss();
        if(image != null){
            downloadImageListener.onImageLoaded(bitmap);
        }else{
            downloadImageListener.onError();
        }
    }
}

