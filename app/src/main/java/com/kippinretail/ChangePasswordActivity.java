package com.kippinretail;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.ServerResponseModal.ServerResponse;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChangePasswordActivity extends SuperActivity implements View.OnClickListener{

    EditText oldPasswordEditText,newPasswordEditText,conformPasswordEditText;
    String oldPassword,currentUserId;
    Button updateButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Initlization();
    }

    private void Initlization(){
        generateActionBar(R.string.title_merchant_updatePassword,true,true,false);
        generateRightText("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        updateButton = (Button)findViewById(R.id.updateButton);
        oldPasswordEditText = (EditText)findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = (EditText)findViewById(R.id.newPasswordEditText);
        conformPasswordEditText = (EditText)findViewById(R.id.conformPasswordEditText);
        oldPassword = CommonData.getUserData(ChangePasswordActivity.this).getPassword();
        currentUserId = String.valueOf(CommonData.getUserData(ChangePasswordActivity.this).getId());
        updateButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()){
            case R.id.updateButton:
                updatePassword();
                break;
        }
    }

    private void updatePassword(){
        if(oldPasswordEditText.getText().toString().length()==0 || newPasswordEditText.getText().toString().length()==0 || conformPasswordEditText.getText().toString().length()==0){
            CommonDialog.With(ChangePasswordActivity.this).Show("Please fill the empty fields.");
        }
        else
        {
            if(oldPassword.equals(oldPasswordEditText.getText().toString())){
                if(newPasswordEditText.getText().toString().length()<6 || conformPasswordEditText.getText().toString().length()<6){
                    MessageDialog.showDialog(ChangePasswordActivity.this , "Password should be of minimum 6 characters.",false);

                }
                else{
                    if(newPasswordEditText.getText().toString().equals(conformPasswordEditText.getText().toString())){
                        changePassword();
                    }
                    else{
                        MessageDialog.showDialog(ChangePasswordActivity.this , "Password mismatch here.Please confirm same password.",false);

                    }
                }
            }
            else
            {
                MessageDialog.showDialog(ChangePasswordActivity.this , "Incorrect old Password.",false);

            }
        }
    }

    private void changePassword(){
        LoadingBox.showLoadingDialog(ChangePasswordActivity.this, "Updating...");
        RestClient.getApiServiceForPojo().Changepassword(currentUserId, oldPasswordEditText.getText().toString(), newPasswordEditText.getText().toString()
                , new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("Output  ",jsonElement.toString());
                Log.e("URL  ", response.getUrl());
                JsonObject jsonObj = jsonElement.getAsJsonObject();
                String strObj = jsonObj.toString();
                try {
                    JSONObject OBJ = new JSONObject(strObj);
                    String ResponseMessage = OBJ.getString("ResponseMessage");
                    String ResponseCode = OBJ.getString("ResponseCode");
                    if(ResponseCode.equals("1")){
                        MessageDialog.showDialog(ChangePasswordActivity.this,"Password has been successfully updated.Kindly Login again",false,true);

                    }else{
                        MessageDialog.showDialog(ChangePasswordActivity.this,ResponseMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Forgot password", " = " + e.toString());
                }
                LoadingBox.dismissLoadingDialog();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("URL  ",error.getUrl());
                LoadingBox.dismissLoadingDialog();;
                MessageDialog.showFailureDialog(ChangePasswordActivity.this);
            }
        });
    }

    private void resetForm(){
        oldPasswordEditText.setText("");
        newPasswordEditText.setText("");
        conformPasswordEditText.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }
}
