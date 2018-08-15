package com.kippinretail.CommonDialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.kippin.app.Kippin;
import com.kippin.utils.Utility;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.GalleryActivity;
import com.kippinretail.ImagePickUtility.CapturePicView;
import com.kippinretail.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sandeep.saini on 4/12/2016.
 */
public class UploadImageDialog {
    private static Dialog lDialog;
    private static boolean isShowing = false;
    static CapturePicView capturePicView = null;
    ;
    static Activity _context;
    public static Uri mImageUri;

    public static void showUploadImageDialog(final Activity context, final Context cntxt) {
        _context = context;
        try {
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_choose_photo);
            lDialog.show();

            Button buttonGallery = (Button) lDialog.findViewById(R.id.gallery_library);
            buttonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (ContextCompat.checkSelfPermission(cntxt, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    CommonUtility.MY_PERMISSION_ACCESS_STORAGE);

                        } else {
                            context.startActivityForResult(new Intent(context, GalleryActivity.class), CommonUtility.REQUEST_CODE_GALLERY);
                            context.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                            lDialog.dismiss();
                        }

                    } catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }

                }
            });


            Button buttonCamera = (Button) lDialog.findViewById(R.id.camera);
            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File photo = null;
                    try {
                        //
                        if (ContextCompat.checkSelfPermission(cntxt, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(context,
                                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    CommonUtility.MY_PERMISSION_ACCESS_CAMERA);
                            // DIALOG CLOSE SO AFTER ACCEPTING USER AGAIN OPEN THE DIALOG
                        } else {
                            photo = createImageFile(context);
                            // photo.delete();
                            if (Build.VERSION.SDK_INT >= 24) {
                                mImageUri = FileProvider.getUriForFile(context,
                                        "kippinretail.com.kippinretail_fixed.provider",
                                        photo);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else {
                                // Locate the ic_gallery to Share
                                mImageUri = Uri.fromFile(photo);
                            }

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                            context.startActivityForResult(intent, CommonUtility.REQUEST_CODE_TAKE_PICTURE);
                            context.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                            lDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v("kippin", "Can't create file to take picture!");
                        Toast.makeText(context, "Please check SD card!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            Button buttonCancel = (Button) lDialog.findViewById(R.id.Cancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            Log.e("", ex.getMessage()


            );
        }
    }
    public static File createImageFile(Context context) throws IOException {
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
        // mImageUri=Uri.fromFile(new File(image.getAbsolutePath()));
        // selectedImagePath = image.getAbsolutePath();
        return image;
    }

    public static void showUploadImageDialog(final Activity context) {
        try {
            lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_choose_photo);
            lDialog.show();

            Button buttonGallery = (Button) lDialog.findViewById(R.id.gallery_library);
            buttonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivityForResult(new Intent(context, GalleryActivity.class), CommonUtility.REQUEST_CODE_GALLERY);
                    context.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                    lDialog.dismiss();
                }
            });

            Button buttonCamera = (Button) lDialog.findViewById(R.id.camera);
            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

                    File photo = null;
                    try {

                        context.startActivityForResult(intent, CommonUtility.REQUEST_CODE_TAKE_PICTURE);
                        context.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        lDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v("kippin", "Can't create file to take picture!");
                        Toast.makeText(context, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            Button buttonCancel = (Button) lDialog.findViewById(R.id.Cancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public static void dismissUploadImageDialog() {
        if (isShowing) {

        }

    }


    public static void cropPic(Activity activity, Uri imageUri) {
        Utility.dismissDialog();
        //call the standard crop action intent (the user device may not support it)
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri

        cropIntent.setDataAndType(imageUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        activity.startActivityForResult(cropIntent, CommonUtility.CROP_PIC);
    }

    private static File createTemporaryFile(String part, String ext) throws Exception {

        File tempDir = new File(Environment.getExternalStorageDirectory() + File.separator + Kippin.res.getString(com.kippin.kippin.R.string.temp_folder) + File.separator);
        tempDir.mkdirs();

        tempDir = new File(tempDir.getPath() + File.separator + part + ext);
        tempDir.createNewFile();
        return tempDir;
    }


    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            switch (requestCode) {
                case CommonUtility.MY_PERMISSION_ACCESS_CAMERA:
                    if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        _context.startActivityForResult(intent, CommonUtility.REQUEST_CODE_TAKE_PICTURE);
                        _context.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        lDialog.dismiss();
                    } else {
                        Toast.makeText(_context, "This is a required for the app to function", Toast.LENGTH_LONG).show();
                        lDialog.dismiss();
                    }
                    break;
                case CommonUtility.MY_PERMISSION_ACCESS_STORAGE:
                    if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        _context.startActivityForResult(new Intent(_context, GalleryActivity.class), CommonUtility.REQUEST_CODE_GALLERY);
                        _context.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        lDialog.dismiss();
                    } else {
                        Toast.makeText(_context, "This is a required for the app to function", Toast.LENGTH_LONG).show();
                        lDialog.dismiss();
                    }
                    break;
            }
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
    }
}
