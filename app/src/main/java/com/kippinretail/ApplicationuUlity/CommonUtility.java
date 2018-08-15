package com.kippinretail.ApplicationuUlity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.CommonDialog.PromtListener;
import com.kippinretail.LoginActivity;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.R;
import com.kippinretail.RegistrationActivity;
import com.kippinretail.UserDashBoardActivity;
import com.kippinretail.app.Retail;
import com.kippinretail.sharedpreferences.Prefs;
import com.kippin.app.Kippin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import static com.kippin.utils.Utility.convertBitmap2ByteArray;

/**
 * Created by Sony on 24-08-2015.
 */
public class CommonUtility {
    public static final int CROP_PIC = 700;
    public static final int REQUEST_LOCATION = 101;
    // REQUEST CODE FOR PERMISSION DURING RUN TIME
    public static final int MY_PERMISSION_ACCESS_CAMERA = 301;
    public static final int MY_PERMISSION_ACCESS_STORAGE = 302;
    public static final int MY_PERMISSION_ACCESS_LOCATION = 303;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 305;
    public static final int REQUEST_MANUAL_BARCODE = 500;
    public static final int REQUEST_CODE_TAKEMANUAL_INPUT = 600;
    public static final int REQUEST_CODE_GALLERY = 100;
    public static final int REQUEST_CODE_TAKE_PICTURE = 200;
    public static final int REQUEST_CODE_BARCODE = 0;
    public static final int REQUEST_CODE_LOYALITY = 1;
    public static final int PIC_CROP = 300;
    public static String INVOICE_TYPE = "";
    public static String IsFinance;
    public static boolean isInvoicedashBoard = false;
    public static Merchant DataDetail;
    public static String GiftCardDetails;
    public static String BarCodeValue;
    public static String UserType = "";
    public static String[] requiredPermission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    public static boolean aboutsUsClicked, contactusClicked, giftcardClicked, layaltycardClicked, voucherclicked, dashboard;
    public static boolean isMoveForward;
    public static boolean isUserDashBoard = false;
    public static boolean isMerchantdashBoard = false;
    public static boolean isCharityDashBoard = false;
    public static String failureMsg = "Unable to connect to server, please try again later";
    public static Bitmap bitmapToCrop = null;
    public static Bitmap cropedBitmap = null;
    public static String Sent_Receiver;
    public static String TIME_OUT_MESSAGE = "Unable to connect to server, please try again later";
    public static Typeface openSansRegular, openSansBold;
    public static ProgressDialog mProgressDialog;
    public static String TemplateIdValue = "";
    public static String TemplateImagePath = "";
    public static Bitmap VoucherBitMap;
    public static String IMAGE_BASEURL = "http://development.web1.anzleads.com/";
    public static String base64Image;
    public static String merchantRoleId = "1";
    public static String userRoleId = "2";
    public static String charityRoleId = "3";
    public static boolean isLogoutClicked;
    public static String friendId;
    public static boolean haveConnectedWifi = false;
    public static boolean haveConnectedMobile = false;
    public static boolean haveConnectedActive = false;
    public static ConnectivityManager connMgr;
    public static NetworkInfo wifi;
    public static NetworkInfo mobile;
    public static String InvoiceTitle = "";
    public static String NonKippinGiftCardDatra;
    public static boolean HomeButtonInsideView;
    private static boolean isPerformAutoLogout = true;
    private static Dialog dialog;
    private String userCountry;

    public static boolean isPerformAutoLogout() {
        return isPerformAutoLogout;
    }

    public static void setIsPerformAutoLogout(boolean isPerformAutoLogout) {
        CommonUtility.isPerformAutoLogout = isPerformAutoLogout;
    }

    // Convert Bitmap into Base64

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
    public static File bitmapToUriConverter(Context context, Bitmap mBitmap) {
        File file = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            options.inSampleSize = 5;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    true);

