package com.kippin.utils.ImagePickUtility;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.customdatepicker.Month_YearPicker;
import com.kippin.addCashEntry.ActivityCashEntry;
import com.kippin.app.Kippin;
import com.kippin.kippin.BuildConfig;
import com.kippin.kippin.R;
import com.kippin.photoPreview.Activity_Photo_Preview;
import com.kippin.selectdate.ModelBankStatement;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModelImagePost;
import com.kippin.webclient.model.TemplateData;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gaganpreet.singh on 1/22/2016.
 */
public class CapturePicView {
    // The request code used in ActivityCompat.requestPermissions()
// and returned in the Activity's onRequestPermissionsResult()
    int PERMISSION_ALL = 1;
    Month_YearPicker month_yearPicker;
    public static String monthString = "", yearString = "";
    String cameragallery = "";
    public static final int REQUEST_CODE_CAMERA = 10001;
    public static final int REQUEST_CODE_GALLERY = 10002;
    public static final int PIC_CROP = 2;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    static private Uri mImageUri;
    CameraPicListener listener;
    Dialog lDialog;
    Activity context;
    boolean isCamerClicked = false;
    Handler handler = new Handler();
    String errorMessage = "";
    private Activity activity;
    private int REQUEST_CAMERA = 0;
    private OnCloudClickListener onCloudClickListener;
    private boolean isCrop;
    private boolean isChooserNeeded = false;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public String monthName(String monthname) {
        if (monthname.equals("January")) {
            monthname = "01";
            return monthname;
        } else if (monthname.equals("February")) {
            monthname = "02";
            return monthname;
        } else if (monthname.equals("March")) {
            monthname = "03";
            return monthname;
        } else if (monthname.equals("April")) {
            monthname = "04";
            return monthname;
        } else if (monthname.equals("May")) {
            monthname = "05";
            return monthname;
        } else if (monthname.equals("June")) {
            monthname = "06";
            return monthname;
        } else if (monthname.equals("July")) {
            monthname = "07";
            return monthname;
        } else if (monthname.equals("August")) {
            monthname = "08";
            return monthname;
        } else if (monthname.equals("September")) {
            monthname = "09";
            return monthname;
        } else if (monthname.equals("October")) {
            monthname = "10";
            return monthname;
        } else if (monthname.equals("November")) {
            monthname = "11";
            return monthname;
        } else if (monthname.equals("December")) {
            monthname = "12";
            return monthname;
        }
        return null;
    }

