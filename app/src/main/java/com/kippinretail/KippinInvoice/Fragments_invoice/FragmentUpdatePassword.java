package com.kippinretail.KippinInvoice.Fragments_invoice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.ApplicationuUlity.Validation;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.Interface.OnDialogDismissListener;
import com.kippinretail.R;
import com.kippinretail.UserDashBoardActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

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

public class FragmentUpdatePassword extends Fragment implements View.OnClickListener {
    EditText etoldPassword, etNewPassword, etConfirmPassword;
    Button btUpdatePassword;

    public static FragmentUpdatePassword newInstance() {
        FragmentUpdatePassword fragment = new FragmentUpdatePassword();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update_password, container, false);
        initUI(v);
        return v;
    }

    private void initUI(View v) {
        // EditText
        etoldPassword = (EditText) v.findViewById(R.id.etoldPassword);
        etNewPassword = (EditText) v.findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) v.findViewById(R.id.etConfirmPassword);
        // Button
        btUpdatePassword = (Button) v.findViewById(R.id.btUpdatePassword);
        btUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etoldPassword.getText().toString().length() != 0 && etNewPassword.getText().length() != 0
                        && etConfirmPassword.getText().toString().length() != 0) {
                    if(etNewPassword.getText().length() >= 6) {
                        if (Validation.checkPassWordAndConfirmPassword(etNewPassword.getText().toString(), etConfirmPassword.getText().toString())) {
                            updateNewPassword();
                        } else {
                            CommonDialog.With(getActivity()).Show(getResources().getString(R.string.mismatch_password));
                        }
                    }else{
                        CommonDialog.With(getActivity()).Show(getResources().getString(R.string.password_length));
                    }
                } else {
                    CommonDialog.With(getActivity()).Show(getResources().getString(R.string.empty_fields));
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btUpdatePassword:

                break;

        }
    }

    private void updateNewPassword() {
        HashMap templatePosts = new HashMap();
        templatePosts.put("UserId", CommonData.getInvoiceUserData(getActivity()).getId());
        templatePosts.put("OldPassword", etoldPassword.getText().toString());
        templatePosts.put("NewPassword", etNewPassword.getText().toString());

        LoadingBox.showLoadingDialog(getActivity(), "");
        RestClient.getApiFinanceServiceForPojo().InvoiceUpdatePassword(getTypedInput(new Gson().toJson(templatePosts)), new Callback<JsonElement>() {
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
                       CommonDialog.showDialog2Button(getActivity(),ResponseMessage,
                        new OnDialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = fm.beginTransaction();
                                transaction.replace(R.id.container_dashboard, FragmentInvoiceDashBoard.newInstance());
                                transaction.commit();
                                ((UserDashBoardActivity)getActivity()).buttons("INVOICE DASHBOARD");

                               // CommonUtility.HomeClick(getActivity());
                            }
                        });
                    } else {
                     CommonDialog.With(getActivity()).Show(ResponseMessage);

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
                ErrorCodes.checkCode(getActivity(), error);
            }
        });


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
}
