package com.kippinretail;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.squareup.picasso.Picasso;

import java.io.File;

public class GalleryActivity extends Activity implements View.OnClickListener ,AdapterView.OnItemClickListener{

    private static Uri[] mUrls = null;
    private static String[] strUrls = null;
    private String[] mNames = null;
    private GridView gridview = null;
    private Cursor cc = null;
    private RelativeLayout layout_cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initilization();
    }

    private void initilization() {
        layout_cancle = (RelativeLayout)findViewById(R.id.layout_cancle);
        layout_cancle.setOnClickListener(this);
        final String orderBy = MediaStore.Images.Media._ID + " DESC";
        cc = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                orderBy);
        if (cc != null) {
            LoadingBox.showLoadingDialog(this, "Loading...");
            new Thread() {
                public void run() {
                    try {
                        cc.moveToFirst();
                        mUrls = new Uri[cc.getCount()];
                        strUrls = new String[cc.getCount()];
                        mNames = new String[cc.getCount()];
                        for (int i = 0; i < cc.getCount(); i++) {
                            cc.moveToPosition(i);
                            mUrls[i] = Uri.parse(cc.getString(1));
                            strUrls[i] = cc.getString(1);
                            mNames[i] = cc.getString(3);
                            Log.e("==============",strUrls[i]+"");

                        }

                    } catch (Exception e) {
                    }
                    LoadingBox.dismissLoadingDialog();
                }
            }.start();
            gridview = (GridView)findViewById(R.id.gridview);
            gridview.setBackgroundColor(Color.WHITE);
            gridview.setVerticalSpacing(1);
            gridview.setHorizontalSpacing(1);
            gridview.setAdapter(new ImageAdapter(this));
            gridview.setOnItemClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_cancle:
                Utils.backPressed(this);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent();
        i.putExtra("imagePath",strUrls[position]);
        setResult(RESULT_OK,i);
        finish();
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        int wPixel;
        int hPixel;

        public ImageAdapter(Context c) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mContext = c;
            wPixel = (mContext.getResources().getDisplayMetrics().widthPixels) / 3;
            hPixel = dpToPx(120);
        }

        public int dpToPx(int dps) {
            final float scale = getResources().getDisplayMetrics().density;
            int pixels = (int) (dps * scale + 0.5f);

            return pixels;
        }

        public int getCount() {
            return cc.getCount();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.galleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.imageview.setLayoutParams(new LinearLayout.LayoutParams(wPixel, hPixel));
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(mUrls!=null) {
                if (mUrls[position] != null && mUrls[position].getPath() != null) {
                    Uri uri = Uri.fromFile(new File(mUrls[position].getPath()));
                    Picasso.with(mContext)
                            .load(uri)
                            .placeholder(R.drawable.gift_bg)
                            .noFade().resize(wPixel, hPixel)
                            .centerCrop()
                            .into(holder.imageview);
                }
            }
            holder.imageview.setPadding(2, 2, 2, 2);
            holder.id = position;
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imageview;
        int id;
    }

}
