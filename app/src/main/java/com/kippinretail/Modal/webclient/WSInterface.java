package com.kippinretail.Modal.webclient;


import android.widget.EditText;

import com.google.gson.Gson;
import com.kippinretail.Modal.webclient.model.TemplateData;

import java.util.ArrayList;

public interface WSInterface {
	void onResult(int requestCode, TemplateData data);

   }
