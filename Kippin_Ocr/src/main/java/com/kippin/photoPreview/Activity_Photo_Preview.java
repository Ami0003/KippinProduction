package com.kippin.photoPreview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippin.activities.WebPdfViewActivity;
import com.kippin.cloudimages.ActivityCloudImages;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.utils.dialogbox.OnClickListener;
import com.kippin.utils.imageDownloader.DownloadImageListener;
import com.kippin.utils.imageDownloader.ImageDownloader;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.TemplateData;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Activity_Photo_Preview extends SuperActivity {

    public static final String IMAGE = "IMAGE";
    public static final String NAME = "NAME";
    public static final String PATH = "PATH";
    public static String PATH_Final = "PATH_FINAL";
    public static final String STATEMENTID = "StatementId";
    public static final String OPERATION_PHOTOPIC = "photopic";
    public static final String OPERATION = "operation";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1000 ;
    public static boolean reloadList = false;
    public String operation;

    ImageView ivImage;

    TextView tvClose;
    //  @Bind(R.id.btnUpload)
    TextView btnUpload;
    //@Bind(R.id.btnDelete)
    TextView btnDelete;
    //   @Bind(R.id.btnCancel)
    TextView btnCancel,tvView;
    String url = null;
    String name = null;
    String path = null;
    String statementId = null;

    @Override
    public void initialiseUI() {
        super.initialiseUI();

    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview_finance);
        permissionUpload();
        initialiseUI();

        ivImage = (ImageView) findViewById(R.id.ivImage);
        tvClose = (TextView) findViewById(R.id.tvClose);
        btnUpload = (TextView) findViewById(R.id.btnUpload);
        btnDelete = (TextView) findViewById(R.id.btnDelete);
        btnCancel = (TextView) findViewById(R.id.btnCancel);
        tvView=(TextView) findViewById(R.id.tvView);
        setUpListeners();

        operation = getIntent().getStringExtra(OPERATION);


        if (operation != null) {
            initiatePhotoPicOperation();
        } else {

            // This functin is call ed when i am goint to upload any image
            // a) Add Cash Entry
            // b) Edit cash Entry
            // c) While Edit Click on Camera Button

            processNonOperation();
        }

    }

    private void initiatePhotoPicOperation() {
//
        btnDelete.setVisibility(View.GONE);
        btnUpload.setVisibility(View.GONE);

        btnCancel.setText("Choose");

        ivImage.setImageBitmap(Utility.bitmap);
    }

    private void processNonOperation() {
        url = getIntent().getStringExtra(IMAGE);
        name = getIntent().getStringExtra(NAME);
        path = getIntent().getStringExtra(PATH);
        PATH_Final=getIntent().getStringExtra("PATH_SERVER");
        statementId = getIntent().getStringExtra(STATEMENTID);
        Log.e("statementId:", "" + statementId);
        Log.e("name:", "" + name);
        Log.e("url:", "" + url);
        tvClose.setText("Cancel");
        boolean isClicked = getIntent().getBooleanExtra(ActivityCloudImages.CLICK_NEEDED, false);

        btnCancel.setVisibility(View.GONE);


        if (!isClicked) {

            generateActionBar(R.string.uploaded_bills, true, true);

            btnUpload.setVisibility(View.GONE);
            tvView.setVisibility(View.VISIBLE);
            tvClose.setText("Close");
        } else {


            generateActionBar(R.string.cloud_images, true, true);

            btnDelete.setVisibility(View.GONE);
        }
        Picasso.with(this).load(url).into(ivImage);
    }

    public void permissionUpload() {
        if (ContextCompat.checkSelfPermission(Activity_Photo_Preview.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Activity_Photo_Preview.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(Activity_Photo_Preview.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void setUpListeners() {
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operation != null) {
                    Utility.cameraPicListener.onImageSelected(Utility.bitmap);
                }
                finish();
                Utility.bitmap = null;
                Utility.cameraPicListener = null;
            }
        });
        tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("url_path",PATH_Final);
                Utility.startActivity(Activity_Photo_Preview.this, WebPdfViewActivity.class, bundle, true);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DialogBox(Activity_Photo_Preview.this, "Are you sure you want to delete?", new OnClickListener() {
                    @Override
                    public void onPositiveClick() {
                        ArrayListPost templatePosts = new ArrayListPost();
                        templatePosts.add("ImagePath", path);
                        templatePosts.add("bankExpenseID", statementId);
                        templatePosts.add("ImageName", name);

                        WSUtils.hitService(Activity_Photo_Preview.this, Url.URL_DELETE_IMAGE, templatePosts, WSMethods.POST, TemplateData.class, new WSInterface() {
                            @Override
                            public void onResult(int requestCode, TemplateData data) {
                                Log.e("Response:", "" + data.aBoolean);
                                String message = null;
                                 /*   if (Boolean.parseBoolean(data.data)) {
                                        message = "Deleted Successfully";
                                        reloadList = true;
                                    } else {
                                        message = "Deleted Not Successfully";
                                    }*/
                                message = "Deleted Successfully";
                                reloadList = true;

                                new DialogBox(Activity_Photo_Preview.this, "" + message, new DialogBoxListener() {
                                    @Override
                                    public void onDialogOkPressed() {
                                        Activity_Photo_Preview.this.finish();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });


            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.bitmap = null;
                Utility.cameraPicListener = null;
                finish();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Utility.yearActivity != null)
                        Utility.yearActivity.finish();

                    if (Utility.monthActivity != null)
                        Utility.monthActivity.finish();

                    if (Utility.activityCloudImages != null)
                        Utility.activityCloudImages.finish();


                    Utility.showProgressDialog(Activity_Photo_Preview.this, R.string.pls_wait);

                    new ImageDownloader(Activity_Photo_Preview.this, url, new DownloadImageListener() {
                        @Override
                        public void onImageLoaded(Bitmap bitmap) {


                            final File location = new File(Utility.getFolderName() + File.separator + "temp.png");

                            if (location.exists()) ;
                            location.delete();

                            try {
                                location.createNewFile();
                                FileOutputStream out = new FileOutputStream(location);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                Bitmap bitmap1 = Utility.resizeIfneeded(location);
                                Utility.cloudClickListener.onCloudClick(bitmap1, name, path, ActivityCloudImages.month, ActivityCloudImages.year);
                                Activity_Photo_Preview.this.finish();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Utility.dismissProgressDialog();

                        }

                        @Override
                        public void onError() {
                            //Utility.cloudClickListener.onCloudClick(null, cloudImages.get(position).getImageName(), ActivityCloudImages.month, ActivityCloudImages.year);
                            new DialogBox(Activity_Photo_Preview.this, "Unable to load image from cloud,Please try later", (DialogBoxListener) null);

                            Activity_Photo_Preview.this.finish();
                        }
                    }).execute();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }

}