    public CapturePicView(Activity context) {
        this.activity = context;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        init(context);
        this.context = context;
        month_yearPicker = new Month_YearPicker(activity);
        month_yearPicker.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("11", "010");
                // tvSelectMonth.setText(month_yearPicker.getSelectedMonthName() + "-" + month_yearPicker.getSelectedYear());
                Log.e("Reuslt:", "" + month_yearPicker.getSelectedMonthName() + " >> " + month_yearPicker.getSelectedYear());
                monthString = monthName("" + month_yearPicker.getSelectedMonthName());
                yearString = "" + month_yearPicker.getSelectedYear();
                if (cameragallery.equals("camera")) {
                    try {
                        launchCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (cameragallery.equals("gallery")) {
                    launchGallery();
                } else {

                }

            }
        }, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("00", "00");
                monthString = "";
                yearString = "";
                if (cameragallery.equals("camera")) {
                    try {
                        launchCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (cameragallery.equals("gallery")) {
                    launchGallery();
                } else {

                }


            }
        });
    }

    private static void createDialog(final Activity context) {

    }

    public static int getWidthOfScreen(Activity context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.heightPixels;
        return width;
    }

    public static int getHeightOfScreen(Activity context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        return height;
    }

    public static Bitmap fixOrientation(Bitmap mBitmap, float angle) {
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        } catch (Exception ex) {
            android.util.Log.e("fixOrientation", ex.getMessage());
        }

        return mBitmap;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Kippin_" + String.valueOf(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }

    public void addOnCloudClickListener(OnCloudClickListener onCloudClickListener) {
        this.onCloudClickListener = onCloudClickListener;
    }

    private void init(final Activity context) {
    }

    private void cameraIntent() {
        this.isCrop = isCrop;
        isChooserNeeded = false;
        File photo = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photo = createTemporaryFile("picture", ".jpg");
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("kippin", "Can't create file to take picture!");
            Toast.makeText(activity, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
        }
        photo.delete();

        if (Build.VERSION.SDK_INT >= 24) {
            mImageUri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photo);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            // Locate the ic_gallery to Share
            mImageUri = Uri.fromFile(photo);
        }


        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void show(boolean is) {
        try {
            Log.e("show HERE", "show HERE");
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dailogphotoadd);
            lDialog.show();

            Button buttonGallery = (Button) lDialog.findViewById(R.id.gallery_library);
            buttonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isCamerClicked = false;
                    if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(context,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                Utility.MY_PERMISSION_ACCESS_STORAGE);


                    } else {
                        cameragallery = "gallery";
                        //month_yearPicker.show();
                        launchGallery();
                        lDialog.dismiss();
                    }
                }
            });

            Button buttonCamera = (Button) lDialog.findViewById(R.id.camera);
            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isCamerClicked = true;
                    if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(context,
                                new String[]{Manifest.permission.CAMERA},
                                Utility.MY_PERMISSION_ACCESS_CAMERA);
                        // DIALOG CLOSE SO AFTER ACCEPTING USER AGAIN OPEN THE DIALOG

                    } else {
                        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    Utility.MY_PERMISSION_ACCESS_STORAGE);


                        } else {
                            Log.e("Resume MONTH:: ", "" + CapturePicView.monthString);
                            Log.e("Resume YEar: ", "" + CapturePicView.yearString);
                            cameragallery = "camera";
                            //month_yearPicker.show();
                            try {
                                launchCamera();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            lDialog.dismiss();
                        }

                    }

                }
            });

            Button btnCloud = (Button) lDialog.findViewById(R.id.btnCloud);
            btnCloud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monthString = "";
                    yearString = "";

                    if (onCloudClickListener != null)
                        onCloudClickListener.onClick();
                }
            });

            Button buttonCancel = (Button) lDialog.findViewById(R.id.Cancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    private File createTemporaryFile(String part, String ext) throws Exception {

        File tempDir = new File(Environment.getExternalStorageDirectory() + File.separator + Kippin.res.getString(R.string.temp_folder) + File.separator);
        tempDir.mkdirs();

        tempDir = new File(tempDir.getPath() + File.separator + part + ext);
        tempDir.createNewFile();
        return tempDir;
    }

    public void launchGallery() {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Utility.MY_PERMISSION_ACCESS_STORAGE);

        }
        // Earlier we show screen before upload
        isChooserNeeded = false;
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE_GALLERY);
    }

    public void launchCamera(boolean isCrop) {
        this.isCrop = isCrop;
        isChooserNeeded = false;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = null;
        try {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Utility.MY_PERMISSION_ACCESS_CAMERA);
                // DIALOG CLOSE SO AFTER ACCEPTING USER AGAIN OPEN THE DIALOG
            } else {
                // place where to store camera taken picture
                if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Utility.MY_PERMISSION_ACCESS_CAMERA);
                    // DIALOG CLOSE SO AFTER ACCEPTING USER AGAIN OPEN THE DIALOG
                } else {
                    photo = createTemporaryFile("picture", ".jpg");
                    photo.delete();
                    //  photo = new File(android.os.Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

                    if (Build.VERSION.SDK_INT >= 24) {
                        mImageUri = FileProvider.getUriForFile(context,
                                BuildConfig.APPLICATION_ID + ".provider",
                                photo);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        // Locate the ic_gallery to Share
                        mImageUri = Uri.fromFile(photo);
                    }
                    // mImageUri = Uri.fromFile(photo);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

                    //start camera intent
                    activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR:", "" + e);
            Log.v("kippin", "Can't create file to take picture!");
            Toast.makeText(activity, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
        }
    }

    public void launch_Camera() {
        this.isCrop = true;
        isChooserNeeded = false;
        boolean result = Utility.checkPermission(activity);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = null;
        try {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        Utility.MY_PERMISSION_ACCESS_CAMERA);
                // DIALOG CLOSE SO AFTER ACCEPTING USER AGAIN OPEN THE DIALOG
            } else {
                // place where to store camera taken picture
                photo = createImageFile();
                //photo = createTemporaryFile("picture", ".jpg");
                photo.delete();
                //photo = new File(android.os.Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                if (Build.VERSION.SDK_INT >= 24) {
                    mImageUri = FileProvider.getUriForFile(context,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photo);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    // Locate the ic_gallery to Share
                    mImageUri = Uri.fromFile(photo);
                }
                // mImageUri = Uri.fromFile(photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

                //start camera intent
                activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);

                /*    mImageUri = Uri.fromFile(photo);*/
                if (Build.VERSION.SDK_INT >= 24) {
                    mImageUri = FileProvider.getUriForFile(context,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photo);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    // Locate the ic_gallery to Share
                    mImageUri = Uri.fromFile(photo);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                //start camera intent
                activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("kippin", "Can't create file to take picture!");
            Toast.makeText(activity, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
        }
    }

    public void launchCamera() throws IOException {
        try {
            Intent pictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            if (!hasPermissions(activity, PERMISSIONS)) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL);
            } else {
                if (pictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    //Create a file to store the image
                    File photoFile = null;
                    try {
                        photoFile = create_Image_File();
                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(activity, "kippinretail.com.kippinretail_fixed.provider", photoFile);
                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                photoURI);
                        activity.startActivityForResult(pictureIntent, REQUEST_CODE_CAMERA);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR:", "" + e);
            Log.v("kippin", "Can't create file to take picture!");
            Toast.makeText(activity, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
        }

    }

    public void launch_Camera_() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            if (!hasPermissions(activity, PERMISSIONS)) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL);
            } else {
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        return;
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            mImageUri = FileProvider.getUriForFile(context,
                                    "com.kippinretail.provider",
                                    createImageFile());
                            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            // Locate the ic_gallery to Share
                            mImageUri = Uri.fromFile(createImageFile());
                        }
                        //mImageUri = Uri.fromFile(createImageFile());
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        activity.startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR:", "" + e);
            Log.v("kippin", "Can't create file to take picture!");
            Toast.makeText(activity, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
        }

    }

    private File create_Image_File() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        selectedImagePath = image.getAbsolutePath();
        return image;
    }

    public void showDialog() {
        Utility.showDialog(context);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //  mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    String selectedImagePath = null;

    public void onActivityResult(int requestCode, int resultCode, final Intent data, final CameraPicListener listener) {
        Log.e("resultCode:", "" + resultCode);
        Log.e("REQUEST_CODE_CAMERA:", "" + REQUEST_CODE_CAMERA);
        Log.e("Activity.RESULT_OK:", "" + Activity.RESULT_OK);


        this.listener = listener;
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CODE_CAMERA) {
                showDialog();
                Log.e("mImageUri Path:", "" + selectedImagePath);
                if (Build.VERSION.SDK_INT >= 24) {
//                    activity.getContentResolver().notifyChange(CapturePicView.this.mImageUri, null);
                } else {
                    //                  activity.getContentResolver().notifyChange(CapturePicView.this.mImageUri, null);
                }
//
                // selectedImagePath = new File(mImageUri.getPath()).getPath();
                //  selectedImagePath = selectedImagePath.replace("/external_files", "");
                Log.e("isCrop:", "" + isCrop);
                if (isCrop) {
                    // performCrop(mImageUri);
                    useBitMap(selectedImagePath);
                    try {
                        Bitmap bm = null;
                        bm = MediaStore.Images.Media.getBitmap(
                                activity.getContentResolver(), mImageUri);
                        ExifInterface exifInterface = new ExifInterface(mImageUri.getPath());
                        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);
                        float angle = 360;
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                angle = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                angle = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                angle = 270;
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default:
                                break;
                        }
                        Bitmap oriented = fixOrientation(bm, angle);
                        Uri u = getImageUri(activity, oriented);
                        UCrop.Options options = new UCrop.Options();
                        options.setToolbarColor(Color.parseColor("#ffffff"));

                        if (Utility.FinanceLogin) {
                            options.setToolbarTitle("");
                        } else {
                            options.setToolbarTitle("Merchants List");
                        }
                        options.setToolbarWidgetColor(Color.parseColor("#000000"));
                        UCrop.of(u, Uri.parse(getRealPathFromURI(mImageUri)))
                                .withOptions(options)
                                .withAspectRatio(4, 3)
                                .withMaxResultSize(getWidthOfScreen(activity), getHeightOfScreen(activity))
                                .start(activity);

                    } catch (Exception ex) {

                    }

                } else {
                    // Bitmap of camera for dashboard
                    try {
                        //Bitmap b=getThumbnail(mImageUri);
                        //Log.e("b:", "" + b);
                        // Uri u = getImageUri(activity, b);
                        // Log.e("u:", "" + u.getPath());
                        // selectedImagePath=new File(u .getPath()).getPath();
                        // Log.e("BMb:", "" + selectedImagePath);
                        // listener.onImageSelected(b);

                        Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);
                        Log.e("Final BM:", "" + bm);
                        ///selectedImagePath
                        //useBitMap(u.getPath());
                        useBitMap(selectedImagePath);
                    } catch (Exception ex) {

                    }
                }

            } else if (requestCode == REQUEST_CODE_GALLERY) {
                showDialog();
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(activity, selectedImageUri, projection, null, null,
                        null);
                final Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                selectedImagePath = cursor.getString(column_index);

                if (selectedImagePath == null) {
                    Utility.dismissDialog();
                    new DialogBox(activity, "App not Supported", (DialogBoxListener) null);
                    return;
                } else {
                    File galleryFile = new File(selectedImagePath);
                    //     performCrop(Uri.fromFile(galleryFile));
                    useBitMap(selectedImagePath);
                  /*  try {
                        Uri u = Uri.fromFile(galleryFile);
                        UCrop.Options options = new UCrop.Options();
                        options.setToolbarColor(Color.parseColor("#ffffff"));
                        if (Utility.FinanceLogin) {
                            options.setToolbarTitle("");
                        } else {
                            options.setToolbarTitle("Merchants List");
                        }
                        options.setToolbarWidgetColor(Color.parseColor("#000000"));
                        UCrop.of(u, u)
                                .withOptions(options)
                                .withAspectRatio(16, 9)
                                .withMaxResultSize(getWidthOfScreen(activity), getHeightOfScreen(activity))
                                .start(activity);


                    } catch (Exception ex) {

                    }
*/
                }

            } else if (requestCode == UCrop.REQUEST_CROP) {
                Uri resultUri = UCrop.getOutput(data);
                useBitMap(resultUri.getPath());
            }
        }


    }

    private void useBitMap(final String imagepath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bm = null;

                if (imagepath == null) {
                    errorMessage = "Image not valid";
                    bm = null;
                } else {

                    bm = Utility.resizeIfneeded(new File(imagepath));

                }
                Log.e("isChooserNeeded:", "" + isChooserNeeded);
                if (isChooserNeeded) {
                    Utility.cameraPicListener = listener;
                    Bundle bundle = new Bundle();
                    bundle.putString(Activity_Photo_Preview.OPERATION, Activity_Photo_Preview.OPERATION_PHOTOPIC);
                    Utility.bitmap = bm;
                    Utility.startActivity(context, Activity_Photo_Preview.class, bundle, false);
                } else {
                    //
                    try {

                        ExifInterface exifInterface = new ExifInterface(imagepath);
                        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);
                        float angle = 360;
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                angle = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                angle = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                angle = 270;
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default:
                                break;
                        }
                        Bitmap oriented = Utility.fixOrientation(bm, angle);
                        listener.onImageSelected(oriented);
                       /* Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                        int rotation = display.getRotation();
                        if(rotation == Configuration.ORIENTATION_LANDSCAPE){
                       //     Bitmap oriented = Utility.fixOrientationInlandcape(bm);
                            listener.onImageSelected(bm);
                        }else{

                            Bitmap oriented = Utility.fixOrientationInPortrait(bm);
                            listener.onImageSelected(oriented);
                        }*/

                    } catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }

                }
                dismissDialog();
            }
        }).start();
    }

    private void dismissDialog() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Utility.dismissDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Bitmap scaleBitmap(String selectedImagePath) {
        Bitmap bm;
//        final int REQUIRED_SIZE = 300;
        int scale = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();

        while (options.outWidth / scale / 2 >= 500
                && options.outHeight / scale / 2 >= 1000)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;

        options.outHeight = 1280;
        options.outWidth = 720;

        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        return bm;
    }

    public void uploadImageForStatement(final Activity activity, final Bitmap bm, final ModelBankStatement modelBankStatement, final TemplateStatementImageUploader templateStatementImageUploader/*final OnResultListener onResultListener*/) {
        {
            if (bm == null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new DialogBox(activity, Kippin.kippin.getResources().getString(R.string.image_size_too_low), (DialogBoxListener) null);
                    }
                });
            } else {

                try {

                    final OnResultListener onResultListener = templateStatementImageUploader.onResultListener;
                    String image = Utility.convertBitmap2Base64(bm);
                    // String image = Utility.convertBitmap2Base64(bm);

                    final WSTemplate wsTemplate = new WSTemplate();

                    ArrayListPost templatePosts = new ArrayListPost();
                    templatePosts.add("Image", image);
                    templatePosts.add("ImageName", System.currentTimeMillis() + ".png");
                    templatePosts.add("BankId", modelBankStatement.getBankId());
                    templatePosts.add("UserId", Singleton.getUser().getId());
                    templatePosts.add("StatementId", modelBankStatement.getId());
                    if (templateStatementImageUploader.isCloudImage) {
                        templatePosts.add("CloudImageName", templateStatementImageUploader.CloudImageName);
                        templatePosts.add("IsCloud", templateStatementImageUploader.isCloudImage + "");
                    } else {
                        templatePosts.add("CloudImageName", "");
                        templatePosts.add("IsCloud", "false");
                    }
                    if (templateStatementImageUploader.month != null) {
                        if (templateStatementImageUploader.month.equals("")) {

                        } else {
                            templatePosts.add("Month", templateStatementImageUploader.month + "");
                            templatePosts.add("Year", templateStatementImageUploader.year + "");
                        }
                    }


                    templatePosts.add("ImagePath", templateStatementImageUploader.CloudImagePath);
                    Log.e("image:", "" + image);
                    Log.e("BankId:", "" + modelBankStatement.getBankId());
                    Log.e("UserId:", "" + Singleton.getUser().getId());
                    Log.e("StatementId:", "" + modelBankStatement.getId());

                    wsTemplate.templatePosts = templatePosts;

                    wsTemplate.wsInterface = new WSInterface() {
                        @Override
                        public void onResult(int requestCode, final TemplateData data) {

                            String responseCode = "1";

                            if (!data.getData(ModelImagePost.class).getResponseCode().equalsIgnoreCase("1")) {

                                new DialogBox(activity, data.getData(ModelImagePost.class).getResponseMessage(), new DialogBoxListener() {
                                    @Override
                                    public void onDialogOkPressed() {
                                        if (onResultListener != null)
                                            onResultListener.openOCR(data, bm, true);

                                    }
                                });

                            } else {
                                if (onResultListener != null)
                                    onResultListener.openOCR(data, bm, true);
                            }

                        }
                    };

                    wsTemplate.url = Url.URL_BANK_STATEMENT_IMAGE;
                    Log.e("URL_URL:", "" + Url.URL_BANK_STATEMENT_IMAGE);
                    wsTemplate.aClass = ModelImagePost.class;
                    wsTemplate.isFormData = false;
                    wsTemplate.context = activity;
                    wsTemplate.methods = WSMethods.POST;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            new WSHandler(wsTemplate).execute();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Error e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void performCrop(Uri u) {
        try {
            Utility.dismissDialog();
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri

            cropIntent.setDataAndType(u, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, u);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            activity.startActivityForResult(cropIntent, PIC_CROP);
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        } catch (Error ex) {
            Log.e("", ex.getMessage());
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, Activity currentActivity) {
        try {
            switch (requestCode) {
                case Utility.MY_PERMISSION_ACCESS_CAMERA:
                    if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    Utility.MY_PERMISSION_ACCESS_STORAGE);


                        } else {
                            cameragallery = "gallery";
                            if (ActivityCashEntry.cashentry == 1) {
                                launchCamera();
                            } else {
                                month_yearPicker.show();
                            }
                            //

                            lDialog.dismiss();
                        }

                    } else {
                        Toast.makeText(currentActivity, "This is a required for the app to function", Toast.LENGTH_LONG).show();
                        lDialog.dismiss();

                    }
                    break;
                case Utility.MY_PERMISSION_ACCESS_STORAGE:
                    if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (isCamerClicked) {
                            isCamerClicked = false;
                            cameragallery = "camera";
                            // month_yearPicker.show();
                            if (ActivityCashEntry.cashentry == 1) {
                                launchCamera();
                            } else {
                                month_yearPicker.show();
                            }
                            //launchCamera();
                            lDialog.dismiss();
                        } else {
                            cameragallery = "gallery";
                            //month_yearPicker.show();
                            if (ActivityCashEntry.cashentry == 1) {
                                launchGallery();
                            } else {
                                month_yearPicker.show();
                            }

                            lDialog.dismiss();
                        }

                    } else {
                        Toast.makeText(currentActivity, "This is a required for the app to function", Toast.LENGTH_LONG).show();
                        lDialog.dismiss();

                    }
                    break;
            }
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        //double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 3;
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }
}
