package com.kippinretail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kippinretail.ApplicationuUlity.CommonUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class CameraActivity extends Activity implements Callback,View.OnClickListener{

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Button flipCamera;
    private Button flashCameraButton;
    private Button captureImage;
    private int cameraId;
    private boolean flashmode = false;
    private int rotation;
    int displayOrientation;
    private boolean isBackcamera = true;
    private String imageName;
    private Uri imageUri = null;
    RelativeLayout bottomLayout;
    LinearLayout bottomLayout2;
    private Bitmap loadedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initilization();
    }

    private void initilization(){

        cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        flipCamera = (Button) findViewById(R.id.flipCamera);
        flashCameraButton = (Button) findViewById(R.id.flash);
        captureImage = (Button)findViewById(R.id.captureImage);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        flipCamera.setOnClickListener(this);
        captureImage.setOnClickListener(this);
        flashCameraButton.setOnClickListener(this);
        bottomLayout = (RelativeLayout)findViewById(R.id.bottomLayout);
        bottomLayout2 = (LinearLayout)findViewById(R.id.bottomLayout2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("Inside Surface Created", "Inside Surface Created");
        try {
            int cameraId = backCamera();
            if (cameraId != -1) {
                if (!openCamera(cameraId)) {
                    alertCameraDialog();
                }
            } else {

                Log.e("Error In Back Camera", "No Back camera");

            }

        }catch(Exception ex){

        }

    }

    private int backCamera()
    {
        int camerId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for(int i=0;i<numberOfCameras;i++)
        {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i,info);
            if(info.facing== Camera.CameraInfo.CAMERA_FACING_BACK)
            {
                camerId = i;
                break;
            }

        }
        return camerId;
    }

    private int frontCamera()
    {
        int camerId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for(int i=0;i<numberOfCameras;i++)
        {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i,info);
            if(info.facing== Camera.CameraInfo.CAMERA_FACING_FRONT)
            {
                camerId = i;
                break;
            }

        }
        return camerId;
    }
    private boolean openCamera(int id) {
        boolean result = false;
        cameraId = id;
        releaseCamera();
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (camera != null) {
            try {
                setUpCamera(camera);
                camera.setErrorCallback(new Camera.ErrorCallback() {

                    @Override
                    public void onError(int error, Camera camera) {

                    }
                });
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
                releaseCamera();
            }
        }
        return result;
    }

    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }
        if(info.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
        {
            displayOrientation = (info.orientation-degree+360)%360;
            Log.e("info.orientation ",""+info.orientation);
            Log.e("degree ",""+degree);
            Log.e("displayOrientation ",""+displayOrientation);

        }
        else
        {
            displayOrientation = (info.orientation+degree)%360;
            displayOrientation = (360 - displayOrientation) % 360;
            Log.e("info.orientation ",""+info.orientation);
            Log.e("degree ",""+degree);
            Log.e("displayOrientation ",""+displayOrientation);
        }
        camera.setDisplayOrientation(displayOrientation);
    }

    private void showFlashButton(Camera.Parameters params) {
        boolean showFlash = (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH) && params.getFlashMode() != null)
                && params.getSupportedFlashModes() != null
                && params.getSupportedFocusModes().size() > 1;

//        flashCameraButton.setVisibility(showFlash ? View.VISIBLE
//                : View.INVISIBLE);

    }
    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.setErrorCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
            camera = null;
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flash:
                flashOnButton();
                break;
            case R.id.flipCamera:
                flipCamera();
                break;
            case R.id.captureImage:
                takeImage();
                break;

            default:
                break;
        }
    }

    private void takeImage()
    {
        int i = 0;
        if(camera!=null)
        {
            camera.takePicture(null, null, new Camera.PictureCallback() {

                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    try {


                        loadedImage = null;

                        Bitmap rotatedBitmap = null;
                        loadedImage = BitmapFactory.decodeByteArray(data, 0,
                                data.length);
                        imageUri = getImageUri(CameraActivity.this, loadedImage);
                       /* ExifInterface exifInterface = new ExifInterface(imagePath);
                        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION ,
                                ExifInterface.ORIENTATION_UNDEFINED);
                        float angle = 360;
                        switch(orientation) {
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
                        }*/

                        bottomLayout.setVisibility(View.GONE);
                        bottomLayout2.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    public String getRealPathFromURI(Uri uri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, String.valueOf(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }

    private void flipCamera() {
        int cameraId = -1;
        if(isBackcamera) {
            cameraId = frontCamera();
            isBackcamera = false;

        }
        else
        {
            cameraId = backCamera();
            isBackcamera = true;
        }
        if (cameraId != -1) {
            if (!openCamera(cameraId)) {
                alertCameraDialog();
            }
        } else {
            Log.e("Error To Open camera", "Error To Open camera");

        }
    }

    private void alertCameraDialog() {
        AlertDialog.Builder dialog = createAlert(CameraActivity.this,
                "Camera info", "error to open camera");
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        dialog.show();
    }

    private AlertDialog.Builder createAlert(Context context, String title, String message) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(context,
                        android.R.style.Theme_Holo_Light_Dialog));
        dialog.setIcon(R.mipmap.ic_launcher);
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("Information");
        dialog.setMessage(message);
        dialog.setCancelable(false);
        return dialog;

    }


    private void flashOnButton() {
        if (camera != null) {

            if(!flashmode)
            {
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                camera.startPreview();
                flashmode = true;
                flashCameraButton.setBackgroundResource(R.drawable.flash_off);
            }
            else
            {
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.stopPreview();
                flashmode = false;
                flashCameraButton.setBackgroundResource(R.drawable.flash_on);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    public void retake(View v){
        try {
            bottomLayout.setVisibility(View.VISIBLE);
            bottomLayout2.setVisibility(View.GONE);
            int cameraId = backCamera();
            if (cameraId != -1) {
                if (!openCamera(cameraId)) {
                    alertCameraDialog();
                }
            } else {

                Log.e("Error In Back Camera", "No Back camera");

            }

        }catch(Exception ex){

        }

    }
    public void usePhoto(View v){
        try {
            Intent i = new Intent();
            i.putExtra("image", imageUri.toString());
            setResult(RESULT_OK, i);
            this.finish();
        }catch(Exception ex){
            Toast.makeText(this,"finish",Toast.LENGTH_LONG).show();
        }
    }
}


