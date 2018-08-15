package com.kippinretail.ApplicationuUlity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.dm.zbar.android.scanner.HandleManualInput;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.kippinretail.Interface.OnBarcodeGetListener;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.R;
import com.kippinretail.app.Retail;

import net.sourceforge.zbar.Symbol;

/**
 * Created by gaganpreet.singh on 4/6/2016.
 */
public class BarcodeUtil {


    private static final int BLACK = Color.BLACK;
    private static final int WHITE = Color.WHITE;

    private static  int HEIGHT = -1;
    private static  int WIDTH = -1;

    private static final int ZBAR_SCANNER_REQUEST = 11;
    private static final int ZBAR_QR_SCANNER_REQUEST = 12;

    private int requestCode=-1;

    Activity activity;
    View v;

    public BarcodeUtil(Activity activity ) {
        this.activity = activity;

        int width = (int) Retail.res.getDimension(R.dimen.gift_template_width);
        int height = (int) Retail.res.getDimension(R.dimen.gift_card_height);

        HEIGHT = Utility.dpToPx(height) ;
        WIDTH = Utility.dpToPx(width) ;
    }
    public BarcodeUtil(Activity activity, View v) {
        this.activity = activity;
        this.v = v;

        int width = (int) Retail.res.getDimension(R.dimen.gift_template_width);
        int height = (int) Retail.res.getDimension(R.dimen.gift_card_height);

        HEIGHT = Utility.dpToPx(height) ;
        WIDTH = Utility.dpToPx(width) ;
    }

    public void launchScanner(int requestCode , HandleManualInput obj,String message) {
        try{
            this.requestCode=-1;
            if (isCameraAvailable()) {
                Intent intent = new Intent(activity, ZBarScannerActivity.class);

                intent.putExtra("txt_message",message);
                intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.CODE128,Symbol.CODABAR,Symbol.CODE39,Symbol.CODE93,Symbol.DATABAR,Symbol.DATABAR_EXP,Symbol.EAN13
                        ,Symbol.EAN8,Symbol.I25,Symbol.ISBN10,Symbol.ISBN13,Symbol.PARTIAL,Symbol.PDF417,Symbol.QRCODE,Symbol.UPCA,Symbol.UPCE});
            //    intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.CODE128});
                activity.startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
                activity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                this.requestCode = requestCode;
            } else {
                Toast.makeText(activity, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception ex){
            Log.e("===== 85 =========",ex.getMessage());
        }

    }
    public void launchScanner(int requestCode , HandleManualInput obj) {
        try{
            this.requestCode=-1;
            if (isCameraAvailable()) {
                Intent intent = new Intent(activity, ZBarScannerActivity.class);
                intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.CODE128,Symbol.CODABAR,Symbol.CODE39,Symbol.CODE93,Symbol.DATABAR,Symbol.DATABAR_EXP,Symbol.EAN13
                        ,Symbol.EAN8,Symbol.I25,Symbol.ISBN10,Symbol.ISBN13,Symbol.PARTIAL,Symbol.PDF417,Symbol.QRCODE,Symbol.UPCA,Symbol.UPCE});
              //  intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.CODE128});
                activity.startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
                activity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                this.requestCode = requestCode;
            } else {
                Toast.makeText(activity, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception ex){
            Log.e("===== 85 =========",ex.getMessage());
        }

    }

    public void launchQRScanner() {

        if (isCameraAvailable()) {
            Intent intent = new Intent(activity, ZBarScannerActivity.class);
            intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.CODE128,Symbol.CODABAR,Symbol.CODE39,Symbol.CODE93,Symbol.DATABAR,Symbol.DATABAR_EXP,Symbol.EAN13
                    ,Symbol.EAN8,Symbol.I25,Symbol.ISBN10,Symbol.ISBN13,Symbol.PARTIAL,Symbol.PDF417,Symbol.QRCODE,Symbol.UPCA,Symbol.UPCE});
          //  intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.CODE128});
            activity.startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
            activity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        } else {
            Toast.makeText(activity, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }


    public Drawable generateBarcode(String data) {

        //Find screen size
        WindowManager manager = (WindowManager)activity. getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;

        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        BitMatrix result = null;
        try {
            result = multiFormatWriter.encode(data, BarcodeFormat.CODE_128, width, height);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
        return new BitmapDrawable(bitmap);
    }

    public boolean isCameraAvailable() {
        PackageManager pm = activity.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data , OnBarcodeGetListener onBarcodeGetListener) {
        switch (requestCode) {
            case ZBAR_SCANNER_REQUEST:
            case ZBAR_QR_SCANNER_REQUEST:
                 if (resultCode == Activity.RESULT_OK) {
//                    Toast.makeText(activity, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
                    onBarcodeGetListener.getBarcode(this.requestCode ,data.getStringExtra(ZBarConstants.SCAN_RESULT));
                } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
                    String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
                    Log.e("error" , error);
                    onBarcodeGetListener.getBarcode(this.requestCode ,null);
//                    if (!TextUtils.isEmpty(error)) {
//                        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
//                    }
                }
                break;
        }
    }

}
