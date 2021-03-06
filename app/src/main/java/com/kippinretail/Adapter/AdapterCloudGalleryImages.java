package com.kippinretail.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.Modal.kippinggallery.KippinCloudgallery;
import com.kippinretail.kippingallerypreview.KippinCloudGalleryPreviewActivity;
import com.kippin.app.Kippin;
import com.kippin.photoPreview.Activity_Photo_Preview;
import com.kippin.utils.Singleton;
import com.kippin.utils.Utility;
import com.squareup.picasso.Callback;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * Created by agnihotri on 07/11/17.
 */

public class AdapterCloudGalleryImages extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    private List<KippinCloudgallery> cloudImages;


    public  int height = 0;
    public  int width = 0;
    public AdapterCloudGalleryImages(Activity activity, List<KippinCloudgallery> cloudImages) {
        this.activity = activity;
        this.cloudImages = cloudImages;
        inflater = activity.getLayoutInflater();

        width = Utility.dpToPx(activity, (int) Kippin.kippin.getResources().getDimension(com.kippin.kippin.R.dimen.cloud_img_width));
        height = Utility.dpToPx(activity, (int) Kippin.kippin.getResources().getDimension(com.kippin.kippin.R.dimen.cloud_img_height));

    }

    @Override
    public int getCount() {
        return cloudImages.size();
    }

    @Override
    public Object getItem(int position) {
        return cloudImages.get(position);
    }

    @Override
    public long getItemId(int position)

    {
        return position;
    }


    class ViewHolder{
        ImageView imageView;
        TextView tvStar;
    }

    AdapterCloudGalleryImages.ViewHolder viewHolder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (true) {
            convertView = inflater.inflate(com.kippin.kippin.R.layout.includecloudimage, parent, false);
            viewHolder= new AdapterCloudGalleryImages.ViewHolder();
            viewHolder.tvStar = (TextView)convertView.findViewById(com.kippin.kippin.R.id.star);
            viewHolder.imageView= (ImageView) convertView.findViewById(com.kippin.kippin.R.id.ivImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder= (AdapterCloudGalleryImages.ViewHolder) convertView.getTag();
        }

        String url = cloudImages.get(position).getTemplatePath();

//        new DownloadImage(url,viewholder).execute();

//        url = "http://media.gettyimages.com/photos/british-finance-minister-george-osborne-poses-for-pictures-with-the-picture-id515880336";
//        url = "http://web2.anzleads.com/test/123.png";
//        url = " http://kippin_finance_api.web1.anzleads.com/OcrImages/12/171/2016-03-16--03-06-24-1458077777541.png";
//        url = "http://kippinfinanceapi.web1.anzleads.com/OcrImages/12/171/2016-03-16--03-06-24-1458077777541.png";


        if(url!=null){

            String url_pre = url.substring(0,url.indexOf(".com")+4);
            url_pre = url_pre.replace("_","");
            String urlPost = url.substring(url.indexOf(".com")+4, url.length());
            url = url_pre+urlPost;



            Log.e("modified url ", url);
            Singleton.getPicasso(activity).load(url)
                    .resize(width,height)
                    .centerInside()
                    .placeholder(com.kippin.kippin.R.drawable.loading)
                    .into(viewHolder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }


        final String url1 = url;

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Bundle bundle = new Bundle();
                bundle.putString(Activity_Photo_Preview.IMAGE ,url1 );

                bundle.putString(KippinCloudGalleryPreviewActivity.NAME, cloudImages.get(position).getTemplateName());
                bundle.putString(KippinCloudGalleryPreviewActivity.ID, ""+cloudImages.get(position).getId());
                bundle.putString(KippinCloudGalleryPreviewActivity.PATH, cloudImages.get(position).getTemplatePath());
                Utility.startActivity(activity,KippinCloudGalleryPreviewActivity.class,bundle,true);


            }
        });

        return convertView;
    }

//    public static Bitmap loadBitmapFromView(View v) {
//        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(b);
//        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
//        v.draw(c);Utility.convertBitmap2Base64(b)
//        return b;
//    }


    // DownloadImage AsyncTask
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        String url;
        ImageView imageView;

        public  DownloadImage(String url,ImageView imageView){
            this.url=url;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            Bitmap bitmap = null;
            try {
                // Download Image from URL
//                InputStream input = new java.net.URL(url).openStream();
//                // Decode Bitmap
//                bitmap = BitmapFactory.decodeStream(input);

//                url = "http://media.gettyimages.com/photos/british-finance-minister-george-osborne-poses-for-pictures-with-the-picture-id515880336";

                java.net.URL url_ = new java.net.URL(url);
                HttpURLConnection connection = (HttpURLConnection) url_
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return getResizedBitmap(myBitmap,height,width);


            } catch (Exception e) {
                e.printStackTrace();
            }
            catch (Error e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                    matrix, false);

            return resizedBitmap;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            try {
                imageView.setImageBitmap(result);
            }catch (Exception e){
                e.printStackTrace();
            }
            catch (Error e){
                e.printStackTrace();
            }
        }
    }


}