            String timeStamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());
            String imageFileName = "IMG_" + timeStamp + "_";
            File storageDir =
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            file = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );


            FileOutputStream out = context.openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();


        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return file;
    }

    public static Bitmap fixOrientation1(Bitmap mBitmap, float angle) {
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
            mBitmap.recycle();
        } catch (Exception ex) {
            android.util.Log.e("fixOrientation", ex.getMessage());
        }

        return mBitmap;
    }

    public static void HomeClick(final Activity context) {
        if (CommonUtility.UserType.equals("0")) {
            Intent in = new Intent();
            if (CommonData.getInvoiceUserData(context) == null) {
                in.setClass(context, Kippin.invoiceDashBoardClass);
                in.putExtra("RegistrationType", "0");
                in.putExtra("InvoiceUserType", "true");
                context.startActivity(in);
                context.overridePendingTransition(com.kippin.kippin.R.anim.push_in_to_left,
                        com.kippin.kippin.R.anim.push_in_to_right);
            } else {
                //Intent in = new Intent();
                in.setClass(context, Kippin._FinanceDashBoard);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                in.putExtra("RegistrationType", "0");
                in.putExtra("InvoiceUserType", "true");
                context.startActivity(in);
                context.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
            }


        } else {
            CommonUtility.HomeButtonInsideView = true;
            CommonUtility.UserType = "2";
            Intent in = new Intent();
            in.setClass(context, UserDashBoardActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.putExtra("InvoiceUserType", "true");
            context.startActivity(in);
            context.finish();
            context.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
        }
    }

    public static void font(Context c) {

        //openSansRegular = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
        //openSansBold = Typeface.createFromAsset(c.getAssets(), "OpenSans-Bold.ttf");

    }

    public static String encodeTobase64(Bitmap bm) throws Exception {
        //Base64.decode()
        return Base64.encodeToString(convertBitmap2ByteArray(bm), Base64.DEFAULT);
    }

    /*public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        String imageEncoded = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (immagex != null) {
            immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        }


        // Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }*/

    // retun currentDate
    public static String getCurrentDate() {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String getAndroidKey() {
        return Retail.res.getString(R.string.server_google_map);
    }

    public static void startNewActivity(Activity context, Class target) {
        Intent i = new Intent(context, target);
        context.startActivity(i);
        context.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    public static void refreshGridView(Activity context, GridView gridView) {

        int gridViewEntrySize = context.getResources().getDimensionPixelSize(R.dimen.grip_view_entry_size);
        int gridViewSpacing = context.getResources().getDimensionPixelSize(R.dimen.grip_view_spacing);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int numColumns = (display.getWidth() - gridViewSpacing) / (gridViewEntrySize + gridViewSpacing);

        gridView.setNumColumns(numColumns);
    }

    public static void moveToTarget(Activity context, Class taget) {
        Intent in = new Intent();
        in.setClass(context, taget);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(in);
        context.finish();
        context.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }

    public static void CallLogout(final Activity context) {

        dialog = new Dialog(context,
                R.style.Theme_AppCompat_Translucent);
        dialog.setContentView(R.layout.dialog_logout_);
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView textForMessage = (TextView) dialog.findViewById(R.id.textMessage);
        textForMessage.setText("Are you sure you want to logout");
        Button btnYES = (Button) dialog.findViewById(R.id.btnYES);
        Button btnNO = (Button) dialog.findViewById(R.id.btnNO);
        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogoutClicked = false;
                dialog.dismiss();
            }
        });

        btnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.with(context).removeAll();
                CommonData.resetData();
                Intent in = new Intent();
                in.setClass(context, LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(in);
                context.finish();
                context.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
            }
        });
        dialog.show();
    }

    public static void logoutInBackground(final Activity context) {
        Prefs.with(context).removeAll();
        DbNew.getInstance(context).deleteAllEntries();
        CommonData.resetData();
        Intent in = new Intent();
        in.setClass(context, RegistrationActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(in);
        context.finish();
        context.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

    }

    public static void autoLogoutInBackground(final Activity context) {
        Prefs.with(context).removeAll();
        CommonData.resetData();
        Intent in = new Intent();
        in.setClass(context, RegistrationActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.putExtra("isback", true);
        context.startActivity(in);
        context.finish();
        context.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

    }

    public static void logout(final Activity context) {
        MessageDialog.showDialog(context, R.string.logout_message, new PromtListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onSubmit() {
                logoutInBackground(context);
            }
        });
    }

    public static int dpToPx(Activity context, double dps) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);

        return pixels;
    }

    public static int getWidthOfScreen(Activity context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        return width;
    }

    public static int getHeightOfScreen(Activity context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        return height;
    }

    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    }

    public static String encryption(String dataToEncrypt, Context c) {
        String encyptData = null;
        try {
            Log.e("acount number", "--" + dataToEncrypt);
            encyptData = CryptLib.encrypt(dataToEncrypt, c.getResources().getString(R.string.key_of_cryptography),
                    c.getResources().getString(R.string.iv_intialzation_vector));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return encyptData;
    }

    public static void slideUp(Activity context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_up);
        view.startAnimation(animation);
        view.findViewById(R.id.txt_accept).setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }

    public static void slideDown(Activity context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_down);
        view.startAnimation(animation);
        view.findViewById(R.id.txt_accept).setVisibility(View.GONE);
        view.setVisibility(View.GONE);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static boolean checkInternetConnection(Context mContext) {

        connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            haveConnectedActive = true;
        }

        if (wifi.isAvailable()) {
            haveConnectedWifi = true;
        }

        if (mobile.isAvailable()) {
            haveConnectedMobile = true;
        }

        if ((haveConnectedWifi == true || haveConnectedMobile == true) && haveConnectedActive == true) {
            return true;
        } else {
            return false;
        }

    }

    public static void removeFocus(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void performCrop(Activity activity, Uri u) {

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
        cropIntent.putExtra("outputX", 512);
        cropIntent.putExtra("outputY", 512);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, u);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        activity.startActivityForResult(cropIntent, CROP_PIC);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        try {
            File pic = new File(Environment.getExternalStorageDirectory() + "/Pictures");
            if (!pic.exists()) {
                pic.mkdir();
            }
        } catch (Exception ex) {

        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, String.valueOf(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Activity activity, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(activity, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static File createTemporaryFile(String part, String ext) throws Exception {

        File tempDir = new File(Environment.getExternalStorageDirectory() + File.separator + Kippin.res.getString(com.kippin.kippin.R.string.temp_folder) + File.separator);
        tempDir.mkdirs();

        tempDir = new File(tempDir.getPath() + File.separator + part + ext);
        tempDir.createNewFile();
        return tempDir;
    }

    public static Bitmap decodeSampledBitmapFromResource(String path,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        //  ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);

        return bm;
    }


    public static Bitmap decodeSampledBitmapFromResource(Bitmap map,
                                                         int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void finishActivity(Activity activity) {
        if (!CommonUtility.UserType.equals("0")) {
            CommonUtility.HomeButtonInsideView = true;
            activity.finish();
            activity.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
        } else {
            Intent in = new Intent();
            in.setClass(activity, Kippin._FinanceDashBoard);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(in);
            activity.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
        }

    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    private void showGPSDisabledAlertToUser(final Activity activity, String message) {
        try {
            final Dialog dialog = new Dialog(activity,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_network);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            Button btnNO = (Button) dialog.findViewById(R.id.btnNO);
            Button btnYES = (Button) dialog.findViewById(R.id.btnYES);

            btnYES.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent callGPSSettingIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivity(callGPSSettingIntent);*/
                }

            });
            btnNO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
