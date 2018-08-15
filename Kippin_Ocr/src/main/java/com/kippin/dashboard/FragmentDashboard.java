package com.kippin.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kippin.activities.ActivityDashboardUpload;
import com.kippin.app.Kippin;
import com.kippin.kippin.R;
import com.kippin.manualAutomaticStatements.ManualAutomaticStatement;
import com.kippin.reconcile.ActivityReconcile;
import com.kippin.superviews.SuperFragment;
import com.kippin.utils.ImagePickUtility.CameraPicListener;
import com.kippin.utils.ImagePickUtility.CapturePicView;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModelImagePost;
import com.kippin.webclient.model.TemplateData;


public class FragmentDashboard extends SuperFragment implements View.OnClickListener {
    private final int REQUEST_CODE_LOGOUT = -1;
    //  @Bind(R.id.ivReconcile)
    ImageView ivReconcile;
    // @Bind(R.id.ivLogout)
    ImageView ivLogout;
    //@Bind(R.id.ivConnection)
    ImageView ivConnection;
    // @Bind(R.id.ivCamera)
    ImageView ivCamera;
    CapturePicView capturePicView = null;
    DialogBox dialogBox;
    String[] array = {};
    View root;
    LayoutInflater inflater;
    boolean isInitialised = false;
    boolean isStatusDialogOpened = false;


//    Kippin.invoice;

    //    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//    }
    Handler handler = new Handler();
    private ImageView ivInvoice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Utility.printLog("time out", System.currentTimeMillis() + "");
        this.inflater = inflater;
        root = inflater.inflate(R.layout.activity_dashboard, null, false);
        ivReconcile = (ImageView) root.findViewById(R.id.ivReconcile);
        ivLogout = (ImageView) root.findViewById(R.id.ivLogout);
        ivConnection = (ImageView) root.findViewById(R.id.ivConnection);
        ivCamera = (ImageView) root.findViewById(R.id.ivCamera);
        ivInvoice = (ImageView) root.findViewById(R.id.ivInvoice);
        initialiseUI();
        setUpListeners();

        generateActionBarLogout();
        ivInvoice.setVisibility(View.VISIBLE);
        ivLogout.setVisibility(View.GONE);
        Utility.FinanceLogin = true;
        setHeightWidth();

        return root;

    }

    public void setHeightWidth() {
        int width, height;
        float myX = getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(getActivity(), 85);
        float x = (myX / 2f);
        float myHeightPX = getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(getActivity(), 180);
        float y = (myHeightPX / 3f - Utility.dpToPx(getActivity(), 20));
        width = Math.round(x);
        height = Math.round(y);

        ivConnection.getLayoutParams().width = width;
        ivConnection.getLayoutParams().height = height;


        ivInvoice.getLayoutParams().width = width;
        ivInvoice.getLayoutParams().height = height;

        ivLogout.getLayoutParams().width = width;
        ivLogout.getLayoutParams().height = height;

        ivReconcile.getLayoutParams().width = width;
        ivReconcile.getLayoutParams().height = height;

        ivCamera.getLayoutParams().width = width;
        ivCamera.getLayoutParams().height = height;

        ivCamera.requestLayout();
        ivReconcile.requestLayout();
        ivLogout.requestLayout();
        ivInvoice.requestLayout();
        ivConnection.requestLayout();


    }

