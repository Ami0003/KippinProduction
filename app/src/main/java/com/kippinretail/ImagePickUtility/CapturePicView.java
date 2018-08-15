package com.kippinretail.ImagePickUtility;//package com.kippinretail.ImagePickUtility;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.R;
import com.kippinretail.app.Retail;

import java.io.File;

/**
 * Created by gaganpreet.singh on 1/22/2016.
 */
public class CapturePicView {

    private Activity activity;
    public static final int REQUEST_CODE_CAMERA = 10001;
    public static final int REQUEST_CODE_GALLERY = 10002;
    static private Uri mImageUri;

    private OnCloudClickListener onCloudClickListener;

    public void addOnCloudClickListener(OnCloudClickListener onCloudClickListener){
        this.onCloudClickListener=onCloudClickListener;
    }

    public CapturePicView(Activity context) {
        this.activity = context;
        init(context);
        this.context = context;
    }

    Activity context;

    private void init(final Activity context) {
    }


//    public void show() {
//
//        MessageDialog.showDialog(activity, new  CameraListener() {
//            @Override
//            public void onGalleryClick() {
//                launchGallery();
//            }
//
//            @Override
//            public void onCameraClick() {
//                launchCamera();
//            }
//
//            @Override
//            public void onCloudClick() {
//                if (onCloudClickListener != null)
//                    onCloudClickListener.onClick();
//            }
//
//            @Override
//            public void onDismiss() {
//            }
//        });
//
//    }


    private File createTemporaryFile(String part, String ext) throws Exception {

        File tempDir = new File(Environment.getExternalStorageDirectory()+File.separator+ Retail.res.getString(R.string.temp_folder)+File.separator);
        tempDir.mkdirs();

        tempDir = new File(tempDir.getPath()+File.separator+part+ext);
        tempDir.createNewFile();

//        tempDir = new File(tempDir.getAbsolutePath() + "/temp/");
//        if (!tempDir.exists()) {
//            tempDir.mkdir();
//        }
//        return File.createTempFile(part, ext, tempDir);
        return tempDir;
    }

    public void launchGallery() {

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE_GALLERY);
    }

    public void launchCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = null;
        try {
            // place where to store camera taken picture
            photo = createTemporaryFile("picture", ".jpg");
            photo.delete();

            mImageUri = Uri.fromFile(photo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            //start camera intent
            activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("kippin", "Can't create file to take picture!");
            Toast.makeText(activity, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialog (){
        Utility.showDialog(context);
    }


    public void onActivityResult(int requestCode, int resultCode, final Intent data, final CameraPicListener listener) {

        String selectedImagePath = null;

        if (resultCode == Activity.RESULT_OK) {

            showDialog();

            if (requestCode == REQUEST_CODE_CAMERA){
                activity.getContentResolver().notifyChange(CapturePicView.this.mImageUri, null);

                final Uri selectedImageUri = mImageUri;
                selectedImagePath = new File(mImageUri.getPath()).getPath();
            }  else if (requestCode == REQUEST_CODE_GALLERY) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(activity, selectedImageUri, projection, null, null,
                        null);
                final Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                selectedImagePath = cursor.getString(column_index);
            }

            final String selectedImagePath_ = selectedImagePath;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bm = null;
                    bm = Utility.resize(new File(selectedImagePath_));

                    returnBitmap(bm,listener);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Utility.dismissDialog();
                            }
                            catch ( Exception e){
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }).start();

        }
    }

    private void returnBitmap(Bitmap bitmap , CameraPicListener cameraPicListener){
        if(bitmap ==null){
            cameraPicListener.onError();
        }else cameraPicListener.onImageSelected(bitmap);
    }

    Handler handler = new Handler();

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

    String errorMessage = "";

//    public void uploadImageForStatement(final Activity activity, final Bitmap bm, final ModelBankStatement modelBankStatement, final TemplateStatementImageUploader templateStatementImageUploader/*final OnResultListener onResultListener*/){
//        {
//            if(bm==null){
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        new DialogBox(activity, Kippin.kippin.getResources().getString(R.string.image_size_too_low),(DialogBoxListener)null);
//                    }
//                });
//            }
//            else
//            {
//
//                try{
//
//                   final OnResultListener onResultListener = templateStatementImageUploader.onResultListener;
//
//                    String image = Utility.convertBitmap2Base64(bm);
//
//                    final WSTemplate wsTemplate = new WSTemplate();
//
//                    ArrayListPost templatePosts = new ArrayListPost();
//                    templatePosts.add("Image",image);
//                    templatePosts.add("ImageName",System.currentTimeMillis()+".png");
//                    templatePosts.add("BankId",modelBankStatement.getBankId() );
//                    templatePosts.add("UserId", Singleton.user.getId());
//                    templatePosts.add("StatementId",modelBankStatement.getId()  );
//                    if(templateStatementImageUploader.isCloudImage){
//                        templatePosts.add("CloudImageName", templateStatementImageUploader.CloudImageName);
//                        templatePosts.add("IsCloud",templateStatementImageUploader.isCloudImage+""  );
//                    }else{
//                        templatePosts.add("CloudImageName", "");
//                        templatePosts.add("IsCloud","");
//                    }
//                        templatePosts.add("Month",templateStatementImageUploader.month+""  );
//                        templatePosts.add("Year",templateStatementImageUploader.year+""  );
//
//
//                    wsTemplate.templatePosts = templatePosts;
//                    wsTemplate.wsInterface = new WSInterface() {
//                        @Override
//                        public void onResult(int requestCode, final TemplateData data) {
//
//                            String responseCode = "1";
//
//                            if(!data.getData(ModelImagePost.class).getResponseCode().equalsIgnoreCase("1")){
//
//                                new DialogBox(activity, data.getData(ModelImagePost.class).getResponseMessage(), new DialogBoxListener() {
//                                    @Override
//                                    public void onDialogOkPressed() {
//                                        if(onResultListener!=null)
//                                        onResultListener. openOCR(data,bm);
//                                    }
//                                });
//
//                            }else{
//                                if(onResultListener!=null)
//                                onResultListener.openOCR(data,bm);
//                            }
//
//                        }
//                    };
//
//                    wsTemplate.url = Url.URL_BANK_STATEMENT_IMAGE;
//                    wsTemplate.aClass = ModelImagePost.class;
//                    wsTemplate.context = activity;
//                    wsTemplate.methods = WSMethods.POST;
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            new WSHandler(wsTemplate).execute();
//                        }
//                    });
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                catch (Error e){
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }
}
