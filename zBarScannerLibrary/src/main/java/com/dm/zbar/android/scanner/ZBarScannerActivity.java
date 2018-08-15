package com.dm.zbar.android.scanner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

public class ZBarScannerActivity extends Activity implements Camera.PreviewCallback, ZBarConstants {

    private static final String TAG = "ZBarScannerActivity";
    private CameraPreview mPreview;
    private Camera mCamera;
    private ImageScanner mScanner;
    private Handler mAutoFocusHandler;
    private boolean mPreviewing = true;
    TextView txt_message;

    static {
        try {
            Log.e("=========","Static call Library Loaded");

            System.loadLibrary("iconv");
        }catch(Exception ex){
            Log.e("===error======","Static call Library Loaded");
        }
        catch(Error ex){

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isCameraAvailable()) {
            // Cancel request if there is no rear-facing camera.
            cancelRequest();
            return;
        }

        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAutoFocusHandler = new Handler();

        // Create and configure the ImageScanner;
        setupScanner();

        // Create a RelativeLayout container that will hold a SurfaceView,
        // and set it as the content of our activity.

        setContentView(R.layout.camera_view);
        mPreview = (CameraPreview)findViewById(R.id.surfaceView);
        txt_message = (TextView)findViewById(R.id.txt_message);
        mPreview.setPreviewCallback(this,this,autoFocusCB);
        updateUI();
      /*  mPreview = new CameraPreview(this, this, autoFocusCB);
        setContentView(mPreview);*/
    }


   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {


            if(!isCameraAvailable()) {
                // Cancel request if there is no rear-facing camera.
                cancelRequest();
                return;
            }
         //   requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mAutoFocusHandler = new Handler();
            setupScanner();
            setContentView(R.layout.camera_view);
            mPreview = (CameraPreview)findViewById(R.id.surfaceView);
            txt_message = (TextView)findViewById(R.id.txt_message);
            mPreview.setPreviewCallback(this,this,autoFocusCB);
            updateUI();

        }if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            if(!isCameraAvailable()) {
                // Cancel request if there is no rear-facing camera.
                cancelRequest();
                return;
            }
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mAutoFocusHandler = new Handler();
            setupScanner();
            setContentView(R.layout.camera_view);
            mPreview = (CameraPreview)findViewById(R.id.surfaceView);
            txt_message = (TextView)findViewById(R.id.txt_message);
            mPreview.setPreviewCallback(this,this,autoFocusCB);
            updateUI();

        }
    }*/

    private void updateUI(){
        if(getIntent().getStringExtra("txt_message")!=null){
            txt_message.setText(getIntent().getStringExtra("txt_message"));
        }
    }

    public void setupScanner() {
        try {
            mScanner = new ImageScanner();
            mScanner.setConfig(0, Config.X_DENSITY, 3);
            mScanner.setConfig(0, Config.Y_DENSITY, 3);

            int[] symbols = getIntent().getIntArrayExtra(SCAN_MODES);
            if (symbols != null) {
                mScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
                for (int symbol : symbols) {
                    mScanner.setConfig(symbol, Config.ENABLE, 1);
                }
            }
        }catch(Exception ex){

        }
        catch(Error ex ){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open the default i.e. the first rear facing camera.
        try {
            mCamera = Camera.open();
            if (mCamera == null) {
                // Cancel request if mCamera is null.
                cancelRequest();
                return;
            }

            mPreview.setCamera(mCamera);
            mPreview.showSurfaceView();

            mPreviewing = true;
        }catch(Exception ex){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            mPreview.setCamera(null);
            mCamera.cancelAutoFocus();
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();

            // According to Jason Kuang on http://stackoverflow.com/questions/6519120/how-to-recover-camera-preview-from-sleep,
            // there might be surface recreation problems when the device goes to sleep. So lets just hide it and
            // recreate on resume
            mPreview.hideSurfaceView();

            mPreviewing = false;
            mCamera = null;
        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void cancelRequest() {
        Intent dataIntent = new Intent();
        dataIntent.putExtra(ERROR_INFO, "Camera unavailable");
        setResult(Activity.RESULT_CANCELED, dataIntent);
        finish();
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        try {
            Runtime.getRuntime().loadLibrary("iconv");
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = mScanner.scanImage(barcode);

            if (result != 0) {
                mCamera.cancelAutoFocus();
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mPreviewing = false;
                SymbolSet syms = mScanner.getResults();
                for (Symbol sym : syms) {
                    String symData = sym.getData();
                    if (!TextUtils.isEmpty(symData)) {
                        Intent dataIntent = new Intent();
                        dataIntent.putExtra(SCAN_RESULT, symData);
                        dataIntent.putExtra(SCAN_RESULT_TYPE, sym.getType());

                        setResult(Activity.RESULT_OK, dataIntent);
                        finish();
                        break;
                    }
                }
            }
        }catch(Exception ex){
            Log.e("Exception",ex.getMessage());
        }
        catch(Error ex){
            Log.e("Error",ex.getMessage());
        }



    }
    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if(mCamera != null && mPreviewing) {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            mAutoFocusHandler.postDelayed(doAutoFocus, 4000);
        }
    };

    public void btn_mannualInput(View v){
        Intent dataIntent = new Intent();
        dataIntent.putExtra("clickedButton", "manualInput");
        setResult(Activity.RESULT_CANCELED, dataIntent);
        finish();
        //overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }
    public void closeBtn(View v){
        Intent dataIntent = new Intent();
        dataIntent.putExtra("clickedButton", "close");
        setResult(Activity.RESULT_CANCELED, dataIntent);
        finish();

      //  overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }
    boolean isFlashOn = false;
    public void flashOn(View v){
        try{
            // mCamera = Camera.open();

            Camera.Parameters p = mCamera.getParameters();
            if(!isFlashOn){

                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(p);
                mCamera.startPreview();
                isFlashOn = true;
            }else{
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(p);
                isFlashOn = false;
            }
        }catch(Exception ex){
            Log.e("Message",ex.getMessage());
        }
    }


}