//    private void checkStatus() {
//
//        if (!isInitialised && !isStatusDialogOpened && Singleton.getUser().getId()!=null ){
//            WSUtils.hitServiceGet(getActivity(), Url.getStatusUrl(), new ArrayListPost(), TemplateData.class, new WSInterface() {
//                @Override
//                public void onResult(int requestCode, TemplateData data) {
//
//                    isStatusDialogOpened = true;
//
//                    if (data == null || data.data == null || data.data.length() == 0) {
//                        isStatusDialogOpened = false;
//                        return;
//                    }
//
//                    if (data.data.equalsIgnoreCase(UserStatus.Error.toString())) {
//                        new DialogBox(getActivity(), "An Error occured. Please login after sometime.", new DialogBoxListener() {
//                            @Override
//                            public void onDialogOkPressed() {
//                                isStatusDialogOpened = false;
//
//                                Utility.logout((SuperActivity)getActivity(), false);
//
//                            }
//                        });
//
//                    } else if (Utility.isUserAllowed(data.data)) {
//                        initialiseUI();
//                        setUpListeners();
//                        isStatusDialogOpened = false;
//                        isInitialised = true;
//                    } else {
//
////                        getIntent().getExtras().getBoolean("REDIRECT_TO_LOGIN");
//                        new DialogBox(getActivity(), "Please pay", new DialogBoxListener() {
//                            @Override
//                            public void onDialogOkPressed() {
//                                Bundle bundle = new Bundle();
//                                bundle.putBoolean("REDIRECT_TO_LOGIN",false);
//                                Utility.startActivity(getActivity(), PaymentDetailsClass.class, bundle,true);
//                                isStatusDialogOpened = false;
//                            }
//                        });
//                    }
//                }
//            });
//        }
//
//    }

    @Override
    public void onResume() {
        super.onResume();
//        checkStatus();
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI(root);
        capturePicView = new CapturePicView(getActivity());
    }

    @Override
    public void setUpListeners() {
        setUpListeners(this, ivReconcile, ivLogout, ivCamera, ivConnection, ivInvoice);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ivReconcile) {
            Utility.startActivity(getActivity(), ActivityReconcile.class, true);

        } else if (id == R.id.ivCamera) {
            Utility.startActivity(getActivity(), ActivityDashboardUpload.class, true);
            // capturePicView.launchCamera();

        } else if (id == R.id.ivLogout) {
            dialogBox = new DialogBox(getResources().getString(R.string.are_sure_uwant_logout), array, getActivity());

        } else if (id == R.id.ivConnection) {
            Utility.startActivity(getActivity(), ManualAutomaticStatement.class, true);
        } else if (id == R.id.ivInvoice) {
            // if 0 and true then dashboard

            Intent in = new Intent();
            // IF NULL THEN GO TO REGISTRATION
            // IF 0 OR 1 THEN USER ALREADY REGISTERED AS INVOICE THEN GO TO DASHBOARD
            // if null then
            Log.e("is--Only-----Invoice:", "" + Singleton.getUser().is_IsOnlyInvoice());
            if (Singleton.getUser().is_IsOnlyInvoice()) {

                if (!Singleton.getUser().is_IsOnlyInvoice()) {
                    in.setClass(getActivity(), Kippin.invoiceRegistrationClass);
                    in.putExtra("RegistrationType", "0");
                    in.putExtra("InvoiceUserType", "true");
                    getActivity().startActivity(in);
                    getActivity().overridePendingTransition(R.anim.push_in_to_left,
                            R.anim.push_in_to_right);
                } else {
                    // call Registration
                    in.setClass(getActivity(), Kippin.invoiceDashBoardClass);
                    in.putExtra("RegistrationType", "0");
                    in.putExtra("InvoiceUserType", "true");
                    getActivity().startActivity(in);
                    getActivity().overridePendingTransition(R.anim.push_in_to_left,
                            R.anim.push_in_to_right);
                }


            } else {
                in.setClass(getActivity(), Kippin.invoiceRegistrationClass);
                in.putExtra("RegistrationType", "0");
                in.putExtra("InvoiceUserType", "true");
                getActivity().startActivity(in);
                getActivity().overridePendingTransition(R.anim.push_in_to_left,
                        R.anim.push_in_to_right);
            }


        }
        /*switch (v.getId()) {
            case R.id.ivReconcile:
                Utility.startActivity(getActivity(), ActivityReconcile.class, true);
                break;

            case R.id.ivCamera:
                capturePicView.launchCamera();
                break;

            case R.id.ivLogout:
                dialogBox = new DialogBox(getResources().getString(R.string.are_sure_uwant_logout), array, getActivity());
                // new ActivityLogoutDialog(ActivityDashboard.this ).show();
                break;

            case R.id.ivConnection:
                Utility.startActivity(getActivity(), ManualAutomaticStatement.class, true);
                break;

        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_LOGOUT:
                Utility.logout(getActivity(), false);
                break;

            case CapturePicView.REQUEST_CODE_CAMERA:
            case CapturePicView.REQUEST_CODE_GALLERY:

                capturePicView.onActivityResult(requestCode, resultCode, data, new CameraPicListener() {
                    @Override
                    public void onImageSelected(Bitmap bm) {

                        if (bm == null) {

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogBox(getActivity(), Kippin.kippin.getResources().getString(R.string.image_size_too_low), (DialogBoxListener) null);
                                }
                            });
                        } else {
                            try {
                                String image = Utility.convertBitmap2Base64(bm);
                                final ArrayListPost templatePosts = new ArrayListPost();
                                templatePosts.add("Base64Image", image);
                                templatePosts.add("ImageName", "test.jpg");
                                templatePosts.add("UserId", Singleton.getUser().getId());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        WSUtils.hitService(getActivity(), Url.URL_UPLOAD, false, templatePosts, WSMethods.POST, ModelImagePost.class, new WSInterface() {
                                            @Override
                                            public void onResult(int requestCode, TemplateData data) {
                                                new DialogBox(getActivity(), ((ModelImagePost) data.getData(ModelImagePost.class)).getResponseMessage(), (DialogBoxListener) null);

                                                Singleton.bm = null;

                                            }
                                        });
                                    }
                                });
                            } catch (
                                    Exception e
                                    )

                            {
                                e.printStackTrace();
                            }
                        }

                    }
                });


                break;
        }

    }
}
