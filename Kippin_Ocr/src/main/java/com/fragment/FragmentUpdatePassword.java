package com.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kippin.app.Kippin;
import com.kippin.dashboard.ActivityDashboard;
import com.kippin.kippin.R;
import com.kippin.loadingindicator.LoadingBox;
import com.kippin.retrofit.RestClient;
import com.kippin.storage.SharedPref;
import com.kippin.superviews.SuperFragment;
import com.kippin.utils.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * Created by kamaljeet.singh on 11/23/2016.
 */

public class FragmentUpdatePassword extends SuperFragment  {
    EditText etoldPassword, etNewPassword, etConfirmPassword;
    Button btUpdate_Password;

    public static FragmentUpdatePassword newInstance() {
        FragmentUpdatePassword fragment = new FragmentUpdatePassword();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_update_password, container, false);
        initUI(v);
        setUpListeners();
        return v;
    }

    private void initUI(View v) {
        // EditText
        etoldPassword = (EditText) v.findViewById(R.id.etoldPassword);
        etNewPassword = (EditText) v.findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) v.findViewById(R.id.etConfirmPassword);
        // Button
        btUpdate_Password = (Button) v.findViewById(R.id.bt_Update_Password);

    }



    /**
     * @param message
     */
    public void Show(String message) {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_custom_msg);
            TextView textMessage = (TextView) dialog.findViewById(R.id.text_msg);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText(message);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }

            });

            dialog.show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage() + "", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // Check password and confirm password
    public static boolean checkPassWordAndConfirmPassword(String password, String confirmPassword) {
        boolean pstatus = false;
        if (confirmPassword != null && password != null) {
            if (password.equals(confirmPassword)) {
                pstatus = true;
            }
        }
        return pstatus;
    }

    private void updateNewPassword() {
        HashMap templatePosts = new HashMap();
        templatePosts.put("FinanceUserID", Singleton.getUser().getId());
        templatePosts.put("OldPassword", etoldPassword.getText().toString());
        templatePosts.put("NewPassword", etNewPassword.getText().toString());

        LoadingBox.showLoadingDialog(getActivity(), "");
        RestClient.getApiFinanceServiceForPojo().UpdatePassword(getTypedInput(new Gson().toJson(templatePosts)), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement element, Response response) {
                 LoadingBox.dismissLoadingDialog();
                Log.e("Tag", "Request data " + new Gson().toJson(element));

                JsonObject jsonObj = element.getAsJsonObject();
                String strObj = jsonObj.toString();
                try {
                    JSONObject OBJ = new JSONObject(strObj);
                    String ResponseMessage = OBJ.getString("ResponseMessage");
                    String ResponseCode = OBJ.getString("ResponseCode");
                    if (ResponseCode.equals("1")) {
                        SharedPref.put(SharedPref.PASSWORD,etNewPassword.getText().toString());

                        showDialog2Button(getActivity(), ResponseMessage);
                    } else {
                        Show(ResponseMessage);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("user updated exception response", " = " + e.toString());
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure ", " = " + error.getMessage());
                 LoadingBox.dismissLoadingDialog();
                //   ErrorCodes.checkCode(getActivity(), error);
            }
        });


    }

    public  void showDialog2Button(Context activity, String message) {
        try {

            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_custom_msg);


            TextView textForMessage = (TextView) dialog
                    .findViewById(R.id.text_msg);
            textForMessage.setMovementMethod(new ScrollingMovementMethod());
            textForMessage.setText(message);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Intent intent = new Intent(getActivity(), ActivityDashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }

            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TypedInput getTypedInput(String data) {
        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return in;
    }

    @Override
    public void setUpListeners() {
        btUpdate_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etoldPassword.getText().toString().length() != 0 && etNewPassword.getText().length() != 0
                        && etConfirmPassword.getText().toString().length() != 0) {
                    if (etNewPassword.getText().length() >= 6) {
                        if (checkPassWordAndConfirmPassword(etNewPassword.getText().toString(), etConfirmPassword.getText().toString())) {
                            updateNewPassword();
                        } else {
                            Show(getResources().getString(R.string.mismatch_password));
                        }
                    } else {
                        Show(getResources().getString(R.string.password_length));
                    }
                } else {
                    Show(getResources().getString(R.string.empty_fields));
                }
            }
        });
    }
}
